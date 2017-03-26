import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;


public class Server
{
	// driver, capable of running multiple games.  Displays connection ip to screen
	public static void main(String[] args) throws IOException
	{
		int port = 9900;
		ServerSocket serverSocket = new ServerSocket(port);
		InetAddress server = InetAddress.getLocalHost();
		System.out.println("Server running at IP Address \"" + server.getHostName() + "\"" );
		try
		{
			while(true)
			{
				Game g = new Game();
				Player red = new Player(serverSocket.accept(), "Red");
				Player blue = new Player(serverSocket.accept(), "Blue");
				red.opponent = blue;
				blue.opponent = red;
				red.game = g;
				blue.game = g;
				g.activePlayer = red;
				red.start();
				blue.start();			
			}
		}
		finally
		{ serverSocket.close();}
	}
}
