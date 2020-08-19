package codepath.simpletodo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

//responsible for displaying data from the model into a row in the recycler view
public class itemsAdapter extends RecyclerView.Adapter<itemsAdapter.ViewHolder> {
    public interface OnClickListener {
        void onItemClicked(int position);
    }





    public interface OnLongClickListener{
        void onItemLongClicked(int position);

    }
    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public itemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflater to a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //wrap it inside a view holder and return it
        return new ViewHolder(todoView);
    }
    //responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //Grab item at the position
        String item = items.get(position);

        //color
        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(265));
        holder.tvItem.setBackgroundColor(currentColor);

        int newColor = Color.rgb(255,255,255);
        holder.tvItem.setTextColor(newColor);

        //bind item into the specified view holder
        holder.bind(item);
    }
    //tells the RV how many items are in the list
    @Override
    public int getItemCount() {
           return items.size();
    }

    //container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);

        }
    //responsible for updating view inside of the view holder with data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //notifying which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());

                    return true;
                }
            });

        }
    }
}
