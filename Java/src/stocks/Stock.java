package stocks;

import java.util.ArrayList;
import java.util.List;

public class Stock {

	private String name;
	private List<Candle> candles = new ArrayList<>();
	private List<Double> ma = new ArrayList<>();
	private List<Double> ema = new ArrayList<>();
	
	public Stock(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Candle> getCandles() {
		return candles;
	}

	public List<Double> getMA() {
		return ma;
	}

	public void clearMA() {
		ma.clear();
	}

	public List<Double> getEMA() {
		return ema;
	}

	public void clearEMA() {
		ema.clear();;
	}

	public void addCandle(Candle candle) {
		candles.add(candle);		
	}
	
	public void calculateMA(int n) {
		ma.clear();
		int num, j; double v, sum;
		for (int i = 0; i < candles.size(); i++) {
			sum = 0;
			j = i;
			for(num = n + 1; num > 0; num--) {
				sum += candles.get(j).getClose();
				if (j == 0) {
					num--; 
					break;
				}
				else j--;
			}
			v = sum / (n + 1 - num);
			ma.add(v);
		}
	}
	
	public void calculateEMA(int n) {
		ema.clear();
		List<Candle> c = new ArrayList<>(); 
		double v; int num, j; 
		for (int i = 0; i < candles.size(); i++) {
			j = i;
			for (num = n + 1; num > 0; num--) {
				c.add(candles.get(j));
				if (j == 0) break;
				else j--;
			}
			v = ema(c.size(), c);
			ema.add(v);
			c.clear();
		}
	}
	
	public double ema(int n, List<Candle> c) {
		double s = 0;
		if (n == 1) return c.get(0).getClose();		
		s += c.get(n-1).getClose() * (2. / (n + 1)) + ema((n - 1), c) * (1 - (2. / (n + 1)));
		return s;
	}

}
