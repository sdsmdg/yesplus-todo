package com.example.myyesproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ShowTodayTask extends Activity {
	TextView res;
	ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showtodaytask);
		list = (ListView) findViewById(R.id.lvtask);
		res = (TextView) findViewById(R.id.textView1);
		ArrayList<Task> tasks = new ArrayList<Task>();
		DatabaseHelper info = new DatabaseHelper(this);
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		ListAdapter boxAdapter;
		try {
			info.open();
			tasks = info.getAllTasks();
			info.close();
		} catch (Exception e) {

		}
		@SuppressWarnings("unused")
		class DateSort implements Comparator<Task> {
			@SuppressWarnings("deprecation")
			public int compare(Task left, Task right) {
				String lday = left.date.split("-")[0];
				String lmonth = left.date.split("-")[1];
				String lyear = left.date.split("-")[2];
				Date ldate = new Date();
				ldate.setDate(Integer.parseInt(lday));
				ldate.setMonth(Integer.parseInt(lmonth));
				ldate.setYear(Integer.parseInt(lyear));

				String rday = right.date.split("-")[0];
				String rmonth = right.date.split("-")[1];
				String ryear = right.date.split("-")[2];
				Date rdate = new Date();
				rdate.setDate(Integer.parseInt(rday));
				rdate.setMonth(Integer.parseInt(rmonth));
				rdate.setYear(Integer.parseInt(ryear));

				return ldate.compareTo(rdate);

			}
		}
		class According_To_Num implements Comparator<Task> {
			public int compare(Task left, Task right) {

				return right.done - left.done;

			}
		}
		Collections.sort(tasks, new According_To_Num());
		ArrayList<Task> todaylist = new ArrayList<Task>();

		for (int i = 0; i < tasks.size(); i++) {
			int lday = Integer.parseInt(tasks.get(i).date.split("-")[0]);
			int lmonth = Integer.parseInt(tasks.get(i).date.split("-")[1]);
			int lyear = Integer.parseInt(tasks.get(i).date.split("-")[2]);
			if (day == lday && (month + 1) == lmonth && year == lyear) {
				todaylist.add(tasks.get(i));
			}
		}
		boxAdapter = new ListAdapter(this, todaylist);
		list.setAdapter(boxAdapter);
	}
}
