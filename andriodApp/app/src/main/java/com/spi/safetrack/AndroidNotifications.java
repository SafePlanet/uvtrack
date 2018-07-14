package com.spi.safetrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spi.track.safetrack.MyFirebaseMessagingService;
import com.spi.track.safetrack.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AndroidNotifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_notifications);

        ArrayList<String> data = new ArrayList<>();
        SharedPreferences shared = getSharedPreferences("App_settings", MODE_PRIVATE);
        ArrayList<MyFirebaseMessagingService.ClassObj> arrPackage = new ArrayList<>();
        Gson gson = new Gson();

        ArrayList<MyFirebaseMessagingService.ClassObj> contactList = gson.fromJson(shared.getString("DATE_LIST", "[]"), new TypeToken<List<MyFirebaseMessagingService.ClassObj>>() {
        }.getType());

        for (MyFirebaseMessagingService.ClassObj contact : contactList) {
            if (!isExpired(new Date(contact.createdOn))) {
                arrPackage.add(contact);
                data.add( new SimpleDateFormat("hh:mm a").format(new Date(contact.createdOn))+ ": "+ contact.msg);
            }
        }

        SharedPreferences.Editor editor = shared.edit();
        editor.putString("DATE_LIST", gson.toJson(arrPackage));
        editor.apply();
        editor.commit();

        ListView simpleList = (ListView) findViewById(R.id.simpleListView);
        simpleList.setAdapter(new ArrayAdapter<>(this, R.layout.activity_list, R.id.textView, data));
    }

    boolean isExpired(Date dt1) {
        Date dt2 = Calendar.getInstance().getTime();
        long diff = dt2.getTime() - dt1.getTime();
        // long diffSeconds = diff / 1000 % 60;
        // long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        // int diffInDays = (int) ((dt2.getTime() - dt1.getTime()) / (1000 * 60 * 60 * 24));
        return diffHours > 12;
    }
}
