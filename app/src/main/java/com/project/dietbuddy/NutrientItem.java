package com.project.dietbuddy;

public class NutrientItem {
	private String text ;
	private String cal;

	NutrientItem(String t, String c){
		this.text = t;
		this.cal = c;
	}

	public String getText() {
		return this.text ;
	}
	public String getCal() {
		return this.cal ;
	}
	public void set(String title, String Kcal) {
		this.text = title;
		this.cal = Kcal;
		return;
	}
}