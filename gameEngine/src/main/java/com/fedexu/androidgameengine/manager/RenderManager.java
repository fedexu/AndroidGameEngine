package com.fedexu.androidgameengine.manager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.fedexu.androidgameengine.Animation;
import com.fedexu.androidgameengine.GameData;
import com.fedexu.androidgameengine.object.GameObject;

import java.util.ArrayList;

/**
 * Created by fperuzzi on 28/12/2017.
 *
 */

public class RenderManager {

    public static void draw(GameData gameData) {
        //to be Render manager
        if (gameData.getSurfaceHolder().getSurface().isValid()) {

            Canvas c = gameData.getSurfaceHolder().lockCanvas();
            //Funzionamento con array di GameObject
            c.drawColor(Color.BLACK);
            ArrayList<GameObject> gameObjects = gameData.getGameObjects();
            for (GameObject g : gameObjects) {
                if (gameData.isDebugEnable() && g.isVisible())
                    c.drawPath(g.getPath(), g.getPaint());

                Animation animation = g.getCurrentAnimation();
                if (animation != null && g.isVisible()) {
                    //to be drow bitmap
                    c.drawBitmap(animation.getBitmapSheet(), animation.getCurrentFrame(gameData.getDeltaFrameTime()), g.getBoundingBox(), g.getPaint());
                }
            }

            if (gameData.isDebugEnable()) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.WHITE);
                paint.setTextSize(40);
                c.drawText("FPS:" + gameData.getAverageSecondFps(), 30, 80, paint);
            }

            gameData.getSurfaceHolder().unlockCanvasAndPost(c);
        }
    }

}
