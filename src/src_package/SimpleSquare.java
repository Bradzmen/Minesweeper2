package src_package;

public class SimpleSquare
{
	int x;
	int y;
	int analysisChainID;
	char[][] adjacent;
	char[][] minefield;
	
	public boolean sameAs(SimpleSquare s)
	{
		boolean out = false;
		
		if (x == s.x && y == s.y)
		{
			out = true;
		}
		
		return out;
	}
	
	SimpleSquare(char[][] infield, int inx, int iny, int chainID)
	{
		minefield = infield;
		x = inx;
		y = iny;
		analysisChainID = chainID;
		adjacent = new char[3][3];
		
		for (int j = -1; j < 2; j++)
		{
			for (int k = -1; k < 2; k++)
			{
				if (MinesweeperGame.isIndexInBounds(x + k, y + j, minefield))
				{
					adjacent[j + 1][k + 1] = minefield[y + j][x + k];
				}
			}
		}
	}
}
