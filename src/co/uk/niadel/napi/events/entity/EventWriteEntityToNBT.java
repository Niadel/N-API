package co.uk.niadel.napi.events.entity;

import co.uk.niadel.napi.events.IEvent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Fired when an entity is written to NBT. Can be used to add NBT to vanilla mobs.
 * @author Niadel
 *
 */
public class EventWriteEntityToNBT implements IEvent
{
	public NBTTagCompound tag;
	public Entity entity;
	
	public EventWriteEntityToNBT(NBTTagCompound tag, Entity entity)
	{
		this.tag = tag;
		this.entity = entity;
	}
}
