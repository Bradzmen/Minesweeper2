package src_package;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class InfoDialog
{
	JPanel holder;
	JPanel flagTogglePanel;
	
	JLabel goalLabel;
	JLabel explainLabel;
	JLabel counterLabel;
	JLabel flagToggleLabel1;
	JLabel flagToggleLabel2;
	JLabel probLabel;
	JLabel settingsLabel;
	JLabel versionLabel;
	
	public InfoDialog()
	{
		holder = new JPanel();
		flagTogglePanel = new JPanel();
		goalLabel = new JLabel("The goal of the game is to flag all mines in the minefield. When you are done or want to restart, click the happy face.");
		explainLabel = new JLabel("The numbers in the field tell you how many mines are adjacent to that square.");
		counterLabel = new JLabel("The counter on the left displays the number of mines left and the one on the right shows how many seconds have passed.");
		flagToggleLabel1 = new JLabel(FieldPanel.mine);
		flagToggleLabel2 = new JLabel(FieldPanel.flag);
		flagToggleLabel2.setText("This button at the top toggles right click versus left click. The icon shown is what left click does, while right click is the other.");
		probLabel = new JLabel(TopPanel.analysis);
		probLabel.setText("When you have to make a guess, this will give you a breakdown of the current probabilities.");
		probLabel.setHorizontalAlignment(JLabel.LEFT);
		settingsLabel = new JLabel(TopPanel.settings);
		settingsLabel.setText("This allows you to change the field size and diffifculty. Default size is 20x20. The smallest possible dimensions are 17x1.");
		settingsLabel.setHorizontalAlignment(JLabel.LEFT);
		versionLabel = new JLabel("v1.2 Made on 17 Nov 2018"); // TODO: update this with every version change
		
		flagTogglePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		flagTogglePanel.add(flagToggleLabel1);
		flagTogglePanel.add(flagToggleLabel2);
		
		holder.setLayout(new GridLayout(7, 1, 1, 1));
		holder.add(goalLabel);
		holder.add(explainLabel);
		holder.add(counterLabel);
		holder.add(flagTogglePanel);
		holder.add(probLabel);
		holder.add(settingsLabel);
		holder.add(versionLabel);
		
		JOptionPane.showMessageDialog(null, holder, "Rules & Information", JOptionPane.INFORMATION_MESSAGE);
	}
}
