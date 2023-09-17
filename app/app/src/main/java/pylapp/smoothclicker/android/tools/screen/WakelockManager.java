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

import android.content.Context;
import android.os.PowerManager;

import pylapp.smoothclicker.android.tools.Logger;

/**
 * Class which manages a wakelock to prevent the device to go to sleep and have its screen off.
 * Base on a singleton design pattern.
 *
 * Use:
        <pre>
                <uses-permission android:name="android.permission.WAKE_LOCK" />
        </pre>
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 30/05/2016
 */
public class WakelockManager {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The wakelock in use
     */
    private PowerManager.WakeLock mWakeLock;

    /**
     * The context in use to retrieve the system service to get the PowerManager
     */
    private Context mContext;

    /**
     * The singleton
     */
    public static WakelockManager instance = new WakelockManager();


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The wakelock to use: this ensure the screen is on at full brightness, the keyboard backlight will be allowed to go off
     */
    private static final int WAKE_LOCK_TYPE = PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                                            | PowerManager.ACQUIRE_CAUSES_WAKEUP; // PowerManager.FULL_WAKE_LOCK

    /**
     * The tag associated to the wake lock in use
     */
    public static final String LOCK_TAG = "pylapp.smoothclicker.android.tools.screen.WakelockManager.WAKE_LOCK";

    private static final String LOG_TAG = WakelockManager.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     *
     */
    private WakelockManager(){
        super();
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     * Refreshes the context used to get the PowerManager and the wake lock
     * @param c - The new context to use, must be defined
     */
    public void refreshContext( Context c ){
        if ( c == null ) throw new IllegalArgumentException("The context must be defined");
        mContext = c;
    }

    /**
     * Returns if a wakelock has been acquired / defined
     * @return boolean - True if wakelock acquired, false otherwise
     */
    public boolean isWakeLockAcquired(){
        boolean isAcquired = mWakeLock != null && mWakeLock.isHeld();
        Logger.d(LOG_TAG, "Wakelock acquired: "+isAcquired);
        return isAcquired;
    }

    /**
     * Acquire a wakelock.
     * If the lock was null, creates a new one and acquired it.
     * If it was not null and held, release it and create a new one.
     * If it was not null and not held, creates a new one
     * @throws IllegalStateException - If no context has been defined
     */
    public void acquireWakelock() throws IllegalStateException {

        Logger.d(LOG_TAG, "Acquire wakelock...");
        if ( mContext == null ) throw new IllegalStateException("No context is defined! Use previously refreshContext(Context).");

        // Case #1 : No wake lock defined
        if ( mWakeLock == null ){
            PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(WAKE_LOCK_TYPE, LOCK_TAG);
            mWakeLock.acquire();
            return;
        }

        // Case #2 : already a non-null and an held wakelock
        if ( mWakeLock.isHeld() ){
            mWakeLock.release();
        }
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(WAKE_LOCK_TYPE, LOCK_TAG);
        mWakeLock.acquire();

    }

    /**
     * Releases the held wake lock and nullify it
     */
    public void releaseWakelock(){
        Logger.d(LOG_TAG, "Release wakelock...");
        if ( mWakeLock == null ) return;
        if ( mWakeLock.isHeld() ) mWakeLock.release();
        mWakeLock = null;
    }

    /**
     * Switches the state of the wakelock.
     * If a wakelock exists: it will be released (if needed) and nullified.
     * If a wakelock does not exist: it will be created and acquired
     * @throws IllegalStateException - If no context has been defined
     */
    public void switchWakelockState() throws IllegalStateException {

        if ( mContext == null ) throw new IllegalStateException("No context is defined! Use previously refreshContext(Context).");

        // Case #1 : A wakelock exists and can be acquired
        if ( mWakeLock != null ){
            Logger.d(LOG_TAG, "Switch state of wakelock: nullify it...");
            if ( mWakeLock.isHeld() ) mWakeLock.release();
            mWakeLock = null;
            return;
        }

        // Case #2 : A wakelock is null, so get it
        Logger.d(LOG_TAG, "Switch state of wakelock: acquire it...");
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(WAKE_LOCK_TYPE, LOCK_TAG);
        mWakeLock.acquire();

    }

    /**
     * Clears the singleton be deleting all its variables (context and wakelocks)
     */
    public void clear(){
        releaseWakelock();
        mContext = null;
    }

}
