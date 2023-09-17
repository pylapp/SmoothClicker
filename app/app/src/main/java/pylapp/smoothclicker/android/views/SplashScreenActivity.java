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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.notifiers.NotificationsManager;
import pylapp.smoothclicker.android.utils.Config;

/**
 * The splash screen activity
 *
 * @author Pierre-Yves Lapersonne
 * @version 3.1.0
 * @since 15/03/2016
 */
public class SplashScreenActivity extends AppCompatActivity {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * Flag indicating if the app is starting for the first time or not
     */
    public static boolean sIsFirstLaunch = true;

    /**
     * The handler with starts the main activity after a delay
     */
    private Handler mHandler;
    /**
     * The callback triggered by the handler
     */
    private Runnable mCallback;


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * The duration in ms of the splash
     */
    private static final int SPLASH_TIME_OUT = 3000;

    //private static final String LOG_TAG = SplashScreenActivity.class.getSimpleName();


    /* ****************************** *
     * METHODS FROM AppCompatActivity *
     * ****************************** */

    /**
     * Triggered to create the view
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        NotificationsManager.getInstance(this).stopAllNotifications();
        // If the app has been started previously, do not start the splash screen and run to the next activity
        if ( ! sIsFirstLaunch) {
            Intent i = new Intent(SplashScreenActivity.this, ClickerActivity.class);
            startActivity(i);
            finish();
        }
        sIsFirstLaunch = false;
        setContentView(R.layout.activity_splash_screen);
    }

    /**
     * Triggered when the view has been created
     * @param savedInstanceState -
     */
    @Override
    protected void onPostCreate( Bundle savedInstanceState ){

        mHandler = new Handler();
        mCallback = new Runnable() {
            @Override
            public void run() {
                // If this is the first start of the app (after the install), show the intro screen
                if ( isFirstStartSinceInstall() ){

                    // Change the settings
                    SharedPreferences sp = getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(Config.SP_KEY_IS_FIRST_START, false);
                    editor.apply();

                    // Starts the intro screen
                    Intent i = new Intent(SplashScreenActivity.this, IntroScreensActivity.class);
                    startActivity(i);
                    finish();

                // The app has been started previously, the user knows it, so go to the "main" activity
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, ClickerActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        mHandler.postDelayed(mCallback, SPLASH_TIME_OUT);

        super.onPostCreate(savedInstanceState);

    }

    /**
     * Triggered when the back button has been pressed
     */
    @Override
    public void onBackPressed(){
        if ( mHandler != null && mCallback != null ){
            mHandler.removeCallbacks(mCallback);
            mHandler = null;
            mCallback = null;
        }
        sIsFirstLaunch = true;
        finish();
    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     * Checks it the app ahs ever been started or not
     * @return boolean - True if the app has never benn started previously, false otherwise
     */
    private boolean isFirstStartSinceInstall(){
        SharedPreferences sp = getSharedPreferences(Config.SMOOTHCLICKER_SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        return sp.getBoolean(Config.SP_KEY_IS_FIRST_START, Config.DEFAULT_IS_FIRST_START);
    }

}
