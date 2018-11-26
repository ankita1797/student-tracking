package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.icu.text.SimpleDateFormat;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Adapter.OutpassAdapter;
import Adapter.OutpassDetailsAdapter;
import Bean.OutpassBean;

/**
 * Created by Ankita on 27-03-2018.
 */

public class ManageOutpassFragment extends Fragment {
    public ArrayList<OutpassBean> liststudents = new ArrayList<>();
    public ArrayList<OutpassBean> listoutpassstudents = new ArrayList<>();

    private MainScreenActivity mainScreenActivity;
    private ListView lv;
    private DatabaseReference databaseOutpass;
    private FirebaseDatabase firebaseDatabase;
    private OutpassBean outpassBean;
    private java.text.SimpleDateFormat simpleDateFormat;
    private Date leavingDate, returningDate, currentDate;

    public ManageOutpassFragment() {
    }

    @SuppressLint("ValidFragment")
    public ManageOutpassFragment(MainScreenActivity mainScreenActivity, ArrayList<OutpassBean> liststudents) {
        this.liststudents = liststudents;
        this.mainScreenActivity = mainScreenActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manageoutpasslistview, container, false);
        simpleDateFormat= new java.text.SimpleDateFormat("dd/MM/yyyy");
        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        lv=rootView.findViewById(R.id.lv_students);
        try {
            currentDate= simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int i, c=0;
        for (i = 0; i < liststudents.size(); i++) {
            try {
                leavingDate = simpleDateFormat.parse(liststudents.get(i).getReturningDate());
                returningDate = simpleDateFormat.parse(liststudents.get(i).getReturningDate());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (currentDate.before(returningDate)) {
                listoutpassstudents.add(liststudents.get(i));
                c++;
            }
        }
        if(c==0)
            Toast.makeText(mainScreenActivity, "No outpass signed for today.!" , Toast.LENGTH_LONG).show();;

        OutpassAdapter outpassDetailsAdapter = new OutpassAdapter(listoutpassstudents, mainScreenActivity);

        lv.setAdapter(outpassDetailsAdapter);



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
