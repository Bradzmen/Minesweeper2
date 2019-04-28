package src_package;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class FaceButton extends JButton implements FieldChangeListener, ActionListener
{
	static ImageIcon happy;
	static ImageIcon happy_clicked;
	static ImageIcon won;
	static ImageIcon won_clicked;
	
	private List<NewFieldListener> listeners = new ArrayList<NewFieldListener>();
	
	public void addNewFieldListener(NewFieldListener toAdd)
	{
		listeners.add(toAdd);
	}
	
	@Override public void fieldChanged(MinesweeperGame msg)
	{
		if (msg.finished)
		{
			setIcon(won);
			setPressedIcon(won_clicked);
		}
	}
	
	@Override public void actionPerformed(ActionEvent e)
	{
		setIcon(happy);
		setPressedIcon(happy_clicked);
		
		for (NewFieldListener nfl : listeners)
		{
			nfl.newField();
		}
	}
	
	public void callFieldChange()
	{
		for (NewFieldListener nfl : listeners)
		{
			nfl.newField();
		}
	}
	
	public FaceButton()
	{
		happy = new ImageIcon(FieldPanel.makeImage("resources/happy.png"));
		happy_clicked = new ImageIcon(FieldPanel.makeImage("resources/happy_clicked.png"));
		won = new ImageIcon(FieldPanel.makeImage("resources/won.png"));
		won_clicked = new ImageIcon(FieldPanel.makeImage("resources/won_clicked.png"));
		
		addActionListener(this);
		setIcon(happy);
		setPressedIcon(happy_clicked);
		setMinimumSize(new Dimension(happy.getIconHeight(), happy.getIconWidth()));
		setMaximumSize(new Dimension(happy.getIconHeight(), happy.getIconWidth()));
	}
	
	@Override public void fieldChangedWithXY(int x, int y)
	{
		// TODO Auto-generated method stub
		
	}
}
