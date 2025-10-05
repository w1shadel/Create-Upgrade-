package com.maxwell.create_must_upgrade.Register;

import com.maxwell.create_must_upgrade.Create_must_upgrade;
import com.maxwell.create_must_upgrade.Item.BaseUpgradeItem;
import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeType;
import com.maxwell.create_must_upgrade.Item.CapabilityWandItem;
import com.maxwell.create_must_upgrade.Item.NetworkInspectorItem;
import com.maxwell.create_must_upgrade.Item.StressAccumulatorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModItems {
    private static final Map<Supplier<UpgradeType>, RegistryObject<Item>> UPGRADE_ITEM_MAP = new HashMap<>();
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static final RegistryObject<CreativeModeTab> CMU_TAB;
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> SPEED_UPGRADE_MODULE;
    public static final RegistryObject<Item> EFFICIENCY_MODULE;
    public static final RegistryObject<Item> CAP_WAND;
    public static final RegistryObject<Item> SCAN_WAND;
    public static final RegistryObject<Item> BROKEN_SCREEN;
    public static final RegistryObject<Item> STRESS_ACCUMRATER;
    static
    {
        TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Create_must_upgrade.MODID);
        ITEMS = DeferredRegister.create(Registries.ITEM, Create_must_upgrade.MODID);
        SPEED_UPGRADE_MODULE = ITEMS.register("speed_upgrade_module", () -> new BaseUpgradeItem(new Item.Properties(), ModUpgradeTypes.SPEED));
        EFFICIENCY_MODULE = ITEMS.register("efficiency_module", () -> new BaseUpgradeItem(new Item.Properties(), ModUpgradeTypes.EFFICIENCY));
        CAP_WAND = ITEMS.register("cap_wand", () -> new CapabilityWandItem(new Item.Properties()));
        SCAN_WAND = ITEMS.register("scan_wand", () -> new NetworkInspectorItem(new Item.Properties()));
        BROKEN_SCREEN = ITEMS.register("broken_screen", () -> new Item(new Item.Properties()));
        STRESS_ACCUMRATER = ITEMS.register("stress_accumrater", () -> new StressAccumulatorItem(new Item.Properties(),90));
        CMU_TAB = TABS.register("create_must_upgrade_items", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.create_must_upgrade.items")).icon(() ->
                new ItemStack(SPEED_UPGRADE_MODULE.get())).displayItems((enabledFeatures, entries) ->
        {
            entries.accept(SPEED_UPGRADE_MODULE.get());
            entries.accept(EFFICIENCY_MODULE.get());
            entries.accept(BROKEN_SCREEN.get());
            entries.accept(CAP_WAND.get());
        }).build());
    }
    public static void initializeUpgradeItemMap() {
        UPGRADE_ITEM_MAP.put(ModUpgradeTypes.SPEED, SPEED_UPGRADE_MODULE);
        UPGRADE_ITEM_MAP.put(ModUpgradeTypes.EFFICIENCY, EFFICIENCY_MODULE);
        // 新しいアップグレードアイテムを追加したら、この行も追加する
    }
    public static Optional<Item> getItemFromType(UpgradeType type) {
        // レジストリへの参照を取得
        IForgeRegistry<UpgradeType> registry = ModRegistries.REGISTRY.get();
        // 探したいUpgradeTypeのレジストリキー(ID)を取得
        var targetKey = registry.getKey(type);

        // キーが取得できなかった場合は、見つからないのでemptyを返す
        if (targetKey == null) {
            return Optional.empty();
        }

        return UPGRADE_ITEM_MAP.entrySet().stream()
                .filter(entry -> {
                    // マップに登録されているUpgradeTypeのキー(ID)を取得
                    var entryKey = registry.getKey(entry.getKey().get());
                    // 2つのキー(ID)が等しいか比較する
                    return targetKey.equals(entryKey);
                })
                .map(entry -> entry.getValue().get())
                .findFirst();
    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        TABS.register(eventBus);
    }
}
