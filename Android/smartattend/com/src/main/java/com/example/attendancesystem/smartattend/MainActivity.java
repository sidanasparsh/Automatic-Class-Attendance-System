package com.example.attendancesystem.smartattend;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
{
    private ProgressDialog progress;
    public long token; //  http://54.67.55.103:3000/api/getclasstoken/1/
    public String imei;
    public String showtoken;
    public String server_url;
    public URL url;
    BluetoothSocket BTsocket;
    BluetoothDevice BTdevice = null;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    ///SENDBTN
    public void sendBtMsg(String msg2send) {
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        Log.d("IN Func", uuid.toString());

        try {
            //Log.d("IN TRY---------------",uuid.toString());
            // BTsocket = BTdevice.createInsecureRfcommSocketToServiceRecord(uuid);
            BTsocket = BTdevice.createRfcommSocketToServiceRecord(uuid);
            // Method m = BTdevice.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
            Log.d("Connected to : ", BTdevice.getName());
            //  BTsocket = (BluetoothSocket) m.invoke(BTdevice, 1);

            if (!BTsocket.isConnected() & BTsocket != null) {
                BTsocket.connect();
            }

            //String msg = msg2send;
            //msg += "\n";
            OutputStream mmOutputStream = BTsocket.getOutputStream();
            mmOutputStream.write(msg2send.getBytes());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    ///SENDBTN ENDS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TelephonyManager telman = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telman.getDeviceId();

        final Handler handler = new Handler();

        //final TextView myLabel = (TextView) findViewById(R.id.btResult);
        final Button tempButton = (Button) findViewById(R.id.rasp);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final class workerThread implements Runnable {

            private String BTmessage;

            public workerThread(String BTMsg) {
                BTmessage = BTMsg;
                Log.d("*Initializing Thread*", BTmessage);
            }

            public void run() {
                Log.d("***I am running***", BTmessage);
                sendBtMsg(BTmessage);
                while (!Thread.currentThread().isInterrupted()) {
                    int bytesAvailable;
                    boolean workDone = false;

                    try {


                        final InputStream mmInputStream;
                        mmInputStream = BTsocket.getInputStream();
                        bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {

                            byte[] packetBytes = new byte[bytesAvailable];
                            //Log.e("Aquarium recv bt", "bytes available");
                            byte[] readBuffer = new byte[1024];
                            mmInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    //The variable data now contains our full command
                                    handler.post(new Runnable() {
                                        public void run() {
                                            //myLabel.setText(data);
                                            token = Long.parseLong(data);
                                            Toast.makeText(MainActivity.this, "Token Received: " + token, Toast.LENGTH_LONG).show();
                                        }
                                    });

                                    workDone = true;
                                    break;


                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }

                            if (workDone == true) {
                                BTsocket.close();
                                break;
                            }

                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

        }
        ;

        tempButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on temp button click

                (new Thread(new workerThread("temp"))).start();

            }
        });


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("raspberrypi")) //Note, you will need to change this to match the name of your device
                {
                    Log.e("Connected to: ", device.getName());
                    BTdevice = device;
                    break;
                }
            }
        }


    }


    public void checkIn(View view) {
        //TODO
        server_url = "http://54.175.54.121:5004/v1/attendence/checkIN/";
        //  imei ="355884056651846"; //For Testing
        new PostClass(this).execute();
    }

    public void checkOut(View view) {
        server_url = "http://54.175.54.121:5004/v1/attendence/checkOUT/";
        //  imei ="355884056651846"; //FOr Testing
        new PostClass(this).execute();
    }

    private class PostClass extends AsyncTask<String, Void, String> {

        private final Context context;

        public PostClass(Context c) {

            this.context = c;
//            this.error = status;
//            this.type = t;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context);
            progress.setMessage("Loading");
            // progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // final TextView outputView = (TextView) findViewById(R.id.showOutput);
                url = new URL(server_url);
                //final RequestParams params = new RequestParams();
                //params.put("imei", imei);
                //params.put("token", token);




                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String json = "";
                String urlParameters1 = imei;
                long urlParameters2 = token;

                connection.setRequestMethod("PUT");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("imei", urlParameters1);
                jsonObject.accumulate("token", urlParameters2);
                //jsonObject.put(urlParameters2,true);
                //jsonObject.accumulate(urlParameters1,params[1]);
                // jsonObject.accumulate(urlParameters2,params[2]);
                json = jsonObject.toString();
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                //dStream.writeBytes(urlParameters1);
                // dStream.writeBytes(urlParameters2);
                dStream.write(json.getBytes());
                dStream.flush();
                dStream.close();
                String responseString = connection.getResponseMessage();
                System.out.println("\nSending 'PUT' request to URL : " + url);
                System.out.println("Post parameters imei: " + urlParameters1);
                System.out.println("Post parameters token: " + urlParameters2);
                System.out.println("Response String : " + responseString);
                final StringBuilder output = new StringBuilder("Request URL " + url);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                System.out.println("***Output***  " + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // outputView.setText(output);
                        showtoken = output.toString();
                        for(int i=0; i<2; i++) {
                            Toast.makeText(MainActivity.this, showtoken, Toast.LENGTH_LONG).show();
                        }
                            progress.dismiss();
                        token = 00000;
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void history_courses(View view) {
        Log.d("Home Activity", "in hostory_courses()");

        Log.d("histpry courses", "bbefore changing intetn");
        Intent newIntent = new Intent(getApplicationContext(), ViewHistory.class);
        Log.d("histpry courses", "aaa changing intetn");

        Log.d("histpry courses", "bbb changing intetn");

        TelephonyManager telman = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telman.getDeviceId();
        newIntent.putExtra("imei", imei);
        Log.d("histpry courses", "ccc changing intetn");
        startActivity(newIntent);
        Log.d("histpry courses", "ddd changing intetn");
        Log.d("histpry courses", "end-------");

    }


}
