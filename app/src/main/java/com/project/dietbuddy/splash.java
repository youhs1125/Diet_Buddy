package com.project.dietbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import android.provider.Settings.Secure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
		getHashKey();
		Loadingstart();
	}
	private void Loadingstart(){
		Handler handler=new Handler();
		handler.postDelayed(new Runnable(){
			public void run(){
				if(flag.equals("1")){
					startActivity(new Intent(getApplicationContext(), UserInfo.class));
					finish();
				}
				else{
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					finish();
				}
			}
		},1000);
	}

	private void getHashKey(){
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageInfo == null)
			Log.e("KeyHash", "KeyHash:null");

		for (Signature signature : packageInfo.signatures) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			} catch (NoSuchAlgorithmException e) {
				Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
			}
		}
	}
}