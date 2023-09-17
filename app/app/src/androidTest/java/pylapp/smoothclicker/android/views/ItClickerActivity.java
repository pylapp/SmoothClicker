package pylapp.smoothclicker.android.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.utils.Config;

import static android.support.test.InstrumentationRegistry.getInstrumentation;


/**
 * Class to use to make UI tests with UIAutomator and Espresso of the ClickerActivity.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.2.0
 * @since 11/04/2016
 * @see AbstractTest
 */
public class ItClickerActivity extends AbstractTest {


    /**
     * The UIDevice object is the primary way to access and manipulate the state of the device
     */
    private UiDevice mDevice;

    /**
     * Defines a rule for our tests : here the activity to test.
     * An Espresso object..
     */
    @Rule
    public ActivityTestRule<ClickerActivity> mActivityRule = new ActivityTestRule<>(ClickerActivity.class);

    /**
     *
     */
    private static final int MOCK_POINT_X = 500;
    /**
     *
     */
    private static final int MOCK_POINT_Y = 600;

    /**
     *
     */
    private static final int LAUNCH_TIMEOUT_MS = 5000;
    /**
     *
     */
    private static final int WAIT_FOR_EXISTS_TIMEOUT = 5000;
    /**
     *
     */
    private static final String PACKAGE_APP_PATH = "pylapp.smoothclicker.android";


    /**
     *
     */
    @Before
    public void init(){
        l(this,"@Before init");
    }

    /**
     *
     */
    @After
    public void clean(){
        l(this, "@After clean");
    }

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
     * Tests the long clicks on the floating action button for start in the arc menu
     *
     * <i>A long click on the button to use to start the process should display a snackbar with an explain message</i>
     */
    @Test
    public void longClickOnArcMenuStartItem(){

        l(this, "@Test longClickOnArcMenuStartItem");

        String expectedText = InstrumentationRegistry.getTargetContext().getString(R.string.info_message_start);

        try {

            /*
             * Display the floating action buttons in the arc menu
             */
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();
            arcMenu.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);

            /*
             * The floating action button
             */
            UiObject fab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH+":id/fabStart")
            );
            fab.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);
            assertTrue(fab.isLongClickable());
            fab.swipeLeft(100); //fab.longClick() makes clicks sometimes, so swipeLeft() is a trick to make always a longclick
            UiObject snack = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );

            assertEquals(expectedText, snack.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Tests the long clicks on the floating action button for stop in the arc menu
     *
     * <i>A long click on the button to use to stop the process should display a snackbar with an explain message</i>
     */
    @Test
    public void longClickOnArcMenuStopItem(){

        l(this, "@Test longClickOnArcMenuStopItem");

        String expectedString = InstrumentationRegistry.getTargetContext().getString(R.string.info_message_stop);

        try {

            /*
             * Display the floating action buttons in the arc menu
             */
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();
            arcMenu.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);

            /*
             * The floating action button
             */
            UiObject fab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH+":id/fabStop")
            );
            fab.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);
            assertTrue(fab.isLongClickable());
            fab.swipeLeft(100); //fab.longClick() makes clicks sometimes, so swipeLeft() is a trick to make always a longclick
            UiObject snack = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );

            assertEquals(expectedString, snack.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Tests the long clicks on the floating action button for SU grant in the arc menu
     *
     * <i>A long click on the button to use to get the SU grant should display a snackbar with an explain message</i>
     */
    @Test
    public void longClickOnArcMenuSuGrantItem(){

        l(this, "@Test longClickOnArcMenuSuGrantItem");

        String expectedString = InstrumentationRegistry.getTargetContext().getString(R.string.info_message_request_su);

        try {

            /*
             * Display the floating action buttons in the arc menu
             */
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();
            arcMenu.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);

            /*
             * The floating action button
             */
            UiObject fab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH+":id/fabRequestSuGrant")
            );
            fab.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);
            assertTrue(fab.isLongClickable());
            fab.swipeLeft(100); //fab.longClick() makes clicks sometimes, so swipeLeft() is a trick to make always a longclick
            UiObject snack = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );

            assertEquals(expectedString, snack.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Tests the long clicks on the floating action button for new point in the arc menu
     *
     * <i>A long click on the button to use to add points to click on should display a snackbar with an explain message</i>
     */
    @Test
    public void longClickOnArcMenuNewPointItem(){

        l(this, "@Test longClickOnArcMenuNewPointItem");

        String expectedString = InstrumentationRegistry.getTargetContext().getString(R.string.info_message_new_point);
        try {

            /*
             * Display the floating action buttons in the arc menu
             */
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();
            arcMenu.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);

            /*
             * The floating action button
             */
            UiObject fab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH+":id/fabSelectPoint")
            );
            fab.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);
            assertTrue(fab.isLongClickable());
            fab.swipeUp(100); //fab.longClick() makes clicks sometimes, so swipeUp() is a trick to make always a longclick
            UiObject snack = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );

            assertEquals(expectedString, snack.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Test if the switch changes (for the start type) modifies well the delay field
     *
     * <i>If the switch for the start type is ON, the delay field is enabled.</i>
     * <i>If the switch for the start type is OFF, the delay field is disabled.</i>
     */
    @Test
    public void changeDelayOnSwitchChanges(){

        l(this, "@Test changeDelayOnSwitchChanges");

        try {

            UiObject startSwitch = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/sTypeOfStartDelayed")
            );

            UiObject delayField = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/etDelay")
            );

            // Check and uncheck the switch
            for ( int i = 1; i <= 2; i++ ) {
                startSwitch.click();
                if (startSwitch.isChecked()) {
                    assertTrue(delayField.isEnabled());
                } else {
                    assertFalse(delayField.isEnabled());
                }
            }

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Test if the endless repeat checkbox changes modifies well the repeat field
     *
     * <i>If the check box for the endless repeat is checked, the repeat field is disabled</i>
     * <i>If the check box for the endless repeat is unchecked, the repeat field is enabled</i>
     */
    @Test
    public void changeRepeatOnCheckboxChanges(){

        l(this, "@Test changeRepeatOnCheckboxChanges");

        try {

            UiObject endlessCheckbox = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/scEndlessRepeat")
            );

            UiObject repeatField = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/etRepeat")
            );

            // Check and uncheck the switch
            for ( int i = 1; i <= 2; i++ ) {
                endlessCheckbox.click();
                if (endlessCheckbox.isChecked()) {
                    assertFalse(repeatField.isEnabled());
                } else {
                    assertTrue(repeatField.isEnabled());
                }
            }

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Test the click on the SU grant button
     *
     * <i>If the button to get the SU grant is clicked, a notification about the good access to SU grant is displayed</i>
     */
    //@Test
    @Deprecated
    public void clickOnSuGrantButton(){

        l(this, "@Test clickOnSuGrantButton");

        try {

            // Open the arc menu
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();

            // Request the SU grant
            UiObject suGrantFab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabRequestSuGrant")
            );
            suGrantFab.click();

            // Check the notifications panel
            UiObject n = null;

            if ( Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH ){
                n = mDevice.findObject(
                        new UiSelector()
                                .resourceId("android:id/text")
                                .className("android.widget.TextView")
                                .packageName("pylapp.smoothclicker.android")
                                .textContains("Droits Super-utilisateur accordés à Smooth Clicker")); // WARNING FIXME French language, get the string in system R
            } else {
                n = mDevice.findObject(
                        new UiSelector()
                                .resourceId("android:id/text")
                                .className("android.widget.TextView")
                                .packageName("com.android.systemui")
                                .textContains("Droits Super-utilisateur accordés à Smooth Clicker")); // WARNING FIXME French language, get the string in system R
            }

            mDevice.openNotification();
            n.waitForExists(2000);
            assertTrue(n.exists());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Test the start button without having defined clicks
     *
     * <i>If the button to start the click process is clicked, and no point has been defined, a snackbar with an error message has to be displayed</i>
     */
    @Test
    public void startButtonWithoutDefinedPoints(){

        l(this, "@Test startButtonWithoutDefinedPoints");

        try {

            // Open the arc menu
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();

            // Click on the button
            UiObject startFab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabStart")
            );
            startFab.click();

            // Check the snackbar
            UiObject snack = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );

            String expectedString = InstrumentationRegistry.getTargetContext().getString(R.string.error_message_no_click_defined);
            assertEquals(expectedString, snack.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Test the stop button without having started the process
     *
     * <i>If the button to stop the click process is clicked, and no process was working, a snackbar with an error message ahs to be displayed/i>
     */
    @Test
    public void stopButtonWithoutStartedProcess(){

        l(this, "@Test stopButtonWithoutStartedProcess");

        try {

            // Open the arc menu
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();

            // Click on the button
            UiObject stopFab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabStop")
            );
            stopFab.click();

            // Check the snackbar
            UiObject snack = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );

            String expectedString = InstrumentationRegistry.getTargetContext().getString(R.string.error_message_was_not_working);
            assertEquals(expectedString, snack.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Tests clicks on the select points activity and if the list of points is well populated (in the clicker activity)
     *
     * <i>If a click is donne at coordinates X/Y on the dedicated activity, the main activity has to display the selected point in its spinner</i>
     */
    @Test
    public void selectPoints(){

        l(this, "@Test selectPoints");

        w(3000);
        // Open the arc menu
        onView(withId(R.id.fabAction)).perform(click());
        w(1000);

        // Start the select multipoints activity
        onView(withId(R.id.fabSelectPoint)).perform(click());
        w(2000);

        int x = 500;
        int y  = 600;

        // Make two clicks on the screen
        onView(withId(R.id.translucentMainView)).perform(clickXY(x, y));
        w(2000);

        // Go back to the main activity
        pressBack();
        w(2000);

        // Check the spinner containing all the selected points
        onView(withId(R.id.sPointsToClick)).perform(click());
        //  onView(withText("1 clicks")).perform(click());
        onView(withText("x = "+x+" / y = "+y)).check(matches(isDisplayed()));

    }

    /**
     * Test the button for selecting points
     *
     * <i>If the button to select points is clicked, the activity to select points have to be launched</i>
     * <i>If in this activity we click on back, the main activity  must be displayed and the previous activity finished</i>
     */
    @Test
    public void selectPointsButton(){

        l(this, "@Test selectPointsButton");

        try {

            // Open the arc menu
            UiObject arcMenu = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabAction")
            );
            arcMenu.click();

            // Click on the button
            UiObject selectPointsFab = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/fabSelectPoint")
            );
            selectPointsFab.click();

            // UIAutomator seems to be useless about getting the current activity (deprecated methods)
            // so check if the enw activity's layout is displayed
            UiObject newActivityMainLayout = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/translucentMainView")
            );
            w(2000);
            assertTrue(newActivityMainLayout.exists());

            // Press back and check if we are in the previous activity (i.e. arc menu displayed)
            mDevice.pressBack();
            w(2000);
            assertTrue(arcMenu.exists());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Tests if the back button displays the exit-pop-up
     * <i>If the back button is pressed on the main activity, an exit dialog is displayed</i>
     * <i>If we click on the exit dialog's cancel button, we stay at this activity</i>
     * <i>If we click on the exit dialog's ok button, we exit the app</i>
     */
    @Test
    public void exitDialog(){

        l(this, "@Test exitDialog");

        try {

            // Wait for the main activity
            UiObject mainActivity = mDevice.findObject(
                    new UiSelector()
                            .resourceId("pylapp.smoothclicker.android:id/clickerActivityMainLayout")
                            .packageName(PACKAGE_APP_PATH)
                            .className("android.widget.ScrollView")
            );
            mainActivity.waitForExists(WAIT_FOR_EXISTS_TIMEOUT*2);

            // Back
            mDevice.pressBack();

            // Get the dialog
            String s = InstrumentationRegistry.getTargetContext().getString(R.string.message_confirm_exit_label);
            UiObject exitDialog = mDevice.findObject(
                    new UiSelector()
                            .resourceId("android:id/alertTitle")
                            .packageName(PACKAGE_APP_PATH)
                            .className("android.widget.TextView")
                            .text(s)
            );
            exitDialog.waitForExists(WAIT_FOR_EXISTS_TIMEOUT);
            w(2000);

            // Click on the cancel button
            UiObject cancelButton = mDevice.findObject(
                    new UiSelector()
                            .resourceId("android:id/button2")
                            .packageName(PACKAGE_APP_PATH)
                            .className("android.widget.Button")
            );
            cancelButton.click();
            w(2000);

            // We have to stay in the main activity
            assertFalse(exitDialog.exists());
            assertEquals(ClickerActivity.class.getSimpleName(), getActivityInstance().getClass().getSimpleName());

            // Back
            mDevice.pressBack();

            // Click on the OK button
            UiObject okButton = mDevice.findObject(
                    new UiSelector()
                            .resourceId("android:id/button1")
                            .packageName(PACKAGE_APP_PATH)
                            .className("android.widget.Button")
            );
            okButton.click();
            w(2000);

            // We have to quit the app
            assertFalse(exitDialog.exists());
            assertTrue(getActivityInstance() == null );
//            assertNotEquals(ClickerActivity.class.getSimpleName(), getActivityInstance().getClass().getSimpleName());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Tests the export feature with its snackbar and its dedicated action button
     *
     * <i>If the configuration is exported in the JSON files, a snackbar with a good message is displayed with an action button</i>
     */
    @Test
    public void exportConfig(){

        l(this, "@Test exportConfig");

        try {

            // Display the pop-up
            UiObject menu = mDevice.findObject(
                    new UiSelector().className("android.widget.ImageView").description("Autres options") // FIXME Raw french string
            );
            menu.click();
            UiObject submenu = mDevice.findObject(
                    new UiSelector().className("android.widget.TextView").text(InstrumentationRegistry.getTargetContext().getString(R.string.action_configuration))
            );
            submenu.click();
            UiObject menuItem = mDevice.findObject(
                    new UiSelector().className("android.widget.TextView").text(InstrumentationRegistry.getTargetContext().getString(R.string.action_export))
            );
            menuItem.click();

            // Check the snackbar
            UiObject snackbar = mDevice.findObject(
                    new UiSelector().className("android.widget.TextView")
                            .resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );
            assertTrue(snackbar.exists());
            assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.info_export_success), snackbar.getText());

            // Check the action button
            UiObject actionButton = mDevice.findObject(
                    new UiSelector().className("android.widget.Button")
                            .resourceId(PACKAGE_APP_PATH + ":id/snackbar_action")
            );
            assertTrue(actionButton.exists());
            assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.snackbar_see_config_file), actionButton.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail(uonfe.getMessage());
        }

    }

    /**
     * Tests the import feature with its snackbar
     *
     * <i>If the configuration is imported in the JSON files, a snackbar with a good message is displayed</i>
     */
    @Test
    public void importConfig(){

        l(this, "@Test importConfig");

        try {

            // Display the pop-up
            UiObject menu = mDevice.findObject(
                    new UiSelector().className("android.widget.ImageView").description("Autres options") // FIXME Raw french string
            );
            menu.click();
            UiObject submenu = mDevice.findObject(
                    new UiSelector().className("android.widget.TextView").text(InstrumentationRegistry.getTargetContext().getString(R.string.action_configuration))
            );
            submenu.click();
            UiObject menuItem = mDevice.findObject(
                    new UiSelector().className("android.widget.TextView").text(InstrumentationRegistry.getTargetContext().getString(R.string.action_import))
            );
            menuItem.click();

            // Check the snackbar
            UiObject snackbar = mDevice.findObject(
                    new UiSelector().className("android.widget.TextView")
                            .resourceId(PACKAGE_APP_PATH + ":id/snackbar_text")
            );
            assertTrue(snackbar.exists());
            assertEquals(InstrumentationRegistry.getTargetContext().getString(R.string.info_import_success), snackbar.getText());

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail(uonfe.getMessage());
        }

    }

    /**
     * Test the click on the button for the "clean all" feature
     *
     * <i>If the button to clean all is clicked, the configuration / values ion the fields have to be the default values, and the list of points has to be cleaned</i>
     * <i>If the button to clean all is clicked and no values has been changed, the values still remain the default ones</i>
     */
    @Test
    public void clickCleanAll(){

        l(this, "@Test clickCleanAll");

        try {

            // Get the menu item
            UiObject mi = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/action_clean_all")
            );

            // Add some values
            UiObject delayField = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/etDelay")
            );
            delayField.setText("007");

            Espresso.closeSoftKeyboard();

            w(1000);
            UiObject repeatField = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/etRepeat")
            );
            repeatField.setText("42");
            Espresso.closeSoftKeyboard();
            w(1000);
            UiObject timeGapField = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/etTimeBeforeEachClick")
            );
            timeGapField.setText("123");
            Espresso.closeSoftKeyboard();
            w(1000);
            UiObject endless = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/scEndlessRepeat")
            );
            endless.click();
            w(1000);

            // Swipe to display items
            UiObject swipeableLayout = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH+":id/clickerActivityMainLayout")
            );
            swipeableLayout.swipeUp(100);

            UiObject vibrateOnStart = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/scVibrateOnStart")
            );
            vibrateOnStart.click();
            w(1000);
            UiObject vibrateOnClick = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/scVibrateOnClick")
            );
            vibrateOnClick.click();
            w(1000);
            UiObject notifications = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/scNotifOnClick")
            );
            notifications.click();
            w(1000);

            fillSpinnerAsUser();

            // Click on the menu item
            mi.click();

            // Check if the values have been made empty
            assertEquals(Config.DEFAULT_DELAY, delayField.getText());
            assertEquals(Config.DEFAULT_TIME_GAP, timeGapField.getText());
            assertEquals(Config.DEFAULT_REPEAT, repeatField.getText());
            assertEquals(Config.DEFAULT_REPEAT_ENDLESS, endless.isChecked());
            assertEquals(Config.DEFAULT_VIBRATE_ON_START, vibrateOnStart.isChecked());
            assertEquals(Config.DEFAULT_VIBRATE_ON_CLICK, vibrateOnClick.isChecked());
            assertEquals(Config.DEFAULT_NOTIF_ON_CLICK, notifications.isChecked());
//            onView(withId(R.id.clickerActivityMainLayout)).perform(swipeUp());
//            onView(withId(R.id.sPointsToClick)).perform(click());
//            onView(withId(R.id.sPointsToClick)).check(ViewAssertions.matches(withListSize(1)));

            // Test again to check if the default values remain
            mi.click();
            assertEquals(Config.DEFAULT_DELAY, delayField.getText());
            assertEquals(Config.DEFAULT_TIME_GAP, timeGapField.getText());
            assertEquals(Config.DEFAULT_REPEAT, repeatField.getText());
            assertEquals(Config.DEFAULT_REPEAT_ENDLESS, endless.isChecked());
            assertEquals(Config.DEFAULT_VIBRATE_ON_START, vibrateOnStart.isChecked());
            assertEquals(Config.DEFAULT_VIBRATE_ON_CLICK, vibrateOnClick.isChecked());
            assertEquals(Config.DEFAULT_NOTIF_ON_CLICK, notifications.isChecked());
//            onView(withId(R.id.sPointsToClick)).check(ViewAssertions.matches(withListSize(1)));

        } catch ( Exception e ){
            e.printStackTrace();
            fail( e.getMessage() );
        }

    }

    /**
     * Test the click on the button for the "clean points" feature
     *
     * <i>If the button to clean points is clicked, the list of points has to be cleaned</i>
     * <i>If the button to clean points is clicked and no values has been changed, the values still remain the default ones</i>
     */
    @Test
    public void clickCleanPoints() {

        l(this, "@Test clickCleanPoints");

        try {

            // Get the menu item
            UiObject mi = mDevice.findObject(
                    new UiSelector().resourceId(PACKAGE_APP_PATH + ":id/action_clean_all")
            );

            w(5000); // If there is no wait, Espresso fails to get the floating action button

            // Bind the list
            fillSpinnerAsUser();

            // Click on the menu item
            mi.click();

            // Check if the values have been made empty
//            onView(withId(R.id.clickerActivityMainLayout)).perform(swipeUp());
//            onView(withId(R.id.sPointsToClick)).perform(click());
//            onView(withId(R.id.sPointsToClick)).check(ViewAssertions.matches(withListSize(1)));

            // Test again to check if the default values remain
            mi.click();
            //           onView(withId(R.id.sPointsToClick)).check(ViewAssertions.matches(withListSize(1)));

        } catch ( UiObjectNotFoundException uonfe ){
            uonfe.printStackTrace();
            fail( uonfe.getMessage() );
        }

    }

    /**
     * Selects some points to fill the spinner
     */
    private void fillSpinnerAsUser(){

        // Open the arc menu
        onView(withId(R.id.fabAction)).perform(click());
        w(1000);

        // Start the select multipoints activity
        onView(withId(R.id.fabSelectPoint)).perform(click());
        w(2000);

        // Make two clicks on the screen
        onView(withId(R.id.translucentMainView)).perform(clickXY(MOCK_POINT_X, MOCK_POINT_Y));
        w(2000);

        // Go back to the main activity
        pressBack();
        w(2000);

        onView(withId(R.id.clickerActivityMainLayout)).perform(swipeUp());
        w(1000);

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
                if (resumedActivities.size() <= 0 ){
                    mResumedActivity = null;
                }
            }
        });
        return mResumedActivity;
    }

//    /**
//     * Matcher for a list with its size
//     * @param size -
//     * @return Matcher<View>
//     */
//    public static Matcher<View> withListSize( final int size ){
//        return new TypeSafeMatcher<View>() {
//            @Override public boolean matchesSafely( final View view ){
//                return ((ListView) view).getChildCount () == size;
//            }
//            @Override public void describeTo( final Description description ){
//                description.appendText ("ListView should have " + size + " items");
//            }
//        };
//    }

}
