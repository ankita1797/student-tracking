/**
 * Created by Ankita on 15-03-2018.
 */

package com.example.ankita.studenttracking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import Bean.StudentBean;
import Bean.WardenBean;
import firebase.FirebaseHelper;


/**
 * Created by Ankita on 01-03-2018.
 */

public class SignupActivity extends AppCompatActivity {
    private TextView login;
    private EditText input_name, enrollment, r_name, password, mailid;
    private Button btn_signup;
    private TextInputLayout layoutenrollment, layoutroomname;
    private RadioButton rb_warden, rb_student;
    private StudentBean studentBean;
    private WardenBean wardenBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        login = (TextView) findViewById(R.id.tv_login);
        input_name = (EditText) findViewById(R.id.input_name);
        mailid = (EditText) findViewById(R.id.mailid);
        enrollment = (EditText) findViewById(R.id.enrollment);
        r_name = (EditText) findViewById(R.id.r_name);
        password = (EditText) findViewById(R.id.password);
        layoutroomname = findViewById(R.id.layoutroomname);
        layoutenrollment = findViewById(R.id.layoutenrollment);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        rb_student = (RadioButton) findViewById(R.id.rb_student);
        rb_warden = (RadioButton) findViewById(R.id.rb_warden);


        rb_warden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutenrollment.setHint("Username");

                r_name.setVisibility(View.GONE);
                layoutroomname.setVisibility(View.GONE);

            }
        });
        rb_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enrollment.getText().toString().equals("") || enrollment.getText().toString().equals("Username")) {

                    layoutenrollment.setHint("Enrollment");
                    r_name.setVisibility(View.VISIBLE);
                    layoutroomname.setVisibility(View.VISIBLE);
                }

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input_name.getText().toString().equals("") || enrollment.getText().toString().equals("") || (!rb_warden.isChecked() && !rb_student.isChecked()) || password.getText().toString().equals("") || r_name.getText().toString().equals(""))
                    Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_LONG).show();
                else {
                    //if(textCaptcha.checkAnswer(captcha.getText().toString().trim()) )
                    //{
                    if (rb_student.isChecked()) {
                        String name = input_name.getText().toString();
                        String room = r_name.getText().toString();
                        studentBean = new StudentBean();
                        studentBean.setName(input_name.getText().toString());
                        studentBean.setEnrollment(enrollment.getText().toString());
                        studentBean.setPasssword(password.getText().toString());
                        studentBean.setEmailid(mailid.getText().toString());
                        studentBean.setRoomNumber(r_name.getText().toString());
                        studentBean.setStatus(false);
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (ActivityCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            studentBean.setDeviceID(telephonyManager.getImei());
                        }
                        FirebaseHelper firebaseHelper = new FirebaseHelper("student");
                            firebaseHelper.insertStudent(studentBean);
                            SharedPreferences.Editor editor = getSharedPreferences("user_login", MODE_PRIVATE).edit();
                            editor.putString("name",name);
                            editor.putString("room",room);
                            editor.commit();
                            input_name.setText("");
                            enrollment.setText("");
                            password.setText("");
                            r_name.setText("");
                            mailid.setText("");
                            Toast.makeText(SignupActivity.this, "wait for admin to confirm your account", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            wardenBean = new WardenBean();
                            wardenBean.setName(input_name.getText().toString());
                            wardenBean.setUsername(enrollment.getText().toString());
                            wardenBean.setPasssword(password.getText().toString());
                            wardenBean.setEmailid(mailid.getText().toString());
                            wardenBean.setStatus(false);
                            FirebaseHelper firebaseHelper = new FirebaseHelper("warden");
                            firebaseHelper.insertWarden(wardenBean);
                            input_name.setText("");
                            enrollment.setText("");
                            password.setText("");
                            mailid.setText("");
                            Toast.makeText(SignupActivity.this, "wait for admin to confirm your account", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }

                }
            }
        });

    }
}