package pylapp.smoothclicker.android.json;

import org.junit.Test;

import pylapp.smoothclicker.android.AbstractTest;

import static junit.framework.Assert.assertEquals;

/**
 * Class to test the MissingJsonPointsFileException
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 20/05/2016
 */
public class UtMissingJsonPointsFileException extends AbstractTest {

    /**
     * Tests the getMessage()
     */
    @Test
    public void getMessage(){

        l(this, "@Test getMessage");

        final String S = "A dummy message";

        MissingJsonPointsFileException e = new MissingJsonPointsFileException(S);
        assertEquals(S, e.getMessage());

    }

}
