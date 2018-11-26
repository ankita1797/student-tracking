package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import  java.util.Date;

import com.example.ankita.studenttracking.MainScreenActivity;
import com.example.ankita.studenttracking.R;
import com.example.ankita.studenttracking.SignupActivity;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import Bean.OutpassBean;
import Bean.StudentBean;
import Bean.WardenBean;
import firebase.FirebaseHelper;

import static android.content.Context.MODE_PRIVATE;
import static com.example.ankita.studenttracking.R.id.sgnopldate;

/**
 * Created by Ankita on 27-03-2018.
 */

public class SignOutpassFragment extends Fragment {
    private MainScreenActivity mainScreenActivity;
    private EditText sgnopenrollment, sgnopaddress, sgnopreason, sgnopldate, sgnoprdate,sgnopphone, sgnoproom;
    private Button sgnopbutton;
    long days;
    private OutpassBean outpassBean;
    private Date leavingDate, returningDate, currentDate;
    private java.text.SimpleDateFormat simpleDateFormat;

    String username, room;
    public SignOutpassFragment() {

    }
    @SuppressLint("ValidFragment")
    public SignOutpassFragment(MainScreenActivity mainScreenActivity) {
        this.mainScreenActivity = mainScreenActivity;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getActivity().getSharedPreferences("user_login", MODE_PRIVATE);
        username=prefs.getString("username",null);
        room= prefs.getString("room", null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signoutpass, container, false);
        sgnopenrollment =(EditText)rootView.findViewById(R.id.sgnopenrollment);
        sgnopaddress =(EditText)rootView.findViewById(R.id.sgnopaddress);
        sgnopreason =(EditText)rootView.findViewById(R.id.sgnopreason);
        sgnopldate =(EditText)rootView.findViewById(R.id.sgnopldate);
        sgnoprdate =(EditText)rootView.findViewById(R.id.sgnoprdate);
        sgnoproom =(EditText)rootView.findViewById(R.id.sgnoproom);
        sgnopphone =(EditText)rootView.findViewById(R.id.sgnopphone);
        sgnopbutton =(Button) rootView.findViewById(R.id.sgnopbutton);

        sgnopenrollment.setText(username);
        sgnopenrollment.setEnabled(false);
        sgnoproom.setText(room);
        sgnoproom.setEnabled(false);
        simpleDateFormat= new java.text.SimpleDateFormat("dd/MM/yyyy");
        String date = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        }
        try {
            currentDate= simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        sgnopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    leavingDate = simpleDateFormat.parse(sgnopldate.getText().toString());
                    returningDate = simpleDateFormat.parse(sgnoprdate.getText().toString());
                    long diffInMilliSec = returningDate.getTime() - leavingDate.getTime();
                    days = (diffInMilliSec / (1000 * 60 * 60 * 24* 365));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (sgnopenrollment.getText().toString().equals("") || sgnopaddress.getText().toString().equals("") || sgnopldate.getText().toString().equals("") || sgnoprdate.getText().toString().equals("") || sgnopreason.getText().toString().equals("") || sgnopphone.getText().toString().equals(""))
                    Toast.makeText(mainScreenActivity, "All fields are mandatory", Toast.LENGTH_LONG).show();
                else if (isValidFormat("dd/MM/yyyy", sgnopldate.getText().toString()) && isValidFormat("dd/MM/yyyy", sgnoprdate.getText().toString()))
                {
                    if ((currentDate.before(leavingDate) || currentDate.equals(leavingDate)) && leavingDate.before(returningDate))
                    {
                        if (days <= 60)
                        {
                            if (sgnopphone.getText().toString().length() == 10)
                            {
                                outpassBean = new OutpassBean();
                                outpassBean.setEnrollment(username);
                                outpassBean.setRoom(room);
                                outpassBean.setAddress(sgnopaddress.getText().toString());
                                outpassBean.setReason(sgnopreason.getText().toString());
                                outpassBean.setPhoneNumber(sgnopphone.getText().toString());
                                outpassBean.setLeavingDate(sgnopldate.getText().toString());
                                outpassBean.setReturningDate(sgnoprdate.getText().toString());
                                FirebaseHelper firebaseHelper = new FirebaseHelper("Outpass Details");
                                firebaseHelper.insertSignOutpass(outpassBean);
                                sgnopaddress.setText("");
                                sgnopphone.setText("");
                                sgnopreason.setText("");
                                sgnopldate.setText("");
                                sgnoprdate.setText("");
                                Toast.makeText(mainScreenActivity, "Outpass Details saved successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mainScreenActivity, "Enter correct phone number", Toast.LENGTH_LONG).show();
                            }
                        } else
                            Toast.makeText(mainScreenActivity, "Your returning date cannot exceed more than 2 months", Toast.LENGTH_LONG).show();

                    } else
                        Toast.makeText(mainScreenActivity, "Enter valid date", Toast.LENGTH_LONG).show();
                }

                else
                    Toast.makeText(mainScreenActivity, "Enter correct date format", Toast.LENGTH_LONG).show();

            }
        });



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


    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

}



