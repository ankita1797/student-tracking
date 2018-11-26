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

public class OutpassDetailsAdapter extends BaseAdapter  {
    List<OutpassBean> listoutpassstudents;
    private Context con;
    private static LayoutInflater inflater;

    public OutpassDetailsAdapter(List<OutpassBean>listoutpassstudents, Context con) {
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
            view=inflater.inflate(R.layout.fragment_outpassdetails,null);
        TextView addressop= (TextView) view.findViewById(R.id.addressop);
        TextView leavingdateop= (TextView) view.findViewById(R.id.leavingdateop);
        TextView returningdateop= (TextView) view.findViewById(R.id.returningdateop);
        TextView reasonopd= (TextView) view.findViewById(R.id.reasonopd);
        TextView addressfetch= (TextView) view.findViewById(R.id.addressfetch);
        TextView returningdatefetch= (TextView) view.findViewById(R.id.returningdatefetch);
        TextView leavingdatefetch= (TextView) view.findViewById(R.id.leavingdatefetch);
        TextView reasonfetch= (TextView) view.findViewById(R.id.reasonfetch);

        addressfetch.setText(listoutpassstudents.get(i).getAddress());
        returningdatefetch.setText(listoutpassstudents.get(i).getReturningDate());
        leavingdatefetch.setText(listoutpassstudents.get(i).getLeavingDate());
        reasonfetch.setText(listoutpassstudents.get(i).getReason());
        return view;
    }

}

