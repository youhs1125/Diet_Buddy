package com.project.dietbuddy;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class cGPlacesAPI
{
	Context mContext;
	StringBuilder mResponseBuilder = new StringBuilder();

	LinkedList<String> mList = new LinkedList<>();

	cGPlacesAPI(Context _con, double _lat, double _lon, double _radius, String _type)
	{
		mContext = _con;
		try
		{
			String uStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + _lat + "," + _lon + "&radius=" + _radius + "&types=" + _type + "&key=AIzaSyA7B0fTUPngVzlNi8NUriDpEsXXPR8b3rQ";
			URL url = new URL(uStr);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null)
			{
				mResponseBuilder.append(inputLine);
			}
			in.close();
		}
		catch (MalformedURLException me)
		{
			me.printStackTrace();
		}
		catch (UnsupportedEncodingException ue)
		{
			ue.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public LinkedList<String> parsing()
	{
		try
		{
			JSONArray jArr;
			JSONObject jObj;

			jObj = new JSONObject(mResponseBuilder.toString());
			jArr = jObj.getJSONArray("results");

			Log.d("LOG", Integer.toString(jArr.length()));

			for (int i = 0; i < jArr.length(); i++)
			{
				// 결과별로 결과 object 얻기
				JSONObject  result = jArr.getJSONObject(i);

				// 위도, 경도 얻기
				JSONObject  geo = result.getJSONObject("geometry");
				JSONObject  location = geo.getJSONObject("location");
				String sLat = location.getString("lat");
				String sLon = location.getString("lng");

				// 이름 얻기
				String      name = result.getString("name");

				// Rating 얻기
				String      rating = "0";
				if (result.has("rating") == true)
					rating  = result.getString("rating");

				mList.add(name + sLat + sLon + "");
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return mList;
	}
}