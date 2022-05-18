package com.project.dietbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class fragmentinfo extends Fragment{

	Fragment loseWeight,maintainWeight,bulkUp;
	public int sex;
	public int temp;

	public float height;
	public float weight;
	public float ratio;

	public int age;
	public int acti;
	public int totalCal;
	public int carb;
	public int protein;
	public int fat;

	EditText inputHeight;
	EditText inputWeight;
	EditText inputAge;
	EditText inputRatio;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		EditText inputHeight = (EditText) view.findViewById(R.id.inputHeight);
		EditText inputWeight = (EditText) view.findViewById(R.id.inputWeight);
		EditText inputAge = (EditText) view.findViewById(R.id.inputAge);
		EditText inputRatio = (EditText) view.findViewById(R.id.inputfatRatio);

		Button maleBut = (Button) view.findViewById(R.id.manButton);
		Button femaleBut = (Button) view.findViewById(R.id.womanButton);
		Button dialogBut = (Button) view.findViewById(R.id.select);
		Button okBut = (Button)view.findViewById(R.id.okButton);

		preferences = getActivity().getSharedPreferences("PREFS", 0);
		editor = preferences.edit();

		temp = -1;

//저장값 가져오기
		height = preferences.getFloat("height",0);
		weight = preferences.getFloat("weight",0);
		age = preferences.getInt("age",0);
		ratio = preferences.getFloat("ratio",0);
		sex = preferences.getInt("sex",-1);
		acti = preferences.getInt("acti",-1);

		if(height != 0)
			inputHeight.setText(""+height);
		if(weight != 0)
			inputWeight.setText(""+weight);
		if(age != 0)
			inputAge.setText(""+age);
		if(ratio != 0)
			inputRatio.setText(""+(int)(ratio*100));
		if(sex == 1)
			maleBut.setBackgroundResource(R.drawable.mangray);
		else if(sex == 0)
			femaleBut.setBackgroundResource(R.drawable.womangray);
		if(acti != -1)
			dialogBut.setText(""+(acti+1)+"번");

//버튼 이벤트

		maleBut.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view){
				System.out.println("man");
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
				AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
				System.out.println(dlg);
				dlg.setTitle("평소 활동량을 골라주세요");
				final String[] strArr = new String[]{"1.거의 운동 하지 않음","2.가벼운 활동(주 1~2회)","3.보통 수준(주 3~5)",
						"4.적극적으로 운동(주 6~7회)","5.고강도로 운동(주 6~7회)"};


				dlg.setSingleChoiceItems(strArr,0,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						acti = which;
						editor.putInt("acti",acti);
						editor.commit();
					}
				});
				dlg.setPositiveButton("선택",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which){
						Toast.makeText(getActivity(),"선택 완료",Toast.LENGTH_SHORT).show();
					}
				});
				dlg.show();
			}
		});

		okBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(inputHeight.getText().toString().equals("")){
					Toast.makeText(getActivity(), "키를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(inputWeight.getText().toString().equals("")){
					Toast.makeText(getActivity(), "몸무게를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(inputAge.getText().toString().equals("")){
					Toast.makeText(getActivity(), "나이를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(dialogBut.getText().toString().equals("선택")){
					Toast.makeText(getActivity(), "운동 빈도를 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
				else if(sex == -1){
					Toast.makeText(getActivity(), "성별을 입력해 주세요.", Toast.LENGTH_LONG).show();
				}
			}
		});


//		탭 메뉴
		loseWeight = new loseWeight();
		maintainWeight = new maintainWeight();
		bulkUp = new bulkUp();

		//

		TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
		getChildFragmentManager().beginTransaction().add(R.id.frame, loseWeight).commit();

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

				getChildFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
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

		return view;
	}
}

