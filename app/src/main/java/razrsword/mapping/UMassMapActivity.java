package razrsword.mapping;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import razrsword.main.R;

public class UMassMapActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Context context;
    private ProgressDialog pDialog;
    private Marker startMarker;
    MapView map;
    private int zoomLevel = 14;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    private double[] userLocation = new double[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_umass_map);
        overridePendingTransition(R.anim.enter_slide_in, R.anim.enter_slide_out);
        OpenStreetMapTileProviderConstants.setUserAgentValue("Razrsword's UMass App V.1");
        map = (MapView) findViewById(R.id.map);
        GeoPoint viewPoint = new GeoPoint(42.38955, -72.52817);
        GeoPoint markerPoint;
        IMapController mapController = map.getController();

        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);

        if (savedInstanceState != null) {
            viewPoint = new GeoPoint(savedInstanceState.getDouble("viewLat"), savedInstanceState.getDouble("viewLon"));
            zoomLevel = savedInstanceState.getInt("Zoom");
            if (savedInstanceState.getDouble("Latitude") != 0) {
                markerPoint = new GeoPoint(savedInstanceState.getDouble("Latitude"), savedInstanceState.getDouble("Longitude"));
                startMarker = new Marker(map);
                startMarker.setPosition(markerPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);
            }
            mapController.setCenter(viewPoint);
            mapController.setZoom(zoomLevel);
        } else {
            mapController.setCenter(viewPoint);
            mapController.setZoom(zoomLevel);
        }

        final ImageButton button = (ImageButton) findViewById(R.id.hideKeyboard);
        final AutoCompleteTextView edittext = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        final FloatingActionButton locateMyself = (FloatingActionButton) findViewById(R.id.fab);
        final List<Place> listOfPlace = getPlaces();


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                edittext.clearFocus();
            }
        });

        locateMyself.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locateMyself.setBackgroundColor(0x881C1C);
                userLocation = getGPS();
                Marker userMarker = new Marker(map);
                userMarker.setPosition(new GeoPoint(userLocation[0], userLocation[1]));
                userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(userMarker);
                map.getController().animateTo(new GeoPoint(userLocation[0], userLocation[1]));
            }
        });


        //Create Array Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.array_dropdown_layout, placesToNames(listOfPlace));
        //adapter.setDropDownViewResource(R.layout.array_dropdown_layout);
        //Find TextView control
        //Set the number of characters the user must type before the drop down list is shown
        edittext.setThreshold(1);
        edittext.setAdapter(adapter);
        edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                int listPosition = 0;
                for (int i = 0; i < listOfPlace.size(); i++) {
                    if (listOfPlace.get(i).getName().compareTo(edittext.getText().toString()) == 0) {
                        listPosition = i;
                        break;
                    }
                }
                hideKeyboard();
                GeoPoint goalLocation = new GeoPoint(Double.valueOf(listOfPlace.get(listPosition).getLatitude()), Double.valueOf(listOfPlace.get(listPosition).getLongitude()));
                addMarker(map,listOfPlace.get(listPosition).getName(),goalLocation);
                edittext.clearFocus();

                Log.v("Map stuff", goalLocation.getLatitude() + " " + listOfPlace.get(listPosition).getLatitude());
                Log.v("Map stuff 2", goalLocation.getLongitude() + " " + listOfPlace.get(listPosition).getLongitude());
                Log.v("Map stuff 3", listPosition + " ");
            }
        });

        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    int listPosition = getTopMatch(edittext,listOfPlace);
                    Log.v("Top Location", " " + listPosition);
                    hideKeyboard();
                    GeoPoint goalLocation = new GeoPoint(Double.valueOf(listOfPlace.get(listPosition).getLatitude()), Double.valueOf(listOfPlace.get(listPosition).getLongitude()));
                    addMarker(map,listOfPlace.get(listPosition).getName(),goalLocation);
                    //Toast toast = Toast.makeText(UMassMapActivity.this,listOfPlace.get(listPosition).getLatitude() + " ", Toast.LENGTH_LONG);
                    //toast.show();
                    return true;
                }
                return false;
            }
        });



    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private int getTopMatch(AutoCompleteTextView edittext, List<Place> listOfPlace){
        int listPosition = 0;
        double topPosition = 0;
        double topValue = 0;
        String name;
        String editTextContent = edittext.getText().toString();
        for (int i = 0; i < listOfPlace.size(); i++) {
            name = listOfPlace.get(i).getName();
            if (name.compareTo(editTextContent) == 0) {
                listPosition = i;
                break;
            } else {
                if((topValue = editDistance(name, editTextContent))>topPosition){
                    listPosition = i;
                    topPosition = topValue;
                }
            }

        }
        return listPosition;
    }

    public void addMarker(MapView map, String locationName, GeoPoint goalLocation){
        startMarker = new Marker(map);
        startMarker.setPosition(goalLocation);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(locationName);
        //map.getController().setCenter(goalLocation);
        map.getOverlays().clear();
        map.getController().setZoom(16);
        map.getOverlays().add(startMarker);
        map.invalidate();
        map.getController().animateTo(goalLocation);
    }

    /** @return an array of adjacent letter pairs contained in the input string */
    private static String[] letterPairs(String str) {
        int numPairs = str.length()-1;
        String[] pairs = new String[numPairs];
        for (int i=0; i<numPairs; i++) {
            pairs[i] = str.substring(i,i+2);
        }
        return pairs;
    }

    /** @return an ArrayList of 2-character Strings. */
    private static ArrayList wordLetterPairs(String str) {
        ArrayList allPairs = new ArrayList();
        // Tokenize the string and put the tokens/words into an array
        String[] words = str.split("\\s");
        // For each word
        for (int w=0; w < words.length; w++) {
            // Find the pairs of characters
            String[] pairsInWord = letterPairs(words[w]);
            for (int p=0; p < pairsInWord.length; p++) {
                allPairs.add(pairsInWord[p]);
            }
        }
        return allPairs;
    }

    /** @return lexical similarity value in the range [0,1] */
    public static double editDistance(String str1, String str2) {
        ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
        ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (int i=0; i<pairs1.size(); i++) {
            Object pair1=pairs1.get(i);
            for(int j=0; j<pairs2.size(); j++) {
                Object pair2=pairs2.get(j);
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }
        return (2.0*intersection)/union;
    }


    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition
        super.onBackPressed();

        overridePendingTransition(R.anim.exit_slide_in, R.anim.exit_slide_out_down);
    }

    /*private void setupWindowAnimations() {
        Slide slide = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slide = new Slide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            slide.setDuration(1000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(slide);
        }
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    public List<Place> getPlaces() {
        List<Place> places = null;
        XMLPullParseHandler parser = new XMLPullParseHandler();
        InputStream is = null;
        try {
            is = getAssets().open("locations.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        places = parser.parse(is);
        return places;
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                double[] gps = new double[2];
                if (l != null) {
                    gps[0] = 42.38955;
                    gps[1] = -72.52817;
                }
                return gps;
            }
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }

        return gps;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        if(this.startMarker != null) {
            savedInstanceState.putDouble("Latitude", UMassMapActivity.this.startMarker.getPosition().getLatitude());
            savedInstanceState.putDouble("Longitude", UMassMapActivity.this.startMarker.getPosition().getLongitude());
        } else {
            savedInstanceState.putDouble("Latitude", 0);
            savedInstanceState.putDouble("Longitude", 0);
        }
        Log.v("onSaveInstanceState", " saving data between instances" + UMassMapActivity.this.map.getMapCenter().getLatitude() + " " + UMassMapActivity.this.map.getZoomLevel());
        savedInstanceState.putDouble("viewLat", UMassMapActivity.this.map.getMapCenter().getLatitude());
        savedInstanceState.putDouble("viewLon", UMassMapActivity.this.map.getMapCenter().getLongitude());
        savedInstanceState.putInt("Zoom", UMassMapActivity.this.map.getZoomLevel());
        // etc.
    }

    public List<String> placesToNames(List<Place> places) {
        List<String> placenames = new ArrayList<>();
        ;
        assert places != null;
        Iterator<Place> location = places.iterator();
        while (location.hasNext()) {
            placenames.add(location.next().getName());
        }
        return placenames;
    }


    public Context getContext() {
        return context;
    }


}