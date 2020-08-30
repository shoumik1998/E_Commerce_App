package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerceapp.Model.Users;
import com.example.e_commerceapp.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button JoinBtn,LogInBtn;
    private  String parentDB="Users";
    private  ProgressDialog loadingBar;
    private TextView Seller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        loadingBar=new ProgressDialog(this);


        final String user_phone = Paper.book().read(Prevalent.USER_PHONE_KEY);
        final String user_password = Paper.book().read(Prevalent.USER_PASSWORD_KEY);
        if (!TextUtils.isEmpty(user_phone) && !TextUtils.isEmpty(user_password)){
            loadingBar.setTitle("Logging In ");
            loadingBar.setMessage("Wait till logged in");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            final DatabaseReference Root_Ref;
            Root_Ref= FirebaseDatabase.getInstance().getReference();

            Root_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(parentDB).child(user_phone).exists()){
                        Users user_data = snapshot.child(parentDB).child(user_phone).getValue(Users.class);
                        if (user_data.getPhone().equals(user_phone)){
                            if (user_data.getPassword().equals(user_password)){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Prevalent.current_online_users=user_data;
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            loadingBar.dismiss();
        }

        JoinBtn = findViewById(R.id.join_now_btnID);
        LogInBtn = findViewById(R.id.logIn_btnID);
        Seller = findViewById(R.id.want_seller_txtID);



        JoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        LogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LogIn_Activity.class);
                startActivity(intent);
            }
        });
        Seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            startActivity(intent);
            finish();

        }
    }
}