import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Player extends Thread
{
	public Player opponent;		// links this player to opponent
	public Game game;			// links this player to game
	public String color;		// color of player

	private Socket socket;				// connection to client
	private ObjectOutputStream out;		// stream to write to client
	private ObjectInputStream in;		// stream to read from client
	private Space clicked;				// clicked space received from client
	private Board newBoard;				// board to send back to client

	// initializes connection to client
	public Player(Socket socket, String color)
	{
		this.socket = socket;
		this.color = color;

			try
			{
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				out.writeByte('C');
				out.writeUTF(color);
				out.flush();
				out.writeByte('M');
				out.writeUTF("Waiting for opponent...");
				out.flush();
			}
			catch (IOException e)
			{ e.printStackTrace(); }
	}

	// main loop, listens for space clicks from client
	public void run()
	{
		try
		{
			if (color == "Red")
			{
				sendBoard(game.getStartBoard());
				sendSelectPiece();
			}
			else
			{ sendPleaseWait(); }
			
			while(true)
			{
				Byte messageType = in.readByte();
				if (messageType == 'S')
				{
					clicked = (Space) in.readObject();
					if(clicked.getContents() == Space.GREEN_SPACE)
					{
						newBoard = game.updateAfterMove(clicked);
						sendBoard(newBoard);
						if (game.turnOver)
						{ switchTurn(); }
					}
					else
					{
						newBoard = game.getLegalMoves(clicked);
						sendBoard(newBoard);
						sendSelectSpace();
					}
				}
				else if (messageType == 'E')
				{ return; }
			}
		}
		catch (IOException | ClassNotFoundException e)
		{ 
			try
			{ opponent.sendDisconnect(); }
			catch (IOException e1)
			{ e1.printStackTrace(); }
		}
		finally
		{
			try
			{ socket.close(); }
			catch (IOException e)
			{ e.printStackTrace(); }
		}
	}

	// sends messages to client, byte followed by board or string to be deserialized
	private void sendBoard(Board b) throws IOException
	{
		out.reset();
		out.writeByte('B');
		out.writeObject(b);
		out.flush();
	}

	private void sendSelectPiece() throws IOException
	{ sendMessage("Your Turn - Select a Piece to Move"); }

	private void sendSelectSpace() throws IOException
	{ sendMessage("Your Turn - Select a Space to Move Into"); }

	private void sendPleaseWait() throws IOException
	{ sendMessage("Opponent's Turn, Please Wait..."); }

	private void sendMessage(String m) throws IOException
	{
		out.reset();
		out.writeByte('M');
		out.writeUTF(m);
		out.flush();
	}

	private void sendWin() throws IOException
	{
		out.reset();
		out.writeByte('E');
		out.writeUTF("You won!");
		out.flush();
	}

	private void sendLoss() throws IOException
	{
		out.reset();
		out.writeByte('E');
		out.writeUTF("You lost!");
		out.flush();
	}

	private void sendDisconnect() throws IOException
	{
		out.reset();
		out.writeByte('D');
		out.writeUTF("Opponent has disconnected!");
		out.flush();
	}

	// switches turns between players if game is not over
	private void switchTurn() throws IOException
	{
		sendPleaseWait();
		game.activePlayer = opponent;
		newBoard = game.getLegalPieces();
		opponent.sendBoard(newBoard);
		if (game.hasLegalPieces)
		{

			opponent.sendSelectPiece();
		}
		else
		{
			sendMessage("Winner!");
			sendWin();
			opponent.sendMessage("Loser!");
			opponent.sendLoss();
		}
	}
}
