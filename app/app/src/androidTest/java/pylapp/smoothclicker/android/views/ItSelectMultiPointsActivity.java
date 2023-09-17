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

package pylapp.smoothclicker.android.views;

import android.content.Context;
import android.content.Intent;

import android.support.test.InstrumentationRegistry;
import static android.support.test.InstrumentationRegistry.getInstrumentation;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.R;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


/**
 * Class to use to make UI tests with UIAutomator of the SelectMultiPointsActivity.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 13/04/2016
 * @see AbstractTest
 */
public class ItSelectMultiPointsActivity extends AbstractTest {


    /**
     * The UIDevice object is the primary way to access and manipulate the state of the device
     */
    private UiDevice mDevice;

    /**
     *
     */
    private static final int LAUNCH_TIMEOUT_MS = 5000;
    /**
     *
     */
    private static final String PACKAGE_APP_PATH = "pylapp.smoothclicker.android";


    /**
     * Starts the main activity to test from the home screen
     */
    @Before
    public void startMainActivityFromHomeScreen() {

        l(this, "@Before startMainActivityFromHomeScreen");

        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT_MS);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_APP_PATH);

        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_APP_PATH).depth(0)), LAUNCH_TIMEOUT_MS);

    }

    /**
     * Tests if the snack bar is displayed when a point has been clicked
     *
     * <i>If a point with (x,y) coordinates has been chosen, a snackbar with the following text has to be displayed "Click X = x / Y =  y"</i>
     */
    @Test
    public void snackBarNewPoint(){

        l(this, "@Test snackBarNewPoint");

        final int X = 500;
        final int Y = 600;
        final String S = "Click X = " + X + " / Y = " + Y;

        try {

            // Wait for the activity because Espresso is too fast
            w(5000);

            // Click on the dedicated screen to make a new point
            clickAt(X, Y);

            // Get the snack bar
            UiObject snackBar = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );

            w(2000);
            assertTrue(snackBar.exists());
            assertEquals( S, snackBar.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Clicks on the screen at coordinates (x,y)
     * @param x - The x-axis coordinate
     * @param y - The y-axis coordinate
     */
    private void clickAt( int x, int y ){

        // Open the arc menu
        onView(withId(R.id.fabAction)).perform(click());
        w(1000);

        // Start the select multipoints activity
        onView(withId(R.id.fabSelectPoint)).perform(click());
        w(2000);

        // Make two clicks on the screen
        onView(withId(R.id.translucentMainView)).perform(clickXY(x, y));

    }

    /**
     * Custom ViewAction to click on dedicated coordinates
     * @param x -
     * @param y -
     * @return ViewAction -
     */
    private ViewAction clickXY( final int x, final int y ){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates( View view ){

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;

                        return new float[]{screenX, screenY};

                    }
                },
                Press.FINGER);
    }

}
