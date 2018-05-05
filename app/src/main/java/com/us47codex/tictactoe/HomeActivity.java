package com.us47codex.tictactoe;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class HomeActivity extends Activity implements TextView.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;
    private EditText edtPlayerOne;
    private EditText edtPlayerTwo;
    private ImageView imgInfo;
    private RelativeLayout relInfo;
    private TextView txtStartGame;
    private TextView txtExit;
    private ToggleButton tgbSound;
    private String strPlayerOne;
    private String strPlayerTwo;
    private boolean isSoundEnable;


    private BluetoothAdapter bluetoothAdapter;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    private ChatService chatService = null;

    private String connectedDeviceName = null;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private StringBuffer outStringBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = HomeActivity.this;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bindView();
    }

    private void bindView() {
        edtPlayerOne = (EditText) findViewById(R.id.edtPlayerOne);
        edtPlayerTwo = (EditText) findViewById(R.id.edtPlayerTwo);
        txtStartGame = (TextView) findViewById(R.id.txtStartGame);
        txtExit = (TextView) findViewById(R.id.txtExit);
        imgInfo = (ImageView) findViewById(R.id.imgInfo);
        relInfo = (RelativeLayout) findViewById(R.id.relInfo);
        tgbSound = (ToggleButton) findViewById(R.id.tgbSound);

        txtStartGame.setOnClickListener(this);
        txtExit.setOnClickListener(this);
        imgInfo.setOnClickListener(this);
        ((TextView) findViewById(R.id.discoverable)).setOnClickListener(this);
        ((TextView) findViewById(R.id.secure_connect_scan)).setOnClickListener(this);
        ((TextView) findViewById(R.id.insecure_connect_scan)).setOnClickListener(this);

        tgbSound.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent serverIntent = null;
        switch (view.getId()) {
            case R.id.txtStartGame:
                if (edtPlayerOne.getText().toString().trim().equals("")) {
                    strPlayerOne = "Player 1";
                } else {
                    strPlayerOne = edtPlayerOne.getText().toString();
                }
                if (edtPlayerTwo.getText().toString().trim().equals("")) {
                    strPlayerTwo = "Player 2";
                } else {
                    strPlayerTwo = edtPlayerTwo.getText().toString();
                }
                Intent intent = new Intent(context, GameBoardActivity.class);
                intent.putExtra(context.getString(R.string.intentPlayerOneName), strPlayerOne);
                intent.putExtra(context.getString(R.string.intentPlayerTwoName), strPlayerTwo);
                intent.putExtra(context.getString(R.string.intentIsSoundEnable), isSoundEnable);
                startActivity(intent);

                break;

            case R.id.txtExit:
                finish();
                break;

            case R.id.imgInfo:
//                relInfo.setVisibility((relInfo.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE);

                String message = edtPlayerOne.getText().toString();
                if (chatService.getState() != ChatService.STATE_CONNECTED) {
                    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (message.length() > 0) {
                    byte[] send = message.getBytes();
                    chatService.write(send);

                    outStringBuffer.setLength(0);
                    edtPlayerTwo.setText(outStringBuffer);
                }

                break;

            case R.id.secure_connect_scan:
                serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                break;

            case R.id.insecure_connect_scan:
                serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                break;

            case R.id.discoverable:
                ensureDiscoverable();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            isSoundEnable = true;
        } else {
            isSoundEnable = false;
        }
    }

    private void ensureDiscoverable() {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChat();
                } else {
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(DeviceListActivity.DEVICE_ADDRESS);
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        chatService.connect(device, secure);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (chatService == null)
                setupChat();
        }
    }

    private void setupChat() {
//        chatArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
//        lvMainChat.setAdapter(chatArrayAdapter);
//
        chatService = new ChatService(this, handler);
//
        outStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (chatService != null) {
            if (chatService.getState() == ChatService.STATE_NONE) {
                chatService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatService != null)
            chatService.stop();
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case ChatService.STATE_CONNECTED:
                            Toast.makeText(HomeActivity.this, getString(R.string.title_connected_to, connectedDeviceName), Toast.LENGTH_LONG).show();
                            break;

                        case ChatService.STATE_CONNECTING:
                            Toast.makeText(HomeActivity.this, R.string.title_connecting, Toast.LENGTH_LONG).show();
                            break;

                        case ChatService.STATE_LISTEN:
                        case ChatService.STATE_NONE:
                            Toast.makeText(HomeActivity.this, R.string.title_not_connected, Toast.LENGTH_LONG).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    String writeMessage = new String(writeBuf);

                    Toast.makeText(HomeActivity.this, "ME => " + writeMessage, Toast.LENGTH_LONG).show();
                    System.out.println("==== MESSAGE_WRITE == ME => " + writeMessage);
                    edtPlayerOne.setText(writeMessage);

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);

                    Toast.makeText(HomeActivity.this, connectedDeviceName + " => " + readMessage, Toast.LENGTH_LONG).show();
                    System.out.println("==== MESSAGE_READ ==" + connectedDeviceName + " => " + readMessage);
                    edtPlayerTwo.setText(readMessage);

                    break;
                case MESSAGE_DEVICE_NAME:

                    connectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + connectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
            return false;
        }
    });
}
