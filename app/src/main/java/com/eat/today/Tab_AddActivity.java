package com.eat.today;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tab_AddActivity extends Activity {
    private ImageView ivPublish;
    private ImageView ivBack;
    private EditText etAddContent;
    private String content;
    private final String serverUrl = "https://today.guaiqihen.com/add_comment.php";
    private String username = "zfy";


    @Override
    //  @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        //     StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //     StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_add);
        initView();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Tab_AddActivity.this.finish();
            }
        });
     /*   ivPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SimpleDateFormat df = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm");
                content =etAddContent.getText().toString();
                if (content == null || content.length()<=0){
                    Toast.makeText(getApplicationContext(), "说说内容不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    String t1 ="0";
                    String temp = ",{id:'1',username:'"+username+"',date:'"+df.format(new Date())+"',phone:'"+android.os.Build.MODEL+"',message:'"+content+"',liked:'"+t1+"'}";
                    Intent intent = new Intent();
                    intent.putExtra("message", temp);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });*/
        ivPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle bundle = getIntent().getExtras();
                content = etAddContent.getText().toString();
                if (content == null || content.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "说说内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new Runnable() {
                        String result;

                        @Override
                        public void run() {
                            HttpURLConnection connection = null;
                            BufferedReader reader = null;
                            try {
                                URL url = new URL(serverUrl);
                                connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setConnectTimeout(5000);
                                connection.setReadTimeout(5000);
                                connection.connect();
                                JSONObject json = new JSONObject();
                                json.put("username", username);
                                //json.put("dish", bundle.getInt("dish"));
                                json.put("dish","1");
                                json.put("message", content);
                                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                                out.writeBytes(json.toString());
                                if (connection.getResponseCode() == 200) {
                                    InputStream in = connection.getInputStream();
                                    reader = new BufferedReader(new InputStreamReader(in));
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        response.append(line);
                                    }
                                    JSONObject json_res = new JSONObject(response.toString());
                                    result = json_res.getString("status");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                if (reader != null) {
                                    try {
                                        reader.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (connection != null) {
                                    connection.disconnect();
                                }
                            }
                            Message msg = new Message();
                            msg.obj =result;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj.toString().equals("success")) {
                Toast.makeText(getApplicationContext(), "说说发表成功",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "发生未知错误导致说说发表失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initView() {
        ivPublish = (ImageView) findViewById(R.id.iv_publish);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        etAddContent = (EditText) findViewById(R.id.etAddContent);
    }
}
