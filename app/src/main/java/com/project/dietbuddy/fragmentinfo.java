package com.project.dietbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class fragmentinfo extends Fragment {

	Fragment loseWeight,maintainWeight,bulkUp;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);

		loseWeight = new loseWeight();
		maintainWeight = new maintainWeight();
		bulkUp = new bulkUp();

		getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frame, loseWeight).commit();

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
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
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
