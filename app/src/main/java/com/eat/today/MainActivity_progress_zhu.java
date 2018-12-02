package com.eat.today;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity_progress_zhu extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_progress);

        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        String years = String.valueOf(year);
        String months = String.valueOf(month);
        String dates = String.valueOf(date);
        String hours = String.valueOf(hour);
        String minutes = String.valueOf(minute);
        String seconds = String.valueOf(second);
        String timor = years+"年"+months+"月"+dates+"日"+" "+hours+":"+minutes+":"+seconds;
        TextView timor_ = (TextView)findViewById(R.id.timor);
        timor_.setText(timor);

        Button campus1 = (Button)findViewById(R.id.campus1);
        campus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity_progress_zhu.this,activity_campus1.class);
                Bundle bundle1 = new Bundle();
                //可以在这里对bundle1添加要传到campus1的数据
                intent1.putExtras(bundle1);
                startActivity(intent1);
            }
        });

        Button campus2 = (Button)findViewById(R.id.campus4);
        campus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity_progress_zhu.this,activity_campus2.class);
                Bundle bundle2 = new Bundle();
                //可以在这里对bundle1添加要传到campus2的数据
                intent2.putExtras(bundle2);
                startActivity(intent2);
            }
        });
        Button campus3 = (Button)findViewById(R.id.button3);
        campus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity_progress_zhu.this,activity_campus3.class);
                Bundle bundle3 = new Bundle();
                //可以在这里对bundle1添加要传到campus3的数据
                intent3.putExtras(bundle3);
                startActivity(intent3);
            }
        });
    }
}

