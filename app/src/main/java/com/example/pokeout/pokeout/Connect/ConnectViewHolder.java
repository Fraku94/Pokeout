package com.example.pokeout.pokeout.Connect;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokeout.pokeout.Chat.ChatActivity;
import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-17.
 */
//Wyglad tu wszysto z Matches activity buttony textView itp.
public class ConnectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView mConnectId,mConnectName;
    public ImageView mConnectImage;


    public ConnectViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mConnectId = (TextView)itemView.findViewById(R.id.Connectid);
        mConnectName = (TextView)itemView.findViewById(R.id.ConnectName);
        mConnectImage = (ImageView)itemView.findViewById(R.id.ConnectImage);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("ConnectId", mConnectId.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }

}
