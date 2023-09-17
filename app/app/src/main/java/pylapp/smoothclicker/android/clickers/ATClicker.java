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

package pylapp.smoothclicker.android.clickers;

import android.app.Activity;
import android.content.SharedPreferences;

import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.notifiers.NotificationsManager;
import pylapp.smoothclicker.android.tools.config.ConfigImporter;
import pylapp.smoothclicker.android.tools.screen.AsyncTaskForScreen;
import pylapp.smoothclicker.android.utils.Config;
import pylapp.smoothclicker.android.tools.Logger;
import pylapp.smoothclicker.android.views.PointsListAdapter;
import pylapp.smoothclicker.android.views.StandaloneActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Async Task which consists on executing the click task
 *
 * @author Pierre-Yves Lapersonne
 * @version 4.0.0
 * @since 02/03/2016
 * @see android.os.AsyncTask
 */
// FIXME Use the Observer/Observable design pattern with NotificationsManager as Observer and ATClicker as observable
public class ATClicker extends AsyncTaskForScreen<List<PointsListAdapter.Point>, Void, Void > {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * Represents an external process. Enables writing to, reading from, destroying,
     * and waiting for it, as well as querying its exit value.
     * It is sued to get a Super User access.
     */
    private Process mProcess;

    /**
     * The output stream of the {@link Process} object
     */
    private DataOutputStream mOutputStream;

    /**
     * The list of points to click on
     */
    private List<PointsListAdapter.Point> mPoints;
    /**
     * The unit time in use
     */
    private ConfigImporter.UnitTime mUnitTime;
    /**
     * The type of start
     */
    private boolean mIsStartDelayed;
    /**
     * The delay of the start
     */
    private int mDelay;
    /**
     * The time to wait between each click
     */
    private int mTimeGap;
    /**
     * The amount of repeat to do
     */
    private int mRepeat;
    /**
     * If the repeat is endless
     */
    private boolean mIsRepeatEndless;

    /**
     * Flag to rise to true if in standalone mode, or to put to false if in app mode
     */
    private boolean mIsStandalone;

    /**
     * The singleton of this class
     */
    private static ATClicker sInstance; // FIXME Dirty, heavy...


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * A token the command line to execute the click should display so as to free the async task and let it make notifications
     */
    private static final String KEYWORD_SHELL_CLICK_DONE = "SMOOTHCLICKER_CLICK_DONE";

    private static final String LOG_TAG = ATClicker.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Constructor
     * @param parent - The activity which possesses the context to use to get the SharedPreferences to get the configuration
     */
    private ATClicker( Activity parent ){
        super(parent);
        mIsStandalone = ( parent instanceof StandaloneActivity);
    }


    /* ********************** *
     * METHODS FROM AsyncTask *
     * ********************** */

    /**
     *
     */
    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        SharedPreferences sp = mContext.getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, Config.SP_ACCESS_MODE);
        int unitTime = sp.getInt(Config.SP_KEY_UNIT_TIME, Config.DEFAULT_TIME_UNIT_SELECTION);
        switch ( unitTime ){
            case R.id.rbUnitTimeMs:
                mUnitTime = ConfigImporter.UnitTime.MILLISECOND;
                break;
            case R.id.rbUnitTimeS:
                mUnitTime = ConfigImporter.UnitTime.SECOND;
                break;
            case R.id.rbUnitTimeM:
                mUnitTime = ConfigImporter.UnitTime.MINUTE;
                break;
            case R.id.rbUnitTimeH:
                mUnitTime = ConfigImporter.UnitTime.HOUR;
                break;
            default:
                mUnitTime = ConfigImporter.UnitTime.SECOND;
                break;
        }
        mIsStartDelayed = sp.getBoolean(Config.SP_KEY_START_TYPE_DELAYED, Config.DEFAULT_START_DELAYED);
        mDelay = sp.getInt(Config.SP_KEY_DELAY, Integer.parseInt(Config.DEFAULT_DELAY));
        if ( mDelay < 0 ){
            throw new IllegalArgumentException("The delay cannot be < 0!");
        }
        mTimeGap = sp.getInt(Config.SP_KEY_TIME_GAP, Integer.parseInt(Config.DEFAULT_TIME_GAP));
        if ( mTimeGap < 0 ){
            throw new IllegalArgumentException("The time gap between clicks cannot be < 0!");
        }
        mRepeat = sp.getInt(Config.SP_KEY_REPEAT, Integer.parseInt(Config.DEFAULT_REPEAT));
        if ( mRepeat < 0 ) {
            throw new IllegalArgumentException("The repeat amount cannot be < 0!");
        }
        mIsRepeatEndless = sp.getBoolean(Config.SP_KEY_REPEAT_ENDLESS, Config.DEFAULT_REPEAT_ENDLESS);
        NotificationsManager.getInstance(mContext).stopAllNotifications();
        NotificationsManager.getInstance(mContext).makeStartNotification();

    }

    /**
     *
     * @param params - Nothing
     * @return Void - Nothing
     */
    @Override
    protected Void doInBackground( List<PointsListAdapter.Point>... params ){ // FIXME Too heavy

        if ( checkIfCancelled() ) return null;
        if ( params == null || params.length <= 0 ){
            throw new IllegalArgumentException("No points to click on!");
        }

        // Prepare the device for the click process: screen on and keep it on, unlock it
        forceScreenState();

        mPoints = params[0];

        /*
         * Step 1 : Get the process as "su"
         */
        try {
            Logger.d(LOG_TAG, "Get 'su' process...");
            mProcess = Runtime.getRuntime().exec("su");
        } catch ( IOException e ){
            Logger.e(LOG_TAG, "Exception thrown during 'su' : " + e.getMessage());
            e.printStackTrace();
            disableForceScreenOn();
            displayToast("An error occurs during super-user process retrieve: "+e.getMessage());
            displayToast(mContext.getString(R.string.error_su_missing));
            return null;
        }

        if ( checkIfCancelled() ) return null;

        /*
         * Step 2 : Get the process output stream
         */
        Logger.d(LOG_TAG, "Get 'su' process data output stream...");
        mOutputStream = new DataOutputStream(mProcess.getOutputStream());

        if ( checkIfCancelled() ) return null;

        /*
         * Step 3 : Execute the command, the same we can execute from ADB within a terminal and deal with the configuration
             $ adb devices
             > ...
             $ adb shell
                $ input tap XXX YYY
         */
        logConfigurationState();

        forceScreenState();

        // Should we delay the execution ?
        if ( mIsStartDelayed ){
            Logger.d(LOG_TAG, "The start is delayed, will sleep : "+mDelay);
            int factor = 0;
            switch ( mUnitTime ){
                case MILLISECOND:
                    factor = 1; // milliseconds
                    break;
                case SECOND:
                    factor = 1; // seconds
                    break;
                case MINUTE:
                    factor = 60; // seconds
                    break;
                case HOUR:
                    factor = 3600; // seconds
                    break;
            }
            final long count = mDelay * factor;
            final int threadSleepTime = ( mUnitTime == ConfigImporter.UnitTime.MILLISECOND ? 1 : 1000 );
            // Loop for each unit time
            for ( int i = 1; i <= count; i++ ){
                try {
                    if (checkIfCancelled()) return null;
                    NotificationsManager.getInstance(mContext).makeCountDownNotification(count - i);
                    Thread.sleep(threadSleepTime);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            NotificationsManager.getInstance(mContext).stopAllNotifications();
        } // End of  if ( mIsStartDelayed )

        if ( mIsStandalone ){
            NotificationsManager.getInstance(mContext).stopClicksOnGoingNotification();
            NotificationsManager.getInstance(mContext).makeClicksOnGoingNotificationStandalone();
        } else {
            NotificationsManager.getInstance(mContext).makeClicksOnGoingNotificationByApp();
        }

        /*
         * Is the execution endless ?
         */
        if (mIsRepeatEndless) {

            while (true) {
                if (checkIfCancelled()) return null;
                Logger.d(LOG_TAG, "Should repeat the process ENDLESSLY");
                forceScreenState();
                executeTap();
                waitIfNeeded();
            }

        /*
         * Should we repeat the execution ?
         */
        } else if ( mRepeat > 1 ){

            Logger.d(LOG_TAG, "Should repeat the process : " + mRepeat);
            for ( int i = 0; i < mRepeat; i++ ){
                if ( checkIfCancelled() ) return null;
                forceScreenState();
                executeTap();
                waitIfNeeded();
            }

        /*
         * Just one execution
         */
        } else {
            if ( checkIfCancelled() ) return null;
            Logger.d(LOG_TAG, "Should NOT repeat the process : "+mRepeat);
            forceScreenState();
            executeTap();
        }

        NotificationsManager.getInstance(mContext).stopAllNotifications();
        NotificationsManager.getInstance(mContext).makeClicksOverNotification();
        disableForceScreenOn();
        Logger.d(LOG_TAG, "The input event seems to be triggered");

        return null;

    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Checks if the task has been canceled or must finish.
     * In this case the task will stop all notifications and notify to the user it is finished
     * @return boolean - True if the AsyncTask has been cancelled, false otherwise
     */
    @Override
    protected boolean checkIfCancelled(){
        boolean isCancelled = super.checkIfCancelled();
        if ( isCancelled ){
            NotificationsManager.getInstance(mContext).stopAllNotifications();
            NotificationsManager.getInstance(mContext).makeClicksStoppedNotification();
        }
        return isCancelled;
    }

    /**
     * @param parent - The parent activity which possesses the context to sue to retrieve data from Shared Preferences
     * @return ATClicker - The singleton
     */
    public static ATClicker getInstance( Activity parent ){
        if ( sInstance == null ) sInstance = new ATClicker( parent );
        return sInstance;
    }

    /**
     * Stops the AsyncTask
     * @return boolean - True if the process was working, false otherwise
     */
    public static boolean stop(){
        Logger.d(LOG_TAG, "Stops the clicking process");
        if ( sInstance == null ){
            Logger.w(LOG_TAG, "The ATClicker is null");
            return false;
        }
        if ( ! sInstance.isCancelled() ){
            sInstance.cancel(true);
            sInstance.disableForceScreenOn();
            sInstance = null;
            return true;
        } else{
            Logger.w(LOG_TAG, "The ATClicker has been canceled previously");
            sInstance = null;
            return false;
        }
    }

    /**
     * Executes the tap action
     */
    private void executeTap(){

        final int NUMBER_OF_POINTS = mPoints.size();

        for ( int i = 0; i < NUMBER_OF_POINTS; i++ ){

            PointsListAdapter.Point p = mPoints.get(i);

            if ( ! p.isUsable) continue;

            int x = p.x;
            int y = p.y;

            String shellCmd = "/system/bin/input tap " + x + " " + y + " && echo \""+KEYWORD_SHELL_CLICK_DONE+"\" \n";
            Logger.d(LOG_TAG, "The system command will be executed : " + shellCmd);
            try {
                if ( mProcess == null || mOutputStream == null ) throw new IllegalStateException("The process or its stream is not defined !");
                mOutputStream.writeBytes(shellCmd);
                InputStream inputStream = mProcess.getInputStream();
                byte[] buffer = new byte[1024];
                while ( true ){ // FIXME May be hazardous
                    int read = inputStream.read(buffer);
                    String out = new String(buffer, 0, read);
                    if (out.contains(KEYWORD_SHELL_CLICK_DONE)) break;
                }
                NotificationsManager.getInstance(mContext).makeNewClickNotifications(x, y);
            } catch ( IOException ioe ){
                Logger.e(LOG_TAG, "Exception thrown during tap execution : " + ioe.getMessage());
                ioe.printStackTrace();
                displayToast("An error occurs during tap execution: " + ioe.getMessage());
                // Retry to get the SU process (this operation may fail)
                try {
                    mProcess = Runtime.getRuntime().exec("su");
                } catch ( IOException ioe2 ){
                    ioe2.printStackTrace();
                    Logger.e(LOG_TAG, "Exception thrown during tap execution (get again SU): " + ioe2.getMessage());
                }
            }

            // Wait before the next point to click if there is some points to click
            // We deal with the TIME GAP option
            if ( i < NUMBER_OF_POINTS - 1 ) {
                waitIfNeeded();
            }

        } // End of for ( PointsListAdapter.Point p : mPoints )

    }

    /**
     * Waits according to the time gap, the unit time and, the unit time factor etc if needed
     */
    private void waitIfNeeded(){
        if ( mTimeGap > 0 ){
            Logger.d(LOG_TAG, "Should wait before each process occurrences : " + mTimeGap);
            switch ( mUnitTime ){
                case MILLISECOND:
                    for ( int k = 1; k <= mTimeGap; k++ ){
                        try {
                            if ( checkIfCancelled() ) return;
                            Thread.sleep( 1 );
                        } catch ( InterruptedException ie ){ie.printStackTrace();}
                    }
                    break;
                case SECOND:
                    for ( int k = 1; k <= mTimeGap; k++ ){
                        try {
                            if ( checkIfCancelled() ) return;
                            Thread.sleep( 1000 ); // 1 second * 1000 milliseconds = 1000 ms
                        } catch ( InterruptedException ie ){ie.printStackTrace();}
                    }
                    break;
                case MINUTE:
                    for ( int k = 1; k <= mTimeGap*60; k++ ){ // Higher frequency
                        try {
                            if ( checkIfCancelled() ) return;
                            Thread.sleep( 1000 ); // If the task is canceled, prevents from check if stopped each minute, better than Thread.sleep( 1 * 60 * 1000); // 1 m * 60 s * 1000 ms
                        } catch ( InterruptedException ie ){ie.printStackTrace();}
                    }
                    break;
                case HOUR:
                    for ( int k = 1; k <= mTimeGap*60*60; k++ ){ // Higher frequency
                        try {
                            if ( checkIfCancelled() ) return;
                            Thread.sleep( 1000 ); // If the task is canceled, prevents from check if stopped each hour, better than Thread.sleep( 1 * 60 * 60 * 1000); // 1 h * 60 m * 60 s * 1000 ms
                        } catch ( InterruptedException ie ){ie.printStackTrace();}
                    }
                    break;
            } // End of  if (mTimeGap > 0)
        } else {
            Logger.d(LOG_TAG, "Should NOT wait before each process occurrences : " + mTimeGap);
        }
    }

    /**
     * Logs the configuration in use
     */
    private void logConfigurationState(){

        StringBuilder sb = new StringBuilder();
        sb.append("*****************************************\n");
        sb.append("ATClicker Clicking process with config: \n");
        sb.append("*****************************************\n");
        sb.append("\t delayed start......: ").append(mIsStartDelayed).append("\n");
        sb.append("\t delay..............: ").append(mDelay).append("\n");
        sb.append("\t time gap...........: ").append(mTimeGap).append("\n");
        sb.append("\t endless repeat.....: ").append(mIsRepeatEndless).append("\n");
        sb.append("\t repeat.............: ").append(mRepeat).append("\n");
        sb.append("\t standalone.........: ").append(mIsStandalone).append("\n");
        sb.append("\t point: \n");
        for ( PointsListAdapter.Point p : mPoints ){
            if ( p.x != PointsListAdapter.Point.UNDEFINED_X
                    && p.y != PointsListAdapter.Point.UNDEFINED_Y ) {
                sb.append("\t\t x=").append(p.x).append(" - y=").append(p.y).append("\n");
            }
        }
        sb.append("*****************************************\n");

        Logger.i(LOG_TAG, sb.toString());

    }

}
