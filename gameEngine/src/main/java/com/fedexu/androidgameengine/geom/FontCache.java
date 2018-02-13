package com.fedexu.androidgameengine.geom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by Federico Peruzzi.
 * Object used for load safety a font and cache it.
 *
 */

public class FontCache {

    /**
     * Static hashTable used for cache the loaded fonts
     */
    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    /**
     * Method call when the context is avable. It try to find a font laded, if it fail
     * it try to load it form the context.
     *
     * @param name of the file TTF to load.
     * @param context where the file is located.
     * @return the searched font.
     */
    public static Typeface getCachedTypeFace(String name, Context context) {
        Typeface typeface = fontCache.get(name);
        if(typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), name);
                Log.d("FONT LOADER: " , "font " + name +" loaded!");
            }
            catch (Exception e) {
                Log.e( "Font loader" , e.toString());
            }
            fontCache.put(name, typeface);
        }
        return typeface;
    }


    /**
     * Callable everyware in the app, it return the font with
     * the given name if it was loaded.
     *
     * @param name of the file TTF to load.
     * @return the searched font if exist.
     */
    public static Typeface getCachedTypeFace(String name){
        return fontCache.get(name);
    }
}