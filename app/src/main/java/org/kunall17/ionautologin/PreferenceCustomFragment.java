package org.kunall17.ionautologin;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.MenuItem;

import org.kunall17.ionautologin.Functions.SharedPreferencesClass;

/**
 * Created by kunall17 on 04/02/18.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("ValidFragment")
public class PreferenceCustomFragment extends PreferenceFragment {
    public Preference IDPref;
    SwitchPreference sp_autocheck;
    ListPreference lp;
    SwitchPreference check_wifi;
    private ListPreference check_list;
    private MainActivity mainActivity;

    public PreferenceCustomFragment() {
    }

    public PreferenceCustomFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);
        setHasOptionsMenu(true);

        IDPref = findPreference("studentID");


        check_list = (ListPreference) findPreference("check_list");
        sp_autocheck = (SwitchPreference) findPreference("check_enabled");


        IDPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), ID_list.class);
                startActivityForResult(intent, 2);
                return true;
            }
        });
        sp_autocheck.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (!sp_autocheck.isChecked()) {
                    check_list.setEnabled(true);
                    startTimer();
                } else {
                    if (mainActivity.cdt != null) mainActivity.cdt.cancel();
                    check_list.setEnabled(false);
                }
                return true;
            }
        });

        check_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                mainActivity.minutes = Integer.parseInt(lp.getValue());
                mainActivity.spc.saveIntegerToSharedPreferences(SharedPreferencesClass.SP_MINUTES, mainActivity.minutes);
                return true;
            }
        });

        getAndSetDefaults();

//            ListPreference lp = (ListPreference) findPreference("list_default_id");
//            String[] array = {"1", "2", "3"};
//            CharSequence[] entries = array;
//            CharSequence[] entryValues = array;
//            lp.setEntries(entries);
//            lp.setDefaultValue("1");
//            lp.setEntryValues(entryValues);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainActivity.saveCredentials();
        mainActivity.loginThread.changeCred(mainActivity.defaultUsername, mainActivity.defaultPassword);

    }

    @Override
    public void onResume() {
        super.onResume();
//            saveCredentials();
        mainActivity.loginThread.changeCred(mainActivity.defaultUsername, mainActivity.defaultPassword);
        IDPref.setSummary("Default- " + mainActivity.spc.getDefaultID());

    }

    public void getAndSetDefaults() {
        if (mainActivity.spc.getDefaultID() != null && mainActivity.databaseAdapter.ifUserNameExists(mainActivity.spc.getDefaultID())) {
            IDPref.setSummary("Default- " + mainActivity.spc.getDefaultID());
        }
        sp_autocheck.setChecked(mainActivity.spc.getAutoCheckBoolean());
        check_list.setValue("" + mainActivity.spc.getDefaultMinutes());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startTimer() {
        Log.d("d", "timer started");
        mainActivity.cdt = new CountDownTimer(Integer.MAX_VALUE, mainActivity.minutes * 60000) { //60000
            @Override
            public void onTick(long millisUntilFinished) {
                mainActivity.CheckInternetNow();
            }

            @Override
            public void onFinish() {
                startTimer();
            }
        }.start();
    }
}

