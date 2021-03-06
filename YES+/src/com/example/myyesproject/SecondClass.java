package com.example.myyesproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SecondClass extends Activity implements OnClickListener {

	static final int CUSTOM_DIALOG_ID = 0;
	Button add, datep, timep, reqTime;
	int a = 0, b = 0;
	int year, month, day, hour, minute;
	EditText t;
	TextView res;
	String s = "";
	String stime = "";
	ImageButton speak;
	protected static final int RESULT_SPEECH = 1;
	CheckBox cburg, cbimpt;
	Button btnDone;
	EditText etHours, etMinutes;
	Dialog dialog = null;
	long setTime, needTime;
	public static final String ACTION = "com.example.android.receivers.NOTIFICATION_ALARM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondpage);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		s = Integer.toString(day) + "-" + Integer.toString(month + 1) + "-"
				+ Integer.toString(year);

		add = (Button) findViewById(R.id.bAdd);
		timep = (Button) findViewById(R.id.bTime);
		reqTime = (Button) findViewById(R.id.bReqTime);
		datep = (Button) findViewById(R.id.bDate);
		res = (TextView) findViewById(R.id.tresult);
		t = (EditText) findViewById(R.id.editText1);
		speak = (ImageButton) findViewById(R.id.Speak);
		cburg = (CheckBox) findViewById(R.id.cburg);
		cbimpt = (CheckBox) findViewById(R.id.cbimpt);
		checkBox_fun();
		add.setOnClickListener(this);
		timep.setOnClickListener(this);
		datep.setOnClickListener(this);
		reqTime.setOnClickListener(this);
		speak.setOnClickListener(this);
	}

	public void checkBox_fun() {
		// TODO Auto-generated method stub
		cburg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					a++;
				} else {
					a--;
				}

			}
		});
		cbimpt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					b++;
				} else {
					b--;
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bAdd:
			int n = 0;
			try {
				String task = t.getText().toString();
				if (a == 1 && b == 1) {
					n = 3;
				} else if (a == 1 && b == 0) {
					n = 1;
				} else if (a == 0 && b == 1) {
					n = 2;
				} else if (a == 0 && b == 0) {
					n = 0;
				}

				DatabaseHelper entry = new DatabaseHelper(SecondClass.this);
				entry.open();
				entry.createTask(task, n, s, stime, 0);
				entry.close();
				Intent i = new Intent(SecondClass.this, MainActivity.class);
				startActivity(i);
				finish();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.bDate:
			showDialog(999);
			break;
		case R.id.Speak:
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
			try {
				startActivityForResult(intent, RESULT_SPEECH);
			} catch (ActivityNotFoundException a) {
				Toast t = Toast.makeText(getApplicationContext(),
						"Opps! Your device doesn't support Speech to Text",
						Toast.LENGTH_SHORT);
				t.show();
			}
			break;
		case R.id.bTime:
			showDialog(888);
			break;
		case R.id.bReqTime:
			showDialog(CUSTOM_DIALOG_ID);
			Log.i("req time", "the button has been clicked");
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case 999:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		case 888:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
		case CUSTOM_DIALOG_ID:
			dialog = new Dialog(SecondClass.this);
			dialog.setContentView(R.layout.customdialog);
			dialog.setTitle("Set Time");
			etHours = (EditText) dialog.findViewById(R.id.etHours);
			etMinutes = (EditText) dialog.findViewById(R.id.etMins);
			btnDone = (Button) dialog.findViewById(R.id.buttonDialog);
			btnDone.setOnClickListener(btnDoneOnClickListener);
			return dialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			s = Integer.toString(day) + "-" + Integer.toString(month + 1) + "-"
					+ Integer.toString(year);
			datep.setText(s);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String dspeak = text.get(0);
				t.setText(dspeak);
			}
			break;
		}
		}
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
			stime = Integer.toString(hour) + "-" + Integer.toString(minute);
			Calendar c2 = Calendar.getInstance();
			c2.set(Calendar.HOUR_OF_DAY, hour);
			c2.set(Calendar.MINUTE, minute);
			setTime = c2.getTimeInMillis();
			timep.setText(stime);
		}
	};
	private Button.OnClickListener btnDoneOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showMessage();
			dialog.dismiss();
		}
	};

	protected void showMessage() {
		// TODO Auto-generated method stub
		Toast.makeText(
				this,
				"The time required for this task is "
						+ etHours.getText().toString() + " hrs : "
						+ etMinutes.getText().toString() + " mins",
				Toast.LENGTH_SHORT).show();
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(etHours.getText().toString()));
		c2.set(Calendar.MINUTE,
				Integer.parseInt(etMinutes.getText().toString()));
		needTime = c2.getTimeInMillis();
		// compare time and call for notification
		long notifyTime = setTime - needTime;
		if (notifyTime == System.currentTimeMillis()) {
			// set up notification
			createNotification(notifyTime, s);
		}
	}

	private void createNotification(long notifyTime, String s2) {
		// TODO Auto-generated method stub
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent,
				0);
		alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance()
				.getTimeInMillis(), alarmIntent);
	}
}
