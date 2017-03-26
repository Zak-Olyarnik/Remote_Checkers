
public class Game
{
	public Player activePlayer;					// player who is currently making moves
	public Board b;								// current state of game board
	public boolean turnOver = false;			// whether turn is over or more jumps can be made
	public boolean hasLegalPieces = false;		// check for end of game
	
	private int size = 8;					// dimensions of board
	private Space lastSelected;				// stores the last click
	private boolean isJumping = false;		// whether to check for further jumps

	// red's front row are valid selections at start of game
	public Board getStartBoard()
	{
		b = new Board();
		b.setAt(5, 1, Space.VALID_PIECE);
		b.setAt(5, 3, Space.VALID_PIECE);
		b.setAt(5, 5, Space.VALID_PIECE);
		b.setAt(5, 7, Space.VALID_PIECE);
		return b;
	}

	// checks for jumps, then steps, of selected piece (depending on color)
	public Board getLegalMoves(Space clicked)
	{
		turnOver = false;
		int row = clicked.getRow();
		int col = clicked.getCol();
		lastSelected = clicked;
		removeGreenSpaces();
		
		if (canJump(row, col))
		{ getJumps(row, col); }
		else if (canStep(row, col))
		{ getSteps(row, col); }
		return b;
	}

	// gets legal jumps of selected piece
	public void getJumps(int row, int col)
	{
		if (activePlayer.color == "Red")
		{
			if (isBlue(row-1, col+1) && isEmpty(row-2, col+2))
			{ b.setAt(row-2, col+2, Space.GREEN_SPACE); }
			if (isBlue(row-1, col-1) && isEmpty(row-2, col-2))
			{ b.setAt(row-2, col-2, Space.GREEN_SPACE); }

			if (isKing(row, col))
			{
				if (isBlue(row+1, col+1) && isEmpty(row+2, col+2))
				{ b.setAt(row+2, col+2, Space.GREEN_SPACE); }
				if (isBlue(row+1, col-1) && isEmpty(row+2, col-2))
				{ b.setAt(row+2, col-2, Space.GREEN_SPACE); }
			}
		}

		else if (activePlayer.color == "Blue")
		{
			if (isRed(row+1, col-1) && isEmpty(row+2, col-2))
			{ b.setAt(row+2, col-2, Space.GREEN_SPACE); }
			if (isRed(row+1, col+1) && isEmpty(row+2, col+2))
			{ b.setAt(row+2, col+2, Space.GREEN_SPACE); }

			if (isKing(row, col))
			{
				if (isRed(row-1, col-1) && isEmpty(row-2, col-2))
				{ b.setAt(row-2, col-2, Space.GREEN_SPACE); }
				if (isRed(row-1, col+1) && isEmpty(row-2, col+2))
				{ b.setAt(row-2, col+2, Space.GREEN_SPACE); }
			}
		}
	}

	// gets legal steps of selected piece
	public void getSteps(int row, int col)
	{
		if (activePlayer.color == "Red")
		{
			if (isEmpty(row-1, col+1))
			{ b.setAt(row-1, col+1, Space.GREEN_SPACE); }
			if (isEmpty(row-1, col-1))
			{ b.setAt(row-1, col-1, Space.GREEN_SPACE); }

			if (isKing(row, col))
			{
				if (isEmpty(row+1, col+1))
				{ b.setAt(row+1, col+1, Space.GREEN_SPACE); }
				if (isEmpty(row+1, col-1))
				{ b.setAt(row+1, col-1, Space.GREEN_SPACE); }
			}
		}

		else if (activePlayer.color == "Blue")
		{
			if (isEmpty(row+1, col-1))
			{ b.setAt(row+1, col-1, Space.GREEN_SPACE); }
			if (isEmpty(row+1, col+1))
			{ b.setAt(row+1, col+1, Space.GREEN_SPACE); }

			if (isKing(row, col))
			{
				if (isEmpty(row-1, col-1))
				{ b.setAt(row-1, col-1, Space.GREEN_SPACE); }
				if (isEmpty(row-1, col+1))
				{ b.setAt(row-1, col+1, Space.GREEN_SPACE); }
			}
		}
	}


	// finds legal piece selections for the active player
	public Board getLegalPieces()
	{
		hasLegalPieces = false;
		if (hasJumps())
		{
			hasLegalPieces = true;
			isJumping = true;
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					if (activePlayer.color == "Red")
					{
						if (isRed(i, j) && canJump(i, j))
						{
							if (isKing(i, j))
							{ b.setAt(i, j, Space.VALID_KING); }
							else
							{ b.setAt(i, j, Space.VALID_PIECE); }
						}
					}
					else if (activePlayer.color == "Blue")
					{
						if (isBlue(i, j) && canJump(i, j))
						{
							if (isKing(i, j))
							{ b.setAt(i, j, Space.VALID_KING); }
							else
							{ b.setAt(i, j, Space.VALID_PIECE); }
						}
					}
				}
			}
		}
		else		// has steps only
		{
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					if (activePlayer.color == "Red")
					{
						if (isRed(i, j) && canStep(i, j))
						{
							hasLegalPieces = true;
							if (isKing(i, j))
							{ b.setAt(i, j, Space.VALID_KING); }
							else
							{ b.setAt(i, j, Space.VALID_PIECE); }
						}
					}
					else if (activePlayer.color == "Blue")
					{
						if (isBlue(i, j) && canStep(i, j))
						{
							hasLegalPieces = true;
							if (isKing(i, j))
							{ b.setAt(i, j, Space.VALID_KING); }
							else
							{ b.setAt(i, j, Space.VALID_PIECE); }
						}
					}
				}
			}
		}

		return b;
	}

	// removes all legal selections and jumped pieces, checks for end of turn
	public Board updateAfterMove(Space clicked)
	{
		int row = clicked.getRow();
		int col = clicked.getCol();

		// set new space contents
		b.setAt(row, col, lastSelected.getContents());
		b.setAt(lastSelected.getRow(), lastSelected.getCol(), Space.BLACK_SPACE);

		// remove all selections
		removeGreenSpaces();
		removeValidSpaces();

		// remove jumped piece
		if(isJumping)
		{
			int remRow = (row + lastSelected.getRow()) / 2;
			int remCol = (col + lastSelected.getCol()) / 2;
			b.setAt(remRow, remCol, Space.BLACK_SPACE);
		}

		// update to king
		if (activePlayer.color == "Red" && row == 0)
		{
			b.setAt(row, col, Space.RED_KING);
			turnOver = true;
			isJumping = false;
			return b;
		}
		else if (activePlayer.color == "Blue" && row == 7)
		{
			b.setAt(row, col, Space.BLUE_KING);
			turnOver = true;
			isJumping = false;
			return b;
		}

		// find further jumps
		if(isJumping && canJump(row, col))
		{
			lastSelected = clicked;
			lastSelected.setImage(b.getAt(row, col), activePlayer.color);
			getJumps(row, col);
			return b;
		}

		// end turn
		turnOver = true;
		isJumping = false;
		return b;
	}
		
	// removes valid selection spaces on board update
	private void removeValidSpaces()
	{
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{
				if (activePlayer.color == "Red")
				{
					if (b.getAt(row, col) == Space.VALID_PIECE)
					{ b.setAt(row, col, Space.RED_PIECE); }
					else if (b.getAt(row, col) == Space.VALID_KING)
					{ b.setAt(row, col, Space.RED_KING); }
				}
				if (activePlayer.color == "Blue")
				{
					if (b.getAt(row, col) == Space.VALID_PIECE)
					{ b.setAt(row, col, Space.BLUE_PIECE); }
					else if (b.getAt(row, col) == Space.VALID_KING)
					{ b.setAt(row, col, Space.BLUE_KING); }
				}
			}
		}
	}
	
	// removes green spaces on board update
	private void removeGreenSpaces()
	{
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{
				if (b.getAt(row, col) == Space.GREEN_SPACE)
				{ b.setAt(row, col, Space.BLACK_SPACE); }
			}
		}
	}

	// checks if active player has jump moves
	private boolean hasJumps()
	{
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (activePlayer.color == "Red")
				{
					if (isRed(i, j) && canJump(i, j))
					{ return true; }
				}
				else if (activePlayer.color == "Blue")
				{
					if (isBlue(i, j) && canJump(i, j))
					{ return true; }
				}
			}
		}
		return false;
	}

	// checks if selected piece can step
	private boolean canStep(int row, int column)
	{
		boolean result = false;
		boolean canStepNW = false, canStepNE = false, canStepSW = false, canStepSE = false;

		if (!canJump(row, column))
		{
			if (activePlayer.color == "Red")
			{
				canStepNW = isEmpty(row-1, column+1);
				canStepNE = isEmpty(row-1, column-1);
				if (isKing(row, column))
				{
					canStepSW = isEmpty(row+1, column+1);
					canStepSE = isEmpty(row+1, column-1);
				}
			}

			else if (activePlayer.color == "Blue")
			{
				canStepNW = isEmpty(row+1, column-1);
				canStepNE = isEmpty(row+1, column+1);
				if (isKing(row, column))
				{
					canStepSW = isEmpty(row-1, column-1);
					canStepSE = isEmpty(row-1, column+1);
				}
			}
		}

		result = canStepNW || canStepNE || canStepSW || canStepSE;
		return result;
	}
	
	// checks if selected piece can jump
	private boolean canJump(int row, int column)
	{	
		boolean result = false;
		boolean canJumpNW = false, canJumpNE = false, canJumpSW = false, canJumpSE = false;

		if (activePlayer.color == "Red")
		{
			canJumpNW = isBlue(row-1, column+1) && isEmpty(row-2, column+2);
			canJumpNE = isBlue(row-1, column-1) && isEmpty(row-2, column-2);
			if (isKing(row, column))
			{
				canJumpSW = isBlue(row+1, column+1) && isEmpty(row+2, column+2);
				canJumpSE = isBlue(row+1, column-1) && isEmpty(row+2, column-2);
			}
		}

		else if (activePlayer.color == "Blue")
		{
			canJumpNW = isRed(row+1, column-1) && isEmpty(row+2, column-2);
			canJumpNE = isRed(row+1, column+1) && isEmpty(row+2, column+2);
			if (isKing(row, column))
			{
				canJumpSW = isRed(row-1, column-1) && isEmpty(row-2, column-2);
				canJumpSE = isRed(row-1, column+1) && isEmpty(row-2, column+2);
			}
		}

		result = canJumpNW || canJumpNE || canJumpSW || canJumpSE;
		return result;
	}

	// wrapper for Board.getAt() that checks if selected space is inside board bounds
	private int getSpace(int row, int col)
	{
		if (row < 0 || row >= size || col < 0 || col >= size)
		{ return 10; }
		else
		{ return b.getAt(row, col); }
	}

	// following methods check space contents
	private boolean isEmpty(int row, int col)
	{ return (getSpace(row, col) == Space.BLACK_SPACE); }
	
	private boolean isBlue(int row, int col)
	{ return (getSpace(row, col) == Space.BLUE_PIECE || getSpace(row, col) == Space.BLUE_KING); }

	private boolean isRed(int row, int col)
	{ return (getSpace(row, col) == Space.RED_PIECE || getSpace(row, col) == Space.RED_KING); }

	private boolean isKing(int row, int col)
	{ return (getSpace(row, col) == Space.RED_KING || getSpace(row, col) == Space.BLUE_KING || getSpace(row, col) == Space.VALID_KING); }

}
