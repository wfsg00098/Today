package com.eat.today;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {

    @SuppressLint("HandlerLeak")
    private Handler RegResult = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_LONG + 5).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "注册失败！", Toast.LENGTH_LONG + 5).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("注册");

        TextView username = findViewById(R.id.username2);
        TextView password = findViewById(R.id.password2);
        TextView nickname = findViewById(R.id.nickname2);

        Button register = findViewById(R.id.register2);

        String userregex = "^[A-Za-z0-9]+$";
        String passregex = "^[A-Za-z0-9!@#$%^&*()\\[\\]\\\\-_=+{}|:\'\"<,>./?]+$";
        String nickregex = "^[\u4e00-\u9fa5A-Za-z0-9!@#$%^&*()\\[\\]\\\\-_=+{}|:\'\"<,>./?]+$";


        register.setOnClickListener(view -> {
            Pattern pattern = Pattern.compile(userregex);
            Matcher matcher = pattern.matcher(username.getText());
            if (!matcher.matches()) {
                Toast.makeText(getApplicationContext(), "用户名中只能使用大小写字母以及数字！", Toast.LENGTH_LONG + 5).show();
                return;
            }

            pattern = Pattern.compile(passregex);
            matcher = pattern.matcher(password.getText());
            if (!matcher.matches()) {
                Toast.makeText(getApplicationContext(), "密码中只能使用大小写字母、数字以及特殊符号！", Toast.LENGTH_LONG + 5).show();
                return;
            }

            pattern = Pattern.compile(nickregex);
            matcher = pattern.matcher(nickname.getText());
            if (!matcher.matches()) {
                Toast.makeText(getApplicationContext(), "昵称中只能使用中文、大小写字母、数字以及特殊符号！", Toast.LENGTH_LONG + 5).show();
                return;
            }

            GlobalSettings.username = username.getText().toString();
            GlobalSettings.password = GlobalSettings.sha1(password.getText().toString());
            GlobalSettings.nickname = nickname.getText().toString();

            class RegThread extends Thread {
                @Override
                public void run() {
                    if (GlobalSettings.Register()) RegResult.sendEmptyMessage(1);
                    else RegResult.sendEmptyMessage(0);
                }
            }
            new RegThread().start();
        });

    }
}
