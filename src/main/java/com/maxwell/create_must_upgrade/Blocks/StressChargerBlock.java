package com.maxwell.create_must_upgrade.Blocks;

import com.maxwell.create_must_upgrade.Register.ModTileEntity;
import com.maxwell.create_must_upgrade.TileEntity.StressChargerBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StressChargerBlock extends HorizontalKineticBlock implements IBE<StressChargerBlockEntity> {

    public StressChargerBlock(Properties properties) {
        super(properties);
    }

    // どのBlockEntityを使うかを指定
    @Override
    public Class<StressChargerBlockEntity> getBlockEntityClass() {
        return StressChargerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StressChargerBlockEntity> getBlockEntityType() {
        // ここで事前に登録したBlockEntityTypeインスタンスを返す
        return ModTileEntity.STRESS_CHARGER_ENTITY.get(); // 仮の登録クラス
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        // 歯車がどの軸で回転するか
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }
}