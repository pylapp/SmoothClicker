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

import android.content.Context;
import android.os.Vibrator;

/**
 * Utility class which manages vibrations notifications.
 * It is based on a wrapper design pattern.
 * Be sure you use the dedicated permission
 *
     <pre>
        <uses-permission android:name="android.permission.VIBRATE" />
     </pre>
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.3.0
 * @since 16/03/2016
 */
public class VibrationNotifier {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The context to use to get the vibrator
     */
    private Context mContext;


    private static final String LOG_TAG = VibrationNotifier.class.getSimpleName();


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The duration of a vibration in ms if the device must vibrate on start
     */
    public static final int VIBRATE_ON_START_DURATION = 500;
    /**
     * The duration of a vibration in ms if the device must vibrate on each click
     */
    public static final int VIBRATE_ON_CLICK_DURATION = 150;

    //private static final String LOG_TAG = VibrationNotifier.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     *
     * @param c - The context to use to get the vibrator, must not be null
     */
    public VibrationNotifier( Context c ){
        super();
        if ( c == null ) throw new IllegalArgumentException("The context param must not be null");
        mContext = c;
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     * Makes the device vibrate during an amount of ms
     * @param duration - The time in ms to vibrate
     */
    public void vibrate( final int duration ){
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(duration);
    }

}
