package app.com.example.android.locationreminder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
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

    TextView HeadView, ContentView;
    EditText HeadEdit, ContentEdit;
    FloatingActionButton fab;
    LocationManager lm;
    RadioButton rb1;
    private LocationListener lListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alarm_display);
        Intent i = getIntent();

        HeadView = (TextView) findViewById(R.id.Heading);
        ContentView = (TextView) findViewById(R.id.Content);
        HeadEdit = (EditText) findViewById(R.id.HeadEdit);
        ContentEdit = (EditText) findViewById(R.id.ContentEdit);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        rb1 = (RadioButton) findViewById(R.id.radioButton);

        String s = i.getStringExtra("note_head");
        String d = i.getStringExtra("note_details");
        HeadView.setText(s);
        ContentView.setText(d);
        HeadEdit.setText(s);
        ContentEdit.setText(d);
    }

    public void edit(View v) {
        HeadView.setVisibility(View.INVISIBLE);
        ContentView.setVisibility(View.INVISIBLE);
        HeadEdit.setVisibility(View.VISIBLE);
        ContentEdit.setVisibility(View.VISIBLE);
        rb1.setVisibility(View.VISIBLE);
        fab.setImageResource(android.R.drawable.ic_menu_save);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(), "Inside", Toast.LENGTH_SHORT).show();
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

        lm.requestLocationUpdates(lm.getBestProvider(new Criteria(), false), 100, 0, lListener);
        /*Location lc = lm.getLastKnownLocation(lm.getBestProvider(new Criteria(), false));
        rb1.setText("Use current location (" + lc.getLatitude() + ", " + lc.getLongitude() + ")");*/

    }
}
