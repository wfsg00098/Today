package com.eat.today;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Activity {

    @SuppressLint("HandlerLeak")
    private Handler LoginResult = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_LONG + 5).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "登录失败！", Toast.LENGTH_LONG + 5).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("登录");


        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);

        String userregex = "^[A-Za-z0-9]+$";
        String passregex = "^[A-Za-z0-9!@#$%^&*()\\[\\]\\\\-_=+{}|:\'\"<,>./?]+$";


        login.setOnClickListener(v -> {
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

            GlobalSettings.username = username.getText().toString();
            GlobalSettings.password = GlobalSettings.sha1(password.getText().toString());


            class LoginThread extends Thread {
                @Override
                public void run() {
                    if (GlobalSettings.Login()) LoginResult.sendEmptyMessage(1);
                    else LoginResult.sendEmptyMessage(0);
                }
            }
            new LoginThread().start();

        });

        register.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));

    }
}
