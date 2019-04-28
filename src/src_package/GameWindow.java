package src_package;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class GameWindow extends JFrame implements ActionListener
{
	FieldPanel fieldPanel;
	TopPanel topPanel;
	static Image frameIcon;
	
	@Override public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource() == topPanel.settingsButton)
		{
			SettingsDialog s = new SettingsDialog(this);
		}
		else if (arg0.getSource() == topPanel.infoButton)
		{
			InfoDialog id = new InfoDialog();
		}
	}
	
	public GameWindow(int x, int y, int diff)
	{
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		fieldPanel = new FieldPanel(x, y, diff); // Must go first
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 10, 10, 10);
		add(fieldPanel, c);
		
		topPanel = new TopPanel();
		topPanel.resetButton.addNewFieldListener(fieldPanel);
		topPanel.resetButton.addNewFieldListener(topPanel.clock);
		topPanel.resetButton.addNewFieldListener(topPanel.mineCount);
		fieldPanel.mineGame.addFieldChangeListener(topPanel.resetButton);
		fieldPanel.mineGame.addFieldChangeListener(topPanel.clock);
		fieldPanel.mineGame.addFieldChangeListener(topPanel.mineCount);
		topPanel.settingsButton.addActionListener(this);
		topPanel.infoButton.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 10, 5, 10);
		add(topPanel, c);
		
		frameIcon = FieldPanel.makeImage("resources/frame_icon.png");
		
		pack();
		int temp = (int) ((getWidth() - (100 + 39 * 2 + 16 * 2 + 26)) / 2) - 36;
		topPanel.empty1.setPreferredSize(new Dimension(temp, 1));
		topPanel.empty2.setPreferredSize(new Dimension(temp, 1));
		topPanel.revalidate();
		topPanel.repaint();
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Minesweeper");
		setIconImage(frameIcon);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		GameWindow gw = new GameWindow(21, 20, 0);
	}
}
