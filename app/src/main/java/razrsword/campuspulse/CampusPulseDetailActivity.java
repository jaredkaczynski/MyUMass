package razrsword.campuspulse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;

import razrsword.main.R;

public class CampusPulseDetailActivity extends AppCompatActivity {

    ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
    //Config for the image caching library i'm using
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.ic_event_white_48dp)// resource or drawable
            .cacheInMemory(false) // default
            .cacheOnDisk(false).showImageForEmptyUri(R.drawable.ic_event_white_48dp)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_pulse_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //final ImageView locationImage = (ImageView) findViewById(R.id.toolbar_layout);
        //final ImageView gradient = mainButtonHolder.gradientImage;
        CampusPulseStackOverflowXmlParser.Entry entry = getIntent().getExtras().getParcelable("eventObject");
        final int vibrantColor = getIntent().getExtras().getInt("entry");
        ImageView locationImage = (ImageView) findViewById(R.id.event_location_image);
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout toolbarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(entry.getTitle());

        //locationImage.setBackgroundResource(R.color.colorPrimary);
        //locationImage.setColorFilter(Color.argb(120, 136, 28, 28));
        TextView eventDescription = (TextView) findViewById(R.id.event_description);
        eventDescription.setText(entry.description);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        Bitmap bitmap = null;
        try {
             bitmap = BitmapFactory.decodeStream(this.getApplicationContext()
                    .openFileInput("temppulseimage"));//here context can be anything like getActivity() for fragment, this or MainActivity.this
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(bitmap != null){
            if(entry.imageLink != null) {
                locationImage.setImageBitmap(bitmap);
                locationImage.setColorFilter(changeAlpha(vibrantColor, 50));
            } else {
                locationImage.setColorFilter(Color.argb(120, 136, 28, 50));
            }
        } else {
            // Load image, decode it to Bitmap and return Bitmap to callback
            ImageSize targetSize = new ImageSize(500, 250); // result Bitmap will be fit to this size
            //imageLoader.displayImage(campusPulseCards.get(i).eventImageURL, temp);
            imageLoader.displayImage(entry.imageLink, locationImage, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    final ImageView temp2 = (ImageView) view;

                    //final Palette palette = Palette.from(loadedImage).generate();
                    //if (loadedImage != null) {
                        //temp2.setBackgroundResource(R.color.colorPrimary);
                        temp2.setImageBitmap(loadedImage);
                        temp2.setColorFilter(changeAlpha(vibrantColor, 50));

                    //}


                    Log.v("Set image", "Set image to event");
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        */
                }
            });
        }
        locationImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }
    int changeAlpha(int origColor, int userInputedAlpha) {
        origColor = origColor & 0x00ffffff; //drop the previous alpha value
        return (userInputedAlpha << 24) | origColor; //add the one the user inputted
    }

}

