package org.sixpetals.zinrow.stopwatch;

import android.app.Activity;
import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by takuma.sugimoto on 2015/12/10.
 */
public abstract class BaseFragment extends Fragment implements TextToSpeech.OnInitListener{
    // tts
    protected TextToSpeech tts;

    public void speechText(String string) {
        if (0 < string.length()) {
            if (tts.isSpeaking()) {
                tts.stop();
            }
            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    // SoundPool
    protected SoundPool mSoundPool;
    protected HashMap<Integer, Integer> soundHash = new HashMap<Integer, Integer>();

    // MediaPlayer
    protected MediaPlayer mMediaPlayer;

    public void playFromSoundPool(View view, int resId) {
        this.mSoundPool.play(soundHash.get(resId), 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playFromSoundPool(int resId) {
        mSoundPool.play(soundHash.get(resId), 1.0F, 1.0F, 0, 0, 1.0F);
    }

    public void playFromMediaPlayer(int resid) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
            }
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        mMediaPlayer = MediaPlayer.create(this.getActivity(), resid);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    public void stopMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
            }
        }
    }


    @Override
    public void onAttach (Activity activity) {
        super.onAttach (activity);

        tts = new TextToSpeech(this.getActivity(), this);
    }


    @Override
    public void onResume() {
        super.onResume();

        this.mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundHash.put(R.raw.lightning, this.mSoundPool.load(this.getActivity(), R.raw.lightning, 1));
        soundHash.put(R.raw.clock_bell, this.mSoundPool.load(this.getActivity(), R.raw.clock_bell, 1));
    }

    @Override
    public void onPause() {
        super.onPause();
        mSoundPool.release();
    }

    @Override
    public void onDestroy() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }

        if(mSoundPool != null) {
            mSoundPool.release();
        }

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status) {
            Locale locale = Locale.ENGLISH;
            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(locale);
            }
        }
    }

}
