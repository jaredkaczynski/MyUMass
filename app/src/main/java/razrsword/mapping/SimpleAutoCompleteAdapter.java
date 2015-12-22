package razrsword.mapping;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.Arrays;

public class SimpleAutoCompleteAdapter extends ArrayAdapter<String> {
    private String[] mData;
    private int[] mCurrentIds;

    public SimpleAutoCompleteAdapter(Context context, int textViewResourceId,
                                     String[] objects) {
        super(context, textViewResourceId, objects);
        mData=objects;
    }

    @Override
    public long getItemId(int position) {
        String data = getItem(position);
        int index = Arrays.asList(mData).indexOf(data);
            /*
             * Atention , if your list has more that one same String , you have to improve here  
             */
        // this will be used to get the id provided to the onItemClick callback
        if (index>0)
            return (long)mCurrentIds[index];
        else return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void setRealIds(int[] realIds) {
        mCurrentIds = realIds;
    }

}