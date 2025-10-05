package com.maxwell.create_must_upgrade.Cap.PressureEnergy;

import net.minecraft.nbt.CompoundTag;

public class PressureEnergyStorage implements IPressureEnergyStorage {

    protected int energy;
    protected int capacity;

    public PressureEnergyStorage(int capacity) {
        this(capacity, 0);
    }

    public PressureEnergyStorage(int capacity, int energy) {
        this.capacity = capacity;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }

        int energyReceived = Math.min(capacity - energy, Math.max(0, maxReceive));
        if (!simulate) {
            energy += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }

        int energyExtracted = Math.min(energy, Math.max(0, maxExtract));
        if (!simulate) {
            energy -= energyExtracted;
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public boolean canExtract() {
        // ハイドロリックイージス装備で消費する際にtrueにする
        return false;
    }

    // NBTへの保存と読み込み
    public void writeToNBT(CompoundTag nbt) {
        nbt.putInt("PressureEnergy", this.energy);
    }

    public void readFromNBT(CompoundTag nbt) {
        this.energy = nbt.getInt("PressureEnergy");
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        }
    }
}