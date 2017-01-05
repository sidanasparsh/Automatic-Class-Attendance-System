package com.example.attendancesystem.smartattend;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.provider.Settings;
import android.provider.Settings.System;
//import android.net.http.RequestQueue;
import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

import static java.net.Proxy.Type.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    EditText etFullName;
    EditText etCourseNumber;
    EditText etSJSUID;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        logging("onCreate");
    }

    public void register(View view) {
        logging("In register function");
        etFullName = (EditText) findViewById(R.id.etFullName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCourseNumber = (EditText) findViewById(R.id.etCourseNumber);
        etSJSUID = (EditText) findViewById(R.id.etSJSUID);
        String username = etFullName.getText().toString();
        String password = etPassword.getText().toString();
        String course = etCourseNumber.getText().toString();
        String id = etSJSUID.getText().toString();

        if (username.length() == 0 || password.length() == 0 || course.length() == 0 || id.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_LONG).show();
            logging("Blank Field");
        } else {
            if (id.length() != 0 && id.contains("[a-zA-Z]+")) {
                Toast.makeText(getApplicationContext(), "Please enter numbers only in SJSU ID.", Toast.LENGTH_LONG).show();
            } else {
                logging("Calling invokeWS()");
                TelephonyManager telman = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telman.getDeviceId();
                String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                int id2 = Integer.parseInt(id);
                invokeWS(username, password, course, id2, imei);
            }
        }
        logging("Register() ending");
    }

    public void invokeWS(final String username, final String password, final String course, final int id, final String androidID) {

        logging("In invokeWS()");
        final RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);

//        logging("params username");

        AsyncHttpClient client = new AsyncHttpClient();
        logging("params async http client calling now");
        client.post("http://54.175.54.121:5004/v1/students/" + username + "/" + password + "/" + course + "/" + id + "/" + androidID, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (statusCode == 208) {
                    logging("success 208");
                    Toast.makeText(getApplicationContext(), "Student ID already registered. ", Toast.LENGTH_LONG).show();
                }

                if (statusCode == 200) {
                    logging("inside 201---------------------");
                    Toast.makeText(getApplicationContext(), "Thanks for registering ", Toast.LENGTH_LONG).show();
                    String str = new String(responseBody);
                    logging("STR is: "+str);
                    try {
                        logging("try 1--------");
                        JSONObject jsonObj = new JSONObject(str);
                        logging("try 2--------");

                        // Getting JSON Array node
                        JSONObject details = jsonObj.getJSONObject("details");
                        logging("try 3--------");
                        String androidID = details.getString("androidID");
                        logging("try 4--------");
                        logging("ANDROID "+androidID);
                        logging("try 5--------");
                        String coursename = details.getString("coursename");
                        logging("try 6--------");
                        logging("course "+coursename);
                        logging("try 7--------");
                        String fullname = details.getString("fulllname");
                        logging("try 8--------");
                        logging("fullname "+fullname);
                        logging("try 9--------");
                        String sjsuid = details.getString("sjsuid");
                        logging("try 10--------");



                        logging("sjsuid "+sjsuid);
                        //logging(details);
                    }catch(Exception e){
                        logging("exception");
                    }
                    logging("successful 201");
                    Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);

                    loginIntent.putExtra("username", username);
                    loginIntent.putExtra("course", course);
                    loginIntent.putExtra("sjsuid", password);

                    startActivity(loginIntent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void logging(String s) {
        Log.d("Register Activity", "Status: " + s);
    }

}


//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.telephony.TelephonyManager;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//
//public class Register extends AppCompatActivity {
//
//    public String device_id;
//    public EditText Stdid;
//    public String StdIdStr;
//    private ProgressDialog progress;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        device_id = tm.getDeviceId();
//
//        Toast.makeText(this,"IMEI: "+device_id,Toast.LENGTH_LONG).show();
//
//    }
//
//
//    public  void regis(View view){
//        Stdid = (EditText)findViewById(R.id.StudentId);
//        StdIdStr = Stdid.getText().toString();
//       // device_id = "355884056651846";
//        markAttendance(device_id,StdIdStr);
//
//    }
//
//    private void markAttendance(String device_id , String Stdid){
//        new PostClass(this).execute();
//
//    }
//
/////Mahita's Code Starts
//
//    private class PostClass extends AsyncTask<String, Void, String> {
//
//        private final Context context;
//
//        public PostClass(Context c){
//
//            this.context = c;
////            this.error = status;
////            this.type = t;
//        }
//        protected void onPreExecute(){
//            progress= new ProgressDialog(this.context);
//            progress.setMessage("Loading");
//            // progress.show();
//        }
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                final TextView outputView = (TextView) findViewById(R.id.showOutput);
//                URL url = new URL("http://54.67.55.103:3000/api/registerstudent/");
//
//                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//                String json="";
//                String urlParameters1 = StdIdStr;
//                String urlParameters2=device_id;
//
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
//                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
//                connection.setDoOutput(true);
//
//                JSONObject jsonObject=new JSONObject();
//                jsonObject.accumulate("studentId",urlParameters1);
//                jsonObject.accumulate("imei",urlParameters2);
//                //jsonObject.put(urlParameters2,true);
//                //jsonObject.accumulate(urlParameters1,params[1]);
//                // jsonObject.accumulate(urlParameters2,params[2]);
//                json=jsonObject.toString();
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                //dStream.writeBytes(urlParameters1);
//                // dStream.writeBytes(urlParameters2);
//                dStream.write(json.getBytes());
//                dStream.flush();
//                dStream.close();
//                String responseString = connection.getResponseMessage();
//                System.out.println("\nSending 'POST' request to URL : " + url);
//                System.out.println("Post parameters : " + urlParameters1);
//                System.out.println("Post parameters : " + urlParameters2);
//                System.out.println("Response String : " + responseString);
//                final StringBuilder output = new StringBuilder("Request URL " + url);
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line = "";
//                StringBuilder responseOutput = new StringBuilder();
//
//                System.out.println("output===============" + br);
//                while((line = br.readLine()) != null ) {
//                    responseOutput.append(line);
//                }
//                br.close();
//                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//                Register.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        outputView.setText(output);
//                        progress.dismiss();
//                    }
//                });
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//        protected void onPostExecute() {
//            progress.dismiss();
//        }
//    }
//
//}


