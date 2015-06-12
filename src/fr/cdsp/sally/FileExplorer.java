package fr.cdsp.sally;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.cdsp.sally.FileDescriptor;
import fr.cdsp.sally.Constants;

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
