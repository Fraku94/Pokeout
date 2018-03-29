package com.example.pokeout.pokeout.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pokeout.pokeout.R;

/**
 * Created by Z710 on 2018-02-17.
 */
//Wyglad tu wszysto z Matches activity buttony textView itp.
public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView mMessage, mTime;
    public LinearLayout mContainer;
    public ChatViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessage = itemView.findViewById(R.id.message);
        mTime = itemView.findViewById(R.id.time);

        mContainer = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view) {
    }
}
