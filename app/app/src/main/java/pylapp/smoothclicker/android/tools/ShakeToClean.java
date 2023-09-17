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

package pylapp.smoothclicker.android.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Class which shows features for a Shake To Clean service.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.4.0
 * @since 17/03/2016
 * @see SensorEventListener
 */
public class ShakeToClean implements SensorEventListener {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The sensor manager for the accelerometer
     */
    private SensorManager mSensorManager;
    /**
     * The accelerometer
     */
    private Sensor mAccelerometer;
    /**
     * The last timestamp for the shake event
     */
    private long mLastTimestampAcc;
    /**
     * For the variations of the axis
     */
    private float mXprev, mYprev, mZprev;

    /**
     * The object to warn when the shake to clean event has been detected
     */
    private ShakeToCleanCallback mCallback;

    /**
     *
     */
    private Context mContext;

    /**
     * The singleton
     */
    private static ShakeToClean sInstance;


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * A threshold for the accelerometer (in ms)
     */
    private static final long SEUIL_TIMESTAMP_ACCELEROMETER = 100;
    /**
     * A threshold for the shake event
     */
    private static final long SEUIL_SHAKE = 800;


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     *
     * @param c - The context, must not be null
     * @throws IllegalArgumentException - If c is null
     */
    public ShakeToClean( Context c ){
        super();
        if ( c == null ) throw new IllegalArgumentException("The context param must not be null");
        mContext = c;
        init();
    }


    /* ******************************** *
     * METHODS FROM SensorEventListener *
     * ******************************** */

    /**
     *
     * @param se -
     */
    @Override
    public void onSensorChanged( SensorEvent se ){
        long actualAccTs = System.currentTimeMillis();
        if ( (actualAccTs - mLastTimestampAcc) > SEUIL_TIMESTAMP_ACCELEROMETER ){
            long diffTime = (actualAccTs - mLastTimestampAcc);
            mLastTimestampAcc = actualAccTs;
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            float speed = Math.abs(x+y+z-mXprev-mYprev-mZprev) / diffTime * 10000;
            if ( speed > SEUIL_SHAKE ){
                if ( mCallback == null ){
                    throw new IllegalStateException("Nobody can handle the shake to clean event !");
                } else {
                    mCallback.shakeToClean();
                }
            }
            mXprev = x;
            mYprev = y;
            mZprev = z;
        }
    }

    /**
     * Does nothing
     * @param sensor -
     * @param accuracy -
     */
    @Override
    public void onAccuracyChanged( Sensor sensor, int accuracy ){
        // Do nothing
    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Initializes the object
     */
    public void init(){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Unregister the listener to the sensor manager
     */
    public void unregister(){
        mSensorManager.unregisterListener(this);
    }

    /**
     * Register the listener to the sensor manager
     */
    public void register(){
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Unregisters the shake to clean callback
     */
    public void unregisterCallback(){
        mCallback = null;
    }

    /**
     * Registers a shake to clean callback
     */
    public void registerCallback( ShakeToCleanCallback stcc ){
        if ( stcc == null ) throw new IllegalArgumentException("The ShakeToClean callback is null !");
        mCallback = stcc;
    }

    /**
     * Returns the singleton
     * @param c -
     * @return ShakeToClean
     */
    public static ShakeToClean getInstance( Context c ){
        if ( sInstance == null ) sInstance = new ShakeToClean(c);
        return sInstance;
    }


    /* *************** *
     * INNER INTERFACE *
     * *************** */

    /**
     * Interface which represents a callback to trigger on each shake to clean event
     */
    public interface ShakeToCleanCallback {

        /**
         * The callback triggered when a shake to clean event has been made
         */
        void shakeToClean();

    } // End of public interface ShakeToCleanCallback

}
