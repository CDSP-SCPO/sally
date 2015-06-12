package fr.cdsp.sally;

/**
 * Classe gerant les constantes du programme
 * @author raphael
 *
 */

public class Constants 
{
	public static final String pathSeparator = getPathSeparator();
	
	/**
	 * detecte si on est sous linux et choisit le separateur de fichiers
	 * @return
	 */
	private static String getPathSeparator()
	{
		if (System.getProperty("os.name").toLowerCase().equals("linux"))
		{
			return "/";
		}
		else
		{
			return "\\";
		}
		
	}
}
