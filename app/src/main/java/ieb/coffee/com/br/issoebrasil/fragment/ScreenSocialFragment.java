package ieb.coffee.com.br.issoebrasil.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ieb.coffee.com.br.issoebrasil.R;
import ieb.coffee.com.br.issoebrasil.activity.DrawerActivity;
import ieb.coffee.com.br.issoebrasil.activity.MapsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenSocialFragment extends Fragment {


    public ScreenSocialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen_social, container, false);
    }

}
