package com.andwho.myplan.view;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.widget.DatePicker;

public class MpDatePickerDialog extends DatePickerDialog {
	public MpDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.setTitle(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

	}

	private String year;
	private String month;
	private String day;

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.setTitle(year + "年" + (month + 1) + "月" + day + "日");

		String strYear = String.valueOf(year);
		String strMonth = String.valueOf(month);
		String strDay = String.valueOf(day);

		this.year = strYear;
		this.month = strMonth;
		this.day = strDay;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public String getDayOfMonth() {
		return day;
	}
}