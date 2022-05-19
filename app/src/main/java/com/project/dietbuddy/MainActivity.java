package com.project.dietbuddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity {

	Fragment fragment0, fragment1, fragment2, fragment3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		fragment0 = new fragmentgraph();
		fragment1 = new fragmentrecommand();
		fragment2 = new fragmentinfo();



		getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment0).commit();

		TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

		tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {

				int position = tab.getPosition();

				Fragment selected = null;
				if(position == 0){

					selected = fragment0;

				}else if (position == 1){

					selected = fragment1;

				}else if (position == 2) {

					selected = fragment2;

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