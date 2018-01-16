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
	* Shape Define
	*/
    private Polygon polygon;

   /**
	* Last fps object rapresentation
	*/
    private Polygon oldPolygon;

   /**
	* Minum bounding box for AABB collision
	*/
    private Rect boundingBox;

   /**
	* Animations objects
	*/
    private Map<String, Animation> animations;
	
   /**
	* Current index Animation used
	*/
    private String currentAnimation;

   /**
	* This object is uset do draw the bounding box for debug
	*/ 
    private Paint paint;

   /**
	* Angle to apply the speed vector
	*/
    private double directionAngle;
	
   /**
	* Speed vector
	* If the speed is equal to 0 the method onCollide() will not be called
	*/
    private float speed;

   /**
	* If false the object will not be drawn
	*/ 
    private boolean isVisible;

   /**
	* If true the object will not be considerate for collision and onCollide() method
	*/
    private boolean isUntouchable;

   /**
	* If true the object will not update the position regardless the speed applyed
	*/
    private boolean immovable;

   /**
	* Empty costructor
	*/
    public GameObject() {

        this.polygon = new Polygon();
        this.oldPolygon = new Polygon();
        this.speed = 0;
        this.directionAngle = 0;

        this.isVisible = true;
        this.isUntouchable = false;
        this.immovable = false;

        this.animations = new HashMap<>();

        this.paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

    }

    public GameObject(BasicObject basicObject){

        this.polygon = new Polygon();
        this.oldPolygon = new Polygon();
        this.speed = 0;
        this.directionAngle = 0;

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

    public void setDirectionAngle(double directionAngle) {
        this.directionAngle = EngineUtils.normalizeAngle(directionAngle);
    }

    public double getSpeedX() {
        return Math.cos(Math.toRadians(this.getDirectionAngle()) * this.getSpeed());
    }

    public double getSpeedY() {
        return Math.sin(Math.toRadians(this.getDirectionAngle()) * this.getSpeed());
    }

    public Point getCenter() {
        return this.polygon.getCenter();
    }

    public Rect getBoundingBox() {
        if (boundingBox == null){
            this.boundingBox = this.polygon.getBounds();
        }
        return boundingBox;
    }

    public void updateBoundingBox(){
        this.boundingBox = this.polygon.getBounds();
    }

    public Animation getCurrentAnimation(){
        if(this.animations.isEmpty())
            return null;
        return this.animations.get(this.currentAnimation);
    }

    public void setCurrentAnimation(String name){
        this.currentAnimation = name;
    }

    public double bounceLeftRight() {
        //controllo se sono nel primo o nel terzo quandrante
        if ( (Math.toRadians(this.directionAngle) > 0 && Math.toRadians(this.directionAngle) < Math.PI/2 )||
                (Math.toRadians(this.directionAngle) > Math.PI && Math.toRadians(this.directionAngle) < (Math.PI*3)/2))
            return this.directionAngle + 90;
        else
            return this.directionAngle - 90;
    }

    public double bounceTopBottom() {
        return Math.abs(this.directionAngle - 360);
    }

    public void comeBack() {
        this.polygon = new Polygon(this.oldPolygon.xpoints, this.oldPolygon.ypoints, this.oldPolygon.npoints);
        this.updateBoundingBox();
    }

    public void translate(int x, int y) {
        this.oldPolygon = new Polygon(this.polygon.xpoints, this.polygon.ypoints, this.polygon.npoints);
        int deltaX = x - this.getCenter().x;
        int deltaY = y - this.getCenter().y;
        this.polygon.translate(deltaX, deltaY);
        this.updateBoundingBox();
    }

    public void translateX(int x) {
        translate(x, this.getCenter().y);
    }

    public void translateY(int y) {
        translate(this.getCenter().x, y);
    }

    public void addAnimation(String name, Animation animation){
        this.animations.put(name,animation);
        this.currentAnimation = name;
    }

    public void updatePosition(GameData gameData) {
        if (!this.immovable) {
            this.translate((int) ((Math.cos(Math.toRadians(this.getDirectionAngle())) * this.getSpeed()) * (1 / (double) gameData.getFps()) + this.getCenter().x),
                    (int) (((-Math.sin(Math.toRadians(this.getDirectionAngle()))) * this.getSpeed()) * (1 / (double) gameData.getFps()) + this.getCenter().y));
        }
    }


    public abstract void update(GameData gameData);

    public abstract void onCollide(GameObject collideObject);

    public abstract void onTouch(GameData gameData, MotionEvent motionEvent);

}

