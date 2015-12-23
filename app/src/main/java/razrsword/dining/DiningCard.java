package razrsword.dining;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import razrsword.main.R;

/**
 * Created by razrs on 23-Dec-15.
 */

public class DiningCard extends RecyclerView.ViewHolder {
    protected TextView locationName;
    protected TextView dailyHours;
    protected TextView trafficLevels;

    public DiningCard(View v) {
        super(v);
        locationName =  (TextView) v.findViewById(R.id.txtName);
        dailyHours = (TextView)  v.findViewById(R.id.txtSurname);
        trafficLevels = (TextView)  v.findViewById(R.id.txtEmail);
    }
}
