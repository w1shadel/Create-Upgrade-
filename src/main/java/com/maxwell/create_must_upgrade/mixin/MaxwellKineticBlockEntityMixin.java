package com.maxwell.create_must_upgrade.mixin;

import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeCapability;
import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeEffect;
import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeType;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(KineticBlockEntity.class)
public abstract class MaxwellKineticBlockEntityMixin {

    @Inject(method = "calculateStressApplied", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyStressApplied(CallbackInfoReturnable<Float> cir) {
        BlockEntity self = (BlockEntity) (Object) this;

        self.getCapability(UpgradeCapability.UPGRADE_STATE).ifPresent(upgradeState -> {
            float originalStress = cir.getReturnValue();
            float modifiedStress = originalStress;

            if (upgradeState.getAllUpgrades().isEmpty()) {
                return;
            }
            for (Map.Entry<UpgradeType, Integer> entry : upgradeState.getAllUpgrades().entrySet()) {
                UpgradeType type = entry.getKey();
                int level = entry.getValue();
                for (UpgradeEffect effect : type.getEffects()) {
                    if (effect.getTarget() == UpgradeType.TargetValue.STRESS) {
                        modifiedStress = effect.apply(modifiedStress, level);
                    }
                }
            }
            cir.setReturnValue(modifiedStress);
        });
    }

    @Inject(method = "getSpeed", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifySpeedOnReturn(CallbackInfoReturnable<Float> cir) {
        BlockEntity self = (BlockEntity) (Object) this;

        self.getCapability(UpgradeCapability.UPGRADE_STATE).ifPresent(upgradeState -> {
            float originalSpeed = cir.getReturnValue();
            float sign = Math.signum(originalSpeed);
            float absoluteSpeed = Math.abs(originalSpeed);
            float modifiedAbsoluteSpeed = absoluteSpeed;

            if (upgradeState.getAllUpgrades().isEmpty()) {
                return;
            }
            if (originalSpeed == 0) return;
            for (Map.Entry<UpgradeType, Integer> entry : upgradeState.getAllUpgrades().entrySet()) {
                UpgradeType type = entry.getKey();
                int level = entry.getValue();
                for (UpgradeEffect effect : type.getEffects()) {
                    if (effect.getTarget() == UpgradeType.TargetValue.SPEED) {
                        modifiedAbsoluteSpeed = effect.apply(modifiedAbsoluteSpeed, level);
                    }
                }
            }
            cir.setReturnValue(modifiedAbsoluteSpeed * sign);
        });
    }

}