package app.com.example.android.locationreminder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayNote extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_note);
		Intent i=getIntent();
		TextView header=(TextView)findViewById(R.id.note_header);
		TextView det=(TextView)findViewById(R.id.note_details);
		String s=i.getStringExtra("note_head");
		String d=i.getStringExtra("note_details");
		header.setText(s);
		det.setText(d);
	}
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
			ed.setText(t.getText().toString());
			edu.setText(tu.getText().toString());

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
		t.setText(ed.getText().toString());
		tu.setText(edu.getText().toString());

	}
	public void delete_note(View view)
	{

			Toast.makeText(this,"Deleting only in edit mode",Toast.LENGTH_SHORT).show();
			//Write code here to delete
			Intent i=new Intent(this,MainActivity.class);
			startActivity(i);


	}
}
