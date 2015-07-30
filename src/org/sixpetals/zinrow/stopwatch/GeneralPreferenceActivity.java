package org.sixpetals.zinrow.stopwatch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class GeneralPreferenceActivity  extends Activity {

    public static final String PREF_SMART_LIST_KEY = "smart_list";
    public static final String PREF_SMART_LIGHT_NONE = "NONE";
    public static final String PREF_SMART_LIGHT_NEXTURN = "NEXTURN";

    public static final String PREF_SMART_LIGHT_KEY = "setting_smart_light_ids";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceFragment fragment = new GeneralPreferenceFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }


    public static class GeneralPreferenceFragment extends PreferenceFragment {
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preference);

            ListPreference smartLightPref = (ListPreference)findPreference(PREF_SMART_LIST_KEY);
            smartLightPref.setSummary(smartLightPref.getEntry());
        }

        @Override
        public void onResume() {
            super.onResume();
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(onPreferenceChangeListener);
        }

        @Override
        public void onPause() {
            super.onPause();
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(onPreferenceChangeListener);
        }

        private SharedPreferences.OnSharedPreferenceChangeListener onPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(PREF_SMART_LIST_KEY)) {
                    ListPreference pref = (ListPreference)findPreference(key);
                    pref.setSummary(pref.getEntry());
                }
            }
        };


    }
}
