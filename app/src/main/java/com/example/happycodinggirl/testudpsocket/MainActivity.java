package com.example.happycodinggirl.testudpsocket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendBtn= (Button) findViewById(R.id.sendBtn);
       // getPhoneIP();
        final EditText sendEdt= (EditText) findViewById(R.id.sendEdt);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        udpSocketSendMSG(sendEdt.getText().toString());
                    }
                }).start();

            }
        });
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

    /*private int int2ip(int i){
        return (i & 0xFF)+"."+((i>>8)&0xFF)+"."+((i>>16)&0xFF)+"."+(i>>24&0xFF
        );
    }*/
    public void udpSocketSendMSG(String sendMsg){
        try {
            Log.v("TAG","=====will=send !!!!!!!!!!!");
            DatagramSocket datagramSocket=new DatagramSocket();
            DatagramPacket sendPacket=new DatagramPacket(sendMsg.getBytes(),sendMsg.getBytes().length, InetAddress.getByName(getPhoneIP()),30000);
            datagramSocket.send(sendPacket);
            Log.v("TAG","======send !!!!!!!!!!!");
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
