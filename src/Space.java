import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Space extends JPanel implements MouseListener, Serializable
{
	public static final int WHITE_SPACE = 0;			// represent all possible space contents
	public static final int BLACK_SPACE = 1;
	public static final int GREEN_SPACE = 2;
	public static final int RED_PIECE = 3;
	public static final int BLUE_PIECE = 4;
	public static final int RED_KING = 5;
	public static final int BLUE_KING = 6;
	public static final int VALID_PIECE = 7;
	public static final int VALID_KING = 8;
	
	public static ImageIcon WHITE_SPACE_IMAGE;			// board images shared across all instances of the Space class
	public static ImageIcon BLACK_SPACE_IMAGE;
	public static ImageIcon GREEN_SPACE_IMAGE;
	public static ImageIcon RED_PIECE_IMAGE;
	public static ImageIcon BLUE_PIECE_IMAGE;
	public static ImageIcon RED_KING_IMAGE;
	public static ImageIcon BLUE_KING_IMAGE;

	private int contents;			// static int representation of the space's contents
	private JLabel lblContents;		// label where image is displayed
	private ImageIcon icon;			// picture set on the label
	private int row;				// space's row location
	private int col;				// space's column location
	
	Space(int r, int c)
	{
		setLayout(new BorderLayout());
		lblContents = new JLabel();
		add(lblContents);
		row = r;
		col = c;
	}

	// Changes the space's image based on the new contents provided.  Player color is
		//  necessary to differentiate the non-unique images for valid pieces and kings.
	public void setImage(int newContents, String color)
	{
		switch (newContents)
		{
		case WHITE_SPACE:
			icon = WHITE_SPACE_IMAGE;
			break;
		case BLACK_SPACE:
			icon = BLACK_SPACE_IMAGE;
			break;
		case GREEN_SPACE:
			icon = GREEN_SPACE_IMAGE;
			break;
		case RED_PIECE:
			icon = RED_PIECE_IMAGE;
			break;
		case BLUE_PIECE:
			icon = BLUE_PIECE_IMAGE;
			break;
		case RED_KING:
			icon = RED_KING_IMAGE;
			break;
		case BLUE_KING:
			icon = BLUE_KING_IMAGE;
			break;
		case VALID_PIECE:
			if (color.equals("Red"))
			{ icon = RED_PIECE_IMAGE; }
			else
			{ icon = BLUE_PIECE_IMAGE; }
			break;
		case VALID_KING:
			if (color.equals("Red"))
			{ icon = RED_KING_IMAGE; }
			else
			{ icon = BLUE_KING_IMAGE; }
			break;
		}
		
		contents = newContents;
		lblContents.setIcon(icon);
	}
	
	public int getContents()
	{ return contents; }
	
	public int getRow()
	{ return row; }
	
	public int getCol()
	{ return col; }
	
	public static void loadImages()
	{
		try		// load images - Eclipse
		{
			WHITE_SPACE_IMAGE = new ImageIcon(Client.class.getResource("/white_space.png"));
			BLACK_SPACE_IMAGE = new ImageIcon(Client.class.getResource("/black_space.png"));
			GREEN_SPACE_IMAGE = new ImageIcon(Client.class.getResource("/green_space.png"));
			RED_PIECE_IMAGE = new ImageIcon(Client.class.getResource("/red_piece.png"));
			BLUE_PIECE_IMAGE = new ImageIcon(Client.class.getResource("/blue_piece.png"));
			RED_KING_IMAGE = new ImageIcon(Client.class.getResource("/red_king.png"));
			BLUE_KING_IMAGE = new ImageIcon(Client.class.getResource("/blue_king.png"));
		}
		catch(NullPointerException e)	// load images - command line
		{
			WHITE_SPACE_IMAGE = new ImageIcon("../res/white_space.png");
			BLACK_SPACE_IMAGE = new ImageIcon("../res/black_space.png");
			GREEN_SPACE_IMAGE = new ImageIcon("../res/green_space.png");
			RED_PIECE_IMAGE = new ImageIcon("../res/red_piece.png");
			BLUE_PIECE_IMAGE = new ImageIcon("../res/blue_piece.png");
			RED_KING_IMAGE = new ImageIcon("../res/red_king.png");
			BLUE_KING_IMAGE = new ImageIcon("../res/blue_king.png");
		}
	}

	@Override
	public void mousePressed(MouseEvent e)	// logic for handling board clicks
	{
		Space clicked = (Space) e.getSource();
		if (clicked.getContents() == VALID_PIECE || clicked.getContents() == VALID_KING || clicked.getContents() == GREEN_SPACE)
		{ Transcription.getTranscription().sendMove(clicked); }
		else
		{
			System.out.println(Integer.toString(clicked.getContents()));
		}

		/*System.out.println(clicked.getContents().toString());
		playerPos.setAt(clicked.getRow(), clicked.getCol(), Element.GREENSPACE);
		gameUI.reDraw(playerPos);*/
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{ }

	@Override
	public void mouseReleased(MouseEvent e)
	{ }

	@Override
	public void mouseEntered(MouseEvent e)
	{ }

	@Override
	public void mouseExited(MouseEvent e)
	{ }
}
