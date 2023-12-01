package stocks;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;

public class MenuPanel extends Panel{
	private MainWindow owner;
	private Label welcome;
	private TextArea options = new TextArea("", 5, 30, TextArea.SCROLLBARS_NONE);
	private Choice choice;
	private Button select;
	
	public MenuPanel(MainWindow owner) {
		this.owner = owner;
		setLayout(new GridLayout(3, 1, 5, 5));
		populatePanel();		
	}

	private void populatePanel() {
		welcome = new Label("Dobrodosli " + owner.user.getUsername() + "!");
		welcome.setAlignment(Label.CENTER);
		welcome.setFont(new Font("Calibri", 0, 15));
		this.add(welcome);
		
		Panel p = new Panel();
		options.setFont(new Font("Calibri", 0, 13));
		options.append("Odaberite stavku iz menija:\n");
		options.append("1. Prikazi portfolio korisnika\n");
		options.append("2. Prikupi podatke o odredjenoj akciji\n");
		options.append("3. Kupi ili prodaj akciju\n");
		options.append("4. Izlogujte se\n");
		
		p.add(options);
		this.add(p);
		
		Panel choiceP = new Panel();
		
		choice = new Choice();
		choice.add("1");
		choice.add("2");
		choice.add("3");
		choice.add("4");
		
		choiceP.add(choice);
		
		select = new Button("Odaberi");
		select.addActionListener(al -> {
			owner.changePanel(Integer.parseInt(choice.getSelectedItem()));
		});
		
		choiceP.add(select);
		
		this.add(choiceP);
		
		owner.pack();
		
	}
}
