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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import pylapp.smoothclicker.android.notifiers.StatusBarNotifier;
import pylapp.smoothclicker.android.notifiers.VibrationNotifier;
import pylapp.smoothclicker.android.tools.Logger;
import pylapp.smoothclicker.android.utils.Config;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Service to call our to start from the outside (e.g. a third party app) so as to trigger the clicking process
 * without using the GUI and its dedicated Activity instances.
 *
     <h3>How to start (and use) the ServiceClicker ?</h3>
     <pre>

         // Create the Intent with the good action
         Intent intentServiceSmoothClicker = new Intent("pylapp.smoothclicker.android.clickers.ServiceClicker.START");

         // Defines the configuration to use
         intentServiceSmoothClicker.putExtra("0x000011", true); // Start delayed ?
         intentServiceSmoothClicker.putExtra("0x000012", 10);   // How much delay for the start ?
         intentServiceSmoothClicker.putExtra("0x000013", 2);    // The amount of time to wait between clicks
         intentServiceSmoothClicker.putExtra("0x000021", 5);    // The number of repeat to do
         intentServiceSmoothClicker.putExtra("0x000022", false);// Endless repeat ?
         intentServiceSmoothClicker.putExtra("0x000031", false);// Vibrate on start ?
         intentServiceSmoothClicker.putExtra("0x000032", true);// Vibrate on each click ?
         intentServiceSmoothClicker.putExtra("0x000041", true);// Make notifications ?
         ArrayList<Integer> points = new ArrayList<Integer>();
         points.add(252); // x0
         points.add(674); // y0
         points.add(266); // x1
         points.add(930); // y1
         points.add(597); // x2
         points.add(936); // y2
         intentServiceSmoothClicker.putIntegerArrayListExtra("0x000051",points); // The list of points

         // Starts the service
         startService(intentServiceSmoothClicker);

     </pre>
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.2.0
 * @since 18/03/2016
 * @see IntentService
 * @see ATClicker
 */
@Deprecated
public class ServiceClicker extends IntentService {


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
     * The application context
     */
    private Context mContext;

    /**
     * The list of points to click on as {x0, y0, x1, y1, ..., xN, yN}
     */
    private ArrayList<Integer> mPoints;
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
     * Vibrate on start
     */
    private boolean mVibrateOnStart;
    /**
     * Vibrate on click
     */
    private boolean mVibrateOnClick;
    /**
     * Display notifications on click
     */
    private boolean mNotif;

    /**
     * Flag to rise to stop the service when possible
     */
    private boolean mIsShouldStop;


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * Action to awake the service (i.e. from the broadcast receiver listening broadcasts of BOOT, etc.)
     */
    public static final String SERVICE_CLICKER_INTENT_FILTER_NAME_WAKEUP = "pylapp.smoothclicker.android.clickers.ServiceClicker.WAKEUP";
    /**
     * Action to start the service
     */
    public static final String SERVICE_CLICKER_INTENT_FILTER_NAME_START = "pylapp.smoothclicker.android.clickers.ServiceClicker.START";
    /**
     * Action to stop the service
     */
    public static final String SERVICE_CLICKER_INTENT_FILTER_NAME_STOP = "pylapp.smoothclicker.android.clickers.ServiceClicker.STOP";

    // Should be equal as R.string.service_label_serviceclicker
    private static final String SERVICE_LABEL_SERVICECLICKER = "Service Clicker of Smooth Clicker";

    /**
     * The key to use to store the delayed start (boolean value) in a bundle
     */
    public static final String BUNDLE_KEY_DELAYED_START = "0x000011";
    /**
     * The key to use to store the delay (integer value) in a bundle
     */
    public static final String BUNDLE_KEY_DELAY = "0x000012";
    /**
     * The key to use to store the time gap to wait between clicks (integer value) in a bundle
     */
    public static final String BUNDLE_KEY_TIME_GAP = "0x000013";
    /**
     * The key to use to store the amount of repetition (integer value) in a bundle
     */
    public static final String BUNDLE_KEY_REPEAT = "0x000021";
    /**
     * The key to use to store the repeat endless (boolean value) in a bundle
     */
    public static final String BUNDLE_KEY_REPEAT_ENDLESS = "0x000022";
    /**
     * The key to use to store the vibrate on start (boolean value) in a bundle
     */
    public static final String BUNDLE_KEY_VIBRATE_ON_START = "0x000031";
    /**
     * The key to use to store the vibrate on each click (boolean value) in a bundle
     */
    public static final String BUNDLE_KEY_VIBRATE_ON_CLICK = "0x000032";
    /**
     * The key to use to store the notification (boolean value) in a bundle
     */
    public static final String BUNDLE_KEY_NOTIFICATIONS = "0x000041";
    /**
     * The key to use to store the points to click on (ArrayList of Integers value) in a bundle
     */
    public static final String BUNDLE_KEY_POINTS = "0x000051";

    /**
     * The key for the status of the service sent within a broadcast
     */
    public static final String BROADCAST_KEY_STATUS = "pylapp.smoothclicker.android.clickers.ServiceClicker.STATUS";

    private static final String BROADCAST_ACTION = "pylapp.smoothclicker.android.clickers.ServiceClicker.BROADCAST";


    private static final String LOG_TAG = ServiceClicker.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     */
    public ServiceClicker() {
        super(SERVICE_LABEL_SERVICECLICKER);
    }


    /* ************************** *
     * METHODS FROM IntentService *
     * ************************** */

    /**
     * Triggered when somebody wants to bind to the service.
     * It checks the action the service received : wake up, stop or start and then does nothing, stops or continues.
     * After that it gets from the intent the configuration, and fills it with default values if needed ni the SharedPreferences.
     * Then, it triggers the clicking task.
     *
     * @param intent -
     */
    @Override
    protected void onHandleIntent( Intent intent ){

        if ( intent == null || intent.getAction() == null ){
            broadcastStatus(StatusTypes.BAD_CONFIG);
            return;
        }

        Logger.d(LOG_TAG, "Starts of the ServiceClicker");

        /*
         * Step 1 : Get the intent : should we start or stop ?
         */
        final String action = intent.getAction();

         /*
         * Step 2 : Broadcast the status
         */
        switch ( action ){
            case SERVICE_CLICKER_INTENT_FILTER_NAME_WAKEUP:
                broadcastStatus(StatusTypes.AWAKE);
                return;
            case SERVICE_CLICKER_INTENT_FILTER_NAME_STOP:
                broadcastStatus(StatusTypes.TERMINATED);
                mIsShouldStop = true;
                stopSelf();
                break;
            case SERVICE_CLICKER_INTENT_FILTER_NAME_START:
                broadcastStatus(StatusTypes.STARTED);
                break;
            default:
                broadcastStatus(StatusTypes.BAD_CONFIG);
                stopSelf();
                break;
        }

        mContext = this;

        /*
         * Step 3a : Saves the config
         */
        mIsStartDelayed = intent.getBooleanExtra(BUNDLE_KEY_DELAYED_START, Config.DEFAULT_START_DELAYED);
        mDelay = intent.getIntExtra(BUNDLE_KEY_DELAY, Integer.parseInt(Config.DEFAULT_DELAY));
        if ( mDelay < 0 ){
            broadcastStatus(StatusTypes.BAD_CONFIG);
            throw new IllegalArgumentException("The delay cannot be < 0!");
        }
        mTimeGap = intent.getIntExtra(BUNDLE_KEY_TIME_GAP, Integer.parseInt(Config.DEFAULT_TIME_GAP));
        if ( mTimeGap < 0 ){
            broadcastStatus(StatusTypes.BAD_CONFIG);
            throw new IllegalArgumentException("The time gap between clicks cannot be < 0!");
        }
        mRepeat = intent.getIntExtra(BUNDLE_KEY_REPEAT, Integer.parseInt(Config.DEFAULT_REPEAT));
        if ( mRepeat < 0 ){
            broadcastStatus(StatusTypes.BAD_CONFIG);
            throw new IllegalArgumentException("The repeat amount cannot be < 0!");
        }
        mIsRepeatEndless = intent.getBooleanExtra(BUNDLE_KEY_REPEAT_ENDLESS, Config.DEFAULT_REPEAT_ENDLESS);
        mVibrateOnStart = intent.getBooleanExtra(BUNDLE_KEY_VIBRATE_ON_START, Config.DEFAULT_VIBRATE_ON_START);
        mVibrateOnClick = intent.getBooleanExtra(BUNDLE_KEY_VIBRATE_ON_CLICK, Config.DEFAULT_VIBRATE_ON_CLICK);
        mNotif = intent.getBooleanExtra(BUNDLE_KEY_NOTIFICATIONS, Config.DEFAULT_NOTIF_ON_CLICK);
        mPoints = intent.getIntegerArrayListExtra(BUNDLE_KEY_POINTS);

        if ( mPoints == null || mPoints.size() <= 0 ){
            broadcastStatus(StatusTypes.BAD_CONFIG);
            throw new IllegalArgumentException("No points to click on!");
        }

        // FIXME Wait for more Java8 support on Android and use lambads and map/reduce/filter pattern !
        for (Integer p : mPoints) if ( p < 0 ){
            broadcastStatus(StatusTypes.BAD_CONFIG);
            throw new IllegalArgumentException("A point cannot have a negative coordinate !");
        }

        /*
         * Step 3b : Broadcast the status
         */
        broadcastStatus(StatusTypes.WORKING);

        /*
         * Step 4 : Starts the clicking process
         */
        makeStartNotification();
        executeTaps();

        /*
         * Step 5 : Broadcast the status when the process is done
         */
        broadcastStatus(StatusTypes.TERMINATED);

        /*
         * Step 6 : Finish !
         */
        // ~=[,,_,,]:3

    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Execute the clicks with the same way as for the ATClicker
     */
    // FIXME Find a way to factorise the sources, design patterns !
    private void executeTaps(){

        if ( checkIfCancelled() ) return;

        /*
         * Step 1 : Get the process as "su"
         */
        try {
            Logger.d(LOG_TAG, "Get 'su' process...");
            mProcess = Runtime.getRuntime().exec("su");
        } catch ( IOException e ){
            Logger.e(LOG_TAG, "Exception thrown during 'su' : " + e.getMessage());
            e.printStackTrace();
        }

        if ( checkIfCancelled() ) return;

        /*
         * Step 2 : Get the process output stream
         */
        Logger.d(LOG_TAG, "Get 'su' process data output stream...");
        mOutputStream = new DataOutputStream(mProcess.getOutputStream());

        if ( checkIfCancelled() ) return;

        /*
         * Step 3 : Execute the command, the same we can execute from ADB within a terminal and deal with the configuration
             $ adb devices
             > ...
             $ adb shell
                $ input tap XXX YYY
         */

        // Should we delay the execution ?
        if ( mIsStartDelayed ){
            Logger.d(LOG_TAG, "The start is delayed, will sleep : "+mDelay);
            final int count = mDelay;
            // Loop for each second
            for ( int i = 1; i <= count; i++ ){
                try {
                    if ( checkIfCancelled() ) return;
                    makeCountDownNotification(mDelay - i);
                    Thread.sleep(1000); // Sleep of 1 second
                    if ( checkIfCancelled() ) return;
                } catch ( InterruptedException ie ){ie.printStackTrace();}
            }
            stopAllNotifications();
        }

        makeClicksOnGoingNotification();

        /*
         * Is the execution endless ?
         */
        if (mIsRepeatEndless) {

            while (true) {
                Logger.d(LOG_TAG, "Should repeat the process ENDLESSLY");
                if ( checkIfCancelled() ) return;
                executeTap();
                // Should we wait before the next action ?
                if ( mTimeGap > 0 ){
                    try {
                        Logger.d(LOG_TAG, "Should wait before each process occurrences : "+mTimeGap);
                        Thread.sleep(mTimeGap*1000);
                    } catch ( InterruptedException ie ){ie.printStackTrace();}
                } else {
                    Logger.d(LOG_TAG, "Should NOT wait before each process occurrences : "+mTimeGap);
                }
            }

        /*
         * Should we repeat the execution ?
         */
        } else if ( mRepeat > 1 ){

            Logger.d(LOG_TAG, "Should repeat the process : " + mRepeat);
            for ( int i = 0; i < mRepeat; i++ ){
                if ( checkIfCancelled() ) return;
                executeTap();
                // Should we wait before the next action ?
                if ( mTimeGap > 0 ){
                    try {
                        Logger.d(LOG_TAG, "Should wait before each process occurrences : "+mTimeGap);
                        Thread.sleep(mTimeGap*1000);
                    } catch ( InterruptedException ie ){ie.printStackTrace();}
                } else {
                    Logger.d(LOG_TAG, "Should NOT wait before each process occurrences : "+mTimeGap);
                }
            }

        /*
         * Just one execution
         */
        } else {
            if ( checkIfCancelled() ) return;
            Logger.d(LOG_TAG, "Should NOT repeat the process : "+mRepeat);
            executeTap();
        }

        stopAllNotifications();
        makeClicksOverNotification();
        Logger.d(LOG_TAG, "The input event seems to be triggered");

    }

    /**
     * Executes the tap action
     */
    private void executeTap(){

        for ( int i = 0; i < mPoints.size(); i+=2 ){


            int x = mPoints.get(i);
            int y = mPoints.get(i+1);

            String shellCmd = "/system/bin/input tap " + x + " " + y + "\n";
            Logger.d(LOG_TAG, "The system command will be executed : " + shellCmd);
            try {
                if ( mProcess == null || mOutputStream == null ) throw new IllegalStateException("The process or its stream is not defined !");
                mOutputStream.writeBytes(shellCmd);
                makeNewClickNotifications(x, y);
            } catch ( IOException ioe ){
                Logger.e(LOG_TAG, "Exception thrown during tap execution : " + ioe.getMessage());
                ioe.printStackTrace();
            }

            // Should we wait before the next action ?
            if ( mTimeGap > 0 ){
                try {
                    Logger.d(LOG_TAG, "Should wait before each process occurrences : "+mTimeGap);
                    Thread.sleep(mTimeGap*1000);
                } catch ( InterruptedException ie ){ie.printStackTrace();}
            } else {
                Logger.d(LOG_TAG, "Should NOT wait before each process occurrences : "+mTimeGap);
            }

        } // End of for ( PointsListAdapter.Point p : mPoints )

    }

    /**
     * Manages the notifications about the count down for delayed starts
     * @param countDown - The leaving amount of seconds before start
     */
    private void makeCountDownNotification( int countDown ){
        if ( ! mNotif ) return;
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.COUNT_DOWN, countDown);
    }

    /**
     * Manages the notifications about the new click.
     * @param x - The x coordinate of the click
     * @param y - The y coordinate of the click
     */
    private void makeNewClickNotifications( int x, int y){
        if ( mVibrateOnClick ){
            new VibrationNotifier(mContext).vibrate(VibrationNotifier.VIBRATE_ON_CLICK_DURATION);
        }
        if ( mNotif ) {
            new StatusBarNotifier(mContext).makeNotification(StatusBarNotifier.NotificationTypes.CLICK_MADE, x, y);
        }
    }

    /**
     * Manages the notifications about the on going clicking process
     */
    private void makeClicksOnGoingNotification(){
        if ( ! mNotif ) return;
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_SERVICE);
    }

    /**
     * Manages the notifications about the clicking process which is over
     */
    private void makeClicksOverNotification(){
        if ( ! mNotif ) return;
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_OVER);
    }

    /**
     * Makes a notification about the fact the clicking process starts
     */
    private void makeStartNotification(){
        if ( mVibrateOnStart ){
            VibrationNotifier vn = new VibrationNotifier(mContext);
            vn.vibrate(VibrationNotifier.VIBRATE_ON_START_DURATION);
        }
    }

    /**
     * Stops all the notifications
     */
    private void stopAllNotifications(){
        if ( ! mNotif ) return;
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeAllNotifications();
    }

    /**
     *
     * @return boolean - True if the AsyncTask has been cancelled, false otherwise
     */
    private boolean checkIfCancelled(){
        if ( mIsShouldStop ){
            stopAllNotifications();
            makeClicksOverNotification();
            return true;
        }
        return false;
    }

    /**
     * Sends a broadcast with the status of the service
     * @param status - The status to broadcast
     */
    private void broadcastStatus( StatusTypes status ){
        Logger.d(LOG_TAG, "Status of the ServiceClicker : "+status.mCode);
        Intent i = new Intent(BROADCAST_ACTION);
        i.putExtra(BROADCAST_KEY_STATUS, status.mCode);
        sendBroadcast(i);
    }


    /* *********** *
     * INNER ENUMS *
     * *********** */

    /**
     * The list of status
     */
    public enum StatusTypes {

        /**
         * The status the service can be: it has received a bad configuration
         */
        BAD_CONFIG("0x001000"),
        /**
         * The status the service can be: it has been triggered
         */
        AWAKE("0x001001"),
        /**
         * The status the service can be: it has been triggered / started by another app
         */
        STARTED("0x001002"),
        /**
         * The status the service can be: its is working on the click process
         */
        WORKING("0x001003"),
        /**
         * The status the service can be: the click process is over
         */
        TERMINATED("0x001004");

        final String mCode;

        StatusTypes( String s ){
            mCode = s;
        }

    }

}
