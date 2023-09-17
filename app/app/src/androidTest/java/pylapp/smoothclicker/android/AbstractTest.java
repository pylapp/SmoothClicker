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

package pylapp.smoothclicker.android;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Class to use to make UI tests with Espresso and UIAutomator.
 * Espresso is less heavy than UI Automator and is for white-box-style tests and single-app testing.
 * UI Automator is more for black-box-style tests, and for multi-apps testing. UIAutomator can reach some APIs or
 * widgets (notifications, networks, etc.).
 *
 * You should see this site : http://testdroid.com/tech/top-5-android-testing-frameworks-with-examples
 * Some samples here: https://google.github.io/android-testing-support-library/samples/index.html
 *
 * More about Espresso
 *  <ul>
 *      <li>http://developer.android.com/training/testing/ui-testing/espresso-testing.html</li>
 *  </ul>
 *
 *  More about UI Automator
 *  <ul>
 *      <li>http://developer.android.com/training/testing/unit-testing/instrumented-unit-tests.html#build</li>
 *      <li>http://developer.android.com/training/testing/ui-testing/index.html</li>
 *      <li>http://developer.android.com/training/testing/ui-testing/uiautomator-testing.html</li>
 *      <li>https://github.com/googlesamples/android-testing</li>
 *  </ul>
 *
 *  @author Pierre-Yves Lapersonne
 *  @version 1.0.1
 *  @since 21/03/2016
 */
//@RunWith(AndroidJUnit4.class)
//@SmallTest      // The test does not interact with any file system or network
// @MediumTest  // The test accesses file systems on box which is running tests
// @LargeTest   // The test accesses external file systems, networks, etc.
public abstract class AbstractTest {


    private static final String LOG_PREFIX = "[Instrumented Test] ";


    /**
     * <i>The tests have to start from the screen home</i>
     */
    @BeforeClass
    public static void start(){
        l("AbstractTest","@BeforeClass start");
        UiDevice d = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        d.pressHome();
    }

    /**
     * <i>The device should be at its initial state when all tests are done</i>
     */
    @AfterClass
    public static void end(){
        l("AbstractTest","@AfterClass end");
        UiDevice d = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        d.pressHome();
    }

    /**
     * Enables to wait before tests or functions calls
     * @param timeMs - The time to wait
     */
    protected void w( long timeMs ){
        try { Thread.sleep(timeMs); } catch ( InterruptedException ie ){ie.printStackTrace();}
    }

    /**
     * Logs a message
     * @param caller - The object calling the method
     * @param message -
     */
    protected static void l( AbstractTest caller, String message ){
        if ( caller == null ){
            System.out.println(LOG_PREFIX + "??? " + message);
            return;
        }
        String className = caller.getClass().getSimpleName();
        System.out.println(LOG_PREFIX +  className + " " + message);
    }

    /**
     * Logs a message
     * @param tag - A tag
     * @param message -
     */
    protected static void l( String tag, String message ){
        System.out.println(LOG_PREFIX + tag + " " + message);
    }


}
