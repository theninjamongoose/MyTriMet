package com.example.tank.mytrimetpro.base;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tank.mytrimetpro.R;
import com.example.tank.mytrimetpro.base.DrawerCallback;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

/**
 * Created by tank on 8/29/16.
 */
public class CustomPlaceAutocompleteFragment extends PlaceAutocompleteFragment {

    private DrawerLayout mDrawerLayout;
    private EditText mSearchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mSearchBar = (EditText) view.findViewById(
                com.google.android.gms.R.id.place_autocomplete_search_input);
        ImageView searchBtn = (ImageView) view.findViewById(com.google.android.gms.R.id.place_autocomplete_search_button);
        searchBtn.setImageResource(R.drawable.ic_menu_black_24dp);
        mDrawerLayout = ((DrawerCallback) getActivity()).getDrawerLayout();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        return view;
    }

    @Override
    public void setText(CharSequence var1) {
        mSearchBar.setText(var1);
    }}
