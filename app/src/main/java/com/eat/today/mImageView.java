package com.eat.today;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class mImageView extends android.support.v7.widget.AppCompatImageView {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(),"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    public mImageView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }
    public mImageView(Context context)
    {
        super(context);
    }
    public mImageView(Context context,AttributeSet attrs)
    {
        super(context,attrs);
    }
    public void setImageURL(final String path)
    {
        new Thread(){
            @Override
            public void run()
            {
                try{
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    int code = connection.getResponseCode();
                    if(code == 200)
                    {
                        InputStream in = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        Message msg = new Message();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        in.close();
                    }
                    else{
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                }catch (Exception e){
                    Log.e("Error",e.toString());
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }
}
