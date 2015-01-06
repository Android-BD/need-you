package com.example.nipun.aid_test_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by nipun on 31/12/14.
 */
public class NotifyUser {

    private static NotifyUser self = null;
    //Mailing variables
    private static int MAIL_SENT = 0;

    private void NotifyUser() { }

    public static NotifyUser getNotifyUser() {
        if( self == null ) {
            self = new NotifyUser();
        }
        return self;
    }

    public void allNotify() {
        registerInLog();
    }

    public void mailNotify() {
        //Send Email
                        /*Intent email = new Intent(Intent.ACTION_SEND);
                        email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jindal.nipun@gmail.com"});
                        email.putExtra(Intent.EXTRA_SUBJECT, "URGENT: Need Help");
                        email.putExtra(Intent.EXTRA_TEXT, "I am currently at location xxx and in trouble");
                        email.setType("message/rfc822");
                        //startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        startActivity(email);*/
    }

    public void registerInLog() {
        Log.d("Notify User", "Mail/SMS To user");
    }

    public void smsNotify() {

    }

    public void audioNotify( Context context ) {

        /*//Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(SingletonApp.getContext().getApplicationContext(), notification);
        r.play();

        //Define Notification Manager
        NotificationManager notificationManager = (NotificationManager) SingletonApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

//Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SingletonApp.getContext().getApplicationContext())
                .setSound(soundUri); //This sets the sound to play

//Display notification
        notificationManager.notify(0, mBuilder.build());
        */
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.sound = Uri.parse("android.resource://com.your.package/raw/sound_file");
        nm.notify(0, notification);
    }
}
