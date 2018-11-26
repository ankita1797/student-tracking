package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.OutpassAdapter;
import Adapter.OutpassDetailsAdapter;
import Bean.OutpassBean;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ankita on 27-03-2018.
 */

public class OutpassDetailsFragment extends Fragment {

    private MainScreenActivity mainScreenActivity;
    private ListView lv;
    public ArrayList<OutpassBean> listoutpassstudents = new ArrayList<>();
    public ArrayList<OutpassBean> liststudents = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    String user;

    public OutpassDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public OutpassDetailsFragment(MainScreenActivity mainScreenActivity,ArrayList<OutpassBean> liststudents) {
        this.mainScreenActivity = mainScreenActivity;
        this.liststudents=liststudents;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outpassdetailslistview, container, false);

        lv = rootView.findViewById(R.id.lv_students);


        SharedPreferences prefs = getActivity().getSharedPreferences("user_login", MODE_PRIVATE);
        user = prefs.getString("username", null);

        int i, c=0;
        for (i = 0; i < liststudents.size(); i++) {
            if (liststudents.get(i).getEnrollment().equals(user)) {
                listoutpassstudents.add(liststudents.get(i));
                c++;
            }
        }
        if(c==0)
        Toast.makeText(mainScreenActivity, "No outpass signed yet!" , Toast.LENGTH_LONG).show();;

        OutpassDetailsAdapter outpassDetailsAdapter = new OutpassDetailsAdapter(listoutpassstudents, mainScreenActivity);

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



