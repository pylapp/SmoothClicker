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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import pylapp.smoothclicker.android.clickers.ATClicker;
import pylapp.smoothclicker.android.notifiers.NotificationsManager;
import pylapp.smoothclicker.android.tools.Logger;
import pylapp.smoothclicker.android.utils.Config;
import pylapp.smoothclicker.android.views.PointsListAdapter;
import pylapp.smoothclicker.android.views.SettingsActivity;


/**
 * Async task which has to check periodically the screen of the device with screen shots.
 * If the just-made-screenshot is the same has a picture in a base, the async task stops itself after
 * having triggered a callback which say the screen matches the picture.
 *
 *
 *  Command line to make a screenshot (in adb shell):
    <pre>
        /system/bin/screencap -p /storage/emulated/legacy/Smooth_Clicker/sc_trigger.png
    </pre>
 * Command line to push a picture file:
    <pre>
        adb push sc_trigger.png /storage/emulated/legacy/Smooth_Clicker/
    </pre>
 *
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.1.0
 * @since 13/06/2016
 * @see AsyncTaskForScreen
 */
public class ATScreenWatcher extends AsyncTaskForScreen<List<PointsListAdapter.Point>, Void, Void> {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The singleton of this class
     */
    private static ATScreenWatcher sInstance; // FIXME Dirty, heavy...


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The default frequency to make the screen captures
     */
    private static final int DEFAULT_FREQUENCY_CHECK_MS = 3000;

    private static final String LOG_TAG = ATScreenWatcher.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Constructor
     * @param parent - The activity which possesses the context to use
     */
    private ATScreenWatcher(Activity parent){
        super(parent);
    }


    /* ********************** *
     * METHODS FROM AsyncTask *
     * ********************** */

    /**
     *
     * @param params -
     * @return Void -
     */
    @Override
    protected Void doInBackground( List<PointsListAdapter.Point>... params ){

        // Get the points for the click process
        if ( params == null || params.length <= 0 ){
            throw new IllegalArgumentException("No points to click on!");
        }

        NotificationsManager.getInstance(mContext).makeClicksOnGoingNotificationStandalone();
        final List<PointsListAdapter.Point> points = params[0];

        forceScreenState();

        while ( true ){ // FIXME Hazardous

            // Remove the previous file if needed
            cleanTempFile();

            Logger.d(LOG_TAG, "New turn in the loop");
            if ( checkIfCancelled() ) return null;

            // Prepare the screen
            forceScreenOnIfNeeded();
            forceScreenUnlockIfNeeded();

            // Make the screen shot
            Logger.d(LOG_TAG, "Get screen shot");
            captureScreenShot();

            // Check the pictures: if there is a match, start the click process
            if (isScreenMatched()) {

                Logger.d(LOG_TAG, "Pictures match!");
                // Start the click process
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Logger.d(LOG_TAG, "Run click process");
                        ATClicker.getInstance(mContext).execute(points);
                    }
                });

                // Exit the loop / stop self
                disableForceScreenOn();
                cancel(true);
                return null;

            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            int frequency = sp.getInt(SettingsActivity.PREF_KEY_CAPTURE_FREQUENCY, DEFAULT_FREQUENCY_CHECK_MS);
            // Wait before the new check
            try {
                Thread.sleep(1000 * frequency);
            } catch ( InterruptedException ie ){ ie.printStackTrace(); }

        } // End of while (true)

    }

    /**
     * Checks if the task has been canceled or must finish.
     * In this case the task will stop all notifications and notify to the user it is finished.
     * @return boolean - True if the AsyncTask has been cancelled, false otherwise
     */
    @Override
    protected boolean checkIfCancelled(){
        boolean isCancelled = super.checkIfCancelled();
        if ( isCancelled ){
            NotificationsManager.getInstance(mContext).stopAllNotifications();
            NotificationsManager.getInstance(mContext).makeWatchProcessStoppedNotification();
        }
        return isCancelled;
    }

    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * @param parent - The parent activity which possesses the context to use to retrieve data from Shared Preferences
     * @return ATClicker - The singleton
     */
    public static ATScreenWatcher getInstance( Activity parent ){
        if ( sInstance == null ) sInstance = new ATScreenWatcher(parent);
        return sInstance;
    }

    /**
     * Stops the AsyncTask
     * @return boolean - True if the process was working, false otherwise
     */
    public static boolean stop(){
        Logger.d(LOG_TAG, "Stops the screen-watcher process");
        if ( sInstance == null ){
            Logger.d(LOG_TAG, "The ATScreenWatcher is null");
            return false;
        }
        if ( ! sInstance.isCancelled() ){
            sInstance.cancel(true);
            sInstance.disableForceScreenOn();
            sInstance = null;
            return true;
        } else{
            Logger.w(LOG_TAG, "The ATScreenWatcher has been canceled previously");
            sInstance = null;
            return false;
        }
    }

    /**
     * Captures the screen to make a new screen shot
     *
     * @return boolean - True if the capture is made, false otherwise
     */
    private boolean captureScreenShot(){
        try {
            Process shell = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(shell.getOutputStream());
            String shellCmd = "/system/bin/screencap -p "+ Config.getAppFolder().getAbsolutePath()+"/"+Config.FILE_CAPTURE_PICTURE;
            Logger.d(LOG_TAG, "This system command will be executed: " + shellCmd);
            dos.writeBytes(shellCmd);
            dos.flush();
            dos.close();
            shell.waitFor();
        } catch ( IOException ie ){
            ie.printStackTrace();
            displayToast(ie.getMessage());
            return false;
        } catch ( InterruptedException ie ){
            ie.printStackTrace();
        }
        return true;
    }

    /**
     * Checks if the capture made matches the base capture
     * @return boolean- True if the screen matches the base picture, false otherwise
     */
    private boolean isScreenMatched(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        String fileName = sp.getString(SettingsActivity.PREF_KEY_FILE_TRIGGER_NAME, Config.DEFAULT_FILE_TRIGGER_PICTURE);

        // Get the base file
        Bitmap baseFile = BitmapFactory.decodeFile(Config.getAppFolder().getAbsolutePath()+"/"+fileName);
        if ( baseFile == null ){
            Logger.e(LOG_TAG, "Base file is null");
            return false;
        }

        // Get the new screen shot
        Bitmap screenShotFile = BitmapFactory.decodeFile(Config.getAppFolder().getAbsolutePath()+"/"+Config.FILE_CAPTURE_PICTURE);
        if ( screenShotFile == null ){
            Logger.e(LOG_TAG, "Capture file is null");
            return false;
        }

        // Make the match
        int threshold = sp.getInt(SettingsActivity.PREF_KEY_PRTHRESHOLD, 10);
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        try {
            return pc.comparePictures( baseFile, screenShotFile, threshold);
        } catch (PicturesComparator.PicturesComparatorException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Cleans the files created by the task: removes them.
     * @return boolean - The result of the file's deletion process
     */
    public static boolean cleanTempFile(){
        File fileToClean = new File(Config.getAppFolder().getAbsolutePath()+"/"+Config.FILE_CAPTURE_PICTURE);
        return fileToClean.delete();
    }

}
