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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class fragmentrecommand extends Fragment implements OnMapReadyCallback, Runnable {
	private GoogleMap mMap;
	MarkerOptions myMarker;
	Location location;
	LinkedList<String> parsing_list;
	private final static int UPDATELOCATION = 0;

	//vars
	private Boolean mLocationPermissionsGranted = false;
	private FusedLocationProviderClient mFusedLocationProviderClient;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
			container, @Nullable Bundle savedInstanceState) {
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
		checkDangerousPermissions();
		mMap = googleMap;

		LatLng SEOUL = new LatLng(37.56, 126.97);

		myMarker = new MarkerOptions();
		//myMarker.position(SEOUL);
		//myMarker.title("서울");
		//myMarker.snippet("한국의 수도");
		//mMap.addMarker(myMarker);
		//mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));
		requestMyLocation();
		location = getLocationFromAddress(getContext(), "샐러드");
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public void run()
	{
		cGPlacesAPI placesAPI;
		placesAPI = new cGPlacesAPI(getActivity(), location.getLatitude(), location.getLongitude(), 500, "food|restaurant");
		parsing_list = placesAPI.parsing();
		handler.sendEmptyMessage(UPDATELOCATION);

		while (true)
		{
			try
			{
				Thread.sleep(50);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private Location getLocationFromAddress(Context context, String address) {
		Location resLocation = new Location("현재 위치");
		final Geocoder geocoder = new Geocoder(getContext());
		String value = "세종대학교";
		List<Address> list = null;
		String str = value;
		try {
			list = geocoder.getFromLocationName(str, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (list != null) {
			if (list.size() == 0) {
				Toast.makeText(getContext(), "해당되는 주소 정보를 찾지 못했습니다.", Toast.LENGTH_LONG).show();
				resLocation.setLatitude(37.56);
				resLocation.setLongitude(126.97);
			} else {
				Address addr = list.get(0);
				addr.getLatitude(); // String value에 대한 위도값
				addr.getLongitude(); // String value에 대한 경도값
				resLocation.setLatitude(addr.getLatitude());
				resLocation.setLongitude(addr.getLongitude());
			}
		}
		showMyMarker(resLocation);
		return resLocation;
	}

	private void requestMyLocation() {
		LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location loc_Current = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		String msg = "Latitutde : " + loc_Current.getLatitude() + "\nLongitude : " + loc_Current.getLongitude();
		System.out.println(msg);
		showMyMarker(loc_Current);
	}

	private void showCurrentLocation(Location location) {
		LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

		//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15)); //마커 찍기
		Location targetLocation = new Location("");
		targetLocation.setLatitude(37.4937);
		targetLocation.setLongitude(127.0643);
		showMyMarker(targetLocation);
	}
	private void showMyMarker(Location location) {
		myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
		myMarker.title("◎ 내위치\n");
		myMarker.snippet("세종대학교");
		mMap.addMarker(myMarker);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
	}

	private void showShopMarker(String name, Location location) {
		myMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
		myMarker.title("◎ 식당\n");
		myMarker.snippet(name);
		mMap.addMarker(myMarker);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
	}

	private void checkDangerousPermissions() {
		String[] permissions = {
				android.Manifest.permission.ACCESS_COARSE_LOCATION,
				android.Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_WIFI_STATE
		};
		int permissionCheck = PackageManager.PERMISSION_GRANTED;
		for (int i = 0; i < permissions.length; i++) {
			permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permissions[i]);
			if (permissionCheck == PackageManager.PERMISSION_DENIED) {
				break;
			}
		}
		if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(getActivity(), "권한 있음", Toast.LENGTH_LONG).show();
		}
		else { Toast.makeText(getActivity(), "권한 없음", Toast.LENGTH_LONG).show();
			if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0])) {
				Toast.makeText(getActivity(), "권한 설명 필요함.", Toast.LENGTH_LONG).show();
			}
			else { ActivityCompat.requestPermissions(getActivity(), permissions, 1);
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 1) {
			for (int i = 0; i < permissions.length; i++) {
				if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(getActivity(), permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
				}
				else { Toast.makeText(getActivity(), permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	private Handler handler = new Handler() { //thread에서 사용될 부분을 정의
		@Override
		public void handleMessage(@NonNull Message msg) {
			if (msg.what == UPDATELOCATION){
				for(String i : parsing_list) {
					String[] info = i.split(",");
					Location lo = new Location("");
					lo.setLatitude(Float.parseFloat(info[1]));
					lo.setLongitude(Float.parseFloat(info[2]));
					showShopMarker(info[0], lo);
				}
			}
		}
	};
}