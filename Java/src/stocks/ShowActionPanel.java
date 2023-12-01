package stocks;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import exceptions.*;
import stocks.MainWindow.DoneDialog;

public class ShowActionPanel extends Panel {
	private MainWindow owner;
	private Stock stock;
	private TextField timestamp1T, timestamp2T, nameT, nT;
	private Label timestamp1L, timestamp2L, nameL, values, maL, emaL, nL;
	private Button show, back;
	private Checkbox emaC, maC;
	private Panel south, center, north;
	private Graph graph;
	
	public ShowActionPanel(MainWindow owner) {
		this.owner = owner;
		setPreferredSize(new Dimension(1400, 650));
		setLayout(new BorderLayout());
		populatePanel();		
	}

	private void populatePanel() {
		south = new Panel();
		
		nameL = new Label("Ime akcije");
		nameT = new TextField("aapl");
		south.add(nameL);
		south.add(nameT);
		
		timestamp1L = new Label("Pocetni trenutak");
		timestamp1T = new TextField("1580000000");
		south.add(timestamp1L);
		south.add(timestamp1T);
		
		timestamp2L = new Label("Krajnji trenutak");
		timestamp2T = new TextField("1600000000");
		south.add(timestamp2L);
		south.add(timestamp2T);
		
		nameT.addTextListener(tl -> {
			if(nameT.getText().equals("") || timestamp1T.getText().equals("") || timestamp1T.getText().equals("")) show.setEnabled(false);
			else show.setEnabled(true);
		});
		
		timestamp1T.addTextListener(tl -> {
			if(nameT.getText().equals("") || timestamp1T.getText().equals("") || timestamp1T.getText().equals("")) show.setEnabled(false);
			else show.setEnabled(true);
		});
		
		timestamp2T.addTextListener(tl -> {
			if(nameT.getText().equals("") || timestamp1T.getText().equals("") || timestamp1T.getText().equals("")) show.setEnabled(false);
			else show.setEnabled(true);
		});
		
		show = new Button("Prikazi podatke");
		//show.setEnabled(false);
		show.addActionListener(al -> {
			try {
				stock = (new JSONParser((new Crawler()).makeJSON(nameT.getText(), timestamp1T.getText(), timestamp2T.getText()))).parse();
				graph.setStock(stock);
				graph.repaint();
				graph.requestFocus();
			} catch (CouldntFindInfoAboutStock e) { owner.new DoneDialog(owner, "Uneti su nevalidni podaci"); } 
		});
		
		south.add(show);
		
		maC = new Checkbox("MA");
		emaC = new Checkbox("EMA");

		maC.addItemListener(ie -> {
			if(ie.getStateChange() == ItemEvent.SELECTED) {
				if(!nT.getText().equals("")) {
					graph.setN(Integer.parseInt(nT.getText()));
					graph.setMA(true);
					graph.repaint();
				}
				else {
					maC.setState(false);
					owner.new DoneDialog(owner, "Prvo upisite n");
				}
			}
			else {
				graph.setMA(false);
				graph.repaint();
			}
		});
		
		emaC.addItemListener(ie -> {
			if(ie.getStateChange() == ItemEvent.SELECTED) {
				if(!nT.getText().equals("")) {
					graph.setN(Integer.parseInt(nT.getText()));
					graph.setEMA(true);
					graph.repaint();
				}
				else {
					emaC.setState(false);
					owner.new DoneDialog(owner, "Prvo upisite n");
				}
			}
			else {
				graph.setEMA(false);
				graph.repaint();
			}			
		});
		
		south.add(maC);
		south.add(emaC);
		
		nL = new Label("n");
		nT = new TextField(3);
		
		nT.addTextListener(tl -> {
			if(!nT.getText().equals("")) {
				try {
					graph.setN(Integer.parseInt(nT.getText()));
					graph.repaint();
				} catch (NumberFormatException e) { owner.new DoneDialog(owner, "Upisite celobrojnu vrednost u n"); }
			}
		});
		
		south.add(nL);
		south.add(nT);
		
		back = new Button("Nazad");
		back.addActionListener(al -> {
			graph.removeStock();
			nameT.setText("");
			timestamp1T.setText("");
			timestamp2T.setText("");
			nameT.setText("");
			nT.setText("");
			maC.setState(false);
			emaC.setState(false);
			show.setEnabled(false);
			graph.repaint();
			values.setText("");
			owner.goToMenu();
		});
		south.add(back);
		
		this.add(south, BorderLayout.SOUTH);
		
		center = new Panel();
		
		graph = new Graph(this);
		graph.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(graph.getStock() != null) {
					try {
						int i = graph.getCandleIndex(e.getX());
						Candle candle = graph.getCandleByIndex(i);
						if(candle.getClose() > candle.getOpen()) values.setForeground(Color.GREEN);
						else values.setForeground(Color.RED);
						values.setText(String.format("%s%.2f %s%.2f %s%.2f %s%.2f", "Open:", candle.getOpen(), "Close:", candle.getClose(), "Low:", candle.getLow(), "High:", candle.getHigh()));
						if(graph.isMa()) maL.setText(String.format("MA:%.2f", graph.getMAForCandle(candle)));
						else maL.setText("");
						if(graph.isEma()) emaL.setText(String.format("EMA:%.2f", graph.getEMAForCandle(candle)));
						else emaL.setText("");
						revalidate();
					} catch(ArithmeticException err) {}
				}
			}
		});

		center.add(graph);
		this.add(center, BorderLayout.CENTER);
		
		north = new Panel();
		values = new Label("");
		values.setFont(new Font(Font.MONOSPACED, 1, 14));
		maL = new Label();
		maL.setFont(new Font(Font.MONOSPACED, 1, 14));
		maL.setForeground(Color.decode("#5662f6"));
		emaL = new Label();
		emaL.setFont(new Font(Font.MONOSPACED, 1, 14));
		emaL.setForeground(Color.decode("#ed9900"));
		north.add(values);
		north.add(maL);
		north.add(emaL);
		this.add(north, BorderLayout.NORTH);
		
		owner.pack();
		
	}

	public MainWindow getOwner() {
		return owner;
	}
	
}
