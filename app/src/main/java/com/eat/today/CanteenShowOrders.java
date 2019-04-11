package com.eat.today;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CanteenShowOrders extends Activity {
    static final int UIChanged = 1;
    static final int ShowToast1 = 2;
    String strUrl = "https://today.guaiqihen.com/get_order.php";
    String order_user;
    int order_canteen;
    double total_price;
    List<Dish> dishList = new ArrayList<>();
    ListView list_orderList;
    TextView txt_userName;
    TextView txt_orderId;
    TextView txt_orderCanteen;
    TextView txt_totalPrice;
    CanteenDishAdapter adapter;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case UIChanged :
                    txt_orderCanteen.setText("食堂编号:" + order_canteen);
                    txt_userName.setText("用户名：" + order_user);
                    txt_totalPrice.setText("合计：" + String.valueOf(total_price));
                    adapter.notifyDataSetChanged();
                    break;
                case ShowToast1:
                    Toast.makeText(CanteenShowOrders.this,"未知错误，请重试！",Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_show_orders);
        setTitle("订单信息");
        txt_userName = findViewById(R.id.CanteenUser);
        txt_orderId = findViewById(R.id.CanteenOderId);
        txt_orderCanteen = findViewById(R.id.CanteenNum);
        txt_totalPrice = findViewById(R.id.CanteenTotal);
        list_orderList = findViewById(R.id.CanteenOrderList);

        Intent intent=this.getIntent();
        String orderId = intent.getStringExtra("orderId");
        txt_orderId.setText("订单号："+orderId);

        adapter = new CanteenDishAdapter(CanteenShowOrders.this,R.layout.layout_for_conf,dishList);
        list_orderList.setAdapter(adapter);

        sendHttpRequest(orderId);

    }

    void sendHttpRequest(String orderId)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                BufferedReader bufferedReader;
                try{
                    URL url = new URL(strUrl);
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    connection.connect();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("order_id",orderId);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(jsonObject.toString());
                    if(connection.getResponseCode() == 200)
                    {
                        InputStream in = connection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line ;
                        while((line = bufferedReader.readLine())!= null)
                        {
                            response.append(line);
                        }
                        JSONObject json_res = new JSONObject(response.toString());
                        String status = json_res.getString("status");
                        if (status.equals("success")) {
                            String str = json_res.getString("order_id");
                            if(str.equals(null))
                            {
                                Log.e("Failed","未知错误");
                                handler.sendEmptyMessage(ShowToast1);
                            }
                            else
                            {
                                order_user = json_res.getString("order_user");
                                order_canteen = json_res.getInt("order_canteen");
                                JSONArray order = json_res.getJSONArray("order");
                                total_price = json_res.getDouble("total_price");

                                String dishName;
                                double dishPrice;
                                int dishCount;
                                for(int i=0;i<order.length();i++)
                                {
                                    JSONObject json = order.getJSONObject(i);
                                    dishName=json.getString("name");
                                    dishPrice=json.getDouble("price");
                                    dishCount = json.getInt("count");
                                    Dish dish = new Dish(dishName,dishPrice,dishCount);
                                    dishList.add(dish);
                                }
                                handler.sendEmptyMessage(UIChanged);
                            }
                        }else{
                            Log.e("Failed","获取json信息失败");
                        }
                    }
                }catch(Exception e)
                {
                    Log.e("Failed",e.toString());
                }
            }
        }).start();
    }

}

