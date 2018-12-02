package com.eat.today;

/*选菜界面-左边类型ListView的Adapter*/

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cosmos on 2018/10/5.
 */

public class TypesAdapter extends ArrayAdapter<String> {
    public TypesAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
    }
    MyListener listener = new MyListener();
    @Override

    public View getView(int position, View convertView, ViewGroup parent){
        String str=(String)getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        }
        TextView txt = convertView.findViewById(android.R.id.text1);
        txt.setText(str);
        txt.setTextColor(Color.parseColor("#222323"));
        txt.setBackgroundColor(Color.parseColor("#f1f1f1"));
        txt.setOnClickListener(listener);
        return convertView;
    }


    private class MyListener implements View.OnClickListener{
        TextView lastTv = null;
        @Override
        public void onClick(View v){
            TextView txt = (TextView) v;
            if (lastTv!=null){
                lastTv.setTextColor(Color.parseColor("#222323"));
                lastTv.setBackgroundColor(Color.parseColor("#f1f1f1"));
            }
            txt.setTextColor(Color.parseColor("#000000"));
            txt.setBackgroundColor(Color.parseColor("#fdfdfe"));
            lastTv = txt;
        }
    }
}
