package com.maxwell.create_must_upgrade.Cap;

import net.minecraftforge.common.capabilities.*;

public class UpgradeCapability {
    // Capabilityのインスタンスを登録・取得するためのトークン
    public static final Capability<IUpgradeState> UPGRADE_STATE =
            CapabilityManager.get(new CapabilityToken<>() {});
}
