package com.maxwell.create_must_upgrade.TileEntity;

import com.maxwell.create_must_upgrade.Cap.PressureEnergy.IPressureEnergyStorage;
import com.maxwell.create_must_upgrade.Item.StressAccumulatorItem;
import com.maxwell.create_must_upgrade.Register.ModTileEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import static com.maxwell.create_must_upgrade.Item.StressAccumulatorItem.ENERGY_CAPABILITY;

public class StressChargerBlockEntity extends KineticBlockEntity {

    protected final ItemStackHandler inventory;
    private LazyOptional<IItemHandler> itemCapability;

    public StressChargerBlockEntity(BlockPos pos, BlockState state) {
        // super()には登録クラスからBlockEntityTypeを渡す
        super(ModTileEntity.STRESS_CHARGER_ENTITY.get(), pos, state);

        inventory = new ItemStackHandler(1);
        itemCapability = LazyOptional.of(() -> inventory);
    }

    @Override
    public void tick() {
        super.tick();

        if (getSpeed() == 0) {
            return;
        }

        ItemStack stack = inventory.getStackInSlot(0);
        if (stack.isEmpty()) {
            return;
        }

        // アイテムからENERGY_CAPABILITYを取得
        LazyOptional<IPressureEnergyStorage> energyCapability = stack.getCapability(ENERGY_CAPABILITY);

        // Capabilityが存在する場合のみ処理
        energyCapability.ifPresent(energyStorage -> {
            // 既に満タンなら何もしない
            if (energyStorage.getEnergyStored() >= energyStorage.getMaxEnergyStored()) {
                return;
            }

            int energyToGenerate = (int) (Math.abs(getSpeed()) / 16f);
            if (energyToGenerate <= 0) {
                return;
            }

            // Capabilityのメソッドを呼び出してエネルギーを注入
            int accepted = energyStorage.receiveEnergy(energyToGenerate, false);

            if (accepted > 0) {
                // ItemStackのNBTを直接変更するのではなく、BlockEntityの状態を更新する
                // これにより、クライアントへの同期が正しく行われる
                setChanged();
                sendData();
            }
        });
    }
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
    }
}