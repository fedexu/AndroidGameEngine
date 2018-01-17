package com.fedexu.androidgameengine.geom;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.Arrays;

/**
 * Created by Federico Peruzzi.
 *
 */

public class Polygon {

    /**
     * The total number of points.  The value of <code>npoints</code>
     * represents the number of valid points in this <code>Polygon</code>
     * and might be less than the number of elements in
     * {@link #xpoints xpoints} or {@link #ypoints ypoints}.
     * This value can be NULL.
     *
     * @serial
     * @see #addPoint(int, int)
     * @since 1.0
     */
    public int npoints;

    /**
     * The array of X coordinates.  The number of elements in
     * this array might be more than the number of X coordinates
     * in this <code>Polygon</code>.  The extra elements allow new points
     * to be added to this <code>Polygon</code> without re-creating this
     * array.  The value of {@link #npoints npoints} is equal to the
     * number of valid points in this <code>Polygon</code>.
     *
     * @serial
     * @see #addPoint(int, int)
     * @since 1.0
     */
    public int xpoints[];

    /**
     * The array of Y coordinates.  The number of elements in
     * this array might be more than the number of Y coordinates
     * in this <code>Polygon</code>.  The extra elements allow new points
     * to be added to this <code>Polygon</code> without re-creating this
     * array.  The value of <code>npoints</code> is equal to the
     * number of valid points in this <code>Polygon</code>.
     *
     * @serial
     * @see #addPoint(int, int)
     * @since 1.0
     */
    public int ypoints[];

    /**
     * The bounds of this {@code Polygon}.
     * This value can be null.
     */
    protected Rect bounds;

    /**
     * The center of this {@code Polygon}.
     * This value can be null.
     */
    protected Point center;

    /*
     * Default length for xpoints and ypoints.
     */
    private static final int MIN_LENGTH = 4;

    /**
     * Creates an empty polygon.
     * @since 1.0
     */
    public Polygon() {
        xpoints = new int[MIN_LENGTH];
        ypoints = new int[MIN_LENGTH];
    }

    /**
     * Constructs and initializes a <code>Polygon</code> from the specified
     * parameters.
     * @param xpoints an array of X coordinates
     * @param ypoints an array of Y coordinates
     * @param npoints the total number of points in the
     *                          <code>Polygon</code>
     * @exception  NegativeArraySizeException if the value of
     *                       <code>npoints</code> is negative.
     * @exception  IndexOutOfBoundsException if <code>npoints</code> is
     *             greater than the length of <code>xpoints</code>
     *             or the length of <code>ypoints</code>.
     * @exception  NullPointerException if <code>xpoints</code> or
     *             <code>ypoints</code> is <code>null</code>.
     * @since 1.0
     */
    public Polygon(int xpoints[], int ypoints[], int npoints) {
        // Fix 4489009: should throw IndexOutofBoundsException instead
        // of OutofMemoryException if npoints is huge and > {x,y}points.length
        if (npoints > xpoints.length || npoints > ypoints.length) {
            throw new IndexOutOfBoundsException("npoints > xpoints.length || "+
                    "npoints > ypoints.length");
        }
        // Fix 6191114: should throw NegativeArraySizeException with
        // negative npoints
        if (npoints < 0) {
            throw new NegativeArraySizeException("npoints < 0");
        }
        // Fix 6343431: Applet compatibility problems if arrays are not
        // exactly npoints in length
        this.npoints = npoints;
        this.xpoints = Arrays.copyOf(xpoints, npoints);
        this.ypoints = Arrays.copyOf(ypoints, npoints);
    }

    /**
     * Resets this <code>Polygon</code> object to an empty polygon.
     * The coordinate arrays and the data in them are left untouched
     * but the number of points is reset to zero to mark the old
     * vertex data as invalid and to start accumulating new vertex
     * data at the beginning.
     * All internally-cached data relating to the old vertices
     * are discarded.
     * Note that since the coordinate arrays from before the reset
     * are reused, creating a new empty <code>Polygon</code> might
     * be more memory efficient than resetting the current one if
     * the number of vertices in the new polygon data is significantly
     * smaller than the number of vertices in the data from before the
     * reset.
     */
    public void reset() {
        npoints = 0;
        bounds = null;
        center = null;
    }

    /**
     * Invalidates or flushes any internally-cached data that depends
     * on the vertex coordinates of this <code>Polygon</code>.
     * This method should be called after any direct manipulation
     * of the coordinates in the <code>xpoints</code> or
     * <code>ypoints</code> arrays to avoid inconsistent results
     * from methods such as <code>getBounds</code> or <code>contains</code>
     * that might cache data from earlier computations relating to
     * the vertex coordinates.
     */
    public void invalidate() {
        bounds = null;
        center = null;
    }

    /**
     * Translates the vertices of the <code>Polygon</code> by
     * <code>deltaX</code> along the x axis and by
     * <code>deltaY</code> along the y axis.
     *
     * @param deltaX the amount to translate along the X axis
     * @param deltaY the amount to translate along the Y axis
     */
    public void translate(int deltaX, int deltaY) {
        for (int i = 0; i < npoints; i++) {
            xpoints[i] += deltaX;
            ypoints[i] += deltaY;
        }
        invalidate();
/*        if (bounds != null) {
            calculateBounds(this.xpoints,this.ypoints,this.npoints);
        }

        if (center != null){
            updateCenter();
        }*/
    }

    /**
     * Calculates the bounding box of the points passed to the constructor.
     * Sets <code>bounds</code> to the result.
     *
     * @param xpoints
     * @param ypoints
     * @param npoints
     */
    void calculateBounds(int xpoints[], int ypoints[], int npoints) {
        int boundsMinX = Integer.MAX_VALUE;
        int boundsMinY = Integer.MAX_VALUE;
        int boundsMaxX = Integer.MIN_VALUE;
        int boundsMaxY = Integer.MIN_VALUE;

        for (int i = 0; i < npoints; i++) {
            int x = xpoints[i];
            boundsMinX = Math.min(boundsMinX, x);
            boundsMaxX = Math.max(boundsMaxX, x);
            int y = ypoints[i];
            boundsMinY = Math.min(boundsMinY, y);
            boundsMaxY = Math.max(boundsMaxY, y);
        }
        bounds = new Rect(boundsMinX, boundsMinY, boundsMaxX, boundsMaxY);
        center = new Point(bounds.centerX(), bounds.centerY());
    }

    void updateCenter(){
        center.x = bounds.centerX();
        center.y = bounds.centerY();
    }
    /*
     * Resizes the bounding box to accomodate the specified coordinates.
     * @param x,&nbsp;y the specified coordinates
     */
    void updateBounds(int x, int y) {
        if (x < bounds.left) {
            //bounds.width = bounds.width + (bounds.x - x);
            bounds.left = x;
        }
        else {
            bounds.right = Math.max(bounds.right, x);
            // bounds.x = bounds.x;
        }

        if (y < bounds.top) {
            //bounds.height = bounds.height + (bounds.y - y);
            bounds.top = y;
        }
        else {
            bounds.bottom = Math.max(bounds.bottom, y);
            // bounds.y = bounds.y;
        }
    }

    /**
     * Appends the specified coordinates to this <code>Polygon</code>.
     * <p>
     * If an operation that calculates the bounding box of this
     * <code>Polygon</code> has already been performed, such as
     * <code>getBounds</code> or <code>contains</code>, then this
     * method updates the bounding box.
     * @param       x the specified X coordinate
     * @param       y the specified Y coordinate
     */
    public void addPoint(int x, int y) {
        if (npoints >= xpoints.length || npoints >= ypoints.length) {
            int newLength = npoints * 2;
            // Make sure that newLength will be greater than MIN_LENGTH and
            // aligned to the power of 2
            if (newLength < MIN_LENGTH) {
                newLength = MIN_LENGTH;
            } else if ((newLength & (newLength - 1)) != 0) {
                newLength = Integer.highestOneBit(newLength);
            }

            xpoints = Arrays.copyOf(xpoints, newLength);
            ypoints = Arrays.copyOf(ypoints, newLength);
        }
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        npoints++;

        if (bounds != null) {
            updateBounds(x, y);
        }

        if (center != null){
            updateCenter();
        }
    }

    /**
     * Gets the bounding box of this <code>Polygon</code>.
     * The bounding box is the smallest {@link Rect} whose
     * sides are parallel to the x and y axes of the
     * coordinate space, and can completely contain the <code>Polygon</code>.
     * @return a <code>Rectangle</code> that defines the bounds of this
     * <code>Polygon</code>.
     */
    public Rect getBounds() {
        if (npoints == 0) {
            return new Rect();
        }
        if (bounds == null) {
            calculateBounds(xpoints, ypoints, npoints);
        }
        return bounds;
    }

    public Point getCenter(){
        if (bounds == null){
            calculateBounds(xpoints,ypoints,npoints);
        }

        return this.center;

    }

}
