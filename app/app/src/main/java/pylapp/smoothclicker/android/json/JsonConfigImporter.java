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

package pylapp.smoothclicker.android.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pylapp.smoothclicker.android.tools.config.ConfigImporter;
import pylapp.smoothclicker.android.utils.Config;
import pylapp.smoothclicker.android.views.PointsListAdapter;

/**
 * Class which consists on importing the configuration of the app from a JSON file
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.3.0
 * @since 27/05/2016
 * @see ConfigImporter
 */
public class JsonConfigImporter implements ConfigImporter {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The unit time in use (ms, s, m or h)
     */
    private UnitTime mUnitTime;

    /**
     * If the start is delayed
     */
    private boolean mIsStartDelayed;

    /**
     * The delay of the start in seconds
     */
    private int mDelay;

    /**
     * The time to wait between each click in seconds
     */
    private int mTimeGap;

    /**
     * The amount of repeat to process
     */
    private int mRepeat;

    /**
     * If the repeat is an endless one
     */
    private boolean mIsEndlessRepeat;

    /**
     * If the device vibrates on start
     */
    private boolean mVibrateOnStart;

    /**
     * If the device vibrates on click
     */
    private boolean mVibrateOnClick;

    /**
     * If the device rings on each click
     */
    private boolean mRingOnClick;

    /**
     * If the device must have a notification on click
     */
    private boolean mNotifyOnClick;

    /**
     * The list of points to click on
     */
    private List<PointsListAdapter.Point> mPoints;

    /**
     * The name of the points file
     */
    private String mPointsFileName;

    /**
     * The name of the config file
     */
    private String mConfigFileName;

    //private static final String LOG_TAG = JsonConfigImporter.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     *
     * @param pointsFileName - The name of the points file to use
     * @param configFileName - The name of the config file to use
     */
    public JsonConfigImporter( String pointsFileName, String configFileName ){
        super();
        if ( pointsFileName == null || pointsFileName.length() <= 0 ) throw new IllegalArgumentException("The points file's name parameter must not be null nor empty");
        if ( configFileName == null || configFileName.length() <= 0 ) throw new IllegalArgumentException("The config file's name parameter must not be null nor empty");
        mPointsFileName = pointsFileName;
        mConfigFileName = configFileName;
    }


    /* *************************** *
     * METHODS FROM ConfigImporter *
     * *************************** */

    /**
     *
     * @return UnitTime - The unit time in use
     */
    @Override
    public UnitTime getUnitTime(){
        return mUnitTime;
    }

    /**
     *
     * @return boolean - The start delayed flag
     */
    @Override
    public boolean getStartDelayed(){
        return mIsStartDelayed;
    }

    /**
     *
     * @return int - The delay (in seconds)
     */
    @Override
    public int getDelay(){
        return mDelay;
    }

    /**
     *
     * @return int - The time gap between each click (in seconds)
     */
    @Override
    public int getTimeGap(){
        return mTimeGap;
    }

    /**
     *
     * @return int - The amount of repeat to do
     */
    @Override
    public int getRepeat(){
        return mRepeat;
    }

    /**
     *
     * @return boolean - The flag about the endless repeat
     */
    @Override
    public boolean getEndlessRepeat() {
        return mIsEndlessRepeat;
    }

    /**
     *
     * @return boolean - The vibrate on start flag
     */
    @Override
    public boolean getVibrateOnStart(){
        return mVibrateOnStart;
    }

    /**
     *
     * @return boolean - The vibrate on click flag
     */
    @Override
    public boolean getVibrateOnClick(){
        return mVibrateOnClick;
    }

    /**
     *
     * @return boolean - The ring on click flag
     */
    @Override
    public boolean getRingOnClick() {
        return mRingOnClick;
    }

    /**
     *
     * @return boolean - The notification click flag
     */
    @Override
    public boolean getNotificationOnCLick(){
        return mNotifyOnClick;
    }

    /**
     *
     * @return List<PointsListAdapter.Point> - The lsit of points to click on
     */
    @Override
    public List<PointsListAdapter.Point> getPointsToClickOn() {
        return mPoints;
    }

    /**
     * Reads the configuration from the JSON file
     * @throws ConfigImportException - When something wrong occurred during the import
     */
    @Override
    public void readConfig() throws ConfigImportException {
        readAppConfig();
        readPoints();
    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Reads the app config to apply from the dedicated config file
     * @throws ConfigImportException - When something wrong occurs
     */
    private void readAppConfig() throws ConfigImportException {

        JSONObject jsonData = null;

        // Get the file, its content and parse it
        File appDir = Config.getAppFolder();
        File file = new File(appDir.getAbsolutePath()+"/"+ mConfigFileName);
        try {
            InputStream is = new FileInputStream( file );
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, "UTF-8");
            jsonData = new JSONObject(jsonString);
        } catch ( IOException | JSONException e  ){
            e.printStackTrace();
            throw new ConfigImportException("A problem occurs with import of the config file: "+e.getMessage());
        }

        // Get the values in JSON
        try {
            String unitTime = jsonData.getString(JsonFileParser.JSON_OBJECT_UNIT_TIME);
            switch ( unitTime ){
                case "ms":
                    mUnitTime = UnitTime.MILLISECOND;
                    break;
                case "m":
                    mUnitTime = UnitTime.MINUTE;
                    break;
                case "h":
                    mUnitTime = UnitTime.HOUR;
                    break;
                case "s":
                default:
                    mUnitTime = UnitTime.SECOND;
                    break;
            }
            mIsStartDelayed = jsonData.getBoolean(JsonFileParser.JSON_OBJECT_DELAYED_START);
            mDelay= jsonData.getInt(JsonFileParser.JSON_OBJECT_DELAY);
            mTimeGap = jsonData.getInt(JsonFileParser.JSON_OBJECT_TIME_GAP);
            mRepeat = jsonData.getInt(JsonFileParser.JSON_OBJECT_REPEAT);
            mIsEndlessRepeat = jsonData.getBoolean(JsonFileParser.JSON_OBJECT_ENDLESS_REPEAT);
            mVibrateOnStart = jsonData.getBoolean(JsonFileParser.JSON_OBJECT_VIBRATE_ON_START);
            mVibrateOnClick = jsonData.getBoolean(JsonFileParser.JSON_OBJECT_VIBRATE_ON_CLICK);
            mRingOnClick = jsonData.getBoolean(JsonFileParser.JSON_OBJECT_RING);
            mNotifyOnClick = jsonData.getBoolean(JsonFileParser.JSON_OBJECT_NOTIFICATIONS);
        } catch ( JSONException jsone ){
            jsone.printStackTrace();
            throw new ConfigImportException("A problem occurs with import of the config file: "+jsone.getMessage());
        }

    }

    /**
     * Reads the points to click on from the dedicated JSON file
     * @throws ConfigImportException - When something wrong occurs
     */
    private void readPoints() throws ConfigImportException {

        mPoints = new ArrayList<>();

        JSONObject jsonData = null;

        // Get the file, its content and parse it
        File appDir = Config.getAppFolder();
        File file = new File(appDir.getAbsolutePath()+"/"+ mPointsFileName);
        try {
            InputStream is = new FileInputStream( file );
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, "UTF-8");
            jsonData = new JSONObject(jsonString);
        } catch ( IOException | JSONException e  ){
            e.printStackTrace();
            throw new ConfigImportException("A problem occurs with import of the points file: "+e.getMessage());
        }

        // Store the parsed data in the list of points
        try {
            JSONArray points = jsonData.getJSONArray(JsonFileParser.JSON_ARRAY_POINTS);
            for ( int i = 0; i < points.length(); i++ ){
                JSONObject point = points.getJSONObject(i);
                int x = Integer.parseInt(point.getString(JsonFileParser.JSON_OBJECT_X));
                int y = Integer.parseInt(point.getString(JsonFileParser.JSON_OBJECT_Y));
                String desc = point.getString(JsonFileParser.JSON_OBJECT_DESC);
                PointsListAdapter.Point p = new PointsListAdapter.Point(x, y, desc);
                mPoints.add(p);
            }
        } catch ( Exception e ){
            e.printStackTrace();
            throw new ConfigImportException("A problem occurs with import of the points file: "+e.getMessage());
        }

    }

}
