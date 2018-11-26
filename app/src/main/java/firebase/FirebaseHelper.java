package firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Bean.LocationBean;
import Bean.OutpassBean;
import Bean.StudentBean;
import Bean.WardenBean;

/**
 * Created by Ankita on 11-04-2018.
 */

public class FirebaseHelper {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseStudent;
    private DatabaseReference databaseWarden;
    private DatabaseReference databaseOutpass;
    private DatabaseReference databaseLocation;
    private StudentBean studentBean[];
    private WardenBean wardenBean;
    private String type;
    public ArrayList<StudentBean> studentList=new ArrayList<>();


    public FirebaseHelper(String type) {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseStudent=firebaseDatabase.getReference().child("Student");
        databaseWarden=firebaseDatabase.getReference().child("Warden");
        databaseOutpass=firebaseDatabase.getReference().child("Outpass Details");
        databaseLocation=firebaseDatabase.getReference().child("Location");
        this.type=type;

    }

    public void insertStudent(StudentBean myBean)
    {
        databaseStudent.child(myBean.getEnrollment()).setValue(myBean);
    }

    public void insertWarden(WardenBean myBean)
    {

        databaseWarden.child(myBean.getUsername()).setValue(myBean);
    }
    public void insertSignOutpass(OutpassBean outpassBean)
    {
        databaseOutpass.push().setValue(outpassBean);
    }

    public void insertLocation(LocationBean locationBean)
    {

            databaseLocation.child(locationBean.getEnrollment()).setValue(locationBean);

    }


}
