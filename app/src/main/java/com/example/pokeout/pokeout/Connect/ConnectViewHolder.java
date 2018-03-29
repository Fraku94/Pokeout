package com.example.pokeout.pokeout.Connect;



import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;
import com.sackcentury.shinebuttonlib.ShineButton;

/**
 * Created by Z710 on 2018-02-17.
 */
//Wyglad tu wszysto z Matches activity buttony textView itp.
public class ConnectViewHolder extends RecyclerView.ViewHolder {
    public String mConnectId;
    public  TextView mName;
    public ImageView mImage;

    public RelativeLayout mGointo;
    public ShineButton mDelete;


    public ConnectViewHolder(View itemView){
        super(itemView);


        mName = (TextView)itemView.findViewById(R.id.Name);
        mImage = (ImageView)itemView.findViewById(R.id.Image);
        mDelete = (ShineButton) itemView.findViewById(R.id.Delete);
        mGointo = (RelativeLayout) itemView.findViewById(R.id.Gointo);


    }



}
