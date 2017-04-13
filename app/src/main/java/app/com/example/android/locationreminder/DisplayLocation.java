package app.com.example.android.locationreminder;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayLocation extends AppCompatActivity {

    TextView HeadView, ContentView, Locat;
    EditText HeadEdit, ContentEdit;
    FloatingActionButton fab, fab2;
    LocationManager lm;
    RadioButton rb1;
    SQLiteDatabase db;
    private LocationListener lListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alarm_display);
        Intent i = getIntent();

        HeadView = (TextView) findViewById(R.id.Heading);
        ContentView = (TextView) findViewById(R.id.Content);
        Locat = (TextView) findViewById(R.id.Location);
        HeadEdit = (EditText) findViewById(R.id.HeadEdit);
        ContentEdit = (EditText) findViewById(R.id.ContentEdit);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        rb1 = (RadioButton) findViewById(R.id.radioButton);

        String s = i.getStringExtra("note_head");
        String d = i.getStringExtra("note_details");
        HeadView.setText(s);
        ContentView.setText(d);
        //HeadEdit.setText(s);
        //ContentEdit.setText(d);
        HeadEdit.setHint(s);
        ContentEdit.setHint(d);


        db = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
        db.execSQL("create table if not exists base(id integer primary key autoincrement, heading text, content text, type integer);");
        db.execSQL("create table if not exists loc(id integer primary key, lat double, long double, foreign key(id) references base(id) on delete cascade);");
        try {
            Cursor c = db.rawQuery("select * from base natural join loc where heading='" + s + "' and content='" + d + "';", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                Locat.setVisibility(View.VISIBLE);
                Locat.setText("Using: " + c.getString(c.getColumnIndex("lat")) + ", " + c.getString(c.getColumnIndex("long")));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        String con=i.getStringExtra("where");
        if(con.equals("new")) {
            edit(fab);
        }

    }

    public void edit(View v) {
        HeadView.setVisibility(View.INVISIBLE);
        ContentView.setVisibility(View.INVISIBLE);
        Locat.setVisibility(View.INVISIBLE);
        HeadEdit.setVisibility(View.VISIBLE);
        ContentEdit.setVisibility(View.VISIBLE);
        rb1.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.VISIBLE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(getApplicationContext(), "Inside", Toast.LENGTH_SHORT).show();
                rb1.setText("Use current location (" + location.getLatitude() + ", " + location.getLongitude() + ")");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, lListener);
        Location lc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        rb1.setText("Use current location (" + lc.getLatitude() + ", " + lc.getLongitude() + ")");

    }

    public void saver(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        Location lc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        db = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
        db.execSQL("create table if not exists base(id integer primary key autoincrement, heading text, content text, type integer);");
        db.execSQL("create table if not exists loc(id integer primary key, lat double, long double, foreign key(id) references base(id) on delete cascade);");
        ContentValues val = new ContentValues();
        val.put("heading", HeadEdit.getText().toString());
        val.put("content", ContentEdit.getText().toString());
        val.put("type", 3);
        long rowid = db.insert("base", null, val);
        Cursor c = db.rawQuery("select id from base where rowid=" + rowid + ";", null);
        //Toast.makeText(getApplicationContext(), String.valueOf(c.getColumnCount()), Toast.LENGTH_SHORT).show();
        c.moveToFirst();
        val = new ContentValues();
        val.put("id", c.getInt(c.getColumnIndex("id")));
        val.put("lat", lc.getLatitude());
        val.put("long", lc.getLongitude());
        db.insert("loc", null, val);
        lm.removeUpdates(lListener);

        HeadView.setVisibility(View.VISIBLE);
        ContentView.setVisibility(View.VISIBLE);
        Locat.setVisibility(View.VISIBLE);
        HeadEdit.setVisibility(View.INVISIBLE);
        ContentEdit.setVisibility(View.INVISIBLE);
        rb1.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        HeadView.setText(HeadEdit.getText());
        ContentView.setText(ContentEdit.getText());
    }
}
