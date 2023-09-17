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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.tools.Logger;
import pylapp.smoothclicker.android.views.ClickerActivity;

/**
 * Utility class which manages notifications in the status bar.
 * It is based on a wrapper design pattern.
 *
 * @author Pierre-Yves Lapersonne
 * @version 2.2.0
 * @since 16/03/2016
 */
public class StatusBarNotifier {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The context to use to get access to the notifications
     */
    private Context mContext;


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The identifier of the notification about tSU permission which has been granted
     */
    public static final int NOTIF_SU_GRANTED                        = 0x000101;
    /**
     * The identifier of the notification about the clicking process which is on going (through the app)
     */
    public static final int NOTIF_CLICK_PROCESS_ON_GOING_BY_APP     = 0x000201;
    /**
     * The identifier of the notification about the clicking process which is on going (through the background service)
     */
    public static final int NOTIF_CLICK_PROCESS_ON_GOING_BY_SERVICE = 0x000202;
    /**
     * The identifier of the notification about the clicking process which has been stopped
     */
    public static final int NOTIF_CLICK_PROCESS_STOPPED             = 0x000203;
    /**
     * The identifier of the notification about the clicking process which has made all its click
     */
    public static final int NOTIF_CLICK_PROCESS_OVER                = 0x000204;
    /**
     * The identifier of the notification about the clicking process which has made a new click
     */
    public static final int NOTIF_CLICK_MADE                        = 0x000205;
    /**
     * The identifier of the notification about the countdown
     */
    public static final int NOTIF_COUNT_DOWN                        = 0x000301;
    /**
     * The identifier of the notification about the clicking process which is on going (in standalone mode)
     */
    public static final int NOTIF_CLICK_PROCESS_ON_GOING_STANDALONE = 0x000401;
    /**
     * The identifier of the notification about the watch process has been stopped
     */
    public static final int NOTIF_WATCH_PROCESS_OVER                = 0x000501;


    private static final String LOG_TAG = StatusBarNotifier.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     *
     * @param c - The context to use to get access to the notifications, must not be null
     */
    public StatusBarNotifier( Context c ){
        super();
        if ( c == null ) throw new IllegalArgumentException("The context param must not be null");
        mContext = c;
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     * Makes a unmovable notification with a dedicated LED color.
     * This notification is an "on going" one, and should be displayed will the app is clicking.
     *
     * @param type - The notification type
     * @param params -
     *               <ul>
     *                  <li>For CLICK_MADE :params[0] for the X coordinate, params[1] for the Y coordinate</li>
     *                  <li>For COUNT_DOWN :params[0] for the leaving time to display</li>
     *                  <li>Nothing otherwise</li>
     *               </ul>
     */
    public void makeNotification( NotificationTypes type, long... params ){

        Logger.d(LOG_TAG, "New notification: " + type);

        NotificationCompat.Builder b = new NotificationCompat.Builder(mContext);
        b.setSmallIcon(R.drawable.notification_icon);
        b.setContentTitle(mContext.getString(R.string.notif_content_title));
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) b.setVisibility(Notification.VISIBILITY_PUBLIC);

        if ( type != NotificationTypes.CLICK_MADE && type != NotificationTypes.CLICKS_ON_GOING_BY_SERVICE ) {
            Intent activityToStartOnClick = new Intent(mContext, ClickerActivity.class);
            activityToStartOnClick.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(mContext, 0, activityToStartOnClick, 0);
            b.setContentIntent(pi);
        }

        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = null;

        switch ( type ){
            case CLICKS_ON_GOING_BY_APP:
                b.setContentText(mContext.getString(R.string.notif_content_text_clicks_on_going_app));
                b.setProgress(0, 0, true);
                b.setLights(0xff9c27b0, 1000, 500);
                n = b.build();
                n.flags |= Notification.FLAG_NO_CLEAR;
                n.flags |= Notification.FLAG_SHOW_LIGHTS;
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_CLICK_PROCESS_ON_GOING_BY_APP, n);
                break;
            case CLICKS_ON_GOING_STANDALONE:
                b.setContentText(mContext.getString(R.string.notif_content_text_clicks_on_going_standalone));
                b.setProgress(0, 0, true);
                b.setLights(0xff9c27b0, 1000, 500);
                n = b.build();
                n.flags |= Notification.FLAG_NO_CLEAR;
                n.flags |= Notification.FLAG_SHOW_LIGHTS;
                n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_CLICK_PROCESS_ON_GOING_STANDALONE, n);
                break;
            case CLICKS_ON_GOING_BY_SERVICE:
                b.setContentText(mContext.getString(R.string.notif_content_text_clicks_on_going_service));
                b.setProgress(0, 0, true);
                b.setLights(0xff9c27b0, 1000, 500);
                n = b.build();
                n.flags |= Notification.FLAG_NO_CLEAR;
                n.flags |= Notification.FLAG_SHOW_LIGHTS;
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_CLICK_PROCESS_ON_GOING_BY_SERVICE, n);
                break;
            case CLICKS_STOPPED:
                b.setContentText(mContext.getString(R.string.notif_content_text_clicks_stop));
                n = b.build();
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_CLICK_PROCESS_STOPPED, n);
                break;
            case CLICKS_OVER:
                b.setContentText(mContext.getString(R.string.notif_content_text_clicks_over));
                n = b.build();
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_CLICK_PROCESS_OVER, n);
                break;
            case WATCH_OVER:
                b.setContentText(mContext.getString(R.string.notif_content_text_watch_over));
                n = b.build();
                n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_WATCH_PROCESS_OVER, n);
                break;
            case CLICK_MADE:
                StringBuilder sb = new StringBuilder();
                sb.append(mContext.getString(R.string.notif_content_text_click_made));
                if ( params != null && params.length == 2 ){
                    sb.append(" : ").append(params[0]).append(" / ").append(params[1]);
                }
                b.setContentText(sb.toString());
                n = b.build();
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_CLICK_MADE, n);
                break;
            case SU_GRANTED:
                b.setContentText(mContext.getString(R.string.notif_content_text_su_granted));
                n = b.build();
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_SU_GRANTED, n);
                break;
            case COUNT_DOWN:
                if ( params != null && params.length == 1 ){
                    b.setContentText(mContext.getString(R.string.notif_content_text_countdown)+" "+params[0]);
                } else {
                    b.setContentText(mContext.getString(R.string.notif_content_text_countdown));
                }
                n = b.build();
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_NO_CLEAR;
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) n.flags |= Notification.FLAG_LOCAL_ONLY;
                nm.notify(NOTIF_COUNT_DOWN, n);
                break;
        }

    }

    /**
     * Removes all notifications
     */
    public void removeAllNotifications(){
        Logger.d(LOG_TAG, "Remove all notifications");
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    /**
     * Removes a notification
     */
    public void removeNotification( NotificationTypes type ){
        Logger.d(LOG_TAG, "Remove notification: "+type);
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        switch (type){
            case CLICKS_ON_GOING_BY_APP:
                nm.cancel(NOTIF_CLICK_PROCESS_ON_GOING_BY_APP);
                break;
            case CLICKS_ON_GOING_STANDALONE:
                nm.cancel(NOTIF_CLICK_PROCESS_ON_GOING_STANDALONE);
                break;
            case CLICKS_ON_GOING_BY_SERVICE:
                nm.cancel(NOTIF_CLICK_PROCESS_ON_GOING_BY_SERVICE);
                break;
            case CLICKS_STOPPED:
                nm.cancel(NOTIF_CLICK_PROCESS_STOPPED);
                break;
            case CLICKS_OVER:
                nm.cancel(NOTIF_CLICK_PROCESS_OVER);
                break;
            case WATCH_OVER:
                nm.cancel(NOTIF_WATCH_PROCESS_OVER);
                break;
            case CLICK_MADE:
                nm.cancel(NOTIF_CLICK_MADE);
                break;
            case SU_GRANTED:
                nm.cancel(NOTIF_SU_GRANTED);
                break;
            case COUNT_DOWN:
                nm.cancel(NOTIF_COUNT_DOWN);
                break;
        }
    }


    /* *********** *
     * INNER ENUMS *
     * *********** */

    /**
     * Inner enum which possesses all status-bar notification types
     */
    public enum NotificationTypes {
        /**
         * The clicking process is running by the app
         */
        CLICKS_ON_GOING_BY_APP,
        /**
         * The clicking process is running in a standalone mode
         */
        CLICKS_ON_GOING_STANDALONE,
        /**
         * The clicking process is running by the background service
         */
        CLICKS_ON_GOING_BY_SERVICE,
        /**
         * A click has been made
         */
        CLICK_MADE,
        /**
         * The clicking process has been stopped
         */
        CLICKS_STOPPED,
        /**
         * The clicking process is over
         */
        CLICKS_OVER,
        /**
         * The watch process is over
         */
        WATCH_OVER,
        /**
         * The SU permission has been granted
         */
        SU_GRANTED,
        /**
         * The amount of time before start, i.e. a count down
         */
        COUNT_DOWN
    } // End of public enum NotificationTypes

}
