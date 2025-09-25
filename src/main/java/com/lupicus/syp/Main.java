package com.lupicus.syp;

import org.jetbrains.annotations.NotNull;

import com.lupicus.syp.advancements.ModTriggers;
import com.lupicus.syp.config.MyConfig;
import com.lupicus.syp.item.ModItems;
import com.lupicus.syp.item.ModLoot;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Main.MODID)
public class Main
{
	public static final String MODID = "syp";

	public Main(FMLJavaModLoadingContext context)
	{
		context.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModEvents
	{
		@SubscribeEvent
		public static void onRegister(final RegisterEvent event)
		{
			@NotNull
			ResourceKey<? extends Registry<?>> key = event.getRegistryKey();
			if (key.equals(ForgeRegistries.Keys.ITEMS))
				ModItems.register(event.getForgeRegistry());
			else if (key.equals(Registries.TRIGGER_TYPE))
				ModTriggers.register();
		}

		@SubscribeEvent
		public static void onCreativeTab(BuildCreativeModeTabContentsEvent event)
		{
			ModItems.setupTabs(event);
		}
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeEvents
	{
		@SubscribeEvent
		public static void onLoot(final LootTableLoadEvent event)
		{
			ModLoot.addLoot(event.getName(), event.getTable());
		}
	}
}
