package razrsword.maingrid;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import razrsword.activities.NavigationActivity;
import razrsword.main.R;

/**
 * Created by razrs on 21-Dec-15.
 */
public class ButtonAdapter extends BaseAdapter {
    private Context mContext;


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
         int width = metrics.widthPixels;
         //int height = metrics.heightPixels;
        //ImageView imageView;
        ImageButton button;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            button = new ImageButton(mContext);
            //This determines the size of the buttons/image
            button.setLayoutParams(new GridView.LayoutParams((int)(width/3), (int)(width/3)));
            //button.setScaleType(ImageView.ScaleType.CENTER_CROP);
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (ImageButton) convertView;
        }
        button.setImageResource(mThumbIds[position]);
        //imageView.setImageResource(mThumbIds[position]);
        button.setId(position);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent;
                switch(position){
                    case 0:
                        myIntent = new Intent(mContext, NavigationActivity.class);
                        //myIntent.putExtra("key", value); //Optional parameters
                        mContext.startActivity(myIntent);
                        break;
                    case 1:
                        myIntent = new Intent(mContext, NavigationActivity.class);
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
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
    };
}