package app.com.example.android.locationreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DisplayAlarm extends AppCompatActivity {
	String id;
	SQLiteDatabase notedb;
	AlarmReceiver alarm = new AlarmReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_alarm);
		Intent i = getIntent();
		TextView header = (TextView) findViewById(R.id.note_header);
		String s = i.getStringExtra("note_head");
		String con = i.getStringExtra("where");
		header.setText(s);
		String date = "date";
		String time = "time";
		notedb = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
		if (con.equals("new")) {
			edit_note(findViewById(R.id.save_button));
		} else {
			id = i.getStringExtra("id");
			Cursor resultSet = notedb.rawQuery("select date,hour,minutes from alarm where id=" + id + ";", null);
			resultSet.moveToFirst();

			date = resultSet.getString(0);
			Log.d("date", date);
			int hour = resultSet.getInt(1);
			int minutes = resultSet.getInt(2);
			TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
			timePicker.setHour(hour);
			timePicker.setMinute(minutes);
			DatePicker d = (DatePicker) findViewById(R.id.dayPicker);
			String ddate=(date.substring(8));
			String dmonth=(date.substring(5,7));
			String dyear=(date.substring(0,4));
			Log.d("ddate",ddate+"");
			Log.d("dmonth",dmonth+"");
			Log.d("dyear",dyear+"");
			String AM_PM;
			if (hour > 12) {
				AM_PM = "PM";
				hour = hour - 12;
			} else {
				AM_PM = "AM";
			}
			resultSet.close();
			time = hour + ":" + minutes + " " + AM_PM;
			date=ddate+"-"+dmonth+"-"+dyear;
		}
		TextView dateText = (TextView) findViewById(R.id.showDate);
		TextView timeText = (TextView) findViewById(R.id.showTime);




		dateText.setText(date);
		timeText.setText(time);
	}

	int flag = 0;

	public void edit_note(View view) {

		Button bell = (Button) findViewById(R.id.save_button);
		bell.setVisibility(View.VISIBLE);
		Button bell2 = (Button) findViewById(R.id.delete_button);
		bell2.setVisibility(View.VISIBLE);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
		fab.setVisibility(View.INVISIBLE);
		EditText edu = (EditText) findViewById(R.id.note_header_edit);
		edu.setVisibility(View.VISIBLE);
		TextView tu = (TextView) findViewById(R.id.note_header);
		tu.setVisibility(View.INVISIBLE);
		TextView dateText = (TextView) findViewById(R.id.showDate);
		dateText.setVisibility(View.INVISIBLE);
		TextView timeText = (TextView) findViewById(R.id.showTime);
		timeText.setVisibility(View.INVISIBLE);
		DatePicker d = (DatePicker) findViewById(R.id.dayPicker);
		d.setVisibility(View.VISIBLE);
		TimePicker time = (TimePicker) findViewById(R.id.timePicker);
		time.setVisibility(View.VISIBLE);
		edu.setText(tu.getText().toString());
		if (view.getId() == R.id.save_button)
			flag = 1;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}

	public void save_note(View view) {
		Button bell = (Button) findViewById(R.id.save_button);
		bell.setVisibility(View.INVISIBLE);
		Button bell2 = (Button) findViewById(R.id.delete_button);
		bell2.setVisibility(View.INVISIBLE);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
		fab.setVisibility(View.VISIBLE);
		EditText edu = (EditText) findViewById(R.id.note_header_edit);
		edu.setVisibility(View.INVISIBLE);
		TextView tu = (TextView) findViewById(R.id.note_header);
		tu.setVisibility(View.VISIBLE);
		TextView dateText = (TextView) findViewById(R.id.showDate);
		dateText.setVisibility(View.VISIBLE);
		TextView timeText = (TextView) findViewById(R.id.showTime);
		timeText.setVisibility(View.VISIBLE);
		DatePicker d = (DatePicker) findViewById(R.id.dayPicker);
		d.setVisibility(View.INVISIBLE);
		TimePicker time = (TimePicker) findViewById(R.id.timePicker);
		time.setVisibility(View.INVISIBLE);

		String day;

		int dd = d.getDayOfMonth();
		if (dd <= 9) {
			day = "0" + Integer.toString(dd);
		} else day = "" + Integer.toString(dd);
		int md = d.getMonth()+1;
		String dbmonth;
		if (md <= 9)
			dbmonth = "0" + Integer.toString(md);
		else
			dbmonth = "" + Integer.toString(md);

		String dbdate = d.getYear()+ "-" + dbmonth + "-" +day  ;
		dateText.setText(dbdate);
		String AM_PM;
		int hourOfDay = time.getHour();
		int h = hourOfDay;
		if (hourOfDay < 12) {
			AM_PM = "AM";

		} else {
			AM_PM = "PM";
			hourOfDay -= 12;
		}
		String minutes = "" + time.getMinute();
		String timeValue = hourOfDay + ":" + time.getMinute() + " " + AM_PM;
		timeText.setText(timeValue);
		dateText.setText(dbdate);
		tu.setText(edu.getText().toString());
		String Contentheading = edu.getText().toString();
		if (flag == 1) {


			notedb.execSQL("insert into base(heading, content, type) values('" + Contentheading + "', 'ALARM', 2);");
			Cursor resultset = notedb.rawQuery("select max(id) from base;", null);
			resultset.moveToFirst();
			String id = resultset.getString(0);
			notedb.execSQL("insert into alarm(id,Date,hour,minutes)values(" + id + ",'" + dbdate + "'," + h + "," + minutes + ");");
			//Write code to add new note onto the database
			flag = 0;
		} else {
			notedb.execSQL("Update alarm set Date='" + dbdate + "',hour=" + h + ",minutes=" + minutes + " where id=" + id + ";");
			notedb.execSQL("Update base set heading='" + Contentheading + "' where id=" + id + ";");
		}

		alarm.setAlarm(this, d.getDayOfMonth(), d.getMonth(), d.getYear(), time.getHour(), time.getMinute());
		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
		i.putExtra(AlarmClock.EXTRA_MESSAGE, Contentheading);
		i.putExtra(AlarmClock.EXTRA_HOUR, time.getHour());
		i.putExtra(AlarmClock.EXTRA_MINUTES, time.getMinute());
		//i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
		startActivity(i);

	}

	public void delete_note(View view) {

		Toast.makeText(this, "The Item has been deleted", Toast.LENGTH_SHORT).show();
		notedb.execSQL("delete from base where id='"+id+"';");
		alarm.cancelAlarm(this);
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);


	}
}
