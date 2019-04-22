package com.eat.today;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class activity_campus1 extends Activity {

    private ProgressBar ProgressBar1;
    private ProgressBar ProgressBar2;
    private ProgressBar ProgressBar3;
    private ProgressBar ProgressBar4;
    private ProgressBar ProgressBar5;
    private ProgressBar ProgressBar6;
    private ProgressBar ProgressBar7;
    private ProgressBar ProgressBar8;

    private int state1 = 0; //用来表示进度条进度
    private int state2 = 0;
    private int state3 = 0;
    private int state4 = 0;
    private int state5 = 0;
    private int state6 = 0;
    private int state7 = 0;
    private int state8 = 0;
    private int state0 = 0;    //state0代表总人数
    private float[] ratio;
    private int[] ratios;

    private Handler mHander1;
    private String result = "";
    private String[] states;
    private String regEx = "[^0-9]";

    private ImageButton Refresh;
    //private ImageButton Navigate;

    public void send() {
        String target = "https://today.guaiqihen.com/get_hot.php";//在这里填写服务器该模块提交的目标地址
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();//创建http链接
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);//自链接中读取数据
            urlConnection.setUseCaches(false);//禁止缓存
            urlConnection.setInstanceFollowRedirects(true);//支持自动重定向
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置内容类型
            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());//设置输出流

            String param = "campus" + URLEncoder.encode("feicuihu", "utf-8"); //向服务器发送字段"feicuihu"
            out.writeBytes(param);//把向服务器输出的内容写入数据输出流
            out.flush();//输出缓存
            out.close();//关闭输出流

            //判断是否响应,并对服务器反馈的值(字符串)进行处理
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());//用于获取读取到的内容
                BufferedReader bufferedReader = new BufferedReader(in);//获取输入流对象
                String inputLine = bufferedReader.readLine();
                JSONObject jb = new JSONObject(inputLine);


                in.close();
                states = inputLine.split(",");//把传来的数据按空格分开,一共七个数据,分别是第一\二...教工餐厅的用餐人数(服务器端发送数据为以空格分开的七个数字)
                try {

                    state1 = jb.getInt("data0");
                    state2 = jb.getInt("data1");
                    state3 = jb.getInt("data2");
                    state4 = jb.getInt("data3");
                    state5 = jb.getInt("data4");
                    state6 = jb.getInt("data5");
                    state7 = jb.getInt("data6");
                    state8 = jb.getInt("data7");
                    state0 = state1 + state2 + state3 + state4 + state5 + state6 + state7 + state8;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                ratio[0] = ((float)state1 / (float)state0) * 100;
                ratio[1] = ((float)state2 / (float)state0) * 100;
                ratio[2] = ((float)state3 / (float)state0) * 100;
                ratio[3] = ((float)state4 / (float)state0) * 100;
                ratio[4] = ((float)state5 / (float)state0) * 100;
                ratio[5] = ((float)state6 / (float)state0) * 100;
                ratio[6] = ((float)state7 / (float)state0) * 100;
                ratio[7] = ((float)state8 / (float)state0) * 100;

                for (int i = 0; i < ratio.length; i++) {
                    ratios[i] = (int) ratio[i];
                }
                regEx = Float.toString(ratios[7]);
                Log.d("ss", regEx);
            }
            urlConnection.disconnect();//断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compus_1);
        ratio = new float[8];
        ratios = new int[8];

        ProgressBar1 = findViewById(R.id.progressBar1);
        ProgressBar2 = findViewById(R.id.progressBar2);
        ProgressBar3 = findViewById(R.id.progressBar3);
        ProgressBar4 = findViewById(R.id.progressBar4);
        ProgressBar5 = findViewById(R.id.progressBar5);
        ProgressBar6 = findViewById(R.id.progressBar6);
        ProgressBar7 = findViewById(R.id.progressBar7);
        ProgressBar8 = findViewById(R.id.progressBar8);
        Refresh = findViewById(R.id.RefreshButton);
        //Navigate = findViewById(R.id.guild_Button);


        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       send();
                       Message m = mHander1.obtainMessage();
                       mHander1.sendMessage(m);
                    }
                }).start();
            }
        });
        /*
        Navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(activity_campus1.this, navigate.class);
                Bundle bundle1 = new Bundle();
                //可以在这里对bundle1添加要传到campus1的数据
                intent1.putExtras(bundle1);
                startActivity(intent1);
            }
        });
        */
        mHander1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                    ProgressBar1.setProgress(ratios[0]);
                    ProgressBar2.setProgress(ratios[1]);
                    ProgressBar3.setProgress(ratios[2]);
                    ProgressBar4.setProgress(ratios[3]);
                    ProgressBar5.setProgress(ratios[4]);
                    ProgressBar6.setProgress(ratios[5]);
                    ProgressBar7.setProgress(ratios[6]);
                    ProgressBar8.setProgress(ratios[7]);
                super.handleMessage(msg);
            }

        };


    }
}
