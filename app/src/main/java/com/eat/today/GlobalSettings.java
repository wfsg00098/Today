package com.eat.today;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class GlobalSettings {
    static boolean isLogged;
    static String username;
    static String password;
    static String nickname;
    private static SQLiteDatabase db;

    static void LoadSettings(String path) {
        db = SQLiteDatabase.openOrCreateDatabase(path + "/settings.db", null);
        String sql = "create table if not exists settings(username text, password text, nickname text, islogged text)";
        db.execSQL(sql);
        Cursor cursor = db.query("settings", new String[]{"username", "password", "nickname", "islogged"}, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            sql = "insert into settings values('','','','')";
            db.execSQL(sql);
            username = "";
            password = "";
            nickname = "";
            isLogged = false;
        } else {
            cursor.moveToFirst();
            username = cursor.getString(0);
            password = cursor.getString(1);
            nickname = cursor.getString(2);
            isLogged = cursor.getString(3).equals("true");
        }
        cursor.close();
    }

    static boolean Login() {
        try {
            HttpURLConnection url = (HttpURLConnection) new URL("https://today.guaiqihen.com/user_login.php?username=" + username + "&password=" + password).openConnection();
            url.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
            br.readLine();
            JSONObject json = new JSONObject(br.readLine());
            String success = json.getString("status");

            if (success.equals("success")) {
                String nick = json.getString("nickname");
                String sql = "update settings set username='" + username + "' , password='" + password + "' , islogged='true' , nickname='" + nick + "'";
                db.execSQL(sql);
                isLogged = true;
                nickname = nick;
                return true;
            } else {
                return false;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    static void Logout() {
        isLogged = false;
        username = "";
        password = "";
        nickname = "";
        String sql = "update settings set username='" + username + "' , password='" + password + "' , islogged='false'";
        db.execSQL(sql);
    }

    static boolean Register() {
        try {

            HttpURLConnection url = (HttpURLConnection) new URL("https://today.guaiqihen.com/user_register.php?username=" + username + "&password=" + password + "&nickname=" + URLEncoder.encode(nickname, "utf-8")).openConnection();
            url.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(url.getInputStream()));
            br.readLine();
            JSONObject json = new JSONObject(br.readLine());
            String success = json.getString("status");
            return success.equals("success");

        } catch (Exception ignored) {
        }
        return false;
    }

    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    static String sha1(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byte2hex(digesta);
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (byte aB : b) {
            stmp = (Integer.toHexString(aB & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }

}
