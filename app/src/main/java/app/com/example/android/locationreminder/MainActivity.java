package app.com.example.android.locationreminder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    String note_names[] = {"First Note", "Sumeet ka pyar", "Tanveer ka lund", "Navneet is cool", "Art Appreciation", "Depressing Sights",
            "Depressing Nights", "Things that scare me", "Rakshason ka Jhund", "bkl = ?"};
    String note_summary[] = {"bola tha this is my first note chutiya", "jo kisi ko na mila", "jo kisiko na dikha, size matters", "badi obvious si baat hain why repeat",
            "Picasso, Donatello, Raphael, Michaelangelo, Leonardo, Pogba", "Andhere mein girlfriend na dikhi", "But alcohol shall cure me tonight",
            "Things between your legs, things that come out from tere", "Tanny rules the roost", "abbey behen ke laude, badda khaas launda"};
    int note_type[] = {1, 1, 2, 2, 1, 1, 2, 2, 2, 1};
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        this.registerForContextMenu(fab);
        SQLiteDatabase notedb = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
        notedb.execSQL("create table if not exists base(id integer primary key autoincrement, heading text, content text, type integer);");
        notedb.execSQL("create table if not exists alarm(id integer foreign key references base(id), Date date, hour integer, minutes integer);");
        //notedb.execSQL("insert into base(heading, content, type) values('Knock knock', 'Who is there?', 0);");
        Cursor c = notedb.rawQuery("select heading, content, type from base;", null);
        String[] Heading = new String[c.getCount()];
        String[] Content = new String[c.getCount()];
        int[] Type = new int[c.getCount()];
        for (int i = 0; i < c.getCount(); i++){
            if(i == 0){
                c.moveToFirst();
            }
            else{
                c.moveToNext();
            }

            Heading[i] = c.getString(0);
	        Content[i] = c.getString(1);

            Type[i] = c.getInt(2);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(Heading, Content, Type, getApplicationContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.fab)
        {
            this.getMenuInflater().inflate(R.menu.contextual_menu,menu);
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
    int selectedItemID=item.getItemId();
        Intent i;
        String s="New Note";
        String d="Add Description";
        switch(selectedItemID)
        {
            case R.id.type1:
                i=new Intent(this,DisplayNote.class);
                i.putExtra("note_head", s);
                i.putExtra("note_details", d);
                i.putExtra("where","new");
                startActivity(i);
                break;

            case  R.id.type2:
                i = new Intent(this, DisplayLocation.class);
                i.putExtra("note_head", s);
                i.putExtra("note_details", d);
	            i.putExtra("where","new");
                startActivity(i);
                break;
            case R.id.type3:
                i = new Intent(this, DisplayAlarm.class);
                i.putExtra("note_head", s);
                i.putExtra("note_details", d);
	            i.putExtra("where","new");
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
