package com.dodgeman.shw.saveddata.models;

import java.util.Objects;

public final class Home {
    private final Position position;

    public Home(Position position) {
        this.position = position;
    }

    public Position position() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Home that = (Home) obj;
        return Objects.equals(this.position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Home[" +
                "position=" + position + ']';
    }

}
