package com.eat.today;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MainActivity extends Activity {

    @SuppressLint("HandlerLeak")
    private Handler LoginResult = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TextView tv = findViewById(R.id.textView10);
            if (msg.what == 1) {
                tv.setText(String.format("欢迎你，%s", GlobalSettings.nickname));
            } else {
                tv.setText("未登录");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalSettings.LoadSettings(getApplicationContext().getFilesDir().getPath());


        class LoginThread extends Thread {
            @Override
            public void run() {
                if (GlobalSettings.Login()) LoginResult.sendEmptyMessage(1);
                else LoginResult.sendEmptyMessage(0);
            }
        }

        if (GlobalSettings.isLogged) new LoginThread().start();

        findViewById(R.id.button2).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Login.class)));
        findViewById(R.id.button4).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Choices.class)));
        findViewById(R.id.button5).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Cateen.class)));
        findViewById(R.id.button6).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Calorie.class)));
        findViewById(R.id.button7).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Recommend.class)));
    }
}
