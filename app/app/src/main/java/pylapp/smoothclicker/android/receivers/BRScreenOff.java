/*
    MIT License

    Copyright (c) 2016  Pierre-Yves Lapersonne (Mail: dev@pylapersonne.info)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
// ✿✿✿✿ ʕ •ᴥ•ʔ/ ︻デ═一


package pylapp.smoothclicker.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import pylapp.smoothclicker.android.tools.Logger;
import pylapp.smoothclicker.android.tools.screen.WakelockManager;
import pylapp.smoothclicker.android.views.SettingsActivity;

/**
 * Broadcast receiver which receives broadcasts about the screen off events.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 01/06/2016
 * @see BroadcastReceiver
 */
public class BRScreenOff extends BroadcastReceiver {


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The action of the broadcast for screen-off event
     */
    public static final String SCREEN_OFF_ACTION = Intent.ACTION_SCREEN_OFF;


    private static final String LOG_TAG = BRScreenOff.class.getSimpleName();


    /* ****************************** *
     * METHODS FROM BroadcastReceiver *
     * ****************************** */

    /**
     * Triggered when the broadcast receiver receives the intent it is listening, i.e. an intent
     * with the SCREEN_OFF action.
     * It this very case the receiver will only force the screen to on.
     * @param context -
     * @param intent -
     */
    @Override
    public void onReceive( Context context, Intent intent ){

        /*
         * Some checks...
         */
        if ( context == null ){
            throw new IllegalArgumentException("The BroadcastReceiver BRScreenOff has received a broadcast without context !");
        }

        if ( intent == null || intent.getAction() == null ){
            Logger.wtf(LOG_TAG, "The BroadcastReceiver BRScreenOff has received a broadcast without intent or action O_ô");
            throw new IllegalArgumentException("The BroadcastReceiver BRScreenOff has received a broadcast without intent or action O_ô");
        }

        // Screen on ?
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isEnabled = sp.getBoolean(SettingsActivity.PREF_KEY_FORCESCREENON_BATTERY, false);
        if ( ! isEnabled ) return;

        /*
         * Force the screen state
         */

        // Screen on ?
        isEnabled = sp.getBoolean(SettingsActivity.PREF_KEY_WAKELOCK, false);
        if ( isEnabled ){
            WakelockManager.instance.refreshContext(context);
            WakelockManager.instance.acquireWakelock();
        }

    } // End of public void onReceive( Context context, Intent intent )

}
