package stocks;

import java.awt.Color;
import java.awt.Label;

import exceptions.CouldntFindInfoAboutStock;

public class Transaction {
	
	private static int ID;
	private int id;
	private Stock stock;
	private int quantity;
	private double price;
	
	public Transaction(Stock stock, int quantity) throws CouldntFindInfoAboutStock {
		this.stock = stock;
		this.quantity = quantity;
		this.price = getCurrentPrice();
		this.id = ++ID;
	}

	public Transaction(Stock stock, int quantity, int id, double price) {
		this.id = id;
		this.stock = stock;
		this.quantity = quantity;
		this.price = price;
		ID = id;
	}

	public double getCurrentPrice() throws CouldntFindInfoAboutStock {
		String timestamp = "1616072670";
		Crawler crawler = new Crawler();
		crawler.makeJSON(stock.getName(), timestamp, timestamp);
		JSONParser jsp = new JSONParser("files/" + stock.getName() + ".json");
		return jsp.getCurrentPrice();
	}
	
	double getRelativeDiff(double x, double y) {
		if (x > y) return (x / y - 1) * 100;
		else return (y / x - 1) * 100;
	}
	
	public Label toLabel() {
		try {
			Label label = new Label();
			double currentPrice = getCurrentPrice();
			if (price - currentPrice >= 0) label.setForeground(Color.RED);
			else label.setForeground(Color.decode("#36b622"));
			label.setText(String.format("%-3d %-8s %-8d   %-13.2f   %-13.2f   %-17.2f   %-17.2f", id, stock.getName(), quantity, price, currentPrice, 
					Math.abs(currentPrice - price), getRelativeDiff(currentPrice, price)));
			return label;
		} catch (CouldntFindInfoAboutStock e) { return null; } 
	}

	public int getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}
	
	public Stock getStock() {
		return stock;
	}

	public static int getID() {
		return ID;
	}

	public static void setID(int iD) {
		ID = iD;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
