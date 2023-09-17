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

import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pylapp.smoothclicker.android.AbstractTest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Class to use to make instrumented / unit tests with on the WakelockManager class
 *
 *  @author Pierre-Yves Lapersonne
 *  @version 1.1.0
 *  @since 30/05/2016
 *  @see AbstractTest
 */
public class ItWakelockManager extends AbstractTest {


    /**
     *
     */
    @Before
    public void init(){
        l(this,"@Before init");
    }

    /**
     *
     */
    @After
    public void clean(){
        l(this, "@After clean");
        WakelockManager.instance.clear();
    }


    /**
     * Tests the refreshContext() method with a null parameter
     *
     * <i>The refreshContext() method must throw an IllegalArgumentException if a null parameter is used/i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void refreshContextWithNullContext(){
        l(this, "@Test refreshContextWithNullContext");
        WakelockManager.instance.refreshContext(null);
    }

    /**
     * Tests the refreshContext() method
     *
     * <i>The refreshContext() can deal with non null context</i>
     */
    @Test
    public void refreshContext(){
        l(this, "@Test refreshContext");
        WakelockManager.instance.refreshContext(InstrumentationRegistry.getContext());
    }

    /**
     * Tests the isWakeLockAcquired() method
     *
     * <i>By default, isWakeLockAcquired() must return false</i>
     * <i>If we acquire a wakelock, isWakeLockAcquired() must return true</i>
     * <i>If we release a wakelock, isWakeLockAcquired() must return false</i>
     */
    @Test
    public void isWakeLockAcquired(){

        l(this, "@Test isWakeLockAcquired");

        assertFalse(WakelockManager.instance.isWakeLockAcquired());

        WakelockManager.instance.refreshContext(InstrumentationRegistry.getTargetContext());
        WakelockManager.instance.acquireWakelock();
        assertTrue(WakelockManager.instance.isWakeLockAcquired());

        WakelockManager.instance.releaseWakelock();
        assertFalse(WakelockManager.instance.isWakeLockAcquired());

    }

    /**
     * Tests the acquireWakelock() method
     *
     * <i>TIf no context has been defined( no refreshContext() call), an IllegalStateException must be thrown</i>
     */
    @Test (expected = IllegalStateException.class)
    public void acquireWakelockWithNullContext(){
        l(this, "@Test acquireWakelockWithNullContext");
        WakelockManager.instance.acquireWakelock();
    }

    /**
     * Tests the acquireWakelock() method
     *
     * <i>The WakelockManager can deal with several acquireWakelock()'s calls, and should release held wakelocks if needed</i>
     */
    @Test
    public void acquireWakelock(){

        l(this, "@Test acquireWakelock");

        WakelockManager.instance.refreshContext(InstrumentationRegistry.getTargetContext());
        final int TRY_NB = 20;
        for ( int i = 0; i < TRY_NB; i++ ) WakelockManager.instance.acquireWakelock();

    }

    /**
     * Tests the releaseWakelock() method
     *
     * <i>The WakelockManager can deal with several releaseWakelock()'s calls</i>
     */
    @Test
    public void releaseWakelock(){

        l(this, "@Test releaseWakelock");

        final int TRY_NB = 20;
        for ( int i = 0; i < TRY_NB; i++ ) WakelockManager.instance.releaseWakelock();

    }

    /**
     * Tests the several calls to acquireWakelock() and releaseWakelock() methods
     *
     * <i>The WakelockManager can deal with several calls to acquireWakelock() and releaseWakelock() methods</i>
     */
    @Test
    public void multipleCalls(){

        l(this, "@Test multipleCalls");

        WakelockManager.instance.refreshContext(InstrumentationRegistry.getTargetContext());
        final int TRY_NB = 20;
        for ( int i = 0; i < TRY_NB; i++ ){
            WakelockManager.instance.acquireWakelock();
            WakelockManager.instance.releaseWakelock();
        }

    }

    /**
     * Tests the several calls to switchWakelockState()
     *
     * <i>The WakelockManager can deal with several calls to switchWakelockState() method</i>
     */
    @Test
    public void switchWakelockState(){

        l(this, "@Test switchWakelockState");

        WakelockManager.instance.refreshContext(InstrumentationRegistry.getTargetContext());
        final int TRY_NB = 20;
        for ( int i = 0; i < TRY_NB; i++ ) WakelockManager.instance.switchWakelockState();

    }

    /**
     * Tests the switchWakelockStateWithNullContext() method
     *
     * <i>TIf no context has been defined (no refreshContext() call), an IllegalStateException must be thrown</i>
     */
    @Test (expected = IllegalStateException.class)
    public void switchWakelockStateWithNullContext(){
        l(this, "@Test switchWakelockStateWithNullContext");
        WakelockManager.instance.switchWakelockState();
    }

}
