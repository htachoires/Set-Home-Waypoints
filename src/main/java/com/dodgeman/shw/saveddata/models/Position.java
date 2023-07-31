package com.dodgeman.shw.saveddata.models;

import java.util.Objects;

public final class Position {
    public static final int THE_NETHER_DIM = -1;
    public static final int THE_END_DIM = 1;
    private final int dimension;
    private final double x;
    private final double y;
    private final double z;
    private final float rYaw;
    private final float rPitch;

    public Position(int dimension, double x, double y, double z, float rYaw, float rPitch) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rYaw = rYaw;
        this.rPitch = rPitch;
    }

    public boolean isInTheNether() {
        return dimension == THE_NETHER_DIM;
    }

    public boolean isInTheEnd() {
        return dimension == THE_END_DIM;
    }

    public int dimension() {
        return dimension;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public float rYaw() {
        return rYaw;
    }

    public float rPitch() {
        return rPitch;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Position that = (Position) obj;
        return Objects.equals(this.dimension, that.dimension) &&
                Double.doubleToLongBits(this.x) == Double.doubleToLongBits(that.x) &&
                Double.doubleToLongBits(this.y) == Double.doubleToLongBits(that.y) &&
                Double.doubleToLongBits(this.z) == Double.doubleToLongBits(that.z) &&
                Float.floatToIntBits(this.rYaw) == Float.floatToIntBits(that.rYaw) &&
                Float.floatToIntBits(this.rPitch) == Float.floatToIntBits(that.rPitch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimension, x, y, z, rYaw, rPitch);
    }

    @Override
    public String toString() {
        return "Position[" +
                "dimension=" + dimension + ", " +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "z=" + z + ", " +
                "rYaw=" + rYaw + ", " +
                "rPitch=" + rPitch + ']';
    }
}

