package stocks;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ItemEvent;

import exceptions.*;

public class SellBuyPanel extends Panel {
	private MainWindow owner;
	private Checkbox buyC, sellC;
	private Label nameL, quantityL, idL;
	private TextField nameT, quantityT, idT;
	private Panel checkboxP, nameP, quantityP, idP, buttonsP;
	private Button buyB, sellB, back;	

	public SellBuyPanel(MainWindow owner) {
		this.owner = owner;
		setLayout(new GridLayout(4, 1, 30, 30));
		setPreferredSize(new Dimension(300, 250));
		populatePanel();
		owner.pack();
		
	}

	private void populatePanel() {
		checkboxP = new Panel();
		CheckboxGroup cg = new CheckboxGroup();
		buyC = new Checkbox("Kupi", true, cg);
		buyC.addItemListener(ie -> {
			if(ie.getStateChange() == ItemEvent.SELECTED) {
				this.remove(idP);
				this.remove(quantityP);
				this.remove(buttonsP);
				this.add(nameP);
				this.add(quantityP);
				buttonsP.remove(sellB);
				buttonsP.remove(back);
				buttonsP.add(buyB);
				buttonsP.add(back);
				this.add(buttonsP);
				revalidate();
				owner.pack();
			}
		});
		
		sellC = new Checkbox("Prodaj", false, cg);
		sellC.addItemListener(ie -> {
			if(ie.getStateChange() == ItemEvent.SELECTED) {
				this.remove(nameP);
				this.remove(quantityP);
				this.remove(buttonsP);
				this.add(idP);
				this.add(quantityP);
				buttonsP.remove(buyB);
				buttonsP.remove(back);
				buttonsP.add(sellB);
				buttonsP.add(back);
				this.add(buttonsP);
				revalidate();
				owner.pack();
			}
		});
		
		checkboxP.add(buyC);
		checkboxP.add(sellC);
		this.add(checkboxP);
		
		nameP = new Panel();
		nameL = new Label("Ime akcije");
		nameT = new TextField(10);
		nameP.add(nameL);
		nameP.add(nameT);
		this.add(nameP);
		
		quantityP = new Panel();
		quantityL = new Label("Kolicina");
		quantityT = new TextField(10);
		quantityP.add(quantityL);
		quantityP.add(quantityT);
		this.add(quantityP);
		
		idP = new Panel();
		idL = new Label("ID transakcije");
		idT = new TextField(10);
		idP.add(idL);
		idP.add(idT);
		
		nameT.addTextListener(tl -> {
			if(nameT.getText().equals("") || quantityT.getText().equals("")) buyB.setEnabled(false);
			else buyB.setEnabled(true);
		});
		
		quantityT.addTextListener(tl -> {
			if(nameT.getText().equals("") || quantityT.getText().equals("")) buyB.setEnabled(false);
			else buyB.setEnabled(true);
			if(idT.getText().equals("") || quantityT.getText().equals("")) sellB.setEnabled(false);
			else sellB.setEnabled(true);
		});
		
		idT.addTextListener(tl -> {
			if(idT.getText().equals("") || quantityT.getText().equals("")) sellB.setEnabled(false);
			else sellB.setEnabled(true);
		});
		
		buttonsP = new Panel();
		buyB = new Button("Kupi");
		buyB.setEnabled(false);
		buyB.addActionListener(al -> {
			try {
				Stock stock = new Stock(nameT.getText());
				Transaction transaction = owner.user.buyStock(stock, Integer.parseInt(quantityT.getText()));
				owner.new DoneDialog(owner, "Uspesno ste kupili akciju");
				nameT.setText(""); quantityT.setText("");
				owner.user.addTransaction(transaction);
			} catch (NotEnoughMoney e) {
				owner.new DoneDialog(owner, "Nemate dovoljno novca");
			} catch (CouldntFindInfoAboutStock e) {owner.new DoneDialog(owner, "Uneti su nevalidni podaci");}
		});
		buttonsP.add(buyB);
		
		sellB = new Button("Prodaj");
		sellB.setEnabled(false);
		sellB.addActionListener(al -> {
			try {
				owner.user.sellStock(Integer.parseInt(idT.getText()), Integer.parseInt(quantityT.getText()));
				owner.new DoneDialog(owner, "Uspesno ste prodali akciju");
				idT.setText(""); quantityT.setText("");
			} catch (CantSell | CouldntFindInfoAboutStock e) { owner.new DoneDialog(owner, "Uneti su nevalidni podaci"); }
		});
				
		back = new Button("Nazad");
		back.addActionListener(al -> {
			owner.goToMenu();
		});
		buttonsP.add(back);
		
		this.add(buttonsP);
		
	}
	
}
