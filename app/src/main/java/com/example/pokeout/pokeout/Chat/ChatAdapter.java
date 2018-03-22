package com.example.pokeout.pokeout.Chat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-02-17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{
    private List<ChatObject> chatList;
    private Context context;


    public ChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());
        if(chatList.get(position).getCurrentUser()){

            holder.mContainer.setGravity(Gravity.RIGHT);
            holder.mMessage.setBackgroundResource(R.drawable.rounded_rectangle_green);
        }else{
            holder.mContainer.setGravity(Gravity.LEFT);

            holder.mMessage.setBackgroundResource(R.drawable.rounded_rectangle_orange);;
        }

    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}