package com.lupicus.syp.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{
	public static final Item PET_BANDAGE = new Item(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16));
	public static final Item GOLDEN_PET_BANDAGE = new Item(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16));

	public static void register(IForgeRegistry<Item> forgeRegistry)
	{
		forgeRegistry.register("pet_bandage", PET_BANDAGE);
		forgeRegistry.register("golden_pet_bandage", GOLDEN_PET_BANDAGE);
	}
}
