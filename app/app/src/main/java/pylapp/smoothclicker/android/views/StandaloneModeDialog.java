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

package pylapp.smoothclicker.android.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import pylapp.smoothclicker.android.R;
import pylapp.smoothclicker.android.tools.Logger;


/**
 * Dialog which possesses items allowing the user to choose the mode of the standalone feature
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 26/05/2016
 */
public class StandaloneModeDialog extends AppCompatDialog {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The listener to trigger when the positive button has been clicked
     */
    private OnPositiveButtonListener mPositiveButtonListener;


    /* ********* *
     * CONSTANTS *
     * ********* */

    /**
     *
     */
    private static final float MIN_HEIGHT_RATIO = 0.33f;
    /**
     *
     */
    private static final float MAX_HEIGHT_RATIO = 0.25f;
    /**
     *
     */
    private static final float MIN_WIDTH_RATIO = 0.9f;

    private static final String LOG_TAG = StandaloneActivity.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     * @param c -
     */
    public StandaloneModeDialog(Context c){
        super(c);
        initView();
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     * Initializes the view
     */
    private void initView(){

        // Defines the dimensions fo the dialog which may be too small
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View builderView = inflater.inflate(R.layout.dialog_standalonemode, null);
        builderView.setMinimumWidth((int) (displayRectangle.width() * MIN_WIDTH_RATIO));
        builderView.setMinimumHeight((int) (displayRectangle.height() * MIN_HEIGHT_RATIO));
        setContentView(builderView);

        setTitle(R.string.widget_standalone_dialog_title);

        // Fill the view
        String[] titles = getContext().getResources().getStringArray(R.array.standalone_mode_titles);
        String[] descs = getContext().getResources().getStringArray(R.array.standalone_mode_descriptions);
        SwipeItem[] items = new SwipeItem[titles.length];
        for ( int i = 0; i < titles.length ; i++ ){
            items[i] =  new SwipeItem(i, titles[i], descs[i]);
        }

        SwipeSelector swipeSelector = (SwipeSelector) findViewById(R.id.ssStandaloneModeSelector);
        swipeSelector.setItems(items);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) swipeSelector.getLayoutParams();
        lp.height = (int) (displayRectangle.height() * MAX_HEIGHT_RATIO);
        swipeSelector.setLayoutParams(lp);

        // Add listeners on items
        Button btAction = (Button) findViewById(R.id.btCancel);
        btAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btAction = (Button) findViewById(R.id.btCreate);
        btAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandaloneActivity.StandaloneMode mode = getSelection();
                if ( mPositiveButtonListener != null ){
                    SwipeSelector swipeSelector = (SwipeSelector) findViewById(R.id.ssStandaloneModeSelector);
                    int selectedItem = (int) swipeSelector.getSelectedItem().value;
                    String[] titles = getContext().getResources().getStringArray(R.array.standalone_mode_titles);
                    Toast.makeText(getContext(), titles[selectedItem], Toast.LENGTH_LONG).show();
                    mPositiveButtonListener.onPositiveButtonClick(mode);
                    Logger.d(LOG_TAG, "Stand alone mode in use : " + mode.toString());
                }
                dismiss();
            }
        });

    }

    /**
     * Gets the selection of the user
     * 
     * @return StandaloneMode - 
     */
    private StandaloneActivity.StandaloneMode getSelection(){

        SwipeSelector swipeSelector = (SwipeSelector) findViewById(R.id.ssStandaloneModeSelector);
        SwipeItem selectedItem = swipeSelector.getSelectedItem();
        int value = (Integer) selectedItem.value;
        
        // Refer to values/arrays.xml
        final int ALL_POINTS                    = 0;
        final int ALL_POINTS_ACCORDING_SCREEN   = 1;

        switch ( value ){
            case ALL_POINTS:
                return StandaloneActivity.StandaloneMode.ALL_POINTS_WITH_CONFIG;
            case ALL_POINTS_ACCORDING_SCREEN:
                return StandaloneActivity.StandaloneMode.ALL_POINTS_WITH_CONFIG_ACCORDING_SCREEN;
        }

        return StandaloneActivity.StandaloneMode.ALL_POINTS_WITH_CONFIG;

    }

    /**
     *
     * @param l -
     */
    public void setPositiveButtonListener( OnPositiveButtonListener l){
        mPositiveButtonListener = l;
    }


    /* *************** *
     * INNER INTERFACE *
     * *************** */

    /**
     * Interface defining the behaviour of an object looking for a result for this dialog when the
     * OK button is clicked
     */
    public interface OnPositiveButtonListener {
        void onPositiveButtonClick(StandaloneActivity.StandaloneMode userSelection);
    }

}
