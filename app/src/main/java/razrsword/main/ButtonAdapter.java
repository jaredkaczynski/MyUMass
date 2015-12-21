package razrsword.main;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

/**
 * Created by razrs on 21-Dec-15.
 */
public class ButtonAdapter extends BaseAdapter {
    private Context mContext;


    public ButtonAdapter(Context c) {
        mContext = c;
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
    public View getView(int position, View convertView, ViewGroup parent) {
         DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
         int width = metrics.widthPixels;
         //int height = metrics.heightPixels;
        //ImageView imageView;
        ImageButton button;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            button = new ImageButton(mContext);
            button.setLayoutParams(new GridView.LayoutParams(width/3, width/3));
            //button.setScaleType(ImageView.ScaleType.CENTER_CROP);
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (ImageButton) convertView;
        }
        button.setImageResource(mThumbIds[position]);
        //imageView.setImageResource(mThumbIds[position]);
        return button;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
}