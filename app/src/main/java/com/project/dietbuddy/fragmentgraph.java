package com.project.dietbuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;

public class fragmentgraph extends Fragment {

	EditText search;
	Button ok;
	ListView listView;
	TextView calendar_tv;

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

	int y;
	int m;
	int d;

	int blue_color = Color.rgb(90,66,235);
	int red_color = Color.rgb(235,91,74);

	BarChart barChart;
	ArrayList<BarEntry> entries;
	BarData data;
	BarDataSet bardataset;

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
		calendar_tv = view.findViewById(R.id.calendar);

		preferences = this.getActivity().getSharedPreferences("PREFS", 0);
		editor = preferences.edit();

		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_WEEK);

		y = calendar.get(Calendar.YEAR);
		m = calendar.get(Calendar.MONTH) + 1;
		d = calendar.get(Calendar.DATE);
		calendar_tv.setText(y+"-"+m+"-"+d);
		calendar_tv.invalidate();
		if(preferences.getString("calendar_date", "") == ""){
			editor.putString("calendar_date", y+"-"+m+"-"+d);
			editor.commit();
		}

		day_reset = preferences.getInt("day", 0);
		if (day != day_reset) {
			LinkedHashSet<String> set = new LinkedHashSet<String>();
			editor.putStringSet("food"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), set);
			editor.putStringSet("foodcal"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), set);
			editor.putStringSet("foodcarbo"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), set);
			editor.putStringSet("foodpro"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), set);
			editor.putStringSet("foodfat"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), set);
			editor.putStringSet("foodsalt"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), set);

			editor.putString("foodint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0");
			editor.putString("foodcalint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0");
			editor.putString("foodcarboint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0");
			editor.putString("foodproint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0");
			editor.putString("foodfatint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0");
			editor.putString("foodsaltint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0");
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

		calendar_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDate();
			}
		});

		makeList();

		barChart = (BarChart) view.findViewById(R.id.barchart);

		entries = new ArrayList<>();
		cal = Integer.parseInt(preferences.getString("foodcalint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
		carbo = Integer.parseInt(preferences.getString("foodcarboint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
		pro = Integer.parseInt(preferences.getString("foodproint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
		fat = Integer.parseInt(preferences.getString("foodfatint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
		entries.add(new BarEntry(cal, 0));
		entries.add(new BarEntry(preferences.getInt("totalCal", 0), 1));
		entries.add(new BarEntry(carbo, 2));
		entries.add(new BarEntry(preferences.getInt("carb", 0), 3));
		entries.add(new BarEntry(pro, 4));
		entries.add(new BarEntry(preferences.getInt("protein", 0), 5));
		entries.add(new BarEntry(fat, 6));
		entries.add(new BarEntry(preferences.getInt("fat", 0), 7));

		bardataset = new BarDataSet(entries, "Cells");

		ArrayList<String> labels = new ArrayList<String>();
		labels.add("칼로리");
		labels.add("목표 칼로리");
		labels.add("탄수화물");
		labels.add("목표 탄수화물");
		labels.add("단백질");
		labels.add("목표 단백질");
		labels.add("지방");
		labels.add("목표 지방");

		data = new BarData(labels, bardataset);
		barChart.setData(data); // set the data and list of labels into chart
		int[] colors = {blue_color, Color.rgb(246,204,52),
				blue_color, Color.rgb(246,204,52),
				blue_color, Color.rgb(246,204,52),
				blue_color, Color.rgb(246,204,52)};

		if(preferences.getInt("totalCal", 0) < cal){
			colors[0] = red_color;
		}
		if(preferences.getInt("carb", 0) < cal){
			colors[2] = red_color;
		}
		if(preferences.getInt("protein", 0) < pro){
			colors[4] = red_color;
		}
		if(preferences.getInt("fat", 0) < fat){
			colors[4] = red_color;
		}
		ArrayList<Integer> color_array = new ArrayList<Integer>();

		for(int c: colors) color_array.add(c);
		bardataset.setColors(color_array);
		barChart.animateY(5000);
		barChart.setDescription("");
		barChart.getLegend().setEnabled(false);

		return view;
	}
	void showDate() {
		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				y = year;
				m = month + 1;
				d = dayOfMonth;
				calendar_tv.setText(y+"-"+m+"-"+d);
				calendar_tv.invalidate();

				makeList();

				cal = Integer.parseInt(preferences.getString("foodcalint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
				carbo = Integer.parseInt(preferences.getString("foodcarboint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
				pro = Integer.parseInt(preferences.getString("foodproint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
				fat = Integer.parseInt(preferences.getString("foodfatint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), "0"));
				bardataset.removeEntry(0);
				bardataset.removeEntry(1);
				bardataset.removeEntry(2);
				bardataset.removeEntry(3);
				bardataset.removeEntry(4);
				bardataset.removeEntry(5);
				bardataset.removeEntry(6);

				int[] colors = {blue_color, Color.rgb(246,204,52),
						blue_color, Color.rgb(246,204,52),
						blue_color, Color.rgb(246,204,52),
						blue_color, Color.rgb(246,204,52)};
				if(preferences.getInt("totalCal", 0) < cal){
					colors[0] = red_color;
				}
				if(preferences.getInt("carb", 0) < cal){
					colors[2] = red_color;
				}
				if(preferences.getInt("protein", 0) < pro){
					colors[4] = red_color;
				}
				if(preferences.getInt("fat", 0) < fat){
					colors[4] = red_color;
				}
				ArrayList<Integer> color_array = new ArrayList<Integer>();

				for(int c: colors) color_array.add(c);
				bardataset.setColors(color_array);
				entries.add(new BarEntry(cal, 0));
				entries.add(new BarEntry(preferences.getInt("totalCal", 0), 1));
				entries.add(new BarEntry(carbo, 2));
				entries.add(new BarEntry(preferences.getInt("carb", 0), 3));
				entries.add(new BarEntry(pro, 4));
				entries.add(new BarEntry(preferences.getInt("protein", 0), 5));
				entries.add(new BarEntry(fat, 6));
				entries.add(new BarEntry(preferences.getInt("fat", 0), 7));
				barChart.invalidate();

				editor.putString("calendar_date", y+"-"+m+"-"+d);
				editor.commit();
			}
		},y, m - 1, d);

		//datePickerDialog.setMessage("메시지");
		datePickerDialog.show();
	}
	void makeList(){
		list = new ArrayList<>();
		list_cal = new ArrayList<>();
		list_carbo = new ArrayList<>();
		list_fat = new ArrayList<>();
		list_pro = new ArrayList<>();
		list_salt = new ArrayList<>();
		list_gram = new ArrayList<>();
		items = new ArrayList<>();
		adapter = new NutrientItemAdapter(getActivity(), R.layout.activity_nutrient_item_adapter, items);

		if (preferences.getStringSet("foodgram"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null) != null) {
			list.addAll(preferences.getStringSet("food"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null));
			list_cal.addAll(preferences.getStringSet("foodcal"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null));
			list_carbo.addAll(preferences.getStringSet("foodcarbo"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null));
			list_pro.addAll(preferences.getStringSet("foodpro"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null));
			list_fat.addAll(preferences.getStringSet("foodfat"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null));
			list_salt.addAll(preferences.getStringSet("foodsalt"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null));
			list_gram.addAll(preferences.getStringSet("foodgram"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(d), null));
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
