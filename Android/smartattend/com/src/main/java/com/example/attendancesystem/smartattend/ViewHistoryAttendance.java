package com.example.attendancesystem.smartattend;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewHistoryAttendance extends AppCompatActivity {

    ListView listview;
    ProductListAdapter adapter;
    List<Rows> list;

    String course="";
    String imei="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_attendance);
        listview = (ListView) findViewById(R.id.list);
        list = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        course = (String) extras.get("coursename");
        imei = (String) extras.get("imei");
        logging(course);
        logging(imei);
        getAttendance(course);


    }

    public void getAttendance(String course){

        Log.d("5 ", "------------------");

        final RequestParams params = new RequestParams();
        Log.d("6 ", "------------------");

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("7 ", "------------------");

        client.get("http://54.175.54.121:5004/v1/students/attendance/"+imei+"/"+course , params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    logging("inside 201---------------------");
                    String str = new String(responseBody);
                    logging("STR is: "+str);
                    try {
                        logging("try 1--------");
                        JSONObject obj = new JSONObject(str);
                        logging("try 3--------");

                        Iterator<?> keys = obj.keys();

                        while( keys.hasNext() ) {
                            String key = (String)keys.next();
                            if ( obj.get(key) instanceof JSONObject ) {
                                JSONObject current = (JSONObject)obj.get(key);
                                String date = (String) current.getString("Date");
                                String CheckInTime=(String) current.get("CheckInTime");
                                String CheckOutTime=(String) current.get("CheckOutTime");
                                String mark = (String) current.getString("Marked");
                                logging("Date is "+date);
                                logging("CheckInTime is "+CheckInTime);
                                logging("CheckOutTime is "+CheckOutTime);
                                logging("Marked is "+mark);
                                // list.add(new rows(date, time, mark));
                                list.add(new Rows(date, CheckOutTime, CheckInTime, mark));
                            }
                        }

                        adapter = new ProductListAdapter(getApplicationContext(), list);
                        listview.setAdapter(adapter);
                    }catch(Exception e){
                        logging("EXCEPTION-----");
                    }

                    Log.d("LOGIN ACTIVITY --- ", "IN SUCCESS 200");
                }

                else {


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
        logging("end of courses()");


    }

    private void logging(String s){
        Log.d("ViewHistoryAttendance", s);
    }
}