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
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;
import android.widget.Toast;

import pylapp.smoothclicker.android.clickers.ATClicker;
import pylapp.smoothclicker.android.json.JsonConfigExporter;
import pylapp.smoothclicker.android.json.JsonConfigImporter;
import pylapp.smoothclicker.android.json.JsonFileParser;
import pylapp.smoothclicker.android.notifiers.NotificationsManager;
import pylapp.smoothclicker.android.tools.PermissionsManager;
import pylapp.smoothclicker.android.tools.ShakeToClean;
import pylapp.smoothclicker.android.tools.config.ConfigExporter;
import pylapp.smoothclicker.android.tools.config.ConfigImporter;
import pylapp.smoothclicker.android.tools.screen.ATScreenWatcher;
import pylapp.smoothclicker.android.tools.screen.WakelockManager;
import pylapp.smoothclicker.android.utils.Config;
import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.utils.ConfigStatus;
import pylapp.smoothclicker.android.tools.Logger;

import com.kyleduo.switchbutton.SwitchButton;

import com.sa90.materialarcmenu.ArcMenu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The main activity of this SmoothClicker app.
 * It shows the configuration widgets to set up the click actions
 *
 * @author Pierre-Yves Lapersonne
 * @version 2.31.0
 * @since 02/03/2016
 * @see AppCompatActivity
 * @see pylapp.smoothclicker.android.tools.ShakeToClean.ShakeToCleanCallback
 */
public class ClickerActivity extends AppCompatActivity implements ShakeToClean.ShakeToCleanCallback {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * Is the standalone mode activated ?
     */
    public static boolean isStandalone = false;


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The result code for the SelectMultiPointsActivity
     */
    private static final int SELECT_POINTS_ACTIVITY_RESULT_CODE = 0x000013;
    /**
     * The key to get the selected points
     */
    public static final String SELECT_POINTS_ACTIVITY_RESULT = "0x000014";

    private static final String LOG_TAG = ClickerActivity.class.getSimpleName();


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
        setContentView(R.layout.activity_clicker);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initInnerListeners();
        initDefaultValues();

        // Check whether we're recreating a previously destroyed instance
        if ( savedInstanceState != null ){
            SwitchButton typeOfStart = (SwitchButton) findViewById(R.id.sTypeOfStartDelayed);
            typeOfStart.setChecked(savedInstanceState.getBoolean(Config.SP_KEY_START_TYPE_DELAYED));
            EditText et = (EditText) findViewById(R.id.etDelay);
            et.setText(savedInstanceState.getString(Config.SP_KEY_DELAY));
            et = (EditText) findViewById(R.id.etTimeBeforeEachClick);
            et.setText(savedInstanceState.getString(Config.SP_KEY_TIME_GAP));
            et = (EditText) findViewById(R.id.etRepeat);
            et.setText(savedInstanceState.getString(Config.SP_KEY_REPEAT));
            SwitchCompat sc = (SwitchCompat) findViewById(R.id.scVibrateOnStart);
            sc.setChecked(savedInstanceState.getBoolean(Config.SP_KEY_VIBRATE_ON_START));
            sc = (SwitchCompat) findViewById(R.id.scVibrateOnClick);
            sc.setChecked(savedInstanceState.getBoolean(Config.SP_KEY_VIBRATE_ON_CLICK));
            sc = (SwitchCompat) findViewById(R.id.scRingOnClick);
            sc.setChecked(savedInstanceState.getBoolean(Config.SP_KEY_RING_ON_CLICK));
            sc = (SwitchCompat) findViewById(R.id.scNotifOnClick);
            sc.setChecked(savedInstanceState.getBoolean(Config.SP_KEY_NOTIF_ON_CLICK));
            sc = (SwitchCompat) findViewById(R.id.scEndlessRepeat);
            sc.setChecked(savedInstanceState.getBoolean(Config.SP_KEY_REPEAT_ENDLESS));
            et = (EditText) findViewById(R.id.etRepeat);
            et.setEnabled( ! sc.isChecked() );
            int checkedRbUnitTimeId = savedInstanceState.getInt(Config.SP_KEY_UNIT_TIME);
            RadioButton rb = (RadioButton) findViewById(checkedRbUnitTimeId);
            rb.setChecked(true);
        }

        // Define listeners for permissions through a dedicated manager
        PermissionsManager pm = PermissionsManager.instance.refreshContext(this);
        pm.createPermissionListenerForWriteExternalStorage(new PermissionsManager.PermissionGrantedCallback() {
            @Override
            public void onPermissionGranted() {
                exportConfig();
            }
        }, null, null);
        pm.createPermissionListenerForReadExternalStorage(new PermissionsManager.PermissionGrantedCallback() {
            @Override
            public void onPermissionGranted() {
                importConfig();
            }
        }, null, null);

    }

    /**
     * Triggered when the back button is pressed
     */
    @Override
    public void onBackPressed(){
        handleExit();
    }

    /**
     * Triggered to save the state
     * @param savedInstanceState -
     */
    @Override
    public void onSaveInstanceState( Bundle savedInstanceState ){

        // Get values to save

        SwitchButton sTypeOfStart = (SwitchButton) findViewById(R.id.sTypeOfStartDelayed);
        boolean isDelayed = sTypeOfStart.isChecked();

        EditText et = (EditText) findViewById(R.id.etDelay);
        int delayInS;
        if ( et.getText() == null || et.getText().toString().length() <= 0 ) delayInS = 0;
        else delayInS = Integer.parseInt(et.getText().toString());

        et = (EditText) findViewById(R.id.etTimeBeforeEachClick);
        int timeGapInS;
        if ( et.getText() == null || et.getText().toString().length() <= 0 ) timeGapInS = 0;
        else timeGapInS = Integer.parseInt(et.getText().toString());

        et = (EditText) findViewById(R.id.etRepeat);
        int repeatEach;
        if ( et.getText() == null || et.getText().toString().length() <= 0 ) repeatEach = 0;
        else repeatEach = Integer.parseInt(et.getText().toString());

        SwitchCompat sc = (SwitchCompat) findViewById(R.id.scEndlessRepeat);
        boolean isEndlessRepeat = sc.isChecked();

        sc= (SwitchCompat) findViewById(R.id.scVibrateOnStart);
        boolean isVibrateOnStart = sc.isChecked();

        sc = (SwitchCompat) findViewById(R.id.scVibrateOnClick);
        boolean isVibrateOnClick = sc.isChecked();

        sc = (SwitchCompat) findViewById(R.id.scRingOnClick);
        boolean isRingOnClick = sc.isChecked();

        sc = (SwitchCompat) findViewById(R.id.scNotifOnClick);
        boolean isNotifOnClick = sc.isChecked();

        //RadioButtonGroupTableLayout rg = (RadioButtonGroupTableLayout) findViewById(R.id.rgUnitsTime);
        RadioButtonGroupTableLayout rbgtl = (RadioButtonGroupTableLayout) findViewById(R.id.rgUnitsTime);
        int checkedRbUnitTimeId = rbgtl.getCheckedRadioButtonId();

        // Save the values

        savedInstanceState.putBoolean(Config.SP_KEY_START_TYPE_DELAYED, isDelayed);
        savedInstanceState.putInt(Config.SP_KEY_DELAY, delayInS);
        savedInstanceState.putInt(Config.SP_KEY_TIME_GAP, timeGapInS);
        savedInstanceState.putInt(Config.SP_KEY_REPEAT, repeatEach);
        savedInstanceState.putBoolean(Config.SP_KEY_REPEAT_ENDLESS, isEndlessRepeat);
        savedInstanceState.putBoolean(Config.SP_KEY_VIBRATE_ON_START, isVibrateOnStart);
        savedInstanceState.putBoolean(Config.SP_KEY_VIBRATE_ON_CLICK , isVibrateOnClick);
        savedInstanceState.putBoolean(Config.SP_KEY_RING_ON_CLICK , isRingOnClick);
        savedInstanceState.putBoolean(Config.SP_KEY_NOTIF_ON_CLICK , isNotifOnClick);
        savedInstanceState.putInt(Config.SP_KEY_UNIT_TIME, checkedRbUnitTimeId);

        super.onSaveInstanceState(savedInstanceState);

    }

    /**
     * Triggered to create the options menu
     * @param menu -
     * @return boolean - Always true
     */
    @Override
    public boolean onCreateOptionsMenu( Menu menu ){
        getMenuInflater().inflate(R.menu.menu_clicker, menu);
        return true;
    }

    /**
     * Triggered when the activity results
     * @param requestCode -
     * @param resultCode -
     * @param data -
     */
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ){
        switch ( requestCode ){
            case SELECT_POINTS_ACTIVITY_RESULT_CODE:
                if ( resultCode == Activity.RESULT_OK ) {
                    ArrayList<Integer> alp = data.getIntegerArrayListExtra(SELECT_POINTS_ACTIVITY_RESULT);
                    handleMultiPointResult(alp);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Triggered when an itemahs been selected on the options menu
     * @param item -
     * @return boolean -
     */
    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        int id = item.getItemId();
        switch ( id ){
            case R.id.action_clean_points:
                handleMultiPointResult( null );
                break;
            case R.id.action_clean_all:
                initDefaultValues();
                break;
            case R.id.action_settings:
                startSettingsActivity();
                break;
            case R.id.action_exit:
                handleExit();
                break;
            case R.id.action_export:
                updateConfig();
                if ( PermissionsManager.isApi23OrHigher() ){
                    PermissionsManager.instance.getAndGoWithPermissionWriteExternalStorage();
                } else {
                    exportConfig();
                }
                break;
            case R.id.action_import:
                if ( PermissionsManager.isApi23OrHigher() ){
                    PermissionsManager.instance.getAndGoWithPermissionReadExternalStorage();
                } else {
                    importConfig();
                }
                break;
            case R.id.action_configuration:
                break;
            case R.id.action_standalone:
                handleStandaloneMode();
                break;
            default:
                showInSnackbarWithoutAction(getString(R.string.error_not_implemented));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * To call to finish this main activity and quit the app
     */
    @Override
    public void finish(){
        if ( ! isStandalone ){
            SplashScreenActivity.sIsFirstLaunch = true;
            if (ATClicker.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.RUNNING
                    || ATClicker.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.PENDING
                    || ATScreenWatcher.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.RUNNING
                    || ATScreenWatcher.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.PENDING){
                stopAllProcesses();
            }
            NotificationsManager.getInstance(this).stopAllNotifications();
            WakelockManager.instance.releaseWakelock();
        }
        super.finish();
    }

    /**
     * Triggered when the activity will be destroyed
     */
    @Override
    public void onDestroy(){
        ATScreenWatcher.cleanTempFile();
        super.onDestroy();
    }

    /**
     * Triggered when the activity is resuming
     */
    @Override
    public void onResume(){
        super.onResume();
        ShakeToClean.getInstance(this).register();
        ShakeToClean.getInstance(this).registerCallback(this);
    }

    /**
     * Triggered when the activity is pausing
     */
    @Override
    public void onPause(){
        super.onPause();
        ShakeToClean.getInstance(this).unregister();
        ShakeToClean.getInstance(this).unregisterCallback();
    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Gets the configuration from the widgets and backs it up
     * @return ConfigStatus - The state of the config
     */
    private ConfigStatus updateConfig(){

        Logger.d(LOG_TAG, "Updates configuration");

        // Get the defined values
        SwitchButton sTypeOfStart = (SwitchButton) findViewById(R.id.sTypeOfStartDelayed);
        boolean isDelayed = sTypeOfStart.isChecked();

        RadioButtonGroupTableLayout rg = (RadioButtonGroupTableLayout) findViewById(R.id.rgUnitsTime);
        int checkedRbUnitTimeId = rg.getCheckedRadioButtonId();

        EditText et = (EditText) findViewById(R.id.etDelay);
        if ( et.getText() == null ) return ConfigStatus.DELAY_NOT_DEFINED;
        int delayInS;
        if ( et.getText().toString().length() <= 0 ) delayInS = 0;
        else delayInS = Integer.parseInt(et.getText().toString());

        et = (EditText) findViewById(R.id.etTimeBeforeEachClick);
        if ( et.getText() == null ) return ConfigStatus.TIME_GAP_NOT_DEFINED;
        int timeGapInS;
        if ( et.getText().toString().length() <= 0 ) timeGapInS = 0;
        else timeGapInS = Integer.parseInt(et.getText().toString());

        et = (EditText) findViewById(R.id.etRepeat);
        if ( et.getText() == null ) return ConfigStatus.REPEAT_NOT_DEFINED;
        int repeatEach;
        if ( et.getText().toString().length() <= 0 ) repeatEach = 0;
        else repeatEach = Integer.parseInt(et.getText().toString());

        SwitchCompat sc = (SwitchCompat) findViewById(R.id.scEndlessRepeat);
        boolean isEndlessRepeat = sc.isChecked();

        sc = (SwitchCompat) findViewById(R.id.scVibrateOnStart);
        boolean isVibrateOnStart = sc.isChecked();

        sc = (SwitchCompat) findViewById(R.id.scVibrateOnClick);
        boolean isVibrateOnClick = sc.isChecked();

        sc = ( SwitchCompat) findViewById(R.id.scRingOnClick);
        boolean isRingOnClick = sc.isChecked();

        sc = ( SwitchCompat) findViewById(R.id.scNotifOnClick);
        boolean isDisplayNotifs = sc.isChecked();

        // Update the shared preferences
        SharedPreferences sp = getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Config.SP_KEY_START_TYPE_DELAYED, isDelayed);
        editor.putInt(Config.SP_KEY_DELAY, delayInS);
        editor.putInt(Config.SP_KEY_TIME_GAP, timeGapInS);
        editor.putInt(Config.SP_KEY_REPEAT, repeatEach);
        editor.putBoolean(Config.SP_KEY_REPEAT_ENDLESS, isEndlessRepeat);
        editor.putBoolean(Config.SP_KEY_VIBRATE_ON_START, isVibrateOnStart);
        editor.putBoolean(Config.SP_KEY_VIBRATE_ON_CLICK, isVibrateOnClick);
        editor.putBoolean(Config.SP_KEY_RING_ON_CLICK, isRingOnClick);
        editor.putBoolean(Config.SP_KEY_NOTIF_ON_CLICK, isDisplayNotifs);
        editor.putInt(Config.SP_KEY_UNIT_TIME, checkedRbUnitTimeId);

        editor.apply();

        return ConfigStatus.TIME_GAP_NOT_DEFINED.READY;

    }

    /**
     * Exports the configuration in JSON files
     */
    private void exportConfig(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String pointsFileName = sp.getString(SettingsActivity.PREF_KEY_FILE_POINTS_NAME, Config.DEFAULT_FILE_JSON_POINTS_NAME);
        String configFileName = sp.getString(SettingsActivity.PREF_KEY_FILE_CONFIG_NAME, Config.DEFAULT_FILE_JSON_CONFIG_NAME);
        ConfigExporter exporter = new JsonConfigExporter(pointsFileName, configFileName);

        sp = getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        exporter.setStartDelayed(sp.getBoolean(Config.SP_KEY_START_TYPE_DELAYED, Config.DEFAULT_START_DELAYED));
        int checkedRbUnitTimeId = sp.getInt(Config.SP_KEY_UNIT_TIME, Config.DEFAULT_TIME_UNIT_SELECTION);
        switch ( checkedRbUnitTimeId ){
            case R.id.rbUnitTimeH:
                exporter.setUnitTime(JsonConfigExporter.UnitTime.HOUR);
                break;
            case R.id.rbUnitTimeM:
                exporter.setUnitTime(JsonConfigExporter.UnitTime.MINUTE);
                break;
            case R.id.rbUnitTimeS:
            default:
                exporter.setUnitTime(JsonConfigExporter.UnitTime.SECOND);
                break;
        }
        exporter.setDelay(sp.getInt(Config.SP_KEY_DELAY, Integer.parseInt(Config.DEFAULT_DELAY)));
        exporter.setTimeGap(sp.getInt(Config.SP_KEY_TIME_GAP, Integer.parseInt(Config.DEFAULT_TIME_GAP)));
        exporter.setRepeat(sp.getInt(Config.SP_KEY_REPEAT, Integer.parseInt(Config.DEFAULT_REPEAT)));
        exporter.setEndlessRepeat(sp.getBoolean(Config.SP_KEY_REPEAT_ENDLESS, Config.DEFAULT_REPEAT_ENDLESS));
        exporter.setVibrateOnStart(sp.getBoolean(Config.SP_KEY_VIBRATE_ON_START, Config.DEFAULT_VIBRATE_ON_START));
        exporter.setVibrateOnCLick(sp.getBoolean(Config.SP_KEY_VIBRATE_ON_CLICK, Config.DEFAULT_VIBRATE_ON_CLICK));
        exporter.setRingOnClick(sp.getBoolean(Config.SP_KEY_RING_ON_CLICK, Config.DEFAULT_RING_ON_CLICK));
        exporter.setNotificationOnCLick(sp.getBoolean(Config.SP_KEY_NOTIF_ON_CLICK, Config.DEFAULT_NOTIF_ON_CLICK));

        PointsListAdapter pla = (PointsListAdapter) ((Spinner) findViewById(R.id.sPointsToClick)).getAdapter();
        List<PointsListAdapter.Point> lp;
        if (pla == null || pla.getList().size() <= 0) lp = new ArrayList<>();
        else lp = pla.getList();
        exporter.setPointsToClickOn(lp);

        try {
            exporter.writeConfig();
            showInSnackbar(getString(R.string.info_export_success), getString(R.string.snackbar_see_config_file), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        File file = JsonFileParser.getPointsFile();
                        Uri path = Uri.fromFile(file);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, "application/json");
                        startActivity(intent);
                    } catch ( ActivityNotFoundException anfe ){
                        anfe.printStackTrace();
                        Toast.makeText(ClickerActivity.this, getString(R.string.error_nothing_to_open_json), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch ( ConfigExporter.ConfigExportException cee ){
            cee.printStackTrace();
            showInSnackbarWithoutAction(getString(R.string.info_export_fail));
            Toast.makeText(this, cee.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Imports the configuration from a JSON file and updates the GUI and the preferences
     */
    private void importConfig(){

        // Read the config file
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String configFileName = sp.getString(SettingsActivity.PREF_KEY_FILE_CONFIG_NAME, Config.DEFAULT_FILE_JSON_CONFIG_NAME);
        String pointsFileName = sp.getString(SettingsActivity.PREF_KEY_FILE_POINTS_NAME, Config.DEFAULT_FILE_JSON_POINTS_NAME);
        ConfigImporter importer = new JsonConfigImporter(pointsFileName, configFileName);
        try {
            importer.readConfig();
        } catch ( ConfigImporter.ConfigImportException cie ){
            cie.printStackTrace();
            showInSnackbarWithoutAction(getString(R.string.info_import_fail));
            Toast.makeText(this, cie.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        // Update the GUI
        ConfigImporter.UnitTime unitTime = importer.getUnitTime();
        RadioButton unitTimeRadioButton = null;
        switch ( unitTime ){
            case MILLISECOND:
                unitTimeRadioButton = (RadioButton) findViewById(R.id.rbUnitTimeMs);
                break;
            case HOUR:
                unitTimeRadioButton = (RadioButton) findViewById(R.id.rbUnitTimeH);
                break;
            case MINUTE:
                unitTimeRadioButton = (RadioButton) findViewById(R.id.rbUnitTimeM);
                break;
            case SECOND:
            default:
                unitTimeRadioButton = (RadioButton) findViewById(R.id.rbUnitTimeS);
                break;
        }
        RadioButtonGroupTableLayout rg = (RadioButtonGroupTableLayout) findViewById(R.id.rgUnitsTime);
        rg.reset();
        unitTimeRadioButton.setChecked(true);
        rg.setActiveRadioButton(unitTimeRadioButton);
        SwitchButton typeOfStart = (SwitchButton) findViewById(R.id.sTypeOfStartDelayed);
        typeOfStart.setChecked(importer.getStartDelayed());
        EditText et = (EditText) findViewById(R.id.etDelay);
        et.setText(importer.getDelay()+"");
        et = (EditText) findViewById(R.id.etTimeBeforeEachClick);
        et.setText(importer.getTimeGap()+"");
        et = (EditText) findViewById(R.id.etRepeat);
        et.setText(importer.getRepeat() + "");
        SwitchCompat sc = (SwitchCompat) findViewById(R.id.scVibrateOnStart);
        sc.setChecked(importer.getVibrateOnStart());
        sc = (SwitchCompat) findViewById(R.id.scVibrateOnClick);
        sc.setChecked(importer.getVibrateOnClick());
        sc = (SwitchCompat) findViewById(R.id.scRingOnClick);
        sc.setChecked(importer.getRingOnClick());
        sc = (SwitchCompat) findViewById(R.id.scNotifOnClick);
        sc.setChecked(importer.getNotificationOnCLick());
        SwitchCompat sw = (SwitchCompat) findViewById(R.id.scEndlessRepeat);
        sw.setChecked(importer.getEndlessRepeat());
        et.setEnabled(!sw.isChecked());

        ArrayList<Integer> coordsAsXY = new ArrayList<>();
        List<PointsListAdapter.Point> lp = importer.getPointsToClickOn();
        for ( PointsListAdapter.Point p : lp ){
            coordsAsXY.add(p.x);
            coordsAsXY.add(p.y);
        }
        handleMultiPointResult(coordsAsXY);

        // Update the shared preferences
        sp = getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Config.SP_KEY_START_TYPE_DELAYED, importer.getStartDelayed());
        editor.putInt(Config.SP_KEY_DELAY, importer.getDelay());
        editor.putInt(Config.SP_KEY_TIME_GAP, importer.getTimeGap());
        editor.putInt(Config.SP_KEY_REPEAT, importer.getRepeat());
        editor.putBoolean(Config.SP_KEY_REPEAT_ENDLESS, importer.getEndlessRepeat());
        editor.putBoolean(Config.SP_KEY_VIBRATE_ON_START, importer.getVibrateOnStart());
        editor.putBoolean(Config.SP_KEY_VIBRATE_ON_CLICK, importer.getVibrateOnClick());
        editor.putBoolean(Config.SP_KEY_RING_ON_CLICK, importer.getRingOnClick());
        editor.putBoolean(Config.SP_KEY_NOTIF_ON_CLICK, importer.getNotificationOnCLick());
        editor.apply();

        showInSnackbarWithoutAction(getString(R.string.info_import_success));
    }

    /**
     * Runs the clicking process
     */
    private void startClickingProcess() {

        // Check if we make an endless repeat...
        SharedPreferences sp = getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, Config.SP_ACCESS_MODE);
        boolean isEndlessRepeat = sp.getBoolean(Config.SP_KEY_REPEAT_ENDLESS, Config.DEFAULT_REPEAT_ENDLESS);

        PointsListAdapter pla = (PointsListAdapter) ((Spinner) findViewById(R.id.sPointsToClick)).getAdapter();
        if ( pla /* à tartes */== null
                || pla.getList().size() <= 0
                || (pla.getList().size() == 1 && pla.get(0).x == PointsListAdapter.Point.UNDEFINED_X) ){
            displayMessage(MessageTypes.NO_CLICK_DEFINED);
            return;
        }

        final List<PointsListAdapter.Point> lp = pla.getList();

        if ( isEndlessRepeat ){

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.warning_hazard_repeat_endless_title))
                    .setMessage(getString(R.string.warning_hazard_repeat_endless_content))
                    .setPositiveButton(R.string.warning_hazard_repeat_endless_yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ){
                            // Go !
                            ATClicker.stop();
                            ATClicker.getInstance(ClickerActivity.this).execute(lp);
                        }
                    })
                    .setNegativeButton(R.string.warning_hazard_repeat_endless_no, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ){
                            // Do nothing
                        }
                    })
                    .show();

        } else {

            // Go !
            ATClicker.stop();
            ATClicker.getInstance(this).execute(lp);

        }

    }

    /**
     * Stops the running process
     */
    private void stopClickingProcess(){
        Logger.d(LOG_TAG, "Stops the clicking process");
        if ( ! ATClicker.stop() ){
            displayMessage(MessageTypes.WAS_NOT_WORKING);
        }
    }

    /**
     * Stops the running processes, i.e. the process which makes the clicks, and the process which looks on the screen to trigger the previous one
     */
    private void stopAllProcesses(){
        Logger.d(LOG_TAG, "Stops all the processes");
        if ( ! ATClicker.stop() ){
            displayMessage(MessageTypes.WAS_NOT_WORKING);
        }
        ATScreenWatcher.stop();
        isStandalone = false;
    }

    /**
     * Initializes the default values
     */
    private void initDefaultValues(){

        Logger.d(LOG_TAG, "Initializes the default values");

        RadioButton rbUnitTime = (RadioButton) findViewById(Config.DEFAULT_TIME_UNIT_SELECTION);
        rbUnitTime.setChecked(true);
        SwitchButton typeOfStart = (SwitchButton) findViewById(R.id.sTypeOfStartDelayed);
        typeOfStart.setChecked(Config.DEFAULT_START_DELAYED);
        EditText et = (EditText) findViewById(R.id.etDelay);
        et.setText(Config.DEFAULT_DELAY);
        et = (EditText) findViewById(R.id.etTimeBeforeEachClick);
        et.setText(Config.DEFAULT_TIME_GAP);
        et = (EditText) findViewById(R.id.etRepeat);
        et.setText(Config.DEFAULT_REPEAT);
        SwitchCompat sc = (SwitchCompat) findViewById(R.id.scVibrateOnStart);
        sc.setChecked(Config.DEFAULT_VIBRATE_ON_START);
        sc = (SwitchCompat) findViewById(R.id.scVibrateOnClick);
        sc.setChecked(Config.DEFAULT_VIBRATE_ON_CLICK);
        sc = (SwitchCompat) findViewById(R.id.scRingOnClick);
        sc.setChecked(Config.DEFAULT_RING_ON_CLICK);
        sc = (SwitchCompat) findViewById(R.id.scNotifOnClick);
        sc.setChecked(Config.DEFAULT_NOTIF_ON_CLICK);
        sc = (SwitchCompat) findViewById(R.id.scEndlessRepeat);
        sc.setChecked(Config.DEFAULT_REPEAT_ENDLESS);

        handleMultiPointResult( null ); // Make the spinner of points to click empty

    }

    /**
     * Displays a message in the snackbar and the logger
     * @param mt - The type of the message to display
     */
    private void displayMessage( MessageTypes mt ){
        String m = null;
        switch ( mt ){
            case START_PROCESS:
                m = ClickerActivity.this.getString(R.string.info_message_start);
                Logger.d("SmoothClicker", m);
                break;
            case STOP_PROCESS:
                m = ClickerActivity.this.getString(R.string.info_message_stop);
                Logger.d("SmoothClicker", m);
                break;
            case SU_GRANTED:
                m = ClickerActivity.this.getString(R.string.info_message_su_granted);
                Logger.i("SmoothClicker", m);
                break;
            case SU_NOT_GRANTED:
                m = ClickerActivity.this.getString(R.string.error_message_su_not_granted);
                Logger.e("SmoothClicker", m);
                break;
            case NOT_IMPLEMENTED:
                m = ClickerActivity.this.getString(R.string.error_not_implemented);
                Logger.e("SmoothClicker", m);
                break;
            case SU_GRANT:
                m = ClickerActivity.this.getString(R.string.info_message_request_su);
                Logger.d("SmoothClicker", m);
                break;
            case NEW_CLICK:
                m = ClickerActivity.this.getString(R.string.info_message_new_point);
                Logger.d("SmoothClicker", m);
                break;
            case NO_CLICK_DEFINED:
                m = ClickerActivity.this.getString(R.string.error_message_no_click_defined);
                Logger.d("SmoothClicker", m);
                break;
            case WAS_NOT_WORKING:
                m = ClickerActivity.this.getString(R.string.error_message_was_not_working);
                Logger.d("SmoothClicker", m);
                break;
            default:
                m = null;
                break;
        }
        if ( m == null || m.length() <= 0 ) return;
        showInSnackbarWithoutAction(m);
    }

    /**
     * Displays a "do you want to exit" pop-up, and if the user clicks on OK, will stop all clicks process and finish.
     */
    private void handleExit(){

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.message_confirm_exit_label))
                .setMessage(getString(R.string.message_confirm_exit_content))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int which ){
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int which ){
                        // Do nothing
                    }
                })
                .show();

    }

    /**
     * Handles the standalone mode (when the user has clicked on the dedicated menu item).
     * Disable the mode if is was running.
     * Displays a list with the action to process in such mode.
     * When the user chooses an option is this list, finish this activity and starts the standalone one with the dedicated intent.
     */
    private void handleStandaloneMode(){

        // If we WERE in standalone mode: stop it
        if ( isStandalone ) {
            if (ATClicker.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.RUNNING
                    || ATClicker.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.PENDING
                    || ATScreenWatcher.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.RUNNING
                    || ATScreenWatcher.getInstance(ClickerActivity.this).getStatus() == AsyncTask.Status.PENDING) {
                stopAllProcesses();
            }
            NotificationsManager.getInstance(this).stopAllNotifications();
            showInSnackbarWithoutAction(getString(R.string.info_standalone_disactivated));
            isStandalone = false;
            return;
        }

        // If we WILL be in standalone mode: start it

        // Show to the user the list of action : use a dedicated activity, as a dialog, with the choices for the standalone mode and the items for picture learning
        StandaloneModeDialog dialog = new StandaloneModeDialog(this);
        dialog.setPositiveButtonListener(new StandaloneModeDialog.OnPositiveButtonListener() {
            @Override
            public void onPositiveButtonClick(StandaloneActivity.StandaloneMode userSelection) {

                // Notify the user the process will start
                showInSnackbarWithoutAction(getString(R.string.info_standalone_activated));
                isStandalone = true;

                final String intentAction;
                // Starts the process
                switch (userSelection) {
                    case ALL_POINTS_WITH_CONFIG:
                        intentAction = StandaloneActivity.ACTION_ALL_POINTS;
                        break;
                    case ALL_POINTS_WITH_CONFIG_ACCORDING_SCREEN:
                        intentAction = StandaloneActivity.ACTION_ALL_POINTS_ACCORDING_SCREEN;
                        break;
                    default:
                        intentAction = "";
                        break;
                }

                // Start the standalone activity
                Intent standaloneActivityIntent = new Intent(ClickerActivity.this, StandaloneActivity.class);
                standaloneActivityIntent.setAction(intentAction);
                startActivity(standaloneActivityIntent);

                // Finish smoothly this activity to let the standalone one work
                finish();
            }
        }); // End of new StandaloneModeDialog.OnPositiveButtonListener()

        dialog.show();

    }

    /**
     * Handles the list of coordinates of the points to click on.
     * Will update the list showing to the user the points.
     * Will update the config so as to allow the ATClicker to click on all these points.
     *
     * @param coords - The list of X/Y values of the points to click on as {x0, y0, x1, y2, ..., xN, yN}
     *               If null, the list will be made empty
     */
    private void handleMultiPointResult( ArrayList<Integer> coords ){

        final Spinner s = (Spinner) findViewById(R.id.sPointsToClick);
        s.setAdapter(null); // Clean the list view

        if ( coords == null || coords.size() <= 0 ){
            s.setAdapter(new PointsListAdapter(this, new ArrayList<Integer>()));
            return;
        }

        s.setAdapter(new PointsListAdapter(this, coords));
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s.setSelection(0); // The item 0 is a label saying to the user the account of items in the list
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                s.setSelection(0); // The item 0 is a label saying to the user the account of items in the list
            }
        });
    }

    /**
     * Starts the settings activity
     */
    private void startSettingsActivity(){
        startActivity(new Intent(ClickerActivity.this, SettingsActivity.class));
    }

    /**
     * Starts the activity which allows the user to select a point on its screen
     */
    private void startSelectPointActivity(){
        Intent i = new Intent(ClickerActivity.this, SelectMultiPointsActivity.class);
        startActivityForResult(i, SELECT_POINTS_ACTIVITY_RESULT_CODE);
    }

    /**
     * Displays in the snack bar a message
     * @param message - The string to display. Will do nothing if null or empty
     */
    private void showInSnackbarWithoutAction( String message ){
        if ( message == null || message.length() <= 0 ) return;
        View v = findViewById(R.id.clickerActivityMainLayout);
        Snackbar.make(v, message, Snackbar.LENGTH_LONG).setAction("", null).show();
    }

    /**
     * Displays in the snack bar a message with dedicated action and callback
     * @param message - The string to display. Will do nothing if null or empty
     * @param action - The action
     * @param callback - The callback to trigger when click on the action
     */
    private void showInSnackbar( String message, String action, View.OnClickListener callback ){
        if ( message == null || message.length() <= 0 ) return;
        View v = findViewById(R.id.clickerActivityMainLayout);
        Snackbar.make(v, message, Snackbar.LENGTH_LONG).setAction(action, callback).show();
    }

    /**
     * Requests the SU grant by starting a SU process which will trigger
     * the "SU grant" system window
     */
    private void requestSuGrant(){
        try {
            Logger.d(LOG_TAG, "Get 'su' process...");
            Runtime.getRuntime().exec("su");
        } catch ( IOException e ){
            Logger.e(LOG_TAG, "Exception thrown during 'su' : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "An error occurs during SU grant : "+e.getMessage(), Toast.LENGTH_LONG).show();
            String s = getString(R.string.error_su_missing);
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initializes the listeners on the widgets
     */
    private void initInnerListeners(){

        // The button to trigger the click(s) process
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabStart);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayMessage(MessageTypes.START_PROCESS);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateConfig();
                NotificationsManager.getInstance(ClickerActivity.this).refresh(ClickerActivity.this);
                ArcMenu arcMenu = (ArcMenu) findViewById(R.id.fabAction);
                arcMenu.toggleMenu();
                startClickingProcess();
            }
        });

        // The stop button
        fab = (FloatingActionButton) findViewById(R.id.fabStop);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayMessage(MessageTypes.STOP_PROCESS);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                stopAllProcesses();
            }
        });

        // The button to add a new point to click on
        fab = (FloatingActionButton) findViewById(R.id.fabSelectPoint);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayMessage(MessageTypes.NEW_CLICK);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcMenu fabAction = (ArcMenu) findViewById(R.id.fabAction);
                if ( fabAction.isMenuOpened() ) fabAction.toggleMenu();
                startSelectPointActivity();
            }
        });

        // The button to request SU grant
        fab = (FloatingActionButton) findViewById(R.id.fabRequestSuGrant);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayMessage(MessageTypes.SU_GRANT);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSuGrant();
            }
        });

        // The switch button about the type of start
        // If checked, enabled the filed for the delay
        SwitchButton sTypeOfStart = (SwitchButton) findViewById(R.id.sTypeOfStartDelayed);
        sTypeOfStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ){
                EditText etDelay = (EditText) findViewById(R.id.etDelay);
                etDelay.setEnabled(isChecked);
                EditText etR = (EditText) findViewById(R.id.etRepeat);
                if ( "666".equals(etR.getText().toString()) ) Toast.makeText(ClickerActivity.this, "✿✿✿✿ ʕ •ᴥ•ʔ/ ︻デ═一 Hotter Than Hell !", Toast.LENGTH_SHORT).show();
            }
        });

        // The endless repeat
        SwitchCompat sc = (SwitchCompat) findViewById(R.id.scEndlessRepeat);
        sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ){
                EditText etRepeat = (EditText) findViewById(R.id.etRepeat);
                etRepeat.setEnabled( ! isChecked );
            }
        });

        // Some things for accessibility
        TextView tv = (TextView) findViewById(R.id.tvDelay);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                EditText et = (EditText) findViewById(R.id.etDelay);
                et.requestFocus();
            }
        });

        tv = (TextView) findViewById(R.id.tvTimeBeforeEachClick);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                EditText et = (EditText) findViewById(R.id.etTimeBeforeEachClick);
                et.requestFocus();
            }
        });

        tv = (TextView) findViewById(R.id.tvRepeat);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                EditText et = (EditText) findViewById(R.id.etRepeat);
                et.requestFocus();
            }
        });

        tv = (TextView) findViewById(R.id.tvEndlessRepeat);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                SwitchCompat sc = (SwitchCompat) findViewById(R.id.scEndlessRepeat);
                sc.setChecked(!sc.isChecked());
            }
        });

        tv = (TextView) findViewById(R.id.tvVibrateOnStart);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                SwitchCompat sc = (SwitchCompat) findViewById(R.id.scVibrateOnStart);
                sc.setChecked(!sc.isChecked());
            }
        });

        tv = (TextView) findViewById(R.id.tvVibrateOnClick);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                SwitchCompat sc = (SwitchCompat) findViewById(R.id.scVibrateOnClick);
                sc.setChecked(!sc.isChecked());
            }
        });

        tv = (TextView) findViewById(R.id.tvRingOnClick);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                SwitchCompat sc = (SwitchCompat) findViewById(R.id.scRingOnClick);
                sc.setChecked(!sc.isChecked());
            }
        });

        tv = (TextView) findViewById(R.id.tvNotifOnClick);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                SwitchCompat sc = (SwitchCompat) findViewById(R.id.scNotifOnClick);
                sc.setChecked(!sc.isChecked());
            }
        });

    }


    /* ********************************* *
     * METHODS FROM ShakeToCleanCallback *
     * ********************************* */

    /**
     * Triggered when a shake to clean event has been thrown
     */
    @Override
    public void shakeToClean() {
        // Check if the shake to clean option is enabled
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SettingsActivity.PREF_KEY_SHAKE_TO_CLEAN, Config.DEFAULT_SHAKE_TO_CLEAN)) {
            Toast.makeText(this, getString(R.string.message_reinit_config), Toast.LENGTH_SHORT).show();
            initDefaultValues();
            ArcMenu fabAction = (ArcMenu) findViewById(R.id.fabAction);
            if ( fabAction.isMenuOpened() ) fabAction.toggleMenu();
        }
    }

    /* *********** *
     * INNER ENUMS *
     * *********** */

    /**
     * The type of messages to display
     */
    private enum MessageTypes {
        SU_GRANTED,
        SU_NOT_GRANTED,
        START_PROCESS,
        STOP_PROCESS,
        NEW_CLICK,
        NOT_IMPLEMENTED,
        SU_GRANT,
        NO_CLICK_DEFINED,
        WAS_NOT_WORKING
    } // private enum MESSAGE_TYPE

}
