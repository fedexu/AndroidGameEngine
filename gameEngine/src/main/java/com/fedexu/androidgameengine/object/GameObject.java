package com.fedexu.androidgameengine.object;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.fedexu.androidgameengine.Animation;
import com.fedexu.androidgameengine.EngineUtils;
import com.fedexu.androidgameengine.GameData;
import com.fedexu.androidgameengine.geom.Polygon;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Federico Peruzzi.
 * Abstract class for all GameObjects implemented.
 *
 */

public abstract class GameObject {

    /**
     * Shape Define.
     */
    protected Polygon polygon;

    /**
     * Last fps object rapresentation.
     */
    private Polygon oldPolygon;

    /**
     * Minum bounding box for AABB collision.
     */
    private Rect boundingBox;

    /**
     * Animations objects.
     */
    private Map<String, Animation> animations;

    /**
     * Current index Animation used.
     */
    private String currentAnimation;

    /**
     * This object is uset do draw the bounding box for debug.
     */
    private Paint paint;

    /**
     * Angle to apply the speed vector.
     */
    private double directionAngle;

    /**
     * Speed vector.
     */
    private float speed;

    /**
     * Angle of inclination applied to the object
     */
    private double gradientAngle;

    /**
     * Speed applied on the gradientAngle
     */
    private double spinSpeed;

    /**
     * Acceleration vector.
     * Will be added the delta velocity every update.
     */
    private float acceleration;

    /**
     * If false the object will not be drawn.
     */
    private boolean isVisible;

    /**
     * If true the object will not be considerate for collision and onCollide() method.
     */
    private boolean isUntouchable;

    /**
     * If true the object will not update the position regardless the speed applyed.
     */
    private boolean immovable;

    /**
     * Empty constructor:
     * simply initialize the object value.
     */
    public GameObject() {

        this.polygon = new Polygon();
        this.oldPolygon = new Polygon();
        this.speed = 0;
        this.directionAngle = 0;
        this.gradientAngle = 0;
        this.spinSpeed = 0;


        this.isVisible = true;
        this.isUntouchable = false;
        this.immovable = false;

        this.animations = new HashMap<>();

        this.paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

    }

    /**
     * Constructor with the BasicObject class:
     * this constructor initialize the object like the empty
     * constructor but create the Polygon element with the x,y points
     * and the visibility flag defined by the BasicObject.
     *
     * @param basicObject
     */
    public GameObject(BasicObject basicObject){

        this.polygon = new Polygon();
        this.oldPolygon = new Polygon();
        this.speed = 0;
        this.acceleration = 0;
        this.directionAngle = 0;
        this.gradientAngle = 0;
        this.spinSpeed = 0;

        this.isVisible = basicObject.isVisible();
        this.isUntouchable = basicObject.isUntouchable();
        this.immovable = basicObject.isImmovable();

        this.animations = new HashMap<>();

        this.paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

        for(int i = 0; i < basicObject.getX().length; i++){
            this.polygon.addPoint((int)basicObject.getX()[i], (int)basicObject.getY()[i]);
        }

    }

    // Setter/getter

    public Polygon getPolygon() {
        return this.polygon;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public Path getPath() {

        Path path = new Path();
        path.reset();

        for (int i = 0; i <= this.polygon.npoints; i++) {
            if (i == 0) {
                path.moveTo(this.getPolygon().xpoints[i], this.getPolygon().ypoints[i]);
            } else {
                if (i == this.polygon.npoints) {
                    path.lineTo(this.getPolygon().xpoints[0], this.getPolygon().ypoints[0]);
                } else {
                    path.lineTo(this.getPolygon().xpoints[i], this.getPolygon().ypoints[i]);
                }
            }
        }

        return path;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setVisible(boolean visibile) {
        this.isVisible = visibile;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return this.speed;
    }

    public boolean isUntouchable() {
        return isUntouchable;
    }

    public void setUntouchable(boolean untouchable) {
        isUntouchable = untouchable;
    }

    public boolean isImmovable() {
        return immovable;
    }

    public void setImmovable(boolean immovable) {
        this.immovable = immovable;
    }

    public double getDirectionAngle() {
        return directionAngle;
    }

    public Point getCenter() {
        return this.polygon.getCenter();
    }

    public void setCurrentAnimation(String name){
        this.currentAnimation = name;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public double getGradientAngle() {
        return gradientAngle;
    }

    public void setGradientAngle(double gradientAngle) {
        this.gradientAngle = gradientAngle;
    }

    public double getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(double spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    // END setter/getter

    /**
     * Set the direction angle. Normalize the angle to
     * stay in the 360 degree before the set.
     *
     * @param directionAngle
     */
    public void setDirectionAngle(double directionAngle) {
        this.directionAngle = EngineUtils.normalizeAngle(directionAngle);
    }

    /**
     * Calculate the x value of the speed vector.
     *
     * @return double
     */
    public double getSpeedX() {
        return Math.cos(Math.toRadians(this.getDirectionAngle()) * this.getSpeed());
    }

    /**
     * Calculate the y value of the speed vector.
     *
     * @return double
     */
    public double getSpeedY() {
        return Math.sin(Math.toRadians(this.getDirectionAngle()) * this.getSpeed());
    }

    /**
     * Gets the smallest {@link Rect} that can completely contain
     * the <code>Polygon</code>.
     *
     * @return a <code>Rectangle</code> that defines the bounds of this
     * <code>Polygon</code>.
     */
    public Rect getBoundingBox() {
        if (boundingBox == null){
            this.boundingBox = this.polygon.getBounds();
        }
        return boundingBox;
    }

    /**
     * Keeps consistent the bounding box value.
     */
    public void updateBoundingBox(){
        this.boundingBox = this.polygon.getBounds();
    }

    /**
     * Get the animation in use.
     *
     * @return <code>Animation</code>
     */
    public Animation getCurrentAnimation(){
        if(this.animations.isEmpty())
            return null;
        return this.animations.get(this.currentAnimation);
    }

    /**
     * Easy method to fix the "stuck inside" problem with the AABB collision.
     * To be called in the onCollide method.
     */
    public void comeBack() {
        this.polygon = new Polygon(this.oldPolygon.xpoints, this.oldPolygon.ypoints, this.oldPolygon.npoints);
        this.updateBoundingBox();
    }

    /**
     * Translate the object in the given x,y point.
     *
     * @param x
     * @param y
     */
    public void translate(int x, int y) {
        this.oldPolygon = new Polygon(this.polygon.xpoints, this.polygon.ypoints, this.polygon.npoints);
        int deltaX = x - this.getCenter().x;
        int deltaY = y - this.getCenter().y;
        this.polygon.translate(deltaX, deltaY);
        this.updateBoundingBox();
    }

    /**
     * Translate the object in the given x point.
     * Ignore the y axis.
     *
     * @param x
     */
    public void translateX(int x) {
        translate(x, this.getCenter().y);
    }

    /**
     * Translate the object in the given y point.
     * Ignore the x axis.
     *
     * @param y
     */
    public void translateY(int y) {
        translate(this.getCenter().x, y);
    }

    /**
     * Add an animation into the animations collection.
     *
     * @param key
     * @param animation
     */
    public void addAnimation(String key, Animation animation){
        this.animations.put(key,animation);
        this.currentAnimation = key;
    }

    /**
     * Method for update the x,y coordinate of the object.
     * If the object is marked as immovable this method will be skip.
     * It is used by the Engine itself. Not need to be called by the user.
     *
     * @param gameData
     */
    public void updatePosition(GameData gameData) {

        if (this.getAcceleration() != 0)
            this.setSpeed((float) (this.getSpeed() + ( this.getAcceleration() * (1 / (double) gameData.getFps() ) )) );

        this.translate((int) ((Math.cos(Math.toRadians(this.getDirectionAngle())) * this.getSpeed()) * (1 / (double) gameData.getFps()) + this.getCenter().x),
                (int) (((-Math.sin(Math.toRadians(this.getDirectionAngle()))) * this.getSpeed()) * (1 / (double) gameData.getFps()) + this.getCenter().y));

        if (this.getSpinSpeed() != 0)
            this.gradientAngle += this.getSpinSpeed() * (1 / (double) gameData.getFps());
        
        while(this.gradientAngle > 360){
            this.gradientAngle -= 360;
        }
    }

    /**
     * Abstract method need to be implement by the user.
     * Will be call every game loop.
     *
     * @param gameData
     */
    public abstract void update(GameData gameData);

    /**
     * Abstract method need to be implement by the user.
     * Will be call in every collision detect.
     *
     * @param collideObject
     */
    public abstract void onCollide(GameObject collideObject);

    /**
     * Abstract method need to be implement by the user.
     * Will be call when the object is touch on the screen.
     *
     * @param gameData
     * @param motionEvent
     */
    public abstract void onTouch(GameData gameData, MotionEvent motionEvent);

}

