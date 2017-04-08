package app.com.example.android.locationreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class location_alarm_display extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_alarm_display);
		Intent i=getIntent();
		TextView header=(TextView)findViewById(R.id.l_note_header);
		TextView det=(TextView)findViewById(R.id.l_note_content);
		String s=i.getStringExtra("note_head");
		String d=i.getStringExtra("note_details");
		header.setText(s);
		det.setText(d);
	}
}
