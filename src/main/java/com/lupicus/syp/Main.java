package com.lupicus.syp;

import com.lupicus.syp.advancements.ModTriggers;
import com.lupicus.syp.config.MyConfig;
import com.lupicus.syp.item.ModItems;
import com.lupicus.syp.item.ModLoot;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "syp";

    public Main()
    {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
		ModItems.register(bus);
    }

	@SubscribeEvent
	public void setupCommon(final FMLCommonSetupEvent event)
	{
		ModTriggers.register();
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void setupClient(final FMLClientSetupEvent event)
    {
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeEvents
	{
		@SubscribeEvent
		public static void onLoot(final LootTableLoadEvent event)
		{
			ModLoot.addLoot(event.getName(), event.getTable(), event.getLootTableManager());
		}
	}
}
