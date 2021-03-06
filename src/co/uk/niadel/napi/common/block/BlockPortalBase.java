package co.uk.niadel.napi.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import co.uk.niadel.napi.annotations.DocumentationAnnotations.Dangerous;
import co.uk.niadel.napi.dimensions.DimensionIdRegistry;

public class BlockPortalBase extends Block
{
	/**
	 * The dimension id this portal will send you to.
	 */
	public int dimensionToTravelTo;

	//So I'm not seen using code that doesn't follow naming conventions.
	public static Material portal = Material.Portal;
	
	public BlockPortalBase(String dimensionIdString)
	{
		super(BlockPortalBase.portal);
		setFreeDimensionId(dimensionIdString);
	}
	
	/**
	 * Used when registering the block, instead of setting it with a specified id, it gets a free id from DimensionIdRegistry.
	 * @param dimensionIdString The String id, used in the N-API idConfig.
	 * @return This object for use in constructing.
	 */
	protected Block setFreeDimensionId(String dimensionIdString)
	{
		this.dimensionToTravelTo = DimensionIdRegistry.registerId(dimensionIdString);
		return this;
	}
	
	/**
	 * Forcibly sets the dimension id this portal sends you to.
	 * @param id
	 * @return
	 */
	@Dangerous(reason = "Compatibility breaking.")
	protected Block setDimensionId(int id)
	{
		this.dimensionToTravelTo = id;
		return this;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (entity.ridingEntity == null && entity.riddenByEntity == null && !world.isClient)
		{
			entity.travelToDimension(this.dimensionToTravelTo);
		}
	}
	
	@Override
	public boolean canCollideCheck(int metadata, boolean clickedWhileHoldingBoat)
	{
		return false;
	}
}
