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


package pylapp.smoothclicker.android.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.receivers.BRClicker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to use to make instrumented / unit tests with Espresso and UiAutomator of the BRClicker.
 *
 *  @author Pierre-Yves Lapersonne
 *  @version 1.0.0
 *  @since 22/03/2016
 *  @see AbstractTest
 */
public class ItBrClicker extends AbstractTest {


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
    }

    /**
     * Tests the onReceive() method with a null intent
     *
     * <i>A BRClicker object can deal with null intent</i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void onReceiveNullIntent(){

        l(this, "@Test onReceiveNullIntent");

        BRClicker receiver = new BRClicker();
        Context c = InstrumentationRegistry.getTargetContext();
        Intent nullIntent = null;
        receiver.onReceive(c, nullIntent);

    }

    /**
     * Tests the onReceive() method with an intent without action
     *
     * <i>A BRClicker object can deal with intent having no action</i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void onReceiveNoAction(){

        l(this, "@Test onReceiveNoAction");

        BRClicker receiver = new BRClicker();
        Context c = InstrumentationRegistry.getTargetContext();
        Intent intentWithoutAction = new Intent();
        receiver.onReceive(c, intentWithoutAction);

    }

    /**
     * Tests the onReceive() method without context
     *
     * <i>A BRClicker object can deal with null context</i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void onReceiveNullContext(){

        l(this, "@Test onReceiveNullContext");

        BRClicker receiver = new BRClicker();
        Context nullContext = null;
        Intent intent = new Intent("");
        receiver.onReceive(nullContext, intent);

    }

    /**
     * Tests the onReceive()
     *
     * <i>A BClicker object can handle BOOT_COMPLETED and BATTERY_LOW signals</i>
     */
    @Test
    public void onReceive(){

        l(this, "@Test onReceive");

        BRClicker receiver = new BRClicker();
        Context context = InstrumentationRegistry.getTargetContext();
        Intent intent = new Intent(BRClicker.BR_BOOT_COMPLETED);
        receiver.onReceive(context, intent);
        w(5000);
        intent = new Intent(BRClicker.BR_BATTERY_LOW);
        receiver.onReceive(context, intent);

    }

}
