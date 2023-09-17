package pylapp.smoothclicker.android.tools;

import android.content.Context;

import pylapp.smoothclicker.android.AbstractTest;

import org.junit.Test;

/**
 * Class to test the ShakeToClean
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 21/03/2016
 * @see UtShakeToClean
 */
public class UtShakeToClean extends AbstractTest {


    /**
     * Tests the constructor
     */
    @Test (expected = IllegalArgumentException.class)
    public void constructor(){
        l(this, "@Test constructor");
        Context nullContext = null;
        new ShakeToClean(nullContext);
    }

}
