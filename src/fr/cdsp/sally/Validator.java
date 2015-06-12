package fr.cdsp.sally;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Validator 
{
	private Map<String, String> titleIdentifierMap;
	Validator(String CSVPath) throws Exception
	{
		CSVWrapper csv = new CSVWrapper(CSVPath);
		titleIdentifierMap = titleIdentifierMapGenerator(csv.read());
	}
	
	public void validate(FileDescriptor file)
	{
		String fileIdentifier = file.getFileName().substring(0, file.getFileName().length() - 4);
		file.setIdentifierStatus(fileIdentifier.equals(file.getIdentifier()));
		file.setTitleStatus(titleIdentifierMap.get(fileIdentifier) != null && titleIdentifierMap.get(fileIdentifier).equals(file.getTitle()));
		file.setLanguageStatus(validateLanguage(file.getLanguage()));
		file.setDateStatus(validateDate(file.getDate()));
		file.validatePdfFormat();
	}
	
	private boolean validateDate(String date)
	{
		if (date.length() != 10)
		{
			return false;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTime = null; 
		Date today = new Date();
		try 
		{
			dateTime = formatter.parse(date);
		} 
		catch (ParseException e) 
		{
			return false;
		}
		if (dateTime.getYear() < 50)
		{
			return false;
		}
		if(dateTime.getYear() > today.getYear())
		{
			return false;
		}
		return true;
	}
	
	private boolean validateLanguage(String language)
	{
		if(language.length() != 2)
		{
			return false;
		}
		String lowerLanguage = language.toLowerCase();
		if((int)lowerLanguage.charAt(0) > 122)
		{
			return false;
		}
		if((int)lowerLanguage.charAt(0) < 97)
		{
			return false;
		}
		if((int)lowerLanguage.charAt(1) > 122)
		{
			return false;
		}
		if((int)lowerLanguage.charAt(1) < 97)
		{
			return false;
		}
		return true;
	}
	

	private Map<String, String> titleIdentifierMapGenerator(List<List<String>> CSV)
	{
		Map<String, String> titleIdentifierMap = new HashMap<>();
		boolean startReading = false;
		for(int i = 0; i < CSV.size(); i++)
		{
			//on attend al fin des headers
			if(!startReading)
			{
				if(CSV.get(i).get(0) != "N°identifiant")
				{
					startReading = true;
				}
				continue;
			}
			if(CSV.get(i).size() > 10)
			{
				titleIdentifierMap.put(CSV.get(i).get(0), CSV.get(i).get(10));
			}
		}
		return titleIdentifierMap;
	}
}
