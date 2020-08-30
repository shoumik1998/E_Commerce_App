package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerceapp.Model.Admin_Orders;
import com.example.e_commerceapp.ViewHolder.Order_ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {
    private RecyclerView RecyclerView_Orders;
    private DatabaseReference Admin_Order_Ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        RecyclerView_Orders = findViewById(R.id.admin_orders_recycler_ID);
        RecyclerView_Orders.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView_Orders.setHasFixedSize(true);

        Admin_Order_Ref = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<Admin_Orders> options=new FirebaseRecyclerOptions.Builder<Admin_Orders>()
                .setQuery(Admin_Order_Ref,Admin_Orders.class).build();

        FirebaseRecyclerAdapter<Admin_Orders, Order_ViewHolder> adapter=new FirebaseRecyclerAdapter<Admin_Orders, Order_ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Order_ViewHolder holder, final int position, @NonNull final Admin_Orders model) {
                holder.User_Name.setText("Name : "+model.getName());
                holder.User_Phone.setText("Phone "+model.getPhone());
                holder.Price.setText("Price : "+model.getPrice());
                holder.Address.setText("Address : "+model.getAddress());
                holder.City.setText("City : "+model.getCity());
                holder.Date_Time.setText(model.getDate()+"  "+model.getTime());
               holder.See_Ordered_Products.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String uid = getRef(position).getKey();
                       Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductActivity.class);
                       intent.putExtra("uid", uid);
                       startActivity(intent);
                   }
               });

               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       CharSequence options[] = new CharSequence[]{"YES", "NO"};
                       AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                       builder.setTitle("Have you shipped product properly ?");
                       builder.setItems(options, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               if (i == 0) {
                                   String uid = getRef(position).getKey();
                                   Admin_Order_Ref.child(uid).removeValue();
                               } else if (i == 1) {
                                   finish();
                               }

                           }
                       }).show();

                   }
               });

            }



            @NonNull
            @Override
            public Order_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orders_layout, parent, false);

                return new Order_ViewHolder(view);
            }
        };

        RecyclerView_Orders.setAdapter(adapter);
        adapter.startListening();
    }
}