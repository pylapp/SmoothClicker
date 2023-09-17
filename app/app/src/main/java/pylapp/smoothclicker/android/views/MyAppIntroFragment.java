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

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntroFragment;

import pylapp.smoothclicker.android.R;

/**
 * The intro screen.
 * This is an override of the AppIntroFragment so as to set a font size for the TextViews inside
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 18/05/2016
 * @see com.github.paolorotolo.appintro.AppIntroFragment
 */
public class MyAppIntroFragment extends AppIntroFragment {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The pictures to display
     */
    private int mDrawable;
    /**
     * The color of the background
     */
    private int mBackgroundColor;
    /**
     * The color of the title
     */
    private int mTitleColor;
    /**
     * The color of the description
     */
    private int mDescriptionColor;

    /**
     * The title to display
     */
    private CharSequence mTitle;
    /**
     * The description to display
     */
    private CharSequence mDescription;


    /* ********* *
     * CONSTANTS *
     * ********* */

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_DRAWABLE = "drawable";
    private static final String ARG_BG_COLOR = "bg_color";
    private static final String ARG_TITLE_COLOR = "title_color";
    private static final String ARG_DESC_COLOR = "desc_color";

    //private static final String LOG_TAG = MyAppIntroFragment.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     *
     */
    public MyAppIntroFragment(){
        super();
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     *
     * @param title -
     * @param description -
     * @param imageDrawable -
     * @param bgColor -
     * @return MyAppIntroFragment
     */
    public static MyAppIntroFragment newInstance( CharSequence title, CharSequence description, int imageDrawable, int bgColor ){
        return newInstance(title, description, imageDrawable, bgColor, 0, 0);
    }

    /**
     *
     * @param title -
     * @param description -
     * @param imageDrawable -
     * @param bgColor -
     * @param titleColor -
     * @param descColor -
     * @return MyAppIntroFragment -
     */
    public static MyAppIntroFragment newInstance( CharSequence title, CharSequence description, int imageDrawable, int bgColor, int titleColor, int descColor ){

        MyAppIntroFragment sampleSlide = new MyAppIntroFragment();

        Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_DESC, description);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        sampleSlide.setArguments(args);

        return sampleSlide;

    }

    /**
     *
     * @param savedInstanceState -
     */
    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().size() != 0) {
            mDrawable = getArguments().getInt(ARG_DRAWABLE);
            mTitle = getArguments().getCharSequence(ARG_TITLE);
            mDescription = getArguments().getCharSequence(ARG_DESC);
            mBackgroundColor = getArguments().getInt(ARG_BG_COLOR);
            mTitleColor = getArguments().containsKey(ARG_TITLE_COLOR) ? getArguments().getInt(ARG_TITLE_COLOR) : 0;
            mDescriptionColor = getArguments().containsKey(ARG_DESC_COLOR) ? getArguments().getInt(ARG_DESC_COLOR) : 0;
        }

    }

    /**
     *
     * @param inflater -
     * @param container -
     * @param savedInstanceState -
     * @return View -
     */
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){

        View v = inflater.inflate(com.github.paolorotolo.appintro.R.layout.fragment_intro, container, false);
        TextView t = (TextView) v.findViewById(com.github.paolorotolo.appintro.R.id.title);
        t.setTextSize(getResources().getDimension(R.dimen.introscreens_title_text_size));
        TextView d = (TextView) v.findViewById(com.github.paolorotolo.appintro.R.id.description);
        d.setTextSize(getResources().getDimension(R.dimen.introscreens_desc_text_size));
        ImageView i = (ImageView) v.findViewById(com.github.paolorotolo.appintro.R.id.image);
        LinearLayout m = (LinearLayout) v.findViewById(com.github.paolorotolo.appintro.R.id.main);

        t.setText(mTitle);
        if ( mTitleColor != 0 ) t.setTextColor(mTitleColor);

        d.setText(mDescription);
        if ( mDescriptionColor != 0 ) d.setTextColor(mDescriptionColor);

        i.setImageDrawable(ContextCompat.getDrawable(getActivity(), mDrawable));
        m.setBackgroundColor(mBackgroundColor);

        return v;

    }

}
