package com.eat.today;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

/**
 * Created by cosmos on 2018/10/21.
 */

public class DishAdapter2 extends RecyclerView.Adapter<DishAdapter2.ViewHolder> {
    private List<Dish> mDishList;
    Handler handler;
    public static final int COUNT_CHANGED = 1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //View dishView;
        mImageView dishImage;
        TextView dishName;
        TextView dishCount;
        TextView dishPrice;
        TextView dishCal;
        TextView dishLiked;
        Button countAdd;
        Button countDel;

        public ViewHolder(View view) {
            super(view);
            //dishView=view;
            dishImage = view.findViewById(R.id.dish_img);
            dishName = view.findViewById(R.id.dish_name);
            dishPrice = view.findViewById(R.id.dish_price);
            dishCount = view.findViewById(R.id.txt_count);
            countAdd = view.findViewById(R.id.btn_add);
            countDel = view.findViewById(R.id.btn_del);
            dishCal = view.findViewById(R.id.choiceCal);
            dishLiked = view.findViewById(R.id.choiceLiked);
        }
    }

    public DishAdapter2(List<Dish> dishList) {
        mDishList = dishList;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.for_choices, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.countAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Dish dish = mDishList.get(position);
                dish.addCount();
                notifyDataSetChanged();
                Message message = new Message();
                message.what = COUNT_CHANGED;
                handler.sendMessage(message);
            }
        });
        holder.countDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Dish dish = mDishList.get(position);
                int count = dish.getCount();
                if (count == 0) {
                    Toast.makeText(v.getContext(), "不能再减少了哦", Toast.LENGTH_SHORT).show();
                } else {
                    dish.delCount();
                    notifyDataSetChanged();
                    Message message = new Message();
                    message.what = COUNT_CHANGED;
                    handler.sendMessage(message);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Dish dish = mDishList.get(position);
        holder.dishImage.setImageURL(dish.getImgUrl());
        holder.dishCount.setText("" + dish.getCount());
        holder.dishPrice.setText("¥" + dish.getPrice());
        holder.dishCal.setText("热量 "+dish.getCalorie()+"Cal");
        holder.dishLiked.setText("赞 "+dish.getLiked());
        holder.dishName.setText(dish.getName());

    }

    @Override
    public int getItemCount() {
        return mDishList.size();
    }
}

