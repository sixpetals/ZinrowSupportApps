package org.sixpetals.zinrow.stopwatch;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BgmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BgmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BgmFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

        public static BgmFragment newInstance() {
        BgmFragment fragment = new BgmFragment();
        return fragment;
}

    public BgmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_bgm, container, false);
        // BGM
        rootView.findViewById(R.id.opening_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.opening);
                    }
                });
        rootView.findViewById(R.id.confirm_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.confirm);
                    }
                });
        rootView.findViewById(R.id.morning_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.morning);
                    }
                });
        rootView.findViewById(R.id.discussion_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.discussion);
                    }
                });
        rootView.findViewById(R.id.vote_bgm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFromMediaPlayer(R.raw.vote);
            }
        });
        rootView.findViewById(R.id.execution_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.execution);
                    }
                });
        rootView.findViewById(R.id.night_bgm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFromMediaPlayer(R.raw.night);
            }
        });

        rootView.findViewById(R.id.no_bgm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMediaPlayer();
            }
        });

        rootView.findViewById(R.id.victory_warewolf_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.victory_warewolf);
                    }
                });

        rootView.findViewById(R.id.victory_villager_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.victory_villager);
                    }
                });

        rootView.findViewById(R.id.victory_thirdparty_bgm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playFromMediaPlayer(R.raw.victory_thirdparty);
                    }
                });

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }




}
