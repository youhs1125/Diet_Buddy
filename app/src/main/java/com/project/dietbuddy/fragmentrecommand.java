package com.project.dietbuddy;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class fragmentrecommand extends Fragment implements OnMapReadyCallback{
	private GoogleMap mMap;

	//vars
	private Boolean mLocationPermissionsGranted = false;
	private FusedLocationProviderClient mFusedLocationProviderClient;

	@Nullable
	@Override
	public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup
	container, @Nullable Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_recommand, container, false);

		SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		SharedPreferences preferences = getActivity().getSharedPreferences("PREFS", 0);
		SharedPreferences.Editor editor = preferences.edit();
		Switch aSwitch = (Switch) view.findViewById(R.id.notification);
		Boolean aSwitchOn = preferences.getBoolean("isSwitchOn", false);
		if (aSwitchOn) {
			aSwitch.setChecked(true);
		} else {
			aSwitch.setChecked(false);
		}
		aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					editor.putBoolean("isSwitchOn", true).commit();
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.set(Calendar.HOUR_OF_DAY, 9);
					calendar.set(Calendar.MINUTE, 0);

					Intent notifyIntent = new Intent(getActivity(), MyReceiver.class);
					PendingIntent pendingIntent = PendingIntent.getBroadcast
							(getActivity(), 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTimeInMillis(System.currentTimeMillis());
					calendar2.set(Calendar.HOUR_OF_DAY, 11);
					calendar2.set(Calendar.MINUTE, 50);

					Intent notifyIntent2 = new Intent(getActivity(), MyReceiver.class);
					PendingIntent pendingIntent2 = PendingIntent.getBroadcast
							(getActivity(), 2, notifyIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager alarmManager2 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
					alarmManager2.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);

					Calendar calendar3 = Calendar.getInstance();
					calendar3.setTimeInMillis(System.currentTimeMillis());
					calendar3.set(Calendar.HOUR_OF_DAY, 17);
					calendar3.set(Calendar.MINUTE, 50);

					Intent notifyIntent3 = new Intent(getActivity(), MyReceiver.class);
					PendingIntent pendingIntent3 = PendingIntent.getBroadcast
							(getActivity(), 2, notifyIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager alarmManager3 = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
					alarmManager3.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent3);
				} else {
					editor.putBoolean("isSwitchOn", false).commit();
				}
			}
		});
		return view;
	}
	@Override
	public void onMapReady(final GoogleMap googleMap) {

		mMap = googleMap;

		LatLng SEOUL = new LatLng(37.56, 126.97);

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(SEOUL);
		markerOptions.title("서울");
		markerOptions.snippet("한국의 수도");
		mMap.addMarker(markerOptions);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));

	}
}