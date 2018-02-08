package com.fedexu.androidgameengine;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Created by Federico Peruzzi.
 * Animation class holds the sprite sheet information
 * and manage the frame visualization.
 *
 */

public class Animation {

    /**
     * The sprite sheet to be draw.
     */
    private Bitmap bitmapSheet;

    /**
     * The window on the bitmap to be draw.
     */
    private Rect frameToDraw;


    /**
     * How many frames there are in the sprite sheet.
     */
    private int frameCount;

    /**
     * Current frame to be draw.
     */
    private int currentFrame;

    /**
     * Last timestamp when the animation is draw.
     */
    private long lastFrameChangeTime;

    /**
     * Configuration of the time between one sprite sheet frame and the next.
     */
    private int framePeriod;

    /**
     * Sprite sheet with and height.
     * These next two values can be anything you like
     * as long as the ratio doesn't distort the sprite too much.
     */
    private int frameWidth;
    private int frameHeight;

    /**
     * Time remain to the timed animation.
     */
    private long animationDurationTime;

    /**
     * Flag for mark the animation as a timed animation.
     */
    private boolean timed;

    /**
     * Constructor for set the animation with the given parameter.
     *
     * @param bitmapSheet The sprite
     * @param frameCount number of frame in the sprite
     * @param framePeriod time between one frame and the next
     * @param frameWidth width dimension of the sprite
     * @param frameHeight height dimension of the sprite
     */
    public Animation(Bitmap bitmapSheet, int frameCount, int framePeriod, int frameWidth, int frameHeight) {

        this.bitmapSheet = Bitmap.createScaledBitmap(bitmapSheet,frameWidth * frameCount, frameHeight, false);
        this.frameCount = frameCount;
        this.framePeriod = framePeriod;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameToDraw = new Rect(0, 0, this.frameWidth, this.frameHeight);
        this.animationDurationTime = 0;
        this.timed = false;


    }

    /**
     * Get the current frame representation based on the timed flag.
     *
     * @param deltaFrameTime time between one game loop an the other
     * @return Rect window for the correct frame
     */
    public Rect getCurrentFrame(long deltaFrameTime) {
        long time = System.currentTimeMillis();

        if (timed)
            return timedAnimation(time, deltaFrameTime);
        else
            return continuosAnimation(time);
    }

    /**
     * Calculate the right Rect window for the continuous animation.
     *
     * @param time current time
     * @return Rect window for the correct frame
     */
    private Rect continuosAnimation(long time){
        if (time > this.lastFrameChangeTime + this.framePeriod) {
            this.lastFrameChangeTime = time;
            this.currentFrame++;
            if (this.currentFrame >= this.frameCount) {
                this.currentFrame = 0;
            }
        }
        // Update the left and right values of the source of
        // the next frame on the spritesheet.
        this.frameToDraw.left = this.currentFrame * this.frameWidth;
        this.frameToDraw.right = this.frameToDraw.left + this.frameWidth;
        return this.frameToDraw;
    }

    /**
     * Calculate the right Rect window for the continuous animation.
     *
     * @param time current time
     * @param deltaFrameTime time between one game loop an the other
     * @return Rect window for the correct frame
     */
    private Rect timedAnimation(long time,long deltaFrameTime){
        if (this.animationDurationTime >= 0) {
            this.animationDurationTime -= deltaFrameTime;
            return continuosAnimation(time);
        }else{
            this.frameToDraw.left = 0;
            this.frameToDraw.right = this.frameToDraw.left + this.frameWidth;
            return this.frameToDraw;
        }
    }

    /**
     * Configuration for the timed animation.
     *
     * @param time current time
     */
    public void setAnimationDurationTime(long time){
        this.animationDurationTime = time;
        this.timed = true;
    }

    /**
     * Configuration for the timed animation.
     *
     * @param time current time
     */
    public void setAnimationDurationTime(long time, int framePeriod){
        this.framePeriod = framePeriod;
        setAnimationDurationTime(time);
    }

    /**
     * Flip the bitmap of the current Animation, horizontally, vertically
     * or both.
     *
     * @param vertically flag to flip vertically
     * @param horizontally flag to flip horizontally
     */
    public void flipBitmap(boolean vertically, boolean horizontally) {
        Matrix matrix = new Matrix();

        if(vertically){
            matrix.postScale(-1, 1, this.bitmapSheet.getWidth()/2, this.bitmapSheet.getHeight()/2);
            this.bitmapSheet = Bitmap.createBitmap(this.bitmapSheet, 0, 0, this.bitmapSheet.getWidth(), this.bitmapSheet.getHeight(), matrix, true);
        }

        if(horizontally){
            matrix.postScale(1, -1, this.bitmapSheet.getWidth()/2, this.bitmapSheet.getHeight()/2);
            this.bitmapSheet = Bitmap.createBitmap(this.bitmapSheet, 0, 0, this.bitmapSheet.getWidth(), this.bitmapSheet.getHeight(), matrix, true);
        }

    }

    public void setFrameCount (int frameCount){
        this.frameCount = frameCount;
    }

    public Bitmap getBitmapSheet (){
        return this.bitmapSheet;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }
}
