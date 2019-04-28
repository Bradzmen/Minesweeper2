package src_package;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

interface FieldChangeListener
{
	void fieldChangedWithXY(int x, int y);
	
	void fieldChanged(MinesweeperGame msg);
}

interface NewFieldListener
{
	void newField();
}

public class MinesweeperGame
{
	public static final double HARD = 0.22625;
	public static final double NORMAL = 0.17625;
	public static final double EASY = 0.14346;
	char[][] masterField;
	char[][] revealedField;
	ArrayList<Integer> invalidLabels;
	public static int numMinesLeft;
	Random rng;
	public boolean finished = false;
	public static boolean ended = false;
	
	protected List<FieldChangeListener> listeners = new ArrayList<FieldChangeListener>();
	
	public void addFieldChangeListener(FieldChangeListener toAdd)
	{
		listeners.add(toAdd);
	}
	
	public static boolean isIndexInBounds(int indexX, int indexY, char[][] array)
	{
		boolean out = true;
		try
		{
			@SuppressWarnings("unused") char c = array[indexY][indexX];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			out = false;
		}
		
		return out;
	}
	
	public void flag(int x, int y)
	{
		if (revealedField[y][x] == '?')
		{
			revealedField[y][x] = 'F';
			numMinesLeft--;
		}
		else if (revealedField[y][x] == 'F')
		{
			revealedField[y][x] = '?';
			numMinesLeft++;
		}
		
		if (fieldFinished())
		{
			finished = true;
			ended = true;
		}
		
		for (FieldChangeListener fcl : listeners)
		{
			fcl.fieldChangedWithXY(x, y);
			fcl.fieldChanged(this);
		}
	}
	
	public boolean fieldFinished()
	{
		boolean out = true;
		
		loop: for (int i = 0; i < masterField.length; i++)
		{
			for (int n = 0; n < masterField[0].length; n++)
			{
				boolean temp = (revealedField[i][n] == 'F' && masterField[i][n] == 'M');
				boolean temp2 = (revealedField[i][n] == masterField[i][n]) || (revealedField[i][n] == '.' && masterField[i][n] == '0');
				boolean total = temp || temp2;
				
				if (!total)
				{
					out = false;
					break loop;
				}
			}
		}
		
		return out;
	}
	
	public void reveal(int x, int y)
	{
		if (revealedField[y][x] != 'F')
		{
			if (masterField[y][x] == 'M')
			{
				revealedField[y][x] = 'X';
				mineHit(x, y);
			}
			else if (masterField[y][x] == '0')
			{
				revealedField[y][x] = '.';
				clear(x, y);
			}
			else
			{
				revealedField[y][x] = masterField[y][x];
			}
		}
		
		if (fieldFinished())
		{
			finished = true;
			ended = true;
		}
		
		for (FieldChangeListener fcl : listeners)
		{
			fcl.fieldChangedWithXY(x, y);
			fcl.fieldChanged(this);
		}
	}
	
	public void clear(int x, int y)
	{
		for (int i = -1; i < 2; i++)
		{
			for (int n = -1; n < 2; n++)
			{
				boolean temp = i == 0 && n == 0;
				
				if (!temp && isIndexInBounds(x + i, y + n, revealedField) && revealedField[y + n][x + i] == '?')
				{
					reveal(x + i, y + n);
				}
			}
		}
	}
	
	public void mineHit(int x, int y)
	{
		System.out.println("Mine hit! " + "(" + x + ", " + y + ")");
		boolean isGuessed = false;
		for (int i = 0; i < masterField.length; i++)
		{
			for (int n = 0; n < masterField[0].length; n++)
			{
				isGuessed = i != y || n != x;
				
				if (masterField[i][n] == 'M' && revealedField[i][n] != 'F' && isGuessed)
				{
					revealedField[i][n] = 'M';
				}
				else if (masterField[i][n] == 'M' && revealedField[i][n] == 'F' && isGuessed)
				{
					revealedField[i][n] = 'F';
				}
				else if (masterField[i][n] != 'M' && revealedField[i][n] == 'F' && isGuessed)
				{
					revealedField[i][n] = 'x';
				}
				System.out.print(masterField[i][n]);
			}
			System.out.println("");
		}
		
		ended = true;
		
		for (FieldChangeListener fcl : listeners)
		{
			fcl.fieldChanged(this);
		}
	}
	
	public static int getNumFlagged(int x, int y, char[][] field)
	{
		int out = 0;
		
		for (int j = -1; j < 2; j++)
		{
			for (int k = -1; k < 2; k++)
			{
				boolean isCenter = k == 0 && j == 0;
				
				if (!isCenter && isIndexInBounds(x + k, y + j, field) && field[y + j][x + k] == 'F')
				{
					out++;
				}
			}
		}
		
		return out;
	}
	
	public int getNumMinesNeeded(int x, int y)
	{
		int numMines = 0;
		
		for (int j = -1; j < 2; j++)
		{
			for (int k = -1; k < 2; k++)
			{
				boolean temp = j == 0 && k == 0;
				
				if (!temp && isIndexInBounds(x + k, y + j, masterField) && masterField[y + j][x + k] == 'M' && masterField[y][x] != 'M')
				{
					numMines++;
				}
			}
		}
		
		return numMines;
	}
	
	public void makeNewField(int fieldx, int fieldy, int difficulty) // FLAG
	{
		finished = false;
		ended = false;
		
		if (difficulty == 0)
		{
			numMinesLeft = (int) Math.floor(fieldx * fieldy * EASY);
		}
		else if (difficulty == 1)
		{
			numMinesLeft = (int) Math.floor(fieldx * fieldy * NORMAL);
		}
		else if (difficulty == 2)
		{
			numMinesLeft = (int) Math.floor(fieldx * fieldy * HARD);
		}
		
		invalidLabels = new ArrayList<Integer>();
		
		revealedField = new char[fieldy][fieldx];
		masterField = new char[fieldy][fieldx];
		for (int i = 0; i < fieldy; i++)
		{
			for (int n = 0; n < fieldx; n++)
			{
				revealedField[i][n] = '?';
			}
		}
		
		ArrayList<Character> pick = new ArrayList<Character>();
		for (int i = 0; i < fieldx * fieldy; i++)
		{
			if (i < numMinesLeft)
			{
				pick.add(i, 'M');
			}
			else
			{
				pick.add(i, '0');
			}
		}
		
		for (int i = 0; i < fieldy; i++)
		{
			for (int n = 0; n < fieldx; n++)
			{
				int temp = rng.nextInt(pick.size());
				masterField[i][n] = pick.get(temp);
				pick.remove(temp);
			}
		}
		
		for (int i = 0; i < fieldy; i++)
		{
			for (int n = 0; n < fieldx; n++)
			{
				int numMines = 0;
				
				for (int j = -1; j < 2; j++)
				{
					for (int k = -1; k < 2; k++)
					{
						boolean temp = j == 0 && k == 0;
						
						if (!temp && isIndexInBounds(n + k, i + j, masterField) && masterField[i + j][n + k] == 'M' && masterField[i][n] != 'M')
						{
							numMines++;
						}
					}
				}
				
				if (masterField[i][n] != 'M')
				{
					masterField[i][n] = (char) (numMines + 48);
				}
			}
		}
		
		for (FieldChangeListener fcl : listeners)
		{
			fcl.fieldChanged(this);
		}
	}
	
	public MinesweeperGame()
	{
		invalidLabels = new ArrayList<Integer>();
		rng = new Random();
	}
}
