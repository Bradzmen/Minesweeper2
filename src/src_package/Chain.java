package src_package;

import java.awt.Point;
import java.util.ArrayList;

public class Chain
{
	ArrayList<Square> squares;
	char[][] afterField;
	char[][] minefield;
	int ID;
	boolean evaluatingChain = false;
	
	public static boolean isIndexInBounds(int indexX, int indexY, char[][] array)
	{
		boolean out = true;
		try
		{
			char c = array[indexY][indexX];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			out = false;
		}
		
		return out;
	}
	
	public static boolean isIndexValid(int index, ArrayList<Integer> array)
	{
		boolean out = true;
		try
		{
			int c = array.get(index);
		}
		catch (IndexOutOfBoundsException e)
		{
			out = false;
		}
		
		return out;
	}
	
	public void evaluateChain(MinesweeperGameWithAI msg, boolean kill) // FLAG
	{
		if (kill)
		{
			boolean killChain = false;
			boolean interested = true;
			ArrayList<Square> toBeRemoved = new ArrayList<Square>();
			
			msg.evaluating = true;
			evaluatingChain = true;
			
			while (interested)
			{
				interested = false;
				for (int i = 0; i < squares.size(); i++)
				{
					if (squares.get(i).getNumFlagged() == squares.get(i).numMinesNeeded)
					{
						// System.out.println("---Chain #" + ID + " Square @ (" + squares.get(i).xCoord + ", " + squares.get(i).yCoord + ") cleared--- nNeeded,nFlags: " + squares.get(i).numMinesNeeded + ","
						// + squares.get(i).getNumFlagged());
						msg.clear(squares.get(i).xCoord, squares.get(i).yCoord);
						killChain = true;
						interested = true;
						toBeRemoved.add(squares.get(i));
					}
					else if (squares.get(i).getNumAUS() + squares.get(i).getNumFlagged() == squares.get(i).numMinesNeeded)
					{
						squares.get(i).markAll();
						msg.clear(squares.get(i).xCoord, squares.get(i).yCoord);
						// System.out.println("---Chain #" + ID + " Square @ (" + squares.get(i).xCoord + ", " + squares.get(i).yCoord + ") cleared--- nNeeded,nFlags: " + squares.get(i).numMinesNeeded + ","
						// + squares.get(i).getNumFlagged());
						killChain = true;
						interested = true;
						toBeRemoved.add(squares.get(i));
					}
					else if (squares.get(i).getNumAUS() == 0)
					{
						// System.out.println("Chain #" + ID + " should be dead and outdated.");
						killChain = true;
						interested = false;
					}
					else if (squares.get(i).getNumAUS() > 0)
					{
						// System.out.println("---Chain #" + ID + " Square @ (" + squares.get(i).xCoord + ", " + squares.get(i).yCoord + ") outdated/not solved--- nNeeded,nFlags: "
						// + squares.get(i).numMinesNeeded + "," + squares.get(i).getNumFlagged());
					}
				}
				
				squares.removeAll(toBeRemoved);
				toBeRemoved.clear();
			}
			
			msg.evaluating = false;
			evaluatingChain = false;
			if (killChain)
			{
				// System.out.println("Chain #" + ID + " called for self-destruct");
				msg.chainHash.remove(ID);
				msg.invalidLabels.add(ID);
				msg.validLabels.removeAll(msg.invalidLabels);
				for (int i = 0; i < msg.idField.length; i++)
				{
					for (int n = 0; n < msg.idField[0].length; n++)
					{
						if (msg.idField[i][n].size() > 0)
						{
							msg.idField[i][n].removeAll(msg.invalidLabels);
						}
					}
				}
				// System.out.println("Killed chain #" + ID);
			}
		}
		else
		{
			boolean interested = true;
			
			msg.evaluating = true;
			evaluatingChain = true;
			
			while (interested)
			{
				interested = false;
				for (int i = 0; i < squares.size(); i++)
				{
					if (squares.get(i).getNumFlagged() == squares.get(i).numMinesNeeded)
					{
						// System.out.println("---Chain #" + ID + " Square @ (" + squares.get(i).xCoord + ", " + squares.get(i).yCoord + ") cleared--- nNeeded,nFlags: " + squares.get(i).numMinesNeeded + ","
						// + squares.get(i).getNumFlagged());
						msg.clear(squares.get(i).xCoord, squares.get(i).yCoord);
						interested = true;
					}
					else if (squares.get(i).getNumAUS() + squares.get(i).getNumFlagged() == squares.get(i).numMinesNeeded)
					{
						squares.get(i).markAll();
						msg.clear(squares.get(i).xCoord, squares.get(i).yCoord);
						// System.out.println("---Chain #" + ID + " Square @ (" + squares.get(i).xCoord + ", " + squares.get(i).yCoord + ") cleared--- nNeeded,nFlags: " + squares.get(i).numMinesNeeded + ","
						// + squares.get(i).getNumFlagged());
						interested = true;
					}
					else if (squares.get(i).getNumAUS() == 0)
					{
						// System.out.println("Chain #" + ID + " should be dead and outdated.");
						interested = false;
					}
					else if (squares.get(i).getNumAUS() > 0)
					{
						// System.out.println("---Chain #" + ID + " Square @ (" + squares.get(i).xCoord + ", " + squares.get(i).yCoord + ") outdated/not solved--- nNeeded,nFlags: "
						// + squares.get(i).numMinesNeeded + "," + squares.get(i).getNumFlagged());
					}
				}
			}
		}
	}
	
	public Point findStartingPoint(int xStart, int yStart, char[][] field) // FLAG
	{
		Point out = new Point();
		ArrayList<Point> jumps = new ArrayList<Point>();
		boolean jumpBack = false;
		
		for (int i = 0; i < field.length; i++)
		{
			for (int n = 0; n < field[0].length; n++)
			{
				afterField[i][n] = field[i][n];
			}
		}
		
		char[][] tempView = new char[5][5];
		boolean done = false;
		int x = xStart;
		int y = yStart;
		
		while (!done)
		{
			// System.out.println("afterField:");
			// for (int i = 0; i < afterField.length; i++)
			// {
			// for (int n = 0; n < afterField[0].length; n++)
			// {
			// System.out.print(afterField[i][n]);
			// }
			// System.out.println("");
			// }
			
			// System.out.println("Initial tempView:");
			
			for (int j = -2; j < 3; j++) // Make a focused view
			{
				for (int k = -2; k < 3; k++)
				{
					if (isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = afterField[y + j][x + k];
						// System.out.print(tempView[j + 2][k + 2]);
					}
					else if (!isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = '\u0000';
						// System.out.print(tempView[j + 2][k + 2]);
					}
					
					if (k == 0 && j == 0)
					{
						tempView[j + 2][k + 2] = 'c';
						afterField[y + j][x + k] = 'c';
						// System.out.print(tempView[j + 2][k + 2]);
					}
				}
				
				// System.out.println("");
			}
			
			for (int j = 1; j < 4; j++) // Decide possible next squares in the chain
			{
				for (int k = 1; k < 4; k++)
				{
					if (isIndexInBounds(k, j, tempView) && tempView[j][k] == 'F')
					{
						tempView[j][k] = '.';
					}
					else if (isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
					{
						boolean notImportant = true;
						
						for (int q = -1; q < 2; q++)
						{
							for (int z = -1; z < 2; z++)
							{
								if (isIndexInBounds(k + z, j + q, tempView) && tempView[j + q][k + z] == '?')
								{
									notImportant = false;
								}
							}
						}
						
						if (notImportant)
						{
							tempView[j][k] = '.';
						}
					}
				}
			}
			
			// System.out.println("important stuff in tempView:");
			
			// for (int i = 0; i < 5; i++)
			// {
			// for (int n = 0; n < 5; n++)
			// {
			// System.out.print(tempView[i][n]);
			// }
			// System.out.println("");
			// }
			
			int numChainMembers = 0;
			for (int j = 1; j < 4; j++)
			{
				for (int k = 1; k < 4; k++)
				{
					if ((k + j) % 2 == 1 && isIndexInBounds(x + (k - 2), y + (j - 2), afterField) && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
					{
						numChainMembers++;
					}
				}
			}
			
			// System.out.println("numChainMembers: " + numChainMembers);
			
			int nextX = 0;
			int nextY = 0;
			
			if (numChainMembers > 1)
			{
				afterField[y][x] = 'c';
				boolean markJumps = false;
				
				for (int j = 1; j < 4; j++)
				{
					for (int k = 1; k < 4; k++)
					{
						if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]) && !markJumps)
						{
							// System.out.println("k + j: " + (k + j));
							nextX = x + (k - 2);
							nextY = y + (j - 2);
							tempView[j][k] = 'c';
							afterField[nextY][nextX] = 'c';
							markJumps = true;
						}
						else if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]) && markJumps)
						{
							// System.out.println("k + j: " + (k + j));
							int tempx = x + (k - 2);
							int tempy = y + (j - 2);
							tempView[j][k] = 'j';
							afterField[tempy][tempx] = 'j';
							jumps.add(new Point(tempx, tempy));
							jumpBack = true;
						}
					}
				}
				
				x = nextX;
				y = nextY;
				
				// System.out.println("jump in tempView:");
				
				// for (int i = 0; i < 5; i++)
				// {
				// for (int n = 0; n < 5; n++)
				// {
				// System.out.print(tempView[i][n]);
				// }
				// System.out.println("");
				// }
			}
			else if (numChainMembers == 1)
			{
				if (isIndexInBounds(x, y, afterField))
				{
					afterField[y][x] = 'c';
					
					loop: for (int j = 1; j < 4; j++)
					{
						for (int k = 1; k < 4; k++)
						{
							if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
							{
								// System.out.println("k + j: " + (k + j));
								x = x + (k - 2);
								y = y + (j - 2);
								afterField[y][x] = 'c';
								
								break loop;
							}
						}
					}
				}
			}
			else if (numChainMembers == 0 && jumpBack)
			{
				x = jumps.get(jumps.size() - 1).x;
				y = jumps.get(jumps.size() - 1).y;
				jumps.remove(jumps.size() - 1);
				
				if (jumps.size() == 0)
				{
					jumpBack = false;
				}
			}
			else if (numChainMembers == 0 && !jumpBack)
			{
				out = new Point(x, y);
				done = true;
			}
			else if (x == xStart && y == yStart)
			{
				out = new Point(x, y);
				done = true;
			}
		}
		
		// System.out.println("Start point: (" + x + ", " + y + ")");
		
		for (int i = 0; i < field.length; i++)
		{
			for (int n = 0; n < field[0].length; n++)
			{
				afterField[i][n] = field[i][n];
			}
		}
		
		return out;
	}
	
	public Chain(Chain c) // FLAG
	{
		minefield = c.minefield;
		
		for (int i = c.squares.size() - 1; i > -1; i--)
		{
			squares.add(c.squares.get(i));
		}
		
		afterField = new char[c.afterField.length][c.afterField[0].length];
		ID = c.ID;
	}
	
	public Chain(int xin, int yin, char[][] field, int chainID, MinesweeperGameWithAI msg) // FLAG
	{
		ArrayList<Point> jumps = new ArrayList<>();
		minefield = field;
		squares = new ArrayList<Square>();
		ID = chainID;
		
		afterField = new char[field.length][field[0].length];
		
		for (int i = 0; i < field.length; i++)
		{
			for (int n = 0; n < field[0].length; n++)
			{
				afterField[i][n] = field[i][n];
			}
		}
		
		Point p = findStartingPoint(xin, yin, field);
		
		char[][] tempView = new char[5][5];
		boolean done = false;
		int x = p.x;
		int xStart = p.x;
		int y = p.y;
		int yStart = p.y;
		squares.add(new Square(xStart, yStart, ID, field));
		afterField[y][x] = 'c';
		
		// If there is a spot the chain maker needs to jump back to
		boolean jumpBack = false;
		int xJump = 0;
		int yJump = 0;
		
		while (!done)
		{
			// System.out.println("afterField:");
			// for (int i = 0; i < afterField.length; i++)
			// {
			// for (int n = 0; n < afterField[0].length; n++)
			// {
			// System.out.print(afterField[i][n]);
			// }
			// System.out.println("");
			// }
			
			// System.out.println("Initial tempView:");
			
			for (int j = -2; j < 3; j++) // Make a focused view
			{
				for (int k = -2; k < 3; k++)
				{
					if (isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = afterField[y + j][x + k];
						// System.out.print(tempView[j + 2][k + 2]);
					}
					else if (!isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = '\u0000';
						// System.out.print(tempView[j + 2][k + 2]);
					}
					
					if (k == 0 && j == 0)
					{
						tempView[j + 2][k + 2] = 'c';
						afterField[y + j][x + k] = 'c';
						// System.out.print(tempView[j + 2][k + 2]);
					}
				}
				
				// System.out.println("");
			}
			
			for (int j = 1; j < 4; j++) // Decide possible next squares in the chain
			{
				for (int k = 1; k < 4; k++)
				{
					if (isIndexInBounds(k, j, tempView) && tempView[j][k] == 'F')
					{
						tempView[j][k] = '.';
					}
					else if (isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
					{
						boolean notImportant = true;
						
						for (int q = -1; q < 2; q++)
						{
							for (int z = -1; z < 2; z++)
							{
								if (isIndexInBounds(k + z, j + q, tempView) && tempView[j + q][k + z] == '?')
								{
									notImportant = false;
								}
							}
						}
						
						if (notImportant)
						{
							tempView[j][k] = '.';
							afterField[y + (j - 2)][x + (k - 2)] = tempView[j][k];
						}
					}
				}
			}
			
			// System.out.println("important stuff in tempView:");
			
			// for (int i = 0; i < 5; i++)
			// {
			// for (int n = 0; n < 5; n++)
			// {
			// System.out.print(tempView[i][n]);
			// }
			// System.out.println("");
			// }
			
			// Decide if there will be a jump or not. If yes, chooses one and marks the jump point. If not, chooses the next chain member.
			int numChainMembers = 0;
			for (int j = 1; j < 4; j++)
			{
				for (int k = 1; k < 4; k++)
				{
					if ((j + k) % 2 == 1 && isIndexInBounds(x + (k - 2), y + (j - 2), afterField) && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
					{
						numChainMembers++;
					}
				}
			}
			
			int nextX = 0;
			int nextY = 0;
			
			// System.out.println("numChainMembers: " + numChainMembers);
			
			if (numChainMembers > 1)
			{
				afterField[y][x] = 'c';
				boolean markJumps = false;
				
				for (int j = 1; j < 4; j++)
				{
					for (int k = 1; k < 4; k++)
					{
						if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]) && !markJumps)
						{
							nextX = x + (k - 2);
							nextY = y + (j - 2);
							// System.out.println("c @ x: " + (x + (k - 2)) + " y: " + (y + (j - 2)));
							squares.add(new Square(nextX, nextY, ID, field));
							tempView[j][k] = 'c';
							afterField[nextY][nextX] = 'c';
							markJumps = true;
						}
						else if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]) && markJumps)
						{
							xJump = (k - 2) + x;
							yJump = (j - 2) + y;
							// System.out.println("k: " + k + " j: " + j);
							// System.out.println("j @ x: " + xJump + " y: " + yJump);
							jumps.add(new Point(xJump, yJump));
							jumpBack = true;
							tempView[j][k] = 'j';
							afterField[yJump][xJump] = tempView[j][k];
						}
					}
				}
				
				x = nextX;
				y = nextY;
				
				// System.out.println("jump in tempView:");
				
				// for (int i = 0; i < 5; i++)
				// {
				// for (int n = 0; n < 5; n++)
				// {
				// System.out.print(tempView[i][n]);
				// }
				// System.out.println("");
				// }
			}
			else if (numChainMembers == 1)
			{
				if (isIndexInBounds(x, y, afterField))
				{
					afterField[y][x] = 'c';
					
					loop: for (int j = 1; j < 4; j++)
					{
						for (int k = 1; k < 4; k++)
						{
							if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
							{
								x = x + (k - 2);
								y = y + (j - 2);
								
								if (isIndexInBounds(x, y, afterField))
								{
									squares.add(new Square(x, y, ID, field));
								}
								
								break loop;
							}
						}
					}
				}
				else if (jumpBack)
				{
					x = jumps.get(jumps.size() - 1).x;
					y = jumps.get(jumps.size() - 1).y;
					jumps.remove(jumps.size() - 1);
					
					squares.add(new Square(x, y, ID, field));
					
					if (jumps.size() == 0)
					{
						jumpBack = false;
					}
				}
			}
			else if (numChainMembers == 0 && jumpBack)
			{
				afterField[y][x] = 'c';
				
				x = jumps.get(0).x;
				y = jumps.get(0).y;
				jumps.remove(0);
				
				if (jumps.size() == 0)
				{
					jumpBack = false;
				}
			}
			else if (numChainMembers == 0 && !jumpBack)
			{
				afterField[y][x] = 'c';
				
				// System.out.println("isIncluded: " + ((x != xStart && y != yStart) || (x != prevX && y != prevY)));
				squares.add(new Square(x, y, ID, field));
				
				done = true;
				
				// System.out.println("afterField:");
				// for (int i = 0; i < afterField.length; i++)
				// {
				// for (int n = 0; n < afterField[0].length; n++)
				// {
				// System.out.print(afterField[i][n]);
				// }
				// System.out.println("");
				// }
			}
		}
		
		for (int i = 0; i < afterField.length; i++)
		{
			for (int n = 0; n < afterField[0].length; n++)
			{
				if (afterField[i][n] == 'c')
				{
					for (int j = -1; j < 2; j++) // Make a focused view
					{
						for (int k = -1; k < 2; k++)
						{
							if (isIndexInBounds(n + k, i + j, afterField) && !msg.idField[i + j][n + k].contains(ID))
							{
								msg.idField[i + j][n + k].add(ID);
							}
						}
					}
				}
			}
		}
		
		// Get rid of duplicate squares
		for (int i = 0; i < squares.size(); i++)
		{
			for (int n = 0; n < squares.size(); n++)
			{
				if (squares.get(i).sameAs(squares.get(n)) && i != n)
				{
					squares.remove(n);
					break;
				}
			}
		}
	}
	
	public Chain(int xin, int yin, char[][] field) // FLAG
	{
		ArrayList<Point> jumps = new ArrayList<>();
		minefield = field;
		squares = new ArrayList<Square>();
		
		afterField = new char[field.length][field[0].length];
		
		for (int i = 0; i < field.length; i++)
		{
			for (int n = 0; n < field[0].length; n++)
			{
				afterField[i][n] = field[i][n];
			}
		}
		
		Point p = findStartingPoint(xin, yin, field);
		
		char[][] tempView = new char[5][5];
		boolean done = false;
		int x = p.x;
		int xStart = p.x;
		int prevX = 0;
		int y = p.y;
		int yStart = p.y;
		int prevY = 0;
		squares.add(new Square(xStart, yStart, ID, field));
		afterField[y][x] = 'c';
		
		// If there is a spot the chain maker needs to jump back to
		boolean jumpBack = false;
		int xJump = 0;
		int yJump = 0;
		
		while (!done)
		{
			// System.out.println("afterField:");
			// for (int i = 0; i < afterField.length; i++)
			// {
			// for (int n = 0; n < afterField[0].length; n++)
			// {
			// System.out.print(afterField[i][n]);
			// }
			// System.out.println("");
			// }
			
			// System.out.println("Initial tempView:");
			
			for (int j = -2; j < 3; j++) // Make a focused view
			{
				for (int k = -2; k < 3; k++)
				{
					if (isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = afterField[y + j][x + k];
						// System.out.print(tempView[j + 2][k + 2]);
					}
					else if (!isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = '\u0000';
						// System.out.print(tempView[j + 2][k + 2]);
					}
					
					if (k == 0 && j == 0)
					{
						tempView[j + 2][k + 2] = 'c';
						afterField[y + j][x + k] = 'c';
						// System.out.print(tempView[j + 2][k + 2]);
					}
				}
				
				// System.out.println("");
			}
			
			for (int j = 1; j < 4; j++) // Decide possible next squares in the chain
			{
				for (int k = 1; k < 4; k++)
				{
					if (isIndexInBounds(k, j, tempView) && tempView[j][k] == 'F')
					{
						tempView[j][k] = '.';
					}
					else if (isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
					{
						boolean notImportant = true;
						
						for (int q = -1; q < 2; q++)
						{
							for (int z = -1; z < 2; z++)
							{
								if (isIndexInBounds(k + z, j + q, tempView) && tempView[j + q][k + z] == '?')
								{
									notImportant = false;
								}
							}
						}
						
						if (notImportant)
						{
							tempView[j][k] = '.';
							afterField[y + (j - 2)][x + (k - 2)] = tempView[j][k];
						}
					}
				}
			}
			
			// System.out.println("important stuff in tempView:");
			
			// for (int i = 0; i < 5; i++)
			// {
			// for (int n = 0; n < 5; n++)
			// {
			// System.out.print(tempView[i][n]);
			// }
			// System.out.println("");
			// }
			
			// Decide if there will be a jump or not. If yes, chooses one and marks the jump point. If not, chooses the next chain member.
			int numChainMembers = 0;
			for (int j = 1; j < 4; j++)
			{
				for (int k = 1; k < 4; k++)
				{
					if ((k + j) % 2 == 1 && isIndexInBounds(x + (k - 2), y + (j - 2), afterField) && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
					{
						numChainMembers++;
					}
				}
			}
			
			// System.out.println("numChainMembers: " + numChainMembers);
			
			int nextX = 0;
			int nextY = 0;
			
			if (numChainMembers > 1)
			{
				afterField[y][x] = 'c';
				boolean markJumps = false;
				
				for (int j = 1; j < 4; j++)
				{
					for (int k = 1; k < 4; k++)
					{
						if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]) && !markJumps)
						{
							nextX = x + (k - 2);
							nextY = y + (j - 2);
							// System.out.println("c @ x: " + x + " y: " + y);
							squares.add(new Square(nextX, nextY, ID, field));
							tempView[j][k] = 'c';
							afterField[nextY][nextX] = 'c';
							markJumps = true;
						}
						else if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]) && markJumps)
						{
							xJump = (k - 2) + x;
							yJump = (j - 2) + y;
							// System.out.println("j @ x: " + xJump + " y: " + yJump);
							jumps.add(new Point(xJump, yJump));
							jumpBack = true;
							tempView[j][k] = 'j';
							afterField[yJump][xJump] = tempView[j][k];
						}
					}
				}
				
				prevX = x;
				prevY = y;
				x = nextX;
				y = nextY;
				
				// System.out.println("jump in tempView:");
				//
				// for (int i = 0; i < 5; i++)
				// {
				// for (int n = 0; n < 5; n++)
				// {
				// System.out.print(tempView[i][n]);
				// }
				// System.out.println("");
				// }
			}
			else if (numChainMembers == 1)
			{
				if (isIndexInBounds(x, y, afterField))
				{
					afterField[y][x] = 'c';
					
					loop: for (int j = 1; j < 4; j++)
					{
						for (int k = 1; k < 4; k++)
						{
							if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && Character.isDigit(tempView[j][k]))
							{
								prevX = x;
								prevY = y;
								x = x + (k - 2);
								y = y + (j - 2);
								squares.add(new Square(x, y, ID, field));
								
								break loop;
							}
						}
					}
				}
				else if (jumpBack)
				{
					prevX = x;
					prevY = y;
					x = jumps.get(jumps.size() - 1).x;
					y = jumps.get(jumps.size() - 1).y;
					jumps.remove(jumps.size() - 1);
					
					if (jumps.size() == 0)
					{
						jumpBack = false;
					}
				}
			}
			else if (numChainMembers == 0 && jumpBack)
			{
				afterField[y][x] = 'c';
				
				prevX = x;
				prevY = y;
				x = jumps.get(jumps.size() - 1).x;
				y = jumps.get(jumps.size() - 1).y;
				jumps.remove(jumps.size() - 1);
				squares.add(new Square(x, y, ID, field));
				
				if (jumps.size() == 0)
				{
					jumpBack = false;
				}
			}
			else if (numChainMembers == 0 && !jumpBack)
			{
				afterField[y][x] = 'c';
				
				if (x != prevX && y != prevY)
				{
					squares.add(new Square(x, y, ID, field));
				}
				
				done = true;
				
				// System.out.println("afterField:");
				// for (int i = 0; i < afterField.length; i++)
				// {
				// for (int n = 0; n < afterField[0].length; n++)
				// {
				// System.out.print(afterField[i][n]);
				// }
				// System.out.println("");
				// }
			}
		}
		
		for (int i = 0; i < squares.size(); i++)
		{
			for (int n = 0; n < squares.size(); n++)
			{
				if (squares.get(i).sameAs(squares.get(n)) && i != n)
				{
					squares.remove(n);
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) // FLAG
	{
		int testNum = 4;
		
		if (testNum == 0)
		{
			MinesweeperGameWithAI msg = new MinesweeperGameWithAI();
			msg.makeNewField(20, 20, 0);
			msg.reveal(10, 10);
			
			System.out.println("revealedField: ");
			
			for (int i = 0; i < msg.masterField.length; i++)
			{
				for (int n = 0; n < msg.masterField[0].length; n++)
				{
					System.out.print(msg.revealedField[i][n]);
				}
				System.out.println("");
			}
			
			msg.makeChain();
			
			if (msg.chainHash.size() > 0 && msg.chainHash.get(0).squares.size() > 0)
			{
				System.out.println("squares.size(): " + msg.chainHash.get(0).squares.size());
				
				System.out.println("afterField @1127:");
				for (int i = 0; i < msg.chainHash.get(0).afterField.length; i++)
				{
					for (int n = 0; n < msg.chainHash.get(0).afterField[0].length; n++)
					{
						System.out.print(msg.chainHash.get(0).afterField[i][n]);
					}
					System.out.println("");
				}
			}
			
			System.out.println("canMakeChain: " + msg.canMakeChain());
			
			if (msg.canMakeChain())
			{
				msg.makeChain();
			}
			
			System.out.println("IDField:");
			for (int i = 0; i < msg.masterField.length; i++)
			{
				for (int n = 0; n < msg.masterField[0].length; n++)
				{
					System.out.print(msg.idField[i][n].size());
				}
				System.out.println("");
			}
			
			msg.chainHash.get(0).evaluateChain(msg, true);
			
			System.out.println("After eval:");
			for (int i = 0; i < msg.masterField.length; i++)
			{
				for (int n = 0; n < msg.masterField[0].length; n++)
				{
					System.out.print(msg.revealedField[i][n]);
				}
				System.out.println("");
			}
			
			System.out.println("IDField:");
			for (int i = 0; i < msg.masterField.length; i++)
			{
				for (int n = 0; n < msg.masterField[0].length; n++)
				{
					System.out.print(msg.idField[i][n].size());
				}
				System.out.println("");
			}
		}
		else if (testNum == 1)
		{
			char[][] field = { { '0', '0', '1', '?', '?', '?', '?', '?', '?', '?' }, { '0', '0', '1', '?', '1', '1', '?', '?', '?', '?' }, { '0', '0', '1', '1', '1', '1', '?', '?', '?', '?' },
					{ '0', '0', '1', '?', '1', '1', '?', '?', '?', '?' }, { '1', '2', '2', '?', '?', '?', '?', '?', '?', '?' }, { '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' },
					{ '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' }, { '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' }, { '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' },
					{ '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' }, };
			Chain c = new Chain(2, 0, field);
			
			System.out.println("squares.size() avant: " + c.squares.size());
			
			ArrayList<Square> removeList = new ArrayList<Square>();
			removeList.addAll(c.squares);
			
			c.squares.removeAll(removeList);
			System.out.println("squares.size() apres: " + c.squares.size());
		}
		else if (testNum == 3)
		{
			char[][] field = { { '0', '0', '1', '?', '?', '?', '?', '?', '?', '?' }, { '0', '0', '1', '?', '1', '1', '?', '?', '?', '?' }, { '0', '0', '1', '1', '1', '1', '?', '?', '?', '?' },
					{ '0', '0', '1', '?', '1', '1', '?', '?', '?', '?' }, { '1', '2', '2', '?', '?', '?', '?', '?', '?', '?' }, { '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' },
					{ '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' }, { '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' }, { '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' },
					{ '?', '?', '?', '?', '?', '?', '?', '?', '?', '?' }, };
			Square s1 = new Square(2, 1, 0, field);
			Square s2 = new Square(2, 1, 0, field);
			System.out.println(s1.sameAs(s2));
		}
		else if (testNum == 4)
		{
			MinesweeperGameWithAI msg = new MinesweeperGameWithAI();
			msg.makeNewField(20, 20, 0);
			msg.reveal(0, 0);
			boolean over = false;
			
			while (msg.canMakeChain() && !over)
			{
				msg.makeChain();
				
				/**
				 * System.out.println("IDField:");
				 * for (int i = 0; i < msg.masterField.length; i++)
				 * {
				 * for (int n = 0; n < msg.masterField[0].length; n++)
				 * {
				 * System.out.print(msg.idField[i][n].size());
				 * }
				 * System.out.println("");
				 * }
				 * System.out.println("");
				 * 
				 * for (int i = 0; i < msg.masterField.length; i++)
				 * {
				 * for (int n = 0; n < msg.masterField[0].length; n++)
				 * {
				 * System.out.print("(");
				 * for (int j = 0; j < msg.idField[i][n].size(); j++)
				 * {
				 * System.out.print(msg.idField[i][n].get(j) + ",");
				 * }
				 * System.out.print(")");
				 * }
				 * System.out.println("");
				 * }
				 * 
				 * System.out.println("max eval label: " + msg.validLabels.get(msg.validLabels.size() - 1));
				 **/
				
				over = msg.validLabels.get(msg.validLabels.size() - 1) >= 100;
				// System.out.println("isOverLimit: " + over);
				
				for (int i = msg.validLabels.size() - 1; i > -1; i--)
				{
					if (isIndexValid(i, msg.validLabels))
					{
						// System.out.println("Evaluating chain#" + msg.validLabels.get(i) + "...");
						msg.chainHash.get(msg.validLabels.get(i)).evaluateChain(msg, true);
					}
				}
				
				/**
				 * System.out.println("revealedField:");
				 * for (int i = 0; i < msg.masterField.length; i++)
				 * {
				 * for (int n = 0; n < msg.masterField[0].length; n++)
				 * {
				 * System.out.print(msg.revealedField[i][n]);
				 * }
				 * System.out.println("");
				 * }
				 * 
				 * System.out.println("canMakeChain: " + msg.canMakeChain());
				 **/
			}
			
			System.out.println(MinesweeperGame.ended + " " + msg.finished);
			
			for (int i = 0; i < msg.masterField.length; i++)
			{
				for (int n = 0; n < msg.masterField[0].length; n++)
				{
					System.out.print(msg.revealedField[i][n]);
				}
				System.out.println();
			}
		}
	}
}