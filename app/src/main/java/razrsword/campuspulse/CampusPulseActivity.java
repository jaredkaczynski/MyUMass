package razrsword.campuspulse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import razrsword.ExtendedItemAnimator;
import razrsword.callbacks.SimpleItemTouchHelperCallback;
import razrsword.dining.RecyclerItemClickListener;
import razrsword.main.R;

public class CampusPulseActivity extends AppCompatActivity {
    RecyclerView rv;
    CampusPulseEventViewAdapter adapter;
    String campusXmlLocalDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CampusPulseXML.xml";
    Context context;
    File eventsXML = new File(campusXmlLocalDirectory);
    List<CampusPulseStackOverflowXmlParser.Entry> eventList;
    List<CampusPulseEventCard> locationNameList = new ArrayList<>();

    //number of entries to load on start
    int numEntries = 10;
    //number of entries to load each end
    int entryChunk = 10;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_pulse);
        setTitle("Campus Pulse Events");
        //eventList = null;
        if(eventsXML.exists()){
            locationNameList = parseXML(numEntries);
        }else{
        /*locationNameList.add(new CampusPulseEventCard("UMass Map",R.drawable.berkshire));
        locationNameList.add(new CampusPulseEventCard("UMass Bus", R.drawable.berkshire));
        locationNameList.add(new CampusPulseEventCard("Dining", R.drawable.berkshire));
        locationNameList.add(new CampusPulseEventCard("Campus Events", R.drawable.berkshire));
        locationNameList.add(new CampusPulseEventCard("DeleteXML", R.drawable.berkshire));*/
        }
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        adapter = new CampusPulseEventViewAdapter(locationNameList);
        //adapter.diningLocations.add(new DiningLocation("Worcester"));
        //adapter.diningLocations.add(new DiningLocation("Berkshire"));
        // Inflate the layout for this fragment
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        rv = (RecyclerView) this.findViewById(R.id.recyclerview_campus_pulse);
        assert rv != null;
        Log.v("RecyclerView", "Should be adding it");
        rv.setItemAnimator(new ExtendedItemAnimator());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //eventList.get(position);
                        //pass image through
                        createImageFromBitmap(locationNameList.get(position).mainImage);
                        animateIntent(view, CampusPulseDetailActivity.class, position);

                    }

                })
        );
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = rv.getChildCount();
                totalItemCount = llm.getItemCount();
                firstVisibleItem = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    Log.v("Yaeye!", "end called");
                    numEntries+=entryChunk;
                    locationNameList = parseXML(numEntries);
                    Toast.makeText(recyclerView.getContext(), "End reached " + locationNameList.size(), Toast.LENGTH_LONG).show();

                    adapter.notifyItemRangeInserted(numEntries-1,entryChunk);



                    loading = true;
                }
            }
        });
    }

    //Used to save an image to file for fast load
    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "temppulseimage";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public List<CampusPulseEventCard>  parseXML(int parseCount){
        CampusPulseStackOverflowXmlParser test = new CampusPulseStackOverflowXmlParser();
        FileInputStream eventsInStream = null;

        try {
            eventsInStream = new FileInputStream(eventsXML);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        eventList = test.parse(eventsInStream, parseCount);

        Iterator<CampusPulseStackOverflowXmlParser.Entry> eventListIterator = eventList.iterator();
        if (eventListIterator.hasNext() && parseCount > entryChunk && locationNameList.size() < eventList.size()){
            int i = 0;
            while(i < parseCount-entryChunk){
                eventListIterator.next();
                i++;
            }
        }
        if(locationNameList.size() < eventList.size()){
            while (eventListIterator.hasNext()) {
                CampusPulseStackOverflowXmlParser.Entry tempEntry = eventListIterator.next();
                String dateStart;
                String dateEnd;
                DateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm a", Locale.US);
                dateStart = formatter.format(new Date(tempEntry.getDateStart()));
                dateEnd = formatter.format(new Date(tempEntry.getDateEnd()));
                DateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
                if(dayFormat.format(tempEntry.getDateStart()) == dayFormat.format(tempEntry.getDateEnd())){
                    dateEnd = new SimpleDateFormat("h:mm a", Locale.US).format(dateStart);
                }
                locationNameList.add(new CampusPulseEventCard(tempEntry.getTitle(),dateStart, dateEnd,tempEntry.getTextField(),tempEntry.getImageLink(),tempEntry.getEventLocation(),0,0));
            }
        }


        return locationNameList;
    }

    public void animateIntent(View sourceView, Class<?> cls,int position) {

        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(CampusPulseActivity.this, cls)
                .putExtra("eventObject", eventList.get(position))
                .putExtra("vibrantcolor", locationNameList.get(position).vibrantColor)
                .putExtra("lightcolor", locationNameList.get(position).lightVibrantColor);
        //intent.putExtra("eventName", locationNameList.get(position).eventName);

        // Get the transition name from the string
        String transitionNameOne = getString(R.string.transition_string_one);
        String transitionNameTwo = getString(R.string.transition_string_two);
        String transitionNameThree = getString(R.string.transition_string_three);
        String transitionNameFour = getString(R.string.transition_string_four);



        // Define the view that the animation will start from
        View viewStart = sourceView;
        Pair<View, String> p1 = Pair.create((View)viewStart.findViewById(R.id.event_card_image_block), transitionNameOne);
        Pair<View, String> p2 = Pair.create((View)viewStart.findViewById(R.id.event_date), transitionNameTwo);
        Pair<View, String> p3 = Pair.create((View)viewStart.findViewById(R.id.event_location), transitionNameThree);
        //Pair<View, String> p4 = Pair.create((View)viewStart.findViewById(R.id.event_name), transitionNameFour);


        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        p1,
                        p2,
                        p3
                );
        //Start the Intent
        ActivityCompat.startActivity(this, intent, options.toBundle());

    }
}
