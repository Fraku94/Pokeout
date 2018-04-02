package com.example.pokeout.pokeout.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.pokeout.pokeout.Fragments.Best.BestFragment;
import com.example.pokeout.pokeout.Fragments.Liked.LikedFragment;
import com.example.pokeout.pokeout.Fragments.Suggest.SuggestFragment;
import com.example.pokeout.pokeout.R;


public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public SampleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // Przypisanie tab do odpowiednich fragmwentow
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new LikedFragment();


        } else if (position == 1){
            return new BestFragment();
        } else {
            return new SuggestFragment();
        }
//        else {
//            return new BlankFragment();
//        }
    }
    @Override
    public int getItemPosition(Object object) {
// POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

//    public int getItemPosition(Object object) {
//        GeoHashQuery.Utils.DummyItem dummyItem = (GeoHashQuery.Utils.DummyItem) ((View) object).getTag();
//        int position = mDummyItems.indexOf(dummyItem);
//        if (position >= 0) {
//            // The current data matches the data in this active fragment, so let it be as it is.
//            return position;
//        } else {
//            // Returning POSITION_NONE means the current data does not matches the data this fragment is showing right now.  Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and show new data.
//            return POSITION_NONE;
//        }
//    }
    // Liczba wyswietlanych tab
    @Override
    public int getCount() {
        return 3;
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
//            case 3:
//                return mContext.getString(R.string.categoryBlank);
            default:
                return null;
        }
    }

}