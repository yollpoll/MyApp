package com.example.xlm.mydrawerdemo.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by 鹏祺 on 2017/6/30.
 */

public class FileUtils {
    public static File cacheFile;//这个是最近一次调用写入文件的目标文件

    /**
     * 写入一段文字到本地文本
     *
     * @param path     路径
     * @param fileName 文件名
     * @param content  内容
     */
    public static boolean writeToFile(String path, String fileName, String content) {
        File sd = Environment.getExternalStorageDirectory();
        String dirPath = sd.getPath() + Constant.SD_CACHE_DIR + "/" + path;
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdir();
        File file = new File(dir + "/" + fileName);
        OutputStream outputStream = null;
        cacheFile = file;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(content);
            objectOutputStream.flush();
            objectOutputStream.close();
            byteArrayOutputStream.close();

            byte[] fileReader = byteArrayOutputStream.toByteArray();
            Log.d("spq", fileReader.toString());
            outputStream = new FileOutputStream(file);
            outputStream.write(fileReader);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    /**
     * @param dir      目录名
     * @param fileName 文件名
     * @param context
     * @return
     */
    public static String readFromFile(String dir, String fileName, Context context) {
        String result = "";
        File file = new File(Environment.getExternalStorageDirectory() + Constant.SD_CACHE_DIR + "/" + dir + "/" + fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            result = (String) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param context
     * @return
     */
    public static String readFromFile(File file) {
        String result = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            result = (String) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
