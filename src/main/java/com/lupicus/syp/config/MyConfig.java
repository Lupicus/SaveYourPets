package com.lupicus.syp.config;

import org.apache.commons.lang3.tuple.Pair;

import com.lupicus.syp.Main;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig
{
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static
	{
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static boolean autoHeal;
	public static boolean showLoc;
	public static boolean useWorldTicks;
	public static int deathTimer;
	public static int healTime;

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfigEvent configEvent)
	{
		if (configEvent instanceof ModConfigEvent.Unloading)
			return;
		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC)
		{
			if (MyConfig.COMMON_SPEC.isLoaded())
				bakeConfig();
		}
	}

	public static void bakeConfig()
	{
		autoHeal = COMMON.autoHeal.get();
		showLoc = COMMON.showLoc.get();
		useWorldTicks = COMMON.useWorldTicks.get();
		deathTimer = COMMON.deathTimer.get();
		healTime = COMMON.healTime.get() * 20;
	}

	public static class Common
	{
		public final BooleanValue autoHeal;
		public final BooleanValue showLoc;
		public final BooleanValue useWorldTicks;
		public final IntValue deathTimer;
		public final IntValue healTime;

		public Common(ForgeConfigSpec.Builder builder)
		{
			String baseTrans = Main.MODID + ".config.";
			String sectionTrans;

			sectionTrans = baseTrans + "general.";
			deathTimer = builder
					.comment("Death Timer")
					.translation(sectionTrans + "death_timer")
					.defineInRange("DeathTimer", 24000, 0, 220000);

			healTime = builder
					.comment("Heal Time for Golden Pet Bandage")
					.translation(sectionTrans + "heal_time")
					.defineInRange("HealTime", 20, 0, 120);

			autoHeal = builder
					.comment("Auto Heal after timer")
					.translation(sectionTrans + "auto_heal")
					.define("AutoHeal", false);

			showLoc = builder
					.comment("Show Location")
					.translation(sectionTrans + "show_loc")
					.define("ShowLoc", true);

			useWorldTicks = builder
					.comment("Use world ticks otherwise chunk ticks")
					.translation(sectionTrans + "world_ticks")
					.define("UseWorldTicks", false);
		}
	}
}
