import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Transcription
{
	private static Transcription singleton;
	private Socket clientSocket;		// connection to server
	private ObjectOutputStream out;		// stream to write to server
	private ObjectInputStream in;		// stream to read from server
	private String server;				// connection ip
	private int port = 9900;			// connection port
	private Byte messageType;			// signifies type of message to be received next
	private String message;				// text message
	private Board newBoard;				// board update
	private int res;					// result of message dialog click

	private Transcription()
	{ }

	// implements singleton
	public static Transcription getTranscription()
	{
		if(singleton == null)
		{ singleton = new Transcription(); }
		return singleton;
	}

	// makes connection to server
	public void connect()
	{
		readFile();
		try
		{
			clientSocket = new Socket(server, port);
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			messageType = in.readByte();
			if(messageType == 'C')
			{
				String color = in.readUTF();
				GameScreen.getGameScreen().setColor(color);
			}
		}
		catch (IOException e)
		{ e.printStackTrace(); }
	}

	// main loop, listens for board updates, messages, and disconnects from server
	public void listen()
	{
		try
		{
			readloop: while(true)
			{
				messageType = in.readByte();
				switch(messageType)
				{
				case 'M':
					message = in.readUTF();
					GameScreen.getGameScreen().displayMessage(message);
					break;
				case 'B':
					newBoard = (Board) in.readObject();
					GameScreen.getGameScreen().redraw(newBoard);
					break;
				case 'E':
					message = in.readUTF();
					res = GameScreen.getGameScreen().showPopup("Game Over", message);
					if (res <= 1)
					{
						GameScreen.getGameScreen().hide();
						MainScreen.getMainScreen().execute();
						break readloop;
					}
				case 'D':
					message = in.readUTF();
					res = GameScreen.getGameScreen().showPopup("Game Over", message);
					if (res <= 1)
					{
						GameScreen.getGameScreen().hide();
						MainScreen.getMainScreen().execute();
						break readloop;
					}
					break;
				}
			}
			sendEnd();
		}
		catch (IOException | ClassNotFoundException e)
		{ e.printStackTrace(); }
		finally
		{
			try
			{ clientSocket.close(); } 
			catch (IOException e)
			{ e.printStackTrace(); }
		}
	}

	// sends messages to server, byte followed by space or end message
	public void sendMove(Space clicked)
	{
		try
		{
			out.reset();
			out.writeByte('S');
			out.writeObject(clicked);
			out.flush();			
		}
		catch (IOException e)
		{ e.printStackTrace(); }
	}

	public void sendEnd()
	{
		try
		{
			out.reset();
			out.writeByte('E');
			out.flush();			
		}
		catch (IOException e)
		{ e.printStackTrace(); }
	}

	// reads Server connection settings from config.txt file
	private void readFile()
	{
		try
		{
			FileInputStream infile = new FileInputStream(Client.configFile); 
			BufferedReader in = new BufferedReader(new InputStreamReader(infile));
			server = in.readLine();
			in.close();
			infile.close(); 
		}
		catch (IOException e)
		{ e.printStackTrace(); }
	}

}
