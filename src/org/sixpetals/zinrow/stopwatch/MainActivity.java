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
import android.app.TimePickerDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnInitListener {

	// SoundPool
	private SoundPool mSoundPool;
	private int mSoundId;


	//MediaPlayer
	private MediaPlayer mMediaPlayer;

	// Timer
	private TextView timer_second;
	private TextView timer_minute;
	private Button start, stop;
	private MyCountDownTimer cdt;
	private boolean notice_flag = false;

	// tts
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TTS
		tts = new TextToSpeech(this, this);

		// BGM
		findViewById(R.id.opening_bgm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						playFromMediaPlayer(R.raw.opening);
					}
				});

		findViewById(R.id.confirm_bgm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						playFromMediaPlayer(R.raw.confirm);
					}
				});
		findViewById(R.id.morning_bgm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						playFromMediaPlayer(R.raw.morning);
					}
				});
		findViewById(R.id.discussion_bgm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						playFromMediaPlayer(R.raw.discussion);
					}
				});
		findViewById(R.id.vote_bgm).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playFromMediaPlayer(R.raw.vote);
			}
		});
		findViewById(R.id.execution_bgm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						playFromMediaPlayer(R.raw.execution);
					}
				});
		findViewById(R.id.night_bgm).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playFromMediaPlayer(R.raw.night);
			}
		});

		// タイマー
		timer_minute = (TextView) findViewById(R.id.time_minute_text_id);
		timer_second = (TextView) findViewById(R.id.time_second_text_id);
		start = (Button) findViewById(R.id.start_button_id);
		stop = (Button) findViewById(R.id.stop_button_id);

		findViewById(R.id.time_set_view_id).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int exist_min = Integer.parseInt( timer_minute.getText().toString());
				int exist_sec =  Integer.parseInt( timer_second.getText().toString());

		        TimePickerDialog timepick= new TimePickerDialog(
		        		MainActivity.this,
		                new TimePickerDialog.OnTimeSetListener() {
		                    public void onTimeSet(TimePicker view,   int minute, int second) {
		            			timer_minute.setText(String.format("%02d", minute));
		            			timer_second.setText(String.format("%02d", second));
		                        }
		                },
		                exist_min,
		                exist_sec,
		                true);

		            // 表示
		            timepick.show();
			}
		});


		// CountDownの初期値
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				start.setEnabled(false);
				stop.setEnabled(true);
				String m = timer_minute.getText().toString();
				String s = timer_second.getText().toString();
				long init_time = Long.parseLong(m) * 60 * 1000
						+ Long.parseLong(s) * 1000;
				cdt = new MyCountDownTimer(init_time, 500);
				cdt.start();
			}
		});

		stop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stop.setEnabled(false);
				start.setEnabled(true);
				if (cdt != null) {
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
		if (mMediaPlayer != null ){
			mMediaPlayer.stop();
		}
		mMediaPlayer = null;
		mMediaPlayer = MediaPlayer.create(this, resid);
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
			timer_minute
					.setText(String.format("%02d", (millisUntilFinished / 1000 / 60)));
			timer_second
					.setText(String.format("%02d", (millisUntilFinished / 1000 % 60)));

			if (notice_flag == false && millisUntilFinished < 60 * 1000) {
				notice_flag = true;
				playFromSoundPool();
				speechText("のこり一分です");
			}
		}

	}

}