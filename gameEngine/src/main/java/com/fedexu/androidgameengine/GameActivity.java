package com.fedexu.androidgameengine;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by Federico Peruzzi.
 * Abstract android Activity for this GameEngine
 *
 */

public abstract class GameActivity extends Activity {

    /**
     * GameView class that holds the game loop and invoke
     * the update call and draw call.
     *
     */
    public GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create the activity and set the surface with a gameView extended by the user.
        gameView = loadGameView();
        setContentView(gameView);
    }

    /**
     * This method is invoked when an activity is resumed.
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    /**
     * This method is invoked when an activity is paused.
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    /**
     * Disable BackButton pressing, if you want to override for specific purpose do it.
     * for navigate through activitys use GameActivityManager instead.
     *
     */
    @Override
    public void onBackPressed() {

    }

    /**
     * This method is called into the onCreate() method of the Activity.
     * Add to the activity the surface that the user implemented.
     *
     * @return the GameView that control and draw the surface
     */
    public abstract GameView loadGameView();
}
