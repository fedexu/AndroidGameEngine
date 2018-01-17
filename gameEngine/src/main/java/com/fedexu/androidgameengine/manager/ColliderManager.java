package com.fedexu.androidgameengine.manager;

import android.graphics.Point;

import com.fedexu.androidgameengine.GameData;
import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;

/**
 * Created by Federico Peruzzi.
 * ColliderManager take care of check if there is a
 * collision between tow object and from witch side.
 *
 */

public class ColliderManager {

    /**
     * Detect if there is a collision between two <code>GameObject</code>.
     *
     * @param projectile
     * @param collideObject
     * @return SideCollision
     */
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

    /**
     * Detect if there is a collision between a <code>Point</code> and a <code>GameObject</code>.
     * @param point
     * @param collideObject
     * @return SideCollision
     */
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

    /**
     * Detect witch side of the <code>GameObject</code> collided.
     * TIP: if the object is stuck inside we have wrong return.
     * Use before the comeBack() function to avoid mistake.
     *
     * @param projectile
     * @param collideObject
     * @return SideCollision
     */
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

    /**
     * Collision check function take care of calling GameObject's onCollide()
     * method of both colliding objects when a collision is detects.
     *
     * @param gameData
     */
    public static void collisionCheck(GameData gameData) {

        // Detect collision AABB,
        ArrayList<GameObject> gameObjects = gameData.getGameObjects();
        for (GameObject g : gameObjects) {
            // To save cycle time we skip objects that have speed != 0 and have untouchable == false.
            if (g.getSpeed() != 0 && !g.isUntouchable()) {
                for (GameObject collideObject : gameObjects) {
                    // Check only object that isn't itself and isn't untouchable.
                    if (g != collideObject && !collideObject.isUntouchable()) {
                        if (ColliderManager.detectCollision(g, collideObject) == SideCollision.COLLISION) {
                            // Calling the onCollide().
                            g.onCollide(collideObject);
                            collideObject.onCollide(g);
                        }
                    }
                }
            }
        }
    }

    /**
     * Check collision for a GameObject with all other object in the screen.
     * return the GameObject that the input collide with
     *
     * @param gameData
     * @param objectToCheck
     * @return
     */
    public static GameObject collisionCheck(GameData gameData, GameObject objectToCheck) {

        ArrayList<GameObject> gameObjects = gameData.getGameObjects();
        for (GameObject collideObject : gameObjects) {
            // To save cycle time we skip objects that have speed != 0 and have untouchable == false.
            if (objectToCheck != collideObject && !collideObject.isUntouchable()) {
                if (ColliderManager.detectCollision(objectToCheck, collideObject) == SideCollision.COLLISION) {
                    return collideObject;
                }
            }
        }
        // Return null is nothing was found.
        return null;
    }
}
