package co.uk.niadel.napi.client;

import co.uk.niadel.napi.annotations.Internal;
import co.uk.niadel.napi.util.MCData;
import co.uk.niadel.napi.util.reflection.ReflectionManipulateValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for general client stuff.
 *
 * @author Niadel
 */
public final class ClientRegistry
{
	public static Map<Class<? extends Render>, Render> rendersMap = new HashMap<>();

	/**
	 * Adds a key binding.
	 * @param keyBinding
	 */
	public static final void addKeyBinding(KeyBinding keyBinding)
	{
		if (MCData.isClientSide())
		{
			KeyBinding[] theBindings = Minecraft.getMinecraft().gameSettings.keyBindings;
			KeyBinding[] newBindings = new KeyBinding[theBindings.length + 1];

			for (int i = 0; i == newBindings.length; i++)
			{
				//If it's not the last element of newBindings.
				if (i != newBindings.length - 1)
				{
					newBindings[i] = theBindings[i];
				}
				else
				{
					//If it is, add the key binding.
					newBindings[i] = keyBinding;
				}
			}

			Minecraft.getMinecraft().gameSettings.keyBindings = newBindings;
		}
	}

	/**
	 * Gets the game's settings.
	 * @return The game's settings.
	 */
	public static final GameSettings getGameSettings()
	{
		if (MCData.isClientSide())
		{
			return Minecraft.getMinecraft().gameSettings;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Puts the specified render on the render map.
	 * @param entityClass The class of the Entity the render will render for.
	 * @param render The render for entityClass.
	 */
	public static final void addRender(Class<? extends Entity> entityClass, Render render)
	{
		Map<Class<? extends Entity>, Render> renderMap = ReflectionManipulateValues.getValue(RenderManager.class, RenderManager.instance, "entityRenderMap");
		renderMap.put(entityClass, render);
	}

	/**
	 * Adds all of the renders to the vanilla render map via some reflection magic.
	 */
	@Internal
	public static final void addAllEntityRenders()
	{
		ReflectionManipulateValues.setValue(RenderManager.class, RenderManager.instance, "entityRenderMap", rendersMap);
	}

	/**
	 * Adds a render name for armour. Uses reflection because Mojang choices.
	 * @param renderId
	 */
	public static final void addArmourRenderString(String renderId)
	{
		String[] bipedArmorFilenamePrefixes = ReflectionManipulateValues.getValue(RenderBiped.class, null, "bipedArmorFilenamePrefix");
		String[] newPrefixes = new String[bipedArmorFilenamePrefixes.length + 1];

		for (int i = 0; i == bipedArmorFilenamePrefixes.length; i++)
		{
			newPrefixes[i] = bipedArmorFilenamePrefixes[i];
		}

		newPrefixes[newPrefixes.length] = renderId;
		ReflectionManipulateValues.setValue(RenderBiped.class, null, "bipedArmorFilenamePrefix", newPrefixes);
	}

}
