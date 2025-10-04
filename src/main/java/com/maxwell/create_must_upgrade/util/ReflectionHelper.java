package com.maxwell.create_must_upgrade.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public final class ReflectionHelper {

    private static Class<?> kineticBlockEntityClass;
    private static Method getSpeedMethod, getTheoreticalSpeedMethod, calculateStressAppliedMethod, isSourceMethod, hasNetworkMethod;
    private static Field sourceField, networkField;

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        try {
            kineticBlockEntityClass = Class.forName("com.simibubi.create.content.kinetics.base.KineticBlockEntity");

            // ★★★ 存在するメソッドだけを取得 ★★★
            getSpeedMethod = kineticBlockEntityClass.getMethod("getSpeed");
            getTheoreticalSpeedMethod = kineticBlockEntityClass.getMethod("getTheoreticalSpeed");
            calculateStressAppliedMethod = kineticBlockEntityClass.getMethod("calculateStressApplied");
            isSourceMethod = kineticBlockEntityClass.getMethod("isSource");
            hasNetworkMethod = kineticBlockEntityClass.getMethod("hasNetwork");

            // ★★★ 存在するフィールドだけを取得 ★★★
            sourceField = kineticBlockEntityClass.getDeclaredField("source");
            sourceField.setAccessible(true);
            networkField = kineticBlockEntityClass.getDeclaredField("network");
            networkField.setAccessible(true);

            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<Object> invokeMethod(Method method, BlockEntity be) {
        if (!initialized || !isKinetic(be)) return Optional.empty();
        try {
            return Optional.ofNullable(method.invoke(be));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean isKinetic(BlockEntity be) {
        if (be == null) return false;
        // 初期化がまだなら試みる(安全のため)
        if (!initialized) init();
        if (!initialized) return false;
        return kineticBlockEntityClass.isAssignableFrom(be.getClass());
    }

    public static Optional<Float> getSpeed(BlockEntity be) {
        return invokeMethod(getSpeedMethod, be).map(o -> (Float) o);
    }

    public static Optional<Float> getTheoreticalSpeed(BlockEntity be) {
        return invokeMethod(getTheoreticalSpeedMethod, be).map(o -> (Float) o);
    }

    public static Optional<Float> calculateStressApplied(BlockEntity be) {
        return invokeMethod(calculateStressAppliedMethod, be).map(o -> (Float) o);
    }

    public static Optional<Boolean> isSource(BlockEntity be) {
        return invokeMethod(isSourceMethod, be).map(o -> (Boolean) o);
    }

    public static Optional<Boolean> hasNetwork(BlockEntity be) {
        return invokeMethod(hasNetworkMethod, be).map(o -> (Boolean) o);
    }

    public static Optional<Long> getNetworkId(BlockEntity be) {
        if (!initialized || !isKinetic(be)) return Optional.empty();
        try {
            // networkフィールドから直接値を取得する
            return Optional.ofNullable((Long) networkField.get(be));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<BlockPos> getSourcePos(BlockEntity be) {
        if (!initialized || !isKinetic(be)) return Optional.empty();
        try {
            return Optional.ofNullable((BlockPos) sourceField.get(be));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}