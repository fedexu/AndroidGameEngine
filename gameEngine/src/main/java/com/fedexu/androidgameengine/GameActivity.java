package com.fedexu.androidgameengine;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by Federico Peruzzi on 10/01/2018.
 *
 */

public abstract class GameActivity extends Activity {
    /**
     * Classe che contiene la View del gioco.
     * la classe conterra il game loop e gestira le call
     * di update e draw.
     */

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inizializziamo la UI e la impostiamo come view principale.
        // La UI vuole come parametri di inzializzazione il contesto
        // e la dimensione dello schermo sul quale dovra girare
        gameView = loadGameView();
        setContentView(gameView);
    }

    /**
     * Questo metodo viene eseguito quando la Activity arriva/torna
     * in primo piano.
     */
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    /**
     * Questo metodo viene eseguito quando la Activity non e piu
     * in primo piano.
     */
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    /**
     * Disable BackButton pressing, if you want to override for specific porpouse do it.
     * for navigate through activitys use GameActivityManager instead.
     */
    @Override
    public void onBackPressed() {

    }

    public abstract GameView loadGameView();
}
