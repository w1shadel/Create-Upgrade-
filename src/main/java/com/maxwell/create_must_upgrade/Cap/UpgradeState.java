package com.maxwell.create_must_upgrade.Cap;

import com.maxwell.create_must_upgrade.Register.ModRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("removal")
public class UpgradeState implements IUpgradeState, INBTSerializable<CompoundTag> {

    private final Map<UpgradeType, Integer> upgradeLevels = new HashMap<>();

    @Override
    public int getUpgradeLevel(UpgradeType type) {
        return upgradeLevels.getOrDefault(type, 0);
    }

    @Override
    public void setUpgradeLevel(UpgradeType type, int level) {
        if (level <= 0) {
            upgradeLevels.remove(type);
        } else {
            upgradeLevels.put(type, level);
        }
    }

    @Override
    public void addUpgradeLevel(UpgradeType type, int amount) {
        setUpgradeLevel(type, getUpgradeLevel(type) + amount);
    }

    @Override
    public Map<UpgradeType, Integer> getAllUpgrades() {
        return upgradeLevels;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        CompoundTag upgradesTag = new CompoundTag();
        IForgeRegistry<UpgradeType> registry = ModRegistries.REGISTRY.get();

        upgradeLevels.forEach((type, level) -> {
            ResourceLocation key = registry.getKey(type);
            if (key != null) {
                upgradesTag.putInt(key.toString(), level);
            }
        });

        rootTag.put("Upgrades", upgradesTag);
        return rootTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        upgradeLevels.clear();
        CompoundTag upgradesTag = nbt.getCompound("Upgrades");
        IForgeRegistry<UpgradeType> registry = ModRegistries.REGISTRY.get();

        for (String keyStr : upgradesTag.getAllKeys()) {
            ResourceLocation key = new ResourceLocation(keyStr);
            if (registry.containsKey(key)) {
                UpgradeType type = registry.getValue(key);
                if (type != null) {
                    upgradeLevels.put(type, upgradesTag.getInt(keyStr));
                }
            }
        }
    }
}