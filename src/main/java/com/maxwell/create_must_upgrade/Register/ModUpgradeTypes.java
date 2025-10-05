package com.maxwell.create_must_upgrade.Register;

import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeEffect;
import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModUpgradeTypes {

    // ModRegistriesで作成したDeferredRegisterを参照
    public static final DeferredRegister<UpgradeType> UPGRADE_TYPES = ModRegistries.UPGRADE_TYPES;

    // --- ここにあなたのModが提供するアップグレードを定義・登録していく ---

    public static final RegistryObject<UpgradeType> SPEED = UPGRADE_TYPES.register("speed",
            () -> new UpgradeType(
                    // --- 効果1: 速度を上げる (加算に変更) ---
                    new UpgradeEffect(
                            UpgradeType.TargetValue.SPEED,
                            (original, level) -> original * (level * 4.0f)
                    ),
                    new UpgradeEffect(
                            UpgradeType.TargetValue.STRESS,
                            (original, level) -> original * (1.0f + (level * 0.4f))
                    )
            )
    );
    public static final RegistryObject<UpgradeType> EFFICIENCY = UPGRADE_TYPES.register("efficiency",
            () -> new UpgradeType(
                    new UpgradeEffect(
                            UpgradeType.TargetValue.STRESS,
                            // (元の値, アップグレードレベル) -> 新しい値
                            // 例: レベル毎に応力負荷が10%ずつ乗算で減少する。ただし最低でも元の10%は残る。
                            (original, level) -> original * Math.max(0.1f, 1.0f - (level * 0.1f))
                    )
            )
    );

}