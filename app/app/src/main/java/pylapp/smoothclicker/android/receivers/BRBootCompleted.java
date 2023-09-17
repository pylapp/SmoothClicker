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

import pylapp.smoothclicker.android.tools.Logger;
import pylapp.smoothclicker.android.views.StandaloneActivity;

/**
 * Broadcast receiver which receives broadcasts about the boot-completed-event
 * so as to start the click process.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 16/06/2016
 * @see android.content.BroadcastReceiver
 */
@Deprecated
public class BRBootCompleted extends BroadcastReceiver {


     /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The action of the broadcast for a boot completed event
     */
    public static final String BR_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    private static final String LOG_TAG = BRBootCompleted.class.getSimpleName();


    /* ****************************** *
     * METHODS FROM BroadcastReceiver *
     * ****************************** */

    @Override
    public void onReceive( Context context, Intent intent ){

        if ( context == null ){
            throw new IllegalArgumentException("The BroadcastReceiver BRBootCompleted has received a broadcast without context !");
        }

        if ( intent == null || intent.getAction() == null ){
            Logger.fe(LOG_TAG, "The BroadcastReceiver BRBootCompleted has received a broadcast without intent or action O_ô");
            throw new IllegalArgumentException("The BroadcastReceiver BRBootCompleted has received a broadcast without intent or action O_ô");
        }

        final String action = intent.getAction();
        Logger.d(LOG_TAG, "Receives new broadcast: "+action);

        switch ( action ){
            case BR_BOOT_COMPLETED:
                Logger.i(LOG_TAG, "Starts the StandaloneActivity in a classic mode, with only the JSON config files");
                Intent standaloneClassicActivity = new Intent( context, StandaloneActivity.class);
                standaloneClassicActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(standaloneClassicActivity);
                break;
            default:
                Logger.w(LOG_TAG, "The BroadcastReceiver BRBootCompleted has received a strange broadcast: "+action);
                break;
        }

    }

}
