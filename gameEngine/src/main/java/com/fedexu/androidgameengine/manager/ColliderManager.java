package com.fedexu.androidgameengine.manager;

import android.graphics.Point;

import com.fedexu.androidgameengine.GameData;
import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;

/**
 * Created by fperuzzi on 28/12/2017.
 */

public class ColliderManager {

    public static SideCollision detectCollision(GameObject projectile, GameObject collideObject) {
        boolean xCollides = projectile.getBoundingBox().right >= collideObject.getBoundingBox().left &&
                projectile.getBoundingBox().left <= collideObject.getBoundingBox().right;
        boolean yCollides = projectile.getBoundingBox().bottom >= collideObject.getBoundingBox().top &&
                projectile.getBoundingBox().top <= collideObject.getBoundingBox().bottom;

        if (xCollides && yCollides)
            return SideCollision.COLLISION;
        else
            return SideCollision.NO_COLLISION;
    }

    public static SideCollision detectCollision(Point point, GameObject collideObject) {
        boolean xCollides = point.x >= collideObject.getBoundingBox().left &&
                point.x <= collideObject.getBoundingBox().right;
        boolean yCollides = point.y >= collideObject.getBoundingBox().top &&
                point.y <= collideObject.getBoundingBox().bottom;

        if (xCollides && yCollides)
            return SideCollision.COLLISION;
        else
            return SideCollision.NO_COLLISION;
    }

    //da usare solamente dopo aver prima chiamato la comeBack dell'oggetto su cui indagare il lato toccato
    public static SideCollision detectSideCollision(GameObject projectile, GameObject collideObject) {

        boolean leftCollision = projectile.getBoundingBox().left <= collideObject.getBoundingBox().right;
        boolean rightCollision = projectile.getBoundingBox().right >= collideObject.getBoundingBox().left;
        boolean topCollision = projectile.getBoundingBox().top <= collideObject.getBoundingBox().bottom;
        boolean bottomCollision = projectile.getBoundingBox().bottom >= collideObject.getBoundingBox().top;

        if (rightCollision && leftCollision) {
            //we are up or down the object. check the top and bottom.
            if (!topCollision)
                return SideCollision.TOP_COLLISION;
            else
                return SideCollision.BOTTOM_COLLISION;
        } else {
            //we are side the object. check the left and right.
            if (!leftCollision)
                return SideCollision.LEFT_COLLISION;
            else
                return SideCollision.RIGHT_COLLISION;
        }
    }

    public static void collisionCheck(GameData gameData) {

        // detect collision static way
        // discretizzo gli oggetti da controllare per risparmiare calcolo
        ArrayList<GameObject> gameObjects = gameData.getGameObjects();
        for (GameObject g : gameObjects) {
            // controllo solo gli oggetti che si muovono e che non sono intoccabili
            if (g.getSpeed() != 0 && !g.isUntouchable()) {
                for (GameObject collideObject : gameObjects) {
                    // controllo solo gli oggetti che non sono l'oggetto stesso e non siano intoccabili
                    if (g != collideObject && !collideObject.isUntouchable()) {
                        if (ColliderManager.detectCollision(g, collideObject) == SideCollision.COLLISION) {
                            g.onCollide(collideObject);
                            collideObject.onCollide(g);
                        }
                    }
                }
            }
        }
    }

    //collision Check for a single object
    public static GameObject collisionCheck(GameData gameData, GameObject objectToCheck) {

        ArrayList<GameObject> gameObjects = gameData.getGameObjects();
        for (GameObject collideObject : gameObjects) {
            // controllo solo gli oggetti che non sono l'oggetto stesso e non siano intoccabili
            if (objectToCheck != collideObject && !collideObject.isUntouchable()) {
                if (ColliderManager.detectCollision(objectToCheck, collideObject) == SideCollision.COLLISION) {
                    return collideObject;
                }
            }
        }
        //if nothing was found
        return null;
    }
}
