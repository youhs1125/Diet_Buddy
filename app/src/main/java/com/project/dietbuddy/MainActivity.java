package com.project.dietbuddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	EditText search;
	Button ok;
	ListView listView;

	ArrayList<String> list;
	ArrayList<String> list_cal;
	ArrayList<String> list_carbo;
	ArrayList<String> list_fat;
	ArrayList<String> list_pro;
	ArrayList<String> list_salt;
	ArrayList<String> list_gram;
	ArrayList<NutrientItem> items;
	ArrayAdapter adapter;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		search = findViewById(R.id.search);
		ok = findViewById(R.id.ok);
		listView = findViewById(R.id.list);
		preferences = getSharedPreferences("PREFS", 0);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		BarChart barChart = (BarChart) findViewById(R.id.barchart);

		ArrayList<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(8f, 0));
		entries.add(new BarEntry(2f, 1));
		entries.add(new BarEntry(5f, 2));
		entries.add(new BarEntry(20f, 3));
		entries.add(new BarEntry(15f, 4));
		entries.add(new BarEntry(19f, 5));

		BarDataSet bardataset = new BarDataSet(entries, "Cells");

		ArrayList<String> labels = new ArrayList<String>();
		labels.add("2016");
		labels.add("2015");
		labels.add("2014");
		labels.add("2013");
		labels.add("2012");
		labels.add("2011");

		BarData data = new BarData(labels, bardataset);
		barChart.setData(data); // set the data and list of labels into chart
		barChart.setDescription("Set Bar Chart Description Here");  // set the description
		bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
		barChart.animateY(5000);

		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, foodsearch.class);
				intent.putExtra("searchFor", search.getText().toString());
				startActivity(intent);
				finish();
			}
		});

		list = new ArrayList<>();
		list_cal = new ArrayList<>();
		list_carbo = new ArrayList<>();
		list_fat = new ArrayList<>();
		list_pro = new ArrayList<>();
		list_salt = new ArrayList<>();
		list_gram = new ArrayList<>();
		items = new ArrayList<>();
		adapter = new NutrientItemAdapter(this, R.layout.activity_nutrient_item_adapter, items);

		if (preferences.getStringSet("foodgram", null) != null) {
			list.addAll(preferences.getStringSet("food", null));
			list_cal.addAll(preferences.getStringSet("foodcal", null));
			list_carbo.addAll(preferences.getStringSet("foodcarbo", null));
			list_pro.addAll(preferences.getStringSet("foodpro", null));
			list_fat.addAll(preferences.getStringSet("foodfat", null));
			list_salt.addAll(preferences.getStringSet("foodsalt", null));
			list_gram.addAll(preferences.getStringSet("foodgram", null));
			int counter = 0;
			for(String i : list){
				String[] title = i.split(",");

				String[] gram = list_gram.get(counter).split(",");
				items.add(new NutrientItem(title[0], gram[1] + "g"));
				counter++;
			}
		}
		else{
			listView.setBackgroundResource(R.drawable.listblank);
		}
		listView.invalidateViews();
		listView.setAdapter(adapter);
		if(list.isEmpty()){
			listView.setBackgroundResource(R.drawable.listblank);
			listView.invalidateViews();
		}
		else{
			listView.setBackgroundColor(Color.argb(0, 235,239,242));
			listView.invalidateViews();
		}
	}
}