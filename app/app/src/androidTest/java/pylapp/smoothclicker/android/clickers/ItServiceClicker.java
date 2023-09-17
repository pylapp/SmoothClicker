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
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.clickers.ServiceClicker;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.rule.ServiceTestRule.withTimeout;
import static org.junit.Assert.assertTrue;

/**
 * Class to use to make instrumented tests with Espresso and UIAutomator of the ServiceClicker.
 *
 *  @author Pierre-Yves Lapersonne
 *  @version 2.0.0
 *  @since 22/03/2016
 *  @see AbstractTest
 */
public class ItServiceClicker extends AbstractTest {


    /**
     * The service rule
     */
    @Rule
    public final ServiceTestRule mServiceRule = withTimeout(10, TimeUnit.SECONDS);
    /**
     * A UIAutomator object to handles the notifications
     */
    private UiDevice mDevice;
    /**
     *
     */
    private Context mContext;


    /**
     * Initializes the NotificationManager
     * <i>The tests have to start from the home screen</i>
     */
    @Before
    public void init(){
        l(this,"@Before init");
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
        mContext = InstrumentationRegistry.getTargetContext();
    }

    /**
     *<i>The tests have to let the device to its initial state</i>
     */
    @After
    public void clean(){
        l(this, "@After clean");
    }

    /**
     * Tests the service start method without data in bundle
     *
     * <i>The clicker service can be started from an intent</i>
     */
    @Test
    public void startServiceWithoutValue(){
        l(this, "@Test startServiceWithoutValue");
        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        try {
            mServiceRule.startService( startIntent );
        } catch ( TimeoutException te ){
            // Do nothing, it can rise even if the service is working properly
        }
    }

    /**
     * Tests the service start method without well formed intent
     *
     * <i>The clicker service can be started from the outside with a dedicated intent with numerous values inside</i>
     * <i>Once started the clicker service have to make notifications displayed</i>
     */
    @Test
    public void startService(){

        l(this, "@Test startService");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        // Create the Intent with the good action
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");

        // Defines the configuration to use
        startIntent.putExtra("0x000011", true); // Start delayed ?
        startIntent.putExtra("0x000012", 20);   // How much delay for the start ?
        startIntent.putExtra("0x000013", 2);    // The amount of time to wait between clicks
        startIntent.putExtra("0x000021", 10);    // The number of repeat to do
        startIntent.putExtra("0x000022", false);// Endless repeat ?
        startIntent.putExtra("0x000031", false);// Vibrate on start ?
        startIntent.putExtra("0x000032", false);// Vibrate on each click ?
        startIntent.putExtra("0x000041", true);// Make notifications ?
        ArrayList<Integer> points = new ArrayList<>();
        points.add(695); // x0
        points.add(799); // y0
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points

        try {
            mServiceRule.startService( startIntent );
        } catch ( TimeoutException te ){
            // Do nothing, it can rise even if the service is working properly
        }

        // Test the countdown notification
        testNotification(mContext.getString(R.string.notif_content_text_countdown));

        w(10000);

        // Test the new click notification
        testNotification(mContext.getString(R.string.notif_content_text_click_made));

        w(5000);

        // Test the on going process notification
        testNotification(mContext.getString(R.string.notif_content_text_clicks_on_going_service));

        w(10000);

        // Test the terminated notification
//        testNotification(mContext.getString(R.string.notif_content_text_clicks_over));

    }

    /**
     * Tests the service start method with dummy values
     *
     * <i>A service can handle bad actions in its intent and make nothing / returns if it occurs</i>
     */
    //@Test
    // FIXME To tests with UT instead of IT
    public void startServiceWithBadAction() {

        l(this, "@Test startServiceWithBadAction");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.STOP");
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }

    /**
     * Tests the service start method with dummy values
     *
     * <i>A service can handle negative delays</i>
     */
    //@Test
    // FIXME To tests with UT instead of IT
    public void startServiceWithNegativeValues1() {

        l(this, "@Test startServiceWithNegativeValues1");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");
        startIntent.putExtra("0x000012", -20);   // How much delay for the start ?
        ArrayList<Integer> points = new ArrayList<>();
        points.add(0); // x0
        points.add(0); // y0
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }

    /**
     * Tests the service start method with dummy values
     *
     *< i>A service can handle negative time gaps</i>
     */
    //@Test
    // FIXME To tests with UT instead of IT
    public void startServiceWithNegativeValues2() {

        l(this, "@Test startServiceWithNegativeValues2");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");
        startIntent.putExtra("0x000013", -2);    // The amount of time to wait between clicks
        ArrayList<Integer> points = new ArrayList<>();
        points.add(0); // x0
        points.add(0); // y0
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }

    /**
     * Tests the service start method with dummy values
     *
     * <i>The service can handle negative repeat</i>
     */
    //@Test
    // FIXME To tests with UT instead of IT
    public void startServiceWithNegativeValues3() {

        l(this, "@Test startServiceWithNegativeValues3");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");
        startIntent.putExtra("0x000021", -10);    // The number of repeat to do
        ArrayList<Integer> points = new ArrayList<>();
        points.add(0); // x0
        points.add(0); // y0
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }

    /**
     * Tests the service start method with dummy values
     *
     * <i>The service can handle points with negative coordinates</i>
     */
    //@Test
    // FIXME To tests with UT instead of IT
    public void startServiceWithNegativeValues4() {

        l(this, "@Test startServiceWithNegativeValues4");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");
        ArrayList<Integer> points = new ArrayList<>();
        points.add(0); // x0
        points.add(42); // y0
        points.add(1337); // x1
        points.add(-50); // y1
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }


    /**
     * Tests the service start method with dummy values
     *
     * <i>The service can handle points with too big coordinates</i>
     */
    @Test
    public void startServiceWithToBigCoordinates() {

        l(this, "@Test startServiceWithToBigCoordinates");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");
        ArrayList<Integer> points = new ArrayList<>();
        points.add(Integer.MAX_VALUE); // x0
        points.add(Integer.MAX_VALUE); // y0
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }

    /**
     * Tests the service start method with dummy values
     *
     * <i>The service can handle points with too small coordinates</i>
     */
    //@Test
    // FIXME To tests with UT instead of IT
    public void startServiceWithToSmallCoordinates() {

        l(this, "@Test startServiceWithToSmallCoordinates");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");
        ArrayList<Integer> points = new ArrayList<>();
        points.add(Integer.MIN_VALUE); // x0
        points.add(Integer.MIN_VALUE); // y0
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }


    /**
     * Tests the service start method without null values
     *
     * <i>The service can handle null values for the points</i>
     */
    //@Test
    // FIXME To tests with UT instead of IT
    public void startServiceWithNullValues(){

        l(this, "@Test startServiceWithNullValues");

        Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), ServiceClicker.class);
        // Create the Intent with the good action
        startIntent.setAction("pylapp.smoothclicker.android.clickers.ServiceClicker.START");

        // Defines the configuration to use
        startIntent.putExtra("0x000011", true); // Start delayed ?
        startIntent.putExtra("0x000012", 20);   // How much delay for the start ?
        startIntent.putExtra("0x000013", 2);    // The amount of time to wait between clicks
        startIntent.putExtra("0x000021", 10);    // The number of repeat to do
        startIntent.putExtra("0x000022", false);// Endless repeat ?
        startIntent.putExtra("0x000031", false);// Vibrate on start ?
        startIntent.putExtra("0x000032", false);// Vibrate on each click ?
        startIntent.putExtra("0x000041", true);// Make notifications ?
        ArrayList<Integer> points = new ArrayList<>();
        startIntent.putIntegerArrayListExtra("0x000051", points); // The list of points
        try { mServiceRule.startService( startIntent ); } catch ( TimeoutException te ){te.printStackTrace();}

    }

    /**
     * Inner method to get a dedicated notification and test it
     * @param textContent - The text to use to get the notification
     */
    private void testNotification( String textContent ){

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
        n.waitForExists(60000);
        assertTrue(n.exists());

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
                if (resumedActivities.iterator().hasNext()) {
                    mResumedActivity = (Activity) resumedActivities.iterator().next();
                }
            }
        });
        return mResumedActivity;
    }

}
