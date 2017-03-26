import java.io.Serializable;


@SuppressWarnings("serial")
public class Board implements Serializable
{
	private int spaces[][];
	private int size = 8;

	// creates the "new game" board
	Board()
	{
		spaces = new int[size][size];
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{
				if (row%2 == col%2)
				{
					if (row<3)
					{ spaces[row][col] = Space.BLUE_PIECE; }
					else if (row>4)
					{ spaces[row][col] = Space.RED_PIECE; }
					else
					{ spaces[row][col] = Space.BLACK_SPACE; }
				}
				else
				{ spaces[row][col] = Space.WHITE_SPACE; }
			}
		}
	}

	public int getAt(int row, int col)
	{ return spaces[row][col]; }

	public void setAt(int row, int col, int newContents)
	{ spaces[row][col] = newContents; }

}
