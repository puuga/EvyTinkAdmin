package com.appspace.evytinkadmin.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appspace.evytinkadmin.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FirstActivityFragment extends Fragment {

    public FirstActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        initInstances(view);

        return view;
    }

    private void initInstances(View view) {
    }
}
