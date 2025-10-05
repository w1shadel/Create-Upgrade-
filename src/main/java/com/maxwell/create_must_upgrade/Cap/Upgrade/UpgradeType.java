package com.maxwell.create_must_upgrade.Cap.Upgrade;

import com.maxwell.create_must_upgrade.Register.ModRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class UpgradeType {

    private final List<UpgradeEffect> effects;
    public UpgradeType(UpgradeEffect... effects) { // ← ここが ... になっているのが重要
        this.effects = Arrays.asList(effects);
    }

    public List<UpgradeEffect> getEffects() {
        return effects;
    }
    public ResourceLocation getRegistryName() {
        return ModRegistries.REGISTRY.get().getKey(this);
    }
    public String getNameTranslationKey() {
        ResourceLocation name = getRegistryName();
        if (name == null) return "upgrade.unregistered";
        // "upgrade." + "create_must_upgrade" + "." + "speed" を組み立てる
        return "upgrade." + name.getNamespace() + "." + name.getPath();
    }
    public String getDescriptionTranslationKey() {
        ResourceLocation name = getRegistryName();
        if (name == null) return "upgrade.description.unregistered";
        return "upgrade." + name.getNamespace() + ".description." + name.getPath();
    }
    public enum TargetValue {
        SPEED,
        STRESS
    }
}