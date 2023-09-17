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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.R;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

/**
 * Class to use to make UI tests with UIAutomator of the ClickerActivity.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.4.0
 * @since 23/03/2016
 * @see AbstractTest
 */
public class ItSettingsActivity extends AbstractTest {


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

        // Prepare for tests
        openSettingsScreenFromMenu();

    }

    /**
     * Check and uncheck the checkboxes in the settings screen
     */
    @Test
    public void selections(){

        l(this, "@Test selections");

        // The checkboxes
        BySelector checkboxSettingsSelector = By.clazz("android.widget.CheckBox");
        List<UiObject2> checkBoxes = mDevice.findObjects(checkboxSettingsSelector);

        // Click...
        for ( UiObject2 cb : checkBoxes ){
            cb.click();
            w(1000);
        }

        // ...and click again
        for ( UiObject2 cb : checkBoxes ){
            cb.click();
            w(1000);
        }

    }

    /**
     * Tests the seekbar for the threshold sued in the picture recognition system
     */
    @Test
    public void thresholdSeekBar(){

        l(this, "@Test thresholdSeekBar");

        try {

            // Swipe to the bottom of the view to get the seekbar
            UiObject list = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.ListView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/list")
            );
            list.swipeUp(100);
            list.swipeUp(100);

            // Get the seek bar
            UiObject seekBar = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.SeekBar")
                            .packageName(PACKAGE_APP_PATH)
            );

            // Swipe to the very right and check if we are at 100%
            seekBar.swipeRight(100);

            UiObject seekBarValue = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId(PACKAGE_APP_PATH + ":id/seekbar_value")
            );
            assertEquals( "10", seekBarValue.getText() );

            // Swipe to the very left and check if we are at 100%
            seekBar.swipeLeft(100);

            seekBarValue = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId(PACKAGE_APP_PATH+":id/seekbar_value")
            );
            assertEquals("1", seekBarValue.getText());
            w(1000);

//            // Click on the item to get the dialog to fill the seekbar: change the value but cancel
//            seekBarValue.click();
//            UiObject customValue = mDevice.findObject(
//                    new UiSelector()
//                            .className("android.widget.EditText")
//                            .packageName(PACKAGE_APP_PATH)
//                            .resourceId(PACKAGE_APP_PATH + ":id/customValue")
//            );
//            customValue.setText("42");
//            UiObject button = mDevice.findObject(
//                    new UiSelector()
//                            .className("android.widget.Button")
//                            .packageName(PACKAGE_APP_PATH)
//                            .resourceId(PACKAGE_APP_PATH + ":id/btn_cancel")
//            );
//            button.click();
//            assertEquals("1", seekBarValue.getText());
//            w(1000);
//
//            // Click on the item to get the dialog to fill the seekbar: change the value but apply
//            seekBarValue.click();
//            customValue = mDevice.findObject(
//                    new UiSelector()
//                            .className("android.widget.EditText")
//                            .packageName(PACKAGE_APP_PATH)
//                            .resourceId(PACKAGE_APP_PATH + ":id/customValue")
//            );
//            customValue.setText("23");
//            button = mDevice.findObject(
//                    new UiSelector()
//                            .className("android.widget.Button")
//                            .packageName(PACKAGE_APP_PATH)
//                            .resourceId(PACKAGE_APP_PATH + ":id/btn_apply")
//            );
//            button.click();
//            assertEquals("23", seekBarValue.getText());
//            w(1000);

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail(uonfe.getMessage());
        }

    }

    /**
     * Test if the item about credits in the Settings activity starts the credits activity
     */
    @Test
    public void credits(){

        l(this, "@Test credits");

        try {

            // Swipe to the bottom of the view to get the credits field
            UiObject list = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.ListView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/list")
            );
            list.swipeUp(100);
            list.swipeUp(100);

            // Clicks on the credits row
            String s = InstrumentationRegistry.getTargetContext().getString(R.string.pref_key_credit_title);
            UiObject creditsRow = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/title")
                            .text(s)
            );
            creditsRow.click();
            w(1000);

            assertEquals(CreditsActivity.class.getSimpleName(), getActivityInstance().getClass().getSimpleName());

        } catch ( UiObjectNotFoundException uonfe ){
            fail(uonfe.getMessage());
            uonfe.printStackTrace();
        }

    }

    /**
     * Test if the item about help in the Settings activity starts the intro screens activity
     */
    @Test
    public void help(){

        l(this, "@Test help");

        try {

            // Swipe to the bottom of the view to get the credits field in an inner preference screen
            UiObject list = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.ListView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/list")
            );

            list.swipeUp(100);
            list.swipeUp(100);

            String innerPreferenceScreenTitle = InstrumentationRegistry.getTargetContext().getString(R.string.pref_about_subtitle);
            UiObject aboutRow = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/title")
                            .text(innerPreferenceScreenTitle)
            );
            aboutRow.click();

            // Clicks on the credits row
            String s = InstrumentationRegistry.getTargetContext().getString(R.string.pref_key_help_title);
            UiObject helpRow = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/title")
                            .text(s)
            );
            helpRow.click();
            w(1000);

            assertEquals(IntroScreensActivity.class.getSimpleName(), getActivityInstance().getClass().getSimpleName());

        } catch ( UiObjectNotFoundException uonfe ){
            fail(uonfe.getMessage());
            uonfe.printStackTrace();
        }

    }

    /**
     * Tests the files names fields
     *
     * <i>If we change the name of the file in sue, we have to see the summary of the dedicated field be updated</i>
     */
    @Test
    public void filesNames(){

        l(this, "@Test filesNames");

        try {

            // Swipe to the bottom of the view to get the fields field in an inner preference screen
            UiObject list = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.ListView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/list")
            );

            list.swipeUp(100);

            String innerPreferenceScreenTitle = InstrumentationRegistry.getTargetContext().getString(R.string.pref_app_files_title);
            UiObject filesRow = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/title")
                            .text(innerPreferenceScreenTitle)
            );
            filesRow.click();

            // Update the name of file containing the points
            testFieldWithName(0, InstrumentationRegistry.getTargetContext().getString(R.string.pref_app_files_points_name));
            // Update the name of file containing the config
            testFieldWithName(1, InstrumentationRegistry.getTargetContext().getString(R.string.pref_app_files_config_name));
            // Update the name of file containing the unlock script
            testFieldWithName(2, InstrumentationRegistry.getTargetContext().getString(R.string.pref_app_files_unlock_name));
            // Update the name of file containing the picture
            testFieldWithName(3, InstrumentationRegistry.getTargetContext().getString(R.string.pref_app_files_trigger_name));

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail(uonfe.getMessage());
        }

    }

    /**
     * Opens the settings screen from the menu
     */
    private void openSettingsScreenFromMenu(){

        try {

            // Clicks the three-points-icon
            UiObject menu = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.ImageView")
                            .packageName(PACKAGE_APP_PATH)
                                    //.descriptionContains("Plus d'options") // WARNING FIXME French string used, use instead system R values
                            .descriptionContains("Autres options") // WARNING FIXME French string used, use instead system R values
            );
            menu.click();

            // Clicks on the settings item
            String s = InstrumentationRegistry.getTargetContext().getString(R.string.action_settings);
            UiObject itemParams = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId(PACKAGE_APP_PATH + ":id/title")
                            .text(s)
            );
            itemParams.click();
            w(1000);

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail(uonfe.getMessage());
        }
        w(1000);

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

    /**
     * @param index - The idnex of the field in the list (start at 0)
     * @param text - The text of the field to get
     */
    private void testFieldWithName( int index, String text ){

        if ( text == null || text.length() <= 0 || index < 0){
            fail("Wrong test");
        }

        final String DUMMY_TEXT = "Hello world";

        try {
            // Display the dialog
            UiObject field = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/title")
                            .text(text)
            );
            field.click();
            // Change the value
            field = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.EditText")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/edit")
            );
            final String BACKUP_TEXT = field.getText();
            field.setText(DUMMY_TEXT);
            // Confirm
            UiObject button = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.Button")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/button1")
            );
            button.click();
            w(1000);
            // Check the summary
            BySelector checkboxSettingsSelector = By.clazz("android.widget.TextView");
            List<UiObject2> summaries = mDevice.findObjects(checkboxSettingsSelector);
            UiObject2 field2 = summaries.get( index * 4 + 2 );
            assertEquals(DUMMY_TEXT, field2.getText());
            // Reset the value
            resetFieldWithText( DUMMY_TEXT, BACKUP_TEXT );
        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail(uonfe.getMessage());
        }

    }

    /**
     * @param text - The text of the field to get
     * @param newValue - The new value
     */
    private void resetFieldWithText( String text, String newValue ){

        if ( text == null || text.length() <= 0 ){
            fail("Wrong test");
        }

        try {
            // Display the dialog
            UiObject field = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.TextView")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/title")
                            .text(text)
            );
            field.click();
            // Change the value
            field = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.EditText")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/edit")
            );
            field.setText(newValue);
            // Confirm
            UiObject button = mDevice.findObject(
                    new UiSelector()
                            .className("android.widget.Button")
                            .packageName(PACKAGE_APP_PATH)
                            .resourceId("android:id/button1")
            );
            button.click();
        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail(uonfe.getMessage());
        }

    }

}
