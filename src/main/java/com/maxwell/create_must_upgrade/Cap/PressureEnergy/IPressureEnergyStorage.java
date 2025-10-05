package com.maxwell.create_must_upgrade.Cap.PressureEnergy;

public interface IPressureEnergyStorage {

    /**
     * エネルギーを受け入れます。
     * @param maxReceive 受け入れ可能な最大量
     * @param simulate trueの場合、実際にはエネルギーを加えずに、受け入れ可能な量を返す
     * @return 実際に受け入れたエネルギー量
     */
    int receiveEnergy(int maxReceive, boolean simulate);

    /**
     * エネルギーを取り出します。（今回は使いませんが、将来の拡張用）
     * @param maxExtract 取り出し可能な最大量
     * @param simulate trueの場合、実際にはエネルギーを減らさずに、取り出し可能な量を返す
     * @return 実際に取り出したエネルギー量
     */
    int extractEnergy(int maxExtract, boolean simulate);

    /**
     * 現在蓄えられているエネルギー量を返します。
     */
    int getEnergyStored();

    /**
     * 最大エネルギー容量を返します。
     */
    int getMaxEnergyStored();

    /**
     * エネルギーを受け入れ可能かどうかを返します。
     */
    boolean canReceive();

    /**
     * エネルギーを取り出し可能かどうかを返します。
     */
    boolean canExtract();
}
