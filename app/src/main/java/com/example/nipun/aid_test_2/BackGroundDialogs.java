package com.example.nipun.aid_test_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by nipun on 31/12/14.
 */


//public class BackGroundDialogs extends Activity {
//
//    private static final String DISMISS_DIALOG = "Dismiss";
//    public BroadcastReceiver receiver;
//    public AlertDialog mAlert;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // custom theme with
//        // no borders
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(DISMISS_DIALOG);// we can dismiss it via an
//        // intent if we choose
//        receiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, final Intent intent) {
//                // do something based on the intent's action
//                if (context == null) return;
//                if (intent.getAction().equals(DISMISS_DIALOG)) {
//                    finish();
//                }
//            }
//        };
//        registerReceiver(receiver, filter);
//    }
//
//    /**
//     * @brief Shows an alert message using a Dialog window.
//     * @param reason
//     *            :the message you wish to display in the alert
//     */
//    public void showAlert(final String reason) {
//        mAlert = new AlertDialog.Builder(this).create();
//        mAlert.setCancelable(false);
//        TextView Msg_tv = new TextView(this);
//        Msg_tv.setTypeface(null, Typeface.BOLD);
//        Msg_tv.setTextSize(16.0f);
//        Msg_tv.setText(reason);
//        Msg_tv.setGravity(Gravity.CENTER_HORIZONTAL);
//        mAlert.setView(Msg_tv);
//        /*mAlert.setButton(0, "ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });*/
//        mAlert.show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Bundle extras = getIntent().getExtras();
//        String reason = extras.getString(Intent.EXTRA_TEXT);
//        if (reason.equalsIgnoreCase("DISMISS")) finish();
//        else showAlert(reason);// invoke the new dialog to show
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mAlert != null) if (mAlert.isShowing()) mAlert.dismiss();
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(receiver);
//    }
//}

public class BackGroundDialogs extends Activity implements View.OnClickListener {

    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private Button startB;
    public TextView text;
    private final long startTime = 10 * 1000;
    private final long interval = 1 * 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        startB = (Button) this.findViewById(R.id.button);
        startB.setOnClickListener(this);
        text = (TextView) this.findViewById(R.id.timer);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        text.setText(text.getText() + String.valueOf(startTime / 1000));
        countDownTimer.start();
        timerHasStarted = true;
    }

    @Override
    public void onClick(View v) {
        Log.d("NOTIFY", startB.getText().toString() );
        if( startB.getText().toString().equals("CLOSE") ) {
            this.finish();
        }
        else {

            countDownTimer.cancel();
            timerHasStarted = false;
            startB.setText("CLOSE");
            text.setTextSize(20);
            text.setText("NOTIFICATION NOT SENT");
        /*if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
            startB.setText("STOP");
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
            startB.setText("RESTART");
        }*/
        }

    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            if( timerHasStarted == true ) {

                text.setTextSize(20);
                text.setText("NOTIFICATION SENT");
                startB.setText( "CLOSE" );

                //Timer is finished put notification here
                NotifyUser.getNotifyUser().allNotify( );


            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text.setText("" + millisUntilFinished / 1000);
        }
    }

}