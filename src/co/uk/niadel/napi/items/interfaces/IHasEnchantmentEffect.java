package co.uk.niadel.napi.items.interfaces;

import net.minecraft.item.ItemStack;

public interface IHasEnchantmentEffect 
{
	/**
	 * Return true on this method.
	 * @param itemStack
	 * @return
	 */
	public boolean hasEffect(ItemStack itemStack);
}
