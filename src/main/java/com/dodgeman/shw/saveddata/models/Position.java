package com.dodgeman.shw.saveddata.models;

public record Position(String dimension, double x, double y, double z, float ry, float rx) {
    public boolean isInTheNether() {
        return dimension.equals("minecraft:the_nether");
    }

    public boolean isInTheEnd() {
        return dimension.equals("minecraft:the_end");
    }
}

