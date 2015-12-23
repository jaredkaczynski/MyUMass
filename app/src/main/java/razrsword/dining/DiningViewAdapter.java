package razrsword.dining;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import razrsword.main.R;

/**
 * Created by razrs on 23-Dec-15.
 */

public class DiningViewAdapter extends RecyclerView.Adapter<DiningViewAdapter.locationViewHolder>{

    List<DiningLocation> diningLocations;

    @Override
    public locationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dining_card_layout, parent, false);
        locationViewHolder lvh = new locationViewHolder(v);
        return lvh;
    }

    DiningViewAdapter(List<DiningLocation> diningLocations){
        this.diningLocations = diningLocations;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(locationViewHolder locationViewHolder, int i) {
        locationViewHolder.locationName.setText(diningLocations.get(i).locationName);
    }

    @Override
    public int getItemCount() {
        return diningLocations.size();
    }

    public static class locationViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView locationName;

        locationViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.location_card);
            locationName = (TextView)itemView.findViewById(R.id.location_name);
        }
    }

}