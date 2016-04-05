package org.sixpetals.zinrow.stopwatch;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.net.Uri;

import android.content.Intent;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
		extends ActionBarActivity
		implements OnInitListener, BgmFragment.OnFragmentInteractionListener, TimerFragment.OnFragmentInteractionListener  {


	private DisplayManager mDisplayManager;
	private List<RemotePresentation> subDisplays;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		subDisplays = new ArrayList<RemotePresentation>();
		// メニューバー
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// サブディスプレイ
		mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
		Display[] displays = mDisplayManager.getDisplays();
		if (displays.length > 1) {
			for (int i= 1; i < displays.length ; i++ ) {
				Display display = displays[i];
				RemotePresentation presentation = new RemotePresentation(this, display);
				String name = display.getName();
				subDisplays.add(presentation);
				presentation.show();
			}
		}
	}

	public void SetTimeToSubDisplay(int minute, int second){
		for (RemotePresentation presentation : subDisplays) {
			TextView sub_time_min = (TextView) presentation.findViewById(R.id.sub_time_minute_text_id);
			sub_time_min.setText(String.format("%02d", minute));
			TextView sub_time_sec = (TextView) presentation.findViewById(R.id.sub_time_second_text_id);
			sub_time_sec.setText(String.format("%02d", second));

		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			setContentView(R.layout.fragment_subdisplay);

		}
	}
}
