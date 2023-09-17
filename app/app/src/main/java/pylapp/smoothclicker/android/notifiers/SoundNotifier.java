package pylapp.smoothclicker.android.notifiers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import pylapp.smoothclicker.android.R;

/**
 * Utility class which manages sound notifications
 *
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.0.0
 * @since 22/06/2016
 */
public class SoundNotifier {


    /* ********** *
     * ATTRIBUTES *
     * ********** */

    /**
     * The context to use to play sound
     */
    private Context mContext;

    //private static final String LOG_TAG = SoundNotifier.class.getSimpleName();


    /* *********** *
     * CONSTRUCTOR *
     * *********** */

    /**
     * Default constructor
     *
     * @param c - The context to use to play sounds, must not be null
     */
    public SoundNotifier( Context c ){
        super();
        if ( c == null ) throw new IllegalArgumentException("The context param must not be null");
        mContext = c;
    }


    /* ******* *
     * METHODS *
     * ******* */

    /**
     * Makes the device play a sound / a ring
     */
    public void ring( ){
        MediaPlayer mp = MediaPlayer.create(mContext, R.raw.new_click);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setLooping(false);
        mp.start();
    }

}
