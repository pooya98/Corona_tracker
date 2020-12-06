package com.example.corona_tracker;

import android.view.View;

import com.example.corona_tracker.AdminClass.NotiData;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DAO {
    boolean admin_account_flag;
    boolean building_id_flag;
    String building_name;
    boolean insert_enter_info_flag;
    boolean insert_notice_flag;
    boolean delete_notice_flag;
    boolean update_notice_flag;
    int noti_data_count;

    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;
    java.util.List<NameValuePair> nameValuePairs;
    ArrayList<NotiData> list = null;
    NotiData notiData = null;
    ArrayList<String> building_list;
    ArrayList<EnterData> enterData_list;

    public void Test(){
        Thread loadThread = new Thread(){
            public void run(){
                test_con();
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }
    }

    public ArrayList<NotiData> get_notiData_array(){

        list = null;

        Thread loadThread = new Thread(){
            public void run(){
                get_notice();
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }
        return list;
    }

    public ArrayList<String> get_building_array(){

        building_list = null;

        Thread loadThread = new Thread(){
            public void run(){
                get_building_array_con();
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }
        return building_list;
    }



    public ArrayList<EnterData> get_EnterData_array(final String building_name, final String enter_date){

        enterData_list = null;

        Thread loadThread = new Thread(){
            public void run(){
                get_EnterData_array_con(building_name, enter_date);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }
        return enterData_list;
    }

    public int get_notiData_count(){

        noti_data_count = 0;

        Thread loadThread = new Thread(){
            public void run(){
                get_notiData_count_con();
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }
        return noti_data_count;
    }

    public NotiData get_notiData(final int notice_id){

        notiData = null;

        Thread loadThread = new Thread(){
            public void run(){
                get_notiData_con(notice_id);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }
        return notiData;
    }


    public boolean insert_notice(final NotiData insert_notiData){

        Thread loadThread = new Thread(){
            public void run(){
                insert_notice_con(insert_notiData);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }

        return insert_notice_flag;
    }

    public boolean update_notice(final NotiData update_notiData){

        Thread loadThread = new Thread(){
            public void run(){
                update_notice_con(update_notiData);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }

        return update_notice_flag;
    }

    public boolean delete_notice(final int notice_id){

        Thread loadThread = new Thread(){
            public void run(){
                delete_notice_con(notice_id);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }

        return delete_notice_flag;
    }

    public boolean check_admin_account(final String admin_ID, final String admin_PW){

        boolean success_flag = false;

        Thread loadThread = new Thread(){
            public void run(){
                check_admin_account_con(admin_ID, admin_PW);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }

        return admin_account_flag;
    }

    public boolean check_building_id(final String building_id){

        Thread loadThread = new Thread(){
            public void run(){
                check_building_id_con(building_id);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }

        return building_id_flag;
    }

    public String get_building_name(final String building_id){

        Thread loadThread = new Thread(){
            public void run(){
                get_building_name_con(building_id);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }

        return building_name;
    }

    public boolean insert_enter_info(final String building_id, final String date, final String time, final String student_id, final String student_phone){

        Thread loadThread = new Thread(){
            public void run(){
                insert_enter_info_con(building_id, date, time, student_id, student_phone);
            }
        };

        loadThread.start();
        System.out.println("--- loadThread go!");

        try{
            loadThread.join();
            System.out.println("--- loadThread done!");
        }catch(Exception e){
            e.getMessage();
        }

        return insert_enter_info_flag;
    }







    //
    //
    //
    //

    void test_con(){
        try {
            URL url = new URL("http://14.45.108.71:80/corona_tracker/show_notice.php");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);

                String a = br.readLine();
                System.out.println("titi"+a);
            }finally {
                urlConnection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    void get_notice(){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/show_notice.php"); // php주소 연동
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
            }

            System.out.println("uuuuu"+response_string);

            String[] token;
            token = response_string.split("\\$");

            System.out.println("ddddd"+token.length);
            if(token[0].equals("")){
            }
            else {
                list = new ArrayList<NotiData>();
                for (int i = 0; i < token.length; i = i+3) {
                    NotiData notiData = new NotiData();

                    notiData.setId(Integer.parseInt(token[i]));
                    System.out.println("token[0] - "+token[i]);
                    notiData.setTitle(token[i+1]);
                    System.out.println("token[1] - "+token[i+1]);
                    notiData.setTimeFromText(token[i+2]);
                    System.out.println("token[2] - "+token[i+2]);
                    list.add(notiData);
                }
            }


        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void get_notiData_count_con(){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/get_notiData_count.php"); // php주소 연동
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";

            while(scan.hasNext()){
                response_string += scan.nextLine();
                if(response_string.equals("")){

                }else{
                    noti_data_count = Integer.parseInt(response_string);
                    System.out.println("assssa"+noti_data_count);
                }
            }


        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void get_notiData_con(int notice_id){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/get_notidata.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("notice_id",Integer.toString(notice_id)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
            }

            System.out.println("uuuuu"+response_string);
            System.out.println("테스트 공지 입니다\n 테스트");

            String[] token;
            token = response_string.split("\\$");

            System.out.println("ddddd"+token.length);
            if(token[0].equals("")){
            }
            else {
                for (int i = 0; i < token.length; i = i+3) {
                    NotiData temp_notiData = new NotiData();

                    temp_notiData.setId(Integer.parseInt(token[i]));
                    System.out.println("token[0] - "+token[i]);
                    temp_notiData.setTitle(token[i+1]);
                    System.out.println("token[1] - "+token[i+1]);
                    temp_notiData.setAuthor_id(token[i+2]);
                    System.out.println("token[2] - "+token[i+2]);
                    temp_notiData.setContent(token[i+3]);
                    System.out.println("token[3] - "+token[i+3]);
                    temp_notiData.setTimeFromText(token[i+4]);
                    System.out.println("token[4] - "+token[i+4]);

                    notiData = temp_notiData;
                    break;
                }
            }


        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void check_admin_account_con(String admin_ID, String admin_PW){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/check_admin_account.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("admin_ID",admin_ID));
            nameValuePairs.add(new BasicNameValuePair("admin_PW",admin_PW));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
                if(response_string.equals("T")){
                    admin_account_flag = true;
                }else{
                    admin_account_flag = false;
                }
            }

            System.out.println("---Response : "+response_string);

        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void check_building_id_con(String building_id){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/check_building_id.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("B_ID", building_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
                if(response_string.equals("T")){
                    building_id_flag = true;
                }else{
                    building_id_flag = false;
                }
            }

            System.out.println("---Response : "+response_string);

        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void get_building_name_con(String building_id){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/get_building_name.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("B_ID", building_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
                building_name = response_string;
            }

            System.out.println("---Response : "+response_string);

        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void get_building_array_con(){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/get_building_array.php"); // php주소 연동
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
            }

            System.out.println("---Response : "+response_string);

            String[] token;
            token = response_string.split("\\^");

            if(token[0].equals("")){
            }
            else {
                building_list = new ArrayList<String>();
                for (int i = 0; i < token.length; i++) {
                    System.out.println("token " + i + " : " + token[i]);
                    building_list.add(token[i]);
                }
            }

        }catch(Exception E){
            E.printStackTrace();
        }
    }


    void insert_enter_info_con(String building_id, String date, String time, String student_id, String student_phone){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/insert_enter_info.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("building_id", building_id));
            nameValuePairs.add(new BasicNameValuePair("date", date));
            nameValuePairs.add(new BasicNameValuePair("time", time));
            nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
            nameValuePairs.add(new BasicNameValuePair("student_phone", student_phone));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
                if(response_string.equals("T")){
                    insert_enter_info_flag = true;
                }else{
                    insert_enter_info_flag = false;
                }
            }

            System.out.println("---Response : "+response_string);

        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void get_EnterData_array_con(String building_name, String enter_date ){
        try{
            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/get_enterData_array.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("building_id", building_name.substring(1,4)));
            nameValuePairs.add(new BasicNameValuePair("enter_date", enter_date));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
            }

            System.out.println("response"+response_string);

            String[] token;
            token = response_string.split("\\$");

            System.out.println("ddddd"+token.length);
            if(token[0].equals("")){
            }
            else {
                for (int i = 0; i < token.length; i = i+3) {
                    enterData_list = new ArrayList<EnterData>();
                    EnterData temp_enterdata = new EnterData(token[i], token[i+1], token[i+2]);

                    System.out.println("token[0] - "+token[i]);
                    System.out.println("token[1] - "+token[i+1]);
                    System.out.println("token[2] - "+token[i+2]);

                    enterData_list.add(temp_enterdata);
                }
            }

        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void insert_notice_con(NotiData insert_notiData){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/insert_notice.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("title", insert_notiData.getTitle()));
            nameValuePairs.add(new BasicNameValuePair("manager_id", insert_notiData.getAuthor_id()));
            nameValuePairs.add(new BasicNameValuePair("contents", insert_notiData.getContent()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
                if(response_string.equals("T")){
                    insert_notice_flag = true;
                }else{
                    insert_notice_flag = false;
                }
            }

            System.out.println("---Response : "+response_string);

        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void update_notice_con(NotiData update_notiData){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/update_notice.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("notice_id", Integer.toString(update_notiData.getId())));
            nameValuePairs.add(new BasicNameValuePair("new_title", update_notiData.getTitle()));
            nameValuePairs.add(new BasicNameValuePair("manager_id", update_notiData.getAuthor_id()));
            nameValuePairs.add(new BasicNameValuePair("new_contents", update_notiData.getContent()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
                if(response_string.equals("T")){
                    update_notice_flag = true;
                }else{
                    update_notice_flag = false;
                }
            }

            System.out.println("---Response : "+response_string);

        }catch(Exception E){
            E.printStackTrace();
        }
    }

    void delete_notice_con(int notice_id){
        try{

            httpclient=new DefaultHttpClient();
            httppost = new HttpPost("http://14.45.108.71:80/corona_tracker/delete_notice.php"); // php주소 연동
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("index", Integer.toString(notice_id)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            response = httpclient.execute(httppost);

            Scanner scan = new Scanner(response.getEntity().getContent());

            String response_string = "";
            while(scan.hasNext()){
                response_string += scan.nextLine();
                if(response_string.equals("T")){
                    delete_notice_flag = true;
                }else{
                    delete_notice_flag = false;
                }
            }

            System.out.println("---Response : "+response_string);

        }catch(Exception E){
            E.printStackTrace();
        }
    }
}
