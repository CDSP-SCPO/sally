package fr.cdsp.sally;


import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
//pour lire le xml
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
//pour les pdf
import com.lowagie.text.pdf.PdfReader;
//pour le format pdf
import javax.activation.FileDataSource;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;

import fr.cdsp.sally.Constants;
import fr.cdsp.sally.UnreadableFileException;

/**
 * classe servant simplement a représenter un fichier sous une forme plus utilisable et avec toutes les 
 * metadonnées dont on peut avoir besoin dans le process d'analyse
 * @author raphael
 *
 */
public class FileDescriptor 
{
	//file informations
	private String fileName;
	private String filePath;
	private String folder;
	private String generatedIdentifier;
	
	//metadata
	private String date = "";
	private String title = "";
	private String identifier = "";
	private String language = "";
	
	//metadata status
	private boolean dateStatus;
	private boolean titleStatus;
	private boolean identifierStatus;
	private boolean languageStatus;
	private String formatStatus = "";
	
	public FileDescriptor(String folder, String fileName)
	{
		this.folder = folder;
		this.fileName = fileName;
		this.filePath = folder + Constants.pathSeparator + fileName;
		this.generatedIdentifier = this.fileName.substring(0, this.fileName.length() - 4 );
	}

	
	public void hydateMetadata() throws UnreadableFileException
	{
		PdfReader reader = null;
		try
		{
			reader = new PdfReader(filePath);
		}
		catch(Exception e)
		{
			System.out.println("cannot open pdf " + filePath);
		}
	    byte metadataByte[] = null;
	    try 
	    {
			metadataByte = reader.getMetadata();
			if(metadataByte != null)
			{
				parseXMLmetadata(new String(metadataByte, "UTF-8"));
			}
		} 
	    catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	    catch(NullPointerException e)
	    {
	    	throw new UnreadableFileException();
	    }
	    try
	    {
	    	reader.close();
	    }
	    catch (Exception e)
	    {
	    	System.out.println("cannot close");
	    }
	}
	
	
	public void validatePdfFormat()
	{
		ValidationResult result = null;

		FileDataSource fd = new FileDataSource(this.filePath);
		try 
		{
			PreflightParser parser = new PreflightParser(fd);
			parser.parse();
			PreflightDocument document = parser.getPreflightDocument();
			document.validate();
			result = document.getResult();
			document.close();
		}
		catch (IOException e)
		{
			this.formatStatus = "unparsable";
		}
		catch(NullPointerException e)
		{
			this.formatStatus = "unparsable";
		}
		catch(Exception e)
		{
			this.formatStatus = "unparsable";
		}
		if (result != null && result.isValid())
		{
			this.formatStatus = "true";
		}
		else if(result == null)
		{
			this.formatStatus = "unparsable";
		}
		else
		{
			this.formatStatus = "false";
		}
	}
	
	
	/**
	 * Methode parsant les metadonnes xml du pdf et remplissant les champs correspondants dans l'objet
	 * @param xmlMetadata
	 */
	private void parseXMLmetadata(String xmlMetadata)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = null;
		try 
		{
			builder = factory.newDocumentBuilder();
		} 
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	    InputSource is = new InputSource(new StringReader(xmlMetadata));
	    Document doc = null;
	    try
	    {
			doc = builder.parse(is);
		} 
	    catch (SAXException | IOException e) 
	    {
			e.printStackTrace();
		}
	    doc.getDocumentElement().normalize();
	    Element root = doc.getDocumentElement();
	    Node descriptionNode =  root.getFirstChild().getNextSibling().getFirstChild().getNextSibling();
	    NodeList descriptionChlidren = descriptionNode.getChildNodes();
	    for(int i = 0; i < descriptionChlidren.getLength(); i++)
	    {
	    	Node node = descriptionChlidren.item(i);
	    	if(node.getNodeName() == "dc:identifier")
	    	{
	    		this.identifier = node.getTextContent();
	    	}
	    	else if(node.getNodeName() == "dc:date")
	    	{
	    		this.date = node.getTextContent().trim();
	    	}
	    	else if(node.getNodeName() == "dc:language")
	    	{
	    		this.language = node.getTextContent().trim();
	    	}
	    	else if(node.getNodeName() == "dc:title")
	    	{
	    		this.title = node.getTextContent().trim();
	    	}
	    }
	    
	}
	
	public boolean isPerfect()
	{
		if(!this.dateStatus || !this.identifierStatus || !this.languageStatus || !this.titleStatus)
		{
			return false;
		}
		if(this.formatStatus != "true")
		{
			return false;
		}
		
		return true;
	}
	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFolder() {
		return folder;
	}

	public String getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getLanguage() {
		return language;
	}


	public boolean isDateStatus() {
		return dateStatus;
	}


	public boolean isTitleStatus() {
		return titleStatus;
	}


	public boolean isIdentifierStatus() {
		return identifierStatus;
	}


	public boolean isLanguageStatus() {
		return languageStatus;
	}

	public String getFormatStatus() {
		return formatStatus;
	}

	public String getGeneratedIdentifier() {
		return generatedIdentifier;
	}


	public void setDateStatus(boolean dateStatus) {
		this.dateStatus = dateStatus;
	}


	public void setTitleStatus(boolean titleStatus) {
		this.titleStatus = titleStatus;
	}


	public void setIdentifierStatus(boolean identifierStatus) {
		this.identifierStatus = identifierStatus;
	}


	public void setLanguageStatus(boolean languageStatus) {
		this.languageStatus = languageStatus;
	}

}
