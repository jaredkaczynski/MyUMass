package razrsword.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import razrsword.main.R;

public class UMassMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umass_map);
        OpenStreetMapTileProviderConstants.setUserAgentValue("Razrsword's UMass App V.05");
        MapView map = (MapView) findViewById(R.id.map);

        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(14);

        map.setTilesScaledToDpi(true);
        GeoPoint startPoint = new GeoPoint(42.38955,-72.52817);
        mapController.setCenter(startPoint);

        Toast toast = Toast.makeText(this, OpenStreetMapTileProviderConstants.getUserAgentValue(), Toast.LENGTH_LONG);
        toast.show();
    }
}
