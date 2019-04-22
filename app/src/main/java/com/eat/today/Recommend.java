package com.eat.today;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class Recommend extends Activity {
    int[][] dishes = {
            {3, 11, 22, 13, 17, 20, 23, 8, 14, 27},
            {18, 6, 8, 10, 19, 25, 17, 14, 23, 12},
            {24, 6, 3, 2, 10, 14, 19, 23, 26, 27},
            {20, 12, 25, 6, 8, 2, 19, 26, 13, 14},
            {7, 11, 10, 3, 13, 27, 24, 12, 17, 22},
            {10, 4, 8, 22, 7, 17, 20, 3, 11, 13}};
    Vector<ImageView> imageViews = new Vector<>();

    class getImage extends Thread {
        String url;
        int index;

        getImage(String url, int index) {
            this.url = url;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setConnectTimeout(6000);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Message msg = new Message();
                msg.obj = bitmap;
                msg.what = index;
                setimage.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class addItem extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                LinearLayout ll = findViewById(R.id.ll);
                View temp = View.inflate(ll.getContext(), R.layout.recomment_list, null);
                TextView name = temp.findViewById(R.id.name);
                TextView cal = temp.findViewById(R.id.cal);
                TextView location = temp.findViewById(R.id.location);
                TextView price = temp.findViewById(R.id.price);
                imageViews.addElement(temp.findViewById(R.id.image));
                JSONObject json = new JSONObject((String) msg.obj);
                name.setText(json.getString("name"));
                cal.setText(String.format("热量 %s 卡路里     %s赞", json.getString("calorie"), json.getString("liked")));
                location.setText(String.format("第 %s 食堂有售", json.getInt("canteen") + 1));
                price.setText(String.format("￥ %s", json.getString("price")));
                new getImage(json.getString("image"), msg.what).start();
                ll.addView(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    addItem additem = new addItem();

    class setImage extends Handler {
        @Override
        public void handleMessage(Message msg) {
            ImageView image = imageViews.get(msg.what);
            image.setImageBitmap((Bitmap) msg.obj);
        }
    }

    setImage setimage = new setImage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        setTitle("智能推荐");
        if (!GlobalSettings.isLogged) {
            Toast.makeText(this, "未登录，无法推荐", Toast.LENGTH_LONG + 5).show();
            finish();
        }
        int select;
        switch (GlobalSettings.username) {
            case "wfsg00098":
                select = 0;
                break;
            case "HakureiReimu":
                select = 1;
                break;
            case "zhangfy":
                select = 2;
                break;
            case "lwq":
                select = 3;
                break;
            case "lzy":
                select = 4;
                break;
            default:
                select = 5;
        }
        int[] dish = dishes[select];
        Toast.makeText(Recommend.this, "查询中", Toast.LENGTH_LONG + 5).show();
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        JSONObject json = new JSONObject();
                        json.put("id", dish[i]);
                        URL url = new URL("https://today.guaiqihen.com/get_dish.php");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.connect();
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeBytes(json.toString());
                        if (connection.getResponseCode() == 200) {
                            InputStream in = connection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                response.append(line);
                            }
                            Message msg = new Message();
                            msg.obj = response.toString();
                            Log.e("json", response.toString());
                            msg.what = i;
                            additem.sendMessage(msg);
                        } else {
                            Toast.makeText(Recommend.this, "连接失败，请检查网络连接", Toast.LENGTH_LONG + 5).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
