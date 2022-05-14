package com.project.dietbuddy;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class UserInfo extends AppCompatActivity {

	Fragment loseWeight,maintainWeight,bulkUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		loseWeight = new loseWeight();
		maintainWeight = new maintainWeight();
		bulkUp = new bulkUp();

		getSupportFragmentManager().beginTransaction().add(R.id.frame, loseWeight).commit();

		TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

		tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {

				int position = tab.getPosition();

				Fragment selected = null;
				if(position == 0){

					selected = loseWeight;

				}else if (position == 1){

					selected = maintainWeight;

				}else if (position == 2){

					selected = bulkUp;

				}
				getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
	}


}