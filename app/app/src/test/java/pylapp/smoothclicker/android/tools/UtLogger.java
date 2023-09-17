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

import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.tools.Logger;

import static org.junit.Assert.fail;
import org.junit.Test;


/**
 * Class to test the Logger
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 21/03/2016
 * @see AbstractTest
 */
public class UtLogger extends AbstractTest {

    /**
     * Tests the log() method
     */
    @Test
    public void log(){

        l(this, "@Test log");

        Logger.LogLevel ll = Logger.LogLevel.DEBUG;
        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.log(ll, nullLogTag, nullMessage);
            Logger.log(ll, emptyLogTag, emptyMessage);
            Logger.log(ll, logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.log");
        }

    }

    /**
     * Tests the d() method
     */
    @Test
    public void d(){

        l(this, "@Test d");

        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.d(nullLogTag, nullMessage);
            Logger.d(emptyLogTag, emptyMessage);
            Logger.d(logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.d");
        }

    }

    /**
     * Tests the i() method
     */
    @Test
    public void i(){

        l(this, "@Test i");

        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.i(nullLogTag, nullMessage);
            Logger.i(emptyLogTag, emptyMessage);
            Logger.i(logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.i");
        }

    }

    /**
     * Tests the v() method
     */
    @Test
    public void v(){

        l(this, "@Test v");

        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.v(nullLogTag, nullMessage);
            Logger.v(emptyLogTag, emptyMessage);
            Logger.v(logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.v");
        }
    }

    /**
     * Tests the w() method
     */
    @Test
    public void w(){

        l(this, "@Test w");

        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.w(nullLogTag, nullMessage);
            Logger.w(emptyLogTag, emptyMessage);
            Logger.w(logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.w");
        }

    }

    /**
     * Tests the e() method
     */
    @Test
    public void e(){

        l(this, "@Test e");

        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.e(nullLogTag, nullMessage);
            Logger.e(emptyLogTag, emptyMessage);
            Logger.e(logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.e");
        }

    }

    /***
     * tests the fe() method
     */
    @Test
    public void fe(){

        l(this, "@Test fe");

        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.fe(nullLogTag, nullMessage);
            Logger.fe(emptyLogTag, emptyMessage);
            Logger.fe(logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.fe");
        }

    }

    /**
     * Tests the wtf() method
     */
    @Test
    public void wtf(){

        l(this, "@Test wtf");

        String nullLogTag = null;
        String nullMessage = null;
        String logTag = "log tag";
        String message = "message";
        String emptyLogTag = "";
        String emptyMessage = "";

        try {
            Logger.wtf(nullLogTag, nullMessage);
            Logger.wtf(emptyLogTag, emptyMessage);
            Logger.wtf(logTag, message);
        } catch ( Exception e ){
            fail("Failure during test of Logger.wtf");
        }

    }

}
