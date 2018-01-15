package com.fedexu.androidgameengine.manager;

import android.graphics.Point;
import android.view.MotionEvent;

import com.fedexu.androidgameengine.GameData;
import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;

/**
 * Created by Federico Peruzzi on 08/01/2018.
 *
 */

public class InputManager {

    public static void touchCheck(GameData gameData, MotionEvent motionEvent) {
        Point pointTouched = new Point((int) motionEvent.getX(), (int) motionEvent.getY());

        //release cache resource
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP){
            gameData.setLockedTouched(null);
        }
        //cache for track object pressed (better performance)
        if (gameData.getLockedTouched() != null){
            gameData.getLockedTouched().onTouch(gameData, motionEvent);
        }
        // loop the list backwards for better performance to other main thread
        ArrayList<GameObject> gameObjects = gameData.getGameObjects();
        for (int i = gameObjects.size()-1; i >= 0;i--) {
            GameObject g = gameObjects.get(i);
            if (ColliderManager.detectCollision(pointTouched, g) == SideCollision.COLLISION) {
                g.onTouch(gameData, motionEvent);
            }
        }
    }

}
