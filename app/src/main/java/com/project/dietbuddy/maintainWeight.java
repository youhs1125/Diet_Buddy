package com.project.dietbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class maintainWeight extends Fragment{
    public int carb;
    public int protein;
    public int fat;
    public int totalCal;

    public double goal;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    TextView calView;
    EditText inputCarb;
    EditText inputProtein;
    EditText inputFat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintain_weight, container, false);

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
                editor.putInt("mode",1);
                editor.putInt("week",0);
                editor.putFloat("goal",0);

                editor.commit();

                if(preferences.getBoolean("isActivity",true))
                    ((UserInfo)getActivity()).onResume();
                else
                    getParentFragment().onResume();
            }
        });



        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(preferences.getBoolean("inputError",false)){
            Toast.makeText(getActivity(),"잘못된 입력이 있습니다", Toast.LENGTH_LONG).show();
            editor.putBoolean(" inputError",true);
            return;
        }
        totalCal = preferences.getInt("totalCal",0);
        carb = preferences.getInt("carb",0);
        protein = preferences.getInt("protein",0);
        fat = preferences.getInt("fat",0);

        calView.setText(""+totalCal);
        inputCarb.setText(""+carb);
        inputProtein.setText(""+protein);
        inputFat.setText(""+fat);
    }
}