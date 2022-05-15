package com.project.dietbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;

public class fragmentgraph extends Fragment {

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

	int cal;
	int carbo;
	int pro;
	int fat;

	private int day;
	private int day_reset;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_graph, container, false);

		search = view.findViewById(R.id.search);
		ok = view.findViewById(R.id.ok);
		listView = view.findViewById(R.id.list);
		preferences = this.getActivity().getSharedPreferences("PREFS", 0);
		editor = preferences.edit();

		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_WEEK);

		day_reset = preferences.getInt("day", 0);
		if (day != day_reset) {
			editor.putString("food", "0");
			editor.putString("foodcal", "0");
			editor.putString("foodcarbo", "0");
			editor.putString("foodpro", "0");
			editor.putString("foodfat", "0");
			editor.putString("foodsalt", "0");
			editor.putInt("day", day);
			editor.commit();
		}

		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), foodsearch.class);
				intent.putExtra("searchFor", search.getText().toString());
				startActivity(intent);
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
		adapter = new NutrientItemAdapter(getActivity(), R.layout.activity_nutrient_item_adapter, items);

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

		BarChart barChart = (BarChart) view.findViewById(R.id.barchart);

		ArrayList<BarEntry> entries = new ArrayList<>();
		cal = Integer.parseInt(preferences.getString("foodcalint", "0"));
		carbo = Integer.parseInt(preferences.getString("foodcarboint", "0"));
		pro = Integer.parseInt(preferences.getString("foodproint", "0"));
		fat = Integer.parseInt(preferences.getString("foodfatint", "0"));
		entries.add(new BarEntry(cal, 0));
		entries.add(new BarEntry(carbo, 1));
		entries.add(new BarEntry(pro, 2));
		entries.add(new BarEntry(fat, 3));

		BarDataSet bardataset = new BarDataSet(entries, "Cells");

		ArrayList<String> labels = new ArrayList<String>();
		labels.add("칼로리");
		labels.add("탄수화물");
		labels.add("단백질");
		labels.add("지방");

		BarData data = new BarData(labels, bardataset);
		barChart.setData(data); // set the data and list of labels into chart
		bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
		barChart.animateY(5000);
		barChart.setDescription("");
		barChart.getLegend().setEnabled(false);

		return view;
	}
}
