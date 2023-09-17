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

import pylapp.smoothclicker.android.tools.Logger;

/**
 * Class which makes pixel by pixel comparisons between pictures
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 02/06/2016
 */
public class PixelByPixelPicturesComparator implements PicturesComparator {



    /* ********* *
     * CONSTANTS *
     * ********* */

    private static final String LOG_TAG = PixelByPixelPicturesComparator.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     */
    public PixelByPixelPicturesComparator(){
        super();
    }


    /* ******************************* *
     * METHODS FROM PicturesComparator *
     * ******************************* */

    /**
     * Compares two pictures as bitmaps.
     * If the pictures are the same, returns true.
     * If the pictures differs, returns false.
     *
     * @param basePicture - The base picture
     * @param pickedPicture - The picked picture, a more recent one, to compare to the base picture
     * @return boolean - Returns true if pictures have the same dimensions, config, and pixel data as this bitmap ; false otherwise
     * @throws PicturesComparatorException - If a problem occurs during the comparison
     */
    @Override
    public boolean comparePictures( Bitmap basePicture, Bitmap pickedPicture ) throws PicturesComparatorException {
        if ( basePicture == null ) throw new PicturesComparatorException("The base picture is null");
        if ( pickedPicture == null ) throw new PicturesComparatorException("The picked picture is null");
        boolean areEqual = basePicture.sameAs(pickedPicture);
        Logger.d(LOG_TAG, "Pictures are equal: " + areEqual);
        return basePicture.sameAs(pickedPicture);
    }

    /**
     * Compares two pictures as bitmaps.
     * If the pictures are the same, returns true.
     * If the pictures differs, returns false.
     * The threshold is a value to use as a percentage of errors in the comparison.
     * For example, if the implementation of PicturesComparator uses pixel-to-pixel comparison, the _threshold_ value
     * can be seen as the amount of pixels which do not match.
     *
     * @param basePicture - The base picture
     * @param pickedPicture - The picked picture, a more recent one, to compare to the base picture
     * @param threshold - The amount of acceptable errors, as a percentage, <b>you may use threshold=1< (i.e. 1%/b>
     * @return boolean - True if the pictures are the same, false otherwise
     * @throws PicturesComparatorException - If a problem occurs during the comparison
     */
    @Override
    public boolean comparePictures( Bitmap basePicture, Bitmap pickedPicture, float threshold ) throws PicturesComparatorException {

        // Some checks
        if ( basePicture == null ) throw new PicturesComparatorException("The base picture is null");
        if ( pickedPicture == null ) throw new PicturesComparatorException("The picked picture is null");
        if ( threshold < 0 || threshold > 100 ) throw new PicturesComparatorException("The threshold must be >= 0 and <= 100, it as a percentage (%)");

        // Heights comparison
        final int BASE_HEIGHT = basePicture.getHeight();
        final int PICKED_HEIGHT = pickedPicture.getHeight();
        if ( BASE_HEIGHT != PICKED_HEIGHT ){
            Logger.d(LOG_TAG, "Pictures have different heights base/picked:" + BASE_HEIGHT+"/"+PICKED_HEIGHT);
            return false;
        }

        // Widths comparison
        final int BASE_WIDTH = basePicture.getWidth();
        final int PICKED_WIDTH = pickedPicture.getWidth();
        if ( BASE_WIDTH != PICKED_WIDTH ){
            Logger.d(LOG_TAG, "Pictures have different widths base/picked:" + BASE_WIDTH+"/"+PICKED_WIDTH);
            return false;
        }

        // Pixels comparison
        double notEqualPixels = 0;
        final double PIXELS_NUMBER = BASE_HEIGHT * BASE_WIDTH;

        for ( int x = 0 ; x < BASE_WIDTH; x++ ){
            for ( int y = 0; y < BASE_HEIGHT; y++ ){
                if ( basePicture.getPixel(x,y) != pickedPicture.getPixel(x,y) ){
                    notEqualPixels++;
                }
            }
        }

        // Check the rate of not equal pixels
        Logger.d(LOG_TAG, "Should start click process according to the capture: "+( notEqualPixels <= ((threshold/100) * PIXELS_NUMBER) ));
        return ( notEqualPixels <= ((threshold/100) * PIXELS_NUMBER) );

    }

}
