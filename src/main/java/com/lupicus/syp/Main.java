package com.lupicus.syp;

import org.jetbrains.annotations.NotNull;

import com.lupicus.syp.advancements.ModTriggers;
import com.lupicus.syp.config.MyConfig;
import com.lupicus.syp.item.ModItems;
import com.lupicus.syp.item.ModLoot;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "syp";

    public Main(FMLJavaModLoadingContext context)
    {
    	context.getModEventBus().register(this);
    	context.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
    }

	@SubscribeEvent
	public void setupCommon(final FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			ModTriggers.register();
		});
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void setupClient(final FMLClientSetupEvent event)
    {
	}

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents
    {
	    @SubscribeEvent
	    public static void onRegister(final RegisterEvent event)
	    {
	    	@NotNull
			ResourceKey<? extends Registry<?>> key = event.getRegistryKey();
	    	if (key.equals(ForgeRegistries.Keys.ITEMS))
	    		ModItems.register(event.getForgeRegistry());
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
