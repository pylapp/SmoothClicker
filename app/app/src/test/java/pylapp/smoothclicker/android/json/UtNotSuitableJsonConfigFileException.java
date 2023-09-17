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

package pylapp.smoothclicker.android.json;

import org.junit.Test;

import pylapp.smoothclicker.android.AbstractTest;

import static junit.framework.Assert.assertEquals;


/**
 * Class to test the NotSuitableJsonConfigFileException
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 20/05/2016
 */
public class UtNotSuitableJsonConfigFileException extends AbstractTest {

    /**
     * Tests the getMessage()
     */
    @Test
    public void getMessage(){

        l(this, "@Test getMessage");

        final String S = "A dummy message";

        NotSuitableJsonConfigFileException e = new NotSuitableJsonConfigFileException(S);
        assertEquals(S, e.getMessage());

    }

}
