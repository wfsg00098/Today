package com.eat.today;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Comment extends Activity {
    private int dishId ;
    private String username = "dstjdrny";
    private TextView tv_userName;
    private itemAdapter adapter;
    private List<ItemModel> itemList = new ArrayList<>();
    private List<ItemModel> data = null;
    private Button iv_refresh,btn_addComment;
    private ListView listview;
    private final String serverUrl = "https://today.guaiqihen.com/get_comment.php";
    private static long lastClickTime;
    private String t1="[";
    private String t2="]";
    private String test = "{id:'1',username:'张方勇',phone:'iPhone 7',date:'3-20 12:00',message:'今天天气真好',liked:'0'}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = this.getIntent();
        dishId=intent.getIntExtra("dish",0);

        initViews();

        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initData();// 重新加载数据到listview中
                    }
                }).start();
            }
        });

        btn_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Comment.this,Tab_AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Add:
                Intent intent = new Intent(Comment.this,Tab_AddActivity.class);
                intent.putExtra("dish",dishId);
                startActivity(intent);
        }
        return true;
    }


    private void initViews(){
        iv_refresh = findViewById(R.id.iv_refresh);
        listview = findViewById(R.id.shuoList);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userName.setText(username);
        btn_addComment = findViewById(R.id.btn_addComment);
    }

    private void initData(){
        itemList = new ArrayList<>();
        HttpURLConnection connection=null;
        BufferedReader reader=null;
        try {
            URL url = new URL(serverUrl);
            connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            JSONObject json=new JSONObject();
            json.put("dish",dishId);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(json.toString());
            out.flush();//输出缓存
            out.close();//关闭输出流

            if(connection.getResponseCode()==200){
                InputStream in = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while((line=reader.readLine())!=null){
                    response.append(line);
                }
                JSONObject temp = new JSONObject(response.toString());
                if (temp.getString("status").equals("success")) {
                    JSONArray jsonArray = new JSONArray(temp.getString("comments"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.i("fjdksalfja", jsonObject.toString());
                        final ItemModel item = new ItemModel();
                        item.setIsPhrase(false);
                        try {
                            if (jsonObject.getInt("liked") > 0) {
                                item.setIsPhrase(true);
                            } else {
                                item.setIsPhrase(false);
                            }
                        } catch (JSONException e) {
                            Log.e("Error",e.toString());
                        }
                        item.setShuoContent(jsonObject.getString("message"));
                        item.setShuoDate(jsonObject.getString("date"));
                        item.setShuoPhoneModel("iPhone 7");
                        item.setShuoPhraseNum(jsonObject.getInt("liked"));
                        item.setUserName(jsonObject.getString("username"));
                        int imgnum = new Random().nextInt(12) + 1;
                        item.setHeadImg(getImgDrawable(imgnum));
                        item.setShuoId(jsonObject.getInt("id"));
                        itemList.add(item);
                    }
                    Message msg = new Message();
                    msg.obj = itemList;
                    handler.sendMessage(msg);
                }else{
                    Log.i("tag","返回参数有问题,"+temp.getString("status"));
                }
            }else {
                ItemModel item = new ItemModel();
                item.setShuoContent("无网络连接");
                itemList.add(item);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            data = (List<ItemModel>) msg.obj;
            Log.i("TAG", data.size() + "数据大小");
            adapter = new itemAdapter(Comment.this, R.layout.item, data);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };

    /*  public static boolean isFastDoubleClick() {
          long time = System.currentTimeMillis();
          long timeD = time - lastClickTime;
          if (0 < timeD && timeD < 4000) { // 500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
              return true;
          }
          lastClickTime = time;
          return false;
      }   */
    private int getImgDrawable(int x) {
        int result = 0;
        switch (x) {
            case 1:
                result = R.drawable.head1;
                break;
            case 2:
                result = R.drawable.head2;
                break;
            case 3:
                result = R.drawable.head3;
                break;
            case 4:
                result = R.drawable.head4;
                break;
            case 5:
                result = R.drawable.head5;
                break;
            case 6:
                result = R.drawable.head6;
                break;
            case 7:
                result = R.drawable.head7;
                break;
            case 8:
                result = R.drawable.head8;
                break;
            case 9:
                result = R.drawable.head9;
                break;
            case 10:
                result = R.drawable.head10;
                break;
            case 11:
                result = R.drawable.head11;
                break;
            case 12:
                result = R.drawable.head12;
                break;
            default:
                break;
        }
        return result;
    }
}
