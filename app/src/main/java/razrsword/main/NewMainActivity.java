package razrsword.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import razrsword.activities.BusTrackerActivity;
import razrsword.dining.DiningActivity;
import razrsword.dining.RecyclerItemClickListener;
import razrsword.dining.SimpleItemTouchHelperCallbackDining;
import razrsword.mapping.UMassMapActivity;

public class NewMainActivity extends AppCompatActivity {
    List<MainCard> locationNameList = null;
    RecyclerView rv;
    MainViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        List<MainCard> locationNameList = null;
        locationNameList = new ArrayList<>();
        locationNameList.add(new MainCard("Berkshire Dining Commons",R.drawable.berkshire));
        locationNameList.add(new MainCard("Worcester Dining Commons", R.drawable.berkshire));
        locationNameList.add(new MainCard("Hampshire Dining Commons", R.drawable.berkshire));
        locationNameList.add(new MainCard("Franklin Dining Commons", R.drawable.berkshire));
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        adapter = new MainViewAdapter(locationNameList);
        //adapter.diningLocations.add(new DiningLocation("Worcester"));
        //adapter.diningLocations.add(new DiningLocation("Berkshire"));
        // Inflate the layout for this fragment
        GridLayoutManager llm = new GridLayoutManager(this,(int) Math.floor(dpWidth / 120));
        rv = (RecyclerView) this.findViewById(R.id.recyclerview_main);
        assert rv != null;
        Log.v("RecyclerView", "Should be adding it");
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallbackMain(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //// TODO: 23-Dec-15 add activity swap that's pretty

                        switch (position){
                            case 0:
                                animateIntent(view,rv.findViewHolderForAdapterPosition(position).itemView,UMassMapActivity.class,position);
                                break;
                            case 1:
                                animateIntent(view,rv.findViewHolderForAdapterPosition(position).itemView,BusTrackerActivity.class,position);
                                break;
                            case 2:
                                animateIntent(view,rv.findViewHolderForAdapterPosition(position).itemView,DiningActivity.class,position);
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
