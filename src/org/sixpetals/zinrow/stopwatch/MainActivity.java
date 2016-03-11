package org.sixpetals.zinrow.stopwatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import android.app.Application;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Handler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.app.TimePickerDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import android.app.Fragment;

public class MainActivity
		extends ActionBarActivity
		implements OnInitListener, BgmFragment.OnFragmentInteractionListener, TimerFragment.OnFragmentInteractionListener  {


	private DisplayManager mDisplayManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        //メニュー
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        //サブディスプレイ
		mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
		Display[] displays = mDisplayManager.getDisplays();
		if (displays.length > 1) {
			for (Display display : displays) {
				RemotePresentation presentation = new RemotePresentation(this, display);
				String name = display.getName();
				presentation.show();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
                Intent intent = new Intent(this, GeneralPreferenceActivity.class);
                startActivity(intent);
		}else if(id == R.id.action_exit){
			this.moveTaskToBack(true);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onInit(int i) {

	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}


	private class RemotePresentation extends Presentation
	{
		public RemotePresentation(Context context, Display display) {
			super(context, display);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.subdisplay_main);
		}
	}
}
