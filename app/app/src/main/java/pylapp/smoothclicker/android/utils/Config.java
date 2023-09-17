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

package pylapp.smoothclicker.android.utils;

import android.content.Context;

import java.io.File;

import pylapp.smoothclicker.android.R;

/**
 * Just a configuration class with useful values
 *
 * @author Pierre-Yves Lapersonne
 * @version 2.0.0
 * @since 02/03/2016
 */
public final class Config {

    /*
     * Default values
     */
    public static final boolean DEFAULT_START_DELAYED    = true;
    public static final String DEFAULT_DELAY             = "5";
    public static final String DEFAULT_TIME_GAP          = "3";
    public static final String DEFAULT_REPEAT            = "0";
    public static final boolean DEFAULT_REPEAT_ENDLESS   = false;
    public static final boolean DEFAULT_VIBRATE_ON_START = false;
    public static final boolean DEFAULT_VIBRATE_ON_CLICK = true;
    public static final boolean DEFAULT_NOTIF_ON_CLICK   = true;
    public static final boolean DEFAULT_RING_ON_CLICK    = false;
    public static final boolean DEFAULT_SHAKE_TO_CLEAN   = false;
    public static final boolean DEFAULT_IS_FIRST_START   = true;
    public static final int DEFAULT_TIME_UNIT_SELECTION  = R.id.rbUnitTimeS;


    /*
     * The shared preferences
     */
    public static final String SMOOTHCLICKER_SHARED_PREFERENCES_NAME = "pylapp.smoothclicker.android.SMOOTHCLICKER_SHARED_PREFERENCES_NAME";
    public static final String SP_KEY_START_TYPE_DELAYED             = "0x000011";
    public static final String SP_KEY_DELAY                          = "0x000012";
    public static final String SP_KEY_TIME_GAP                       = "0x000021";
    public static final String SP_KEY_REPEAT                         = "0x000031";
    public static final String SP_KEY_REPEAT_ENDLESS                 = "0x000032";
    public static final String SP_KEY_VIBRATE_ON_START               = "0x000041";
    public static final String SP_KEY_VIBRATE_ON_CLICK               = "0x000042";
    public static final String SP_KEY_NOTIF_ON_CLICK                 = "0x000051";
    public static final String SP_KEY_IS_FIRST_START                 = "0x000061";
    public static final String SP_KEY_UNIT_TIME                      = "0x000071";
    public static final String SP_KEY_RING_ON_CLICK                  = "0x000081";

    public static final int SP_ACCESS_MODE                           = Context.MODE_PRIVATE;

    /*
     * The folders and files in use
     */
    public static final String DEFAULT_FILE_JSON_POINTS_NAME = "sc_points.json";
    public static final String DEFAULT_FILE_JSON_CONFIG_NAME = "sc_config.json";
    public static final String DEFAULT_FILE_SH_UNLOCK_NAME   = "sc_unlock.sh";
    public static final String DEFAULT_FILE_TRIGGER_PICTURE  = "sc_trigger.png";
    public static final String FILE_CAPTURE_PICTURE          = "sc_capture.tmp.png";
    public static final String FOLDER_APP                    = "Smooth_Clicker";


    /**
     * Gets the app's folder, and create it if necessary
     * @return File - The app's folder
     */
    public static File getAppFolder(){
        File f = new File(android.os.Environment.getExternalStorageDirectory(), File.separator + FOLDER_APP);
        if ( f.exists() ) return f;
        f.mkdirs();
        return f;
    }

}
