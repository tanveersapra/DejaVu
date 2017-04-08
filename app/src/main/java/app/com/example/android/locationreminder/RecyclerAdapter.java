package app.com.example.android.locationreminder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Navneet Jain on 4/2/2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

	Context context;
	String[] note_names, note_summary;
	int [] note_type;


	public RecyclerAdapter(String[] note_names, String[] note_summary,int[] note_type, Context con) {
		context = con;
		this.note_names = note_names;
		this.note_summary = note_summary;
		this.note_type=note_type;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main, parent, false);
		RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
		return recyclerViewHolder;
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
		holder.tx_note_name.setText(note_names[position]);
		holder.tx_note_summary.setText(note_summary[position]);
		holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				//Toast.makeText(v,"You clicked on a Item",Toast.LENGTH_SHORT).show();

				//int point=recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild());
				String s=note_names[position];
				String d = note_summary[position];
				int type=note_type[position];
				Intent i;
				if(type==1) {
					i = new Intent(context, DisplayNote.class);
					i.putExtra("note_head", s);
					i.putExtra("note_details", d);
				}
				else{
					i = new Intent(context, location_alarm_display.class);
					i.putExtra("note_head", s);
					i.putExtra("note_details", d);
				}
				context.startActivity(i);
			}
		});
	}

	@Override
	public int getItemCount() {
		return note_names.length;
	}

	public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
		TextView tx_note_name, tx_note_summary;
		RelativeLayout relativeLayout;

		public RecyclerViewHolder(View view) {
			super(view);
			relativeLayout = (RelativeLayout) view.findViewById(R.id.rel_layout);
			tx_note_name = (TextView) view.findViewById(R.id.note_name);
			tx_note_summary = (TextView) view.findViewById(R.id.note_summary);

		}
	}
}
