package razrsword.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import razrsword.ExtendedItemAnimator;
import razrsword.activities.BusTrackerActivity;
import razrsword.campuspulse.CampusPulseActivity;
import razrsword.dining.DiningActivity;
import razrsword.dining.RecyclerItemClickListener;
import razrsword.mapping.UMassMapActivity;

public class NewMainActivity extends AppCompatActivity {
    List<MainCard> locationNameList = null;
    RecyclerView rv;
    MainViewAdapter adapter;
    String campusXmlUrl = "https://umassamherst.collegiatelink.net/EventRss/EventsRss";
    String campusXmlLocalDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CampusPulseXML.xml";
    Context context;
    final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    List<Class<?>> locationClassList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_new_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                0,
                0
        )

        {
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //Set the custom toolbar
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle.syncState();

        //toolbar.setNavigationIcon(R.drawable.ic_menu_share);
        List<MainCard> locationNameList;
        locationNameList = new ArrayList<>();
        locationClassList = new ArrayList<>();
        locationClassList.add(UMassMapActivity.class);
        locationClassList.add(CampusPulseActivity.class);
        locationClassList.add(DiningActivity.class);
        locationClassList.add(BusTrackerActivity.class);

        locationNameList.add(new MainCard("UMass Map",R.drawable.berkshire));
        locationNameList.add(new MainCard("UMass Bus", R.drawable.berkshire));
        locationNameList.add(new MainCard("Dining", R.drawable.berkshire));
        locationNameList.add(new MainCard("Campus Events", R.drawable.berkshire));
        locationNameList.add(new MainCard("DeleteXML", R.drawable.berkshire));
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
        checkStoragePermission();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void checkStoragePermission(){
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                final String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                Snackbar.make(NewMainActivity.this.rv, "Storage access is required for events and dining.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat
                                        .requestPermissions(NewMainActivity.this, permissions,
                                                MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                            }
                        })
                        .show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            checkForUpdateData();
        }
    }
    /*
    private List<MainCard> getGridOrder(){
        SharedPreferences prefs=this.getSharedPreferences("mainactivityprefs", Context.MODE_PRIVATE);
        Set<MainCard> set = prefs.getStringSet("gridlist", null);
        return null;
    }

    private void setGridOrder(List<MainCard> locationNameList){
        SharedPreferences prefs=this.getSharedPreferences("mainactivityprefs", Context.MODE_PRIVATE);
        //Set the values
        Set<MainCard> set = new HashSet<MainCard>();
        set.addAll(locationNameList);
        prefs.putStringSet("gridlist", set);
        scoreEditor.commit();
        String value = prefs.getString("list", null);

    }*/

    private void checkForUpdateData(){
        File file = new File(campusXmlLocalDirectory);
        //Toast.makeText(context, "Deleted " + file.exists(), Toast.LENGTH_LONG).show();
        // runs the downloader if the xml is over 24 hours old
        if(!file.exists() || file.lastModified() == 0 || file.lastModified() + 86400000 < System.currentTimeMillis()) {
            if(haveNetworkConnection()) {
                //Toast.makeText(context, " downloading ", Toast.LENGTH_LONG).show();
                // Here, thisActivity is the current activity
                DownloadTask downloadTask = new DownloadTask(context);
                downloadTask.execute(campusXmlUrl);
            }
        }
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
    // usually, subclasses of AsyncTask are declared inside the activity class.
    // that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(campusXmlLocalDirectory);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            //mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            //mProgressDialog.setIndeterminate(false);
            //mProgressDialog.setMax(100);
            //mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            //mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"Data Downloaded", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkForUpdateData();

                } else {

                    Toast.makeText(context,"This app won't work right. :(", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
