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

/**
 * Interface which defines the behavior an object must have to compare pictures.
 * Indeed we want to compare a new screenshot of the device to a some pictures so as to know
 * if a specific item appears on it to start teh click process.
 *
 * For example, if a screen shot is the same picture as the base picture (e.g. the home screen), do nothing.
 * If a screen shot is the same picture has the home screen with a specific item, start the click process.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 02/06/2016
 */
public interface PicturesComparator {

    /**
     * Compares two pictures as bitmaps.
     * If the pictures are the same, returns true.
     * If the pictures differs, returns false.
     *
     * @param basePicture - The base picture
     * @param pickedPicture - The picked picture, a more recent one, to compare to the base picture
     * @return boolean - True if the pictures are the same, false otherwise
     * @throws PicturesComparatorException - If a problem occurs during the comparison
     */
    boolean comparePictures(Bitmap basePicture, Bitmap pickedPicture) throws PicturesComparatorException;

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
     * @param threshold - The amount of acceptable errors
     * @return boolean - True if the pictures are the same, false otherwise
     * @throws PicturesComparatorException - If a problem occurs during the comparison
     */
    boolean comparePictures(Bitmap basePicture, Bitmap pickedPicture, float threshold) throws PicturesComparatorException;


    /**
     * Exception thrown when an error / a problem occurs during a comparison
     */
    class PicturesComparatorException extends Exception {
        PicturesComparatorException( String message ){
            super(message);
        }
    }

}
