package com.fedexu.androidgameengine;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Federico Peruzzi on 03/01/2018.
 *
 */

public class Animation {

    Bitmap bitmapSheet;

    Rect frameToDraw;
    // How many frames are there on the sprite sheet?
    private int frameCount;

    private int currentFrame;
    private long lastFrameChangeTime;

    // How long should each frame last
    private int framePeriod;

    // These next two values can be anything you like
    // As long as the ratio doesn't distort the sprite too much
    private int frameWidth;
    private int frameHeight;

    //Time remain to the timed animation
    private long animationDurationTime;
    private boolean timed;

    public Animation(Bitmap bitmapSheet, int frameCount, int framePeriod, int frameWidth, int frameHeight) {

        // Scale the bitmap to the correct size
        // We need to do this because Android automatically
        // scales bitmaps based on screen density
        this.bitmapSheet = Bitmap.createScaledBitmap(bitmapSheet,frameWidth * frameCount, frameHeight, false);
        this.frameCount = frameCount;
        this.framePeriod = framePeriod;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameToDraw = new Rect(0, 0, this.frameWidth, this.frameHeight);
        this.animationDurationTime = 0;
        this.timed = false;


    }

    public Rect getCurrentFrame(long deltaFrameTime) {
        long time = System.currentTimeMillis();

        if (timed)
            return timedAnimation(time, deltaFrameTime);
        else
            return continuosAnimation(time);
    }

    private Rect continuosAnimation(long time){
        if (time > this.lastFrameChangeTime + this.framePeriod) {
            this.lastFrameChangeTime = time;
            this.currentFrame++;
            if (this.currentFrame >= this.frameCount) {
                this.currentFrame = 0;
            }
        }
        //update the left and right values of the source of
        //the next frame on the spritesheet
        this.frameToDraw.left = this.currentFrame * this.frameWidth;
        this.frameToDraw.right = this.frameToDraw.left + this.frameWidth;
        return this.frameToDraw;
    }

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

    public void setAnimationDurationTime(long time){
        this.animationDurationTime = time;
        this.timed = true;
    }

    public void setAnimationDurationTime(long time, int framePeriod){
        this.framePeriod = framePeriod;
        setAnimationDurationTime(time);
    }

    public void setFrameCount (int frameCount){
        this.frameCount = frameCount;
    }

    public Bitmap getBitmapSheet (){
        return this.bitmapSheet;
    }


}
