package com.example.pokeout.pokeout.UserDescryption;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-02-17.
 */

public class UserDescryptionAdapter extends RecyclerView.Adapter<UserDescryptionViewHolder>{
    private List<UserDescryptionObject> userDescryptionObject;
    private Context context;

    public UserDescryptionAdapter(List<UserDescryptionObject> userDescryptionObject, Context context){
        this.userDescryptionObject = userDescryptionObject;
        this.context = context;
    }

    @Override
    public UserDescryptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connect, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        UserDescryptionViewHolder rcv = new UserDescryptionViewHolder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(UserDescryptionViewHolder holder, int position) {

        holder.mConnectId.setText(userDescryptionObject.get(position).getUserId());
        holder.mConnectName.setText(userDescryptionObject.get(position).getName());
        if(!userDescryptionObject.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(userDescryptionObject.get(position).getProfileImageUrl()).into(holder.mConnectImage);
        }

    }
    @Override
    public int getItemCount() {
        return this.userDescryptionObject.size();
    }
}
