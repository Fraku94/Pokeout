package com.example.pokeout.pokeout.Liked;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-02-22.
 */

public class LikedAdapter extends RecyclerView.Adapter<LikedViewHolder> {

    private List<LikedObject> likedList;

    private Context context;

    public LikedAdapter(List<LikedObject> likedList, Context context){
        this.likedList = likedList;
        this.context = context;
    }

    @Override
    public LikedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked, null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        LikedViewHolder rcv = new LikedViewHolder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(LikedViewHolder holder, int position) {

        holder.mCategoryId.setText(likedList.get(position).getCategoryId());
        holder.mCategoryName.setText(likedList.get(position).getName());
        if(!likedList.get(position).getCategoryImageUrl().equals("default")){
            Glide.with(context).load(likedList.get(position).getCategoryImageUrl()).into(holder.mCategoryImage);
        }

    }

    @Override
    public int getItemCount() {
        return this.likedList.size();
    }
}
