package stocks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import exceptions.*;

public class User {
	private File file = new File("files/users.txt");
	private File userfile;
	private String username;
	private String password;
	private double money;
	private List<Transaction> transactions = new ArrayList<Transaction>();
	boolean found, loginSuccessful = true;

	public User(String username, String password) {
		try {
			this.username = username;
			this.password = password;
			BufferedReader br = new BufferedReader(new FileReader(file));
			Stream<String> s = br.lines();
			found = false;
			s.forEach(line -> {
				if (line.equals(username + " " + password)) {
					found = true;
					try {
					userfile = new File("files/" + username + ".txt");
					Pattern p = Pattern.compile("^(.*);(.*);(.*);(.*)$");
					BufferedReader br2 = new BufferedReader(new FileReader(userfile));
					
					Stream<String> s2 = br2.lines();
					s2.forEach(l -> {
						Matcher m = p.matcher(l);
						if(m.matches()) {
							int id = Integer.parseInt(m.group(1));
							String stockName = m.group(2);
							int quantity = Integer.parseInt(m.group(3));
							double price = Double.parseDouble(m.group(4));
							Stock stock = new Stock(stockName);
							Transaction t = new Transaction(stock, quantity, id, price);
							transactions.add(t);
						}
						else {
							money = Double.parseDouble(l);
						}
						
					});
					br2.close();
					} catch (FileNotFoundException e) { System.out.println("Fajl nije pronadjen"); System.exit(1); }
					catch (IOException e) {}
				}
			});
			if (!found) {
				loginSuccessful = false;
			}
			br.close();
		} catch (FileNotFoundException e) { System.out.println("Fajl nije pronadjen"); System.exit(1); }
		catch (IOException e) {}
	}
	
	public User(String username, String password, String money) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			Stream<String> s = br.lines();
			s.forEach(line -> {
				if (line.equals(username + " " + password)) {
					loginSuccessful = false;
					return;
				}
			});
			if (!loginSuccessful) {
				br.close();
				return;
			}
			this.username = username;
			this.password = password;
			this.money = Double.parseDouble(money);
			
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(username + " " + password + "\n");
			
			userfile = new File("files/" + username + ".txt");
			FileWriter fw2 = new FileWriter(userfile);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			bw2.write(money);
			
			br.close();
			bw.close();
			bw2.close();
			
		} catch (FileNotFoundException e) { System.out.println("Fajl sa korisnicima nije pronadjen"); }
		catch (IOException e) {}
	}

	public String getUsername() {
		return username;
	}

	public double getMoney() {
		return money;
	}	
	
	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public void addTransaction(Transaction t) {
		transactions.add(t);
	}
	
	public Transaction buyStock(Stock stock, int quantity) throws NotEnoughMoney, CouldntFindInfoAboutStock {
		Transaction transaction = new Transaction(stock, quantity);
		double price = transaction.getPrice();
		if (money < price * quantity) throw new NotEnoughMoney();
		money -= price * quantity;
		return transaction;
	}

	public void sellStock(int id, int quantity) throws CantSell, CouldntFindInfoAboutStock {
		Transaction transaction = findTransaction(id);
		if (transaction == null) throw new CantSell();
		int currentQuantity = transaction.getQuantity();
		if (currentQuantity < quantity) throw new CantSell();
		else if(currentQuantity == quantity) {
			transactions.remove(transaction);
		}
		else {
			transaction.setQuantity(currentQuantity - quantity);
		}
		double price = transaction.getCurrentPrice();
		money += price * quantity;
	}

	private Transaction findTransaction(int id) {
		for(Transaction t: transactions) {
			if(t.getId() == id) return t;
		}
		return null;
	}
	
	public void logout() {
		try {
			File file = new File("files/" + username + ".txt");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(((Double)money).toString() + "\n");
			for(Transaction t: transactions) {
				bw.write(t.getId() + ";" + t.getStock().getName() + ";" + t.getQuantity() + ";" + t.getPrice() + "\n");
			}
			bw.close();
		} catch (IOException e) {}
		
	}
	
}
