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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pylapp.smoothclicker.android.tools.config.ConfigExporter;
import pylapp.smoothclicker.android.utils.Config;
import pylapp.smoothclicker.android.views.PointsListAdapter;

/**
 * Class which consists on exporting the configuration of the app in a JSON file
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.6.0
 * @since 26/05/2016
 * @see ConfigExporter
 */
public class JsonConfigExporter implements ConfigExporter {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The unit time to use (s, m or h)
     */
    private UnitTime mUnitTime = UnitTime.SECOND;
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
     * If the device vibrate on start
     */
    private boolean mVibrateOnStart;

    /**
     * If the device vibrate on click
     */
    private boolean mVibrateOnClick;

    /**
     * If the device must ring a sound on click
     */
    private boolean mRingOnClick;

    /**
     * If the device must have a notification on click
     */
    private boolean mNotifiyOnClick;

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

    //private static final String LOG_TAG = JsonConfigExporter.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     *
     * @param pointsFileName - The name of the points file to use
     * @param configFileName - The name of the config file to use
     */
    public JsonConfigExporter( String pointsFileName, String configFileName ){
        super();
        if ( pointsFileName == null || pointsFileName.length() <= 0 ) throw new IllegalArgumentException("The points file's name parameter must not be null nor empty");
        if ( configFileName == null || configFileName.length() <= 0 ) throw new IllegalArgumentException("The config file's name parameter must not be null nor empty");
        mPointsFileName = pointsFileName;
        mConfigFileName = configFileName;
    }


    /* *************************** *
     * METHODS FROM ConfigExporter *
     * *************************** */

    /**
     *
     * @param unitTime - The unit time in use
     */
    @Override
    public void setUnitTime( UnitTime unitTime ){
        mUnitTime = unitTime;
    }

    /**
     *
     * @param isStartDelayed - If the start is delayed
     */
    @Override
    public void setStartDelayed( boolean isStartDelayed ){
        mIsStartDelayed = isStartDelayed;
    }

    /**
     *
     * @param delayInS - The delay in second, must be >= 0
     */
    @Override
    public void setDelay( int delayInS ){
        if ( delayInS < 0 ) throw new IllegalArgumentException("The delay must be >= 0");
        mDelay = delayInS;
    }

    /**
     *
     * @param timeGapInS - The time to wait (in second) between each click, must be >= 0
     */
    @Override
    public void setTimeGap( int timeGapInS ){
        if ( timeGapInS < 0 ) throw new IllegalArgumentException("The time gap must be >= 0");
        mTimeGap = timeGapInS;
    }

    /**
     *
     * @param repeat - The amount of repeat to do, must be >= 0
     */
    @Override
    public void setRepeat( int repeat ){
        if ( repeat < 0 ) throw new IllegalArgumentException("The repeat must be >= 0 ");
        mRepeat = repeat;
    }

    /**
     *
     * @param isEndlessRepeat - If the repeat will be endless
     */
    @Override
    public void setEndlessRepeat( boolean isEndlessRepeat ){
        mIsEndlessRepeat = isEndlessRepeat;
    }

    /**
     *
     * @param isVibrateOnStart - If the device must vibrate on start
     */
    @Override
    public void setVibrateOnStart( boolean isVibrateOnStart ){
        mVibrateOnStart = isVibrateOnStart;
    }

    /**
     *
     * @param isVibrateOnClick - If the device must vibrate on each click
     */
    @Override
    public void setVibrateOnCLick( boolean isVibrateOnClick ){
        mVibrateOnClick = isVibrateOnClick;
    }

    /**
     *
     * @param isRingOnClick - If the device must play a sound on each click
     */
    @Override
    public void setRingOnClick( boolean isRingOnClick ){
        mRingOnClick = isRingOnClick;
    }

    /**
     *
     * @param isNotificationOnClick - If the device must have a notification on click
     */
    @Override
    public void setNotificationOnCLick( boolean isNotificationOnClick ){
        mNotifiyOnClick = isNotificationOnClick;
    }

    /**
     *
     * @param pointsToClickOn - The list of points to click on
     */
    @Override
    public void setPointsToClickOn( List<PointsListAdapter.Point> pointsToClickOn ){
        if ( pointsToClickOn == null ) mPoints = new ArrayList<>();
        else mPoints = new ArrayList<>(pointsToClickOn);
    }

    /**
     *  Writes the config
     */
    @Override
    public void writeConfig() throws ConfigExportException {
        writeAppConfig();
        writePoints();
    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Writes the app config in a dedicated JSON file
     * @throws ConfigExportException - When a problem occurs
     */
    private void writeAppConfig() throws ConfigExportException {

        // Prepare the file's content
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_COMMENT).append("\"            : ").append("\"").append(new Date()).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_UNIT_TIME).append("\"        : ").append("\"");
        switch ( mUnitTime ){
            case MILLISECOND:
                sb.append("ms").append("\",\n");
                break;
            case MINUTE:
                sb.append("m").append("\",\n");
                break;
            case HOUR:
                sb.append("h").append("\",\n");
                break;
            case SECOND:
            default:
                sb.append("s").append("\",\n");
                break;
        }
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_DELAYED_START).append("\"    : ").append("\"").append(mIsStartDelayed).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_DELAY).append("\"           : ").append("\"").append(mDelay).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_TIME_GAP).append("\"         : ").append("\"").append(mTimeGap).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_REPEAT).append("\"          : ").append("\"").append(mRepeat).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_ENDLESS_REPEAT).append("\"   : ").append("\"").append(mIsEndlessRepeat).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_VIBRATE_ON_START).append("\"  : ").append("\"").append(mVibrateOnStart).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_VIBRATE_ON_CLICK).append("\"  : ").append("\"").append(mVibrateOnClick).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_RING).append("\"            : ").append("\"").append(mRingOnClick).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_NOTIFICATIONS).append("\"   : ").append("\"").append(mNotifiyOnClick).append("\"");
        sb.append("\n}");

        // Prepare the file
        File appDir = Config.getAppFolder();
        File file = new File(appDir.getAbsolutePath()+"/"+ mConfigFileName);

        // Write the content into the file
        String content = sb.toString();

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(content);
            bw.close();
        } catch ( Exception e ){
            e.printStackTrace();
            throw new ConfigExportException(e.getMessage());
        }

    }

    /**
     * Writes the points to click on in a dedicated JSON file
     * @throws ConfigExportException - When a problem occurs
     */
    private void writePoints() throws ConfigExportException {

        // Prepare the file's content
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("\"").append(JsonFileParser.JSON_OBJECT_COMMENT).append("\"            : ").append("\"").append(new Date()).append("\",\n");
        sb.append("\"").append(JsonFileParser.JSON_ARRAY_POINTS).append("\"          : [").append("\n");

        if ( mPoints != null ){

            // Delete the header fake point
            if (mPoints.size() > 0) mPoints.remove(0);

            // Write points if we have some points to write...
            if (mPoints.size() > 1) {
                for (int i = 0; i < mPoints.size() - 1; i++) {
                    PointsListAdapter.Point p = mPoints.get(i);
                    sb.append("\t").append(p.toJson()).append(", \n"); // Add a ',' after each point...
                }
                sb.append("\t").append(mPoints.get(mPoints.size() - 1).toJson()).append("\n"); // ...except of the least one
            } else if (mPoints.size() == 1) {
                sb.append("\t").append(mPoints.get(mPoints.size() - 1).toJson()).append("\n");
            }

        }
        sb.append("]\n}");

        // Prepare the file
        File appDir = Config.getAppFolder();
        File file = new File(appDir.getAbsolutePath()+"/"+ mPointsFileName);

        // Write the content into the file
        String content = sb.toString();

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(content);
            bw.close();
        } catch ( Exception e ){
            e.printStackTrace();
            throw new ConfigExportException(e.getMessage());
        }

    }

}
