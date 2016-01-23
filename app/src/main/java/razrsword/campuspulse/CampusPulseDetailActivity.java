package razrsword.campuspulse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        TextView eventLocation = (TextView) findViewById(R.id.event_location);
        TextView eventDate = (TextView) findViewById(R.id.event_date);
        NestedScrollView contentFrame = (NestedScrollView) findViewById(R.id.content_frame);

        setSupportActionBar(toolbar);
        final CampusPulseStackOverflowXmlParser.Entry entry = getIntent().getExtras().getParcelable("eventObject");


        final int vibrantColor = getIntent().getExtras().getInt("vibrantcolor");
        final int lightColor = getIntent().getExtras().getInt("lightcolor");
        //contentFrame.setBackgroundColor(vibrantColor);
        ImageView locationImage = (ImageView) findViewById(R.id.event_location_image);
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout toolbarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(entry.getTitle());
        toolbarLayout.setExpandedTitleTextAppearance(R.style.toolbar_text);
        DateFormat dayFormat = new SimpleDateFormat("D", Locale.US);
        DateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm a", Locale.US);
        String dateEnd = null;
        if(dayFormat.format(entry.getDateStart()).compareTo(dayFormat.format(entry.getDateEnd())) == 0){
            dateEnd = new SimpleDateFormat("h:mm a", Locale.US).format(entry.getDateEnd());
        } else {
            dateEnd = formatter.format(entry.getDateEnd());
        }
        eventDate.setText(formatter.format(entry.getDateStart()) + " - " + dateEnd);
        eventLocation.setText(entry.getEventLocation());
        //locationImage.setBackgroundResource(R.color.colorPrimary);
        //locationImage.setColorFilter(Color.argb(120, 136, 28, 28));
        TextView eventDescription = (TextView) findViewById(R.id.event_description);
        eventDescription.setText(entry.getDescription());
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
            if(entry.getImageLink() != null) {
                locationImage.setImageBitmap(fastblur(bitmap,1,10));
                //locationImage.setColorFilter(changeAlpha(vibrantColor, 50));
            } else {
                locationImage.setImageBitmap(fastblur(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.ic_event_white_48dp), 1, 10));
                locationImage.setBackgroundResource(R.color.colorPrimary);
                locationImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

            }
        }
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addToCalendar(entry.getTitle(), entry.getDateStart(), entry.getDateEnd(), entry.getDescription(), entry.getEventLocation());
            }
        });

    }

    public String dateToHHmm(String dateInput){
        if(!dateInput.contains(",")){
            DateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy h:mm a", Locale.US);
            Date date = null;
            try {
                date = (Date)formatter.parse(dateInput);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formatter = new SimpleDateFormat("MM dd, yyyy ");
            dateInput = formatter.format(date);
        }
        return dateInput;
    }

    public void addToCalendar(String title, long beginTime, long endTime, String description, String location){
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", beginTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("eventLocation", location);
        startActivity(intent);
    }

    /*public long fixTimeFormat(long inputTime){
        DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.US);
        return timestamp;
    }*/

    /**
     * Stack Blur v1.0 from
     * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
     * Java Author: Mario Klingemann <mario at quasimondo.com>
     * http://incubator.quasimondo.com
     *
     * created Feburary 29, 2004
     * Android port : Yahel Bouaziz <yahel at kayenko.com>
     * http://www.kayenko.com
     * ported april 5th, 2012
     *
     * This is a compromise between Gaussian Blur and Box blur
     * It creates much better looking blurs than Box Blur, but is
     * 7x faster than my Gaussian Blur implementation.
     *
     * I called it Stack Blur because this describes best how this
     * filter works internally: it creates a kind of moving stack
     * of colors whilst scanning through the image. Thereby it
     * just has to add one new block of color to the right side
     * of the stack and remove the leftmost color. The remaining
     * colors on the topmost layer of the stack are either added on
     * or reduced by one, depending on if they are on the right or
     * on the left side of the stack.
     *
     * If you are using this algorithm in your code please add
     * the following line:
     * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
     */

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

}

