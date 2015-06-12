package fr.cdsp.sally;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.cdsp.sally.FileDescriptor;
import fr.cdsp.sally.Constants;
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
public class FileExplorer 
{
	private String path;
	
	public FileExplorer(String path)
	{
		this.path = path;
	}
	
	public List<FileDescriptor> explore()
	{
		return explore(path);
	}
	
	/**
	 * Méthode parcourant l'integralité du dossier et de ses sous-dossiers pour en sortir tout les fichiers finissant pas *.pdf
	 * @param path le chemin du dossier a examiner
	 * @return
	 */
	private List<FileDescriptor> explore(String path)
	{
		File folder = new File(path);
		String[] listOfFiles = folder.list();
		
		ArrayList<FileDescriptor> fileNameList = new ArrayList<>();
		for(String file : listOfFiles)
		{
			File candidate = new File(folder.getPath() + Constants.pathSeparator + file);
			if(candidate.isFile())
			{
				if (candidate.getName().endsWith(".pdf"))
				{
					fileNameList.add(new FileDescriptor(candidate.getParentFile().getPath(), candidate.getName()));
				}
			}
			else
			{
				fileNameList.addAll(explore(candidate.getParent() + Constants.pathSeparator + candidate.getName()));
			}
		}
		return fileNameList;
	}
	
}
