package com.lupicus.syp.advancements;

import com.lupicus.syp.Main;
import com.lupicus.syp.advancements.criterion.SavePetTrigger;

import net.minecraft.advancements.CriteriaTriggers;

public class ModTriggers
{
	public static final SavePetTrigger SAVE_PET = CriteriaTriggers.register(makeKey("save_pet"), new SavePetTrigger());

	public static void register()
	{
	}

	private static String makeKey(String name)
	{
		return Main.MODID + ":" + name;
	}
}
