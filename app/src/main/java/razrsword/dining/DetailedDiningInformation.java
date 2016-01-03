package razrsword.dining;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razrsword.main.R;

public class DetailedDiningInformation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_dining_information);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        @SuppressLint("WrongViewCast") AppCompatImageView test = (AppCompatImageView) this.findViewById(R.id.dining_location_image);
        int statusBarHeight = (int) Math.ceil(25 * DetailedDiningInformation.this.getResources().getDisplayMetrics().density);
        test.setPadding(0,getStatusBarHeight(),0,0);
        test.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        String location = "error";
        if (extras != null) {
            location = extras.getString("locationName");
        }

        TextView locationName = (TextView) this.findViewById(R.id.location_name);
        locationName.setText(location);
    }
    int getStatusBarHeight(){
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight= contentViewTop - statusBarHeight;
        Log.v("statusbar height", statusBarHeight + " ");
        return statusBarHeight;
    }


}
