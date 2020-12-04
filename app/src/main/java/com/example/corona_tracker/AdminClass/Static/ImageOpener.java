package com.example.corona_tracker.AdminClass.Static;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ImageOpener {

    // URL 에서 이미지를 다운로드
    public static File openImage(Context c, final String urlString) {
        File file = ImageFileManager.createImageFile(c);

        try {
            java.net.URL url = new java.net.URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            String contentType = connection.getHeaderField("Content-Type");
            if(!contentType.startsWith("image/") || contentType.startsWith("image/svg")) {
                return null;
            }
            InputStream input = connection.getInputStream();
            byte[] strToByte = ImageCompute.inputStreamToByteArray(input);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(strToByte);
            fos.close();
            input.close();

        } catch (IOException e) {
            return null;
        }

        return file;
    }

    // 갤러리(Uri)에서 이미지를 저장
    public static File openImage(Context c, Uri uri) throws IOException {
        InputStream inputStream = c.getContentResolver().openInputStream(uri);
        byte[] strToByte = ImageCompute.inputStreamToByteArray(inputStream);

        File file = ImageFileManager.createImageFile(c);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(strToByte);
        fos.close();
        inputStream.close();

        return file;
    }
}
