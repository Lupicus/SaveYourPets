package com.lupicus.syp.item;

import com.lupicus.syp.Main;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

    public static final RegistryObject<Item> PET_BANDAGE = ITEMS.register("pet_bandage", () -> new Item(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16)));
    public static final RegistryObject<Item> GOLDEN_PET_BANDAGE = ITEMS.register("golden_pet_bandage", () -> new Item(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16)));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
