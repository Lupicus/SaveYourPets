package com.lupicus.syp.item;

import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.EmptyLootEntry;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.LootTables;
import net.minecraft.util.ResourceLocation;

public class ModLoot
{
	public static void addLoot(ResourceLocation res, LootTable lootTable, LootTableManager lootTableManager)
	{
		if (res.equals(LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD))
		{
			LootPool pool = LootPool.builder().name("pet_bandage")
					.rolls(new ConstantRange(1))
					.addEntry(ItemLootEntry.builder(ModItems.PET_BANDAGE).weight(50))
					.addEntry(ItemLootEntry.builder(ModItems.GOLDEN_PET_BANDAGE).weight(30))
					.addEntry(EmptyLootEntry.func_216167_a().weight(20))
					.build();
			lootTable.addPool(pool);
		}
	}
}
