package org.sixpetals.zinrow.stopwatch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SubDisplayFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SubDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View subView =  inflater.inflate(R.layout.fragment_subdisplay, container, false);

        return subView;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
