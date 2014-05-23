package co.uk.niadel.api.forgewrapper.eventhandling;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import co.uk.niadel.api.events.EventCancellable;
import co.uk.niadel.api.events.EventsList;
import co.uk.niadel.api.events.entity.EventEntityDeath;
import co.uk.niadel.api.events.entity.EventEntitySpawned;
import co.uk.niadel.api.events.entity.EventEntityStruckByLightning;
import co.uk.niadel.api.events.items.EventItemDespawned;
import co.uk.niadel.api.events.server.EventPlayerChat;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Used so I don't have to ASM as many events in.
 * @author Niadel
 *
 */
public class EventHandlerForge
{
	@SubscribeEvent
	public void onServerChat(ServerChatEvent event)
	{
		EventsList.fireEvent(new EventPlayerChat(event.player, event.message), "EventServerChatEvent");
	}
	
	@SubscribeEvent
	public void onEntityStruckByLightning(EntityStruckByLightningEvent event)
	{
		EventsList.fireEvent(new EventEntityStruckByLightning(event.lightning, event.entity), "EventEntityStruckByLightningEvent");
	}
	
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event)
	{
		EventCancellable deathEvent = new EventEntityDeath(event.entity);
		
		EventsList.fireEvent(deathEvent, "EventEntityDeath");
		
		if (deathEvent.isCancelled())
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntitySpawned(LivingSpawnEvent event)
	{
		EventCancellable eventEntitySpawn = new EventEntitySpawned(event.entity);
		
		EventsList.fireEvent(eventEntitySpawn, "EntityJoinWorldEvent");
		
		if (eventEntitySpawn.isCancelled())
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onItemDespawned(ItemExpireEvent event)
	{
		EventCancellable despawnEvent = new EventItemDespawned(event.entityItem);
		EventsList.fireEvent(despawnEvent, "EventItemDespawned");
		
		if (despawnEvent.isCancelled())
		{
			//COUGH COUGH WRONG SPELLING OF CANCELLED COUGH COUGH #blamelex
			event.setCanceled(true);
		}
	}
}