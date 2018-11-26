package com.example.ankita.studenttracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import Bean.LocationBean;
import Bean.OutpassBean;
import Bean.StudentBean;
import Bean.WardenBean;
import email.GMailSender;
import firebase.FirebaseHelper;

/**
 * Created by Ankita on 24-04-2018.
 */

public class ForgotPassword extends AppCompatActivity {
    private long generatedOTP;
    private String s1, s2, user, type;
    int i, b, c;
    private ProgressDialog progressDialog;
    private RadioButton rbwardenfgp, rbstudentfgp;
    public ArrayList<StudentBean> studentList = new ArrayList<>();
    public ArrayList<WardenBean> wardenList = new ArrayList<>();
    private DatabaseReference databaseStudent;
    private DatabaseReference databaseWarden;
    private TelephonyManager telephonyManager;
    FirebaseDatabase firebaseDatabase;
    private TextView tvfgp;
    private EditText usernamefgp, otp, username, password, repassword;
    private Button getOTP, submitOTP, changePass;
    private String message1 = "We have received your request to change your password for STUDENT TRACKING mobile application designed by A.C.S as a part of minor project. Your one time password(OTP) for changing password is- ";
    private String message2 = ". Don't share it with anyone.If you have not requested to change your password, kindly ignore this email and relax.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        usernamefgp = (EditText) findViewById(R.id.usernamefgp);
        tvfgp = (TextView) findViewById(R.id.tvfgp);
        otp = (EditText) findViewById(R.id.otp);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        getOTP = (Button) findViewById(R.id.getOTP);
        submitOTP = (Button) findViewById(R.id.submitOTP);
        rbwardenfgp = (RadioButton) findViewById(R.id.rbwardenfgp);
        rbstudentfgp = (RadioButton) findViewById(R.id.rbstudentfgp);
        changePass = (Button) findViewById(R.id.changePass);
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseStudent = firebaseDatabase.getReference("Student");
        databaseStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    StudentBean studentBean = postSnapshot.getValue(StudentBean.class);
                    studentList.add(studentBean);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });



        databaseWarden = firebaseDatabase.getReference("Warden");
        databaseWarden.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                wardenList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    WardenBean wardenBean = postSnapshot.getValue(WardenBean.class);
                    wardenList.add(wardenBean);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });


        rbwardenfgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernamefgp.setHint("Username");
                type = "warden";

            }
        });
        rbstudentfgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernamefgp.setHint("Enrollment");
                type = "student";

            }
        });

        //new FetchStudent().execute();




        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user= usernamefgp.getText().toString();
                if (usernamefgp.getText().toString().equals("") || (!rbwardenfgp.isChecked() && !rbstudentfgp.isChecked())) {
                    Toast.makeText(ForgotPassword.this, "Enter Username", Toast.LENGTH_LONG).show();
                } else if (rbstudentfgp.isChecked()) {
                    for (i = 0; i < studentList.size(); i++) {
                        if (studentList.get(i).getEnrollment().equals(usernamefgp.getText().toString())) {
                            s1 = studentList.get(i).getEmailid();
                        }
                    }
                    Random random = new Random();
                    generatedOTP = random.nextInt() % 1000000;
                    if (generatedOTP < 1)
                        generatedOTP *= -1;
                    sendMessage(s1, message1 + String.valueOf(generatedOTP) + message2);

                    usernamefgp.setVisibility(View.GONE);
                    rbwardenfgp.setVisibility(View.GONE);
                    rbstudentfgp.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);
                    getOTP.setVisibility(View.GONE);
                    submitOTP.setVisibility(View.VISIBLE);
                    tvfgp.setVisibility(View.VISIBLE);
                } else if (rbwardenfgp.isChecked()) {
                    for (i = 0; i < wardenList.size(); i++) {
                        if (wardenList.get(i).getUsername().equals(usernamefgp.getText().toString())) {
                            s2 = wardenList.get(i).getEmailid();
                        }
                    }
                    Random random = new Random();
                    generatedOTP = random.nextInt() % 1000000;
                    if (generatedOTP < 1)
                        generatedOTP *= -1;
                    sendMessage(s2, message1 + String.valueOf(generatedOTP) + message2);

                    usernamefgp.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);
                    getOTP.setVisibility(View.GONE);
                    submitOTP.setVisibility(View.VISIBLE);
                    tvfgp.setVisibility(View.VISIBLE);
                }


            }
        });

        submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.getText().toString().equals("")) {
                    Toast.makeText(ForgotPassword.this, "Enter OTP", Toast.LENGTH_LONG).show();
                } else if (otp.getText().toString().equals(String.valueOf(generatedOTP))) {
                    submitOTP.setVisibility(View.GONE);
                    otp.setVisibility(View.GONE);

                    username.setVisibility(View.VISIBLE);
                    username.setText(user);
                    password.setVisibility(View.VISIBLE);
                    repassword.setVisibility(View.VISIBLE);
                    changePass.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(ForgotPassword.this, "Wrong OTP", Toast.LENGTH_LONG).show();
                }

            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("") || password.getText().toString().equals("") || repassword.getText().toString().equals("")) {
                    Toast.makeText(ForgotPassword.this, "All Fields are mandatory", Toast.LENGTH_LONG).show();
                } else if (type. equals("student") ){
                    String pass = password.getText().toString();
                    if (password.getText().toString().equals(repassword.getText().toString())) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseLocation = firebaseDatabase.getReference().child("Student")
                                .child(username.getText().toString()).child("passsword");
                        databaseLocation.setValue(pass);
                        Toast.makeText(ForgotPassword.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else
                        Toast.makeText(ForgotPassword.this, "Password mismatched. Please Try again", Toast.LENGTH_LONG).show();
                    password.setText("");
                    repassword.setText("");
                } else if (type.equals( "warden") ){
                    String pass = password.getText().toString();
                    if (password.getText().toString().equals(repassword.getText().toString())) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseLocation = firebaseDatabase.getReference().child("Warden")
                                .child(username.getText().toString()).child("passsword");
                        databaseLocation.setValue(pass);
                        Toast.makeText(ForgotPassword.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Password mismatched. Please Try again", Toast.LENGTH_LONG).show();
                        password.setText("");
                        repassword.setText("");
                    }
                }

            }
        });
    }

    private void sendMessage(final String reciepent, final String message) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Generating OTP");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("noreplystudenttracking@gmail.com", "noreply123");
                    sender.sendMail("Your OTP is here",
                            message,
                            "noreplystudenttracking@gmail.com",
                            reciepent);
                    dialog.dismiss();

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
        Toast.makeText(ForgotPassword.this, "Please check your Email", Toast.LENGTH_LONG).show();

    }

    /*private class FetchStudent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ForgotPassword.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }

        @Override
        protected Void doInBackground(Void... voids) {

            firebaseDatabase = FirebaseDatabase.getInstance();

                databaseStudent = firebaseDatabase.getReference("Student");
                databaseStudent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        studentList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            StudentBean studentBean = postSnapshot.getValue(StudentBean.class);
                            studentList.add(studentBean);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: ");
                    }
                });



                databaseWarden = firebaseDatabase.getReference("Warden");
                databaseWarden.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        wardenList.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            WardenBean wardenBean = postSnapshot.getValue(WardenBean.class);
                            wardenList.add(wardenBean);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: ");
                    }
                });



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

        }
    }*/
}
