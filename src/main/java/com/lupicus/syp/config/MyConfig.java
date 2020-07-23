package com.lupicus.syp.config;

import org.apache.commons.lang3.tuple.Pair;

import com.lupicus.syp.Main;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig
{
	public static final Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;
	static
	{
		final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	public static boolean autoHeal;
	public static boolean showLoc;
	public static int deathTimer;
	public static int healTime;

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent)
	{
		if (configEvent.getConfig().getSpec() == MyConfig.SERVER_SPEC)
		{
			bakeConfig();
		}
	}

	public static void bakeConfig()
	{
		autoHeal = SERVER.autoHeal.get();
		showLoc = SERVER.showLoc.get();
		deathTimer = SERVER.deathTimer.get();
		healTime = SERVER.healTime.get() * 20;
	}

	public static class Server
	{
		public final BooleanValue autoHeal;
		public final BooleanValue showLoc;
		public final IntValue deathTimer;
		public final IntValue healTime;

		public Server(ForgeConfigSpec.Builder builder)
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
		}
	}
}
