package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Confirm_Final_Order extends AppCompatActivity {
    private EditText Name,Number,Address,City_Name;
    private Button Confirm_Order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm__final__order);

        Name = findViewById(R.id.confirm_order_nameID);
        Number = findViewById(R.id.confirm_order_numberID);
        Address = findViewById(R.id.confirm_order_addressID);
        City_Name = findViewById(R.id.confirm_order_city_nameID);
        Confirm_Order = findViewById(R.id.confirm_order_btnID);


        String  price = getIntent().getStringExtra("total_price");
        Toast.makeText(this, ""+price, Toast.LENGTH_SHORT).show();



        Confirm_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Name.getText().toString())) {

                }else if (TextUtils.isEmpty(Number.getText().toString().trim())) {

                }else if (TextUtils.isEmpty(Address.getText().toString())) {

                }else if (TextUtils.isEmpty(City_Name.getText().toString())) {

                }else {
                    confirm_order();
                }
            }
        });

    }

    private void confirm_order() {
        String save_current_date,save_current_time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date_format=new SimpleDateFormat("MMM dd, yyyy");
        save_current_date=date_format.format(calendar.getTime());

        SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss a");
        save_current_time = time_format.format(calendar.getTime());

        final DatabaseReference order_ref = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.current_online_users.getPhone());

        HashMap<String, Object> order_map = new HashMap<>();
        order_map.put("Name", Name.getText().toString());
        order_map.put("Phone", Number.getText().toString().trim());
        order_map.put("Address", Address.getText().toString());
        order_map.put("City", City_Name.getText().toString());
        order_map.put("Price", getIntent().getStringExtra("total_price"));
        order_map.put("Date", save_current_date);
        order_map.put("Time",save_current_time );
        order_map.put("State","not shipped" );


        order_ref.updateChildren(order_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Confirm_Final_Order.this, "Order confirmation done successfully", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(Prevalent.current_online_users.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(Confirm_Final_Order.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }

                                }
                            });

                }

            }
        });

    }
}