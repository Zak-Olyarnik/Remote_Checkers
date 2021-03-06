import java.io.IOException;
import java.util.*;

public class MatchMaking
{
	private ArrayList<ClientInfo> clientOnes;
	private ArrayList<ClientInfo> clientTwos;
	
	private static MatchMaking singleton;
	
	private MatchMaking()
	{
		clientOnes = new ArrayList<ClientInfo>();
		clientTwos = new ArrayList<ClientInfo>();
	}
	
	public static MatchMaking getInstance()
	{
		if (singleton == null)
			singleton = new MatchMaking();
		return singleton;
	}
	
	private int getIndexForNewMatch()
	{
		for (int i = 0; i < clientOnes.size(); i++)
		{
			if (clientOnes.get(i) == null)
				return i;
		}
		
		return clientOnes.size();
	}
	
	public void matchClients()
	{
		Transcription transcription = Transcription.getInstance();
		int matchIndex = getIndexForNewMatch();
		
		if (transcription.getClientsCount() % 2 == 0)
		{
			
			ClientInfo firstClient = transcription.getClientForMatching();
			ClientInfo secondClient = transcription.getClientForMatching();
			
			transcription.write(secondClient.getSocket(), (byte)'C');
			transcription.write(secondClient.getSocket(), "Red");
			
			Thread listenThread = new Thread()
			{
				public void run()
				{
					Listen listen = new Listen(secondClient);
					listen.retrieveMessages(matchIndex);
				}
			}; 
			listenThread.start();
			
			if (matchIndex == clientOnes.size())
			{
				clientOnes.add(firstClient);
				clientTwos.add(secondClient);
			}
			else
			{
				clientOnes.set(matchIndex, firstClient);
				clientTwos.set(matchIndex, secondClient);
			}
			GameManagement.getInstance().openGameForClients(matchIndex, firstClient.getSocket(), secondClient.getSocket());
		}
		else
		{
			ClientInfo firstClient = transcription.peakClientForMatching(); 
			
			Thread listenThread = new Thread()
			{
				public void run()
				{
					Listen listen = new Listen(firstClient);
					listen.retrieveMessages(matchIndex);
				}
			}; 
			listenThread.start();
			
			transcription.write(firstClient.getSocket(), (byte)'C');
			transcription.write(firstClient.getSocket(), "Blue");
			transcription.write(firstClient.getSocket(), (byte)'M');
			transcription.write(firstClient.getSocket(), "Waiting for Opponent...");
		}
	}
	
	public void unmatchClients(int matchIndex, ClientInfo disconnectedClient)
	{
		Transcription transcription = Transcription.getInstance();
		
		if (clientOnes.size() > matchIndex)
		{
			if (clientOnes.get(matchIndex) != null && clientTwos.get(matchIndex) != null)
			{
				try
				{
					GameManagement.getInstance().closeGame(matchIndex);
					ClientInfo clientOne = clientOnes.get(matchIndex);
					ClientInfo clientTwo = clientTwos.get(matchIndex);
					
					if (disconnectedClient == clientOne)
					{
						transcription.write(clientTwo.getSocket(), (byte)'D');
						transcription.write(clientTwo.getSocket(), "");
						clientTwo.getSocket().close();
						clientOnes.set(matchIndex, null);
					}
					else
					{
						transcription.write(clientOne.getSocket(), (byte)'D');
						transcription.write(clientOne.getSocket(), "");
						clientOne.getSocket().close();
						clientTwos.set(matchIndex, null);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else if (clientOnes.get(matchIndex) == null && clientTwos.get(matchIndex) != null)
				clientTwos.set(matchIndex, null);
			else if (clientOnes.get(matchIndex) != null && clientTwos.get(matchIndex) == null)
				clientOnes.set(matchIndex, null);
			else
				transcription.getClientForMatching();
		}
		else
			transcription.getClientForMatching();
	}
}
