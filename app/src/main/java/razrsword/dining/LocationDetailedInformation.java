package razrsword.dining;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;

import razrsword.main.R;


public class LocationDetailedInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }
        //overridePendingTransition(R.anim.enter_slide_in, R.anim.enter_slide_out);
        setContentView(R.layout.activity_location_detailed_information);
    }
}
