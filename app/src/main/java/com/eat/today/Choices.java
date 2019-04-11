/* 食堂菜色选择界面 */

package com.eat.today;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Choices extends Activity {

    public static final int COUNT_CHANGED = 1;
    public static final int TOAST_DISPLAY = 2;
    public static final int GET_PICTURE_SUCCESS = 3;
    private static final int TOTAL = 0;
    private static final int HOT = 1;
    private static final int MEAT = 2;
    private static final int VEGETABLE = 3;
    private List<Dish> dish_list = new ArrayList<>();
    private List<Dish> hot_list = new ArrayList<>();
    private List<Dish> vega_list = new ArrayList<>();
    private List<Dish> meat_list = new ArrayList<>();
    private List<Dish> total_list = new ArrayList<>();

    private List<String> types = new ArrayList<>();
    private List<Dish> conf_dish = new ArrayList<>();
    private List<String> canteen_list = new ArrayList<>();
    private TextView sum;
    private Spinner spinner;
    private String url_str = "https://today.guaiqihen.com/get_dishes.php";
    private int canteenId;
    private TypesAdapter adapter;
    private DishAdapter2 dish_adapter2;
    private RecyclerView recyclerView;
    private ListView type_list;

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case COUNT_CHANGED:
                    float temp=0;
                    for (int i=0;i<total_list.size();i++)
                        temp+=total_list.get(i).getPrice()*total_list.get(i).getCount();
                    sum.setText("￥"+temp);
                    break;
                case TOAST_DISPLAY:
                    Toast.makeText(Choices.this,"这里什么都没有噢",Toast.LENGTH_SHORT).show();
                    sum.setText("￥0");
                case GET_PICTURE_SUCCESS:
                    sum.setText("￥0");
                    dish_adapter2.notifyDataSetChanged();
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choices);
        recyclerView = findViewById(R.id.for_dish);
        sum=findViewById(R.id.txt_sum);
        spinner = findViewById(R.id.canteenSelectSpinner);

        initSpinner();

        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,canteen_list);
        spinner.setAdapter(adapter_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                canteenId = position;
                if (adapter.selectedPosition != -1) {
                    TextView v_selected = (TextView) type_list.getChildAt(adapter.selectedPosition - type_list.getFirstVisiblePosition());
                    v_selected.setTextColor(Color.parseColor("#222323"));
                    v_selected.setBackgroundColor(Color.parseColor("#f1f1f1"));
                    v_selected = (TextView) type_list.getChildAt(0);
                    v_selected.setTextColor(Color.parseColor("#000000"));
                    v_selected.setBackgroundColor(Color.parseColor("#fdfdfe"));
                }
                sendHttpRequest(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.hotmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Choices.this,MainActivity_progress_zhu.class));
            }
        });

        initDish();

        adapter = new TypesAdapter(Choices.this,
                android.R.layout.simple_list_item_1,types);

        type_list = findViewById(R.id.type_list);
        type_list.setAdapter(adapter);
        type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.flag=true;
                adapter.selectedPosition = position;
                for (int i=0;i<parent.getCount();i++){
                    TextView v = (TextView)parent.getChildAt(i-type_list.getFirstVisiblePosition());
                    if(position==i){
                        v.setTextColor(Color.parseColor("#000000"));
                        v.setBackgroundColor(Color.parseColor("#fdfdfe"));
                    }
                    else
                    {
                        v.setTextColor(Color.parseColor("#222323"));
                        v.setBackgroundColor(Color.parseColor("#f1f1f1"));
                    }
                }
                dish_list.clear();
                Log.e("Check",String.valueOf(position));
                switch(position){
                    case TOTAL:
                        dish_list.addAll(total_list);
                        Log.e("Check","total_list的长度为"+String.valueOf(total_list.size()));
                        Log.e("Check","total_list的长度为"+String.valueOf(total_list.size()));
                        dish_adapter2.notifyDataSetChanged();
                        break;
                    case HOT:
                        dish_list.addAll(hot_list);
                        dish_adapter2.notifyDataSetChanged();
                        break;
                    case MEAT:
                        dish_list.addAll(meat_list);
                        dish_adapter2.notifyDataSetChanged();
                        break;
                    case VEGETABLE:
                        dish_list.addAll(vega_list);
                        dish_adapter2.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                if(dish_list.size()==0)
                    Toast.makeText(Choices.this,"这里什么都没有噢",Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dish_adapter2 = new DishAdapter2(Choices.this, dish_list);
        dish_adapter2.setHandler(handler);
        recyclerView.setAdapter(dish_adapter2);

        Button btn_conf=findViewById(R.id.btn_conf);
        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                conf_dish.clear();

                Dish dish;
                for(int i=0;i<total_list.size();i++)
                {
                    dish=total_list.get(i);
                    if (dish.getCount()>0)
                        conf_dish.add(dish);
                }
                bundle.putSerializable("dish",(Serializable)conf_dish);
                Intent intent = new Intent();
                intent.setClass(Choices.this,ConfProducts.class);
                intent.putExtras(bundle);
                intent.putExtra("canteenId",canteenId);
                startActivity(intent);
            }
        });


    }
    private void initDish(){
        types.add("全部");
        types.add("热销");
        types.add("蔬菜");
        types.add("肉类");
    }

    private void initSpinner()
    {
        canteen_list.add("一食堂");
        canteen_list.add("二食堂");
        canteen_list.add("三食堂");
        canteen_list.add("四食堂");
        canteen_list.add("五食堂");
        spinner.setPrompt("一食堂");
    }

    public void dishClick(View view){
        int position = recyclerView.getChildAdapterPosition(view);
        Dish dish = dish_list.get(position);
        Intent intent = new Intent();
        intent.setClass(Choices.this,Comment.class);
        intent.putExtra("dish",dish.getId());
        startActivity(intent);
    }

    private void sendHttpRequest(int canteenNum)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(url_str);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    JSONObject json = new JSONObject();
                    json.put("username",GlobalSettings.username);
                    json.put("canteen",String.valueOf(canteenNum));
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(json.toString());
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
                            int count = json_res.getInt("count");
                            // 数量为0，说明没有菜品，清空列表，也清空购物车内容
                            if (count == 0) {
                                conf_dish.clear();
                                dish_list.clear();
                                Message msg = new Message();
                                msg.what = TOAST_DISPLAY;
                                handler.sendMessage(msg);
                            } else {
                                dish_list.clear();
                                total_list.clear();
                                hot_list.clear();
                                meat_list.clear();
                                vega_list.clear();
                                int id,liked,calorie,type;
                                double price;
                                String imgUrl,name;
                                for (int i = 0;i<count;i++)
                                {
                                    id = json_res.getInt("id"+(i+1));
                                    name = json_res.getString("name"+(i+1));
                                    liked = json_res.getInt("liked"+(i+1));
                                    calorie = json_res.getInt("calorie"+(i+1));
                                    price = json_res.getDouble("price"+(i+1));
                                    imgUrl = json_res.getString("image"+(i+1));
                                    type = json_res.getInt("type"+(i+1));
                                    Dish dish = new Dish(id,name,imgUrl,price,liked,calorie,type);
                                    total_list.add(dish);
                                    dish_list.add(dish);
                                    switch(type){
                                        case 0:
                                            hot_list.add(dish);
                                            break;
                                        case 1:
                                            meat_list.add(dish);
                                            break;
                                        case 2:
                                            vega_list.add(dish);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                Message msg = new Message();
                                msg.what = GET_PICTURE_SUCCESS;
                                handler.sendMessage(msg);

                            }
                        }else{
                            Log.e("Failed","获取json信息失败");
                            Toast.makeText(Choices.this,"获取信息失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Log.e("Failed","连接失败");
                        Toast.makeText(Choices.this,
                                "连接失败，请检查网络连接",Toast.LENGTH_SHORT);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e("TAG","Exception:"+ e.toString());
                }finally {
                    if(bufferedReader!= null)
                    {
                        try{
                            bufferedReader.close();
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection!=null)
                        connection.disconnect();
                }
            }
        }).start();
    }
}
