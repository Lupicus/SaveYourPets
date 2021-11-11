package com.lupicus.syp.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModLoot
{
	public static void addLoot(ResourceLocation res, LootTable lootTable, LootTables lootTableManager)
	{
		if (res.equals(BuiltInLootTables.VILLAGE_SHEPHERD))
		{
			LootPool pool = LootPool.lootPool().name("pet_bandage")
					.setRolls(ConstantValue.exactly(1.0f))
					.add(LootItem.lootTableItem(ModItems.PET_BANDAGE).setWeight(50))
					.add(LootItem.lootTableItem(ModItems.GOLDEN_PET_BANDAGE).setWeight(30))
					.add(EmptyLootItem.emptyItem().setWeight(20))
					.build();
			lootTable.addPool(pool);
		}
	}
}
