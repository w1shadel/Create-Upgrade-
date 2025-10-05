package com.maxwell.create_must_upgrade.Cap.Upgrade;

import java.util.Map;

public interface IUpgradeState {
    int getUpgradeLevel(UpgradeType type);
    void setUpgradeLevel(UpgradeType type, int level);
    void addUpgradeLevel(UpgradeType type, int amount);
    Map<UpgradeType, Integer> getAllUpgrades();
}