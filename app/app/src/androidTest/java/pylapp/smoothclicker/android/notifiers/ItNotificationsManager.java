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

import android.app.Activity;
import android.content.Context;

import android.os.Build;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import static android.support.test.InstrumentationRegistry.getInstrumentation;

import android.test.suitebuilder.annotation.SmallTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.views.ClickerActivity;

import java.util.Collection;


/**
 * Class to use to make UI tests with Espresso and UIAutomator of the NotificationsManager.
 *
 *  @author Pierre-Yves Lapersonne
 *  @version 1.2.0
 *  @since 21/03/2016
 *  @see AbstractTest
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ItNotificationsManager extends AbstractTest {


    /**
     * Defines a rule for our tests : here the activity to test.
     * An Espresso object..
     */
    @Rule
    public ActivityTestRule<ClickerActivity> mActivityRule = new ActivityTestRule<>(ClickerActivity.class);

    /**
     * The object to test its behaviour
     */
    private NotificationsManager mNm;

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
     * <i>Tests should start from the home screen</i>
     */
    @Before
    public void init(){
        l(this,"@Before init");
        mContext = mActivityRule.getActivity().getApplicationContext();
        mNm = NotificationsManager.getInstance(mContext);
        mDevice = UiDevice.getInstance(getInstrumentation());
        mDevice.pressHome();
    }

    /**
     * Cleans
     *
     * <i>Tests should put the device at it initial state when done</i>
     */
    @After
    public void clean(){
        l(this,"@After clean");
        mDevice.pressHome();
    }

    /**
     * Test the countdown notifications
     *
     * <i>A notification with an int value can be displayed for the countdown</i>
     * <i>If we click on a countdown notification the main activity is displayed</i>
     */
    @Test
    public void makeCountDownNotification(){

        l(this,"@Test makeCountDownNotification");

        final int countDown = 15;
        String base = mContext.getString(R.string.notif_content_text_countdown);
        String m = base + " " + countDown;
        mNm.makeCountDownNotification(countDown);

        testIfNotificationExists(m);
        testNotificationClick(m);

    }

    /**
     * Test the click over notifications
     *
     * <i>A notification for clicks over can be displayed</i>
     * <i>If we click on a click over notification, the main activity has to be displayed</i>
     */
    @Test
    public void makeClickOverNotification(){

        l(this,"@Test makeClickOverNotification");

        mNm.makeClicksOverNotification();

        String m = mContext.getString(R.string.notif_content_text_clicks_over);
        testIfNotificationExists(m);
        testNotificationClick(m);

    }

    /**
     * Test the click stopped notifications
     *
     * <i>Notifications about stopped click process can be displayed</i>
     * <i>If we click on such notification the main activity will be displayed </i>
     */
    @Test
    public void makeClickStoppedNotification(){

        l(this,"@Test makeClickStoppedNotification");

        mNm.makeClicksStoppedNotification();

        String m = mContext.getString(R.string.notif_content_text_clicks_stop);
        testIfNotificationExists(m);
        testNotificationClick(m);

    }

   /**
    * Test the click on going notifications
    *
    * <i>A notification about on-going process (by the app) can be displayed</i>
    * <i>If such notification has been clicker, the main activity will be displayed</i>
    */
   @Test
   public void makeClickOnGoingNotification(){

        l(this, "@Test makeClickOnGoingNotification");

        mNm.makeClicksOnGoingNotificationByApp();

        String m = mContext.getString(R.string.notif_content_text_clicks_on_going_app);
        testIfNotificationExists(m);

    }

    /**
     * Test the new click notifications
     *
     * <i>A notification about a new click can be displayed</i>
     * <i>If we click on such notification, nothing occurs</i>
     */
    @Test
    public void makeNewClickNotification(){

        l(this,"@Test makeNewClickNotification");

        // Make the notification appear
        final int X = 42;
        final int Y = 1337;
        String m = mContext.getString(R.string.notif_content_text_click_made);
        mNm.makeNewClickNotifications(X, Y);

        // Check it
        testIfNotificationExists(m);

    }

    /**
     * Inner method to get a dedicated notification and test it
     * @param textContent - The text to use to get the notification
     */
    private void testIfNotificationExists( String textContent ) {

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

    /**
     * Inner method to get a dedicated notification and test if this notification is clicakble and display the good activity on click
     * @param textContent - The text to use to get the notification
     */
    private void testNotificationClick( String textContent ){

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

        try {
            n.click();
            w(5000);
            assertEquals(ClickerActivity.class.getSimpleName(), getActivityInstance().getClass().getSimpleName());
        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail();
        }

    }

    private static Activity mResumedActivity;

    /**
     * Retrieves the on going activity
     * @return Activity - The current activity
     */
    private static Activity getActivityInstance(){
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                        .getActivitiesInStage(Stage.RESUMED);
                if ( resumedActivities.iterator().hasNext() ){
                    mResumedActivity = (Activity) resumedActivities.iterator().next();
                }
            }
        });
        return mResumedActivity;
    }

}
