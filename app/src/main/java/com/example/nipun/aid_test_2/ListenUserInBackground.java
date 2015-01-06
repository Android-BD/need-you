package com.example.nipun.aid_test_2;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class ListenUserInBackground extends Service implements SensorEventListener {

    // Data members to register Power button press
    private static int counter = 0;
    private static long startTime = new Date().getTime();

    private int mStartMode;         // Indicates how to behave if the service is killed
    private IBinder mBinder;        // Interface for clients that bind
    private boolean mAllowRebind;   // Indicates whether onRebind should be used

    private SensorManager mSensorManager;   //sensor Manager for detching accerlerometer sensor
    private Sensor mSensor;                 //accelerometer sensor

    //Detect accelerometer shake
    private long lastUpdate = 0;
    private int numShake = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private static final int NUM_SHAKE_THRESHOLD = 5;

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            if ((new Date().getTime() - startTime) < 300) {
                counter++;
                startTime = new Date().getTime();
                if (counter == 2) {
                    counter = 0;

                    // Trigger recording
                    Log.d("PowerButtonTrigger", "power button pressed two times");
                    //Need to check alert box
                    unMuteSystemAudio();
                    alertUser();
                    //fireListen();
                    //fireVibration();
                }
            } else {
                counter = 1;
                startTime = new Date().getTime();
            }
        }

    };

    private void fireListen() {
        class MyRecognitionListener implements RecognitionListener {

            @Override
            public void onBeginningOfSpeech() {
                Log.d("RecognitionListener", "onBeginningOfSpeech");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                Log.d("RecognitionListener", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("RecognitionListener", "onEndOfSpeech");
            }

            @Override
            public void onError(int error) {
                Log.d("RecognitionListener", "Error" + Integer.toString(error));
                unMuteSystemAudio();
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                Log.d("RecognitionListener", "onEvent");
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                Log.d("RecognitionListener", "onPartialResults");
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("RecognitionListener", "onReadyForSpeech");
            }


            @Override
            public void onResults(Bundle results) {
                Log.d("RecognitionListener", "onResults");
                ArrayList<String> strlist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < strlist.size(); i++) {
                    Log.d("RecognitionListener", "result=" + strlist.get(i));
                    if( strlist.get(i).contains("help") ) {
                        Log.d("RecognitionListener", "Sending Mail");
                        alertUser();
                    }
                }

                unMuteSystemAudio();
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Log.d("Speech", "onRmsChanged");
            }
        }

        SpeechRecognizer speechRecObj = SpeechRecognizer.createSpeechRecognizer(this);

        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.d("SystemAudio", "Starting voice capture");
            muteSystemAudio();

            MyRecognitionListener recListenObj = new MyRecognitionListener();
            speechRecObj.setRecognitionListener(recListenObj);
            Intent intentObj = new Intent(RecognizerIntent.getVoiceDetailsIntent(getApplicationContext()));
            intentObj.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
            speechRecObj.startListening(intentObj);
        }
    }

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        Log.d("BackgroundService", "Entering into onCreate");
        //Create an object for broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myReceiver, filter);

        //Create Accelerometer Sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return START_STICKY;
        return mStartMode;
    }

    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called when all clients have unbound with unbindService()
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * Called when a client is binding to the service with bindService()
     */
    @Override
    public void onRebind(Intent intent) {

    }

    /**
     * Called when the service is being destroyed.
     */
    @Override
    public void onDestroy() {
        Log.d("BackgroundService", "destroy");
        unregisterReceiver(myReceiver);
        mSensorManager.unregisterListener(this);
    }

    public void muteSystemAudio() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        Log.d("SystemAudio", "system audio is muted");
    }

    public void unMuteSystemAudio() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        Log.d("SystemAudio", "system audio is unmuted");
    }

    //override SensorEventListener interface
    //TODO: need to pull in logic for number of times the device was shook
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if( sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            //Log.d("Accelerometer", "Sensor Changed");
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    numShake++;
                    //Log.d("Accelerometer", "User shook the phone");
                    if( numShake >= NUM_SHAKE_THRESHOLD ) {
                        numShake = 0;
                        Log.d("Accelerometer", "User shook the phone trigger listen");
                        fireListen();
                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
            else {
                numShake = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Log.d("Accelerometer", "Accuracy Changed!");
    }

    public void alertUser() {
        //TODO: NEED TO SOUND NOTIFICATION AS WELL
        final Intent dialog = new Intent(this, BackGroundDialogs.class);
        dialog.setType("text/plain");
        //dialog.putExtra(android.content.Intent.EXTRA_TEXT, "reason");
        dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(dialog);
    }
}
