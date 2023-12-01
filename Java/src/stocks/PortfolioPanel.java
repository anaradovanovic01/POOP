package stocks;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import exceptions.CouldntFindInfoAboutStock;

public class PortfolioPanel extends Panel {
	private MainWindow owner;
	Panel labels;
	
	public PortfolioPanel(MainWindow owner) {
		this.owner = owner;
		setLayout(new BorderLayout());
		
		setFont(new Font(Font.MONOSPACED, 0, 18));
		
		populateLabelPanel();
		
		Panel buttonP = new Panel();		
		Button back = new Button("Nazad");
		back.addActionListener(al -> {
			this.owner.goToMenu();
		});
		buttonP.add(back);
		this.add(buttonP, BorderLayout.SOUTH);
		
		this.owner.pack();

	}

	public void populateLabelPanel() {
		if(labels != null) this.remove(labels);
		
		labels = new Panel();
		labels.setLayout(new GridLayout(0,1));
		
		labels.add(new Label(String.format("%s%5.2f\n", "Trenutno stanje na racunu: ", owner.user.getMoney())));

		if (this.owner.user.getTransactions().isEmpty()) labels.add(new Label("Transakcije: ne posedujete akcije."));
		else {
			labels.add(new Label("Transakcije: "));
			labels.add(new Label((String.format("%-3s %-8s %s   %s   %s   %s   %s", "ID", "Akcija", "Kolicina", "Cena kupovine", "Trenutna cena", "Apsolutna razlika", "Relativna razlika(%)"))));
			for(Transaction t: this.owner.user.getTransactions()) {
				labels.add(t.toLabel());
			}
		}
		
		this.add(labels, BorderLayout.CENTER);
	}
	
	
}
