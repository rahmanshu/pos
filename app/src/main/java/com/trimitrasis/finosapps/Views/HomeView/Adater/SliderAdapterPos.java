package com.trimitrasis.finosapps.Views.HomeView.Adater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.trimitrasis.finosapps.Views.HomeView.ContentSliderAdapter;

import java.util.ArrayList;

/**
 * Created by rahman on 03/05/2017.
 */

public class SliderAdapterPos extends FragmentPagerAdapter {

    ArrayList<ContentSliderAdapter> contentSliderFragments = new ArrayList<>();

    public SliderAdapterPos(FragmentManager fm, ArrayList<ContentSliderAdapter> contentSliderFragments) {
        super(fm);
        this.contentSliderFragments = contentSliderFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return contentSliderFragments.get(position);
    }

    @Override
    public int getCount() {
        return contentSliderFragments.size();
    }
}
