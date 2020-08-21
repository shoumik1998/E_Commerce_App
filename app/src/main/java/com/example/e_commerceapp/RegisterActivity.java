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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button Create_Account_Btn;
    private EditText Input_Name,Input_Phone_Number,Input_Password;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Create_Account_Btn = findViewById(R.id.reg_btnID);
        Input_Name = findViewById(R.id.reg_name_editID);
        Input_Phone_Number = findViewById(R.id.reg_phone_editID);
        Input_Password = findViewById(R.id.reg_pasword_editID);

        Create_Account_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_account();
            }
        });


    }

    private void create_account() {
        String name=Input_Name.getText().toString();
        String phone=Input_Phone_Number.getText().toString().trim();
        String password=Input_Password.getText().toString().trim();
        loadingBar=new ProgressDialog(this);

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Name is empty ", Toast.LENGTH_SHORT).show();

        }else  if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Phone number is empty", Toast.LENGTH_SHORT).show();

        }else  if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();

        }else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("wait till creating account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validate_phone_number(name,phone,password);


        }


    }

    private void validate_phone_number(final String name, final String phone, final String password) {
        final DatabaseReference Root_Ref;
        Root_Ref= FirebaseDatabase.getInstance().getReference();

        Root_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String ,Object> user_data_map=new HashMap<>();
                    user_data_map.put("name",name);
                    user_data_map.put("phone", phone);
                    user_data_map.put("password", password);

                    Root_Ref.child("Users").child(phone).updateChildren(user_data_map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account is successfully created.", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(RegisterActivity.this,LogIn_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(RegisterActivity.this, phone+" already exists. ", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "please try with another phone number ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}