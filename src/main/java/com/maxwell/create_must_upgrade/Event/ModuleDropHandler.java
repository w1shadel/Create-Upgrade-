package com.maxwell.create_must_upgrade.Event;

import com.maxwell.create_must_upgrade.Cap.UpgradeCapability;
import com.maxwell.create_must_upgrade.Create_must_upgrade;
import com.maxwell.create_must_upgrade.Register.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = Create_must_upgrade.MODID)
public class ModuleDropHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * ブロックが破壊された時に呼び出されるイベントハンドラ。
     * @param event BreakEvent
     */
    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        // isClientSide() は LevelReader にあるので、ILevel からキャスト可能
        // サーバーサイドでのみ処理を実行し、プレイヤーがクリエイティブモードでないことを確認
        LOGGER.info("BlockEvent.BreakEvent fired!");
        if (event.getLevel().isClientSide() || event.getPlayer().isCreative()) {
            return;
        }
        LOGGER.info("BlockEvent.BreakEvent fired! This is only server event!");
        // 破壊されるブロックのBlockEntityを取得
        BlockEntity be = event.getLevel().getBlockEntity(event.getPos());
        if (be == null) {
            return;
        }

        // BlockEntityがアップグレードのCapabilityを持っているか確認
        be.getCapability(UpgradeCapability.UPGRADE_STATE).ifPresent(upgradeState -> {
            LOGGER.info("BlockEvent.BreakEvent fired! It has Upgrades!");
            // 適用されている全てのアップグレードを取得
            var allUpgrades = upgradeState.getAllUpgrades();

            if (allUpgrades.isEmpty()) {
                return; // アップグレードがなければ何もしない
            }
            LOGGER.info("BlockEvent.BreakEvent fired!" + "get:" +  allUpgrades);
            // ILevelAccessor を ServerLevel に安全にキャスト
            // アイテムのスポーンにはServerLevelが必要
            if (event.getLevel() instanceof ServerLevel level) {
                LOGGER.info("BlockEvent.BreakEvent fired!" + "this is a " + level);
                // 各アップグレードについてループ処理
                allUpgrades.forEach((type, installedLevel) -> {
                    LOGGER.info("BlockEvent.BreakEvent fired!" + "forの直前");
                    // ModItemsに実装したヘルパーメソッドを使って、UpgradeTypeに対応するItemを探す
                    ModItems.getItemFromType(type).ifPresent(item -> {

                        // インストールされているレベルの数だけアイテムをドロップ
                        for (int i = 0; i < installedLevel; i++) {
                            ItemStack dropStack = new ItemStack(item, 1);
                            LOGGER.info("BlockEvent.BreakEvent fired!" + "get:" +  dropStack);
                            Block.popResource(level, event.getPos(), dropStack);
                        }
                    });
                });
            }
        });
    }
}