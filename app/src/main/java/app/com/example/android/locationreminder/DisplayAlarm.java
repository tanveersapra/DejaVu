package app.com.example.android.locationreminder;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DisplayAlarm extends AppCompatActivity {

	SQLiteDatabase notedb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_alarm);
		Intent i=getIntent();
		TextView header=(TextView)findViewById(R.id.note_header);
		TextView det=(TextView)findViewById(R.id.note_details);
		String s=i.getStringExtra("note_head");
		String d=i.getStringExtra("note_details");
		String con=i.getStringExtra("where");

		header.setText(s);
		det.setText(d);
		if(con.equals("new"))
		{
			edit_note(findViewById(R.id.save_button));
		}
		notedb = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
		notedb.execSQL("create table if not exists base(id integer primary key autoincrement, heading text, content text, type integer);");
		//notedb.execSQL("insert into base(heading, content, type) values('Knock knock', 'Who is there?', 0);");
	}
	int flag=0;
	public void edit_note(View view)
	{

		Button bell = (Button) findViewById(R.id.save_button);
		bell.setVisibility(View.VISIBLE);
		Button bell2 = (Button) findViewById(R.id.delete_button);
		bell2.setVisibility(View.VISIBLE);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
		fab.setVisibility(View.INVISIBLE);
		EditText ed=(EditText)findViewById(R.id.note_detail_edit);
		ed.setVisibility(View.VISIBLE);
		TextView t=(TextView)findViewById(R.id.note_details);
		t.setVisibility(View.INVISIBLE);
		EditText edu=(EditText)findViewById(R.id.note_header_edit);
		edu.setVisibility(View.VISIBLE);
		TextView tu=(TextView)findViewById(R.id.note_header);
		tu.setVisibility(View.INVISIBLE);
		TextView dateText=(TextView)findViewById(R.id.showDate);
		dateText.setVisibility(View.INVISIBLE);
		TextView timeText=(TextView)findViewById(R.id.showTime);
		timeText.setVisibility(View.INVISIBLE);
		DatePicker d=(DatePicker)findViewById(R.id.dayPicker);
		d.setVisibility(View.VISIBLE);
		TimePicker time=(TimePicker)findViewById(R.id.timePicker);
		time.setVisibility(View.VISIBLE);
		ed.setText(t.getText().toString());
		edu.setText(tu.getText().toString());
		if(view.getId()==R.id.save_button)
			flag=1;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i=new Intent(this,MainActivity.class);
		startActivity(i);
	}

	public void save_note(View view)
	{
		Button bell=(Button)findViewById(R.id.save_button);
		bell.setVisibility(View.INVISIBLE);
		Button bell2=(Button)findViewById(R.id.delete_button);
		bell2.setVisibility(View.INVISIBLE);
		FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab2);
		fab.setVisibility(View.VISIBLE);
		EditText ed=(EditText)findViewById(R.id.note_detail_edit);
		ed.setVisibility(View.INVISIBLE);
		TextView t=(TextView)findViewById(R.id.note_details);
		t.setVisibility(View.VISIBLE);
		EditText edu=(EditText)findViewById(R.id.note_header_edit);
		edu.setVisibility(View.INVISIBLE);
		TextView tu=(TextView)findViewById(R.id.note_header);
		tu.setVisibility(View.VISIBLE);
		TextView dateText=(TextView)findViewById(R.id.showDate);
		dateText.setVisibility(View.VISIBLE);
		TextView timeText=(TextView)findViewById(R.id.showTime);
		timeText.setVisibility(View.VISIBLE);
		DatePicker d=(DatePicker)findViewById(R.id.dayPicker);
		d.setVisibility(View.INVISIBLE);
		TimePicker time=(TimePicker)findViewById(R.id.timePicker);
		time.setVisibility(View.INVISIBLE);
		String date=d.getDayOfMonth()+"-"+d.getMonth()+"-"+d.getYear();
		dateText.setText(date);
		String AM_PM ;
		int hourOfDay=time.getHour();
		if(hourOfDay < 12) {
			AM_PM = "AM";

		} else {
			AM_PM = "PM";
			hourOfDay-=12;
		}
		String timeValue=hourOfDay+":"+time.getMinute()+" "+AM_PM;
		timeText.setText(timeValue);
		t.setText(ed.getText().toString());
		tu.setText(edu.getText().toString());
		if(flag==1)
		{
			String contentdetails=ed.getText().toString();
			String Contentheading=edu.getText().toString();
			notedb.execSQL("insert into base(heading, content, type) values('"+Contentheading+"', '"+contentdetails+"', 2);");
			//Write code to add new note onto the database
			flag=0;
		}
	}
	public void delete_note(View view)
	{

		Toast.makeText(this,"Deleting only in edit mode",Toast.LENGTH_SHORT).show();
		//Write code here to delete
		Intent i=new Intent(this,MainActivity.class);
		startActivity(i);


	}
}
