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
import pylapp.smoothclicker.android.tools.config.ConfigExporter;
import pylapp.smoothclicker.android.utils.Config;

/**
 * Class to use to test the JsonConfigImporter class
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 27/05/2016
 */
public class ItJsonConfigImporter extends AbstractTest {

    /**
     * Tests the constructor with a null config file
     *
     * <i>If a null config file is used, an exception must be thrown</i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void constructorWithNullConfigFile(){
        l(this, "@Test constructorWithNullConfigFile");
        new JsonConfigExporter("dummy", null);
    }

    /**
     * Tests the constructor with an empty config file
     *
     * <i>If a null config file is used, an exception must be thrown</i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void constructorWithEmptyConfigFile(){
        l(this, "@Test constructorWithEmptyConfigFile");
        new JsonConfigExporter("dummy", "");
    }

    /**
     * Tests the constructor with a null points file
     *
     * <i>If a null points file is used, an exception must be thrown</i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void constructorWithNullPointFile(){
        l(this, "@Test constructorWithNullPointFile");
        new JsonConfigExporter(null, "kitten");
    }

    /**
     * Tests the constructor with an empty points file
     *
     * <i>If an empty points file is used, an exception must be thrown</i>
     */
    @Test (expected = IllegalArgumentException.class)
    public void constructorWithEmptyPointsFile(){
        l(this, "@Test constructorWithEmptyPointsFile");
        new JsonConfigExporter("", "dummy");
    }

    /***
     * Tests the writeConfig method
     */
    @Test
    public void writeConfig() throws ConfigExporter.ConfigExportException {
        l(this, "@Test writeConfig");
        ConfigExporter exporter = new JsonConfigExporter(Config.DEFAULT_FILE_JSON_POINTS_NAME, Config.DEFAULT_FILE_JSON_CONFIG_NAME);
        exporter.writeConfig();
    }

}
