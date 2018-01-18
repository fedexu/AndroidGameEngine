package com.fedexu.androidgameengine;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.fedexu.androidgameengine.manager.ColliderManager;
import com.fedexu.androidgameengine.manager.InputManager;
import com.fedexu.androidgameengine.manager.RenderManager;

/**
 * Created by Federico Peruzzi.
 * Abstract View that holds the game loop and
 * manage the update and draw call.
 *
 */

public abstract class GameView extends SurfaceView implements Runnable {

    /**
     * The data of the view.
     */
    public GameData gameData;

    /**
     * Size in px of the display used.
     */
    public Point displeySize;

    /**
     * Thread to be started and stopped.
     */
    private Thread thisThread;

    /**
     * Flag for the game loop to be started.
     */
    private boolean threadPoused;

    /**
     * Flag for the game loop to do or not the update phase.
     */
    private boolean gameLoopOn;

    /**
     * Create a new view based on the context of the
     * activity and the display size.
     *
     * @param context context of the activity holds this view.
     * @param display display to draw the stuff.
     */
    public GameView(Context context, Display display){
        super(context);

        this.gameData = new GameData(getHolder());
        this.displeySize = new Point();
        this.gameLoopOn = false;

        Point size = new Point();
        display.getSize(size);
        this.displeySize.x = size.x;
        this.displeySize.y = size.y;
    }

    /**
     * Run method of the thread. Holds the game loop.
     */
    @Override
    public void run() {
        while (!this.threadPoused) {
            // Calculate the start time of the game loop
            long startFrameTime = System.currentTimeMillis();

            if(gameLoopOn) {
                gameData.updateGameObjectData();

                ColliderManager.collisionCheck(gameData);

                this.update();
            }
            RenderManager.draw(gameData);

            // Calculate the time nedded for 1 cycle of the game loop.
            long deltaFrameTime = System.currentTimeMillis() - startFrameTime;
            gameData.updateFps(deltaFrameTime);
        }
    }

    /**
     * Called on a separate thread when the display is touched.
     * Simply wrap the user implemented logic by calling first the
     * onTouch method of the object involved.
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        InputManager.touchCheck(gameData, motionEvent);

        this.onTouch(motionEvent);

        return true;

    }

    /**
     * Stop the current thread
     */
    public void pause() {
        threadPoused = true;
        try {
            thisThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    /**
     * Start the current thread
     */
    public void resume() {
        thisThread = new Thread(this);
        threadPoused = false;
        thisThread.start();
    }

    /**
     * Method for set if the update phase will be skipped or not.
     *
     * @param gameLoopOn
     */
    public void setGameLoopOn(boolean gameLoopOn){
        this.gameLoopOn = gameLoopOn;
    }

    /**
     * To be implemented with the logic of what
     * happened when the display is touched.
     *
     * @param motionEvent
     */
    public abstract void onTouch(MotionEvent motionEvent);

    /**
     * To be implemented for update the game data every loop.
     */
    public abstract void update();
}
