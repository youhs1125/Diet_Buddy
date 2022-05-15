package com.project.dietbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class loseWeight extends Fragment{
    public int week;
    public int totalCal;
    public int carb;
    public int protein;
    public int fat;

    public double goal;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lose_weight, container, false);

        EditText inputWeek = view.findViewById(R.id.inputWeek);
        EditText inputGoal = view.findViewById(R.id.inputGoal);
        EditText inputCarb = view.findViewById(R.id.inputCarbo);
        EditText inputProtein = view.findViewById(R.id.inputProtein);
        EditText inputFat = view.findViewById(R.id.inputFat);

        TextView calView = view.findViewById(R.id.calResult);



        preferences = getActivity().getSharedPreferences("PREFS",0);
        editor = preferences.edit();

        Button calBut = view.findViewById(R.id.calButton);

        calBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("cal",0);
                editor.putString("week",inputWeek.getText().toString());
                editor.putString("goal",inputGoal.getText().toString());

                editor.commit();
            }
        });

        totalCal = preferences.getInt("totalCal",0);
        carb = preferences.getInt("carb",0);
        protein = preferences.getInt("protein",0);
        fat = preferences.getInt("fat",0);

        calView.setText(""+totalCal);

        return view;
    }
}