package com.lupicus.syp.item;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.EmptyLootEntry;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.LootTables;

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
