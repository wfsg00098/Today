package com.eat.today;

/*选菜界面-左边类型ListView的Adapter*/

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TypesAdapter extends ArrayAdapter<String> {
    public TypesAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);

    }
    public boolean flag =false;
    public int selectedPosition = -1;
    @Override

    public View getView(int position, View convertView, ViewGroup parent){
        String str=(String)getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        TextView txt = convertView.findViewById(android.R.id.text1);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(15);
        txt.setSingleLine(true);
        txt.setText(str);
        if(!flag && position==0)
        {
            txt.setTextColor(Color.parseColor("#000000"));
            txt.setBackgroundColor(Color.parseColor("#fdfdfe"));
        }
        else {
            txt.setTextColor(Color.parseColor("#222323"));
            txt.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        return convertView;
    }

}
