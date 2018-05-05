package com.us47codex.tictactoe;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

/**
 * Created by admin on 18/2/17.
 */

public class GameBoardActivity extends Activity implements View.OnClickListener {

    private Context context;

    public ImageView button1, button2, button3, button4, button5, button6, button7, button8, button9;
    public TextView txvRightCount, txvCrossCount;
    private TextView txvPlayerOneName;
    private TextView txvPlayerTwoName;
    private RelativeLayout relPlayerOne;
    private RelativeLayout relPlayerTwo;
    private String strPlayerOne;
    private String strPlayerTwo;
    private boolean isSoundEnable;
    private View incResult;
    private TextView txvPlayerName;
    private TextView txvNewGame;
    private TextView txvHome;
    private ImageView imgCrown;
    public boolean isPlayerOneTurn = false;
    public boolean isContinue = true;
    public int[][] table = new int[3][3];
    public int rightCount = 0;
    public int crossCount = 0;
    public int playerOne = 1;
    public int playerTwo = 2;

    public MediaPlayer tickSound = null;

    public boolean isSystemTurn = false;

    private AdView adView;

    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final int MESSAGE_CONNECTION_STATUS= 1;
    public static final int MESSAGE_RECEIVE = 2;
    public static final int MESSAGE_SEND = 3;

    private ChatServiceGame chatServiceGame = null;

    private String connectedDeviceName = null;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private StringBuffer outStringBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        context = GameBoardActivity.this;

        bindView();
        createAd();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    private void bindView() {
        button1 = (ImageView) findViewById(R.id.button1);
        button2 = (ImageView) findViewById(R.id.button2);
        button3 = (ImageView) findViewById(R.id.button3);
        button4 = (ImageView) findViewById(R.id.button4);
        button5 = (ImageView) findViewById(R.id.button5);
        button6 = (ImageView) findViewById(R.id.button6);
        button7 = (ImageView) findViewById(R.id.button7);
        button8 = (ImageView) findViewById(R.id.button8);
        button9 = (ImageView) findViewById(R.id.button9);

        relPlayerOne = (RelativeLayout) findViewById(R.id.relPlayerOne);
        txvPlayerOneName = (TextView) findViewById(R.id.txvPlayerOneName);
        txvRightCount = (TextView) findViewById(R.id.txvRightCount);

        relPlayerTwo = (RelativeLayout) findViewById(R.id.relPlayerTwo);
        txvPlayerTwoName = (TextView) findViewById(R.id.txvPlayerTwoName);
        txvCrossCount = (TextView) findViewById(R.id.txvCrossCount);

        incResult = findViewById(R.id.incResult);
        txvPlayerName = (TextView) findViewById(R.id.txvPlayerName);
        txvNewGame = (TextView) findViewById(R.id.txvNewGame);
        txvHome = (TextView) findViewById(R.id.txvHome);
        imgCrown = (ImageView) findViewById(R.id.imgCrown);

        txvNewGame.setOnClickListener(this);
        txvHome.setOnClickListener(this);

        strPlayerOne = getIntent().getStringExtra(context.getString(R.string.intentPlayerOneName));
        strPlayerTwo = getIntent().getStringExtra(context.getString(R.string.intentPlayerTwoName));
        isSoundEnable = getIntent().getBooleanExtra(context.getString(R.string.intentIsSoundEnable), false);

        if (isSoundEnable) {
            tickSound = MediaPlayer.create(this, R.raw.tick);
        }

        txvPlayerOneName.setText(strPlayerOne);
        txvPlayerTwoName.setText(strPlayerTwo);


        ((TextView) findViewById(R.id.discoverable)).setOnClickListener(this);
        ((TextView) findViewById(R.id.secure_connect_scan)).setOnClickListener(this);
        ((TextView) findViewById(R.id.insecure_connect_scan)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent serverIntent = null;
        byte[] send;
        String message;
        switch (v.getId()) {

            case R.id.button1:
                message = "1";
                System.out.println("==== button1 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button1, 0, 0);
                break;

            case R.id.button2:
                message = "2";
                System.out.println("==== button2 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button2, 0, 1);
                break;

            case R.id.button3:
                message = "3";
                System.out.println("==== button3 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button3, 0, 2);
                break;

            case R.id.button4:
                message = "4";
                System.out.println("==== button4 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button4, 1, 0);
                break;

            case R.id.button5:
                message = "5";
                System.out.println("==== button5 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button5, 1, 1);
                break;

            case R.id.button6:
                message = "6";
                System.out.println("==== button6 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button6, 1, 2);
                break;

            case R.id.button7:
                message = "7";
                System.out.println("==== button7 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button7, 2, 0);
                break;

            case R.id.button8:
                message = "8";
                System.out.println("==== button8 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button8, 2, 1);
                break;

            case R.id.button9:
                message = "9";
                System.out.println("==== button9 ====");
                send = message.getBytes();
                chatServiceGame.write(send);
//                mark(button9, 2, 2);
                break;

            case R.id.txvNewGame:
                newGame();
                break;

            case R.id.txvHome:
                Intent intent = new Intent(GameBoardActivity.this, GameBoardActivity.class);
                startActivity(intent);
                finish();
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

    public void systemTurn() {
        Random rand = new Random();
        int n = rand.nextInt(9);

        if (n == 0) {
            mark(button1, 0, 0);
        } else if (n == 1) {
            mark(button2, 0, 1);
        } else if (n == 2) {
            mark(button3, 0, 2);
        } else if (n == 3) {
            mark(button4, 1, 0);
        } else if (n == 4) {
            mark(button5, 1, 1);
        } else if (n == 5) {
            mark(button6, 1, 2);
        } else if (n == 6) {
            mark(button7, 2, 0);
        } else if (n == 7) {
            mark(button8, 2, 1);
        } else if (n == 8) {
            mark(button9, 2, 2);
        }

        isSystemTurn = false;
    }

    public void blueMark(String i) {

        System.out.println("==== blueMark ==== " + i);
        switch (i) {

            case "1":
                mark(button1, 0, 0);
                break;

            case "2":
                mark(button2, 0, 1);
                break;

            case "3":
                mark(button3, 0, 2);
                break;

            case "4":
                mark(button4, 1, 0);
                break;

            case "5":
                mark(button5, 1, 1);
                break;

            case "6":
                mark(button6, 1, 2);
                break;

            case "7":
                mark(button7, 2, 0);
                break;

            case "8":
                mark(button8, 2, 1);
                break;

            case "9":
                mark(button9, 2, 2);
                break;

        }
    }

    // For mark

    public void mark(ImageView imageView, int i, int j) {
        System.out.println("===== mark =====");
        if (isContinue) {
            System.out.println("===== mark =====isContinue");

            if (imageView.getDrawable() == null) {
                System.out.println("===== mark =====isContinue ===== isPlayerOneTurn === " + isPlayerOneTurn);

                if (isSoundEnable) {
                    tickSound.start();
                }
                if (isPlayerOneTurn) {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.right));
                    imageView.setPadding(20, 20, 20, 20);
                    relPlayerTwo.setBackgroundColor(getResources().getColor(R.color.colorResultBG));
                    relPlayerOne.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                    table[i][j] = playerOne;
                    isPlayerOneTurn = false;
//                    systemTurn();
                } else if (!isPlayerOneTurn) {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.cross));
                    relPlayerOne.setBackgroundColor(getResources().getColor(R.color.colorResultBG));
                    relPlayerTwo.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                    table[i][j] = playerTwo;
                    isPlayerOneTurn = true;
                }
            }
            checkResult();
        }
    }

    //For Checking result
    public void checkResult() {
        boolean isEmpty = false;

        //for row check
        for (int i = 0; i < 3; i++) {
            if (table[i][0] == playerOne && table[i][1] == playerOne && table[i][2] == playerOne) {
                showResult(playerOne);
                rightCount++;
                txvRightCount.setText(String.valueOf(rightCount));
                isContinue = false;
                return;
            } else if (table[i][0] == playerTwo && table[i][1] == playerTwo && table[i][2] == playerTwo) {
                showResult(playerTwo);
                crossCount++;
                txvCrossCount.setText(String.valueOf(crossCount));
                isContinue = false;
                return;
            }
        }

        // for column check
        for (int i = 0; i < 3; i++) {
            if (table[0][i] == playerOne && table[1][i] == playerOne && table[2][i] == playerOne) {
                showResult(playerOne);
                rightCount++;
                txvRightCount.setText(String.valueOf(rightCount));
                isContinue = false;
                return;
            } else if (table[0][i] == playerTwo && table[1][i] == playerTwo && table[2][i] == playerTwo) {
                showResult(playerTwo);
                crossCount++;
                txvCrossCount.setText(String.valueOf(crossCount));
                isContinue = false;
                return;
            }
        }

        //for diagonal check
        if (table[0][0] == playerOne && table[1][1] == playerOne && table[2][2] == playerOne) {
            showResult(playerOne);
            rightCount++;
            txvRightCount.setText(String.valueOf(rightCount));
            isContinue = false;
            return;
        } else if (table[0][0] == playerTwo && table[1][1] == playerTwo && table[2][2] == playerTwo) {
            showResult(playerTwo);
            crossCount++;
            txvCrossCount.setText(String.valueOf(crossCount));
            isContinue = false;
            return;
        } else if (table[0][2] == playerOne && table[1][1] == playerOne && table[2][0] == playerOne) {
            showResult(playerOne);
            rightCount++;
            txvRightCount.setText(String.valueOf(rightCount));
            isContinue = false;
            return;
        } else if (table[0][2] == playerTwo && table[1][1] == playerTwo && table[2][0] == playerTwo) {
            showResult(playerTwo);
            crossCount++;
            txvCrossCount.setText(String.valueOf(crossCount));
            isContinue = false;
            return;
        }

        // for Draw
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (table[i][j] == 0) {
                    isEmpty = true;
                }
            }
        }

        if (!isEmpty && isContinue) {
            showResult(0);
            isContinue = false;
            return;
        }
    }

    //For New Game
    public void newGame() {
        incResult.setVisibility(View.GONE);

        isContinue = true;
        table = new int[3][3];

        button1.setImageDrawable(null);
        button2.setImageDrawable(null);
        button3.setImageDrawable(null);
        button4.setImageDrawable(null);
        button5.setImageDrawable(null);
        button6.setImageDrawable(null);
        button7.setImageDrawable(null);
        button8.setImageDrawable(null);
        button9.setImageDrawable(null);
    }

    //For showing result
    public void showResult(int player) {

        incResult.setVisibility(View.VISIBLE);
        MediaPlayer winning = MediaPlayer.create(this, R.raw.winning);

        if (player == 0) {
            imgCrown.setVisibility(View.GONE);
            txvPlayerName.setText(getResources().getString(R.string.gameDrawn));

        } else if (player == playerOne) {
            if (isSoundEnable) {
                winning.start();
            }

            imgCrown.setVisibility(View.VISIBLE);
            txvPlayerName.setText(strPlayerOne);
            isPlayerOneTurn = true;
            relPlayerOne.setBackgroundColor(getResources().getColor(R.color.colorResultBG));
            relPlayerTwo.setBackgroundColor(getResources().getColor(R.color.colorTransparent));

        } else if (player == playerTwo) {
            if (isSoundEnable) {
                winning.start();
            }
            imgCrown.setVisibility(View.VISIBLE);
            txvPlayerName.setText(strPlayerTwo);
            isPlayerOneTurn = false;
            relPlayerTwo.setBackgroundColor(getResources().getColor(R.color.colorResultBG));
            relPlayerOne.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        }
    }

    //For create an Ad
    public void createAd() {
        MobileAds.initialize(this, context.getString(R.string.addmobAppId));
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });


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
        chatServiceGame.connect(device, secure);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (chatServiceGame == null)
                setupChat();
        }
    }

    private void setupChat() {
//        chatArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
//        lvMainChat.setAdapter(chatArrayAdapter);
//
        chatServiceGame = new ChatServiceGame(this, handler);
//
        outStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (chatServiceGame != null) {
            if (chatServiceGame.getState() == ChatServiceGame.STATE_NONE) {
                chatServiceGame.start();
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
        if (chatServiceGame != null)
            chatServiceGame.stop();
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case ChatServiceGame.STATE_CONNECTED:
                            Toast.makeText(GameBoardActivity.this, getString(R.string.title_connected_to, connectedDeviceName), Toast.LENGTH_LONG).show();
                            break;

                        case ChatServiceGame.STATE_CONNECTING:
                            Toast.makeText(GameBoardActivity.this, R.string.title_connecting, Toast.LENGTH_LONG).show();
                            break;

                        case ChatServiceGame.STATE_LISTEN:
                        case ChatServiceGame.STATE_NONE:
                            Toast.makeText(GameBoardActivity.this, R.string.title_not_connected, Toast.LENGTH_LONG).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    String writeMessage = new String(writeBuf);

                    Toast.makeText(GameBoardActivity.this, "ME => " + writeMessage, Toast.LENGTH_LONG).show();
                    System.out.println("==== MESSAGE_WRITE == ME => " + writeMessage);

                    blueMark(writeMessage);

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);

                    Toast.makeText(GameBoardActivity.this, connectedDeviceName + " => " + readMessage, Toast.LENGTH_LONG).show();
                    System.out.println("==== MESSAGE_READ ==" + connectedDeviceName + " => " + readMessage);

                    blueMark(readMessage);
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

    private Handler myhandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CONNECTION_STATUS:
                    switch (msg.arg1) {
                        case ChatServiceGame.STATUS_CONNECTED:
                            Toast.makeText(GameBoardActivity.this, getString(R.string.title_connected_to, connectedDeviceName), Toast.LENGTH_LONG).show();
                            break;

                        case ChatServiceGame.STATUS_CONNECTING:
                            Toast.makeText(GameBoardActivity.this, R.string.title_connecting, Toast.LENGTH_LONG).show();
                            break;

                        case ChatServiceGame.STATUS_WAITING:
                        case ChatServiceGame.STATUS_NOT_CONNECTED:
                            Toast.makeText(GameBoardActivity.this, R.string.title_not_connected, Toast.LENGTH_LONG).show();
                            break;
                    }
                    break;
                case MESSAGE_SEND:
                    byte[] writeBuf = (byte[]) msg.obj;

                    String writeMessage = new String(writeBuf);

                    Toast.makeText(GameBoardActivity.this, "ME => " + writeMessage, Toast.LENGTH_LONG).show();
                    System.out.println("==== MESSAGE_WRITE == ME => " + writeMessage);

                    blueMark(writeMessage);

                    break;
                case MESSAGE_RECEIVE:
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);

                    Toast.makeText(GameBoardActivity.this, connectedDeviceName + " => " + readMessage, Toast.LENGTH_LONG).show();
                    System.out.println("==== MESSAGE_READ ==" + connectedDeviceName + " => " + readMessage);

                    blueMark(readMessage);
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
