package com.project.dietbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class UserInfo extends AppCompatActivity {

	Fragment loseWeight,maintainWeight,bulkUp;
	public int sex;
	public int flag;
	public double height;
	public double weight;
	public double ratio;


	public int age;
	public int acti;
	public int totalCal;
	public int carb;
	public int protein;
	public int fat;
	public int temp;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;


	EditText inputHeight;
	EditText inputWeight;
	EditText inputAge;
	EditText inputRatio;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		preferences = getSharedPreferences("PREFS",0);
		editor = preferences.edit();

		editor.putString("isFirst", "0");
		editor.putBoolean("isActivity", true);
		editor.commit();
//값 입력
		inputHeight = (EditText) findViewById(R.id.inputHeight);
		inputWeight = (EditText) findViewById(R.id.inputWeight);
		inputAge = (EditText) findViewById(R.id.inputAge);
		inputRatio = (EditText) findViewById(R.id.inputfatRatio);

		Button maleBut = (Button) findViewById(R.id.manButton);
		Button femaleBut = (Button) findViewById(R.id.womanButton);
		Button dialogBut = (Button) findViewById(R.id.select);
		Button okBut = (Button)findViewById(R.id.okButton);

		sex = -1;
		temp = 0;
		flag = -1;

//버튼 이벤트

		okBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(inputHeight.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "키를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(inputWeight.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "몸무게를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(inputAge.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "나이를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(dialogBut.getText().toString().equals("선택")){
					Toast.makeText(getApplicationContext(), "운동 빈도를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(sex == -1){
					Toast.makeText(getApplicationContext(), "성별을 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else {
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					finish();
				}
			}
		});

		maleBut.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view){
				maleBut.setBackgroundResource(R.drawable.mangray);
				femaleBut.setBackgroundResource(R.drawable.woman);
				sex = 1;
			}
		});

		femaleBut.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				femaleBut.setBackgroundResource(R.drawable.womangray);
				maleBut.setBackgroundResource(R.drawable.man);
				sex = 0;
			}
		});

		dialogBut.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				AlertDialog.Builder dlg = new AlertDialog.Builder(UserInfo.this);
				System.out.println(dlg);
				dlg.setTitle("평소 활동량을 골라주세요");
				final String[] strArr = new String[]{"1.거의 운동 하지 않음","2.가벼운 활동(주 1~2회)","3.보통 수준(주 3~5)",
				"4.적극적으로 운동(주 6~7회)","5.고강도로 운동(주 6~7회)"};


				dlg.setSingleChoiceItems(strArr,0,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						acti = which;
						dialogBut.setText(""+(which + 1)+"번");
					}
				});
				dlg.setPositiveButton("선택",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						Toast.makeText(UserInfo.this,"선택 완료",Toast.LENGTH_SHORT).show();
					}
				});
				dlg.show();
			}
		});

//		탭 메뉴
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
	@Override
	public void onResume() {
		super.onResume();
		int week;
		double goal;

		System.out.println("onResume!!"+temp);
		temp++;


		preferences = getSharedPreferences("PREFS", 0);
		editor = preferences.edit();

		flag = preferences.getInt("mode", -1);
		System.out.println("UserInfoflag"+flag);

		if (flag != -1) {
			String temp;
			if(!inputHeight.getText().toString().equals(""))
				height = Double.parseDouble(inputHeight.getText().toString());
			else
				editor.putBoolean("inputError", true);
			if(!inputWeight.getText().toString().equals(""))
				weight = Double.parseDouble(inputWeight.getText().toString());
			else
				editor.putBoolean("inputError", true);
			if(!inputAge.getText().toString().equals(""))
				age = Integer.parseInt(inputAge.getText().toString());
			else {
				editor.putBoolean("inputError", true);
			}
			temp = inputRatio.getText().toString();

			week = preferences.getInt("week", 0);
			goal = (double)preferences.getFloat("goal", 0);

			if(goal > 0)
			{
				if(flag == 0){
					goal = weight - goal;
					if(goal < 0)
						editor.putBoolean("inputError",true);
				}
				if(flag == 2){
					goal = goal - weight;
					if(goal < 0)
						editor.putBoolean("inputError",true);
				}
			}


			if (temp.equals("")) {
				totalCal = (int) (10 * weight + 6.25 * height - 5 * age);
				if (sex == 1)
					totalCal += 5;
				else if(sex == 0)
					totalCal -= 161;
			} else {
				ratio = Double.parseDouble(temp);
				ratio/=100;
				totalCal = (int) (370 + (21.6 * weight * (1 - ratio)));

			}
			System.out.println("BtotalCal:" + totalCal);
			switch (acti) {
				case 0:
					totalCal *= 1.02f;
					break;
				case 1:
					totalCal *= 1.375f;
					break;
				case 2:
					totalCal *= 1.555f;
					break;
				case 3:
					totalCal *= 1.729f;
					break;
				case 4:
					totalCal *= 1.9f;
					break;
			}
			System.out.println("AtotalCal:" + totalCal);
			switch (flag) {
				case 0:
					totalCal -= goal * 7700 / (week * 7);
					break;
				case 2:
					totalCal += goal * 1500 / (week * 7);
					break;
				default:
					break;
			}


//			탄단지 분배 35 30 35
			carb = (int) (totalCal * 0.35 / 4);
			protein = (int) (totalCal * 0.3 / 4);
			fat = (int) (totalCal * 0.35 / 9);

			editor.putFloat("height",(float)height);
			editor.putFloat("weight",(float)weight);
			editor.putInt("sex",sex);
			editor.putFloat("ratio",(float)ratio);
			editor.putInt("age",age);
			editor.putInt("totalCal", totalCal);
			editor.putInt("acti",acti);
			editor.putInt("carb", carb);
			editor.putInt("protein", protein);
			editor.putInt("fat", fat);

			editor.commit();

			switch (flag) {
				case 0:
					loseWeight.onResume();
					break;
				case 1:
					maintainWeight.onResume();
					break;
				case 2:
					bulkUp.onResume();
					break;
				default:
					break;
			}
		}
	}
}