package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ankita.studenttracking.R;

import java.util.List;

import Bean.LocationBean;

/**
 * Created by Ankita on 18-04-2018.
 */

public class StudentAdapter extends BaseAdapter  {
    List<LocationBean> liststudents;
    private Context con;
    private static LayoutInflater inflater;

    public StudentAdapter(List<LocationBean> liststudents, Context con) {
        this.liststudents = liststudents;
        this.con = con;
        inflater =(LayoutInflater)  con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return liststudents.size();
    }

    @Override
    public Object getItem(int i) {
        return liststudents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
            view=inflater.inflate(R.layout.fragment_homeadmin,null);
        TextView enrollmentfetch= (TextView) view.findViewById(R.id.enrollmentfetch);
        TextView locationfetch= (TextView) view.findViewById(R.id.locationfetch);
        TextView roomfetch =(TextView) view.findViewById(R.id.roomfetch);
        TextView timefetch= (TextView) view.findViewById(R.id.timefetch);


        enrollmentfetch.setText(liststudents.get(i).getEnrollment());
        locationfetch.setText(liststudents.get(i).getLocality()+" , " +liststudents.get(i).getPlace());
        roomfetch.setText(liststudents.get(i).getRoom());
        timefetch.setText(liststudents.get(i).getTime());
        return view;

    }


}


