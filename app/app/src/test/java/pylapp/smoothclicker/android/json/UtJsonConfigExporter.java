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

import java.util.ArrayList;
import java.util.List;

import pylapp.smoothclicker.android.AbstractTest;
import pylapp.smoothclicker.android.tools.config.ConfigExporter;
import pylapp.smoothclicker.android.views.PointsListAdapter;

/**
 * Class to use to test the JsonConfigExporter class
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.2.0
 * @since 26/05/2016
 */
public class UtJsonConfigExporter extends AbstractTest {


    /**
     * Tests the unit time to apply
     */
    @Test
    public void setUnitTime(){

        l(this, "@Test setUnitTime");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setUnitTime(ConfigExporter.UnitTime.SECOND);
        exporter.setUnitTime(ConfigExporter.UnitTime.MINUTE);
        exporter.setUnitTime(ConfigExporter.UnitTime.HOUR);

    }

    /**
     * Tests the setDelay method with bad values
     *
     * <i>The setDelay() cannot have negative values, it must thrown an IllegalArgumentException</i>
     */
    @Test ( expected = IllegalArgumentException.class)
    public void setDelayWithBadValues(){

        l(this, "@Test setDelayWithBadValues");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setDelay(-1);

    }

    /**
     * Tests the setDelay method with good values
     *
     * <i>The setDelay() can deal with positive and null values</i>
     */
    @Test
    public void setDelay(){

        l(this, "@Test setDelay");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setDelay(0);
        exporter.setDelay(1);
        exporter.setDelay(Integer.MAX_VALUE);

    }

    /**
     * Tests the setTimeGap method with bad values
     *
     * <i>The setTimeGap() cannot have negative values, it must thrown an IllegalArgumentException</i>
     */
    @Test ( expected = IllegalArgumentException.class)
    public void setTimeGapWithBadValues(){

        l(this, "@Test setTimeGapWithBadValues");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setTimeGap(-1);

    }

    /**
     * Tests the setTimeGap method with good values
     *
     * <i>The setTimeGap() can deal with positive and null values</i>
     */
    @Test
    public void setTimeGap(){

        l(this, "@Test setTimeGap");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setTimeGap(0);
        exporter.setTimeGap(1);
        exporter.setTimeGap(Integer.MAX_VALUE);

    }

    /**
     * Tests the setRepeat method with bad values
     *
     * <i>The setRepeat() cannot have negative values, it must thrown an IllegalArgumentException</i>
     */
    @Test ( expected = IllegalArgumentException.class)
    public void setRepeatWithBadValues(){

        l(this, "@Test setRepeatWithBadValues");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setRepeat(-1);

    }

    /**
     * Tests the setRepeat method with good values
     *
     * <i>The setRepeat() can deal with positive and null values</i>
     */
    @Test
    public void setRepeat(){

        l(this, "@Test setRepeat");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setRepeat(0);
        exporter.setRepeat(1);
        exporter.setRepeat(Integer.MAX_VALUE);

    }

    /**
     * Tests the setPointsToClickOn method with good values
     *
     * <i>The setPointsToClickOn() can deal with null, empty or not empty lists</i>
     */
    @Test
    public void setPointsToClickOn(){

        l(this, "@Test setPointsToClickOn");

        ConfigExporter exporter = new JsonConfigExporter("file", "another file");
        exporter.setPointsToClickOn(null);
        exporter.setPointsToClickOn(new ArrayList<PointsListAdapter.Point>());
        List<PointsListAdapter.Point> lp = new ArrayList<>();
        lp.add( new PointsListAdapter.Point(1,2) );
        lp.add( new PointsListAdapter.Point(3,4) );
        lp.add( new PointsListAdapter.Point(3,4) );
        exporter.setPointsToClickOn(lp);

    }

}
