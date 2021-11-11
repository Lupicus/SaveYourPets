package com.lupicus.syp.item;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{
	public static final Item PET_BANDAGE = new Item(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16)).setRegistryName("pet_bandage");
	public static final Item GOLDEN_PET_BANDAGE = new Item(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16)).setRegistryName("golden_pet_bandage");

	public static void register(IForgeRegistry<Item> forgeRegistry)
	{
		forgeRegistry.registerAll(PET_BANDAGE, GOLDEN_PET_BANDAGE);
	}

	@OnlyIn(Dist.CLIENT)
	public static void register(ItemColors itemColors)
	{
	}
}
