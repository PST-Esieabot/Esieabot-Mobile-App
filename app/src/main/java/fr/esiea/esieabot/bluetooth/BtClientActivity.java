package fr.esiea.esieabot.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import fr.esiea.esieabot.R;

//TODO Reecire la class en fragment
public class BtClientActivity extends AppCompatActivity {

    private String mMessageFromServer = "";

    private TextView mMessageTextView;
    private SeekBar mSpeedSeekBar;

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
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte up = 00;
                mBluetoothConnection.write(up);
            }
        });

        mSpeedSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser==true) {

                    for (byte b : String.valueOf(progress).getBytes()) {
                        mBluetoothConnection.write(b);
                    }
                    mBluetoothConnection.write((byte)'.');

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
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothConnection.disconnect();
    }
}

