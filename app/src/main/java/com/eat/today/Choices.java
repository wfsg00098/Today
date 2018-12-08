package com.eat.today;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Choices extends Activity {

    public static final int COUNT_CHANGED=1;
    private List<Dish> dish_list = new ArrayList<Dish>();
    private List<Dish> vega_list = new ArrayList<Dish>();
    private List<Dish> meat_list = new ArrayList<Dish>();
    private List<String> types = new ArrayList<String>();
    private List<Dish> conf_dish = new ArrayList<>();
    private TextView sum;
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case COUNT_CHANGED:
                    float temp=0;
                    for (int i=0;i<dish_list.size();i++)
                        temp+=dish_list.get(i).getPrice()*dish_list.get(i).getCount();
                    sum.setText("￥"+temp);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choices);

        setTitle("X食堂");

        findViewById(R.id.hotmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Choices.this,MainActivity_progress_zhu.class));
            }
        });

        initDish();

        final TypesAdapter adapter = new TypesAdapter(Choices.this,
                android.R.layout.simple_list_item_1,types);
        ListView type_list = findViewById(R.id.type_list);
        type_list.setAdapter(adapter);

        RecyclerView recyclerView = findViewById(R.id.for_dish);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DishAdapter2 dish_adapter2 = new DishAdapter2(dish_list);
        dish_adapter2.setHandler(handler);
        recyclerView.setAdapter(dish_adapter2);

        Button btn_conf=findViewById(R.id.btn_conf);
        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                conf_dish.clear();

                Dish dish;
                for(int i=0;i<dish_list.size();i++)
                {
                    dish=dish_list.get(i);
                    if (dish.getCount()>0)
                        conf_dish.add(dish);
                }
                bundle.putSerializable("dish",(Serializable)conf_dish);
                Intent intent = new Intent();
                intent.setClass(Choices.this,ConfProducts.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        sum=findViewById(R.id.txt_sum);

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
    private void initDish(){
        Dish dish_1 = new Dish(1,"炒包菜",R.mipmap.chao_bao_cai, 1.5);
        dish_list.add(dish_1);
        vega_list.add(dish_1);
        Dish dish_2 = new Dish(2,"炒土豆丝",R.mipmap.tu_dou_si, 1.5);
        dish_list.add(dish_2);
        vega_list.add(dish_2);
        Dish dish_3 = new Dish(3,"炒豆芽",R.mipmap.chao_dou_ya,1);
        dish_list.add(dish_3);
        vega_list.add(dish_3);
        Dish dish_4 = new Dish(4,"红烧鸡块",R.mipmap.hong_shao_ji_kuai,3);
        dish_list.add(dish_4);
        meat_list.add(dish_4);
        Dish dish_5 = new Dish(5,"回锅肉",R.mipmap.hui_guo_rou,3.5);
        dish_list.add(dish_5);
        meat_list.add(dish_5);
        Dish dish_6 = new Dish(6,"狮子头",R.mipmap.shi_zi_tou,2.5);
        dish_list.add(dish_6);
        meat_list.add(dish_6);
        types.add("热销");
        types.add("蔬菜");
        types.add("肉类");
    }
}
