package razrsword.dining;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import razrsword.main.R;
import razrsword.mapping.UMassMapActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiningViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiningViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param2";

    RecyclerView rv;
    List<DiningLocation> locationNameList = null;



    private OnFragmentInteractionListener mListener;

    public DiningViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiningViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiningViewFragment newInstance(String param1, String param2, List<DiningLocation> param3) {
        DiningViewFragment fragment = new DiningViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelableArrayList(ARG_PARAM3, (ArrayList<? extends Parcelable>) param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            locationNameList = getArguments().getParcelableArrayList(ARG_PARAM3);
        }


        final DiningViewAdapter adapter = new DiningViewAdapter(locationNameList);

        //adapter.diningLocations.add(new DiningLocation("Worcester"));
        //adapter.diningLocations.add(new DiningLocation("Berkshire"));
        // Inflate the layout for this fragment
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        View v = inflater.inflate(R.layout.fragment_dining_commons, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerview_dining);
        assert rv != null;
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(llm);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //// TODO: 23-Dec-15 add activity swap that's pretty

                        switch (position){
                            case 0:
                                animateIntent(view,rv.findViewHolderForAdapterPosition(position).itemView,DetailedDiningInformation.class,position);
                                break;
                            case 1:
                                animateIntent(view,rv.findViewHolderForAdapterPosition(position).itemView,DetailedDiningInformation.class,position);
                                break;
                            case 2:
                                animateIntent(view,rv.findViewHolderForAdapterPosition(position).itemView,DetailedDiningInformation.class,position);
                                break;
                        }
                    }
                })
        );
        //setupRecyclerView(rv);
        return v;
    }


    public void animateIntent(View view,View sourceView, Class<?> cls,int position) {

        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(getContext(), cls);
        intent.putExtra("locationName", locationNameList.get(position).locationName);

        // Get the transition name from the string
        String transitionName = getString(R.string.transition_string);

        // Define the view that the animation will start from
        View viewStart = sourceView;

        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        viewStart,   // Starting view
                        transitionName    // The String
                );
        //Start the Intent
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnDiningCommonInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnDiningCommonInteractionListener(Uri uri);
    }

}
