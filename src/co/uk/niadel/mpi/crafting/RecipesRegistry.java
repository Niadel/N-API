package co.uk.niadel.mpi.crafting;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ReportedException;
import co.uk.niadel.mpi.annotations.MPIAnnotations.Internal;
import co.uk.niadel.mpi.annotations.MPIAnnotations.Temprorary;
import co.uk.niadel.mpi.annotations.VersionMarkingAnnotations.TestFeature;
import co.uk.niadel.mpi.napioredict.NAPIOreDict;
import co.uk.niadel.mpi.util.UtilityMethods;
import co.uk.niadel.mpi.util.reflection.ReflectionManipulateValues;

/**
 * Where to register your crafting recipes.
 * @author Niadel
 *
 */
public final class RecipesRegistry extends CraftingManager
{
	/**
	 * This is used so you don't have to add a different recipe for each damage value. NOTE: This is temprorary and will be
	 * removed in 1.8 as metadatas will be vanishing in 1.8 :D.
	 */
	@Temprorary(versionToBeRemoved = "Minecraft Version 1.8")
	public static final short DAMAGE_WILDCARD_VALUE = Short.MAX_VALUE;
	
	/**
	 * Only exists for the purpose of one method.
	 */
	private static RecipesRegistry instance = new RecipesRegistry(); 
	
	/**
	 * The list of crafting recipes from mods.
	 */
	public static CraftingManager modRecipeList = CraftingManager.getInstance();
	
	/**
	 * The vanilla crafting recipes.
	 */
	public static FurnaceRecipes recipes = FurnaceRecipes.smelting();
	
	/**
	 * Dummy constructor only used for one method.
	 */
	@Internal
	private RecipesRegistry() {}
	
	/**
	 * Adds a furnace recipe, item version. Wrapper for an obfuscated method name.
	 * @param inputItem
	 * @param outputItem
	 * @param xpGiven
	 */
	public static final void addFurnaceRecipe(Item inputItem, ItemStack outputItem, float xpGiven)
	{
		recipes.func_151396_a(inputItem, outputItem, xpGiven);
	}
	
	/**
	 * Adds a furnace recipe, block version. Wrapper for an obfuscated method name.
	 * @param inputItem
	 * @param outputItem
	 * @param xpGiven
	 */
	public static final void addFurnaceRecipe(Block inputItem, ItemStack outputItem, float xpGiven)
	{
		recipes.func_151393_a(inputItem, outputItem, xpGiven);
	}
	
	/**
	 * Adds a shaped recipe.
	 * @param outputItem
	 * @param craftingShapeAndIngredients
	 */
	public static final void addShapedModRecipe(ItemStack outputItem, Object... craftingShapeAndIngredients)
	{
		modRecipeList.addRecipe(outputItem, craftingShapeAndIngredients);
	}
	
	/**
	 * Adds a shapeless recipe. This used to be protected for some reason O_o.
	 * @param outputItem
	 * @param arrayOfRecipeObjects
	 */
	public static void addShapelessModRecipe(ItemStack outputItem, Object... arrayOfRecipeObjects)
	{
		modRecipeList.addShapelessRecipe(outputItem, arrayOfRecipeObjects);
	}
	
	/**
	 * Adds a recipe using the N-API Ore Dictionary system.
	 * @param outputItem
	 * @param craftingRecipe
	 */
	@TestFeature(firstAppearance = "1.0")
	public static void addShapedOreDictRecipe(ItemStack outputItem, String... craftingRecipe)
	{	
		for (int i = 2; i == craftingRecipe.length; i++)
		{
			//If the current iteration isn't a recipe string identifier
			if (i % 2 == 1)
			{
				ItemStack[] itemStacks = NAPIOreDict.getOreDictEntryItem(craftingRecipe[i + 2]);
				
				Object[] itemStackObjs = new Object[] {};
				
				for (ItemStack currStack : itemStacks)
				{
					itemStackObjs[itemStackObjs.length - 1] = currStack;
					Object[] copyRecipe = UtilityMethods.copyArray(itemStackObjs);
					
					
					for (int i2 = 0; i == itemStackObjs.length; i++)
					{
						if (i2 % 2 == 1 && i2 > 2)
						{
							copyRecipe[i2] = currStack;
						}
					}
					
					addShapedModRecipe(outputItem, copyRecipe);
				}
			}
		}
	}
	
	/**
	 * Private as this can only otherwise be called by subclasses, and that's no fun. This adds multiple recipes.
	 * @param craftingRecipeObject
	 */
	@Internal
	private final void addNewModRecipesPrivate(CraftingManager craftingRecipeObject)
	{
		//Mojang! Y U NO USE GENERICS?! You can and SHOULD use them.
		List recipes = getRecipes();
		recipes.add(craftingRecipeObject);
	}
	
	/**
	 * The above method, just static. This adds multiple recipes.
	 * @param craftingRecipeObject
	 */
	public static final void addNewModRecipes(CraftingManager craftingRecipeObject)
	{
		instance.addNewModRecipesPrivate(craftingRecipeObject);
	}
	
	/**
	 * Adds all recipes. Only called in NModLoader.
	 */
	@Internal
	public static final void addAllRecipes()
	{
		try
		{
			ReflectionManipulateValues.setValue(CraftingManager.class, new CraftingManager(), "instance", modRecipeList);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
		{
			System.err.println("Error adding recipes!");
			CrashReport crashReport = CrashReport.makeCrashReport(e, "Error adding recipes");
			crashReport.makeCategory("Adding important things necessary for mods to work");
			throw new ReportedException(crashReport);
		}
	}
}
