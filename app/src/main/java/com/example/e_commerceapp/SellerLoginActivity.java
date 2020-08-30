package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    private EditText Seller_Log_Email,Seller_Log_Password;
    private Button Seller_Log_Btn;
    private TextView Seller_Log_Having_No_Acc;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        Seller_Log_Email = findViewById(R.id.seller_log_email_editID);
        Seller_Log_Password = findViewById(R.id.seller_log_password_editID);
        Seller_Log_Btn = findViewById(R.id.seller_log_btnID);
        Seller_Log_Having_No_Acc = findViewById(R.id.seller_log_having_no_acc);

        firebaseAuth = FirebaseAuth.getInstance();

        Seller_Log_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=Seller_Log_Email.getText().toString().trim();
                String password = Seller_Log_Password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SellerLoginActivity.this, "failed.."+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

        Seller_Log_Having_No_Acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SellerLoginActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}