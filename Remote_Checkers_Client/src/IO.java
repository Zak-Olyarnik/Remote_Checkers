import java.net.Socket;
import java.time.LocalDateTime;

public abstract class IO
{
	private String configFile = "res/config.txt";
	private String logFile = "logs/log.txt";
	private String errorFile = "logs/error.txt";
	
	protected Socket useSocket; 
	 
	protected String dateTimeString = LocalDateTime.now().toString();
	
	//Getters
	public String getConfigFilePath()
	{
		return configFile; 
	}
	public String getLogFilePath()
	{
		return logFile; 
	}
	public String getErrorFilePath()
	{
		return errorFile; 
	}
	public Socket getSocketToUse()
	{ 
		useSocket = ServerInfo.getInstance().getSocket(); 
		return useSocket; 
	}
	
	//Setters
	public void setConfigFilePath(String filePath)
	{
		configFile = filePath; 
	}
	public void setLogFilePath(String filePath)
	{
		logFile = filePath; 
	}
	public void setErrorFilePath(String filePath)
	{
		errorFile = filePath; 
	}
}
