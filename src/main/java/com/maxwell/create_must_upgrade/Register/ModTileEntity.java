package com.maxwell.create_must_upgrade.Register;

import com.maxwell.create_must_upgrade.Create_must_upgrade;
import com.maxwell.create_must_upgrade.TileEntity.StressChargerBlockEntity;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTileEntity {
    // 1. BlockEntityType用のDeferredRegister
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Create_must_upgrade.MODID);

    // 2. BlockEntityを登録
    public static final RegistryObject<BlockEntityType<StressChargerBlockEntity>> STRESS_CHARGER_ENTITY =
            BLOCK_ENTITIES.register("stress_charger_entity", () ->
                    BlockEntityType.Builder.of(StressChargerBlockEntity::new, ModBlocks.STRESS_CHARGER.get())
                            .build(null));

    // 3. メインクラスから呼ばれるregisterメソッド
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
