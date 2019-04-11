package com.eat.today;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CanteenDishAdapter extends ArrayAdapter<Dish> {
    private int resourceId;
    private List<Dish> mlist=new ArrayList<>();

    public CanteenDishAdapter(Context context, int textViewResourceId, List<Dish> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        mlist=objects;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent)
    {
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        if(converView == null) {
            Dish dish = getItem(position);
            mImageView imageView = view.findViewById(R.id.dish_img_conf);
            TextView dish_name = view.findViewById(R.id.dish_name_conf);
            TextView dish_price = view.findViewById(R.id.dish_price_conf);
            TextView dish_count = view.findViewById(R.id.txt_count_conf);
            imageView.setVisibility(View.GONE);
            dish_name.setText(dish.getName());
            dish_price.setText("¥" + dish.getPrice()*dish.getCount());
            dish_price.setTextColor(Color.parseColor("#d61919"));
            dish_count.setText("×" + dish.getCount());
            dish_count.setTextColor(Color.parseColor("#d61919"));
        }
        return view;
    }
}
