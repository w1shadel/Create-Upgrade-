package com.maxwell.create_must_upgrade.Item;

import com.maxwell.create_must_upgrade.Cap.PressureEnergy.IPressureEnergyStorage;
import com.maxwell.create_must_upgrade.Cap.PressureEnergy.PressureEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class StressAccumulatorItem extends Item {

    // Capabilityの参照を定義
    public static final Capability<IPressureEnergyStorage> ENERGY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private final int capacity;

    public StressAccumulatorItem(Properties properties, int capacity) {
        super(properties);
        this.capacity = capacity;
    }

    // ツールチップ表示
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        stack.getCapability(ENERGY_CAPABILITY).ifPresent(energyStorage -> {
            tooltip.add(Component.translatable("tooltip.yourmodid.stress_accumulator.energy",
                    energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored()));
        });

        tooltip.add(Component.translatable("tooltip.yourmodid.stress_accumulator.flavor").withStyle(s -> s.withColor(0xAAAAAA)));
    }

    // 耐久値バーの表示
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(ENERGY_CAPABILITY).map(energyStorage ->
                Math.round(13.0F * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored())
        ).orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x8888FF;
    }

    public int getCapacity() {
        return this.capacity;
    }
}