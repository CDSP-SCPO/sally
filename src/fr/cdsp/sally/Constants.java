package fr.cdsp.sally;
/*
	Copyright 2015 
	Centre de données socio-politiques (CDSP)
	Fondation nationale des sciences politiques (FNSP)
	Centre national de la recherche scientifique (CNRS)
License
	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
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
