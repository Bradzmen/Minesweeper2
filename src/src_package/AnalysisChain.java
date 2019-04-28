package src_package;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AnalysisChain
{
	ArrayList<SimpleSquare> squares;
	int ID;
	char[][] minefield;
	char[][] afterField;
	
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
	
	public static char[][] fieldReader() throws IOException // FLAG
	{
		Scanner scan = new Scanner(Paths.get("tests/chain_tests/2019-02-10-18-40-58/31.txt"));
		HashMap<Integer, char[]> rowHash = new HashMap<Integer, char[]>();
		int counter = 0;
		String toParse = scan.next();
		int length = toParse.length();
		
		while (scan.hasNext())
		{
			rowHash.put(counter, new char[length]);
			
			for (int i = 0; i < length; i++)
			{
				rowHash.get(counter)[i] = toParse.charAt(i);
			}
			
			toParse = scan.next();
			length = toParse.length();
			counter++;
		}
		
		char[][] out = new char[rowHash.size()][length];
		
		for (int i = 0; i < rowHash.size(); i++)
		{
			for (int n = 0; n < length; n++)
			{
				out[i][n] = rowHash.get(i)[n];
			}
		}
		
		scan.close();
		return out;
	}
	
	public Point findStartingPoint(int xStart, int yStart, MinesweeperGameWithAI msg) // FLAG
	{
		char[][] field = msg.revealedField;
		Point out = new Point();
		ArrayList<Point> jumps = new ArrayList<Point>();
		ArrayList<Integer> toCheck = new ArrayList<Integer>();
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
			System.out.println("afterField:");
			for (int i = 0; i < afterField.length; i++)
			{
				for (int n = 0; n < afterField[0].length; n++)
				{
					System.out.print(afterField[i][n]);
				}
				System.out.println("");
			}
			System.out.println("Initial tempView:");
			
			for (int j = -2; j < 3; j++) // Make a focused view
			{
				for (int k = -2; k < 3; k++)
				{
					if (isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = afterField[y + j][x + k];
						System.out.print(tempView[j + 2][k + 2]);
					}
					else if (!isIndexInBounds(x + k, y + j, afterField) && !(j == 0 && k == 0))
					{
						tempView[j + 2][k + 2] = '\u0000';
						System.out.print(tempView[j + 2][k + 2]);
					}
					
					if (k == 0 && j == 0)
					{
						tempView[j + 2][k + 2] = 'c';
						afterField[y + j][x + k] = 'c';
						System.out.print(tempView[j + 2][k + 2]);
					}
				}
				
				System.out.println("");
			}
			
			for (int j = 1; j < 4; j++) // Decide possible next squares in the chain
			{
				for (int k = 1; k < 4; k++)
				{
					if (isIndexInBounds(k, j, tempView) && tempView[j][k] == 'F')
					{
						tempView[j][k] = '.';
					}
					else if (isIndexInBounds(k, j, tempView) && tempView[j][k] == '?')
					{
						boolean notImportant = true;
						
						if (msg.idField[y + (j - 2)][x + (k - 2)].size() > 0)
						{
							notImportant = false;
						}
						
						if (notImportant)
						{
							tempView[j][k] = '.';
						}
					}
				}
			}
			
			System.out.println("important stuff in tempView:");
			
			for (int i = 0; i < 5; i++)
			{
				for (int n = 0; n < 5; n++)
				{
					System.out.print(tempView[i][n]);
				}
				System.out.println("");
			}
			
			int numChainMembers = 0;
			for (int j = 1; j < 4; j++)
			{
				for (int k = 1; k < 4; k++)
				{
					if ((k + j) % 2 == 1 && isIndexInBounds(x + (k - 2), y + (j - 2), afterField) && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?')
					{
						numChainMembers++;
					}
				}
			}
			
			System.out.println("numChainMembers: " + numChainMembers);
			
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
						if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?' && !markJumps)
						{
							// System.out.println("k + j: " + (k + j));
							nextX = x + (k - 2);
							nextY = y + (j - 2);
							tempView[j][k] = 'c';
							afterField[nextY][nextX] = 'c';
							markJumps = true;
						}
						else if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?' && markJumps)
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
				
				System.out.println("jump in tempView:");
				
				for (int i = 0; i < 5; i++)
				{
					for (int n = 0; n < 5; n++)
					{
						System.out.print(tempView[i][n]);
					}
					System.out.println("");
				}
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
							if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?')
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
				for (int i = 0; i < squares.size(); i++)
				{
					int tempx = squares.get(i).x;
					int tempy = squares.get(i).y;
					
					if (msg.idField[tempy][tempx].size() > 0)
					{
						for (int n = 0; n < msg.idField[tempy][tempx].size(); n++)
						{
							if (!toCheck.contains(msg.idField[tempy][tempx].get(n)))
							{
								toCheck.add(msg.idField[tempy][tempx].get(n));
							}
						}
					}
				}
				
				out = new Point(x, y);
				done = true;
			}
			else if (x == xStart && y == yStart)
			{
				out = new Point(x, y);
				done = true;
			}
		}
		
		System.out.println("Start point: (" + x + ", " + y + ")");
		
		for (int i = 0; i < field.length; i++)
		{
			for (int n = 0; n < field[0].length; n++)
			{
				afterField[i][n] = field[i][n];
			}
		}
		
		return out;
	}
	
	public AnalysisChain(AnalysisChain ac) // FLAG Constructor
	{
		minefield = ac.minefield;
		squares = new ArrayList<SimpleSquare>();
		
		for (int i = ac.squares.size() - 1; i > -1; i--)
		{
			squares.add(ac.squares.get(i));
		}
		
		afterField = new char[ac.afterField.length][ac.afterField[0].length];
		ID = ac.ID;
	}
	
	public AnalysisChain(ArrayList<Point> plist, MinesweeperGameWithAI msg)
	{
		squares = new ArrayList<SimpleSquare>();
		
		for (int i = 0; i < plist.size(); i++)
		{
			squares.add(new SimpleSquare(msg.revealedField, plist.get(i).x, plist.get(i).y, 0));
		}
		
		int fieldx = msg.revealedField[0].length;
		int fieldy = msg.revealedField.length;
		
		afterField = new char[fieldy][fieldx];
		minefield = msg.revealedField;
		ArrayList<Point> jumps = new ArrayList<>();
		ID = 0;
	}
	
	public AnalysisChain(int xin, int yin, char[][] inField, int id, MinesweeperGameWithAI msg) // FLAG Constructor
	{
		int fieldx = inField[0].length;
		int fieldy = inField.length;
		
		afterField = new char[fieldy][fieldx];
		minefield = inField;
		ArrayList<Point> jumps = new ArrayList<>();
		squares = new ArrayList<SimpleSquare>();
		ID = id;
		
		for (int i = 0; i < minefield.length; i++)
		{
			for (int n = 0; n < minefield[0].length; n++)
			{
				afterField[i][n] = minefield[i][n];
			}
		}
		
		Point p = findStartingPoint(xin, yin, msg);
		
		char[][] tempView = new char[5][5];
		boolean done = false;
		int x = p.x;
		int xStart = p.x;
		int prevX = 0;
		int y = p.y;
		int yStart = p.y;
		int prevY = 0;
		squares.add(new SimpleSquare(minefield, xStart, yStart, ID));
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
					else if (isIndexInBounds(k, j, tempView) && tempView[j][k] == '?')
					{
						boolean notImportant = true;
						
						for (int q = -1; q < 2; q++)
						{
							for (int z = -1; z < 2; z++)
							{
								if (isIndexInBounds(k + z, j + q, tempView) && Character.isDigit(tempView[j + q][k + z]))
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
			
			// Decide if there will be a jump or not. If yes, chooses one and marks the jump point. If not, chooses the next chain member.
			int numChainMembers = 0;
			for (int j = 1; j < 4; j++)
			{
				for (int k = 1; k < 4; k++)
				{
					if ((k + j) % 2 == 1 && isIndexInBounds(x + (k - 2), y + (j - 2), afterField) && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?')
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
						if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?' && !markJumps)
						{
							nextX = x + (k - 2);
							nextY = y + (j - 2);
							// System.out.println("c @ x: " + x + " y: " + y);
							squares.add(new SimpleSquare(minefield, nextX, nextY, ID));
							tempView[j][k] = 'c';
							afterField[nextY][nextX] = 'c';
							markJumps = true;
						}
						else if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?' && markJumps)
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
							if ((k + j) % 2 == 1 && isIndexInBounds(k, j, tempView) && tempView[j][k] == '?')
							{
								prevX = x;
								prevY = y;
								x = x + (k - 2);
								y = y + (j - 2);
								squares.add(new SimpleSquare(minefield, x, y, ID));
								
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
				squares.add(new SimpleSquare(minefield, x, y, ID));
				
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
					squares.add(new SimpleSquare(minefield, x, y, ID));
				}
				
				done = true;
				
				System.out.println("afterField:");
				for (int i = 0; i < afterField.length; i++)
				{
					for (int n = 0; n < afterField[0].length; n++)
					{
						System.out.print(afterField[i][n]);
					}
					System.out.println("");
				}
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
		
		for (int i = 0; i < squares.size(); i++)
		{
			int X = squares.get(i).x;
			int Y = squares.get(i).y;
			
			for (int j = -1; j < 2; j++)
			{
				for (int k = -1; k < 2; k++)
				{
					if (isIndexInBounds(X + k, Y + j, minefield) && !msg.idFieldChains[Y + j][X + k].contains(ID))
					{
						msg.idFieldChains[Y + j][X + k].add(ID);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException // FLAG
	{
		char[][] toPrint = null;
		
		toPrint = AnalysisChain.fieldReader();
		
		for (int i = 0; i < toPrint.length; i++)
		{
			for (int n = 0; n < toPrint[0].length; n++)
			{
				System.out.print(toPrint[i][n]);
			}
			System.out.println();
		}
		
		System.out.println(toPrint[0].length);
		
		MinesweeperGameWithAI msg = new MinesweeperGameWithAI();
		msg.makeNewField(AnalysisChain.fieldReader());
		
		AnalysisChain ac = new AnalysisChain(0, 0, AnalysisChain.fieldReader(), 5, msg);
		
		System.out.println("idFieldChains:");
		for (int i = 0; i < msg.idFieldChains.length; i++)
		{
			for (int n = 0; n < msg.idFieldChains[0].length; n++)
			{
				System.out.print(msg.idFieldChains[i][n].size());
			}
			System.out.println("");
		}
	}
}
