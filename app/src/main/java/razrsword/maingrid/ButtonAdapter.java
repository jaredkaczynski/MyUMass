package razrsword.maingrid;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import razrsword.activities.NavigationActivity;
import razrsword.dining.DiningActivity;
import razrsword.mapping.UMassMapActivity;
import razrsword.main.R;

/**
 * Created by razrs on 21-Dec-15.
 */
public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private int width;



    public ButtonAdapter(Context c) {
        mContext = c;
    }

    public Context getContext(){
        return mContext;
    }
    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }



    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        width = metrics.widthPixels > metrics.heightPixels ? metrics.heightPixels : metrics.widthPixels;
         //int height = metrics.heightPixels;
        //ImageView imageView;
        ImageButton button;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            AbsListView.LayoutParams gridView = new GridView.LayoutParams((int)(width/3), (int)(width/3));
            button = new ImageButton(mContext);
            //This determines the size of the buttons/image
            button.setLayoutParams(gridView);
            //button.setScaleType(ImageView.ScaleType.CENTER_CROP);
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (ImageButton) convertView;
        }
        button.setImageResource(mThumbIds[position]);
        if(buttonColorFilter[position] != null){
            button.setColorFilter(buttonColorFilter[position]);
        }
        //imageView.setImageResource(mThumbIds[position]);
        button.setId(position);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent;
                switch (position) {
                    case 0:
                        myIntent = new Intent(mContext, UMassMapActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        mContext.startActivity(myIntent);
                        break;
                    case 1:
                        myIntent = new Intent(mContext, NavigationActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        mContext.startActivity(myIntent);
                        break;
                    case 2:
                        myIntent = new Intent(mContext, DiningActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        mContext.startActivity(myIntent);
                        break;
                }
            }
        });
        return button;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_map_black_48dp, R.drawable.ic_directions_bus_black_48dp,
            R.drawable.ic_place_black_48dp, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
    };
    private ColorFilter[] buttonColorFilter = {new LightingColorFilter(0x003B5C,0x003B5C),
            new LightingColorFilter(0x003B5C,0x003B5C),null,null,null,null,null,null,

    };
}