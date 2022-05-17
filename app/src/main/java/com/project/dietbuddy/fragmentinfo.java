package com.project.dietbuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

	public double height;
	public double weight;
	public double ratio;

	public int flag;

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
		editor.putInt("mode",-1);
		editor.putBoolean("isActivity", false);

		editor.commit();

//저장값 가져오기
		height = (double)preferences.getFloat("height",0);
		weight = (double)preferences.getFloat("weight",0);
		age = preferences.getInt("age",0);
		ratio = (double)preferences.getFloat("ratio",0);
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
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		int week;
		double goal;

		flag = preferences.getInt("mode", -1);
		System.out.println(flag);
		if (flag != -1) {
			String temp;
			if(!inputHeight.getText().toString().equals(""))
				height = Double.parseDouble(inputHeight.getText().toString());
			else {
				editor.putBoolean("inputError", true);
				return;
			}
			if(!inputWeight.getText().toString().equals(""))
				weight = Double.parseDouble(inputWeight.getText().toString());
			else {
				editor.putBoolean("inputError", true);
				return;
			}
			if(!inputAge.getText().toString().equals(""))
				age = Integer.parseInt(inputAge.getText().toString());
			else {
				editor.putBoolean("inputError", true);
				return;
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
			editor.putInt("ratio",(int)(ratio*100));
			editor.putInt("age",age);
			editor.putInt("totalCal", totalCal);
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
	@Override
	public void onDestroy() {
		super.onDestroy();
		editor.remove("mode");
		editor.remove("inputError");
		editor.remove("isActivity");
		editor.commit();
	}
}

