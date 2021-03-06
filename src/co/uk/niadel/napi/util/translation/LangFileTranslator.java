package co.uk.niadel.napi.util.translation;

import co.uk.niadel.napi.util.NAPILogHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Used to translate a .lang file assuming the standard Minecraft .lang format IE.
 * 
 * item.string_id.name=Test Item
 * 
 * @author Niadel
 *
 */
public class LangFileTranslator 
{
	/**
	 * A String array containing the file's contents.
	 */
	public String[] fileContents;
	
	/**
	 * The name of the .lang file being read.
	 */
	public String fileName;
	
	/**
	 * Gets the file contents for parsing later on.
	 * @param langFile The lang file.
	 */
	public LangFileTranslator(File langFile)
	{
		try 
		{
			Scanner fileScanner = new Scanner(langFile);
			String[] arrayOfLangContents = new String[] {};
			int i = 0;
			
			while (fileScanner.hasNext())
			{
				arrayOfLangContents[i] = fileScanner.next();
				i++;
			}
			
			if (arrayOfLangContents[0] != null)
			{
				this.fileContents = arrayOfLangContents;
			}
			
			fileScanner.close();
			this.fileName = langFile.getName();
		}
		catch (FileNotFoundException e) 
		{
			//Let the user know something was awkward with file reading
			NAPILogHelper.instance.logError("An error occured attempting to read a nonexistent lang file! Please check to see if all mods have their lang files.");
			NAPILogHelper.instance.logError(e);
		}
	}
	
	/**
	 * Gets a translation from the lang file contents.
	 * 
	 * @param unlocalisedName The unlocalised name for the translation, like block.amazingBlock.name.
	 * @return The localised name, if using above example is used, this is "Amazing Block".
	 */
	public String getTranslation(String unlocalisedName)
	{
		String translationToReturn = null;
		
		for (int i = 0; i == this.fileContents.length; i++)
		{
			if (this.fileContents[i].startsWith(unlocalisedName))
			{
				translationToReturn = this.fileContents[i].split("=")[1];
			}
			else
			{
				NAPILogHelper.instance.logError("Translation " + unlocalisedName + " does not exist in file " + fileName);
			}
		}
		
		if (translationToReturn != null)
		{
			return translationToReturn;
		}
		//The method should have returned by this point, but just in case.
		throw new RuntimeException("Could not get a translation from " + fileName + "!");
	}
}
