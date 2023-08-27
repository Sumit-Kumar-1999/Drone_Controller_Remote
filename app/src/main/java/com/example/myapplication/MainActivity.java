package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
//import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {
//    private TextView angleTextView;
//    private TextView powerTextView;
//    private int accel;
    private int DirectionX = 50;
    private int DirectionY = 50;
    private int Left_DirectionX = 50;
    private int Left_DirectionY = 0;
    private int Roll;
    private int Pitch;
    private int Throttle;
    private int Yaw;
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    private InetAddress address;
    private String sendData;
    private int size;
    private String ip;
    private TextView textViewIP;
    private TextView CoordinateRight;
    private TextView CoordinateLeft;
    public JoystickView1 joystickView1;
    public JoystickView2 joystickView2;
    public Button button;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CoordinateRight = findViewById(R.id.coordinate_right);
        joystickView1 = findViewById(R.id.joystick_right);
        joystickView1.setOnJoystickMoveListener(new JoystickView1.OnJoystickMoveListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
//                angleTextView.setText(" " + angle + "°");
//                powerTextView.setText(" " + power + "%");
                CoordinateRight.setText(String.format("Roll:%03d, Pitch:%03d", joystickView1.getNormalizedX(), joystickView1.getNormalizedY()));
                DirectionX = joystickView1.getNormalizedX();
                DirectionY = joystickView1.getNormalizedY();
                if (DirectionX == 0){DirectionX = 1;}
                else if (DirectionX == 100){DirectionX = 99;}
                if (DirectionY == 0){DirectionY = 1;}
                else if (DirectionY == 100){DirectionY = 99;}
            }
        }, JoystickView1.DEFAULT_LOOP_INTERVAL);

        CoordinateLeft = findViewById(R.id.coordinate_left);
        joystickView2 = findViewById(R.id.joystick_left);
        joystickView2.setOnJoystickMoveListener(new JoystickView2.OnJoystickMoveListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
//
                CoordinateLeft.setText(String.format("Yaw:%03d, Throttle:%03d", joystickView2.getNormalizedX(), joystickView2.getNormalizedY()));
                Left_DirectionX = joystickView2.getNormalizedX();
                Left_DirectionY = joystickView2.getNormalizedY();
                if (Left_DirectionX == 0){Left_DirectionX = 1;}
                else if (Left_DirectionX == 100){Left_DirectionX = 99;}
                if (Left_DirectionY == 0){Left_DirectionY = 1;}
                else if (Left_DirectionY == 100){Left_DirectionY = 99;}
            }
        }, JoystickView2.DEFAULT_LOOP_INTERVAL);

        Roll = DirectionX;
        Pitch = DirectionY;
        Throttle = Left_DirectionY;
        Yaw = Left_DirectionX;

        textViewIP = findViewById(R.id.IPAddress);
        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            if (textViewIP != null) {
                ip = textViewIP.getText().toString();
                final Handler handler = new Handler();
                Thread thread = new Thread(() -> {
                    try {
                        while (true) {
                            socket = new DatagramSocket();
                            address = InetAddress.getByName(ip);
                            sendData = String.valueOf((char) DirectionX) + (char) DirectionY + (char) Left_DirectionX + (char) Left_DirectionY;
                            size = sendData.length();
                            socket.setBroadcast(true);
                            sendData = String.valueOf((char) DirectionX) + (char) DirectionY + (char) Left_DirectionX + (char) Left_DirectionY;
                            packet = new DatagramPacket(sendData.getBytes(), size, address, 1221);
                            socket.send(packet);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (socket != null) {
                            socket.close();
                        }
                    }
                });
                thread.start();
//                });
            }
        });
    }
    protected void exitByBackKey(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Do yo want to exit this Application")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        do nothing....
                    }
                }).show();
    }
    @Override
    public void onBackPressed() {
        exitByBackKey();
    }
}