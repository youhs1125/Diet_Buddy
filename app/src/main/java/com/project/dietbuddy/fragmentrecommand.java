package com.project.dietbuddy;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class fragmentrecommand extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_recommand, container, false);

		SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
		SharedPreferences.Editor editor = preferences.edit();
		Switch aSwitch = (Switch) view.findViewById(R.id.notification);
		Boolean aSwitchOn = preferences.getBoolean("isSwitchOn", false);
		if(aSwitchOn){
			aSwitch.setChecked(true);
		}
		else{
			aSwitch.setChecked(false);
		}
		aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					editor.putBoolean("isSwitchOn", true).commit();
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.set(Calendar.HOUR_OF_DAY, 5);
					calendar.set(Calendar.MINUTE, 39);

					Intent notifyIntent = new Intent(getActivity(), MyReceiver.class);
					PendingIntent pendingIntent = PendingIntent.getBroadcast
							(getActivity(), 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
					//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),1000, pendingIntent);//1000 * 60 * 60 * 24
					//numberPicker.removeView(dialogView);
				}
				else{
					editor.putBoolean("isSwitchOn", false).commit();
				}
			}
		});
		return view;
	}
}