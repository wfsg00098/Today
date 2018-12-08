package com.eat.today;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

public class SubmitSuccess extends Activity {

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
                finish();
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
                        try {
                            Bitmap bitmap = CodeCreator.createQRCode(order_id,400,400,null);
                            ImageView img = findViewById(R.id.success_qr);
                            img.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }).start();
    }
}
