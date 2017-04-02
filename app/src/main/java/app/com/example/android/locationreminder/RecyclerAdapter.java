package app.com.example.android.locationreminder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Navneet Jain on 4/2/2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    String[] note_names,note_summary;
    public RecyclerAdapter(String[] note_names,String[] note_summary)
    {
        this.note_names=note_names;
        this.note_summary=note_summary;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main,parent,false);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position)
    {
        holder.tx_note_name.setText(note_names[position]);
        holder.tx_note_summary.setText(note_summary[position]);
    }

    @Override
    public int getItemCount() {
        return note_names.length;
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView tx_note_name,tx_note_summary;
        public RecyclerViewHolder(View view)
        {
            super(view);
            tx_note_name=(TextView)view.findViewById(R.id.note_name);
            tx_note_summary=(TextView)view.findViewById(R.id.note_summary);
        }
    }
}
