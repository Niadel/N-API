package co.uk.niadel.napi.events.entity;

import co.uk.niadel.napi.events.IEvent;
import net.minecraft.entity.Entity;

/**
 *	Fired when an entity picks up an item.
 */
public class EventEntityPickupItem implements IEvent
{
	public Entity entity;

	public int stackSize;

	public EventEntityPickupItem(Entity entity, int stackSize)
	{
		this.entity = entity;
		this.stackSize = stackSize;
	}
}
