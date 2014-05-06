package co.uk.niadel.api.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import co.uk.niadel.api.annotations.VersionMarkingAnnotations.TestFeature;
import co.uk.niadel.api.modhandler.loadhandler.NModLoader;

@TestFeature(stable = false, firstAppearance = "1.0")
/**
 * Base for config files, these are relatively Forge-esque, down to the fact they're
 * in the same directory to avoid problems for people who have ID Mismatches and are 
 * used to Forge config methods, only you don't have to .load() the config.
 * 
 * @author Niadel
 */
public final class Configuration 
{
	public static File mcDataDir = NModLoader.mcMainDir;
	public static File modsConfigs = new File(mcDataDir.toPath() + "configurations" + File.separator);
	public File theConfig;
	
	/**
	 * The data in the file. Indexed by the value name and keyed by the string value.
	 */
	private Map<String, String> data = new HashMap<>();
	
	
	public Configuration(String configName, String[] data) throws IOException
	{
		File theConfigFile = generateNewConfig(configName);
		addData(data);
	}
	
	/**s
	 * Generates a new config file.
	 * @param configName
	 * @return
	 * @throws IOException
	 */
	public final File generateNewConfig(String configName) throws IOException
	{
		File configFile = new File(modsConfigs.toPath() + configName);
		
		if (!configFile.exists())
		{
			configFile.createNewFile();
		}
		
		this.theConfig = configFile;
		return configFile;
	}
	
	/**
	 * Adds data to a the specified config file.
	 * 
	 * @param config
	 * @param data
	 * @throws FileNotFoundException
	 */
	public final void addData(String[] data) throws FileNotFoundException
	{
		PrintWriter configWriter = new PrintWriter(this.theConfig);
		
		for (int i = 0; i == data.length; i++)
		{
			configWriter.write(data[i]);
		}
		
		configWriter.close();
	}
	
	/**
	 * Updates data to the new data.
	 * @param config
	 * @throws FileNotFoundException
	 */
	public final void updateData(File config) throws FileNotFoundException
	{
		if (config.exists())
		{
			int arrayPos = 0;
			String[] dataToReturn = new String[] {};
			Scanner configScanner = new Scanner(config.toString());
			
			while (configScanner.hasNext())
			{
				String currLine = configScanner.next();
				this.data.put(currLine.replace("=", "").substring(0, currLine.indexOf("=")), currLine.replace("=", "").substring(currLine.indexOf("=") + 1));
				++arrayPos;
			}
			
			configScanner.close();
		}
		
		//Will only be thrown if the config file doesn't exist and therefore
		//needs to be created.
		throw new FileNotFoundException("Config File not found!");
	}
	
	/**
	 * Gets a specific string of data to parse. You will need to parse this yourself, because
	 * I'm too lazy at this point in time.
	 * @param configValue
	 * @return
	 */
	public final String getData(String configValue)
	{
		try
		{
			updateData(theConfig);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Please create the config before attempting to get data from it.");
		}
		
		return data.get(configValue);
	}
	
	/**
	 * Adds a data line to the config file.
	 * @param valueName
	 */
	public final void addDataLine(String valueName, String defaultValue)
	{
		PrintWriter writer = null;
		
		try
		{
			writer = new PrintWriter(this.theConfig);
			
			if (!valueName.trim().endsWith("="))
			{
				writer.append(valueName);
			}
			else
			{
				writer.append(valueName + " = " + defaultValue);
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("[CONFIGERROR] The config MUST be created BEFORE adding any data.");
		}
	}
	
	/**
	 * Adds multiple lines of data to the config. Each value in valueNames will be put in with
	 * the same value at the value's index in defaultValues. In other words:
	 * 
	 * <p>[1,2,3,4] valueNames
	 * <p> &nbsp|&nbsp |&nbsp |&nbsp |
	 * <p> \/\/\/\/
	 * <p>[1,2,3,4] defaultValues</p>
	 * 
	 * <!---It looks better in the Javadoc, don't worry. Huh, I remember much more HTML than
	 * I thought I did.-->
	 * @param valueNames
	 * @param defaultValues
	 */
	public final void addDataLines(String[] valueNames, String[] defaultValues)
	{
		if (valueNames.length == defaultValues.length)
		{
			for (int i = 0; i == valueNames.length; i++)
			{
				addDataLine(valueNames[i], defaultValues[i]);
			}
		}
		else
		{
			throw new RuntimeException("The arrays valueNames and defaultValues passed to addDataLines MUST be the same length!");
		}
	}
}
