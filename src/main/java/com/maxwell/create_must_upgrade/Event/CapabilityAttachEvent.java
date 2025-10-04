package com.maxwell.create_must_upgrade.Event;

import com.maxwell.create_must_upgrade.Cap.IUpgradeState;
import com.maxwell.create_must_upgrade.Cap.UpgradeCapability;
import com.maxwell.create_must_upgrade.Cap.UpgradeState;
import com.maxwell.create_must_upgrade.Create_must_upgrade;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Create_must_upgrade.MODID)
public class CapabilityAttachEvent {
    // 任意の識別子
    private static final ResourceLocation UPGRADE_CAPABILITY_KEY = new ResourceLocation(Create_must_upgrade.MODID, "upgrade_state");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof KineticBlockEntity) {
            event.addCapability(UPGRADE_CAPABILITY_KEY, new ICapabilitySerializable<CompoundTag>() {
                private final UpgradeState backend = new UpgradeState();
                private final LazyOptional<IUpgradeState> instance = LazyOptional.of(() -> backend);

                @NotNull
                @Override
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    return UpgradeCapability.UPGRADE_STATE.orEmpty(cap, this.instance);
                }

                @Override
                public CompoundTag serializeNBT() {
                    return backend.serializeNBT();
                }

                @Override
                public void deserializeNBT(CompoundTag nbt) {
                    backend.deserializeNBT(nbt);
                }
            });
        }
    }
}
