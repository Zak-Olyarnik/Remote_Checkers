
public class Client
{
	public static String configFile = "res/config.txt";
	
	// driver
	public static void main(String[] args)
	{
		UI.setFrame();
		MainScreen.getMainScreen().execute();
	}
}