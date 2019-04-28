package src_package;

import java.awt.Point;
import java.util.ArrayList;

public class Square implements FieldChangeListener
{
	int xCoord;
	int yCoord;
	int numMinesNeeded;
	int numMinesRevealed;
	char[][] minefield; // same as MSG.revealedField, initialized in the constructor
	char[][] adjacentAUS; // AUS are adjacent undiscovered squares
	char[][] adjacentSquares;
	double[][] probAUS; // TODO May not be necessary; contains the probabilities of mines in the AUS
	int chainID;
	char center;
	
	@Override public void fieldChangedWithXY(int x, int y)
	{
		boolean adjacent = false;
		
		loop: for (int i = -1; i < 2; i++)
		{
			for (int n = -1; n < 2; n++)
			{
				if (xCoord + n == x && yCoord + i == y)
				{
					adjacent = true;
					break loop;
				}
			}
		}
		
		if (adjacent)
		{
			if (minefield[y][x] != 'F')
			{
				// Kill chain?
			}
			else
			{
				numMinesRevealed++;
				adjacentAUS[y][x] = 'F';
				adjacentSquares[y][x] = 'F';
				probAUS[y][x] = 0.0;
			}
		}
		else if (xCoord == x && yCoord == y)
		{
			center = minefield[y][x];
		}
	}
	
	public int getNumAUS()
	{
		int out = 0;
		
		for (int i = -1; i < 2; i++)
		{
			for (int n = -1; n < 2; n++)
			{
				if (MinesweeperGame.isIndexInBounds(xCoord + n, yCoord + i, adjacentAUS) && adjacentAUS[yCoord + i][xCoord + n] == '?')
				{
					out++;
				}
			}
		}
		
		return out;
	}
	
	public ArrayList<Point> getAUS()
	{
		ArrayList<Point> aus = new ArrayList<Point>();
		
		for (int i = -1; i < 2; i++)
		{
			for (int n = -1; n < 2; n++)
			{
				if (MinesweeperGame.isIndexInBounds(xCoord + n, yCoord + i, adjacentAUS) && adjacentAUS[yCoord + i][xCoord + n] == '?')
				{
					aus.add(new Point(xCoord + n, yCoord + i));
				}
			}
		}
		
		return aus;
	}
	
	public int getNumFlagged()
	{
		int out = 0;
		
		for (int i = -1; i < 2; i++)
		{
			for (int n = -1; n < 2; n++)
			{
				if (MinesweeperGame.isIndexInBounds(xCoord + n, yCoord + i, minefield) && minefield[yCoord + i][xCoord + n] == 'F')
				{
					out++;
				}
			}
		}
		
		return out;
	}
	
	public void markAll()
	{
		for (int i = -1; i < 2; i++)
		{
			for (int n = -1; n < 2; n++)
			{
				if (MinesweeperGame.isIndexInBounds(xCoord + n, yCoord + i, adjacentAUS) && adjacentAUS[yCoord + i][xCoord + n] == '?')
				{
					minefield[yCoord + i][xCoord + n] = 'F';
					adjacentAUS[yCoord + i][xCoord + n] = 'F';
				}
			}
		}
	}
	
	public boolean sameAs(Square s)
	{
		boolean out = true;
		
		if (minefield != s.minefield)
		{
			out = false;
		}
		if (xCoord != s.xCoord || yCoord != s.yCoord)
		{
			out = false;
		}
		if (/* chainID != s.chainID || */numMinesNeeded != s.numMinesNeeded || numMinesRevealed != s.numMinesRevealed)
		{
			out = false;
		}
		
		return out;
	}
	
	public Square(int x, int y, int cid, char[][] field)
	{
		xCoord = x;
		yCoord = y;
		chainID = cid;
		minefield = field;
		probAUS = new double[minefield.length][minefield[0].length];
		adjacentSquares = new char[minefield.length][minefield[0].length];
		adjacentAUS = new char[minefield.length][minefield[0].length];
		
		for (int j = -1; j < 2; j++)
		{
			for (int n = -1; n < 2; n++)
			{
				if (MinesweeperGame.isIndexInBounds(x + n, y + j, minefield) && !(j == 0 && n == 0))
				{
					adjacentSquares[y + j][x + n] = minefield[y + j][x + n];
				}
			}
		}
		
		for (int j = -1; j < 2; j++)
		{
			for (int n = -1; n < 2; n++)
			{
				if (MinesweeperGame.isIndexInBounds(x + n, y + j, minefield) && minefield[y + j][x + n] == '?' && !(j == 0 && n == 0))
				{
					adjacentAUS[y + j][x + n] = minefield[y + j][x + n];
				}
			}
		}
		
		numMinesRevealed = MinesweeperGame.getNumFlagged(x, y, minefield);
		numMinesNeeded = (minefield[y][x] - 48);
		
		center = minefield[xCoord][yCoord];
	}
	
	@Override public void fieldChanged(MinesweeperGame msg)
	{
	}
}
