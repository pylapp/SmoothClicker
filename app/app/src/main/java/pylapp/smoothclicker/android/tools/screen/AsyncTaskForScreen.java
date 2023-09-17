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

package pylapp.smoothclicker.android.tools.screen;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import pylapp.smoothclicker.android.receivers.BRScreenOff;
import pylapp.smoothclicker.android.utils.Config;
import pylapp.smoothclicker.android.views.SettingsActivity;


/**
 * Async Task which consists on working on the device and more on its screen of displayed elements.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.1.0
 * @since 13/06/2016
 * @see AsyncTask
 */
public abstract class AsyncTaskForScreen<T, U, V> extends AsyncTask<T, U, V>{


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The application context
     */
    protected Activity mContext;

    /**
     * The receiver which will deal with the screen-off events
     */
    private BRScreenOff mBrScreenOff;

    /**
     * Flag which indicates if the broadcast receiver dedicated to screen-off events is registered
     */
    private boolean mIsBrScreenOffRegistered;


    //private static final String LOG_TAG = AsyncTaskForScreen.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     *
     * @param c - The context to use, must not be null
     */
    public AsyncTaskForScreen(Activity c){
        super();
        if ( c == null ) throw new IllegalArgumentException("The context parameter must not be null!");
        mContext = c;
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     * Forces the screen state making the screen on and unlocking the device (if needed).
     * It registers a receiver which will deal with screen-off events
     */
    protected void forceScreenState(){
        forceScreenOnIfNeeded();
        forceScreenUnlockIfNeeded();
        registerScreenOffReceiver();
    }

    /**
     * Forces the screen's state to make it always on if needed (i.e. if the settings have the dedicated option activated).
     * It will make the screen on and keeps it on.
     * Wakelocks will be used to awake the screen and keep it on.
     */
    protected void forceScreenOnIfNeeded(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isEnabled = sp.getBoolean(SettingsActivity.PREF_KEY_WAKELOCK, false);
        if ( isEnabled ){
            WakelockManager.instance.refreshContext(mContext);
            WakelockManager.instance.acquireWakelock();
        }

    }

    /**
     * Forces the phone to have its screen unlocked by executing a dedicated custom script
     * the user has put in its device
     */
    protected void forceScreenUnlockIfNeeded(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isEnabled = sp.getBoolean(SettingsActivity.PREF_KEY_UNLOCK_SCRIPT, false);

        if ( isEnabled ){
            KeyguardManager km = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
            if (km.inKeyguardRestrictedInputMode()) {
                UnlockerStub unlocker = new UnlockerImpl(sp.getString(SettingsActivity.PREF_KEY_FILE_UNLOCK_NAME, Config.DEFAULT_FILE_SH_UNLOCK_NAME));
                try {
                    unlocker.unlock(null);
                } catch (UnlockerStub.UnlockException ue) {
                    ue.printStackTrace();
                    displayToast("An error occurs during unlock execution: " + ue.getMessage());
                }
            }
        }

    }

    /**
     * Disable the lock which made the screen always on
     */
    protected void disableForceScreenOn(){
        WakelockManager.instance.releaseWakelock();
        unregisterScreenOffReceiver();
    }

    /**
     * Checks if the task has been canceled or must finish.
     * In this case the task will release the screen-always--on-lock
     * @return boolean - True if the AsyncTask has been cancelled, false otherwise
     */
    protected boolean checkIfCancelled(){
        if ( isCancelled() || getStatus() == Status.FINISHED ){
            disableForceScreenOn();
            return true;
        }
        return false;
    }

    /**
     * Registers a broadcast receiver which is triggered when a screen-off event is broadcast
     */
    protected void registerScreenOffReceiver(){
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(BRScreenOff.SCREEN_OFF_ACTION);
        if ( mBrScreenOff != null && mIsBrScreenOffRegistered ){
            try {
                mContext.unregisterReceiver(mBrScreenOff);
                mIsBrScreenOffRegistered = false;
            } catch ( IllegalArgumentException iae ){
                iae.printStackTrace();
            }
        }
        mBrScreenOff = new BRScreenOff();
        mContext.registerReceiver(mBrScreenOff, screenStateFilter);
        mIsBrScreenOffRegistered = true;
    }

    /**
     * Unregisters the broadcast receiver which is triggered when a screen-off event is broadcast
     */
    protected void unregisterScreenOffReceiver(){
        if ( mBrScreenOff != null && mIsBrScreenOffRegistered ){
            try {
                mContext.unregisterReceiver(mBrScreenOff);
                mIsBrScreenOffRegistered = false;
            } catch ( IllegalArgumentException iae ){
                iae.printStackTrace();
            }
        }
    }

    /**
     * Displays a toast with a dedicated message
     * @param m - The message to display in a toast
     */
    protected void displayToast( final String m ){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, m, Toast.LENGTH_LONG).show();
            }
        });
    }

}
