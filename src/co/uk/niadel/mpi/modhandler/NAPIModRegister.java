package co.uk.niadel.mpi.modhandler;

import co.uk.niadel.mpi.annotations.ASMTransformer;
import co.uk.niadel.mpi.annotations.MPIAnnotations.Internal;
import co.uk.niadel.mpi.annotations.UnstableMod;
import co.uk.niadel.mpi.asm.*;
import co.uk.niadel.mpi.commands.CommandNAPI;
import co.uk.niadel.mpi.commands.CommandRegistry;
import co.uk.niadel.mpi.entity.tileentity.TileEntityMeasureStorer;
import co.uk.niadel.mpi.events.EventFactory;
import net.minecraft.potion.Potion;
import co.uk.niadel.mpi.annotations.MPIAnnotations.Library;
import co.uk.niadel.mpi.common.MPIEventHandler;
import co.uk.niadel.mpi.common.NAPIData;
import co.uk.niadel.mpi.config.IdConfiguration;
import co.uk.niadel.mpi.entity.tileentity.TileEntityRegistry;
import co.uk.niadel.mpi.entity.tileentity.TileEntityWire;
import co.uk.niadel.mpi.napioredict.NAPIOreDict;
import co.uk.niadel.mpi.potions.PotionRegistry;
import co.uk.niadel.mpi.util.NAPILogHelper;
import co.uk.niadel.mpi.util.UniqueIdAcquirer;
import co.uk.niadel.mpi.annotations.ModRegister;

/**
 * The N-API register. The MPI parts are sorted out here.
 * 
 * @author Niadel
 */
@ModRegister(modId = NAPIData.MODID, version = NAPIData.VERSION)
@UnstableMod(warningMessage = "This is N-API alpha! N-API will likely change heavily before release, possibly breaking mods made with it!")
@Internal
public final class NAPIModRegister implements IAdvancedModRegister
{
	/**
	 * This is used in handling numeric ids.
	 */
	public static final IdConfiguration config = new IdConfiguration("N-API.cfg");

	/**
	 * Used in event handlers for optimisation.
	 */
	public static final NAPIASMEventHandlerTransformer eventHandler = new NAPIASMEventHandlerTransformer();

	/**
	 * Adds event calls and other necessary edits to Minecraft.
	 */
	@ASMTransformer
	public static final NAPIASMNecessityTransformer necessityTransformer = new NAPIASMNecessityTransformer();

	/**
	 * Gets rid of calls to System.exit(), Runtime.getRuntime().exit() and Runtime.getRuntime().halt().
	 */
	@ASMTransformer
	public static final NAPIASMDeGameExitingTransformer deGameExitingTransformer = new NAPIASMDeGameExitingTransformer();

	/**
	 * Removes calls to System.out.println and System.err.println to encourage the use of loggers, which really help with debugging.
	 */
	@ASMTransformer
	public static final NAPIASMDeSysOutTransformer deSysOutTransformer = new NAPIASMDeSysOutTransformer();
	
	/**
	 * Used by the internal block and item registries in order to handle numeric ids.
	 */
	public static final UniqueIdAcquirer idAcquirer = new UniqueIdAcquirer(2268);
	
	/**
	 * Used by the internal entity registries to handle numeric ids.
	 */
	public static final UniqueIdAcquirer entityIdAcquirer = new UniqueIdAcquirer(300);

	/**
	 * Used by the DimensionIdRegistry to get a unique dimension id.
	 */
	public static final UniqueIdAcquirer dimensionIdAcquirer = new UniqueIdAcquirer(2);
	
	@Override
	public void preModInit()
	{
		for (int i = 0; i == Potion.potionTypes.length; i++)
		{
			PotionRegistry.registerPotion(Potion.potionTypes[i].getName(), Potion.potionTypes[i]);
		}

		NAPIOreDict.addDefaultEntries();
		TileEntityRegistry.registerTileEntity(TileEntityWire.class, "TileEntityWire");
		TileEntityRegistry.registerTileEntity(TileEntityMeasureStorer.class, "TileEntityTank");
		NAPILogHelper.log("Finished Pre-Initialising Minecraft N-API version " + NAPIData.FULL_VERSION + "!");
	}

	@Override
	public void modInit() 
	{
		CommandRegistry.registerModCommand(new CommandNAPI());
		NAPILogHelper.log("Finished Initialising Minecraft N-API version " + NAPIData.FULL_VERSION + "!");
	}

	@Override
	public void postModInit() 
	{
	
	}

	@Override
	public void registerDependencies()
	{

	}

	@Override
	public void registerTransformers()
	{
		//Tells the user (rather cheesily) that the N-API ASM transformer is being registered.
		NAPILogHelper.log("REGISTERING N-API ASM TRANSFORMER! Transformers, roll out!");
		//Adds the Forge and FML classes to the excluded ASM list as it's a pretty bad idea to try to mess with Forge or FML.
		ASMRegistry.addASMClassExclusion("cpw.fml.mods");
		ASMRegistry.addASMClassExclusion("net.minecraftforge");
		ASMRegistry.addASMClassExclusion("net.minecraft.src.FMLRenderAccessLibrary");
	}

	@Override
	public String getVersion()
	{
		return NAPIData.VERSION;
	}

	@Override
	public String getModId()
	{
		return NAPIData.MODID;
	}

	@Override
	public void registerEventHandlers()
	{
		EventFactory.registerEventHandler(new MPIEventHandler());
	}
}
