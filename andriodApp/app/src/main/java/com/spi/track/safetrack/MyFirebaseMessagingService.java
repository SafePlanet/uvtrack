package com.spi.track.safetrack;

/**
 * Created by Niteesh Mishra on 01-09-2017.
 */

        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.BitmapFactory;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.preference.PreferenceManager;
        import android.support.constraint.solver.SolverVariable;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;

        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;
        import com.google.gson.Gson;
        import com.google.gson.annotations.SerializedName;
        import com.google.gson.reflect.TypeToken;
        import com.spi.safetrack.AndroidNotifications;
        import com.spi.safetrack.MainActivity;

        import java.lang.reflect.Array;
        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Date;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Set;
        import java.util.StringTokenizer;
        import java.util.concurrent.TimeUnit;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("body"));
        Arrays.asList(remoteMessage.getData()).indexOf(1);
        Log.d(TAG, "From: " + remoteMessage.getData().get(1));

        //  String click_action = remoteMessage.getNotification().getClickAction();
        //Calling method to generate notificatio

        sendNotification(remoteMessage.getData().get("body"));

    }


    public class ClassObj {
        @SerializedName("msg") public String msg;
        public long createdOn;
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {

        //1. ArrayList<String> list
        //2. fetch array list from pref
        //3. list.add new msg
        //4. store back to pref
        ArrayList<ClassObj> data;
        SharedPreferences shared = getSharedPreferences("App_settings", MODE_PRIVATE);
        String jsonFavorites = shared.getString("DATE_LIST", "[]");
        Gson gson = new Gson();
        data = gson.fromJson(jsonFavorites, new TypeToken<ArrayList<ClassObj>>(){}.getType());

        ClassObj obj = new ClassObj();
        obj.msg = messageBody;
        obj.createdOn = new Date(System.currentTimeMillis()).getTime();
        data.add(obj);

        SharedPreferences.Editor editor = shared.edit();
        editor.putString("DATE_LIST", gson.toJson(data));
        editor.apply();
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), AndroidNotifications.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("NotificationMessage", messageBody);
        intent.putExtra("message", messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT );


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bus)
                .setContentTitle("iSafeTrack Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
               // .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());

    }
}
