package com.example.pokeout.pokeout.Search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by simco on 1/24/2018.
 */

public class SearchViewHolders extends RecyclerView.ViewHolder{
    public TextView mEmail;
    public Button mFollow;

    public SearchViewHolders(View itemView){
        super(itemView);
        mEmail = itemView.findViewById(R.id.email);
        mFollow = itemView.findViewById(R.id.follow);

    }


}
