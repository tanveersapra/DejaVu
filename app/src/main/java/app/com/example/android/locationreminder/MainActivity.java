package app.com.example.android.locationreminder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	public static ArrayList<String> head = new ArrayList<String>();
	public static ArrayList<String> content = new ArrayList<String>();
	public static ArrayList<String> ida = new ArrayList<String>();
	public static ArrayList<Integer> type = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		this.registerForContextMenu(fab);
		final SQLiteDatabase notedb = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
		notedb.execSQL("create table if not exists base(id integer primary key autoincrement, heading text, content text, type integer);");
		notedb.execSQL("create table if not exists alarm(id integer,  Date date, hour integer, minutes integer, foreign key(id) references base(id) on delete cascade);");
		try {
			notedb.execSQL("delete from base  where id in (select id from alarm where date < date('now'));");
		} catch (Exception e) {
			Log.d("********SQLite Error", "onCreate: " + e.getMessage());
		}
		Cursor c = notedb.rawQuery("select heading, content, type,id from base;", null);
		String[] Heading = new String[c.getCount()];
		String[] Content = new String[c.getCount()];
		int[] Type = new int[c.getCount()];
		final String[] id = new String[c.getCount()];

		head.clear();
		content.clear();
		ida.clear();
		type.clear();
		for (int i = 0; i < c.getCount(); i++) {
			if (i == 0) {
				c.moveToFirst();
			} else {
				c.moveToNext();
			}

			Heading[i] = c.getString(0);
			Content[i] = c.getString(1);
			id[i] = c.getString(3);
			Type[i] = c.getInt(2);
			head.add(Heading[i]);
			content.add(Content[i]);
			ida.add(id[i]);
			type.add(Type[i]);

		}


		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		adapter = new RecyclerAdapter(getApplicationContext());
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		SwipeableRecyclerViewTouchListener swipeTouchListener =
				new SwipeableRecyclerViewTouchListener(recyclerView,
						new SwipeableRecyclerViewTouchListener.SwipeListener() {

							public boolean canSwipe(int position) {
								return true;
							}

							@Override
							public boolean canSwipeLeft(final int position) {
								return true;
							}

							@Override
							public boolean canSwipeRight(final int position) {
								return true;
							}

							@Override
							public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
								for (int position : reverseSortedPositions) {
									String removableID = id[position];
									notedb.execSQL("delete from base where id='" + removableID + "';");
									head.remove(position);
									content.remove(position);
									ida.remove(position);
									type.remove(position);
									adapter.notifyItemRemoved(position);
								}
								adapter.notifyDataSetChanged();
//								recyclerView.setAdapter(adapter);

							}

							@Override
							public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
								for (int position : reverseSortedPositions) {
									String removableID = id[position];
									notedb.execSQL("delete from base where id='" + removableID + "';");
									head.remove(position);
									content.remove(position);
									ida.remove(position);
									type.remove(position);
									adapter.notifyItemRemoved(position);
								}
								adapter.notifyDataSetChanged();

							}
						});

		recyclerView.addOnItemTouchListener(swipeTouchListener);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
		}
		startService(new Intent(this, ListenerService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.fab) {
			this.getMenuInflater().inflate(R.menu.contextual_menu, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		int selectedItemID = item.getItemId();
		Intent i;
		String s = "New Note";
		String d = "Add Description";
		switch (selectedItemID) {
			case R.id.type1:
				i = new Intent(this, DisplayNote.class);
				i.putExtra("note_head", s);
				i.putExtra("note_details", d);
				i.putExtra("where", "new");
				startActivity(i);
				break;

			case R.id.type2:
				i = new Intent(this, DisplayLocation.class);
				i.putExtra("note_head", s);
				i.putExtra("note_details", d);
				i.putExtra("where", "new");
				startActivity(i);
				break;
			case R.id.type3:
				i = new Intent(this, DisplayAlarm.class);
				i.putExtra("note_head", s);
				i.putExtra("note_details", d);
				i.putExtra("where", "new");
				startActivity(i);
				break;
		}
		return super.onContextItemSelected(item);
	}

	public void new_note(View view) {
		//Toast.makeText(this,"You clicked on the plus sign",Toast.LENGTH_SHORT).show();
		view.showContextMenu();

//        Intent i = new Intent(this, DisplayAlarm.class);
//        startActivity(i);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
				.setMessage("Are you sure?")
				.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
					}
				}).setNegativeButton("no", null).show();
	}
}
