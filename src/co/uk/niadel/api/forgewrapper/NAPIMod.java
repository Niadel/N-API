package co.uk.niadel.api.forgewrapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import net.minecraftforge.common.MinecraftForge;
import co.uk.niadel.api.asm.ASMRegistry;
import co.uk.niadel.api.exceptions.MCreatorDetectedException;
import co.uk.niadel.api.forgewrapper.eventhandling.EventHandlerFML;
import co.uk.niadel.api.forgewrapper.eventhandling.EventHandlerForge;
import co.uk.niadel.api.modhandler.loadhandler.NModLoader;
import co.uk.niadel.api.util.GameDataAquisitionUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Yes, I do know SOME Forge, but I have to ignore 500+ errors to do this :3.
 * @author Niadel
 *
 */
@Mod(modid = "NIADEL_n_api", version = "1.0", name = "N-API", acceptedMinecraftVersions = "1.7.2")
public class NAPIMod
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		try
		{
			//Tell the game data that the game is a Forge environment.
			GameDataAquisitionUtils.isForge = true;
			//Register the event handlers.
			MinecraftForge.EVENT_BUS.register(new EventHandlerForge());
			FMLCommonHandler.instance().bus().register(new EventHandlerFML());
			//Begin loading N-API mods.
			NModLoader.loadModsFromDir();
			ASMRegistry.invokeAllTransformers();
			NModLoader.invokeRegisterMethods();
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | InstantiationException | IOException | MCreatorDetectedException e) //Oh sweet lawd the amount of exceptions :3
		{
			if (!(e instanceof MCreatorDetectedException))
			{
				//Let the user know stuff is broken.
				FMLLog.severe("SERIOUS ISSUE OCCURED LOADING N-API FORGE WRAPPER! EXCEPTION IS BELOW:");
				e.printStackTrace();
			}
			else
			{
				FMLLog.severe("MCREATOR DETECTED! NOT CONTINUING LOAD!");
			}
		}
	}
}
