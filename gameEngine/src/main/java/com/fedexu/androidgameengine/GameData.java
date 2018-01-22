package com.fedexu.androidgameengine;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Federico Peruzzi.
 * Class that holds a view generic data. For a specific data to hold,
 * put a custom class in the viewData variable.
 *
 */

public class GameData<T> {

    /**
     * List of object to be managed in the view.
     */
    private ArrayList<GameObject> gameObjects;

    /**
     * A map of clock for timing some action.
     */
    private Map<String,Long> clocks;

    /**
     * GameObject to lock if necessary after a touch on the screen.
     */
    private GameObject lockedTouched;

    /**
     * The surface where the object is drawn.
     */
    private SurfaceHolder surfaceHolder;

    /**
     * Fps in the current cycle.
     */
    private long fps;

    /**
     * Delta time between one fps and the next.
     */
    private long deltaFrameTime;

    /**
     * Resets after 1 sec.
     */
    private long averageSecond;

    /**
     * Average fps.
     */
    private long averageSecondFps;

    /**
     * Number of fps drawn.
     */
    private long nFps;

    /**
     * If true, the Engine draw the bounding box and the fps.
     */
    private boolean debugEnable;

    /**
     * Holds custom additional class data for the user implementation
     * TODO find a more elegant way to do this and not a generic Object
     */
    private T viewData;

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

    /**
     * Update every cycle the fps information and decrease clocks time.
     *
     * @param deltaFrameTime
     */
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

    /**
     * Run every cycle. Update the Positon of GameObjects
     */
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
