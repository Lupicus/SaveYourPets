package com.lupicus.syp.item;

import java.util.function.Function;

import com.lupicus.syp.Main;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems
{
	public static final Item PET_BANDAGE = register("pet_bandage", Item::new, new Properties().stacksTo(16));
	public static final Item GOLDEN_PET_BANDAGE = register("golden_pet_bandage", Item::new, new Properties().stacksTo(16));

	public static void register(IForgeRegistry<Item> forgeRegistry)
	{
	}

	private static Item register(String name, Function<Properties, Item> func, Properties prop)
	{
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Main.MODID, name));
		return Items.registerItem(key, func, prop);
	}

	public static void setupTabs(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
		{
			event.accept(PET_BANDAGE);
			event.accept(GOLDEN_PET_BANDAGE);
		}
	}
}
