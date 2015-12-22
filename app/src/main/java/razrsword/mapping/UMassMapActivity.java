package razrsword.mapping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import razrsword.main.R;

public class UMassMapActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Context context;
    private ProgressDialog pDialog;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_umass_map);
        OpenStreetMapTileProviderConstants.setUserAgentValue("Razrsword's UMass App V.05");
        final MapView map = (MapView) findViewById(R.id.map);

        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(14);

        map.setTilesScaledToDpi(true);

        final GeoPoint startPoint = new GeoPoint(42.38955, -72.52817);
        mapController.setCenter(startPoint);


        final ImageButton button = (ImageButton) findViewById(R.id.imageButton);
        final EditText edittext = (EditText) findViewById(R.id.autoCompleteTextView);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    //String oUrl = poiProvider.urlForPOISearch("brown", map.getBoundingBox(), 100, 10);
                    //ArrayList<POI> pois = poiProvider.getPOIsFromUrl(oUrl);
                    /*AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute(startPoint.toString(),edittext.getText().toString());
                    ArrayList<POI> pois = runner.pois;
                    if (pois != null) {
                        Toast toast = Toast.makeText(UMassMapActivity.this.getContext(), pois.get(0).mDescription, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    */
                    File file = new File("assets/locations.xml");
                    InputStream stream = null;
                    try {
                        stream = context.getAssets().open("locations.xml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(stream));




                    /*Marker startMarker = new Marker(map);
                    startMarker.setPosition(GeoPoint.fromIntString(edittext.getText().toString()));
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(startMarker);*/

                    Toast toast = null;
                    try {
                        toast = Toast.makeText(UMassMapActivity.this, reader.readLine(), Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    toast.show();
                    return true;
                }
                return false;
            }
        });





        /*Toast toast = Toast.makeText(this, OpenStreetMapTileProviderConstants.getUserAgentValue(), Toast.LENGTH_LONG);
        toast.show();*/
        //Button button =
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    public Context getContext(){
        return context;
    }


}
