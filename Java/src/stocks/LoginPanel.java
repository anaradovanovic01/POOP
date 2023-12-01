package stocks;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

public class LoginPanel extends Panel {
	private MainWindow owner;
	private Label choiceL;
	private Panel choiceP;
	private Button loginB = new Button("Uloguj se"), signupB = new Button("Napravi nalog");
	private Panel usernameP, passwordP, moneyP;
	private Label usernameL, passwordL, moneyL;
	private TextField usernameT, passwordT, moneyT;
	private Button newAccountB, existingAccountB, backB = new Button("Nazad");;
	private Panel buttonsP;
		
	public LoginPanel(MainWindow owner) {
		this.owner = owner;
		populatePanel();
		this.setPreferredSize(new Dimension(350, 200));
		setLayout(new GridLayout(2, 1, 5, 5));
		
		loginB.addActionListener(ae -> {
			login();
		});
		
		signupB.addActionListener(ae -> {
			signUp();
		});
		
	}

	private void signUp() {
		this.remove(choiceL);
		this.remove(choiceP);
		this.setPreferredSize(new Dimension(300, 350));
		setLayout(new GridLayout(4, 1, 5, 5));
		
		usernameP.add(usernameL);
		usernameP.add(usernameT);
		this.add(usernameP);
		
		passwordP.add(passwordL);
		passwordP.add(passwordT);
		this.add(passwordP);
		
		moneyP.add(moneyL);
		moneyP.add(moneyT);
		this.add(moneyP);
		
		buttonsP = new Panel();
		buttonsP.add(newAccountB);
		
		backB.addActionListener(al -> {
			this.remove(usernameP);
			this.remove(passwordP);
			this.remove(moneyP);
			this.remove(buttonsP);
			usernameT.setText("");
			passwordT.setText("");
			moneyT.setText("");
			this.setPreferredSize(new Dimension(350, 200));
			setLayout(new GridLayout(2, 1, 5, 5));
			this.add(choiceL);
			this.add(choiceP);
			owner.pack();
		});
		
		buttonsP.add(backB);
		this.add(buttonsP);
		
		owner.pack();
		
	}

	private void login() {
		this.remove(choiceL);
		this.remove(choiceP);
		setLayout(new GridLayout(3, 1, 5, 5));
		this.setPreferredSize(new Dimension(300, 250));
		
		usernameP.add(usernameL);
		usernameP.add(usernameT);
		this.add(usernameP);
		
		passwordP.add(passwordL);
		passwordP.add(passwordT);
		this.add(passwordP);
		
		buttonsP = new Panel();
		buttonsP.add(existingAccountB);
		
		backB.addActionListener(al -> {
			this.remove(usernameP);
			this.remove(passwordP);
			this.remove(buttonsP);
			usernameT.setText("");
			passwordT.setText("");
			this.setPreferredSize(new Dimension(350, 200));
			setLayout(new GridLayout(2, 1, 5, 5));
			this.add(choiceL);
			this.add(choiceP);
			owner.pack();
		});
		
		buttonsP.add(backB);
		this.add(buttonsP);
		
		owner.pack();
	}

	private void populatePanel() {
		usernameP = new Panel();
		usernameL = new Label("Korisnicko ime: ");
		usernameT = new TextField(15);
		
		passwordP = new Panel();
		passwordL = new Label("Lozinka: ");
		passwordT = new TextField(15);
		passwordT.setEchoChar('*');
		
		moneyP = new Panel();
		moneyL = new Label("Ulog: ");
		moneyT = new TextField(15);
		
		usernameT.addTextListener(tl -> {
			if(usernameT.getText().equals("") || passwordT.getText().equals("")) existingAccountB.setEnabled(false);
			else existingAccountB.setEnabled(true);
			if(usernameT.getText().equals("") || passwordT.getText().equals("") || moneyT.getText().equals("")) newAccountB.setEnabled(false);
			else newAccountB.setEnabled(true);
		});
		
		passwordT.addTextListener(tl -> {
			if(usernameT.getText().equals("") || passwordT.getText().equals("")) existingAccountB.setEnabled(false);
			else existingAccountB.setEnabled(true);
			if(usernameT.getText().equals("") || passwordT.getText().equals("") || moneyT.getText().equals("")) newAccountB.setEnabled(false);
			else newAccountB.setEnabled(true);
		});
		
		moneyT.addTextListener(tl -> {
			if(usernameT.getText().equals("") || passwordT.getText().equals("")) existingAccountB.setEnabled(false);
			else existingAccountB.setEnabled(true);
			if(usernameT.getText().equals("") || passwordT.getText().equals("") || moneyT.getText().equals("")) newAccountB.setEnabled(false);
			else newAccountB.setEnabled(true);
		});
		
		existingAccountB = new Button("Uloguj se");
		existingAccountB.setEnabled(false);
		existingAccountB.addActionListener(al -> {
			owner.user = new User(usernameT.getText(), passwordT.getText());
			if (owner.user.loginSuccessful) owner.loggedIn();
			else {
				owner.new DoneDialog(owner, "Korisnik ne postoji");
			}
			this.remove(usernameP);
			this.remove(passwordP);
			buttonsP.remove(existingAccountB);
			buttonsP.remove(backB);
			this.remove(buttonsP);
			usernameT.setText("");
			passwordT.setText("");
			this.setPreferredSize(new Dimension(350, 200));
			setLayout(new GridLayout(2, 1, 5, 5));
			this.add(choiceL);
			this.add(choiceP);
			owner.pack();
		});
		
		newAccountB = new Button("Napravi nalog");
		newAccountB.setEnabled(false);
		newAccountB.addActionListener(al -> {
			owner.user = new User(usernameT.getText(), passwordT.getText(), moneyT.getText());
			if (owner.user.loginSuccessful) owner.loggedIn();
			else {
				owner.new DoneDialog(owner, "Korisnik vec postoji");
			}
			this.remove(usernameP);
			this.remove(passwordP);
			this.remove(moneyP);
			this.remove(buttonsP);
			usernameT.setText("");
			passwordT.setText("");
			moneyT.setText("");
			this.setPreferredSize(new Dimension(350, 200));
			setLayout(new GridLayout(2, 1, 5, 5));
			this.add(choiceL);
			this.add(choiceP);
			owner.pack();
		});
		
		choiceL = new Label("Da li zelite da se ulogujete na svoj nalog ili napravite novi?");
		choiceL.setAlignment(Label.CENTER);
		this.add(choiceL);
		
		choiceP = new Panel();
		
		choiceP.add(loginB);
		choiceP.add(signupB);
		
		this.add(choiceP);
		
		
	}
}
