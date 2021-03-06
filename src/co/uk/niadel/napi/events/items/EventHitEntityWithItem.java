package co.uk.niadel.napi.events.items;

import co.uk.niadel.napi.events.IEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import co.uk.niadel.napi.events.EventCancellable;

/**
 * Fired when an entity hits another with an item like a Sword. This allows for entities to only 
 * take damage from one type of item.
 * @author Niadel
 *
 */
public class EventHitEntityWithItem extends EventCancellable implements IEvent
{
	/**
	 * The item the hitee was hit with.
	 */
	public ItemStack item;
	
	/**
	 * The entity that hit and the one being hit respectively.
	 */
	public EntityLivingBase hitter, hitee;
	
	public EventHitEntityWithItem(ItemStack item, EntityLivingBase hitter, EntityLivingBase hitee)
	{
		this.item = item;
		this.hitter = hitter;
		this.hitee = hitee;
	}
	
	/**
	 * Gets the item from the ItemStack (Eg. if the ItemStack is that of a sword, this will return an ItemSword object).
	 * @return The Item Object mentioned above.
	 */
	public Item getItem()
	{
		return this.item.getItem();
	}
}
