package com.lupicus.syp.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{
	public static final Item PET_BANDAGE = new Item(new Properties().stacksTo(16));
	public static final Item GOLDEN_PET_BANDAGE = new Item(new Properties().stacksTo(16));

	public static void register(IForgeRegistry<Item> forgeRegistry)
	{
		forgeRegistry.register("pet_bandage", PET_BANDAGE);
		forgeRegistry.register("golden_pet_bandage", GOLDEN_PET_BANDAGE);
	}

	public static void setupTabs(CreativeModeTabEvent.BuildContents event)
	{
		if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES)
		{
			event.accept(PET_BANDAGE);
			event.accept(GOLDEN_PET_BANDAGE);
		}
	}
}
