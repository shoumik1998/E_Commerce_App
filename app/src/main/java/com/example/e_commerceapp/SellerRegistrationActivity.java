package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private EditText Seller_Name,Seller_Phone,Seller_Email,Seller_Password,Seller_Shop_Address;
    private Button Seller_Registration;
    private TextView Seller_Having_Account;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        Seller_Name = findViewById(R.id.seller_name_editID);
        Seller_Phone = findViewById(R.id.seller_phone_editID);
        Seller_Email = findViewById(R.id.seller_email_editID);
        Seller_Password = findViewById(R.id.seller_password_editID);
        Seller_Shop_Address = findViewById(R.id.seller_shop_address_editID);
        Seller_Registration = findViewById(R.id.seller_reg_btnID);
        Seller_Having_Account = findViewById(R.id.seller_having_acc_txtID);

        firebaseAuth = FirebaseAuth.getInstance();

        Seller_Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seller_register();
            }
        });

        Seller_Having_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);

            }
        });


    }

    private void seller_register() {
        final String name = Seller_Name.getText().toString();
        final String phone_number = Seller_Phone.getText().toString().trim();
        final String email = Seller_Email.getText().toString().trim();
        final String password = Seller_Password.getText().toString().trim();
        final String shop_address = Seller_Shop_Address.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is empty" , Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(phone_number)) {
            Toast.makeText(this, "Phone number is empty" , Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is empty" , Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty" , Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(shop_address)) {
            Toast.makeText(this, "Shop address is empty" , Toast.LENGTH_SHORT).show();

        }else {
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String seller_id = firebaseAuth.getUid();
                                DatabaseReference seller_ref = FirebaseDatabase.getInstance().getReference();
                                HashMap<String, Object> seller_info = new HashMap<>();
                                seller_info.put("sid", seller_id);
                                seller_info.put("name", name);
                                seller_info.put("phone", phone_number);
                                seller_info.put("emil", email);
                                seller_info.put("password", password);
                                seller_info.put("shop_address", shop_address);

                                seller_ref.child("Seller").child(seller_id).updateChildren(seller_info)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SellerRegistrationActivity.this, "Congratulations, Registered successfully.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SellerRegistrationActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SellerRegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SellerRegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}