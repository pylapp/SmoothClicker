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

package pylapp.smoothclicker.android.tools.screen;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pylapp.smoothclicker.android.utils.Config;

/**
 * A class which can unlock a device (i.e. unlock the lock screen).
 * It will uses a configuration file to make to unlock it.
 * This configuration file must contain the commands to execute in ADB Shell.
 * It must be defined in the app's dedicated folder and have the name <b>sc_unlock.sh/b>.
 *
 * To push it in the device:
     <pre>
            adb push sc_unlock.sh /storage/emulated/legacy/Smooth_Clicker/
     </pre>
 *
 * Here a sample of such file (For a Samsung Galaxy S3 with Android 4.4.4 through CyanogenMod 11)
     <pre>
            #  With an unlock pattern where the unlock item is on the right bottom)
                 input swipe 350 1050 700 1050
            # With a pattern to swipe with four points
                 sendevent /dev/input/event2 3 57 14
                 sendevent /dev/input/event2 1 330 1

                 sendevent /dev/input/event2 3 53 192
                 sendevent /dev/input/event2 3 54 1032
                 sendevent /dev/input/event2 3 58 57
                 sendevent /dev/input/event2 0 0 0

                 sendevent /dev/input/event2 3 53 347
                 sendevent /dev/input/event2 3 54 882
                 sendevent /dev/input/event2 3 58 57
                 sendevent /dev/input/event2 0 0 0
                 sleep 1

                 sendevent /dev/input/event2 3 53 375
                 sendevent /dev/input/event2 3 54 1033
                 sendevent /dev/input/event2 3 58 57
                 sendevent /dev/input/event2 0 0 0
                 sleep 1

                 sendevent /dev/input/event2 3 53 542
                 sendevent /dev/input/event2 3 54 1027
                 sendevent /dev/input/event2 3 58 57
                 sendevent /dev/input/event2 0 0 0
                 sleep 1

                 sendevent /dev/input/event2 3 57 4294967295
                 sendevent /dev/input/event2 1 330 0
                 sendevent /dev/input/event2 0 0 0

                 echo "UNLOCK_DONE" # Do not forget it!
    </pre>
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.1.0
 * @since 31/05/2016
 */
public class UnlockerImpl implements UnlockerStub {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The name of the file which contains the unlock script
     */
    private String mFileName;


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     * Token which must be returned by the unlock script to let the current thread
     * do its remaining job.
     */
    public static final String END_OF_SCRIPT_TOKEN = "UNLOCK_DONE";

    //private static final String LOG_TAG = UnlockerImpl.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * ************ */

    /**
     * Default constructor
     *
     * @param fileName - The name of the unlock script to use
     */
    public UnlockerImpl( String fileName ){
        super();
        if ( fileName == null || fileName.length() <= 0 ) throw new IllegalArgumentException("The fileName parameter cannot be null nor empty");
        mFileName = fileName;
    }


    /* ************************* *
     * METHODS FROM UnlockerStub *
     * ************************* */

    /**
     * Unlocks the device and trigger the callback when the unlock process has been done.
     * It will wait for a token displayed by the script, the token "UNLOCK_DONE".
     * When the current thread reads in the process output this token, it will exit from its loop and
     * let the task do its remaining job.
     * @param callback - The callback to trigger, can be null
     * @throws UnlockException - Thrown when something wrong occurs during the unlock process
     */
    @Override
    public void unlock( UnlockCallback callback ) throws UnlockException {

        // Get the script
        String scriptLines;
        File appDir = Config.getAppFolder();
        File file = new File(appDir.getAbsolutePath()+"/"+ mFileName);
        try {
            InputStream is = new FileInputStream( file );
            int size = 0;
            size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            scriptLines = new String(buffer, "UTF-8");
        } catch ( IOException ioe  ){
            ioe.printStackTrace();
            throw new UnlockException("A problem occurs with the unlock process: "+ioe.getMessage());
        }

        if ( scriptLines.length() <= 0 || ! scriptLines.contains(END_OF_SCRIPT_TOKEN) ){
            throw new UnlockException(("The token "+END_OF_SCRIPT_TOKEN+" is not is the script. It must be displayed in the output by the script"));
        }

        // Execute the script
        try {
            Process process = Runtime.getRuntime().exec("su"); // FIXME Is it mandatory? I do not think so
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(scriptLines);
            InputStream is = process.getInputStream();
            final int BUFF_LENG = 1024;
            byte[] buffer = new byte[BUFF_LENG];
            while ( true ){ // FIXME May be hazardous
                int read = is.read(buffer);
                String out = new String(buffer, 0, read);
                if (out.contains(END_OF_SCRIPT_TOKEN)) break;
            }
        } catch ( IOException ioe ){
            ioe.printStackTrace();
            if ( callback != null ) callback.onUnlockFail();
            throw new UnlockException("A problem occurs with the unlock process: "+ioe.getMessage());
        }

        // Trigger the callback
        if ( callback != null ) callback.onUnlockSuccess();

    }

}
