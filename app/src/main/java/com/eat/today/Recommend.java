package com.eat.today;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Recommend extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        setTitle("智能推荐");
        Toast.makeText(this, "网络无连接，无法推荐", Toast.LENGTH_LONG + 5).show();
    }
}
