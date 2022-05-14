package com.project.dietbuddy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NutrientItemAdapter extends ArrayAdapter<NutrientItem> {

	private int resourceLayout;
	private Context mContext;

	public NutrientItemAdapter(Context context, int resource, List<NutrientItem> items) {
		super(context, resource, items);
		this.resourceLayout = resource;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;

		if (v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(mContext);
			v = vi.inflate(resourceLayout, null);
		}

		NutrientItem p = getItem(position);

		if (p != null) {
			TextView tt1 = (TextView) v.findViewById(R.id.text);
			TextView tt2 = v.findViewById(R.id.cal);

			if (tt1 != null) {
				tt1.setText(p.getText());
			}
			if(tt2 != null){
				tt2.setText(p.getCal());
			}
		}

		return v;
	}

}