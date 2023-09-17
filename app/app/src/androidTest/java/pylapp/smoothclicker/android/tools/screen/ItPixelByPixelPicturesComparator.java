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

import android.graphics.Bitmap;


import org.junit.Test;

import pylapp.smoothclicker.android.AbstractTest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Class to use to make instrumented / unit tests with on the PixelByPixelPicturesComparator class
 *
 *  @author Pierre-Yves Lapersonne
 *  @version 1.1.0
 *  @since 13/062016
 *  @see AbstractTest
 */
public class ItPixelByPixelPicturesComparator extends AbstractTest {

    /**
     * Tests the constructor
     */
    @Test
    public void constructor(){
        l(this, "@Test constructor");
        new PixelByPixelPicturesComparator();
    }

    /**
     * Tests the comparePictures() with identical pictures
     *
     * <i>Two identical pictures must be considered as equal, so return true</i>
     */
    @Test
    public void comparePictures(){

        l(this, "@Test comparePictures");
        l(this, "NOT IMPLEMENTED");
        // TODO

    }

    /**
     * Tests the comparePictures() with empty pictures
     *
     * <i>Two empty pictures must be considered as equal</i>
     */
    @Test
    public void comparePicturesWithEmptyPictures() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithEmptyPictures");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertTrue(
                pc.comparePictures(createEmptyBitmap(), createEmptyBitmap())
        );

    }

    /**
     * Tests the comparePictures() with null base picture
     *
     * <i>Comparisons with null base picture must thrown an exception</i>
     */
    @Test ( expected = PicturesComparator.PicturesComparatorException.class )
    public void comparePicturesWithNullBasePicture() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNullBasePicture");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        pc.comparePictures(null, createEmptyBitmap());

    }

    /**
     * Tests the comparePictures() with null picked picture
     *
     * <i>Comparisons with null picked picture must thrown an exception</i>
     */
    @Test ( expected = PicturesComparator.PicturesComparatorException.class )
    public void comparePicturesWithNullPickedPicture() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNullPickedPicture");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        pc.comparePictures(createEmptyBitmap(), null);

    }

    /**
     * Tests the comparePictures() with pictures with same heights but different widths
     *
     * <i>Comparison with pictures having same heights but different widths must return false</i>
     */
    @Test
    public void comparePicturesWithNotEqualWidthsPictures() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNotEqualWidthsPictures");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertFalse(
                pc.comparePictures(createEmptyBitmap(1000, 2000), createEmptyBitmap(1000, 1337))
        );

    }

    /**
     * Tests the comparePictures() with pictures with same widths but different heights
     *
     * <i>Comparison with pictures having same widths but different heights must return false</i>
     */
    @Test
    public void comparePicturesWithNotEqualHeightsPictures() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNotEqualHeightsPictures");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertFalse(
                pc.comparePictures(createEmptyBitmap(666, 42), createEmptyBitmap(310315, 42))
        );

    }

    /**
     * Tests the comparePictures() with pictures with same widths, same heights but different contents
     *
     * <i>Comparison with pictures having same widths and heights but different contents must return false</i>
     */
    @Test
    public void comparePicturesWithSameDimensionsButDifferentContents(){

        l(this, "@Test comparePicturesWithSameDimensionsButDifferentContents");
        l(this, "NOT IMPLEMENTED");
        // TODO

    }

    /**
     * Tests the comparePictures() with pictures with different widths and heights and contents
     *
     * <i>Comparison with pictures having different widths, heights and contents must return false</i>
     */
    @Test
    public void comparePicturesWithDifferentPictures(){

        l(this, "@Test comparePicturesWithDifferentPictures");
        l(this, "NOT IMPLEMENTED");
        // TODO

    }

    // TODO Same kind of tests with the method with threshold param

    /**
     * Tests the comparePictures() with identical pictures and a threshold
     *
     * <i>Two identical pictures must be considered as equal, so return true</i>
     */
    @Test
    public void comparePicturesWithThreshold(){

        l(this, "@Test comparePicturesWithThreshold");
        l(this, "NOT IMPLEMENTED");
        // TODO

    }

    /**
     * Tests the comparePictures() with empty pictures and a threshold
     *
     * <i>Two empty pictures must be considered as equal</i>
     */
    @Test
    public void comparePicturesWithEmptyPicturesWithThreshold() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithEmptyPicturesWithThreshold");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertTrue(
                pc.comparePictures(createEmptyBitmap(), createEmptyBitmap(), 0)
        );

    }

    /**
     * Tests the comparePictures() with null base picture and a threshold
     *
     * <i>Comparisons with null base picture must thrown an exception</i>
     */
    @Test ( expected = PicturesComparator.PicturesComparatorException.class )
    public void comparePicturesWithNullBasePictureWithThreshold() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNullBasePictureWithThreshold");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        pc.comparePictures(null, createEmptyBitmap(), 0);

    }

    /**
     * Tests the comparePictures() with null picked picture and a threshold
     *
     * <i>Comparisons with null picked picture must thrown an exception</i>
     */
    @Test ( expected = PicturesComparator.PicturesComparatorException.class )
    public void comparePicturesWithNullPickedPictureWithThreshold() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNullPickedPictureWithThreshold");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        pc.comparePictures(createEmptyBitmap(), null, 0);

    }

    /**
     * Tests the comparePictures() with pictures with same heights but different widths, and a threshold
     *
     * <i>Comparison with pictures having same heights but different widths must return false</i>
     */
    @Test
    public void comparePicturesWithNotEqualWidthsPicturesWithThreshold() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNotEqualWidthsPicturesWithThreshold");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertFalse(
                pc.comparePictures(createEmptyBitmap(1000, 2000), createEmptyBitmap(1000, 1337), 0)
        );

    }

    /**
     * Tests the comparePictures() with pictures with same widths but different heights, and a threshold
     *
     * <i>Comparison with pictures having same widths but different heights must return false</i>
     */
    @Test
    public void comparePicturesWithNotEqualHeightsPicturesWithThreshold() throws PicturesComparator.PicturesComparatorException {

        l(this, "@Test comparePicturesWithNotEqualHeightsPicturesWithThreshold");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertFalse(
                pc.comparePictures(createEmptyBitmap(666, 42), createEmptyBitmap(310315, 42), 0)
        );

    }

    /**
     * Tests the comparePictures() with pictures with same widths, same heights but different contents, and a threshold
     *
     * <i>Comparison with pictures having same widths and heights but different contents must return false</i>
     */
    @Test
    public void comparePicturesWithSameDimensionsButDifferentContentsWithThreshold(){

        l(this, "@Test comparePicturesWithSameDimensionsButDifferentContentsWithThreshold");
        l(this, "NOT IMPLEMENTED");
        // TODO

    }

    /**
     * Tests the comparePictures() with pictures with different widths and heights and contents, and a threshold
     *
     * <i>Comparison with pictures having different widths, heights and contents must return false</i>
     */
    @Test
    public void comparePicturesWithDifferentPicturesWithThreshold(){

        l(this, "@Test comparePicturesWithDifferentPicturesWithThreshold");
        l(this, "NOT IMPLEMENTED");
        // TODO

    }

    /**
     * Tests the comparePictures() method with a negative threshold
     *
     * <i>If a negative threshold is used, an exception is thrown</i>
     */
    @Test (expected = PicturesComparator.PicturesComparatorException.class)
    public void comparePicturesWithNegativeThreshold() throws PicturesComparator.PicturesComparatorException {
        l(this, "@Test comparePicturesWithNegativeThreshold");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertFalse(
                pc.comparePictures(createEmptyBitmap(5, 5), createEmptyBitmap(5, 5), -1)
        );
    }


    /**
     * Tests the comparePictures() method with a too big threshold
     *
     * <i>If a to big threshold is used, an exception is thrown</i>
     */
    @Test (expected = PicturesComparator.PicturesComparatorException.class)
    public void comparePicturesWithTooBigThreshold() throws PicturesComparator.PicturesComparatorException {
        l(this, "@Test comparePicturesWithTooBigThreshold");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        assertFalse(
                pc.comparePictures(createEmptyBitmap(5, 5), createEmptyBitmap(5, 5), 101)
        );
    }

    /**
     * Tests the comparePictures() method with border values for threshold
     *
     * <i>Border values of threshold (0 and 100) must be handled without problem</i>
     */
    @Test
    public void comparePicturesWithBorderValues() throws PicturesComparator.PicturesComparatorException {
        l(this, "@Test comparePicturesWithBorderValues");
        PicturesComparator pc = new PixelByPixelPicturesComparator();
        pc.comparePictures(createEmptyBitmap(5, 5), createEmptyBitmap(5, 5), 0);
        pc.comparePictures(createEmptyBitmap(5, 5), createEmptyBitmap(5, 5), 100);
    }

    /**
     *
     * @return Bitmap - A bitmap object
     */
    private Bitmap createEmptyBitmap(){
        final int W = 1000;
        final int H = 1000;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        return Bitmap.createBitmap(W, H, config); // Mutable bitmap ;)
    }

    /**
     * @param h - The height
     * @param w - The width
     * @return Bitmap - A bitmap object
     */
    private Bitmap createEmptyBitmap( int h, int w ){
        if ( h < 0 ) h = 1000;
        if ( w < 0 ) w = 1000;
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        return Bitmap.createBitmap(w, h, config); // Mutable bitmap ;)
    }

}
