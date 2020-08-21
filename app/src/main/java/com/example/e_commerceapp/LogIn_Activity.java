package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerceapp.Model.Users;
import com.example.e_commerceapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LogIn_Activity extends AppCompatActivity {
    private Button LogIn_Btn;
    private EditText LogIn_Input_Phone_Number,LogIn_Input_Password;
    private TextView Admin_LInk,Not_Admin_Link;
    private ProgressDialog loadingBar;
    private  String parentDB="Users";
    private CheckBox Remember_me_checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_);

        LogIn_Btn=findViewById(R.id.login_btnID2);
        LogIn_Input_Phone_Number=findViewById(R.id.phn_number_editID);
        LogIn_Input_Password=findViewById(R.id.password_editID);
        Remember_me_checkbox=findViewById(R.id.remember_me_chkb);
        Admin_LInk=findViewById(R.id.i_am_admin_textID);
        Not_Admin_Link=findViewById(R.id.i_am_not_admin_textID);

        Admin_LInk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn_Btn.setText("LogIn Admin");
                Admin_LInk.setVisibility(View.INVISIBLE);
                Not_Admin_Link.setVisibility(View.VISIBLE);
                parentDB="Admins";
            }
        });

        Not_Admin_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn_Btn.setText("LOg in ");
                Admin_LInk.setVisibility(View.VISIBLE);
                Not_Admin_Link.setVisibility(View.INVISIBLE);
                parentDB="Users";
            }
        });
        Paper.init(this);

        LogIn_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_login();
            }
        });


    }


    private void user_login() {
        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Login IN");
        loadingBar.setMessage("Wait till login in.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        String phone=LogIn_Input_Phone_Number.getText().toString().trim();
        String password = LogIn_Input_Password.getText().toString().trim();

        allow_access_to_account(phone,password);


    }

    private void allow_access_to_account(final String phone, final String password) {

        if (Remember_me_checkbox.isChecked()){
            Paper.book().write(Prevalent.USER_PHONE_KEY,phone);
            Paper.book().write(Prevalent.USER_PASSWORD_KEY,password);

        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDB).child(phone).exists()){
                    Users user_data=snapshot.child(parentDB).child(phone).getValue(Users.class);
                    if (!TextUtils.isEmpty(user_data.getPhone())){
                        if (user_data.getPhone().equals(phone)){
                            if (user_data.getPassword().equals(password)){
                                if (parentDB.equals("Admins")){
                                    loadingBar.dismiss();
                                    Intent intent=new Intent(LogIn_Activity.this, AdminCategoryActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if (parentDB.equals("Users")){
                                    loadingBar.dismiss();
                                    Intent intent=new Intent(LogIn_Activity.this,HomeActivity.class);
                                    Prevalent.current_online_users = user_data;
                                    startActivity(intent);
                                    finish();
                                }


                            }else {
                                loadingBar.dismiss();
                                Toast.makeText(LogIn_Activity.this, "Incorrect password ", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(LogIn_Activity.this, "Incorrect phone ", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        loadingBar.dismiss();
                        Toast.makeText(LogIn_Activity.this, "Phone is empty", Toast.LENGTH_SHORT).show();
                    }
                    }
                     else {
                         loadingBar.dismiss();
                    Toast.makeText(LogIn_Activity.this, phone+" does not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}