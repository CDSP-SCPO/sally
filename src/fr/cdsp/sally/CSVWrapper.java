package fr.cdsp.sally;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVWrapper 
{
	protected String path;
	protected String separator = ",";
	protected String grouper = "\"";
	
	private boolean open;
	BufferedWriter bufferedWriter;
	public CSVWrapper(String path)
	{
		this.path = path;
		this.open = false;
	}
	
	/**
	 * Methode pour écrire une ligne dans un fichier csv, elle l'ouvre si besoin
	 * @param line le tableau des cases a creer
	 * @throws IOException 
	 */
	public void writeLine(String[] line) throws IOException
	{
		this.open();
		String printableLine = "";
		for (int i = 0; i < line.length; i++)
		{
			String thisLine = new String(line[i]);
			thisLine = thisLine.replace(grouper, grouper+grouper);
			thisLine = grouper + thisLine + grouper;
			if(i != 0)
			{
				printableLine += ",";
			}
			printableLine += thisLine;
		}
		printableLine += '\n';
		this.bufferedWriter.write(printableLine);
	}
	
	/**
	 * Methode pour parser un fichier csv
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> read() throws Exception
	{
		BufferedReader br = null;
 
		String currentLine;
 		br = new BufferedReader(new FileReader(this.path));
 		List<List<String>> csvContent = new ArrayList<>();
 		
 		while ((currentLine = br.readLine()) != null) 
		{
			String[] lineBlock = currentLine.split(",");
			List<String> cleanLinkeBlock = new ArrayList<>();
			for(int i = 0; i < lineBlock.length; i++)
			{
				if(lineBlock[i].startsWith("\""))
				{
					String currentBlock = "";
					for(; i<lineBlock.length; i++)
					{
						currentBlock += lineBlock[i];
						if(lineBlock[i].endsWith("\""))
						{
							break;
						}
					}
					if (currentBlock.length() > 2)
					{
						currentBlock = currentBlock.substring(1);
						currentBlock = currentBlock.substring(0, currentBlock.length() - 1);
					}
					if (currentBlock.contains("\"\""))
					{
						currentBlock = currentBlock.replaceAll("\"\"","\"");
					}
					cleanLinkeBlock.add(currentBlock);
				}
				else
				{
					if (lineBlock[i].contains("\"\""))
					{
						lineBlock[i] = lineBlock[i].replaceAll("\"\"","\"");
					}
					cleanLinkeBlock.add(lineBlock[i]);
				}
			}
			csvContent.add(cleanLinkeBlock);
		}
		try 
		{
			if (br != null)
			{
				br.close();
			}
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
		return csvContent;
	}
	
	/**
	 * methode prive gerant l'ouverture si besoin du fichier
	 * @throws IOException 
	 */
	private void open() throws IOException
	{
		if(!this.open)
		{
			File file = new File(this.path);
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			this.bufferedWriter = new BufferedWriter(fileWriter);
			this.open = true;
		}
	}
	
	public void close()
	{
		if(this.open && this.bufferedWriter != null)
		{
			try 
			{
				this.bufferedWriter.flush();
				this.bufferedWriter.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			this.open = false;
		
		}
	}
}
