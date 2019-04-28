package src_package;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class ModButton extends JButton implements ActionListener, FieldChangeListener, MouseListener
{
	MinesweeperGame game;
	int x;
	int y;
	int pos;
	
	@Override public void mouseClicked(MouseEvent me)
	{
	}
	
	@Override public void mouseEntered(MouseEvent me)
	{
	}
	
	@Override public void mouseExited(MouseEvent me)
	{
	}
	
	@Override public void mouseReleased(MouseEvent me)
	{
		if (me.getButton() == MouseEvent.BUTTON3)
		{
			if (!TopPanel.flagMode)
			{
				game.flag(x, y);
				
				int temp = (int) game.revealedField[y][x] - 48;
				
				if (game.revealedField[y][x] == game.masterField[y][x] && temp == MinesweeperGame.getNumFlagged(x, y, game.revealedField))
				{
					game.clear(x, y);
				}
			}
			else
			{
				int temp = (int) game.revealedField[y][x] - 48;
				
				if (game.revealedField[y][x] == game.masterField[y][x] && temp == MinesweeperGame.getNumFlagged(x, y, game.revealedField))
				{
					game.clear(x, y);
				}
				else
				{
					game.reveal(x, y);
				}
			}
		}
	}
	
	@Override public void mousePressed(MouseEvent me)
	{
	}
	
	@Override public void actionPerformed(ActionEvent arg0)
	{
		if (TopPanel.flagMode)
		{
			game.flag(x, y);
			
			int temp = (int) game.revealedField[y][x] - 48;
			
			if (game.revealedField[y][x] == game.masterField[y][x] && temp == MinesweeperGame.getNumFlagged(x, y, game.revealedField))
			{
				game.clear(x, y);
			}
		}
		else
		{
			int temp = (int) game.revealedField[y][x] - 48;
			
			if (game.revealedField[y][x] == game.masterField[y][x] && temp == MinesweeperGame.getNumFlagged(x, y, game.revealedField))
			{
				game.clear(x, y);
			}
			else
			{
				game.reveal(x, y);
			}
		}
	}
	
	@Override public void fieldChanged(MinesweeperGame msg)
	{
		// System.out.println("At (" + x + ", " + y + "), is this my icon?" + (FieldPanel.charToImageIcon(game.revealedField[y][x]) != getIcon()));
		if (FieldPanel.charToImageIcon(game.revealedField[y][x]) != getIcon())
		{
			setIcon(FieldPanel.charToImageIcon(game.revealedField[y][x]));
			// System.out.println("Button at (" + x + ", " + y + ") was clicked.");
		}
	}
	
	public ModButton(int inx, int iny, int ncolumns, MinesweeperGame g)
	{
		game = g;
		x = inx;
		y = iny;
		pos = x + ncolumns * y;
		addActionListener(this);
		addMouseListener(this);
	}
	
	@Override public void fieldChangedWithXY(int xin, int yin)
	{
		if (x == xin && y == yin)
		{
			setIcon(FieldPanel.charToImageIcon(game.revealedField[y][x]));
			// System.out.println("Button at (" + x + ", " + y + ") was clicked.");
			// System.out.println("input x: " + xin + " input y: " + yin);
		}
	}
}
