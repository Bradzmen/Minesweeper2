package src_package;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsDialog
{
	JPanel holder;
	
	JLabel widthLabel;
	JLabel heightLabel;
	JTextField widthField;
	JTextField heightField;
	JLabel difficultyLabel;
	JComboBox difficultyBox;
	String[] labels = { "Easy", "Medium", "Hard" };
	
	public int labelToInt(String label)
	{
		if (label.equals("Easy"))
		{
			return 0;
		}
		else if (label.equals("Medium"))
		{
			return 1;
		}
		else if (label.equals("Hard"))
		{
			return 2;
		}
		else
		{
			return 3; // None of the above, return nonsense
		}
	}
	
	public boolean isStringInteger(String s)
	{
		boolean out = true;
		try
		{
			int i = Integer.parseInt(s);
		}
		catch (NumberFormatException nfe)
		{
			out = false;
		}
		
		return out;
	}
	
	public SettingsDialog(GameWindow gw)
	{
		holder = new JPanel();
		widthLabel = new JLabel("Width:");
		heightLabel = new JLabel("Height:");
		difficultyLabel = new JLabel("Difficulty:");
		widthField = new JTextField(5);
		heightField = new JTextField(5);
		difficultyBox = new JComboBox(labels);
		
		holder.setLayout(new GridLayout(3, 2, 5, 5));
		
		holder.add(widthLabel);
		holder.add(widthField);
		holder.add(heightLabel);
		holder.add(heightField);
		holder.add(difficultyLabel);
		holder.add(difficultyBox);
		
		int option = JOptionPane.showConfirmDialog(null, holder, "Settings", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			int width = 0;
			int height = 0;
			int difficulty = 0;
			
			difficulty = labelToInt((String) difficultyBox.getSelectedItem());
			
			if (!isStringInteger(widthField.getText()))
			{
				width = gw.fieldPanel.fieldX;
			}
			else
			{
				width = Integer.parseInt(widthField.getText());
				System.out.println("width: " + widthField.getText());
			}
			
			if (!isStringInteger(heightField.getText()))
			{
				height = gw.fieldPanel.fieldY;
			}
			else
			{
				height = Integer.parseInt(heightField.getText());
				System.out.println("height: " + heightField.getText());
			}
			
			gw.dispose();
			gw = new GameWindow(width, height, difficulty);
			for (int i = 0; i < gw.fieldPanel.mineGame.masterField.length; i++)
			{
				for (int n = 0; n < gw.fieldPanel.mineGame.masterField[0].length; n++)
				{
					System.out.print(gw.fieldPanel.mineGame.masterField[i][n]);
				}
				System.out.println("");
			}
		}
	}
}
