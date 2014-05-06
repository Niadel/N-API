package co.uk.niadel.api.block;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.RegistryNamespaced;
import co.uk.niadel.api.annotations.MPIAnnotations.RecommendedMethod;
import co.uk.niadel.api.annotations.VersionMarkingAnnotations.TestFeature;
import co.uk.niadel.api.items.EnumItemType;

/**
 * Where you register blocks. You can also extend this, but you don't have to.
 * @author Niadel
 */
public final class BlockRegistry extends Block
{
	private int numericId;
	private String nonNumericId;
	public String namespace;
	
	/**
	 * The type of item this block is. Used mainly to improve mods like Tinker's Construct.
	 */
	public EnumItemType itemType;
	private EnumBlockTypes type;
	
	/**
	 * Used to prevent a ClassCastException.
	 */
	public static RegistryNamespaced registry = getRegistry();
	
	public BlockRegistry(Material material)
	{
		super(material);
	}

	@RecommendedMethod
	/**
	 * Adds a standard block with the same method used in Block.java. I think you specify 
	 * the namespace in the nonNumericId, but don't hold me to that.
	 * 
	 * @param numericId
	 * @param nonNumericId
	 * @param block
	 */
	public static void addBlock(int numericId, String nonNumericId, Block block)
	{
		registry.addObject(numericId, nonNumericId, block);
	}
}
