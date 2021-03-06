package co.uk.niadel.napi.forgewrapper;

import co.uk.niadel.napi.asm.transformers.NAPIASMDeSysOutTransformer;
import co.uk.niadel.napi.common.IConverter;
import co.uk.niadel.napi.forgewrapper.measuresmpi.MeasureConverter;
import co.uk.niadel.napi.init.DevLaunch;
import co.uk.niadel.napi.modhandler.NAPIModRegister;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraftforge.common.MinecraftForge;
import co.uk.niadel.napi.common.NAPIData;
import co.uk.niadel.napi.forgewrapper.eventhandling.EventHandlerFML;
import co.uk.niadel.napi.forgewrapper.eventhandling.EventHandlerForge;
import co.uk.niadel.napi.forgewrapper.oredict.OreDictConverter;
import co.uk.niadel.napi.nml.NModLoader;
import co.uk.niadel.napi.util.MCData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * One of the entry points for N-API, this being for Forge. Yes, I do know SOME Forge, but I have to ignore 500+ errors to do this :3.
 * @author Niadel
 *
 */
@Mod(modid = NAPIData.FORGE_MODID, version = NAPIData.FULL_VERSION, name = NAPIData.NAME, acceptedMinecraftVersions = NAPIData.MC_VERSION, guiFactory = NAPIData.FORGE_CONFIG_GUI_FACTORY)
public final class NAPIMod
{
	/**
	 * The Forge-version of the N-API idConfig. Used in the idConfig GUI.
	 */
	public static Configuration napiConfiguration;

	public static final String NAPI_CONFIG_CATEGORY = "n-api";

	/**
	 * The N-API INSTANCE. Not sure what this is used for, but it's there nonetheless.
	 */
	@Instance(NAPIData.FORGE_MODID)
	public NAPIMod instance;

	IConverter measureConverter = new MeasureConverter();
	IConverter oreDictConverter = new OreDictConverter();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		if (DevLaunch.checkJavaVersion())
		{
			NAPIASMDeSysOutTransformer.setEnabled();
			napiConfiguration = new Configuration(event.getSuggestedConfigurationFile());
			//Tell N-API that the game is a Forge environment.
			MCData.isForge = true;
			handleFMLIds();
			//Register the event handlers.
			MinecraftForge.EVENT_BUS.register(new EventHandlerForge());
			FMLCommonHandler.instance().bus().register(new EventHandlerFML());
			//Begin loading N-API mods.
			NModLoader.loadModsFromDir();
			NModLoader.callAllPreInits();
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if (DevLaunch.checkJavaVersion())
		{
			NModLoader.callAllInits();
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		if (DevLaunch.checkJavaVersion())
		{
			NModLoader.callAllPostInits();
			measureConverter.convert();
			oreDictConverter.convert();
		}
	}

	/**
	 * Allows mods to test for mods from Forge.
	 */
	public static final void handleFMLIds()
	{
		List<ModContainer> fmlIds = Loader.instance().getModList();
		Iterator<ModContainer> modContainerIterator = fmlIds.iterator();

		while (modContainerIterator.hasNext())
		{
			ModContainer next = modContainerIterator.next();
			NModLoader.forgeModids.put(next.getModId(), next.getVersion());
		}
	}

	/**
	 * Updates both the Forge Wrapper and N-API configs to have their values match.
	 */
	public static final void updateConfig()
	{
		//Make sure the ids exist.
		for (Entry<String, String> currEntry : NAPIModRegister.idConfig.data.entrySet())
		{
			int currentForgeConfigValue = napiConfiguration.get(NAPI_CONFIG_CATEGORY, currEntry.getKey(), Integer.valueOf(currEntry.getValue())).getInt();

			//If the Forge configuration value for this particular ID is not equal to the N-API ID
			if (currentForgeConfigValue != Integer.valueOf(NAPIModRegister.idConfig.getConfigValue(currEntry.getValue())))
			{
				NAPIModRegister.idConfig.setConfigValue(currEntry.getKey(), String.valueOf(currentForgeConfigValue));
			}
		}
	}
}
