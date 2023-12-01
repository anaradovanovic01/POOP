package stocks;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends Frame {
	private LoginPanel login = new LoginPanel(this);
	private MenuPanel menu;
	private Panel currentPanel;
	private ShowActionPanel showAction;
	private PortfolioPanel portfolio;
	private SellBuyPanel sellBuy;
	User user;
	
	public class DoneDialog extends Dialog {	
		public DoneDialog(MainWindow owner, String label) {
			super(owner);
			this.setFont(new Font("Calibri", 0, 13));
			setTitle("Info");
			setBounds(owner.getX() + owner.getWidth() / 2 - 100, owner.getY() + owner.getHeight() / 2 - 75, 200, 150);
			setLayout(new GridLayout(2, 1));
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			Label labelL = new Label(label);
			labelL.setAlignment(Label.CENTER);;
			this.add(labelL);
			Panel buttons = new Panel();
			Button ok = new Button("OK");
			ok.addActionListener((ae) -> {
				user.logout();
				this.dispose();
			});
			buttons.add(ok);
			this.add(buttons);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
			setVisible(true);
		}
	}
	
	
	public MainWindow() {
		this.setFont(new Font("Calibri", 0, 13));
		setLocation(700, 200);
		setResizable(false);
		setTitle("Prijavljivanje");
				
		this.add(login, BorderLayout.CENTER);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}});
		
		pack();
		setVisible(true);
	}	
	
	
	
	public void loggedIn() {
		setTitle("Meni");
		this.remove(login);
		menu = new MenuPanel(this);
		this.add(menu, BorderLayout.CENTER);
		pack();		
	}

	public void changePanel(int panel) {
		switch (panel) {
		case 1:
			if(portfolio == null) portfolio = new PortfolioPanel(this);
			else portfolio.populateLabelPanel();
			currentPanel = portfolio;
			setTitle("Prikaz podataka o korisniku");
			setLocation(300, 200);
			break;
		case 2:
			if(showAction == null) showAction = new ShowActionPanel(this);
			currentPanel = showAction;
			setTitle("Prikaz podataka o akciji");
			setLocation(100, 100);
			break;
		case 3:
			if(sellBuy == null) sellBuy = new SellBuyPanel(this); 
			currentPanel = sellBuy;
			setTitle("Kupovina/prodaja akcije");
			break;
		case 4: 
			currentPanel = login;
			setTitle("Prijavljivanje");
			user.logout();
			user = null;
			break;
		}
		this.remove(menu);
		this.add(currentPanel, BorderLayout.CENTER);
		pack();
		
	}

	public void goToMenu() {
		setTitle("Meni");
		this.remove(currentPanel);
		currentPanel = menu;
		setLocation(700, 200);
		this.add(menu, BorderLayout.CENTER);
		pack();
	}
	
	public static void main(String[] args) {
		System.loadLibrary("libcurl-x64");
		System.loadLibrary("DLL_Crawler");
		new MainWindow();
	}
	
}
