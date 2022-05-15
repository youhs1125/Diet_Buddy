package com.project.dietbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import android.provider.Settings.Secure;

public class splash extends AppCompatActivity implements LifecycleObserver {

	String flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		SharedPreferences preferences = getSharedPreferences("PREFS", 0);

		flag = preferences.getString("isFirst", "1");

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		Loadingstart();
	}
	private void Loadingstart(){
		Handler handler=new Handler();
		handler.postDelayed(new Runnable(){
			public void run(){
				if(flag == "0"){
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					finish();
				}
				else{
					startActivity(new Intent(getApplicationContext(), UserInfo.class));
					finish();
				}
			}
		},1000);
	}
}