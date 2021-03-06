import java.io.IOException;
import java.net.Socket;


public class Transcription
{
	private Connect connect;
	private Listen listen;
	private Send send;
	private Socket socket;
	private OutFile logging; 
	private static Transcription singleton;
	
	private static boolean serverCheck; 
	
	private Transcription()
	{
		serverCheck = false; 
		connect = new Connect();
		socket = connect.connectSocket(); 
		logging = OutFile.getInstance(); 

		if (socket == null)
		{
			//Error out...no server available 
			logging.writeError("No server to connect to.");
			return; 
		}
		
		serverCheck = true; 
		
		send = new Send(socket);
		send.sendMessage((byte)'C'); 
		send.sendMessage("Test message");
		
		listen = new Listen(socket);
		Thread listenThread = new Thread()
		{
			public void run()
			{
				listen.retrieveMessages();
			}
		};  
		listenThread.start();
	}
	
	public static Transcription getInstance()	// implements singleton
	{
		if(singleton == null)
		{ 
			singleton = new Transcription();
		}
		if (!serverCheck) singleton = null; 
			
		return singleton;  
	}
	
	/**
	 * read() - Blocking call to get the next message. Waits for server. 
	 * @return
	 */
	public Object read()
	{ return listen.getNextMessage()[1]; }
	
	/**
	 * readByte() - Blocking call to get the next byte. Waits for server. 
	 * @return
	 */
	public Byte readByte()
	{ return (Byte) listen.getNextMessage()[0]; }
	
	public void write(byte b)
	{
		send.sendMessage(b);
	}

	public static void clear()
	{
		singleton = null;
	}
	
	public void closeSocket()
	{
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			logging.writeError(e.getMessage());
		}
	}
			
	public void write(Object message)
	{
		send.sendMessage(message);
	}
	
	public void sendMove(Space clicked)
	{ send.sendMove(clicked); }
	
	public void sendMove(Space lastClicked, Space clicked)
	{ send.sendMove(lastClicked, clicked); }
	
	public void listen()
	{ listen.retrieveMessages(); }
}
