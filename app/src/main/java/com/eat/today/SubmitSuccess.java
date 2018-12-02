package com.eat.today;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubmitSuccess extends AppCompatActivity {

    private String order_id;
    private TextView txt_orderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_success);
        final Intent intent=getIntent();
        order_id=intent.getStringExtra("order_id");

        Button btn_back = findViewById(R.id.btn_sub_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setClass(SubmitSuccess.this,MainActivity.class);
                startActivity(intent1);
            }
        });
        updateUI();
    }
    private void updateUI()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SubmitSuccess.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_orderID = findViewById(R.id.txt_orderID);
                        txt_orderID.setText("订单号："+order_id);
                        txt_orderID.setTextColor(Color.BLACK);
                    }
                });
            }
        }).start();
    }
}
