package com.project.dietbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class fragmentinfo extends Fragment {

	Fragment loseWeight,maintainWeight,bulkUp;
	public int sex;
	public int flag;
	public double height;
	public double weight;
	public double ratio;
	public double goal;


	public int age;
	public int acti;
	public int week;
	public int totalCal;
	public int carb;
	public int protein;
	public int fat;

	SharedPreferences preferences;
	SharedPreferences.Editor editor;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);


		EditText inputHeight = (EditText) view.findViewById(R.id.inputHeight);
		EditText inputWeight = (EditText) view.findViewById(R.id.inputWeight);
		EditText inputAge = (EditText) view.findViewById(R.id.inputAge);
		EditText inputRatio = (EditText) view.findViewById(R.id.inputfatRatio);

		Button maleBut = (Button) view.findViewById(R.id.manButton);
		Button femaleBut = (Button) view.findViewById(R.id.womanButton);
		Button dialogBut = (Button) view.findViewById(R.id.select);
		Button okBut = (Button)view.findViewById(R.id.okButton);

//버튼 이벤트

		maleBut.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view){
				System.out.println("man");
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
				AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
				System.out.println(dlg);
				dlg.setTitle("평소 활동량을 골라주세요");
				final String[] strArr = new String[]{"1.거의 운동 하지 않음","2.가벼운 활동(주 1~2회)","3.보통 수준(주 3~5)",
						"4.적극적으로 운동(주 6~7회)","5.고강도로 운동(주 6~7회)"};


				dlg.setSingleChoiceItems(strArr,0,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						acti = which;
						dialogBut.setText(""+(acti+1)+"번");
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

		preferences = getActivity().getSharedPreferences("PREFS",0);
		editor = preferences.edit();

		flag = preferences.getInt("cal",-1);

		if(flag == 0){
			String temp;
			height = Double.parseDouble(inputHeight.getText().toString());
			weight = Double.parseDouble(inputWeight.getText().toString());
			age = Integer.parseInt(inputAge.getText().toString());
			temp = inputRatio.getText().toString();

			week = Integer.parseInt(preferences.getString("week","-1"));
			goal = Double.parseDouble(preferences.getString("goal","-1"));

//			week,goal이 -1일때 계산 하지 않음

			if(temp.isEmpty()){
				totalCal = (int)(10*weight + 6.25*height - 5*age);
				if(sex == 1)
					totalCal+=5;
				else
					totalCal-=161;
			}
			else{
				ratio = Double.parseDouble(temp);
				totalCal = (int)(370+(21.6*weight*(1-ratio)));

			}

			switch(acti){
				case 0: totalCal*=1.02f;
				case 1: totalCal*=1.375f;
				case 2: totalCal*=1.555f;
				case 3: totalCal*=1.729f;
				case 4: totalCal*=1.9f;
			}

			totalCal -= goal*7700/week;

//			탄단지 분배 35 30 35
			carb = (int)(totalCal*0.35/4);
			protein = (int)(totalCal*0.3/4);
			fat = (int)(totalCal*0.35/9);

			editor.putInt("totalCal",totalCal);
			editor.putInt("carb",carb);
			editor.putInt("protein",protein);
			editor.putInt("fat",fat);

			editor.commit();
			flag = -1;
		}


//		탭 메뉴
		loseWeight = new loseWeight();
		maintainWeight = new maintainWeight();
		bulkUp = new bulkUp();

		getChildFragmentManager().beginTransaction().add(R.id.frame, loseWeight).commit();
		TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
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
		return view;
	}
}
