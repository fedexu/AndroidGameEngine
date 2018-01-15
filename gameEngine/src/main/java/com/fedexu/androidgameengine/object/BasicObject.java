package com.fedexu.androidgameengine.object;

/**
 * Created by Federico Peruzzi on 08/01/2018.
 *
 */

public class BasicObject {

    private int id;

    private boolean visible;

    private boolean untouchable;

    private boolean immovable;

    double x [];

    double y [];

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isUntouchable() {
        return untouchable;
    }

    public void setUntouchable(boolean untouchable) {
        this.untouchable = untouchable;
    }

    public boolean isImmovable() {
        return immovable;
    }

    public void setImmovable(boolean immovable) {
        this.immovable = immovable;
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }
}
