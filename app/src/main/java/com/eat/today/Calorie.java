package com.eat.today;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calorie extends Activity {
    private File mPhotoFile;
    private String mPhotoPath;
    public final static int CAMERA_RESULT = 1;
    private static final int IMAGE = 2;
    private static ProgressDialog pd;

    class showInfo extends Handler {
        @Override
        public void handleMessage(Message msg) {
            pd.cancel();
            int sum = 0;
            findViewById(R.id.textView15).setVisibility(View.VISIBLE);
            String result = msg.obj.toString();
            String[] resultset = result.split(" ");
            TextView tv = findViewById(R.id.textView16);
            for (String temp : resultset) {
                if (temp.equals("drumstick")) {
                    sum += 200;
                    tv.append("鸡腿  约200 Calorie\n");
                }
                if (temp.equals("mantou")) {
                    sum += 50;
                    tv.append("馒头  约50 Calorie\n");
                }
            }
            tv = findViewById(R.id.textView18);
            tv.setText("共计约 " + sum + " Calorie");
        }
    }

    Handler show = new showInfo();

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);
        setTitle("卡路里智能计算");
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("上传中");
        pd.setIndeterminate(false);
        pd.setCancelable(false);

        findViewById(R.id.button8).setOnClickListener(v -> {
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//开始拍照
                mPhotoPath = getSDPath() + "/" + getPhotoFileName();//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面
                mPhotoFile = new File(mPhotoPath);
                if (!mPhotoFile.exists()) {
                    mPhotoFile.createNewFile();//创建新文件
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT,//Intent有了图片的信息
                        Uri.fromFile(mPhotoFile));
                startActivityForResult(intent, CAMERA_RESULT);//跳转界面传回拍照所得数据
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.button9).setOnClickListener(v -> {
            Intent intent2 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent2, IMAGE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            assert c != null;
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            //showImage(imagePath);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ((ImageView) findViewById(R.id.imageView4)).setImageBitmap(bm);
            pd.show();
            new newthread(bm).start();
            c.close();
        } else if (requestCode == CAMERA_RESULT && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
            ((ImageView) findViewById(R.id.imageView4)).setImageBitmap(bitmap);
            pd.show();
            new newthread(bitmap).start();
        }
    }

    class newthread extends Thread {
        Bitmap bitmap;

        newthread(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            byte[] result = output.toByteArray();
            try {
                Socket socket = new Socket("192.168.1.102", 10404);
                socket.getOutputStream().write(result);
                socket.shutdownOutput();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String link = br.readLine();
                Message msg = new Message();
                msg.obj = link;
                show.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
