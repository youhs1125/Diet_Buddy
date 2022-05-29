package com.project.dietbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class foodsearch extends AppCompatActivity {

	XmlPullParser xpp;

	String key="c844a29044064ad494bc";
//	LinkedHashSet<String> data;
	StringSettingList data;

	private ProgressDialog progressDialog;
	private String text;
	private ListView listView;
	private LinkedHashSet<String> menu;
	private ArrayAdapter adapter;
	private List list;

	private List list_cal;
	private List list_carbo;
	private List list_pro;
	private List list_fat;
	private List list_salt;
	private List list_gram;

	private List searchlist;
	private List searchlist_cal;
	private List searchlist_carbo;
	private List searchlist_pro;
	private List searchlist_fat;
	private List searchlist_salt;
	private List searchlist_gram;

	double multiply;
	boolean flag;
	AlertDialog alertDialog;

	int y;
	int m;
	int dd;

	private ArrayList<NutrientItem> searchListNewLayout;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String userID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foodsearch);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		listView = findViewById(R.id.list);

		preferences = getSharedPreferences("PREFS", 0);
		editor = preferences.edit();

		String date = preferences.getString("calendar_date", "0-0-0");
		String[] now_date = date.split("-");
		y = Integer.parseInt(now_date[0]);
		m = Integer.parseInt(now_date[1]);
		dd = Integer.parseInt(now_date[2]);

		list = new ArrayList<>();
		list_cal = new ArrayList<>();
		list_carbo = new ArrayList<>();
		list_pro = new ArrayList<>();
		list_fat = new ArrayList<>();

		searchlist = new ArrayList<>();
		searchlist_cal = new ArrayList<>();
		searchlist_carbo = new ArrayList<>();
		searchlist_pro = new ArrayList<>();
		searchlist_fat = new ArrayList<>();
		list_salt = new ArrayList<>();
		searchlist_salt = new ArrayList();
		list_gram = new ArrayList<>();
		searchlist_gram = new ArrayList();

		searchListNewLayout = new ArrayList<>();

		adapter = new NutrientItemAdapter(getApplicationContext(), R.layout.activity_nutrient_item_adapter, searchListNewLayout);

		progressDialog = ProgressDialog.show(foodsearch.this, "검색 중입니다.", "loading");
		new Thread(new Runnable() {
			@Override
			public void run() {
				data = getXmlData();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(data == null){
							noHandler.sendEmptyMessage(0);
							startActivity(new Intent(foodsearch.this, MainActivity.class));
							finish();
						}
						else{
							Message msg = handler.obtainMessage();
							handler.sendMessage(msg);
						}
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						handler.sendEmptyMessage(0);
					}
				});
			}
		}).start();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {



				StringSettingList ssl = new StringSettingList();
				StringSettingList ssl_cal = new StringSettingList();
				StringSettingList ssl_carbo = new StringSettingList();
				StringSettingList ssl_pro = new StringSettingList();
				StringSettingList ssl_fat = new StringSettingList();
				StringSettingList ssl_salt = new StringSettingList();
				StringSettingList ssl_gram = new StringSettingList();

				String _title = searchlist.get(a_position).toString();
				String _cal = searchlist_cal.get(a_position).toString();
				String _carbo = searchlist_carbo.get(a_position).toString();
				String _pro = searchlist_pro.get(a_position).toString();
				String _fat = searchlist_fat.get(a_position).toString();
				String _salt = searchlist_salt.get(a_position).toString();
				String _gram = searchlist_gram.get(a_position).toString();

				final AlertDialog.Builder d = new AlertDialog.Builder(foodsearch.this);
				LayoutInflater inflater = getLayoutInflater();

				final View dialogView = inflater.inflate(R.layout.activity_nutrient_dialog, null);
				d.setView(dialogView);
				//d.setTitle(" ");
				TextView title = dialogView.findViewById(R.id.titleInd);
				TextView carbo = dialogView.findViewById(R.id.carboInd);
				TextView pro = dialogView.findViewById(R.id.proInd);
				TextView fat = dialogView.findViewById(R.id.fatInd);
				TextView salt = dialogView.findViewById(R.id.saltInd);
				EditText input = dialogView.findViewById(R.id.editInd);
				Button add = dialogView.findViewById(R.id.addButton);

				String[] title_split = _title.split(",");

				title.setText(title_split[0]+" ("+_gram+"g)");
				carbo.setText("탄  " + _carbo);
				pro.setText("단  " + _pro);
				fat.setText("지  " + _fat);
				salt.setText("염  " + (Math.round(Double.parseDouble(_salt) / 10) / 100.0));
				input.setText(_gram);
				alertDialog = d.create();
				alertDialog.show();
				add.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (input.getText().toString().equals("") || Integer.parseInt(input.getText().toString()) <= 0) {
							Toast.makeText(getApplicationContext(), "입력 값이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
						}
						else {
							multiply = Double.parseDouble(input.getText().toString()) / Double.parseDouble(_gram);
							System.out.println("곱해지는 값" + multiply);

							if(!preferences.getString("food"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),"").toString().equals(""))
								ssl.fromJSONString(preferences.getString("food"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),""));
							if(!preferences.getString("foodcal"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),"").toString().equals(""))
								ssl_cal.fromJSONString(preferences.getString("foodcal"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),""));
							if(!preferences.getString("foodcarbo"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),"").toString().equals(""))
								ssl_carbo.fromJSONString(preferences.getString("foodcarbo"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),""));
							if(!preferences.getString("foodpro"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),"").toString().equals(""))
								ssl_pro.fromJSONString(preferences.getString("foodpro"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),""));
							if(!preferences.getString("foodfat"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),"").toString().equals(""))
								ssl_fat.fromJSONString(preferences.getString("foodfat"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),""));
							if(!preferences.getString("foodsalt"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),"").toString().equals(""))
								ssl_salt.fromJSONString(preferences.getString("foodsalt"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),""));
							if(!preferences.getString("foodgram"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),"").toString().equals(""))
								ssl_gram.fromJSONString(preferences.getString("foodgram"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),""));


							ssl.add(searchlist.get(a_position).toString());
							ssl_cal.add(searchlist.get(a_position).toString() + Double.toString(Math.round(Double.parseDouble(searchlist_cal.get(a_position).toString()) * multiply * 100) / 100));
							ssl_carbo.add(searchlist.get(a_position).toString() + Double.toString(Math.round(Double.parseDouble(searchlist_carbo.get(a_position).toString()) * multiply * 100) / 100));
							ssl_pro.add(searchlist.get(a_position).toString() + Double.toString(Math.round(Double.parseDouble(searchlist_pro.get(a_position).toString()) * multiply * 100) / 100));
							ssl_fat.add(searchlist.get(a_position).toString() + Double.toString(Math.round(Double.parseDouble(searchlist_fat.get(a_position).toString()) * multiply * 100) / 100));
							ssl_salt.add(searchlist.get(a_position).toString() + Double.toString(Math.round(Double.parseDouble(searchlist_salt.get(a_position).toString()) * multiply * 100) / 100));
							ssl_gram.add(searchlist.get(a_position).toString() + Double.toString(Math.round(Double.parseDouble(searchlist_gram.get(a_position).toString()) * multiply * 100) / 100));

							int prefoodcal = (int)(Double.parseDouble(_cal) + Double.parseDouble(preferences.getString("foodcalint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd) * multiply, "0")));
							int prefoodcarbo = (int)(Double.parseDouble(_carbo) + Double.parseDouble(preferences.getString("foodcarboint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd)* multiply, "0")));
							int prefoodpro = (int)(Double.parseDouble(_pro) + Double.parseDouble(preferences.getString("foodproint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd) * multiply, "0")));
							int prefoodfat = (int)(Double.parseDouble(_fat) + Double.parseDouble(preferences.getString("foodfatint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd) * multiply, "0")));

							editor.putString("foodint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd), _title);
							editor.putString("foodcalint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd), String.valueOf(prefoodcal));
							editor.putString("foodcarboint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd), String.valueOf(prefoodcarbo));
							editor.putString("foodproint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd), String.valueOf(prefoodpro));
							editor.putString("foodfatint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd), String.valueOf(prefoodfat));
							editor.putString("foodsaltint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd), _salt);
							editor.putString("foodgramint"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd), _gram);


							System.out.println("sslPUTSTRING::"+ssl.toString());
							editor.putString("food"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),ssl.toString());
							editor.putString("foodcal"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),ssl_cal.toString());
							editor.putString("foodcarbo"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),ssl_carbo.toString());
							editor.putString("foodpro"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),ssl_pro.toString());
							editor.putString("foodfat"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),ssl_fat.toString());
							editor.putString("foodsalt"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),ssl_salt.toString());
							editor.putString("foodgram"+Integer.valueOf(y)+Integer.valueOf(m)+Integer.valueOf(dd),ssl_gram.toString());

							editor.commit();
							alertDialog.dismiss();
							startActivity(new Intent(foodsearch.this, MainActivity.class));
							finish();
						}
					}
				});
			}
		});
	}



	StringSettingList getXmlData(){
		Intent intent = getIntent();
		StringBuffer buffer = new StringBuffer();
		String str = intent.getStringExtra("searchFor");
		System.out.println(str);

		StringSettingList ssl = new StringSettingList();
		String food_name = "";
		String pre_food_name = "";

		int start_num = 1;
		int end_num = start_num + 998;

		//for(int i = 1; i <= 5; i++) {
		String queryUrl = "https://openapi.foodsafetykorea.go.kr/api/" + key + "/I2790/xml/" + start_num + "/" + end_num + "/DESC_KOR=" + str;
		try {
			URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
			InputStream is = url.openStream(); //url위치로 입력스트림 연결

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();//xml파싱을 위한
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

			String tag;

			xpp.next();
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						buffer.append("파싱 시작...\n\n");
						break;

					case XmlPullParser.START_TAG:
						tag = xpp.getName();//테그 이름 얻어오기
						if (tag.contains("row id")) ;// 첫번째 검색결과

						else if(tag.equals("DESC_KOR")){
							xpp.next();
							buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
							buffer.append("\n"); //줄바꿈 문자 추가
							food_name = xpp.getText();
						}
						else if (tag.equals("NUTR_CONT3")) {
							buffer.append("단백질 : ");
							xpp.next();
							buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
							buffer.append("\n"); //줄바꿈 문자 추가
							if(xpp.getText() == null){
								searchlist_pro.add("0");
							}
							else {
								searchlist_pro.add(xpp.getText());
							}
						} else if (tag.equals("NUTR_CONT2")) {
							buffer.append("탄수화물 : ");
							xpp.next();
							buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
							buffer.append("\n");//줄바꿈 문자 추가
							if(xpp.getText() == null){
								searchlist_carbo.add("0");
							}
							else {
								searchlist_carbo.add(xpp.getText());
							}
						} else if (tag.equals("NUTR_CONT1")) {
							buffer.append("칼로리 :");
							xpp.next();
							buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
							buffer.append("\n");//줄바꿈 문자 추가
							if(xpp.getText() == null){
								searchlist_cal.add("0");
							}
							else {
								searchlist_cal.add(xpp.getText());
							}
						}
						else if (tag.equals("NUTR_CONT6")) {
							buffer.append("나트륨 :");
							xpp.next();
							buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
							buffer.append("\n");//줄바꿈 문자 추가
							if (xpp.getText() == null) {
								searchlist_salt.add("0");
							} else {
								//double salt = Double.parseDouble(xpp.getText().toString()) / 1000;
								searchlist_salt.add(xpp.getText());
							}
						}
						else if (tag.equals("NUTR_CONT4")) {
							buffer.append("지방 :");
							xpp.next();
							buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
							buffer.append("\n");//줄바꿈 문자 추가
							if(xpp.getText() == null){
								searchlist_fat.add("0");
							}
							else {
								searchlist_fat.add(xpp.getText());
							}
						}
						else if (tag.equals("SERVING_SIZE")) {
							//buffer.append("용량 :");
							xpp.next();
							buffer.append(xpp.getText());//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
							buffer.append("\n");//줄바꿈 문자 추가
							if(xpp.getText() == null){
								searchlist_gram.add("0");
							}
							else {
								searchlist_gram.add(xpp.getText());
							}
						}
						if(!food_name.equals("")) {
							if(!food_name.equals(pre_food_name)) {
								//searchlist.add(food_name + "\n" + searchlist_cal.get(searchlist_cal.size() - 1) + "kcal" + " " + searchlist_carbo.get(searchlist_carbo.size() - 1) + "g" + " " + searchlist_pro.get(searchlist_pro.size() - 1) + "g" + " " + searchlist_fat.get(searchlist_fat.size() - 1) + "g" + " " + searchlist_salt.get(searchlist_salt.size() - 1)+ "g");
								searchlist.add(food_name + ",");
								searchListNewLayout.add(new NutrientItem(food_name, searchlist_cal.get(searchlist_cal.size() - 1) + "Kcal"));
							}
							pre_food_name = food_name;
						}
						break;

					case XmlPullParser.TEXT:
						break;

					case XmlPullParser.END_TAG:
						tag = xpp.getName(); //테그 이름 얻어오기

						if (tag.contains("row id")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
						break;
				}
				eventType = xpp.next();
			}
			ssl.addAll(searchlist);
			if(!searchlist.isEmpty()){
				return ssl;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		start_num = start_num + 1000;
		//}

		buffer.append("파싱 끝\n");
		return null;//StringBuffer 문자열 객체 반환

	}


	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			listView.setBackgroundColor(Color.WHITE);
			listView.setAdapter(adapter);
			adapter.setNotifyOnChange(true);
			adapter.notifyDataSetChanged();
			listView.invalidate();
			progressDialog.dismiss();
		}
	};

	Handler noHandler = new Handler() {
		@Override
		public void handleMessage(@NonNull Message msg) {
			Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onBackPressed() {
		startActivity(new Intent(foodsearch.this, MainActivity.class));
		finish();
	}
}