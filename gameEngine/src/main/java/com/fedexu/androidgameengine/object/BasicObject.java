package com.fedexu.androidgameengine.object;

/**
 * Created by Federico Peruzzi.
 * Class used to create a GameObject fom JsonFile.
 *
 */

public class BasicObject {

    /**
     * Field used to identify the object on the Level Builder
     *
     */
    private int id;

    /**
     * Map the visible flag of <code>GameObject</code>
     *
     */
    private boolean visible;

    /**
     * Map the untouchable flag of <code>GameObject</code>
     */
    private boolean untouchable;

    /**
     * Map the immovable flag of <code>GameObject</code>
     */
    private boolean immovable;

    /**
     * X points of the polygon
     *
     */
    double x [];

    /**
     * Y points of the polygon
     *
     */
    double y [];

    // Getter/setter
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
