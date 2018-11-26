package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.ankita.studenttracking.R;

import java.util.List;

import Bean.OutpassBean;

/**
 * Created by Ankita on 18-04-2018.
 */

public class OutpassAdapter extends BaseAdapter implements Filterable {
    List<OutpassBean> listoutpassstudents;
    private Context con;
    private static LayoutInflater inflater;

    public OutpassAdapter(List<OutpassBean>listoutpassstudents, Context con) {
        this.listoutpassstudents = listoutpassstudents;
        this.con = con;
        inflater =(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return listoutpassstudents.size();
    }

    @Override
    public Object getItem(int i) {
        return listoutpassstudents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
            view=inflater.inflate(R.layout.fragment_manageoutpass,null);
        TextView enrollmentop= (TextView) view.findViewById(R.id.enrollmentop);
        TextView leavingdateop= (TextView) view.findViewById(R.id.leavingdateop);
        TextView returningdateop= (TextView) view.findViewById(R.id.returningdateop);
        TextView enrollmentfetch= (TextView) view.findViewById(R.id.enrollmentfetch);
        TextView returningdatefetch= (TextView) view.findViewById(R.id.returningdatefetch);
        TextView leavingdatefetch= (TextView) view.findViewById(R.id.leavingdatefetch);
        TextView reasonfetch= (TextView) view.findViewById(R.id.reasonfetch);
        TextView placefetch= (TextView) view.findViewById(R.id.placefetch);
        TextView phonenumberfetch= (TextView) view.findViewById(R.id.phonenumberfetch);
        TextView roomnumberfetch= (TextView) view.findViewById(R.id.roomnumberfetch);


        enrollmentfetch.setText(listoutpassstudents.get(i).getEnrollment());
        returningdatefetch.setText(listoutpassstudents.get(i).getReturningDate());
        leavingdatefetch.setText(listoutpassstudents.get(i).getLeavingDate());
        reasonfetch.setText(listoutpassstudents.get(i).getReason());
        placefetch.setText(listoutpassstudents.get(i).getAddress());
        phonenumberfetch.setText(listoutpassstudents.get(i).getPhoneNumber());
        roomnumberfetch.setText(listoutpassstudents.get(i).getRoom());


        return view;

    }


    @Override
    public Filter getFilter() {
        return null;
    }
}
