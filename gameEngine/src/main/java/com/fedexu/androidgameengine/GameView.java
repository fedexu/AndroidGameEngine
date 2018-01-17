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
 *
 */

public abstract class GameView extends SurfaceView implements Runnable {

    public GameData gameData;

    public Point displeySize;

    private Thread thisThread;

    private boolean threadPoused;

    private boolean gameLoopOn;

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

    @Override
    public void run() {
        while (!this.threadPoused) {
            long startFrameTime = System.currentTimeMillis();

            if(gameLoopOn) {
                gameData.updateGameObjectData();

                ColliderManager.collisionCheck(gameData);

                this.update();
            }
            RenderManager.draw(gameData);

            long deltaFrameTime = System.currentTimeMillis() - startFrameTime;
            gameData.updateFps(deltaFrameTime);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        InputManager.touchCheck(gameData, motionEvent);

        this.onTouch(motionEvent);

        return true;

    }

    public void pause() {
        threadPoused = true;
        try {
            thisThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        thisThread = new Thread(this);
        threadPoused = false;
        thisThread.start();
    }

    public void setGameLoopOn(boolean gameLoopOn){
        this.gameLoopOn = gameLoopOn;
    }

    public abstract void onTouch(MotionEvent motionEvent);

    public abstract void update();
}
