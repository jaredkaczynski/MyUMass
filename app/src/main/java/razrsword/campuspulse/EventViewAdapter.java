package razrsword.campuspulse;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import razrsword.dining.ItemTouchHelperAdapter;
import razrsword.main.MainCard;
import razrsword.main.R;

/**
 * Created by razrs on 03-Jan-16.
 */
public class EventViewAdapter extends RecyclerView.Adapter<EventViewAdapter.mainButtonHolder>
        implements ItemTouchHelperAdapter {

    List<EventCard> mainActivityCards;

    @Override
    public mainButtonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_layout, parent, false);
        mainButtonHolder lvh = new mainButtonHolder(v);
        return lvh;
    }

    EventViewAdapter(List<EventCard> mainActivityCards) {
        this.mainActivityCards = mainActivityCards;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(mainButtonHolder mainButtonHolder, int i) {
        mainButtonHolder.locationName.setText(mainActivityCards.get(i).eventTitle);
        mainButtonHolder.locationImage.setImageResource(mainActivityCards.get(i).imageID);
        mainButtonHolder.locationImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return mainActivityCards.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mainActivityCards, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mainActivityCards, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public static class mainButtonHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView locationName;
        ImageView locationImage;

        mainButtonHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.location_card);
            locationName = (TextView) itemView.findViewById(R.id.location_name);
            locationImage = (ImageView) itemView.findViewById(R.id.dining_location_image);
        }
    }

}

