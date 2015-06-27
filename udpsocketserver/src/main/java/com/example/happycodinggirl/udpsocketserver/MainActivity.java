package com.example.happycodinggirl.udpsocketserver;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {


    boolean toWait=true;
    TextView receivedTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receivedTextView= (TextView) findViewById(R.id.receivedMsg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket datagramSocket=new DatagramSocket(30000,InetAddress.getByName(getPhoneIP()));
                    byte[] inInfo=new byte[2048];
                    //    String
                    DatagramPacket inDataPacket=new DatagramPacket(inInfo,inInfo.length);
                    datagramSocket.receive(inDataPacket);
                    startJS();
                    byte[] receivedData=inDataPacket.getData();

                    while(toWait){
                        if (receivedData.length==0){
                            Log.v("TAG","receivedData length is 0");
                            datagramSocket.receive(inDataPacket);

                        }else{
                            Log.v("TAG", "____________________HSHSH receivedMsg");
                            final String receivedString=new String(receivedData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    receivedTextView.setText(receivedString);
                                }
                            });

                        }
                    }
                    datagramSocket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private String getPhoneIP() {
        WifiManager wifiManager= (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // wifiManager.disNetwork(true);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        int ipAddress=wifiInfo.getIpAddress();
        String ip=int2ip(ipAddress);
        Log.v("TAG","---CLIENT IP IS "+ip);
        return ip;

    }
    public String int2ip(int i){
        return (i&0xFF)+"."+((i>>8)&0xFF)+"."+((i>>16)&0xFF)+"."+(i>>24&0xFF);
    }
    private void startJS() {
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.v("TAG","----toWait false ");
                toWait=false;
            }
        },60*1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
