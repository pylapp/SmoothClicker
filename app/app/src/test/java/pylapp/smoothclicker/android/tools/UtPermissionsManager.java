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

import org.junit.Test;

import pylapp.smoothclicker.android.AbstractTest;

/**
 * Class to test the PermissionsManager
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 21/06/2016
 * @see PermissionsManager
 */
public class UtPermissionsManager extends AbstractTest {


    /**
     * Tests the refreshContext() method with a null context
     */
    @Test (expected = IllegalArgumentException.class)
    public void refreshContextWithNullValue(){
        l(this, "@Test refreshContextWithNullValue");
        PermissionsManager.instance.refreshContext(null);
    }

    /**
     * Tests the createPermissionListenerForWriteExternalStorage() method
     */
    @Test
    public void createPermissionListenerForWriteExternalStorage(){
        l(this, "@Test createPermissionListenerForWriteExternalStorage");
        PermissionsManager.instance.createPermissionListenerForWriteExternalStorage(null, null, null);
        PermissionsManager.instance.createPermissionListenerForWriteExternalStorage(new PermissionsManager.PermissionGrantedCallback() {
            @Override
            public void onPermissionGranted() {

            }
        }, new PermissionsManager.PermissionDeniedCallback() {
            @Override
            public void onPermissionDenied() {

            }
        }, new PermissionsManager.PermissionRationShouldBeShownCallback() {
            @Override
            public void onPermissionRationaleShouldBeShown() {

            }
        });
    }

    /**
     * Tests the createPermissionListenerForReadExternalStorage() method
     */
    @Test
    public void createPermissionListenerForReadExternalStorage(){
        l(this, "@Test createPermissionListenerForReadExternalStorage");
        PermissionsManager.instance.createPermissionListenerForReadExternalStorage(null, null, null);
        PermissionsManager.instance.createPermissionListenerForReadExternalStorage(new PermissionsManager.PermissionGrantedCallback() {
            @Override
            public void onPermissionGranted() {

            }
        }, new PermissionsManager.PermissionDeniedCallback() {
            @Override
            public void onPermissionDenied() {

            }
        }, new PermissionsManager.PermissionRationShouldBeShownCallback() {
            @Override
            public void onPermissionRationaleShouldBeShown() {

            }
        });
    }

    /**
     * Tests the clean() method
     */
    @Test
    public void clean(){
        l(this, "@Test clean");
        PermissionsManager.instance.clean();
    }

    /**
     * Tests the initialize() method with a null context
     */
    @Test (expected = IllegalArgumentException.class)
    public void initializeWithNullContext(){
        l(this, "@Test initializeWithNullContext");
        PermissionsManager.instance.initialize(null);
    }

}
