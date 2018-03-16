package com.example.pokeout.pokeout.UserInCategory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokeout.pokeout.R;

import java.util.List;

/**
 * Created by Z710 on 2018-03-15.
 */

public class UserInCategoryAdapter extends RecyclerView.Adapter<UserInCategoryViewHolder>{
    private Context context;
    private List<UserInCategoryObject> UserInCategoryList;


    public UserInCategoryAdapter(Context context, List<UserInCategoryObject> UserInCategoryList) {
        this.context = context;
        this.UserInCategoryList = UserInCategoryList;
    }

    @Override
    public UserInCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//Przypisanie wygladu okna do adaptera
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_in_category, null,false);

        //Ustawienie RecycleView
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        //Podpienie i zwrocenie wygladu
        UserInCategoryViewHolder rcv = new UserInCategoryViewHolder((layoutView));

        return rcv;
    }

    @Override
    public void onBindViewHolder(UserInCategoryViewHolder holder, int position) {
        holder.name.setText(UserInCategoryList.get(position).getName());
        holder.description.setText(UserInCategoryList.get(position).getDescryption());
        holder.price.setText(UserInCategoryList.get(position).getSex());



//        Glide.with(context)
//                .load(item.getImageUrl())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return UserInCategoryList.size();
    }

    public void removeItem(int position) {
        UserInCategoryList.remove(position);

        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }


    public void restoreItem(UserInCategoryObject item, int position) {
        UserInCategoryList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

}