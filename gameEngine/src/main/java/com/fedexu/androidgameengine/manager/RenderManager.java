package com.fedexu.androidgameengine.manager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.fedexu.androidgameengine.Animation;
import com.fedexu.androidgameengine.EngineUtils;
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
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setTextSize(40);

            // Draw a standard black background.
            c.drawColor(Color.BLACK);

            // Get all Object from GameData.
            ArrayList<GameObject> gameObjects = gameData.getGameObjects();
            for (GameObject g : gameObjects) {

                // If debug flag is true draw the boundingBox.
                if (gameData.isDebugEnable() && g.isVisible())
                    c.drawPath(g.getPath(), paint);

                // Draw the object sprite if the object is visible.
                Animation animation = g.getCurrentAnimation();
                if (animation != null && g.isVisible()) {
                    /*if(g.getGradientAngle() != 0){
                        Matrix matrix = new Matrix();

                        matrix.postScale(g.getBoundingBox().width(), g.getBoundingBox().height());
                        matrix.postRotate((float)g.getGradientAngle(), g.getBoundingBox().width()/2, g.getBoundingBox().height()/2);

                        matrix.setTranslate(g.getBoundingBox().left, g.getBoundingBox().top);

                        c.drawBitmap(EngineUtils.cropBitmap(animation.getBitmapSheet(), animation.getCurrentFrame(gameData.getDeltaFrameTime())), matrix, null);
                    }else*/
                        c.drawBitmap(animation.getBitmapSheet(), animation.getCurrentFrame(gameData.getDeltaFrameTime()), g.getBoundingBox(), paint);
                }
            }

            // Display the average FPS if debug flag is true.
            if (gameData.isDebugEnable()) {
                c.drawText("FPS:" + gameData.getAverageSecondFps(), 30, 80, paint);
            }

            // Unlock the Canvans and draw.
            gameData.getSurfaceHolder().unlockCanvasAndPost(c);
        }
    }

}
