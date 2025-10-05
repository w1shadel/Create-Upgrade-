package com.maxwell.create_must_upgrade.Item;

import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeCapability;
import com.maxwell.create_must_upgrade.Cap.Upgrade.UpgradeType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BaseUpgradeItem extends Item {
    private final Supplier<UpgradeType> upgradeTypeSupplier;

    public BaseUpgradeItem(Properties properties, Supplier<UpgradeType> upgradeTypeSupplier) {
        super(properties);
        this.upgradeTypeSupplier = upgradeTypeSupplier;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // --- ラムダ式の外側で、必要な変数を全て取得する ---
        final Level level = context.getLevel(); // finalキーワードを付けるか、実質的にfinalにする
        final BlockPos pos = context.getClickedPos();
        final Player player = context.getPlayer();

        // クライアントサイドでは音の再生指示はしない
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be != null) {
            be.getCapability(UpgradeCapability.UPGRADE_STATE).ifPresent(state -> {
                // --- ここからがラムダ式の内側 ---

                UpgradeType type = this.upgradeTypeSupplier.get();
                state.addUpgradeLevel(type, 1);
                be.setChanged();

                // ラムダ式の外側で定義された 'level' と 'pos' をここで使う
                level.playSound(
                        null,
                        pos, // context.getClickedPos() の代わりに変数 'pos' を使う
                        SoundEvents.ARMOR_EQUIP_IRON,
                        SoundSource.BLOCKS,
                        1.0f,
                        1.2f
                );

                if (player != null) {
                    context.getItemInHand().shrink(1);
                }
            });
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        UpgradeType type = this.upgradeTypeSupplier.get();
        if (type == null) return;

        // --- 説明文を追加 ---
        // 翻訳キーを取得し、Component.translatable()で翻訳されたComponentを取得
        Component description = Component.translatable(type.getDescriptionTranslationKey())
                .withStyle(ChatFormatting.GOLD);
        pTooltipComponents.add(description);
    }
}