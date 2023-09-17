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

import pylapp.smoothclicker.android.clickers.ServiceClicker;
import pylapp.smoothclicker.android.tools.Logger;

/**
 * Broadcast receiver which receives broadcasts about the battery state and the boot state so as to start or stop the service making clicks..
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 18/03/2016
 * @see android.content.BroadcastReceiver
 */
@Deprecated
public class BRClicker extends BroadcastReceiver {


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The action of the broadcast for a boot completed event
     */
    public static final String BR_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
//    /**
//     * The action of the broadcast for a battery okay event
//     */
//    private static final String BR_BATTERY_OKAY = "android.intent.action.ACTION_BATTERY_OKAY";
    /**
     * The action of the broadcast for a battery low event
     */
    public static final String BR_BATTERY_LOW = "android.intent.action.ACTION_BATTERY_LOW";


    private static final String LOG_TAG = BRClicker.class.getSimpleName();


    /* ****************************** *
     * METHODS FROM BroadcastReceiver *
     * ****************************** */

    @Override
    public void onReceive( Context context, Intent intent ){

        if ( context == null ){
            throw new IllegalArgumentException("The BroadcastReceiver BRClicker has received a broadcast without context !");
        }

        if ( intent == null || intent.getAction() == null ){
            Logger.fe(LOG_TAG, "The BroadcastReceiver BRClicker has received a broadcast without intent or action O_ô");
            throw new IllegalArgumentException("The BroadcastReceiver BRClicker has received a broadcast without intent or action O_ô");
        }

        final String action = intent.getAction();
        Logger.d(LOG_TAG, "Receives new broadcast: "+action);

        switch ( action ){
            case BR_BATTERY_LOW:
                stopClickerService( context );
                break;
//            case BR_BATTERY_OKAY:
//                restartClickerService( context );
//                break;
            case BR_BOOT_COMPLETED:
                startService( context );
                break;
            default:
                Logger.w(LOG_TAG, "The BroadcastReceiver BRClicker has received a strange broadcast: "+action);
                break;
        }

    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Starts the clicker service
     * @param c - The context to sue to start the service
     */
    private void startService( Context c ){
        Intent i = new Intent(c, ServiceClicker.class);
        i.setAction(ServiceClicker.SERVICE_CLICKER_INTENT_FILTER_NAME_WAKEUP);
        c.startService(i);
    }

    /**
     * Stops the clicker service
     * @param c - The context to use to start the service
     */
    private void stopClickerService( Context c ){
        Intent i = new Intent(c, ServiceClicker.class);
        i.setAction(ServiceClicker.SERVICE_CLICKER_INTENT_FILTER_NAME_STOP);
        c.stopService(i);
    }

//    /**
//     * Restarts the clicker service
//     * @param c - The context to use to start the service
//     */
//    private void restartClickerService( Context c ){
//        stopClickerService(c);
//        startService(c);
//    }

}
