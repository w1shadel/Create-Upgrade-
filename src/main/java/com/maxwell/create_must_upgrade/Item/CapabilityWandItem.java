package com.maxwell.create_must_upgrade.Item;

import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayList;
import java.util.List;

public class CapabilityWandItem extends Item {

    public CapabilityWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        // サーバーサイドかつプレイヤーが存在する場合のみ処理
        if (level.isClientSide() || player == null) {
            return InteractionResult.PASS;
        }

        BlockEntity be = level.getBlockEntity(context.getClickedPos());
        if (be == null) {
            player.sendSystemMessage(Component.literal("このブロックはBlockEntityを持っていません。").withStyle(ChatFormatting.RED));
            return InteractionResult.PASS;
        }

        // --- ここからCapabilityの調査を開始 ---
        List<Component> messages = new ArrayList<>();
        messages.add(Component.literal("--- Capability Inspector ---").withStyle(ChatFormatting.GOLD));
        messages.add(Component.literal("Block: " + be.getBlockState().getBlock().getName().getString()));

        // 1. アイテムハンドラ (インベントリ) の調査
        be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            messages.add(Component.literal("Inventory: ").withStyle(ChatFormatting.AQUA)
                    .append(Component.literal(handler.getSlots() + " slots")));
            // 最初のスロットの中身だけ表示
            if (handler.getSlots() > 0) {
                messages.add(Component.literal("  Slot 0: " + handler.getStackInSlot(0).toString()));
            }
        });

        // 2. 液体ハンドラ (タンク) の調査
        be.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(handler -> {
            messages.add(Component.literal("Fluid Tank: ").withStyle(ChatFormatting.BLUE));
            for (int i = 0; i < handler.getTanks(); i++) {
                messages.add(Component.literal(String.format("  Tank %d: %s (%d / %d mB)",
                        i,
                        handler.getFluidInTank(i).getDisplayName().getString(),
                        handler.getFluidInTank(i).getAmount(),
                        handler.getTankCapacity(i)
                )));
            }
        });

        // 3. エネルギー (FE/RF) の調査
        be.getCapability(ForgeCapabilities.ENERGY).ifPresent(energy -> {
            messages.add(Component.literal("Energy Storage: ").withStyle(ChatFormatting.RED)
                    .append(Component.literal(String.format("%,d / %,d FE", energy.getEnergyStored(), energy.getMaxEnergyStored()))));
        });

        // 4. 私たちのカスタムアップグレードCapabilityの調査
        be.getCapability(UpgradeCapability.UPGRADE_STATE).ifPresent(upgradeState -> {
            messages.add(Component.literal("Upgrades: ").withStyle(ChatFormatting.GREEN));
            if (upgradeState.getAllUpgrades().isEmpty()) {
                messages.add(Component.literal("  (No upgrades installed)"));
            } else {
                upgradeState.getAllUpgrades().forEach((type, upgradeLevel) -> {
                    // 翻訳キーを取得
                    String nameKey = type.getNameTranslationKey();

                    // Component.translatable() にキーを渡す
                    Component translatedName = Component.translatable(nameKey);

                    messages.add(Component.literal(String.format("  - %s: Level %d", translatedName.getString(), upgradeLevel)));
                });
            }
        });

        // --- 調査結果の表示 ---
        if (messages.size() <= 2) { // ヘッダーとブロック名しか無ければ
            messages.add(Component.literal("このBlockEntityは既知のCapabilityを持っていません。").withStyle(ChatFormatting.GRAY));
        }
        messages.add(Component.literal("---------------------------").withStyle(ChatFormatting.GOLD));

        // 全てのメッセージをプレイヤーに送信
        messages.forEach(player::sendSystemMessage);

        return InteractionResult.SUCCESS;
    }
}