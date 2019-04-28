package src_package;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MineCounter extends JPanel implements FieldChangeListener, NewFieldListener
{
	// TODO: limit field size not to exceed an area of 2855 (999*(100/35)) where .35 is the max density.
	
	JLabel digit1;
	JLabel digit2;
	JLabel digit3;
	
	@Override public void fieldChanged(MinesweeperGame msg)
	{
		String numMines = Integer.toString(MinesweeperGame.numMinesLeft);
		
		if (numMines.length() == 1)
		{
			digit1.setIcon(TimerClock.charToIcon(numMines.charAt(0)));
			digit2.setIcon(TimerClock.zero);
			digit3.setIcon(TimerClock.zero);
		}
		else if (numMines.length() == 2)
		{
			digit2.setIcon(TimerClock.charToIcon(numMines.charAt(0)));
			digit1.setIcon(TimerClock.charToIcon(numMines.charAt(1)));
			digit3.setIcon(TimerClock.zero);
		}
		else if (numMines.length() == 3)
		{
			digit3.setIcon(TimerClock.charToIcon(numMines.charAt(0)));
			digit2.setIcon(TimerClock.charToIcon(numMines.charAt(1)));
			digit1.setIcon(TimerClock.charToIcon(numMines.charAt(2)));
		}
	}
	
	@Override public void newField()
	{
		String numMines = Integer.toString(MinesweeperGame.numMinesLeft);
		
		if (numMines.length() == 1)
		{
			digit1.setIcon(TimerClock.charToIcon(numMines.charAt(0)));
			digit2.setIcon(TimerClock.zero);
			digit3.setIcon(TimerClock.zero);
		}
		else if (numMines.length() == 2)
		{
			digit2.setIcon(TimerClock.charToIcon(numMines.charAt(0)));
			digit1.setIcon(TimerClock.charToIcon(numMines.charAt(1)));
			digit3.setIcon(TimerClock.zero);
		}
		else if (numMines.length() == 3)
		{
			digit3.setIcon(TimerClock.charToIcon(numMines.charAt(0)));
			digit2.setIcon(TimerClock.charToIcon(numMines.charAt(1)));
			digit1.setIcon(TimerClock.charToIcon(numMines.charAt(2)));
		}
	}
	
	public MineCounter()
	{
		digit1 = new JLabel(TimerClock.zero);
		digit2 = new JLabel(TimerClock.zero);
		digit3 = new JLabel(TimerClock.zero);
		
		setLayout(new GridLayout(1, 3, 0, 0));
		add(digit3);
		add(digit2);
		add(digit1);
	}
	
	@Override public void fieldChangedWithXY(int x, int y)
	{
		// TODO Auto-generated method stub
		
	}
}
