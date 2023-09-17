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

package pylapp.smoothclicker.android.tools;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import pylapp.smoothclicker.android.R;

/**
 * A utility class which manage permissions according to the device's Android's version.
 * Class based on the singleton design pattern.
 *
 * This class uses the Dexter library to deal with permissions: https://github.com/Karumi/Dexter
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 21/06/2016
 */
public class PermissionsManager {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The listener which deals with the WRITE_EXTERNAL_STORAGE permission
     */
    private PermissionListener mPermissionListenerWriteExternalStorage;

    /**
     * The listener which deals with the READ_EXTERNAL_STORAGE permission
     */
    private PermissionListener mPermissionListenerReadExternalStorage;

    /**
     * The context in use to to use to build dialogs
     */
    private Context mContext;

    /**
     * The singleton
     */
    public static final PermissionsManager instance = new PermissionsManager();


    /* ********* *
     * CONSTANTS *
     * ********* */

    private static final String LOG_TAG = PermissionsManager.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     *
     */
    private PermissionsManager(){
        super();
    }

    /* ******* *
     * METHODS *
     * ****** */

    /**
     *
     * @param c - The context to use
     * @return PermissionsManager - this
     */
    public PermissionsManager refreshContext( Context c ){
        if ( c == null ) throw new IllegalArgumentException("The context parameter cannot be null!");
        mContext = c;
        return this;
    }

    /**
     * Creates permissions listeners for the WRITE_EXTERNAL_STORAGE permission
     * Check previously if the APi is 23+.
     * Do not forget to refresh the context in use.
     *
     * @param callbackGranted - The callback to trigger, can be null
     * @param callbackDenied - The callback to trigger, can be null
     * @param callbackRationale - The callback to trigger, can be null
     */
    public void createPermissionListenerForWriteExternalStorage( final PermissionGrantedCallback callbackGranted,
                                                                 final PermissionDeniedCallback callbackDenied,
                                                                 final PermissionRationShouldBeShownCallback callbackRationale ){

        if ( ! isApi23OrHigher() ) return;

        // Build a dialog which will be displayed if the users denies the dedicated permission
        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(mContext)
                        .withTitle(R.string.permission_write_external_storage_title)
                        .withMessage(R.string.permission_write_external_storage_summary)
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.drawable.logo_512)
                        .build();

        // Create a lister which will trigger the export if permission granted
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Logger.i(LOG_TAG, "Permission " + Manifest.permission.WRITE_EXTERNAL_STORAGE + " granted");
                if ( callbackGranted != null ) callbackGranted.onPermissionGranted();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Logger.i(LOG_TAG, "Permission "+ Manifest.permission.WRITE_EXTERNAL_STORAGE+" denied");
                if ( callbackDenied != null ) callbackDenied.onPermissionDenied();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                Logger.i(LOG_TAG, "Permission "+ Manifest.permission.WRITE_EXTERNAL_STORAGE+" rationale should be shown");
                token.continuePermissionRequest();
                if ( callbackRationale != null ) callbackRationale.onPermissionRationaleShouldBeShown();
            }
        };

        mPermissionListenerWriteExternalStorage = new CompositePermissionListener(permissionListener, dialogPermissionListener);
    }

    /**
     * Checks for the permissions WRITE_EXTERNAL_STORAGE.
     * Will ask for this permissions, if granted (or not) will trigger the dedicated callbacks
     */
    public void getAndGoWithPermissionWriteExternalStorage(){
        Dexter.checkPermission(mPermissionListenerWriteExternalStorage, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * Creates permissions listeners for the READ_EXTERNAL_STORAGE permission
     * Check previously if the APi is 23+.
     * Do not forget to refresh the context in use.
     *
     * @param callbackGranted - The callback to trigger, can be null
     * @param callbackDenied - The callback to trigger, can be null
     * @param callbackRationale - The callback to trigger, can be null
     */
    public void createPermissionListenerForReadExternalStorage( final PermissionGrantedCallback callbackGranted,
                                                                 final PermissionDeniedCallback callbackDenied,
                                                                 final PermissionRationShouldBeShownCallback callbackRationale ){

        if ( ! isApi23OrHigher() ) return;

        // Build a dialog which will be displayed if the users denies the dedicated permission
        PermissionListener dialogPermissionListener2 =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(mContext)
                        .withTitle(R.string.permission_read_external_storage_title)
                        .withMessage(R.string.permission_read_external_storage_summary)
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.drawable.logo_512)
                        .build();

        // Create a lister which will trigger the export if permission granted
        PermissionListener permissionListener2 = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Logger.i(LOG_TAG, "Permission " + Manifest.permission.READ_EXTERNAL_STORAGE + " granted");
                if ( callbackGranted != null ) callbackGranted.onPermissionGranted();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Logger.i(LOG_TAG, "Permission "+ Manifest.permission.READ_EXTERNAL_STORAGE+" denied");
                if ( callbackDenied != null ) callbackDenied.onPermissionDenied();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                Logger.i(LOG_TAG, "Permission "+ Manifest.permission.READ_EXTERNAL_STORAGE+" rationale should be shown");
                token.continuePermissionRequest();
                if ( callbackRationale != null ) callbackRationale.onPermissionRationaleShouldBeShown();
            }
        };

        mPermissionListenerReadExternalStorage = new CompositePermissionListener(permissionListener2, dialogPermissionListener2);

    }

    /**
     * Checks for the permissions READ_EXTERNAL_STORAGE.
     * Will ask for this permissions, if granted (or not) will trigger the dedicated callbacks
     */
    public void getAndGoWithPermissionReadExternalStorage(){
        Dexter.checkPermission(mPermissionListenerReadExternalStorage, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * Deletes each listeners and context of this object
     */
    public void clean(){
        mPermissionListenerReadExternalStorage = null;
        mPermissionListenerWriteExternalStorage = null;
        mContext = null;
    }

    /**
     * Initializes the Dexter library this manager uses
     * @param c - The context in use, must not be null
     */
    public void initialize( Context c ){
        if ( c == null ) throw new IllegalArgumentException("The context must not be null!");
        Dexter.initialize(c);
        refreshContext(c);
    }

    /**
     *
     * @return boolean - True if the API in use is API 23 (Android 6 - Marshmallow) or more, false otherwise (less than 23)
     */
    public static boolean isApi23OrHigher(){
        return ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M );
    }


    /* ************* *
     * INNER CLASSES *
     * ************* */

    /**
     * Interface defining a behaviour about granted permissions
     */
    public interface PermissionGrantedCallback {

        /**
         * If the permission has been granted
         */
        void onPermissionGranted();

    } // End of public interface PermissionGrantedCallback

    /**
     * Interface defining a behaviour about denied permissions
     */
    public interface PermissionDeniedCallback {

        /**
         * If the permission has been denied
         */
        void onPermissionDenied();

    } // End of public interface PermissionDeniedCallback

    /**
     * Interface defining a behaviour about the fact a rationale should be shown
     */
    public interface PermissionRationShouldBeShownCallback{

        /**
         * If rationale should be shown
         */
        void onPermissionRationaleShouldBeShown();

    } // End of public interface PermissionRationShouldBeShownCallback

}
