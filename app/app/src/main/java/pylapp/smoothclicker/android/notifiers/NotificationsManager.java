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

package pylapp.smoothclicker.android.notifiers;

import android.content.Context;
import android.content.SharedPreferences;

import pylapp.smoothclicker.android.utils.Config;

/**
 * Utility class which manages notifications.
 * It is base on a facade design pattern for notification features.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.6.0
 * @since 16/03/2016
 */
public final class NotificationsManager {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The context to use to get the shared preferences, to get vibrations, etc.
     */
    private Context mContext;

    /**
     * If we have to vibrate on start
     */
    private boolean mVibrateOnStart;
    /**
     * If we have to vibrate on each click
     */
    private boolean mVibrateOnClick;
    /**
     * If we have to display a notification on each new click
     */
    private boolean mNotifOnClick;
    /**
     * If we have to play a sound on each new click
     */
    private boolean mRingOnClick;

    /**
     * The singleton
     */
    private static NotificationsManager sInstance;

    //private static final String LOG_TAG = NotificationsManager.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     * @param c - The context to use to get the SharedPreferences, must not be null
     * @throws IllegalArgumentException - If c is null
     */
    private NotificationsManager( Context c ){
        super();
        if ( c == null ) throw  new IllegalArgumentException("The context parameter mut not be null");
        mContext = c;
        init();
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     * Returns the singleton
     * @param c - The context to use, must not be null
     * @return NotificationsManager - The singleton, null if c is null
     */
    public static NotificationsManager getInstance( Context c ){
        if ( c == null ) return null;
        if ( sInstance == null ){
            sInstance = new NotificationsManager(c);
        }
        return sInstance;
    }

    /**
     * Makes a notification about the fact the clicking process starts
     */
    public void makeStartNotification(){
        if ( mVibrateOnStart ){
            VibrationNotifier vn = new VibrationNotifier(mContext);
            vn.vibrate(VibrationNotifier.VIBRATE_ON_START_DURATION);
        }
    }

    /**
     * Manages the notifications about the new click.
     * @param x - The x coordinate of the click
     * @param y - The y coordinate of the click
     */
    public void makeNewClickNotifications( int x, int y){

        // Vibrations?
        if ( mVibrateOnClick ){
            new VibrationNotifier(mContext).vibrate(VibrationNotifier.VIBRATE_ON_CLICK_DURATION);
        }

        // Notification in status bar?
        if ( mNotifOnClick ) {
            new StatusBarNotifier(mContext).makeNotification(StatusBarNotifier.NotificationTypes.CLICK_MADE, x, y);
        }

        // Sound notifications?
        if ( mRingOnClick ) {
            new SoundNotifier(mContext).ring();
        }

    }


    /**
     * Manages the notifications about the on going clicking process in the "app" mode
     */
    public void makeClicksOnGoingNotificationByApp(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_APP);
    }

    /**
     * Manages the notifications about the on going clicking process in the "standalone" mode
     */
    public void makeClicksOnGoingNotificationStandalone(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_STANDALONE);
    }

    /**
     * Manages the notifications about the stopped clicking process
     */
    public void makeClicksStoppedNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_STOPPED);
    }

    /**
     * Manages the notifications about the clicking process which is over
     */
    public void makeClicksOverNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_OVER);
    }

    /**
     * Manages the notifications about the watch process is over
     */
    public void makeWatchProcessStoppedNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.WATCH_OVER);
    }

    /**
     * Manages the notifications about the count down for delayed starts
     * @param countDown - The leaving amount of seconds before start
     */
    public void makeCountDownNotification( long countDown ){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.COUNT_DOWN, countDown);
    }

    /**
     * Manages the notifications about the granted SU permission
     */
    public void makeSuGrantedNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.makeNotification(StatusBarNotifier.NotificationTypes.SU_GRANTED);
    }

    /**
     * Stops the notifications about the on going clicking process
     */
    public void stopClicksOnGoingNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_APP);
    }

    /**
     * Stops the notifications about the stopped clicking process
     */
    public void stopClicksStoppedNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICKS_STOPPED);
    }

    /**
     * Stops the notifications about the over clicking process
     */
    public void stopClickOverNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICKS_OVER);
    }

    /**
     * Stops the notifications about the granted SU permission
     */
    public void stopSuGrantedNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeNotification(StatusBarNotifier.NotificationTypes.SU_GRANTED);
    }

    /**
     * Stops the notifications about the new made click
     */
    public void stopClickMadeNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICK_MADE);
    }

    /**
     * Stops the notifications about the count-down
     */
    public void stopCountdownNotification(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeNotification(StatusBarNotifier.NotificationTypes.COUNT_DOWN);
    }

    /**
     * Stops all the notifications
     */
    public void stopAllNotifications(){
        StatusBarNotifier sbn = new StatusBarNotifier(mContext);
        sbn.removeAllNotifications();
    }

    /**
     * Initializes the singleton
     */
    private void init(){

        SharedPreferences sp = mContext.getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, Config.SP_ACCESS_MODE);

        // Vibrations
        mVibrateOnStart = sp.getBoolean(Config.SP_KEY_VIBRATE_ON_START, Config.DEFAULT_VIBRATE_ON_START);
        mVibrateOnClick = sp.getBoolean(Config.SP_KEY_VIBRATE_ON_CLICK, Config.DEFAULT_VIBRATE_ON_CLICK);

        // Notifications
        mNotifOnClick = sp.getBoolean(Config.SP_KEY_NOTIF_ON_CLICK, Config.DEFAULT_NOTIF_ON_CLICK);

        // Sounds
        mRingOnClick = sp.getBoolean(Config.SP_KEY_RING_ON_CLICK, Config.DEFAULT_RING_ON_CLICK);
    }

    /**
     * Defines the context to use for this object, the context ahs to be defined to get accesses to vibrator, shared preferences, etc.
     * @param c -
     */
    public void refresh( Context c ){
        mContext = c;
        init();
    }

}
