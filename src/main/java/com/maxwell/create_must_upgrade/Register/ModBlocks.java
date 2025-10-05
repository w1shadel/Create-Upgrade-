package com.maxwell.create_must_upgrade.Register;

import com.maxwell.create_must_upgrade.Blocks.StressChargerBlock;
import com.maxwell.create_must_upgrade.Create_must_upgrade;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class ModBlocks {
    // 1. ブロック用のDeferredRegister
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Create_must_upgrade.MODID);

    // 2. ブロックを登録
    public static final RegistryObject<Block> STRESS_CHARGER = registerBlock("stress_charger",
            () -> new StressChargerBlock(BlockBehaviour.Properties.of()
                    .strength(5f) // 硬さ
                    .requiresCorrectToolForDrops() // 適切なツールでの破壊が必要
                    .noOcclusion() // ブロックが完全に不透明ではないことを示す (描画用)
            ));

    // 3. ブロックとアイテム版ブロックを同時に登録するためのヘルパーメソッド
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    // 4. アイテム版ブロックを登録
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    // 5. メインクラスから呼ばれるregisterメソッド
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
