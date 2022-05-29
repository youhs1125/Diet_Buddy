package com.project.dietbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

	public int acti;

	public int temp;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	EditText inputHeight;
	EditText inputWeight;
	EditText inputAge;
	EditText inputRatio;

	Button dialogBut;
	Button maleBut;
	Button femaleBut;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		preferences = getSharedPreferences("PREFS",0);
		editor = preferences.edit();

		editor.putString("isFirst", "0");
		editor.commit();

		inputHeight = (EditText) findViewById(R.id.inputHeight);
		inputWeight = (EditText) findViewById(R.id.inputWeight);
		inputAge = (EditText) findViewById(R.id.inputAge);
		inputRatio = (EditText) findViewById(R.id.inputfatRatio);

		maleBut = (Button) findViewById(R.id.manButton);
		femaleBut = (Button) findViewById(R.id.womanButton);
		dialogBut = (Button) findViewById(R.id.select);
		Button okBut = (Button)findViewById(R.id.okButton);

		sex = -1;
		temp = 0;

//버튼 이벤트

		okBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				System.out.println("Cal:"+preferences.getInt("totalCal",0));
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
				else if(preferences.getInt("totalCal",0) == 0){
					Toast.makeText(getApplicationContext(), "계산 버튼을 눌러 주세요.", Toast.LENGTH_LONG).show();
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
				editor.putInt("sex",1);
				editor.commit();
			}
		});

		femaleBut.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				femaleBut.setBackgroundResource(R.drawable.womangray);
				maleBut.setBackgroundResource(R.drawable.man);
				sex = 0;
				editor.putInt("sex",0);
				editor.commit();
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

				editor.putInt("acti",0);
				editor.commit();
				dialogBut.setText("1번");

				dlg.setSingleChoiceItems(strArr,0,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						acti = which;
						dialogBut.setText(""+(which + 1)+"번");
						editor.putInt("acti",acti);
						editor.commit();
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

//	editText 이벤트 리스너
		inputHeight.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable arg0){
				if(!inputHeight.getText().toString().equals(""))
					editor.putFloat("height",Float.parseFloat(inputHeight.getText().toString()));
				editor.commit();
			}
		});
		inputWeight.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable arg0){
				if(!inputWeight.getText().toString().equals(""))
					editor.putFloat("weight",Float.parseFloat(inputWeight.getText().toString()));
				editor.commit();
			}
		});
		inputAge.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable arg0){
				if(!inputAge.getText().toString().equals(""))
					editor.putInt("age",Integer.parseInt(inputAge.getText().toString()));
				editor.commit();
			}
		});
		inputAge.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable arg0){
				if(!inputRatio.getText().toString().equals("")){
					System.out.println("ratioInput"+inputRatio.getText().toString());
					editor.putFloat("ratio",Float.parseFloat(inputRatio.getText().toString())/100);
					editor.commit();
				}
			}
		});
	}
}