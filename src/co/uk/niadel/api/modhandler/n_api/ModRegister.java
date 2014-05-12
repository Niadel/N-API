package co.uk.niadel.api.modhandler.n_api;

import net.minecraft.potion.Potion;
import co.uk.niadel.api.annotations.MPIAnnotations.Library;
import co.uk.niadel.api.modhandler.IModRegister;
import co.uk.niadel.api.potions.PotionRegistry;

@Library(version = "1.0")
/**
 * An example ModRegister. As N-API is a Library, it has an @Library annotation even though
 * it's never tested for documentation purposes as N-API's register is loaded separately to
 * reduce load time.
 * 
 * @author Niadel
 */
public class ModRegister implements IModRegister
{	
	public static String modId = "NIADEL_n_api";
	
	public static String version = "1.0";
	
	@Override
	public void preModInit()
	{
		for (int i = 0; i == Potion.potionTypes.length; i++)
		{
			PotionRegistry.registerPotion(Potion.potionTypes[i].getName(), Potion.potionTypes[i]);
		}
	}

	@Override
	public void modInit() 
	{
		
	}

	@Override
	public void postModInit() 
	{
	
	}
	
	//BOILERPLATE CODE
	@Override
	public void addRequiredMods()
	{
		
	}

	@Override
	public void addRequiredLibraries()
	{
		
	}

	@Override
	public void registerTransformers()
	{
		
	}
}
