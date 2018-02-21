package com.example.pokeout.pokeout.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pokeout.pokeout.Fragments.BestFragment;
import com.example.pokeout.pokeout.Fragments.BlankFragment;
import com.example.pokeout.pokeout.Fragments.LikedFragment;
import com.example.pokeout.pokeout.Fragments.SuggestFragment;
import com.example.pokeout.pokeout.R;

/**
 * Created by bartl on 20.02.2018.
 */

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SampleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // Przypisanie tab do odpowiednich fragmwentow
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new BestFragment();
        } else if (position == 1){
            return new LikedFragment();
        } else if (position == 2){
            return new SuggestFragment();
        } else {
            return new BlankFragment();
        }
    }

    // Liczba wyswietlanych tab
    @Override
    public int getCount() {
        return 4;
    }

    // Przypisanie nazw do tablayot tam na góre
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.categoryLiked);
            case 1:
                return mContext.getString(R.string.categoryBest);
            case 2:
                return mContext.getString(R.string.categorySuggest);
            case 3:
                return mContext.getString(R.string.categoryBlank);
            default:
                return null;
        }
    }

}