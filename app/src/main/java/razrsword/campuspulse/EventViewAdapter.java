package razrsword.campuspulse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import razrsword.dining.ItemTouchHelperAdapter;
import razrsword.main.R;

/**
 * Created by razrs on 03-Jan-16.
 */
public class EventViewAdapter extends RecyclerView.Adapter<EventViewAdapter.mainButtonHolder>
        implements ItemTouchHelperAdapter {

    List<EventCard> campusPulseCards;

    @Override
    public mainButtonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout, parent, false);
        mainButtonHolder lvh = new mainButtonHolder(v);
        return lvh;
    }

    EventViewAdapter(List<EventCard> campusPulseCards) {
        this.campusPulseCards = campusPulseCards;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(mainButtonHolder mainButtonHolder, int i) {
        mainButtonHolder.eventName.setText(campusPulseCards.get(i).eventTitle);
        mainButtonHolder.eventDate.setText(campusPulseCards.get(i).eventDate);
        if(campusPulseCards.get(i).eventImageURL != null){
        Log.v("EventImageURL", campusPulseCards.get(i).eventImageURL);
            new DownloadImageTask((ImageView) mainButtonHolder.locationImage)
                    .execute(campusPulseCards.get(i).eventImageURL);
        mainButtonHolder.locationImage.setImageDrawable(LoadImageFromWebOperations(campusPulseCards.get(i).eventImageURL));
        mainButtonHolder.locationImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            mainButtonHolder.locationImage.setImageResource(R.drawable.ic_close_black_24dp);
            mainButtonHolder.locationImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public int getItemCount() {
        return campusPulseCards.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(campusPulseCards, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(campusPulseCards, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static class mainButtonHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView eventName;
        TextView eventDate;
        ImageView locationImage;

        mainButtonHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.event_card);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            locationImage = (ImageView) itemView.findViewById(R.id.event_location_image);
        }
    }

}

