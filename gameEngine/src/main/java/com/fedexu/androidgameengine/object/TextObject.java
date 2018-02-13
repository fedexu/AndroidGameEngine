package com.fedexu.androidgameengine.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;


/**
 * Created by Federico Peruzzi.
 */

public class TextObject {

    private Paint paint;

    private ArrayList<String> text;

    private float offsetX;

    private float offsetY;

    private int pixelSize;

    public TextObject (Typeface font, int pixelSize, float offsetX, float offsetY){
        this.paint = new Paint();
        this.paint.setTypeface(font);
        this.paint.setTextSize(pixelSize);
        this.text = new ArrayList<>();

        this.pixelSize = pixelSize;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void addLine(String line){
        this.text.add(line);
    }

    public int lineNumber(){
        return this.text.size();
    }

    public void removeLine(int num){
        this.text.remove(num);
    }

    public void removeAll(){this.text.clear();}

    public Paint getPaint() {
        return paint;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(int pixelSize) {
        this.pixelSize = pixelSize;
    }
}
