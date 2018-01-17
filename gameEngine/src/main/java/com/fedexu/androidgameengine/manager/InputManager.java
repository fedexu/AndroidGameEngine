package com.fedexu.androidgameengine.manager;

import android.graphics.Point;
import android.view.MotionEvent;

import com.fedexu.androidgameengine.GameData;
import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;

/**
 * Created by Federico Peruzzi.
 * Manage the input event and call the <code>onTouch()</code>
 * for the object touched in the screen.
 *
 */

public class InputManager {

    /**
     * Check if the point touched on the screen is inside a <code>GameObject</code>
     * and call the onTouch() method.
     *
     * @param gameData
     * @param motionEvent
     */
    public static void touchCheck(GameData gameData, MotionEvent motionEvent) {

        // Get the point touched on the screen.
        Point pointTouched = new Point((int) motionEvent.getX(), (int) motionEvent.getY());

        // When the event is the finger leave the display,
        // unlock the object for cache optimization.
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP){
            gameData.setLockedTouched(null);
        }
        // If was set a lock on an object touched (example a finger swipe on an object)
        // call the locked object onTouched().
        if (gameData.getLockedTouched() != null){
            gameData.getLockedTouched().onTouch(gameData, motionEvent);
        }

        // Cycle the object list to find the object touched.
        // Loop on the list is implemented backwards for better performance with other main thread.
        ArrayList<GameObject> gameObjects = gameData.getGameObjects();
        for (int i = gameObjects.size()-1; i >= 0;i--) {
            GameObject g = gameObjects.get(i);
            if (ColliderManager.detectCollision(pointTouched, g) == SideCollision.COLLISION) {
                g.onTouch(gameData, motionEvent);
            }
        }
    }

}
