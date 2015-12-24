package razrsword.mapping;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class UMassMapActivity extends AppCompatActivity implements OnMapReadyCallback{


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Context context;
    private ProgressDialog pDialog;
    private Marker startMarker;
    MapView map;
    com.google.android.gms.maps.MapView mapView;
    GoogleMap googleMap;
    private int zoomLevel = 14;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    private double[] userLocation = new double[2];
    private boolean GoogleMaps = true;
    List<Place> listOfPlace;
    private final com.google.android.gms.maps.model.Marker[] placeMarker = new com.google.android.gms.maps.model.Marker[1];
    Bundle globalSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalSavedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_umass_map);
        overridePendingTransition(R.anim.enter_slide_in, R.anim.enter_slide_out);
        context = this;

        final ImageButton backButton = (ImageButton) findViewById(R.id.hideKeyboard);
        final ImageButton clearButton = (ImageButton) findViewById(R.id.clearText);
        final AutoCompleteTextView edittext = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        final FloatingActionButton locateButton = (FloatingActionButton) findViewById(R.id.locateFab);


        initializeClearButton(clearButton, edittext);
        initializeBackButton(backButton, edittext);

        IMapController mapController = null;
        if (GoogleMaps) {
            initializeLocateButtonGoogleMaps(locateButton);
            initializeGoogleMaps(savedInstanceState);
        } else {
            mapController = initializeOSMDroidMaps(savedInstanceState);
            initializeLocateButtonOSM(locateButton);
            map.setVisibility(View.VISIBLE);

        }

        if (savedInstanceState != null) {
            if (!GoogleMaps) {
                mapController.setCenter(new GeoPoint(savedInstanceState.getDouble("viewLat"), savedInstanceState.getDouble("viewLon")));
                mapController.setZoom((int) savedInstanceState.getFloat("Zoom"));
                if (savedInstanceState.getDouble("Latitude") != 0) {
                    startMarker = new Marker(map);
                    startMarker.setPosition(new GeoPoint(savedInstanceState.getDouble("Latitude"), savedInstanceState.getDouble("Longitude")));
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(startMarker);
                }
            }


        } else if(!GoogleMaps){
            mapController.setCenter(new GeoPoint(42.38955, -72.52817));
            mapController.setZoom(zoomLevel);
        }

        listOfPlace = getPlaces();

        //Create Array Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.array_dropdown_layout, placesToNames(listOfPlace));
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
                hideKeyboard(getCurrentFocus().getWindowToken());
                Double latitude = Double.valueOf(listOfPlace.get(listPosition).getLatitude());
                Double longitude = Double.valueOf(listOfPlace.get(listPosition).getLongitude());

                if (GoogleMaps) {
                    if (placeMarker[0] != null) {
                        placeMarker[0].remove();
                    }
                    placeMarker[0] = addMarkerGoogleMaps(mapView, listOfPlace.get(listPosition).getName(), new LatLng(latitude, longitude));
                } else {
                    GeoPoint goalLocation = new GeoPoint(latitude, longitude);
                    addMarkerOSM(map, listOfPlace.get(listPosition).getName(), goalLocation);
                }
                edittext.clearFocus();

                Log.v("Map onItemClick", " " + listOfPlace.get(listPosition).getLatitude());
            }
        });

        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" backButton
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    int listPosition = getTopMatch(edittext, listOfPlace);
                    Log.v("Top Location", " " + listPosition);
                    hideKeyboard(getCurrentFocus().getWindowToken());

                    Double latitude = Double.valueOf(listOfPlace.get(listPosition).getLatitude());
                    Double longitude = Double.valueOf(listOfPlace.get(listPosition).getLongitude());
                    if (GoogleMaps) {
                        if (placeMarker[0] != null) {
                            placeMarker[0].remove();
                        }
                        placeMarker[0] = addMarkerGoogleMaps(mapView, listOfPlace.get(listPosition).getName(), new LatLng(latitude, longitude));
                    } else {
                        GeoPoint goalLocation = new GeoPoint(latitude, longitude);
                        addMarkerOSM(map, listOfPlace.get(listPosition).getName(), goalLocation);
                    }
                    return true;
                }
                return false;
            }
        });


    }

    private IMapController initializeOSMDroidMaps(Bundle savedInstanceState) {
        OpenStreetMapTileProviderConstants.setUserAgentValue("Razrsword's UMass App V.1");
        map = (MapView) findViewById(R.id.osmmap);
        IMapController mapController = map.getController();
        //map.setMaxZoomLevel(21);
        //ITileSource test = TileSourceFactory.DEFAULT_TILE_SOURCE;
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        //map.setDPIScaleFactor(.5);
        //map.setTileSize(750);
        map.setTilesScaledToDpi(true);
        return mapController;
    }

    private void initializeGoogleMaps(Bundle savedInstanceState) {
        mapView = (com.google.android.gms.maps.MapView) this.findViewById(R.id.googlemap);
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        mapView.setVisibility(View.VISIBLE);
        mapView.onResume();
        googleMap = map;
        //googleMap.setPadding(0,0,0,200);
        //googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        //map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this);

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(42.38955, -72.52817)
                , 15);
        googleMap.moveCamera(cameraUpdate);
        //do this first as it tries to change zoom
        if(globalSavedInstanceState!=null) {
            if (globalSavedInstanceState.getDouble("Latitude") != 0) {
                placeMarker[0] = addMarkerGoogleMaps(mapView, globalSavedInstanceState.getString("Name"),
                        new LatLng(globalSavedInstanceState.getDouble("Latitude"),
                                globalSavedInstanceState.getDouble("Longitude")));
            }
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(globalSavedInstanceState.getDouble("viewLat"),
                    globalSavedInstanceState.getDouble("viewLon"))
                    , globalSavedInstanceState.getFloat("Zoom"));
            googleMap.moveCamera(cameraUpdate);
        }
    }

    private void initializeLocateButtonOSM(final FloatingActionButton locateMyself) {
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
    }


    private void initializeLocateButtonGoogleMaps(final FloatingActionButton locateMyself) {
        locateMyself.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(UMassMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                        (ActivityCompat.checkSelfPermission(UMassMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(UMassMapActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // Enable MyLocation Layer of Google Map
                googleMap.setMyLocationEnabled(true);

                // Get LocationManager object from System Service LOCATION_SERVICE
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                // Create a criteria object to retrieve provider
                Criteria criteria = new Criteria();

                // Get the name of the best provider
                String provider = locationManager.getBestProvider(criteria, true);

                // Get Current Location
                Location myLocation = locationManager.getLastKnownLocation(provider);

                // Get latitude of the current location
                double latitude = myLocation.getLatitude();

                // Get longitude of the current location
                double longitude = myLocation.getLongitude();

                // Create a LatLng object for the current location
                LatLng latLng = new LatLng(latitude, longitude);

                // Zoom in the Google Map
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                /*userLocation = getGPS();
                com.google.android.gms.maps.model.Marker userMarker =
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(userLocation[0], userLocation[1])));
                userMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation[0], userLocation[1])
                        , 14);
                googleMap.animateCamera(cameraUpdate);*/
            }
        });
    }

    private void initializeClearButton(ImageButton clearButton, final AutoCompleteTextView edittext){
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edittext.setText("");
            }
        });
    }

    private void initializeBackButton(ImageButton backButton, final AutoCompleteTextView edittext){
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard(getCurrentFocus().getWindowToken());
                edittext.clearFocus();
            }
        });
    }
    private void hideKeyboard(IBinder window){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(window,
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
                Log.v("GetTopMatch", name + " " + editTextContent);
                if((topValue = editDistance(name, editTextContent))>topPosition){
                    listPosition = i;
                    topPosition = topValue;
                }
            }

        }
        return listPosition;
    }


    public void addMarkerOSM(MapView map, String locationName, GeoPoint goalLocation){
        startMarker = new Marker(map);
        startMarker.setPosition(goalLocation);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(locationName);
        ColorFilter filter = new LightingColorFilter(0x003B5C,0x003B5C);
        Drawable markerImage = getResources().getDrawable(R.drawable.ic_place_black_48dp);
        assert markerImage != null;
        markerImage.setColorFilter(filter);
        startMarker.setIcon(markerImage);
        //map.getController().setCenter(goalLocation);
        map.getOverlays().clear();
        map.getController().setZoom(16);
        map.getOverlays().add(startMarker);
        map.invalidate();
        map.getController().animateTo(goalLocation);
    }
    public com.google.android.gms.maps.model.Marker addMarkerGoogleMaps(com.google.android.gms.maps.MapView map, String locationName, LatLng goalLocation){
        Log.v("AddMarker Google", goalLocation.latitude + " ");
        com.google.android.gms.maps.model.Marker marker = googleMap.addMarker(new MarkerOptions().position(goalLocation).title(locationName));
        marker.showInfoWindow();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(goalLocation, 18);
        googleMap.animateCamera(cameraUpdate);
        return marker;
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
        if(words[words.length-1].isEmpty()){
            words[words.length-1] = "Z";
        }
        for (int w=0; w < words.length; w++) {
            // Find the pairs of characters
            Log.v("wordLetterPairs",words[w] + " String");
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Log.v("onSaveInstanceState", " saving data between instances");
        if(GoogleMaps){
            if (placeMarker[0] != null) {
                savedInstanceState.putDouble("Latitude", placeMarker[0].getPosition().latitude);
                savedInstanceState.putDouble("Longitude", placeMarker[0].getPosition().longitude);
                savedInstanceState.putString("Name",placeMarker[0].getTitle());
            } else {
                savedInstanceState.putDouble("Latitude", 0);
                savedInstanceState.putDouble("Longitude", 0);
            }
            savedInstanceState.putDouble("viewLat", mapView.getMap().getCameraPosition().target.latitude);
            savedInstanceState.putDouble("viewLon", mapView.getMap().getCameraPosition().target.longitude);
            savedInstanceState.putFloat("Zoom", mapView.getMap().getCameraPosition().zoom);
        }else {
            if (this.startMarker != null) {
                savedInstanceState.putDouble("Latitude", UMassMapActivity.this.startMarker.getPosition().getLatitude());
                savedInstanceState.putDouble("Longitude", UMassMapActivity.this.startMarker.getPosition().getLongitude());
            } else {
                savedInstanceState.putDouble("Latitude", 0);
                savedInstanceState.putDouble("Longitude", 0);
            }
            savedInstanceState.putDouble("viewLat", UMassMapActivity.this.map.getMapCenter().getLatitude());
            savedInstanceState.putDouble("viewLon", UMassMapActivity.this.map.getMapCenter().getLongitude());
            savedInstanceState.putFloat("Zoom", UMassMapActivity.this.map.getZoomLevel());
            savedInstanceState.putString("Name","");
        }

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

    protected Object doInBackground(String... test) {
        listOfPlace = getPlaces();
        return null;
    }

}