package com.project.dietbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import android.provider.Settings.Secure;

public class splash extends AppCompatActivity implements LifecycleObserver {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		Loadingstart();
	}
	private void Loadingstart(){
		Handler handler=new Handler();
		handler.postDelayed(new Runnable(){
			public void run(){
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		},1000);
	}
}