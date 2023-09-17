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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.clickers.ATClicker;
import pylapp.smoothclicker.android.json.JsonFileParser;
import pylapp.smoothclicker.android.json.NotSuitableJsonConfigFileException;
import pylapp.smoothclicker.android.json.NotSuitableJsonPointsFileException;
import pylapp.smoothclicker.android.notifiers.NotificationsManager;
import pylapp.smoothclicker.android.tools.Logger;
import pylapp.smoothclicker.android.tools.screen.ATScreenWatcher;
import pylapp.smoothclicker.android.utils.Config;


/**
 * A kind of main activity of this Smooth Clicker app, more for stand-alone purposes.
 * It parses the JSON file containing the points to click on, it parses also the JSON file containing the configuration to use,
 * and it starts the clicking process with these values.
 * This activity must be used remotely e.g. with Appium thanks to dedicated intent and actions, or thanks to the GUI to trigger this mode.
 * Standalone mode is more for "already-defined-configurations"
 *
 *
 * To click on all the points defined in the JSON file (with the JSON config file too):
     <pre>
        am start -a pylapp.smoothclicker.android.CLICK_ON_ALL_POINTS -n pylapp.smoothclicker.android/pylapp.smoothclicker.android.views.StandaloneActivity
     </pre>
 *
 * To click on all the points defined in the JSON file (with the JSON config file too) but only if the screen state is the same as the scren picture referenced in the app's folder:
     <pre>
     am start -a pylapp.smoothclicker.android.CLICK_ON_ALL_POINTS_ACCORDING_SCREEN -n pylapp.smoothclicker.android/pylapp.smoothclicker.android.views.StandaloneActivity
     </pre>
 * 
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.2.0
 * @since 10/05/2016
 * @see AppCompatActivity
 */
public class StandaloneActivity extends AppCompatActivity {


    /* ********** *
     * ATTRIBUTES *
     * ********** */


    /**
     * The list of points to click on
     */
    private List<PointsListAdapter.Point> mPointsToClickOn;

    /**
     * The action which is related to the start of this activity
     */
    private StandaloneMode mStandaloneMode;



    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * Make the standalone process click all the points defined in the JSON points file using the JSON config file
     */
    public static final String ACTION_ALL_POINTS = "pylapp.smoothclicker.android.CLICK_ON_ALL_POINTS";
    /**
     * Make the standalone process click on all the point of the JSON points file and with the use of an OCR-like system which
     * can trigger the click process according to a picture (e.g. file of a picture with the very button I want to click on)
     */
    public static final String ACTION_ALL_POINTS_ACCORDING_SCREEN = "pylapp.smoothclicker.android.CLICK_ON_ALL_POINTS_ACCORDING_SCREEN";


    private static final String LOG_TAG = StandaloneActivity.class.getSimpleName();


    /* ****************************** *
     * METHODS FROM AppCompatActivity *
     * ****************************** */

    /**
     * Triggered to create the view
     *
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninja);
        messageToUser(getString(R.string.app_name_standalone));
        String action = getIntent().getAction();
        defineStandaloneMode(action);
        Config.getAppFolder(); // Create the app's folder
        Logger.i(LOG_TAG, "Action to process: " + action);
    }


    /**
     * Triggered when the view is started
     */
    @Override
    public void onResume(){
        super.onResume();
        initConfigValues();
        NotificationsManager.getInstance(StandaloneActivity.this).refresh(StandaloneActivity.this);
        initPointsToClickOn();
        try {
            startClickingProcess();
        } catch ( IllegalStateException ise ){
            ise.printStackTrace();
            Toast.makeText(this, ise.getMessage(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    /**
     * Triggered when this view finishes
     */
    @Override
    public void finish(){
        ClickerActivity.isStandalone = false;
        super.finish();
    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Initializes the config values from a dedicated JSON file
     */
    public void initConfigValues(){

        messageToUser(getString(R.string.standalone_init_config));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            String fileName = sp.getString(SettingsActivity.PREF_KEY_FILE_CONFIG_NAME, Config.DEFAULT_FILE_JSON_CONFIG_NAME);
            JsonFileParser.instance.parseConfigFile(this, fileName);
        } catch ( NotSuitableJsonConfigFileException nsjcfe ){
            nsjcfe.printStackTrace();
            errorToUser(nsjcfe.getMessage());
        }

    }

    /**
     * Initializes the points to click on defined in a JSON file
     */
    public void initPointsToClickOn(){

        messageToUser(getString(R.string.standalone_init_points));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String fileName = sp.getString(SettingsActivity.PREF_KEY_FILE_POINTS_NAME, Config.DEFAULT_FILE_JSON_POINTS_NAME);
        try {
            int [] pointsAsArray = JsonFileParser.instance.getPointFromJsonFile(fileName);
            mPointsToClickOn = new ArrayList<>();
            for ( int i = 0; i < pointsAsArray.length; i+=2 ){
                mPointsToClickOn.add( new PointsListAdapter.Point(pointsAsArray[i], pointsAsArray[i+1]) );
            }
        } catch ( NotSuitableJsonPointsFileException e ){
            e.printStackTrace();
            errorToUser(e.getMessage());
        }

    }

    /**
     *  Starts the clicking process
     *  @throws IllegalStateException - If a problem occurs with the process
     */
    public void startClickingProcess() throws IllegalStateException {

        messageToUser(getString(R.string.standalone_start_click_process));

        // Get the points
        List<PointsListAdapter.Point> filteredPointsToClickON = new ArrayList<>();
        if ( mPointsToClickOn == null
                || mPointsToClickOn.size() <= 0 ){
            throw new IllegalStateException("Not enough point !");
        }
        filteredPointsToClickON.addAll(mPointsToClickOn);

        // Go !
        ATClicker.stop();
        ATScreenWatcher.stop();
        if ( mStandaloneMode == StandaloneMode.ALL_POINTS_WITH_CONFIG ){
            ATClicker.getInstance(this).execute(filteredPointsToClickON);
        } else {
            ATScreenWatcher.getInstance(this).execute(filteredPointsToClickON);
        }

    }

    /**
     * Defines the mode to sue for the standalone activity according to the action passed to this activity
     * @param action -
     */
    private void defineStandaloneMode( String action ){
        switch ( action ){
            case ACTION_ALL_POINTS:
                mStandaloneMode = StandaloneMode.ALL_POINTS_WITH_CONFIG;
                break;
            case ACTION_ALL_POINTS_ACCORDING_SCREEN:
                mStandaloneMode = StandaloneMode.ALL_POINTS_WITH_CONFIG_ACCORDING_SCREEN;
                break;
            default:
                errorToUser(getString(R.string.standalone_bad_action));
                break;
        }
    }

    /**
     * Notifies to the user what the app is doing
     * @param message - The message to send
     */
    private void messageToUser(String message){
        if ( message == null || message.length() <= 0 ) message = "Working...'";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Notifies to the user what the app is doing
     * @param message - The message to send
     */
    private void errorToUser( String message ){
        if ( message == null || message.length() <= 0 ) message = "Error...'";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    /* ********** *
     * INNER ENUM *
     * ********** */

    /**
     * An enum which will list the features the standalone activity has
     */
    public enum StandaloneMode {

        /**
         * Click on all the points of the JSON points file using the configuration in the config file
         */
        ALL_POINTS_WITH_CONFIG,

        /**
         * Click on all the points of the JSON points file using the configuration in the config file
         * after being triggered by the screen which matched a reference picture in the app's folder
         */
        ALL_POINTS_WITH_CONFIG_ACCORDING_SCREEN

    } // End of public enum StandaloneMode

 }

