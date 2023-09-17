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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import pylapp.smoothclicker.android.R;


/**
 * The activity which displays the credits / third-parties licences.
 *
 * @author Pierre-Yves Lapersonne
 * @version 3.0.0
 * @since 15/03/2016
 */
public class CreditsActivity extends AppCompatActivity {


    //private static final String LOG_TAG = CreditsActivity.class.getSimpleName();


    /* ****************************** *
     * METHODS FROM AppCompatActivity *
     * ****************************** */

    /**
     *
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        ListView lv = (ListView)findViewById(R.id.list);
        String[] labels = getApplicationContext().getResources().getStringArray(R.array.credits_labels);
        String[] descriptions = getApplicationContext().getResources().getStringArray(R.array.credits_descriptions);
        String[] urls = getApplicationContext().getResources().getStringArray(R.array.credits_urls);
        String[] licenses = getApplicationContext().getResources().getStringArray(R.array.credits_licenses);
        String[] thanksLabels = getApplicationContext().getResources().getStringArray(R.array.thanks_labels);
        String[] thanksFlags = getApplicationContext().getResources().getStringArray(R.array.thanks_flags);
        String[] thanksWhos = getApplicationContext().getResources().getStringArray(R.array.thanks_whos);
        lv.setAdapter(new CreditsBaseAdapter( labels, descriptions, urls, licenses, thanksLabels, thanksFlags, thanksWhos, this));
    }

    /* *********** *
     * INNER CLASS *
     * *********** */

    /**
     * An adapter for the list of credits
     */
    public static class CreditsBaseAdapter extends BaseAdapter {

        String [] creditsLabels;
        String [] creditsDescriptions;
        String [] creditsUrls;
        String [] creditsLicenses;
        String [] thanksLabels;
        String [] thanksFlags;
        String [] thanksWhos;

        Context context;
        LayoutInflater layoutInflater;

        public CreditsBaseAdapter( String [] creditsLabels, String[] creditsDescriptions, String [] creditsUrls, String[] creditsLicenses,
                                   String [] thanksLabels, String [] thanksFlags, String[] thanksWhos, Context context ){
            super();
            if ( creditsLabels == null ) throw new IllegalArgumentException("Labels cannot be null!");
            if ( creditsDescriptions == null ) throw new IllegalArgumentException("Descriptions cannot be null!");
            if ( creditsUrls == null ) throw new IllegalArgumentException("URLs cannot be null!");
            if ( creditsLicenses == null ) throw new IllegalArgumentException("Licenses cannot be null!");
            if ( thanksLabels == null ) throw new IllegalArgumentException("Thanks labels cannot be null!");
            if ( thanksWhos == null ) throw new IllegalArgumentException("Thanks whoes cannot be null!");
            if ( thanksFlags == null ) throw new IllegalArgumentException("Thanks flags cannot be null!");
            if ( context == null ) throw new IllegalArgumentException("Context cannot be null!");
            this.creditsLabels = creditsLabels;
            this.creditsDescriptions = creditsDescriptions;
            this.creditsUrls = creditsUrls;
            this.creditsLicenses = creditsLicenses;
            this.thanksLabels = thanksLabels;
            this.thanksFlags = thanksFlags;
            this.thanksWhos = thanksWhos;
            this.context = context;
            layoutInflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount(){
            return creditsLabels.length + thanksLabels.length;
        }

        @Override
        public Object getItem( int position ){
            return null;
        }

        @Override
        public long getItemId( int position ){
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent ){

            // First case, we have not displayed all the common credits
            if ( position < creditsLabels.length ) {

                // Get the data
                String label = creditsLabels[position];
                String description = creditsDescriptions[position];
                String url = creditsUrls[position];
                String license = creditsLicenses[position];

                // Build a view
                convertView = layoutInflater.inflate(R.layout.content_credit_row, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_credits_label);
                tv.setText(label);
                tv = (TextView) convertView.findViewById(R.id.tv_credits_description);
                tv.setText(description);
                tv = (TextView) convertView.findViewById(R.id.tv_credits_url);
                tv.setText(url);
                tv = (TextView) convertView.findViewById(R.id.tv_credits_license);
                tv.setText(license);

            // Other case, we can now display the thanks credits
            } else {

                // Get the data
                int fixedPosition = position - creditsLabels.length;
                String label = thanksLabels[fixedPosition];
                String who = thanksWhos[fixedPosition];
                String flag = thanksFlags[fixedPosition];

                // Build a view
                convertView = layoutInflater.inflate(R.layout.content_thanks_row, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_thanks_label);
                tv.setText(label);

                tv = (TextView) convertView.findViewById(R.id.tv_thanks_who);
                tv.setText(who);

                ImageView iv = (ImageView) convertView.findViewById(R.id.iv_thanks_flag);
                switch ( Flags.valueOf(flag) ){
                    case RU:
                        iv.setImageResource(R.drawable.credits_flag_ru);
                        break;
                    case IT:
                        iv.setImageResource(R.drawable.credits_flag_it);
                        break;
                    case FR:
                        iv.setImageResource(R.drawable.credits_flag_fr);
                        break;
                    case EN:
                        iv.setImageResource(R.drawable.credits_flag_en);
                        break;
                }

            }

            return convertView;

        } // End of public View getView( int position, View convertView, ViewGroup parent )

        /**
         * An enumeration of flags to display
         */
        private enum Flags {
            /**
             * Russia - Russian - ru
             */
            RU,
            /**
             * Italy - Italian - it
             */
            IT,
            /**
             * France - French - fr
             */
            FR,
            /**
             * United-Kingdom - English - en
             */
            EN
        } // End of private enum Flags

    } // End of public static class CreditsBaseAdapter extends BaseAdapter

} // End of public class CreditsActivity extends AppCompatActivity
