package com.project.dietbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class bulkUp extends Fragment{
    public int totalCal;
    public int carb;
    public int protein;
    public int fat;

    public int week;
    public float goal;

    public float height;
    public float weight;
    public float ratio;
    public int age;
    public int sex;
    public int acti;

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    TextView calView;
    EditText inputCarb;
    EditText inputProtein;
    EditText inputFat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bulk_up, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        EditText inputWeek = view.findViewById(R.id.inputWeek);
        EditText inputGoal = view.findViewById(R.id.inputGoal);
        inputCarb = view.findViewById(R.id.inputCarbo);
        inputProtein = view.findViewById(R.id.inputProtein);
        inputFat = view.findViewById(R.id.inputFat);

        calView = view.findViewById(R.id.calResult);

        Button calBut = view.findViewById(R.id.calButton);

        preferences = getActivity().getSharedPreferences("PREFS",0);
        editor = preferences.edit();


        calBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getFloat("height",-1) == -1) {
                    Toast.makeText(getActivity(), "키를 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if (preferences.getFloat("weight",-1) == -1) {
                    Toast.makeText(getActivity(), "체중을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if (preferences.getInt("age",-1) == -1) {
                    Toast.makeText(getActivity(), "나이를 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if(preferences.getInt("sex",-1) == -1){
                    Toast.makeText(getActivity(), "성별을 선택해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if (preferences.getInt("acti",-1) == -1) {
                    Toast.makeText(getActivity(), "활동량을 선택해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                if(inputWeek.getText().toString().equals("") || inputWeek.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "목표기간을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    week = Integer.parseInt(inputWeek.getText().toString());
                }
                if(inputGoal.getText().equals("")) {
                    Toast.makeText(getActivity(), "목표체중을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    goal = Float.parseFloat(inputGoal.getText().toString());
                }

                height = preferences.getFloat("height", -1);
                weight = preferences.getFloat("weight", -1);
                ratio = preferences.getFloat("ratio",-1);

                age = preferences.getInt("age", -1);
                acti = preferences.getInt("acti", -1);
                sex = preferences.getInt("sex", -1);

                if(sex == -1)
                {
                    Toast.makeText(getActivity(), "성별을 선택해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                System.out.println("weight goal" + weight + "," + goal);
                if(weight > goal){
                    Toast.makeText(getActivity(), "목표체중은 현재체중보다 높아야 합니다", Toast.LENGTH_LONG).show();
                    return;
                }
                goal = weight - goal;
                System.out.println("ratio"+ratio);

                if(ratio <= 0){
                    totalCal = (int) (10 * weight + 6.25 * height - 5 * age);
                    if (sex == 1)
                        totalCal += 5;
                    else if(sex == 0)
                        totalCal -= 161;
                }
                else {
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

//  감량 증량
                totalCal += goal * 1800 / (week * 7);


                carb = (int) (totalCal * 0.35 / 4);
                protein = (int) (totalCal * 0.3 / 4);
                fat = (int) (totalCal * 0.35 / 9);

                calView.setText("" + totalCal);
                inputCarb.setText("" + carb);
                inputProtein.setText("" + protein);
                inputFat.setText("" + fat);

                editor.putInt("totalCal",totalCal);
                editor.putInt("carb",carb);
                editor.putInt("protein",protein);
                editor.putInt("fat",fat);

                editor.putInt("totalCal",totalCal);
                editor.putInt("carb",carb);
                editor.putInt("protein",protein);
                editor.putInt("fat",fat);

                editor.commit();
            }
        });

        inputCarb.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputCarb.getText().toString().equals(""))
                    carb = 0;
                else
                    carb = Integer.parseInt(inputCarb.getText().toString());
                totalCal = 4*carb + 4*protein + 9*fat;
                calView.setText(""+totalCal);
                editor.putInt("totalCal",totalCal);
                editor.putInt("carb",carb);
                editor.commit();
                return;
            }
        });

        inputFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputFat.getText().toString().equals(""))
                    fat = 0;
                else
                    fat = Integer.parseInt(inputFat.getText().toString());
                totalCal = 4*carb + 4*protein + 9*fat;
                calView.setText(""+totalCal);
                editor.putInt("totalCal",totalCal);
                editor.putInt("fat",fat);
                editor.commit();
                return;
            }
        });

        inputProtein.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputProtein.getText().toString().equals(""))
                    protein = 0;
                else
                    protein = Integer.parseInt(inputProtein.getText().toString());
                totalCal = 4*carb + 4*protein + 9*fat;
                calView.setText(""+totalCal);
                editor.putInt("totalCal",totalCal);
                editor.putInt("protein",protein);
                editor.commit();
                return;
            }
        });
        return view;
    }
}