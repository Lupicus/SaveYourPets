package com.lupicus.syp.advancements;

import com.lupicus.syp.advancements.criterion.SavePetTrigger;

import net.minecraft.advancements.CriteriaTriggers;

public class ModTriggers
{
	public static final SavePetTrigger SAVE_PET = CriteriaTriggers.register(new SavePetTrigger());

	public static void register()
	{
	}
}
