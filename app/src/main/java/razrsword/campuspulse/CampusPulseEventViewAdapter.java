package razrsword.campuspulse;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import razrsword.dining.ItemTouchHelperAdapter;
import razrsword.main.R;

/**
 * Created by razrs on 03-Jan-16.
 */
public class CampusPulseEventViewAdapter extends RecyclerView.Adapter<CampusPulseEventViewAdapter.mainButtonHolder>
        implements ItemTouchHelperAdapter {

    List<CampusPulseEventCard> campusPulseCards;
    ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.ic_event_white_48dp) // resource or drawable
            .cacheInMemory(false) // default
            .cacheOnDisk(false)
            .build();

    @Override
    public mainButtonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout, parent, false);
        mainButtonHolder lvh = new mainButtonHolder(v);
        return lvh;
    }

    CampusPulseEventViewAdapter(List<CampusPulseEventCard> campusPulseCards) {
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
        final ImageView temp = mainButtonHolder.locationImage;
        if(campusPulseCards.get(i).eventImageURL != null){
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mainButtonHolder.cv.getContext()).build();
            ImageLoader.getInstance().init(config);
        Log.v("EventImageURL", campusPulseCards.get(i).eventImageURL);
            // Load image, decode it to Bitmap and return Bitmap to callback
            ImageSize targetSize = new ImageSize(500, 250); // result Bitmap will be fit to this size
            imageLoader.loadImage(campusPulseCards.get(i).eventImageURL, targetSize, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    temp.setBackgroundResource(R.color.colorPrimary);
                    temp.setColorFilter(Color.argb(255, 255, 255, 255));
                    Palette palette = Palette.from(loadedImage).generate();
                    temp.setImageBitmap(loadedImage);
                    int[] colors = {Color.parseColor(palette.getVibrantSwatch().toString()),Color.parseColor(palette.getMutedSwatch().toString())};

                    //create a new gradient color
                    GradientDrawable gd = new GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT, colors);
                    Log.v("Set image", "Set image to event");
                    temp.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            });
            //mainButtonHolder.locationImage.setImageDrawable(LoadImageFromWebOperations(campusPulseCards.get(i).eventImageURL));
        } else {
            temp.setImageResource(R.drawable.ic_event_white_48dp);
            temp.setScaleType(ImageView.ScaleType.FIT_CENTER);
            temp.setBackgroundResource(R.color.colorPrimary);
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

