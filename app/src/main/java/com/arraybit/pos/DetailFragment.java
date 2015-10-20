package com.arraybit.pos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class DetailFragment extends Fragment {

    public DetailFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_detail, container, false);

        Toolbar app_bar=(Toolbar)view.findViewById(R.id.app_bar);

        if(app_bar!=null)
        {
            ((AppCompatActivity)getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_detail));
        app_bar.setLogo(R.mipmap.app_logo);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
