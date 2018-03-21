package com.example.pokeout.pokeout.Connect;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-02-17.
 */

public class ConnectAdapter extends RecyclerView.Adapter<ConnectViewHolder>{
    private List<ConnectObject> connectObject;
    private Context context;

    public ConnectAdapter(List<ConnectObject> connectObject, Context context){
        this.connectObject = connectObject;
        this.context = context;
    }

    @Override
    public ConnectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connect, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        ConnectViewHolder rcv = new ConnectViewHolder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(ConnectViewHolder holder, int position) {

        holder.mConnectId.setText(connectObject.get(position).getUserId());
        holder.mConnectName.setText(connectObject.get(position).getName());
        if(!connectObject.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(connectObject.get(position).getProfileImageUrl()).into(holder.mConnectImage);
        }

    }
    @Override
    public int getItemCount() {
        return this.connectObject.size();
    }
}
