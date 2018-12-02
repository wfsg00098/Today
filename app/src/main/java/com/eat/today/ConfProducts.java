package com.eat.today;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConfProducts extends AppCompatActivity{
    private List<Dish> dish_list = new ArrayList<Dish>();
    private List<Dish> vega_list = new ArrayList<Dish>();
    private List<Dish> meat_list = new ArrayList<Dish>();
    private String username = "Example";                                                            //用户名
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confproducts);
        //initDish();
        Intent intent = this.getIntent();
        dish_list=(List<Dish>) intent.getSerializableExtra("dish");

        Toolbar toolbar = findViewById(R.id.toolbar_conf);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.list_conf);
        ConfDishAdapter adapter = new ConfDishAdapter(ConfProducts.this,
                R.layout.layout_for_conf,dish_list);
        listView.setAdapter(adapter);

        Button btn_pay = findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click", "onClick!!!!!!!!!!!!!!!!!!!!!!!!! ");
                sendRequestHttpURLConnection();
            }
        });
        updateUISum();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUISum(){                                                                     //合计金额
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConfProducts.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dish dish;
                        double sum=0;
                        for(int i=0;i<dish_list.size();i++)
                        {
                            dish = dish_list.get(i);
                            sum += dish.getCount()*dish.getPrice();
                        }
                        TextView sum_conf=findViewById(R.id.txt_sum_conf);
                        sum_conf.setText("￥"+sum);
                    }
                });
            }
        }).start();
    }

    private void sendRequestHttpURLConnection(){                                                    //
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try {
                    String str_temp;
                    URL url = new URL("https://today.guaiqihen.com/generate_order_id.php");
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    JSONObject json=new JSONObject();
                    json.put("username",username);
                    json.put("canteen","1");
                    json.put("count","2");
                    json.put("id1","xxx");
                    json.put("count1","x");
                    json.put("id2","xxx");
                    json.put("count2","x");

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(json.toString());
                    if(connection.getResponseCode()==200){
                        InputStream in = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        JSONObject json_res = new JSONObject(response.toString());
                        String status = json_res.getString("status");
                        Log.e("!!!!!!!!!!!!!!!!!!!!!", status);
                        if(status.equals("success"))
                        {
                            String order_id = json_res.getString("order_id");
                            Intent intent = new Intent();
                            intent.setClass(ConfProducts.this,SubmitSuccess.class);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                        }
                        else
                        {
                            str_temp = "提交失败，请重试。";
                            Log.e("Click", "onClick????????????????????????????? ");
                            Intent intent=new Intent();
                            intent.setClass(ConfProducts.this,SubmitFailed.class);
                            startActivity(intent);
                        }
                    }
                    else {
                        str_temp = "提交失败，请重试。错误码："+connection.getResponseCode();
                        Intent intent=new Intent();
                        intent.setClass(ConfProducts.this,SubmitFailed.class);
                        startActivity(intent);
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    Log.e("TAG","Exception!!!"+ Log.getStackTraceString(e));
                }finally {
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
