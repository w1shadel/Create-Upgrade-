package com.maxwell.create_must_upgrade.Cap.Upgrade;

import java.util.function.BiFunction;

public class UpgradeEffect {

    // --- フィールド ---
    private final UpgradeType.TargetValue target;
    private final BiFunction<Float, Integer, Float> modificationLogic;

    // --- コンストラクタ ---
    /**
     * 新しいUpgradeEffectを作成する。
     * @param target この効果が影響する値の種類 (SPEED or STRESS)
     * @param modificationLogic 値をどう変更するかの計算ロジック
     */
    public UpgradeEffect(UpgradeType.TargetValue target, BiFunction<Float, Integer, Float> modificationLogic) {
        this.target = target;
        this.modificationLogic = modificationLogic;
    }

    // --- メソッド ---
    public UpgradeType.TargetValue getTarget() {
        return target;
    }

    public float apply(float originalValue, int level) {
        return modificationLogic.apply(originalValue, level);
    }
}