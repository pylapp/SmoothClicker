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
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import pylapp.smoothclicker.android.R;

/**
 * The custom view to display several radio button in a grid-liek view
 * in a radio button group/
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.2.0
 * @since 01/07/2016
 */
public class RadioButtonGroupTableLayout extends TableLayout implements View.OnClickListener {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The active radio button
     */
    private RadioButton mActiveRadioButton;


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     * @param context -
     */
    public RadioButtonGroupTableLayout( Context context ){
        super(context);
    }

    /**
     * Constructor
     * @param context -
     * @param attrs -
     */
    public RadioButtonGroupTableLayout( Context context, AttributeSet attrs ){
        super(context, attrs);
    }


    /* ************************ *
     * METHODS FROM TableLayout *
     * ************************ */

    /**
     *
     * @param v -
     */
    @Override
    public void onClick( View v ){
        final RadioButton rb = (RadioButton) v;
        resetExcept(rb.getId());
        if ( mActiveRadioButton != null ) {
            mActiveRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        mActiveRadioButton = rb;
    }

    /**
     *
     * @param child -
     * @param index -
     * @param params -
     */
    @Override
    public void addView( View child, int index, ViewGroup.LayoutParams params ){
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow) child);
        if ( findViewById(R.id.rbUnitTimeS) != null ){
            mActiveRadioButton = (RadioButton) findViewById(R.id.rbUnitTimeS);
        }
    }


    /**
     *
     * @param child -
     * @param params -
     */
    @Override
    public void addView( View child, ViewGroup.LayoutParams params ){
        super.addView(child, params);
        setChildrenOnClickListener((TableRow) child);
    }


    /* ************* *
     * OTHER METHODS *
     * ************* */

    /**
     *
     * @param rb - The active radio button
     */
    public void setActiveRadioButton( RadioButton rb ){
        mActiveRadioButton = rb;
    }

    /**
     * Uncheck all buttons
     */
    public void reset(){
        RadioButton rb = (RadioButton) findViewById(R.id.rbUnitTimeH);
        if ( rb != null ) rb.setChecked(false);
        rb = (RadioButton) findViewById(R.id.rbUnitTimeM);
        if ( rb != null ) rb.setChecked(false);
        rb = (RadioButton) findViewById(R.id.rbUnitTimeS);
        if ( rb != null ) rb.setChecked(false);
        rb = (RadioButton) findViewById(R.id.rbUnitTimeMs);
        if ( rb != null ) rb.setChecked(false);
    }

    /**
     * Uncheck all buttons except the button which has this id
     * @param id - The id of the button to keep
     */
    public void resetExcept( int id ){
        RadioButton rb = (RadioButton) findViewById(R.id.rbUnitTimeH);
        if ( rb != null && rb.getId() != id ) rb.setChecked(false);
        rb = (RadioButton) findViewById(R.id.rbUnitTimeM);
        if ( rb != null && rb.getId() != id ) rb.setChecked(false);
        rb = (RadioButton) findViewById(R.id.rbUnitTimeS);
        if ( rb != null && rb.getId() != id ) rb.setChecked(false);
        rb = (RadioButton) findViewById(R.id.rbUnitTimeMs);
        if ( rb != null && rb.getId() != id ) rb.setChecked(false);
    }

    /**
     *
     * @param tr -
     */
    private void setChildrenOnClickListener( TableRow tr ){
        final int c = tr.getChildCount();
        for ( int i = 0; i < c; i++ ){
            View v = tr.getChildAt(i);
            if ( v instanceof RadioButton ) v.setOnClickListener(this);
        }
    }

    /***
     *
     * @return int -
     */
    public int getCheckedRadioButtonId(){
        if ( mActiveRadioButton != null ) return mActiveRadioButton.getId();
        return -1;
    }

}
