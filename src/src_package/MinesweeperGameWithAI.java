package src_package;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MinesweeperGameWithAI extends MinesweeperGame
{
	HashMap<Integer, Chain> chainHash;
	HashMap<Integer, AnalysisChain> analysisChainHash; // Used as a holder for all cases, not the original.
	AnalysisChain ac;
	ArrayList<Integer> validLabels;
	ArrayList<Integer>[][] idField;
	ArrayList<Integer>[][] idFieldChains;
	double[][] probField; // Holds the probabilities after a complex analysis.
	int nextID;
	int nextChainID;
	Timer timer; // TODO AI does tasks regularly
	TimerTask timerTask;
	boolean evaluating = false;
	
	public boolean containsValidID(ArrayList<Integer> ids)
	{
		boolean out = false;
		for (int i = 0; i < ids.size(); i++)
		{
			if (!invalidLabels.contains(ids.get(i)))
			{
				out = true;
				break;
			}
		}
		return out;
	}
	
	@Override public void reveal(int x, int y)
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
		
		for (int i = 0; i < idField[y][x].size(); i++)
		{
			if (idField[y][x].size() > 0 && !chainHash.get(idField[y][x].get(i)).evaluatingChain)
			{
				int id = idField[y][x].get(i);
				chainHash.remove(id);
				invalidLabels.add(id);
				validLabels.removeAll(invalidLabels);
				for (int j = 0; j < idField.length; j++)
				{
					for (int n = 0; n < idField[0].length; n++)
					{
						if (idField[j][n].size() > 0)
						{
							idField[j][n].removeAll(invalidLabels);
						}
					}
				}
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
	
	public boolean canMakeChain() // Note: If you change the decision making for this method, you MUST change it in makeChain()!
	{
		boolean out = false;
		
		for (int i = 0; i < revealedField.length; i++)
		{
			for (int n = 0; n < revealedField[0].length; n++)
			{
				boolean important = false;
				
				for (int j = -1; j < 2; j++)
				{
					for (int k = -1; k < 2; k++)
					{
						if (isIndexInBounds(n + k, i + j, revealedField) && !(j == 0 && k == 0) && revealedField[i + j][n + k] == '?')
						{
							important = true;
						}
					}
				}
				
				boolean isSquare = false;
				loop: for (int j = 0; j < idField[i][n].size(); j++)
				{
					for (int k = 0; k < chainHash.get(idField[i][n].get(j)).squares.size(); k++)
					{
						isSquare = chainHash.get(idField[i][n].get(j)).squares.get(k).sameAs(new Square(n, i, idField[i][n].get(j), revealedField));
						
						if (isSquare)
						{
							break loop;
						}
					}
				}
				
				if (Character.isDigit(revealedField[i][n]) && important && (idField[i][n].size() == 0 || !isSquare))
				{
					out = true;
				}
			}
		}
		
		if (ended || finished)
		{
			out = false;
		}
		
		return out;
	}
	
	public void makeChain() // FLAG makeChain
	{
		search: for (int i = 0; i < revealedField.length; i++)
		{
			for (int n = 0; n < revealedField[0].length; n++)
			{
				boolean important = false;
				
				for (int j = -1; j < 2; j++)
				{
					for (int k = -1; k < 2; k++)
					{
						if (isIndexInBounds(n + k, i + j, revealedField) && !(j == 0 && k == 0) && revealedField[i + j][n + k] == '?')
						{
							important = true;
						}
					}
				}
				
				boolean isSquare = false;
				loop: for (int j = 0; j < idField[i][n].size(); j++)
				{
					for (int k = 0; k < chainHash.get(idField[i][n].get(j)).squares.size(); k++)
					{
						isSquare = chainHash.get(idField[i][n].get(j)).squares.get(k).sameAs(new Square(n, i, idField[i][n].get(j), revealedField));
						
						if (isSquare)
						{
							break loop;
						}
					}
				}
				
				if (Character.isDigit(revealedField[i][n]) && (idField[i][n].size() == 0 || !isSquare) && important)
				{
					chainHash.put(nextID, new Chain(n, i, revealedField, nextID, this));
					validLabels.add(nextID);
					nextID++;
					break search;
				}
			}
		}
	}
	
	public boolean isAdjacentToChain(int x, int y, char[][] field) // FLAG isAdjacentToChain Probably will not need this method.
	{
		boolean out = false;
		char c = field[y][x];
		
		loop: for (int i = 0; i < validLabels.size(); i++)
		{
			for (int n = 0; n < chainHash.get(validLabels.get(i)).squares.size(); n++)
			{
				if (chainHash.get(validLabels.get(i)).squares.get(n).adjacentSquares[y][x] == c)
				{
					boolean verified1 = false;
					boolean verified2 = false;
					loop2: for (int j = -1; j < 2; j++)
					{
						for (int k = -1; k < 2; k++)
						{
							if (isIndexInBounds(x + k, y + j, chainHash.get(validLabels.get(i)).squares.get(n).adjacentSquares))
							{
								if (field[y + j][x + k] == chainHash.get(validLabels.get(i)).squares.get(n).adjacentSquares[y + j][x + k])
								{
									if (!verified1)
									{
										verified1 = true;
									}
									else if (verified1)
									{
										verified2 = true;
										break loop2;
									}
								}
							}
						}
					}
					
					if (verified1 && verified2)
					{
						out = true;
						break loop;
					}
				}
			}
		}
		
		return out;
	}
	
	public ArrayList<Integer> getAdjacentChainID(int x, int y, char[][] field) // FLAG getAdjacentChainID Probably will not need this method.
	{
		ArrayList<Integer> out = new ArrayList<Integer>();
		char c = field[y][x];
		
		for (int i = 0; i < validLabels.size(); i++)
		{
			loop: for (int n = 0; n < chainHash.get(validLabels.get(i)).squares.size(); n++)
			{
				if (chainHash.get(validLabels.get(i)).squares.get(n).adjacentSquares[y][x] == c)
				{
					boolean verified1 = false;
					boolean verified2 = false;
					loop2: for (int j = -1; j < 2; j++)
					{
						for (int k = -1; k < 2; k++)
						{
							if (isIndexInBounds(x + k, y + j, chainHash.get(validLabels.get(i)).squares.get(n).adjacentSquares))
							{
								if (field[y + j][x + k] == chainHash.get(validLabels.get(i)).squares.get(n).adjacentSquares[y + j][x + k])
								{
									if (!verified1)
									{
										verified1 = true;
									}
									else if (verified1)
									{
										verified2 = true;
										break loop2;
									}
								}
							}
						}
					}
					
					if (verified1 && verified2)
					{
						out.add(chainHash.get(validLabels.get(i)).ID);
						break loop;
					}
				}
			}
		}
		
		return out;
	}
	
	public boolean canMakeAnalysisChain() // FLAG canMakeAnalysisChain Probably will not need this method.
	{
		boolean out = false;
		
		looop: for (int i = 0; i < revealedField.length; i++)
		{
			for (int n = 0; n < revealedField[0].length; n++)
			{
				if (revealedField[i][n] == '?')
				{
					boolean important = false;
					
					for (int j = -1; j < 2; j++)
					{
						for (int k = -1; k < 2; k++)
						{
							if (isIndexInBounds(n + k, i + j, revealedField) && !(j == 0 && k == 0) && Character.isDigit(revealedField[i + j][n + k]))
							{
								important = true;
							}
						}
					}
					
					boolean isSquare = false;
					loop: for (int j = 0; j < idFieldChains[i][n].size(); j++)
					{
						for (int k = 0; k < analysisChainHash.get(idFieldChains[i][n].get(j)).squares.size(); k++)
						{
							isSquare = analysisChainHash.get(idFieldChains[i][n].get(j)).squares.get(k).sameAs(new SimpleSquare(revealedField, n, i, idField[i][n].get(j)));
							
							if (isSquare)
							{
								break loop;
							}
						}
					}
					
					if (important && (idFieldChains[i][n].size() == 0 || !isSquare))
					{
						out = true;
						break looop;
					}
				}
			}
		}
		
		if (ended || finished)
		{
			out = false;
		}
		
		return out;
	}
	
	public void makeAnalysisChain() // FLAG makeAnalysisChain
	{
		ArrayList<Point> toAdd = new ArrayList<Point>();
		
		for (int c = 0; c < validLabels.size(); c++)
		{
			for (int s = 0; s < chainHash.get(validLabels.get(c)).squares.size(); s++)
			{
				ArrayList<Point> temp = chainHash.get(validLabels.get(c)).squares.get(s).getAUS();
				
				for (int i = 0; i < temp.size(); i++)
				{
					if (!toAdd.contains(temp.get(i)))
					{
						toAdd.add(temp.get(i));
					}
				}
			}
		}
		
		ac = new AnalysisChain(toAdd, this);
	}
	
	public void reveal(int x, int y, char[][] field)
	{
		if (revealedField[y][x] != 'F')
		{
			if (field[y][x] == 'M')
			{
				revealedField[y][x] = 'X';
				mineHit(x, y);
			}
			else if (field[y][x] == '0')
			{
				revealedField[y][x] = '.';
				clear(x, y);
			}
			else
			{
				revealedField[y][x] = field[y][x];
			}
		}
	}
	
	public char[][] makeCopyField()
	{
		char[][] pseudoField = new char[masterField.length][masterField[0].length];
		for (int i = 0; i < masterField.length; i++)
		{
			for (int n = 0; n < masterField[0].length; n++)
			{
				pseudoField[i][n] = revealedField[i][n];
			}
		}
		
		return pseudoField;
	}
	
	public int getNumAUSLeft()
	{
		int out = 0;
		
		for (int i = 0; i < masterField.length; i++)
		{
			for (int n = 0; n < masterField[0].length; n++)
			{
				if (revealedField[i][n] == '?')
				{
					out++;
				}
			}
		}
		
		return out;
	}
	
	public void doComplexAnalysis() // TODO When guessing on the lowest probabilities, have it guess on the one next to the most adjacent '?'.
	{
		boolean doAnalysis = true;
		
		if (numMinesLeft == 0)
		{
			for (int i = 0; i < revealedField.length; i++)
			{
				for (int n = 0; n < revealedField[0].length; n++)
				{
					if (revealedField[i][n] == '?')
					{
						reveal(n, i);
					}
				}
			}
			
			doAnalysis = false;
		}
		else
		{
			int numLeft = 0;
			
			for (int i = 0; i < revealedField.length; i++)
			{
				for (int n = 0; n < revealedField[0].length; n++)
				{
					if (revealedField[i][n] == '?')
					{
						numLeft++;
					}
				}
			}
			
			if (numLeft == numMinesLeft)
			{
				for (int i = 0; i < revealedField.length; i++)
				{
					for (int n = 0; n < revealedField[0].length; n++)
					{
						if (revealedField[i][n] == '?')
						{
							flag(n, i);
						}
					}
				}
			}
			
			doAnalysis = false;
		}
		
		if (doAnalysis)
		{
			ArrayList<AnalysisChain> chainHolder = new ArrayList<AnalysisChain>();
			HashMap<SimpleSquare, Integer> squareToID = new HashMap<SimpleSquare, Integer>();
			HashMap<Integer, SimpleSquare> idToSquare = new HashMap<Integer, SimpleSquare>();
			boolean done = false;
			int counter = 0;
			
			char[][] pseudoField = new char[masterField.length][masterField[0].length];
			for (int i = 0; i < masterField.length; i++)
			{
				for (int n = 0; n < masterField[0].length; n++)
				{
					pseudoField[i][n] = '^';
				}
			}
			
			char[][] masterCopy = new char[masterField.length][masterField[0].length];
			{
				for (int i = 0; i < masterField.length; i++)
				{
					for (int n = 0; n < masterField[0].length; n++)
					{
						masterCopy[i][n] = masterField[i][n];
					}
				}
			}
			
			masterField = pseudoField;
			
			makeAnalysisChain();
			
			for (int n = 0; n < ac.squares.size(); n++)
			{
				squareToID.put(ac.squares.get(n), n);
				idToSquare.put(n, ac.squares.get(n));
			}
			
			char[][] revealedCopyField = makeCopyField();
			
			while (!done) // TODO Implement backwards operator, repeat(putting one mine down -> evaluate) until the end, then work backwards
			{
				AnalysisChain acCopyMaster = new AnalysisChain(ac);
				chainHolder.add(acCopyMaster);
				
				boolean iterating = true;
				int squareNum = 0;
				
				while (iterating)
				{
					int x = acCopyMaster.squares.get(squareNum).x;
					int y = acCopyMaster.squares.get(squareNum).y;
					flag(x, y);
					
					for (int i = 0; i < idField[y][x].size(); i++)
					{
						for (int n = 0; n < 5; n++) // 5 is arbitrary. I could make a system to detect when no change occurs, but that takes more time.
						{
							chainHash.get(idField[y][x].get(i)).evaluateChain(this, false);
						}
					}
					
					ArrayList<Integer> toRemove = new ArrayList<Integer>();
					
					for (int i = 0; i < acCopyMaster.squares.size(); i++)
					{
						if (revealedField[acCopyMaster.squares.get(i).y][acCopyMaster.squares.get(i).x] == '^')
						{
							toRemove.add(i);
						}
					}
					
					for (int i = toRemove.size() - 1; i > -1; i--)
					{
						acCopyMaster.squares.remove(toRemove.get(i));
					}
					
					squareNum++;
					
					if (squareNum == acCopyMaster.squares.size())
					{
						iterating = false;
					}
				}
				
				analysisChainHash.put(counter, acCopyMaster);
				counter++;
				
				int backwardCounter = 1;
				iterating = true;
				
				while (iterating)
				{
					revealedField = revealedCopyField;
					int index = squareToID.get(acCopyMaster.squares.get(acCopyMaster.squares.size() - backwardCounter));
					AnalysisChain acCopy = new AnalysisChain(ac);
					
					analysisChainHash.put(counter, acCopy);
					backwardCounter++;
					counter++;
				}
			}
			
			revealedField = revealedCopyField;
			masterField = masterCopy;
		}
	}
	
	public void makeNewField(int fieldx, int fieldy, int difficulty)
	{
		finished = false;
		ended = false;
		
		for (int i = 0; i < nextID; i++)
		{
			if (chainHash.containsKey(i))
			{
				chainHash.remove(i);
			}
		}
		
		nextID = 0;
		nextChainID = 0;
		
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
		idField = new ArrayList[fieldy][fieldx];
		idFieldChains = new ArrayList[fieldy][fieldx];
		probField = new double[fieldy][fieldx];
		for (int i = 0; i < fieldy; i++)
		{
			for (int n = 0; n < fieldx; n++)
			{
				idField[i][n] = new ArrayList<Integer>();
				idFieldChains[i][n] = new ArrayList<Integer>();
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
	}
	
	public void makeNewField(char[][] fieldIn)
	{
		int fieldx = fieldIn[0].length;
		int fieldy = fieldIn.length;
		
		finished = false;
		ended = false;
		
		for (int i = 0; i < nextID; i++)
		{
			if (chainHash.containsKey(i))
			{
				chainHash.remove(i);
			}
		}
		
		nextID = 0;
		
		invalidLabels = new ArrayList<Integer>();
		
		revealedField = new char[fieldy][fieldx];
		masterField = new char[fieldy][fieldx];
		idField = new ArrayList[fieldy][fieldx];
		idFieldChains = new ArrayList[fieldy][fieldx];
		probField = new double[fieldy][fieldx];
		for (int i = 0; i < fieldy; i++)
		{
			for (int n = 0; n < fieldx; n++)
			{
				idField[i][n] = new ArrayList<Integer>();
				idFieldChains[i][n] = new ArrayList<Integer>();
				revealedField[i][n] = '?';
			}
		}
		
		masterField = fieldIn;
		revealedField = fieldIn;
	}
	
	public MinesweeperGameWithAI()
	{
		invalidLabels = new ArrayList<Integer>();
		rng = new Random();
		chainHash = new HashMap<Integer, Chain>();
		analysisChainHash = new HashMap<Integer, AnalysisChain>();
		validLabels = new ArrayList<Integer>();
	}
}
