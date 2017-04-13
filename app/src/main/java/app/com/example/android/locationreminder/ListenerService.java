package app.com.example.android.locationreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class  ListenerService extends Service {

    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            Log.i("***********************", "Location changed");
            double clat = loc.getLatitude(), clong = loc.getLongitude(), tlat, tlong;
            Log.i("current", clat + ", " + clong);
            SQLiteDatabase db = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
            db.execSQL("create table if not exists base(id integer primary key autoincrement, heading text, content text, type integer);");
            db.execSQL("create table if not exists loc(id integer primary key, lat double, long double, foreign key(id) references base(id));");
            try {
                Cursor c = db.rawQuery("select * from base natural join loc;", null);
                if (c.getCount() > 0) {
                    for (int i = 0; i < c.getCount(); i++) {
                        if (i == 0) {
                            c.moveToFirst();
                        } else {
                            c.moveToNext();
                        }
                        tlat = c.getDouble(c.getColumnIndex("lat"));
                        tlong = c.getDouble(c.getColumnIndex("long"));
                        Log.i("table", tlat + ", " + tlong);
                        double dist = Math.sqrt(Math.pow(clat - tlat, 2) + Math.pow(clong - tlong, 2));
                        Log.i("dist", "dist: " + dist);

                        if (dist < 1) {
                            Log.i("***********************", "Inside");
                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Intent i1 = new Intent(getApplicationContext(), DisplayLocation.class);
                            i1.putExtra("note_head", c.getString(c.getColumnIndex("heading")));
                            i1.putExtra("note_details", c.getString(c.getColumnIndex("content")));
                            i1.putExtra("where", "old");
                            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), i1, 0);
                            Notification n = new Notification.Builder(getApplicationContext())
                                    .setContentTitle(c.getString(c.getColumnIndex("heading")))
                                    .setContentText(c.getString(c.getColumnIndex("content")))
                                    .setContentIntent(pi)
                                    .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                                    .setVibrate(new long[]{100, 0, 300, 0, 10, 0, 50, 0, 100})
                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                    .build();
                            nm.notify(0, n);
                        }
                    }

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (isBetterLocation(loc, previousBestLocation)) {

            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}
