package ieb.coffee.com.br.issoebrasil.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ieb.coffee.com.br.issoebrasil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenEconomicoFragment extends Fragment {


    public ScreenEconomicoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_screen_economico, container, false);
    }

}
