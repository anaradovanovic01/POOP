package stocks;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import exceptions.*;
import stocks.MainWindow.DoneDialog;

public class Graph extends Canvas {
	private ShowActionPanel owner;
	private Stock stock;
	private List<Candle> currentCandles = new ArrayList<>();
	private double max, min;
	private int candleWidth, t1, t2, n;
	private Candle first, last;
	private boolean ma = false, ema = false;
	
	public Graph(ShowActionPanel owner) {
		this.owner = owner;
		setPreferredSize(new Dimension(1400, 600));
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				char key = Character.toUpperCase(e.getKeyChar());
				t1 = Integer.parseInt(currentCandles.get(0).getTimestamp());
				t2 = Integer.parseInt(currentCandles.get(currentCandles.size()-1).getTimestamp());
				if(code == KeyEvent.VK_LEFT) {
					while(stock.getCandles().indexOf(first) == 0 && t1 > 347155200) {
						t1 -= 2629743;
						getMoreCandles();
					}
					if(t1 <= 347155200) {
						owner.getOwner().new DoneDialog(owner.getOwner(), "Nema ranijih podataka");
					}
					else if(stock.getCandles().indexOf(first) != 0) {
						currentCandles.remove(last);
						currentCandles.add(0, stock.getCandles().get(stock.getCandles().indexOf(first)-1));
					}
				}
				if(code == KeyEvent.VK_RIGHT) {
					while(stock.getCandles().indexOf(last) == stock.getCandles().size()-1 && t2 < 1624233600) {
						t2 += 2629743;
						getMoreCandles();
					}
					if(t2 >= 1624233600) {
						owner.getOwner().new DoneDialog(owner.getOwner(), "Nema novijih podataka");
					}
					else if(stock.getCandles().indexOf(last) != stock.getCandles().size()-1) {
						currentCandles.add(stock.getCandles().get(stock.getCandles().indexOf(last)+1));
						currentCandles.remove(first);
					}
				}
				if(key == KeyEvent.VK_MINUS) {
					if(currentCandles.size() < 327) {
						while ((stock.getCandles().indexOf(last) == stock.getCandles().size()-1 || stock.getCandles().indexOf(first) == 0) && t1 > 347155200 && t2 < 1624233600) {
							t1 -= 2629743 ; t2 += 2629743;
							getMoreCandles();
						}
						if(t2 >= 1624233600 || t1 <= 347155200) owner.getOwner().new DoneDialog(owner.getOwner(), "Nema daljih podataka");
						else if(stock.getCandles().indexOf(first) != 0 && stock.getCandles().indexOf(last) != stock.getCandles().size()-1) {
							currentCandles.add(stock.getCandles().get(stock.getCandles().indexOf(last)+1));
							currentCandles.add(0, stock.getCandles().get(stock.getCandles().indexOf(first)-1));
						}
					}
					else owner.getOwner().new DoneDialog(owner.getOwner(), "Maksimalno ste smanjili");
				}
				if(key == '+') {
					if(currentCandles.size() > 17) {
						currentCandles.remove(last);
						currentCandles.remove(first);
					}
					else owner.getOwner().new DoneDialog(owner.getOwner(), "Maksimalno ste uvecali");
				}
				first = currentCandles.get(0);
				last = currentCandles.get(currentCandles.size() - 1);
				repaint();
			}
			
		});
		
	}
	
	public void getMoreCandles() {
		try {
			stock = (new JSONParser((new Crawler()).makeJSON(stock.getName(), ((Integer)t1).toString(), ((Integer)t2).toString()))).parse();
		} catch (CouldntFindInfoAboutStock e) {  owner.getOwner().new DoneDialog(owner.getOwner(), "Uneti su nevalidni podaci"); } 
	}

	public void setStock(Stock stock) {
		this.stock = stock;
		if(!currentCandles.isEmpty()) currentCandles.clear(); 
		for(int i = stock.getCandles().size() - 1, j = 0; i >= 0 && j < 50; i--, j++) {
			currentCandles.add(0, stock.getCandles().get(i));
		}
		first = currentCandles.get(0);
		last = currentCandles.get(currentCandles.size() - 1);
	}

	public void removeStock() { stock = null; }
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(stock != null) {
			min = Math.round(getMin() * 10) / 10.0;
			max = Math.round(getMax() * 10) / 10.0;
			min -= Math.round((max - min) / 20 * 100) / 100.0;
			max += Math.round((max - min) / 20 * 100) / 100.0;
			
			g.setColor(Color.BLACK);
			g.setFont(new Font(Font.MONOSPACED, 0, 14));
			int y = 0;
			double step = Math.round((max - min) / 20 * 100) / 100.0;
			for(double i = max; i > min; i=i-step) {				
				if(i != max && y != 600) g.drawString(String.format("%-7.2f", i), getWidth() - g.getFontMetrics().stringWidth(String.format("%-7.2f", i)), y + g.getFontMetrics().getHeight() / 4);
				y = y + 30;
			}
			
			g.setColor(Color.RED);
			int x = 0;
			if(currentCandles.size() < 15) candleWidth = (getWidth() - g.getFontMetrics().stringWidth(String.format("%-7.2f", min))) / 15;
			else candleWidth = (getWidth() - g.getFontMetrics().stringWidth(String.format("%-7.2f", min))) / currentCandles.size();
			for(Candle c: currentCandles) {
				if(c.getClose() > c.getOpen()) {
					g.setColor(Color.GREEN);
					g.fillRect(x, valueToCoordinate(c.getClose()), candleWidth, valueToCoordinate(c.getOpen()) - valueToCoordinate(c.getClose()));
				}
				else {
					g.setColor(Color.RED);
					g.fillRect(x, valueToCoordinate(c.getOpen()), candleWidth, valueToCoordinate(c.getClose()) - valueToCoordinate(c.getOpen()));
				}
				g.drawLine(x + candleWidth/2, valueToCoordinate(c.getLow()), x + candleWidth/2, valueToCoordinate(c.getHigh()));
				x += candleWidth;
				
			}
			
			if(ma) {
				x = candleWidth/2;
				stock.calculateMA(n);
				for(int i = stock.getCandles().indexOf(first); i < stock.getCandles().indexOf(last); i++) {
					g.setColor(Color.decode("#5662f6"));
					g.drawLine(x, valueToCoordinate(stock.getMA().get(i)), x+candleWidth, valueToCoordinate(stock.getMA().get(i+1)));
					x += candleWidth;
				}
			}
			
			if(ema) {
				x = candleWidth/2;
				stock.calculateEMA(n);
				for(int i = stock.getCandles().indexOf(first); i < stock.getCandles().indexOf(last); i++) {
					g.setColor(Color.decode("#ed9900"));
					g.drawLine(x, valueToCoordinate(stock.getEMA().get(i)), x+candleWidth, valueToCoordinate(stock.getEMA().get(i+1)));
					x += candleWidth;
				}
			}
		}
	}
	
	public int valueToCoordinate(double value) { return getHeight() - (int)((value-min)*getHeight()/(max-min)); }
	
	public double coordinateToValue(int c) { return (max-min)*(getHeight()-c)/getHeight() + min; }
	
	public int getCandleIndex(int width) {
		return width / candleWidth;
	}

	public Candle getCandleByIndex(int i) {
		if(i > currentCandles.size() - 1) return currentCandles.get(currentCandles.size()-1);
		return currentCandles.get(i);
	}

	public Stock getStock() {
		return stock;
	}	
	
	public void setMA(boolean ma) {
		this.ma = ma;
	}

	public void setEMA(boolean ema) {
		this.ema = ema;
	}

	public boolean isMa() {
		return ma;
	}

	public boolean isEma() {
		return ema;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getMin() {
		double min = currentCandles.get(1).getLow();
		for(Candle c: currentCandles) {
			if(c.getLow() < min) min = c.getLow();
		}
		return min;
	}

	public double getMax() {
		double max = currentCandles.get(1).getHigh();
		for(Candle c: currentCandles) {
			if(c.getHigh() > max) max = c.getHigh();
		}
		return max;
	}

	public double getMAForCandle(Candle candle) {
		int index = stock.getCandles().indexOf(candle);
		return stock.getMA().get(index);
	}

	public double getEMAForCandle(Candle candle) {
		int index = stock.getCandles().indexOf(candle);
		return stock.getEMA().get(index);
	}
}
