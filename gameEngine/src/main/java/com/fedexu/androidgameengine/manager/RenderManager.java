package com.fedexu.androidgameengine.manager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.fedexu.androidgameengine.Animation;
import com.fedexu.androidgameengine.GameData;
import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;

/**
 * Created by Federico Peruzzi.
 * RenderManager takes care of rendering the scene object of the GameData
 * in the current GameView.
 *
 */

public class RenderManager {

    /**
     * Draw method use the GameView's GameData for cycle every Object in the view
     * and draw the sprite.
     *
     * @param gameData
     */
    public static void draw(GameData gameData) {

        // Check if the surface is avable to draw.
        if (gameData.getSurfaceHolder().getSurface().isValid()) {

            Canvas c = gameData.getSurfaceHolder().lockCanvas();

            // Draw a standard black background.
            c.drawColor(Color.BLACK);

            // Get all Object from GameData.
            ArrayList<GameObject> gameObjects = gameData.getGameObjects();
            for (GameObject g : gameObjects) {

                // If debug flag is true draw the boundingBox.
                if (gameData.isDebugEnable() && g.isVisible())
                    c.drawPath(g.getPath(), g.getPaint());

                // Draw the object sprite if the object is visible.
                Animation animation = g.getCurrentAnimation();
                if (animation != null && g.isVisible()) {
                    c.drawBitmap(animation.getBitmapSheet(), animation.getCurrentFrame(gameData.getDeltaFrameTime()), g.getBoundingBox(), g.getPaint());
                }
            }

            // Display the average FPS if debug flag is true.
            if (gameData.isDebugEnable()) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.WHITE);
                paint.setTextSize(40);
                c.drawText("FPS:" + gameData.getAverageSecondFps(), 30, 80, paint);
            }

            // Unlock the Canvans and draw.
            gameData.getSurfaceHolder().unlockCanvasAndPost(c);
        }
    }

}
