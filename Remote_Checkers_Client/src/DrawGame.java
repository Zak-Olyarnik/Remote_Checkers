import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


//Responsible for creating, showing, and hiding the Game GUI, as well as redrawing the board.
public class DrawGame extends DrawUI
{
	private JPanel panel;				// main panel of content
	private JButton btnHelp;			// launches Help window
	private JLabel lblPlayer;			// displays player color
	private JLabel lblMessage;			// displays messages from server
	private JLabel lblScore;			// displays player score
	private JLabel lblOppScore;			// displays opponent score
	private JButton btnResign;			// forfeits the game and offers a rematch
	private JPanel boardPanel;			// GUI representation of the checkerboard
	private Space[][] board;
	private int size;
	private String color;				// this will eventually be set by the constructor
	
	DrawGame()		// calls createElements(), drawElements(), and registerEventHandlers(),
	{ 					// then loads images
		super();
		Space.loadImages();
	}
	
	public void displayMessage(String message)		// displays String messages from server
	{ lblMessage.setText(message); }
	
	// sets player color (and draws board) when received from Server
	public void setColor(String color)
	{
		this.color = color;
		lblPlayer.setText("Welcome, " + color + "!");
		setupBoard();
	}
		
	public void reDraw(Board newBoard)	// redraws only the board spaces that have updated
	{
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{
				Element newElement = newBoard.getSpace(row, col);
				if(board[row][col].getContents() != newElement)
				{
					board[row][col].setImage(newElement, color);
					board[row][col].revalidate();
					board[row][col].repaint();
				}
			}
		}
	}
	
	public void setupBoard()	// draws the "new game" board, flipping depending on player color
	{
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{
				if (row%2 ==col%2)
				{
					if (row<3)
					{ board[row][col].setImage(Element.RED, color); }
					else if (row>4)
					{ board[row][col].setImage(Element.BLUE, color); }
					else
					{ board[row][col].setImage(Element.BLACKSPACE, color); }
				}
				else
				{ board[row][col].setImage(Element.WHITESPACE, color); }
				if (color.equals("Blue"))
				{ boardPanel.add(board[row][col]); }
			}
		}
		if (color.equals("Red"))
		{
			for (int row=size-1; row>-1; row--)
			{
				for (int col=size-1; col>-1; col--)
				{ boardPanel.add(board[row][col]); }
			}
		}
	}

	@Override
	public void createElements()		// initializes each GUI element
	{
		size = 8;
		Dimension messageSize = new Dimension(500, 40);
		Dimension scoreSize = new Dimension(200, 40);
		
		panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		btnHelp = new JButton("Help");
		btnHelp.setActionCommand("Help");
		btnHelp.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnHelp.setFont(BUTTON_FONT);
		btnHelp.setToolTipText("View Rules and Usage Guide");
		
		lblPlayer = new JLabel();
		lblPlayer.setFont(LABEL_FONT);
		lblPlayer.setMinimumSize(messageSize); 
		lblPlayer.setMaximumSize(messageSize); 
		lblPlayer.setPreferredSize(messageSize);
		lblPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblMessage = new JLabel("Opponent's Turn, Please Wait...");
		lblMessage.setFont(LABEL_FONT);
		lblMessage.setMinimumSize(messageSize); 
		lblMessage.setMaximumSize(messageSize); 
		lblMessage.setPreferredSize(messageSize);
		lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblScore = new JLabel("Score: 0");
		lblScore.setFont(LABEL_FONT);
		lblScore.setMinimumSize(scoreSize); 
		lblScore.setMaximumSize(scoreSize); 
		lblScore.setPreferredSize(scoreSize);
		lblScore.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblScore.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblOppScore = new JLabel("Opponent: 0");
		lblOppScore.setFont(LABEL_FONT);
		lblOppScore.setMinimumSize(scoreSize); 
		lblOppScore.setMaximumSize(scoreSize); 
		lblOppScore.setPreferredSize(scoreSize);
		lblOppScore.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblOppScore.setHorizontalAlignment(SwingConstants.RIGHT);
		
		btnResign = new JButton("Resign");
		btnResign.setActionCommand("Resign");
		btnResign.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnResign.setFont(BUTTON_FONT);
		btnResign.setToolTipText("Forfeit the Game");
		
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(size, size));
		
		board = new Space[size][size];
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{ board[row][col] = new Space(row, col); }
		}
	}

	@Override
	public void drawElements()			// places GUI elements
	{
		Dimension borderSize = new Dimension(100, 100);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.add(lblPlayer);
		messagePanel.add(lblMessage);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
		scorePanel.add(lblScore);
		scorePanel.add(lblOppScore);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(btnHelp);
		topPanel.add(messagePanel);
		topPanel.add(scorePanel);
		topPanel.add(Box.createRigidArea(new Dimension(25, 25)));
		topPanel.add(btnResign);

		JPanel boardHolderPanel = new JPanel();
		boardHolderPanel.setLayout(new BoxLayout(boardHolderPanel, BoxLayout.X_AXIS));
		boardHolderPanel.add(Box.createRigidArea(borderSize));
		boardHolderPanel.add(boardPanel);
		boardHolderPanel.add(Box.createRigidArea(borderSize));
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(25, 25)));
		panel.add(topPanel);
		panel.add(Box.createRigidArea(new Dimension(35, 35)));
		panel.add(boardHolderPanel);
		panel.add(Box.createRigidArea(borderSize));
	}

	@Override
	public void registerEventHandlers()		// adds click handlers to buttons and board spaces
	{
		btnResign.addActionListener(Game.getGame());
		btnHelp.addActionListener(Game.getGame());
		
		for (int row=0; row<size; row++)
		{
			for (int col=0; col<size; col++)
			{ board[row][col].addMouseListener(Game.getGame()); }
		}
	}

	@Override
	public void show()
	{
		frame.add(panel);
        frame.revalidate();
        frame.repaint();
	}
	
	@Override
	public void hide()
	{ frame.remove(panel); }
}
