package com.maxwell.create_must_upgrade.Register;

import com.maxwell.create_must_upgrade.Cap.UpgradeType;
import com.maxwell.create_must_upgrade.Create_must_upgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;
@SuppressWarnings("removal")
public class ModRegistries {
    // 1. レジストリ自体を識別するためのキーを作成
    public static final ResourceLocation UPGRADE_TYPE_REGISTRY_KEY = new ResourceLocation(Create_must_upgrade.MODID, "upgrade_types");

    // 2. このキーを使って DeferredRegister を作成
    public static final DeferredRegister<UpgradeType> UPGRADE_TYPES =
            DeferredRegister.create(UPGRADE_TYPE_REGISTRY_KEY, Create_must_upgrade.MODID);

    // 3. DeferredRegisterに新しいレジストリの作成を依頼する (これがsetTypeの代わり)
    public static final Supplier<IForgeRegistry<UpgradeType>> REGISTRY = UPGRADE_TYPES.makeRegistry(RegistryBuilder::new);

    // 4. イベントバスへの登録メソッド
    public static void register(IEventBus eventBus) {
        UPGRADE_TYPES.register(eventBus);
    }
}