package com.project.dietbuddy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class StringSettingList extends ArrayList<String> {
    public StringSettingList(String json) {
        super();
        fromJSONString(json);
    }

    public StringSettingList() {
        super();
    }

    public String toString(){
        JSONArray ja = new JSONArray();
        for(int idx = 0; idx < size(); idx++){
            ja.put(get(idx));
        }

        return ja.toString();
    }

    public void fromJSONString(String json){
        clear();
        try{
            JSONArray ja = new JSONArray(json);
            for(int idx = 0; idx < ja.length(); idx++){
                add(ja.getString(idx));
            }
        }catch (JSONException je){

        }
    }
}