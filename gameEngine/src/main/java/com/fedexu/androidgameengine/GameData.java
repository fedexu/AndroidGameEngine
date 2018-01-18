package com.fedexu.androidgameengine;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Federico Peruzzi.
 *
 */

public class GameData<T> {

    protected ArrayList<GameObject> gameObjects;

    protected Map<String,Long> clocks;

    protected GameObject lockedTouched;

    protected Canvas canvas;

    protected SurfaceHolder surfaceHolder;

    protected long fps;

    protected long deltaFrameTime;

    protected long averageSecond;

    protected long averageSecondFps;

    protected long nFps;

    protected boolean debugEnable;

    //Holds custom additional class data for the user implementation
    //TODO find a more elegant way to do this and not a generic Object
    protected T viewData;

    public GameData(SurfaceHolder surfaceHolder) {

        this.gameObjects = new ArrayList<GameObject>();
        this.surfaceHolder = surfaceHolder;
        this.debugEnable = false;
        this.averageSecond = 0;
        this.averageSecondFps = 0;
        this.nFps = 0;
        this.deltaFrameTime = 0;
        this.clocks = new HashMap<>();

    }

    public ArrayList<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public long getFps() {
        return fps;
    }

    public boolean isDebugEnable() {
        return debugEnable;
    }

    public void setDebugEnable(boolean debugEnable) {
        this.debugEnable = debugEnable;
    }

    public long getAverageSecondFps() {
        return averageSecondFps;
    }

    public GameObject getLockedTouched(){
        return this.lockedTouched;
    }

    //until the touchScreen is released
    public void setLockedTouched(GameObject lockedTouched){
        this.lockedTouched = lockedTouched;
    }

    public long getDeltaFrameTime(){
        return this.deltaFrameTime;
    }

    public void updateFps(long deltaFrameTime) {
        this.deltaFrameTime = deltaFrameTime;
        if (deltaFrameTime >= 1) {
            this.fps = 1000 / deltaFrameTime;
            //Protezione di minimo fps in caso di debugging
            if (this.getFps() == 0) this.fps = 25;
        }
        //for android simulator only
        //gameData.setFps(25);
        this.averageSecond += deltaFrameTime;
        this.nFps += 1;
        if (this.averageSecond > 1000) {
            this.averageSecondFps = this.nFps;
            this.averageSecond = 0;
            this.nFps = 0;
        }

        for (Map.Entry<String, Long> entry : this.clocks.entrySet()){
            entry.setValue(entry.getValue() - deltaFrameTime) ;
        }
    }

    public void updateGameObjectData() {
        for (GameObject g : this.getGameObjects()) {
                if (g.getSpeed() != 0)
                    g.updatePosition(this);
                g.update(this);
        }
    }

    public void addClock(String name, long time){
        if (this.clocks.get(name) != null){
            this.clocks.remove(name);
        }
        this.clocks.put(name, time);
    }

    public Long getClock(String name){
        return this.clocks.get(name);
    }

    public T getViewData() {
        return viewData;
    }

    public void setViewData(T viewData) {
        this.viewData = viewData;
    }
}
