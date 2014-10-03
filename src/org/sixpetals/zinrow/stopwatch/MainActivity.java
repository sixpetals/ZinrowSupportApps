package org.sixpetals.zinrow.stopwatch;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnInitListener {

	// SoundPool
	private SoundPool mSoundPool;
	private int mSoundId;

	// Timer
	TextView timer_second;
	TextView timer_minute;
	Button start, stop;
	MyCountDownTimer cdt;
	boolean notice_flag = false;

	//tts
	TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//TTS
		tts = new TextToSpeech(this, this);


		// 効果音
		findViewById(R.id.lightning_sound).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						playFromSoundPool();
					}
				});

		// BGM
		findViewById(R.id.opening_bgm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						playFromMediaPlayer(R.raw.opening);
					}
				});

		// タイマー
		timer_second = (TextView) findViewById(R.id.time_second_text_id);
		timer_minute = (TextView) findViewById(R.id.time_minute_text_id);
		start = (Button) findViewById(R.id.start_button_id);
		stop = (Button) findViewById(R.id.stop_button_id);

		// CountDownの初期値
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				start.setEnabled(false);
				stop.setEnabled(true);
				String s = timer_second.getText().toString();
				String m = timer_minute.getText().toString();
				long init_time =  Long.parseLong(s) * 60 * 1000 + Long.parseLong(m) * 1000 ;
				cdt = new MyCountDownTimer(init_time, 500);
				cdt.start();
			}
		});

		stop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stop.setEnabled(false);
				start.setEnabled(true);
				if (cdt != null){
					cdt.cancel();
					cdt = null;
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void playFromSoundPool(View view) {
		this.mSoundPool.play(this.mSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		this.mSoundId = this.mSoundPool.load(this, R.raw.lightning, 1);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSoundPool.release();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
        if (null != tts) {
            tts.shutdown();
        }
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

	private void playFromSoundPool() {
		mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
	}

	private void playFromMediaPlayer(int resid) {
		MediaPlayer mMediaPlayer = MediaPlayer.create(this, resid);
		mMediaPlayer.start();
	}

	private void speechText(String string) {
        if (0 < string.length()) {
            if (tts.isSpeaking()) {
                tts.stop();
            }
            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onFinish() {
			timer_second.setText("00");
			timer_minute.setText("00");
			notice_flag = false;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			timer_second
					.setText(Long.toString(millisUntilFinished / 1000 / 60));
			timer_minute
					.setText(Long.toString(millisUntilFinished / 1000 % 60));

			if( notice_flag == false && millisUntilFinished < 60 * 1000  ){
				notice_flag = true;
				speechText("のこり一分です");
			}
		}


	}




}
