package com.example.attendancesystem.smartattend;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

import static java.net.Proxy.Type.HTTP;

import org.apache.http.ExceptionLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import okhttp3.HttpUrl;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Request;
import org.json.JSONObject;
import okhttp3.OkHttpClient;


public class ViewHistory extends AppCompatActivity {

    String imei="";
    ListView lv;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        Bundle extras = getIntent().getExtras();
        imei=extras.getString("imei");
        Log.d(">>>>>>>>>>sjsuid2 is ", imei);


        Log.d("calling getCourses() ", "------------------");

        getCourses();
    }

    private void getCourses(){
        Log.d("5 ", "------------------");

        final RequestParams params = new RequestParams();
        Log.d("6 ", "------------------");

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("7 ", "------------------");

        client.get("http://54.175.54.121:5004/v1/students/courses/"+imei,params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    logging("inside 201---------------------");
                    String str = new String(responseBody);
                    logging("STR is: "+str);
                    try {
                        logging("try 1--------");
                        JSONObject course = new JSONObject(str);
                        logging("try 3--------");
                        String courses = course.getString("Courses");
                        logging("Courses are: "+courses);

                        String[] splitting = courses.split(";");
                        setMethod1(splitting);

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
    public void setMethod1(final String[] courses){


        if(courses!=null || courses.length!=0){
            adapter = new ArrayAdapter<String>(
                    this,R.layout.layout,courses);
            Log.d("2 ", "------------------");

            lv = (ListView) findViewById(R.id.mylist);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String a = (String) adapterView.getItemAtPosition(i);
                    logging("Item clicked is "+courses[i]);

                    Intent intent = new Intent(getApplicationContext(), ViewHistoryAttendance.class);
                    intent.putExtra("coursename", courses[i]);
                    intent.putExtra("imei", imei);

                    startActivity(intent);
                }
            });
            Log.d("3 ", "------------------");

            Log.d("4 ", "------------------");
        }
    };

//    lv.setOnCLick

    public void logging(String s) {
        Log.d("ViewHistory Activity", "Status: " + s);
    }
}