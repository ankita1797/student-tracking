package service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Bean.LocationBean;
import email.GMailSender;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ankita on 30-04-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseLocation;
    private ArrayList<LocationBean> studentLocation=new ArrayList<>();
    private String studentLocations = "";
    @Override
    public void onReceive(Context context, Intent intent) {

        firebaseDatabase= FirebaseDatabase.getInstance();

        databaseLocation=firebaseDatabase.getReference("Location");
        databaseLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentLocation.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    LocationBean locationBean = postSnapshot.getValue(LocationBean.class);
                    studentLocation.add(locationBean);

                }

                for(int i=0;i<studentLocation.size();i++)
                {
                    studentLocations +="Enrollment :"+studentLocation.get(i).getEnrollment()+"\nName :"+studentLocation.get(i).getName()+"\nLatitude : "+studentLocation.get(i).getLat()+"\nLongitude : "
                            +studentLocation.get(i).getLongi()+"\n Locality : "+studentLocation.get(i).getLocality()+"\n Place:"+ studentLocation.get(i).getPlace()+"\nTime: "+studentLocation.get(i).getTime()+"\n\n";
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });

        final Thread sender = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(4000);
                    GMailSender sender = new GMailSender("noreplystudenttracking@gmail.com", "noreply123");
                    sender.sendMail("Location Updater",studentLocations,
                            "noreplystudenttracking@gmail.com",
                            "wardenstudenttracking@gmail.com");

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
        Toast.makeText(context,"Please check your Email",Toast.LENGTH_LONG).show();

    }
}
