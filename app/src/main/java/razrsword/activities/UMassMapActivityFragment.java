package razrsword.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import razrsword.main.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class UMassMapActivityFragment extends Fragment {

    public UMassMapActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_umass_map, container, false);
    }
}
