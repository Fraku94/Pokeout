package com.example.pokeout.pokeout.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by simco on 1/24/2018.
 */

public class SearchAdapater extends RecyclerView.Adapter<SearchViewHolders>{

    private List<SearchObject> usersList;
    private Context context;

    public SearchAdapater(List<SearchObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }
    @Override
    public SearchViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_search_item, null);
        SearchViewHolders rcv = new SearchViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final SearchViewHolders holder, int position) {
        holder.mEmail.setText(usersList.get(position).getEmail());


    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}
