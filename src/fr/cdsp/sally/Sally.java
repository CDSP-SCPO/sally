package fr.cdsp.sally;

import java.util.ArrayList;
import java.util.List;

import fr.cdsp.sally.FileExplorer;
import fr.cdsp.sally.FileDescriptor;
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
public class Sally 
{
	public static void main(String [] args) throws Exception
	{
		
		String workingFolder = args[0];
		String validatorCSV = args[1];
		System.out.println("Recherche des fichier pdf");
		FileExplorer fe = new FileExplorer(workingFolder);
		Validator validator = new Validator(validatorCSV);
		
		List<FileDescriptor> fileList = fe.explore();
		List<FileDescriptor> excludeList = new ArrayList<>();
		
		System.out.println(fileList.size() + " pdf found");
		int i = 0;
		
		for (FileDescriptor file : fileList)
		{
			i++;
			System.out.println("recuperation des meta donnees " + i + "/" + fileList.size());
			try 
			{
				file.hydateMetadata();
			} 
			catch (UnreadableFileException e)
			{
				excludeList.add(file);
			}
		}
		
		i = 0;
		System.out.println("checking metadata...");
		for (FileDescriptor file : fileList)
		{
			i++;
			System.out.println("checking metadata " + i + "/" + fileList.size());
			if(excludeList.contains(file))
			{
				System.out.println("ignored file" + file.getFileName());
			}
			else
			{
				validator.validate(file);
			}
		}
		
		System.out.println("writting reports");
		CSVWrapper csvReport = new CSVWrapper(workingFolder + Constants.pathSeparator + "report.csv");
		CSVWrapper controlePDF = new CSVWrapper(workingFolder + Constants.pathSeparator + "controle_pdf.csv");
		//on ecrit les headers des deux 
		String[] controlePDFLine = {"Bibliothèque de SCIENCES PO (Paris)", "Total des erreurs majeures du format PDF", "Validation du contrôle du format PDF"," "," "};
		controlePDF.writeLine(controlePDFLine);
		controlePDFLine[0] = "Prestataire : Azentis";
		controlePDFLine[1] = countErrorFile(fileList);
		controlePDFLine[2] = " ";
		controlePDF.writeLine(controlePDFLine);
		controlePDFLine[0] = " ";
		controlePDFLine[1] = " ";
		controlePDF.writeLine(controlePDFLine);
		controlePDFLine[0] = "Nbre de volumes : " + fileList.size();
		controlePDF.writeLine(controlePDFLine);
		controlePDFLine[0] = " ";
		controlePDF.writeLine(controlePDFLine);
		controlePDF.writeLine(controlePDFLine);
		controlePDF.writeLine(controlePDFLine);
		controlePDF.writeLine(controlePDFLine);
		controlePDFLine[0] = "N°identifiant";
		controlePDFLine[1] = "Nombre de fichiers à contrôler";
		controlePDFLine[2] = "Format de fichier non respecté";
		controlePDFLine[3] = "Initiales contrôleur";
		controlePDFLine[4] = "Observations";
		controlePDF.writeLine(controlePDFLine);
		String[] header = {"Nom du fichier","Lisible", "Valide", "Date", "Identifier", "Langue", "Title"};
		csvReport.writeLine(header);
		//on ecrit le slignes pour els deux
		for (FileDescriptor file : fileList)
		{
			i = 0;
			String[] line = new String[7];
			line[i++] = file.getFileName();
			line[i++] = !excludeList.contains(file) ? "true" : "false";
			line[i++] = file.getFormatStatus();
			line[i++] = file.isDateStatus() ? "true" : "false";
			line[i++] = file.isIdentifierStatus() ? "true" : "false";
			line[i++] = file.isLanguageStatus() ? "true" : "false";
			line[i++] = file.isTitleStatus() ? "true" : "false";
			csvReport.writeLine(line);
			i = 0;
			String[] controlePDFDataLine = new String[5];
			controlePDFDataLine[i++] = file.getGeneratedIdentifier();
			controlePDFDataLine[i++] = "1";
			controlePDFDataLine[i++] = file.isPerfect() ? "0" : "1";
			controlePDFDataLine[i++] = "Sally";
			controlePDFDataLine[i++] = "";
			controlePDF.writeLine(controlePDFDataLine);
			
		}
		csvReport.close();
		controlePDF.close();
		System.out.println("end");
	}
	
	private static String countErrorFile(List<FileDescriptor>fileList)
	{
		int i = 0;
		for (FileDescriptor file : fileList)
		{
			if(! file.isPerfect())
			{
				i++;
			}
		}
		return (i + "");
	}
}
