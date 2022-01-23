package fr.esiea.esieabot.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;

import fr.esiea.esieabot.R;

//TODO Reecire la class en fragment
public class BtClientActivity extends AppCompatActivity {

    private String mMessageFromServer = "";
    private TextView mMessageTextView;

    private String mDeviceAddress;
    protected CommunicationsTask mBluetoothConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_control);

        // Retrieve the address of the bluetooth device from the BluetoothListDeviceActivity
        Intent newint = getIntent();
        mDeviceAddress = newint.getStringExtra(DeviceListActivity.EXTRA_ADDRESS);

        // Create a connection to this device
        mBluetoothConnection = new CommunicationsTask(this, mDeviceAddress);
        mBluetoothConnection.execute();


        mMessageTextView = (TextView)findViewById(R.id.serverReplyText);

        ImageButton btnUp = (ImageButton) findViewById(R.id.arrow_up);
        ImageButton btnDown = (ImageButton) findViewById(R.id.arrow_down);
        ImageButton btnLeft = (ImageButton) findViewById(R.id.arrow_left);
        ImageButton btnRight = (ImageButton) findViewById(R.id.arrow_right);
        ImageButton btnStop = (ImageButton) findViewById(R.id.control_btn);

        String up = "up";
        String down = "down";
        String left = "left";
        String right = "right";
        String stop = "stop";

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.write(up.getBytes());
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.write(down.getBytes());
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.write(left.getBytes());
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.write(right.getBytes());
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothConnection.write(stop.getBytes());
            }
        });
/*
 while (mBluetoothConnection.available() > 0) {

                        char c = (char)mBluetoothConnection.read();

                        if (c == '.') {

                            if (mMessageFromServer.length() > 0) {
                                mMessageTextView.setText(mMessageFromServer);
                                mMessageFromServer = "";
                            }
                        }
                        else {
                            mMessageFromServer += c;
                        }
 */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothConnection.disconnect();
    }
}


