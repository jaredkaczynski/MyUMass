package razrsword.main;

/**
 * Created by razrs on 24-Dec-15.
 */

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

/**
 * Created by razrs on 23-Dec-15.
 */

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.mainButtonHolder>
        implements ItemTouchHelperAdapter {

    List<MainCard> mainActivityCards;
    List<Class<?>> locationClassList;

    @Override
    public mainButtonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_layout, parent, false);
        mainButtonHolder lvh = new mainButtonHolder(v);
        return lvh;
    }

    MainViewAdapter(List<MainCard> mainActivityCards, List<Class<?>> locationClassList) {
        this.mainActivityCards = mainActivityCards;
        this.locationClassList = locationClassList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(mainButtonHolder mainButtonHolder, int i) {
        mainButtonHolder.locationName.setText(mainActivityCards.get(i).locationName);
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
                Collections.swap(locationClassList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mainActivityCards, i, i - 1);
                Collections.swap(locationClassList, i, i - 1);
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
