package com.example.ankita.studenttracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Bean.StudentBean;
import Bean.WardenBean;
import Utilities.TextCaptcha;
import Utilities.Tools;

import static Utilities.Tools.MY_PERMISSIONS_REQUEST_LOCATION;


/**
 * Created by Ankita on 01-03-2018.
 */


    public class LoginActivity extends AppCompatActivity  {
    private TextView signup, input_memType, forgetpassword;
    private EditText inputenrollment, password;
    private RadioButton rbwarden, rbstudent;
    private Button btn_login;
    private ImageView image;
    public boolean result;
    private StudentBean studentBean;
    public ArrayList<StudentBean> studentList=new ArrayList<>();
    public ArrayList<WardenBean> wardenList=new ArrayList<>();
    private DatabaseReference databaseStudent;
    private DatabaseReference databaseWarden;
    private TelephonyManager telephonyManager;
    private TextCaptcha textCaptcha;
    FirebaseDatabase firebaseDatabase;
    AppCompatEditText captcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("user_login", MODE_PRIVATE);
        if(prefs.getBoolean("isLogin", false))
        {
            startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
            finish();
        }
        telephonyManager= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Tools.checkPermission(LoginActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signup= (TextView) findViewById(R.id.signup);
        forgetpassword= (TextView) findViewById(R.id.forgetpassword);
        rbwarden=(RadioButton)findViewById(R.id.rbwarden);
        rbstudent=(RadioButton)findViewById(R.id.rbstudent);
        input_memType=(TextView)findViewById(R.id.input_memType);
        inputenrollment=(EditText )findViewById(R.id.inputenrollment);
        password=(EditText )findViewById(R.id.password);
        btn_login =(Button)findViewById(R.id.btn_login);
        image = (ImageView) findViewById(R.id.image);
        captcha = (AppCompatEditText) findViewById(R.id.captcha);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseStudent=firebaseDatabase.getReference("Student");
        databaseWarden=firebaseDatabase.getReference("Warden");
        databaseStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    StudentBean studentBean = postSnapshot.getValue(StudentBean.class);
                    studentList.add(studentBean);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });

        databaseWarden.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                wardenList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    WardenBean wardenBean = postSnapshot.getValue(WardenBean.class);
                    wardenList.add(wardenBean);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });



        rbwarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputenrollment.setHint("Username");

            }
        });
        rbstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputenrollment.setHint("Enrollment");

            }
        });
        textCaptcha= new TextCaptcha(600, 150, 4, TextCaptcha.TextOptions.LETTERS_ONLY);
        image.setImageBitmap(textCaptcha.getImage());




       signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i= new Intent(LoginActivity.this, SignupActivity.class);
               startActivity(i);

           }
       });
       forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(i);

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cap=captcha.getText().toString();
                cap=cap.trim();
                if(inputenrollment.getText().toString().equals("")||(!rbwarden.isChecked()&&!rbstudent.isChecked())||password.getText().toString().equals("")||captcha.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this, "All fields are mandatory",Toast.LENGTH_LONG).show();
                else {
                       String user= inputenrollment.getText().toString();
                       String pass= password.getText().toString();
                       captcha.setError(null);

                    if(rbstudent.isChecked())
                    {

                        int i,c=0;
                        for(i=0;i<studentList.size();i++)
                        {
                            if(studentList.get(i).getEnrollment().equals(user))
                            {
                                c++;
                                if(studentList.get(i).isStatus())
                                {
                                    if(studentList.get(i).getPasssword().equals(pass))
                                    {
                                        if (!textCaptcha.checkAnswer(cap))
                                        {
                                            Toast.makeText(LoginActivity.this, "Please ensure that captcha is correct", Toast.LENGTH_SHORT).show();
                                            textCaptcha = new TextCaptcha(600, 150, 4, TextCaptcha.TextOptions.LETTERS_ONLY);
                                            image.setImageBitmap(textCaptcha.getImage());
                                            captcha.setText("");
                                            break;
                                        }
                                        else if(!studentList.get(i).getDeviceID().equals(telephonyManager.getImei()))
                                        {
                                            Toast.makeText(LoginActivity.this,"Device Not Recognised",Toast.LENGTH_LONG).show();
                                            break;
                                        }
                                        else
                                        {


                                            SharedPreferences.Editor editor = getSharedPreferences("user_login", MODE_PRIVATE).edit();
                                            editor.putBoolean("isLogin", true);
                                            editor.putString("username", user);
                                            editor.putString("type", "student");
                                            editor.putString("room",studentList.get(i).getRoomNumber());
                                            editor.putString("name",studentList.get(i).getName());
                                            editor.putString("enrollment",studentList.get(i).getEnrollment());
                                            editor.commit();
                                            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }

                                    else
                                    {
                                        Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Wait for admin to confirm your account",Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        }
                        if(c==0)
                            Toast.makeText(LoginActivity.this,"Invalid Username",Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        int i,c=0;
                        for(i=0;i<wardenList.size();i++)
                        {
                            if(wardenList.get(i).getUsername().equals(user))
                            {
                                c++;
                                if(wardenList.get(i).isStatus())
                                {
                                    if(wardenList.get(i).getPasssword().equals(pass))
                                    {
                                        if (textCaptcha.checkAnswer(captcha.getText().toString().trim()))
                                        {
                                            SharedPreferences.Editor editor = getSharedPreferences("user_login", MODE_PRIVATE).edit();
                                            editor.putBoolean("isLogin", true);
                                            editor.putString("username", user);
                                            editor.putString("type", "warden");
                                            editor.commit();
                                            Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        else
                                        {

                                            Toast.makeText(LoginActivity.this, "Please ensure that captcha is correct", Toast.LENGTH_SHORT).show();
                                            textCaptcha = new TextCaptcha(600, 150, 4, TextCaptcha.TextOptions.LETTERS_ONLY);
                                            image.setImageBitmap(textCaptcha.getImage());
                                            captcha.setText("");

                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this,"Incorrect Password",Toast.LENGTH_LONG).show();
                                    }

                                    break;
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Wait for admin to confirm your account",Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        }
                        if(c==0)
                            Toast.makeText(LoginActivity.this,"Invalid Username",Toast.LENGTH_LONG).show();



                    }


                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Tools.MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    result= Tools.checkPermission(LoginActivity.this);
                }
                break;
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(LoginActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }
                } else {
                    Toast.makeText(LoginActivity.this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
