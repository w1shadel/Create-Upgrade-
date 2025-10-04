package com.maxwell.create_must_upgrade.Item;

import com.maxwell.create_must_upgrade.Cap.UpgradeCapability;
import com.maxwell.create_must_upgrade.util.ReflectionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class NetworkInspectorItem extends Item {

    public NetworkInspectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (level.isClientSide() || player == null) return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(context.getClickedPos());
        if (be == null) {
            player.sendSystemMessage(Component.literal("BlockEntityが見つかりません。").withStyle(ChatFormatting.RED));
            return InteractionResult.PASS;
        }

        List<Component> messages = new ArrayList<>();
        messages.add(Component.literal("--- Network Inspector ---").withStyle(ChatFormatting.GOLD));
        messages.add(Component.literal("Target: " + be.getBlockState().getBlock().getName().getString()));

        // ★★★ ここからがリフレクションによる調査 ★★★
        if (ReflectionHelper.isKinetic(be)) {
            // リフレクション経由で各値を取得する
            messages.add(Component.literal(String.format("Current Speed: %.2f RPM", ReflectionHelper.getSpeed(be).orElse(0f))));
            messages.add(Component.literal(String.format("Theoretical Speed: %.2f RPM", ReflectionHelper.getTheoreticalSpeed(be).orElse(0f))));
            messages.add(Component.literal(String.format("Stress Applied: %.2f SU", ReflectionHelper.calculateStressApplied(be).orElse(0f))));
            messages.add(Component.literal(String.format("Is Source: %s", ReflectionHelper.isSource(be).orElse(false))));
            ReflectionHelper.getSourcePos(be).ifPresent(pos -> messages.add(Component.literal("Source Pos: " + pos.toString())));
            ReflectionHelper.getNetworkId(be).ifPresent(id -> messages.add(Component.literal("Network ID: " + id)));
        } else {
            messages.add(Component.literal("これはKineticBlockEntityではありません。").withStyle(ChatFormatting.GRAY));
        }

        messages.add(Component.literal("---------------------------").withStyle(ChatFormatting.GOLD));
        messages.forEach(player::sendSystemMessage);

        return InteractionResult.SUCCESS;
    }
}