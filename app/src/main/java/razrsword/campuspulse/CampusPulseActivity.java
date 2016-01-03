package razrsword.campuspulse;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import razrsword.ExtendedItemAnimator;
import razrsword.activities.BusTrackerActivity;
import razrsword.dining.DiningActivity;
import razrsword.dining.RecyclerItemClickListener;
import razrsword.main.MainCard;
import razrsword.main.MainViewAdapter;
import razrsword.main.R;
import razrsword.main.SimpleItemTouchHelperCallbackMain;
import razrsword.mapping.UMassMapActivity;

public class CampusPulseActivity extends AppCompatActivity {
    RecyclerView rv;
    EventViewAdapter adapter;
    String campusXmlLocalDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CampusPulseXML.xml";
    Context context;

    List<Class<?>> locationClassList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_pulse);
        List<EventCard> locationNameList;
        locationNameList = new ArrayList<>();
        locationClassList = new ArrayList<>();
        locationClassList.add(UMassMapActivity.class);
        locationClassList.add(BusTrackerActivity.class);
        locationClassList.add(DiningActivity.class);
        locationClassList.add(CampusPulseActivity.class);

        locationNameList.add(new EventCard("UMass Map",R.drawable.berkshire));
        locationNameList.add(new EventCard("UMass Bus", R.drawable.berkshire));
        locationNameList.add(new EventCard("Dining", R.drawable.berkshire));
        locationNameList.add(new EventCard("Campus Events", R.drawable.berkshire));
        locationNameList.add(new EventCard("DeleteXML", R.drawable.berkshire));

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        adapter = new EventViewAdapter(locationNameList);
        //adapter.diningLocations.add(new DiningLocation("Worcester"));
        //adapter.diningLocations.add(new DiningLocation("Berkshire"));
        // Inflate the layout for this fragment
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv = (RecyclerView) this.findViewById(R.id.recyclerview_campus_pulse);
        assert rv != null;
        Log.v("RecyclerView", "Should be adding it");
        rv.setItemAnimator(new ExtendedItemAnimator());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallbackMain(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0:
                                //animateIntent(view,rv.findViewHolderForAdapterPosition(position).itemView,UMassMapActivity.class,position);
                                Intent intent = new Intent(view.getContext(), locationClassList.get(position));
                                startActivity(intent);
                                break;
                            case 1:
                                animateIntent(view, rv.findViewHolderForAdapterPosition(position).itemView, locationClassList.get(position), position);
                                break;
                            case 2:
                                animateIntent(view, rv.findViewHolderForAdapterPosition(position).itemView, locationClassList.get(position), position);
                                break;
                            case 3:
                                animateIntent(view, rv.findViewHolderForAdapterPosition(position).itemView, locationClassList.get(position), position);
                                break;
                            case 4:
                                File file = new File(campusXmlLocalDirectory);
                                file.delete();
                                Toast.makeText(context, "XML Deleted", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                })
        );
    }
    public void animateIntent(View view,View sourceView, Class<?> cls,int position) {

        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(this, cls);
        //intent.putExtra("locationName", locationNameList.get(position).locationName);

        // Get the transition name from the string
        String transitionName = getString(R.string.transition_string);

        // Define the view that the animation will start from
        View viewStart = sourceView;

        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        viewStart,   // Starting view
                        transitionName    // The String
                );
        //Start the Intent
        ActivityCompat.startActivity(this, intent, options.toBundle());

    }
}
