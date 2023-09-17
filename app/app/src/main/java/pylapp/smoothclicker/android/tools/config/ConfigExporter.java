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

package pylapp.smoothclicker.android.tools.config;

import java.util.List;

import pylapp.smoothclicker.android.views.PointsListAdapter;

/**
 * Interface which defines the behaviour an object must have if it wants to save the app's config
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.3.0
 * @since 26/05/2016
 */
public interface ConfigExporter {


    /* ******* *
     * METHODS *
     * ******* */

    /**
     *
     * @param unitTime - The unit time in use
     */
    void setUnitTime( UnitTime unitTime );

    /**
     *
     * @param isStartDelayed - If the start is delayed
     */
    void setStartDelayed( boolean isStartDelayed );

    /**
     *
     * @param delayInS - The delay in second
     */
    void setDelay( int delayInS );

    /**
     *
     * @param timeGapInS - The time to wait (in second) between each click
     */
    void setTimeGap( int timeGapInS );

    /**
     *
     * @param repeat - The amount of repeat to do
     */
    void setRepeat( int repeat );

    /**
     *
     * @param isEndlessRepeat - If the repeat will be endless
     */
    void setEndlessRepeat( boolean isEndlessRepeat );

    /**
     *
     * @param isVibrateOnStart - If the device must vibrate on start
     */
    void setVibrateOnStart( boolean isVibrateOnStart );

    /**
     *
     * @param isVibrateOnClick - If the device must vibrate on each click
     */
    void setVibrateOnCLick( boolean isVibrateOnClick );

    /**
     *
     * @param isRingOnClick - If the device must play a sound on each click
     */
    void setRingOnClick( boolean isRingOnClick );

    /**
     *
     * @param isNotificationOnClick - If the device must have a notification on click
     */
    void setNotificationOnCLick( boolean isNotificationOnClick );

    /**
     *
     * @param pointsToClickOn - The list of points to click on
     */
    void setPointsToClickOn( List<PointsListAdapter.Point> pointsToClickOn );

    /**
     *  Writes the config
     *  @throws ConfigExportException - When a problem occurred
     */
    void writeConfig() throws ConfigExportException;


    /* ********** *
     * EXCEPTIONS *
     * ********** */

    /**
     * An exception which occurs when a problem occured during the export
     */
    class ConfigExportException extends Exception {
        public ConfigExportException( String message ){
            super(message);
        }
    }

    /* ***** *
     * ENUMS *
     * ***** */

    /**
     * The unit time in use
     */
    enum UnitTime {
        /**
         * ms
         */
        MILLISECOND,
        /**
         * s
         */
        SECOND,
        /**
         * m
         */
        MINUTE,
        /**
         * h
         */
        HOUR
    }

}
