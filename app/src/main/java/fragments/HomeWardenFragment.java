package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ankita.studenttracking.MainScreenActivity;
import com.example.ankita.studenttracking.R;

import java.util.ArrayList;
import java.util.Calendar;

import Adapter.StudentAdapter;
import Bean.LocationBean;
import service.AlarmReceiver;

/**
 * Created by Ankita on 27-03-2018.
 */

public class HomeWardenFragment extends Fragment {
    private MainScreenActivity mainScreenActivity;
    private ListView lv;
    public StudentAdapter studentAdapter;
    private ArrayList<LocationBean> studentLoaction;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public HomeWardenFragment(){

    }

    @SuppressLint("ValidFragment")
    public HomeWardenFragment(MainScreenActivity mainScreenActivity,ArrayList<LocationBean> studentLoaction) {
        this.mainScreenActivity = mainScreenActivity;
        this.studentLoaction=studentLoaction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homeadminlistview, container, false);



        lv = (ListView) rootView.findViewById(R.id.lv_students);
        studentAdapter=new StudentAdapter(studentLoaction,mainScreenActivity);
        lv.setAdapter( studentAdapter);


        alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, i, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 15);


        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}


