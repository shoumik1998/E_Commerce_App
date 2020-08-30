package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.e_commerceapp.Model.Products;
import com.example.e_commerceapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView Product_ImgView;
    private TextView Product_Name,Product_Desc,Product_Price;
    private ElegantNumberButton Counter_Btn;
    private FloatingActionButton Fat;
    private  String product_id,state="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Product_ImgView = findViewById(R.id.pd_imageID);
        Product_Name = findViewById(R.id.pd_product_name_txtID);
        Product_Desc = findViewById(R.id.pd_product_des_txtID);
        Product_Price = findViewById(R.id.pd_product_price_txtID);
        Counter_Btn = findViewById(R.id.elegent_btnID);
        Fat = findViewById(R.id.pd_fatID);

        product_id = getIntent().getStringExtra("pid");
        get_product_details(product_id);
        Fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (state.equals("order placed") || state.equals("order shipped")) {
                    Toast.makeText(ProductDetailsActivity.this, "You can select products to order only after your previous order is shipped successfully.", Toast.LENGTH_LONG).show();
                } else {
                    adding_to_cart_list();
                }
            }
        });



check_order_state();

    }

    private void adding_to_cart_list() {
        String save_current_date,save_current_time;

        Calendar cal_for_date = Calendar.getInstance();
        SimpleDateFormat current_date = new SimpleDateFormat("MM dd,yyyy");
        save_current_date = current_date.format(cal_for_date.getTime());

        SimpleDateFormat current_time = new SimpleDateFormat("HH:mm:ss a");
        save_current_time = current_time.format(cal_for_date.getTime());

       final  DatabaseReference cart_list_ref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String ,Object> cart_map=new HashMap<>();
        cart_map.put("pid", product_id);
        cart_map.put("pname", Product_Name.getText().toString());
        cart_map.put("price", Product_Price.getText().toString());
        cart_map.put("date", save_current_date);
        cart_map.put("time", save_current_time);
        cart_map.put("quantity", Counter_Btn.getNumber());
        cart_map.put("discount","");

        cart_list_ref.child("User View").child(Prevalent.current_online_users.getPhone())
                .child("Products").child(product_id)
                .updateChildren(cart_map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){

                       cart_list_ref.child("Admin View").child(Prevalent.current_online_users.getPhone())
                               .child("Products").child(product_id)
                               .updateChildren(cart_map)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                           startActivity(new Intent(ProductDetailsActivity.this,HomeActivity.class));
                                       }
                                   }
                               });

                   }
                    }
                });



    }

    private void get_product_details(String product_id) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Products");

        ref.child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
                    Product_Name.setText(products.getPname());
                    Product_Desc.setText(products.getDescription());
                    Product_Price.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(Product_ImgView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private   void check_order_state(){
        final DatabaseReference state_ref=FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.current_online_users.getPhone());
        state_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shipping_state = snapshot.child("State").getValue().toString();


                    if (shipping_state.equals("shipped")) {
                        state = "order shipped";

                    } else if (shipping_state.equals("not shipped")) {
                        state = "order placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}