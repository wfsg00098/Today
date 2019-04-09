package com.eat.today;

import android.util.Log;
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


public class ChangeShuoPhraseNum {
    private String serverUrl = "https://today.guaiqihen.com/like_comment.php";
    private String result;
    HttpURLConnection connection=null;
    BufferedReader reader=null;

    public void addShuoPhraseNum(int shuoId) {
        final int t_shuoId = shuoId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(serverUrl);
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    JSONObject json=new JSONObject();
                    json.put("id",t_shuoId);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(json.toString());
                    Log.i("fanhui",String.valueOf(connection.getResponseCode())+"  "+json);
                    if(connection.getResponseCode()==200){
                        InputStream in = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        JSONObject json_res = new JSONObject(response.toString());
                        result = json_res.getString("status");
                        Log.e("!!!!!!!!!!!!!!!!!!!!!", result);
                        if(result.equals("success"))
                        {
                            Log.i("gdhs","addShuoPhraseNum成功");
                        }
                        else
                        {
                            result="fail";
                            Log.i("zgyusz",result);
                        }
                    }else{
                        Log.i("dianzan","说说点赞失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
