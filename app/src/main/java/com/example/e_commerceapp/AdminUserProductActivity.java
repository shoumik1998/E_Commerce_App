package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerceapp.Model.Cart;
import com.example.e_commerceapp.Prevalent.Prevalent;
import com.example.e_commerceapp.ViewHolder.User_Product_ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductActivity extends AppCompatActivity {
    private RecyclerView Recycler_View;
    private DatabaseReference admin_user_product_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);

        String uid = getIntent().getStringExtra("uid");
        Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
        Recycler_View = findViewById(R.id.admin_user_product_recyclerID);
        admin_user_product_ref= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(uid).child("Products");
        Recycler_View.setHasFixedSize(true);
        Recycler_View.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(admin_user_product_ref,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, User_Product_ViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, User_Product_ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull User_Product_ViewHolder holder, int position, @NonNull Cart model) {
                holder.Product_Name.setText(""+model.getPname());
                holder.Product_Quantity.setText("Quantity : "+model.getQuantity());
                holder.Product_Price.setText(""+model.getPrice()+"$");


            }

            @NonNull
            @Override
            public User_Product_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_usr_product_layout, parent, false);
                return new User_Product_ViewHolder(view);
            }
        };

        Recycler_View.setAdapter(adapter);
        adapter.startListening();


    }
}