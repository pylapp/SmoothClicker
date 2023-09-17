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
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.notifiers.StatusBarNotifier;
import pylapp.smoothclicker.android.views.ClickerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Class to use to make UI tests with Espresso and UIAutomator of the NotificationsManager.
 *
 *  @author Pierre-Yves Lapersonne
 *  @version 1.3.0
 *  @since 21/03/2016
 *  @see AbstractTest
 */
public class ItStatusBarNotifier extends AbstractTest {


    /**
     * Defines a rule for our tests : here the activity to test.
     * An Espresso object.
     */
    @Rule
    public ActivityTestRule<ClickerActivity> mActivityRule = new ActivityTestRule<>(ClickerActivity.class);

    /**
     * The object to test its behaviour
     */
    private StatusBarNotifier mSbn;

    /**
     * A UIAutomator object to handles the notifications
     */
    private UiDevice mDevice;

    /**
     * The application context.
     */
    private Context mContext;


    /**
     * Initializes the NotificationManager
     *
     * <i>The tests have to start on the home screen</i>
     */
    @Before
    public void init(){
        l(this,"@Before init");
        mContext = mActivityRule.getActivity().getApplicationContext();
        mSbn = new StatusBarNotifier(mContext);
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
    }

    /**
     * Cleans
     * <i>The tests have make the device to its initial state</i>
     */
    @After
    public void clean(){
        l(this,"@After clean");
        mDevice.pressHome();
    }

    /**
     * Test the makeNotification() method
     *
     * <i>The notification features can handle removable (or not) notifications, with clicks on them and wrong inputs</i>
     */
    @Test
    public void makeNotification(){

        l(this, "@Test makeNotification");

        String textContent = "";

        //  Clicks on going by app
        textContent = mContext.getString(R.string.notif_content_text_clicks_on_going_app);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_APP);

        //  Clicks on going by service
        textContent = mContext.getString(R.string.notif_content_text_clicks_on_going_service);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_SERVICE);

        //  Countdown
        textContent = mContext.getString(R.string.notif_content_text_countdown);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.COUNT_DOWN);

        //  New click made
        textContent = mContext.getString(R.string.notif_content_text_click_made);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.CLICK_MADE);

        //  Click process overhttp://forum.korben.info/topic/2688-mp3-de-notifications/
        textContent = mContext.getString(R.string.notif_content_text_clicks_over);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.CLICKS_OVER);

        //  Clicks process stopped
        textContent = mContext.getString(R.string.notif_content_text_clicks_stop);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.CLICKS_STOPPED);

        //  Watch/picture recognition process stopped
        textContent = mContext.getString(R.string.notif_content_text_watch_over);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.WATCH_OVER);

        //  SU Granted
        textContent = mContext.getString(R.string.notif_content_text_su_granted);
        testMakeNotification(textContent, StatusBarNotifier.NotificationTypes.SU_GRANTED);

    }

    /**
     * Test the removeNotification() method
     *
     * <i>All notifications have to be removable</i>
     */
    @Test
    public void removeNotification(){

        l(this,"@Test removeNotification");

        //  Clicks on going by app
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_APP);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_APP);

        //  Clicks on going by service
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_SERVICE);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICKS_ON_GOING_BY_SERVICE);

        //  Countdown
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.COUNT_DOWN);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.COUNT_DOWN);

        //  New click made
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICK_MADE);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICK_MADE);

        //  Click process over
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_OVER);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICKS_OVER);

        //  Clicks process stopped
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.CLICKS_STOPPED);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.CLICKS_STOPPED);

        //  Watch process stopped
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.WATCH_OVER);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.WATCH_OVER);

        //  SU Granted
        mSbn.makeNotification(StatusBarNotifier.NotificationTypes.SU_GRANTED);
        w(1000);
        mSbn.removeNotification(StatusBarNotifier.NotificationTypes.SU_GRANTED);

    }

    /**
     * Inner method to test a the makeNotification() method with different values
     * @param textContent - The text to look of the notification to test
     * @param type - The notification type
     */
    private void testMakeNotification( String textContent, StatusBarNotifier.NotificationTypes type ){
        mSbn.makeNotification(type);
        testNotification(textContent);
        mSbn.removeAllNotifications();
        w(1000);
        mSbn.makeNotification(type);
        testNotification(textContent);
        mSbn.removeAllNotifications();
        w(1000);
        mSbn.makeNotification(type, 0, 1, 2, 3, -5);
        testNotification(textContent);
        mSbn.removeAllNotifications();
        w(1000);
    }

    /**
     * Inner method to get a dedicated notification and test it
     * @param textContent - The text to use to get the notification
     */
    private void testNotification( String textContent ){

        if ( textContent == null ) textContent = "";

        UiObject n = null;

        if ( Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH ){
            n = mDevice.findObject(
                    new UiSelector()
                            .resourceId("android:id/text")
                            .className("android.widget.TextView")
                            .packageName("pylapp.smoothclicker.android")
                            .textContains(textContent));
        } else {
            n = mDevice.findObject(
                    new UiSelector()
                            .resourceId("android:id/text")
                            .className("android.widget.TextView")
                            .packageName("com.android.systemui")
                            .textContains(textContent));
        }

        mDevice.openNotification();
        n.waitForExists(2000);
        assertTrue(n.exists());

    }

}
