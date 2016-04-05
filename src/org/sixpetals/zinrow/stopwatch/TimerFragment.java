package org.sixpetals.zinrow.stopwatch;

import android.app.Activity;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;

    // Timer
    private TextView timer_second;
    private TextView timer_minute;
    private TextView sub_timer_second;
    private TextView sub_timer_minute;

    private Button start, stop;
    private LinearLayout editGroup;
    private MyCountDownTimer cdt;
    private boolean notice5_flag = false;
    private boolean finished_flag = false;
    private DisplayManager mDisplayManager;

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public TimerFragment()

    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_timer, container, false);

        //View subView =  inflater.inflate(R.layout.fragment_subdisplay, container, false);

        // タイマー
        timer_minute = (TextView) rootView.findViewById(R.id.time_minute_text_id);
        timer_second = (TextView) rootView.findViewById(R.id.time_second_text_id);


        /*
        sub_timer_minute = (TextView) subView.findViewById(R.id.sub_time_minute_text_id);
        sub_timer_second = (TextView) subView.findViewById(R.id.sub_time_second_text_id);
        */

        start = (Button) rootView.findViewById(R.id.start_button_id);
        stop = (Button) rootView.findViewById(R.id.stop_button_id);
        editGroup = (LinearLayout)rootView.findViewById(R.id.edit_buttons_id);

        //１分プラス
        rootView.findViewById(R.id.minute_plus_button_id).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int exist_min = Integer.parseInt(timer_minute.getText().toString());
                        timer_minute.setText(String.format("%02d", exist_min+1));
                    }
                }
        );

        // 10秒プラス
        rootView.findViewById(R.id.second_plus_button_id).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int exist_min = Integer.parseInt(timer_minute.getText().toString());
                        int exist_sec = Integer.parseInt(timer_second.getText().toString());
                        int new_sec = exist_sec+10;
                        if  (new_sec > 59){
                            timer_minute.setText(String.format("%02d",exist_min+1));
                            new_sec = 0;
                        }
                        timer_second.setText(String.format("%02d",new_sec));
                    }
                }
        );

        //1分マイナス
        rootView.findViewById(R.id.minute_minus_button_id).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int exist_min = Integer.parseInt(timer_minute.getText().toString());
                        int new_min = exist_min-1;
                        if  (new_min < 0){
                            new_min = 0;
                        }
                        timer_minute.setText(String.format("%02d", new_min));
                    }
                }
        );

        rootView.findViewById(R.id.second_minus_button_id).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int exist_min = Integer.parseInt(timer_minute.getText().toString());
                        int exist_sec = Integer.parseInt(timer_second.getText().toString());
                        int new_sec = exist_sec-10;
                        if  (new_sec < 0){
                            int new_min = exist_min -1;
                            if( new_min < 0) {
                                new_min = 0;
                                new_sec = 0;
                            }else{
                                new_sec = 50;
                            }
                            timer_minute.setText(String.format("%02d",new_min));

                        }
                        timer_second.setText(String.format("%02d",new_sec));
                    }
                }
        );

        rootView.findViewById(R.id.reset_button_id).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer_minute.setText(String.format("%02d", 5));
                        timer_second.setText(String.format("%02d", 0));
                    }
                }
        );

        // CountDownの初期値
        stop.setEnabled(false);
        start.setEnabled(true);
        editGroup.setVisibility(View.VISIBLE) ;

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start.setEnabled(false);
                stop.setEnabled(true);
                editGroup.setVisibility(View.INVISIBLE);
                String m = timer_minute.getText().toString();
                String s = timer_second.getText().toString();
                long init_time = Long.parseLong(m) * 60 * 1000
                        + Long.parseLong(s) * 1000;
                cdt = new MyCountDownTimer(init_time, 500);
                cdt.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop.setEnabled(false);
                start.setEnabled(true);
                editGroup.setVisibility(View.VISIBLE);
                if (cdt != null) {
                    cdt.cancel();
                    cdt = null;
                }
            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onInit(int status) {
        super.onInit(status);

        if (TextToSpeech.SUCCESS == status) {
            Locale locale = Locale.ENGLISH;
            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(locale);
            }
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (tts != null) {
            tts.shutdown();
        }
    }



    public void SetTimeToSubDisplay(int min,int sec){
        ((MainActivity)getActivity()).SetTimeToSubDisplay(min,sec);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }



        @Override
        public void onFinish() {
            timer_second.setText("00");
            timer_minute.setText("00");

            SetTimeToSubDisplay(0,0);

            notice5_flag = false;
            finished_flag = false;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int min =(int)(millisUntilFinished / 1000 / 60);
            int sec =(int)(millisUntilFinished / 1000 % 60);
            String minute_str = String.format("%02d", min);
            String second_str = String.format("%02d",sec);
            timer_minute.setText(minute_str);
            timer_second.setText(second_str);
            SetTimeToSubDisplay(min,sec);


            if (notice5_flag == false && millisUntilFinished < 60 * 1000 && millisUntilFinished > 55 * 1000) {
                notice5_flag = true;
                playFromSoundPool(R.raw.lightning);
                speechText("のこり一分です");
            }else if(finished_flag == false && millisUntilFinished < 1 * 1000) {
                finished_flag = true;
                playFromSoundPool(R.raw.clock_bell);
                speechText("時間になりました");
            }
        }

    }
}
