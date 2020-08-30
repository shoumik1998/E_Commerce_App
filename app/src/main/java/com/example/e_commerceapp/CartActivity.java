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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerceapp.Model.Cart;
import com.example.e_commerceapp.Prevalent.Prevalent;
import com.example.e_commerceapp.ViewHolder.Cart_View_Holder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
    private TextView Total_Price,Msg;
    private RecyclerView RecyclerView;
    private RecyclerView.LayoutManager LayoutManager;
    private Button Next_Btn;
    private  int total_price=0;
    private  int total_price_identical_product;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Total_Price = findViewById(R.id.total_price_txtID);
        Msg = findViewById(R.id.msg_txtID);
        RecyclerView = findViewById(R.id.cart_recyclerID);
        Next_Btn = findViewById(R.id.cart_next_btnID);
        LayoutManager = new LinearLayoutManager(this);
        RecyclerView.setLayoutManager(LayoutManager);
        RecyclerView.setHasFixedSize(true);

        final DatabaseReference cart_list_rf= FirebaseDatabase.getInstance().getReference()
                .child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cart_list_rf.child("User View")
                        .child(Prevalent.current_online_users.getPhone())
                        .child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, Cart_View_Holder> adapter=new FirebaseRecyclerAdapter<Cart, Cart_View_Holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Cart_View_Holder holder, int position, @NonNull final Cart model) {
                holder.Product_Name_Item.setText("Product Name : "+model.getPname());
                holder.Product_Quantity_Item.setText("Product Quantity : "+model.getQuantity());
                holder.Product_Price_Item.setText("Product price : $"+model.getPrice());

                total_price_identical_product = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                total_price = total_price + total_price_identical_product;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]=new CharSequence[]{ "Edit","Remove"};
                        final AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i==0){
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }
                                else if (i==1){
                                    cart_list_rf.child("User View")
                                            .child(Prevalent.current_online_users.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(CartActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();

                    }
                });
            }

            @NonNull
            @Override
            public Cart_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_items,parent,false);
                return new Cart_View_Holder(view);
            }
        };

        RecyclerView.setAdapter(adapter);
        adapter.startListening();

        Next_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Total_Price.setText("$"+total_price);
                Intent intent = new Intent(CartActivity.this, Confirm_Final_Order.class);
                intent.putExtra("total_price", String.valueOf(total_price));
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       check_order_state();
    }


    private   void check_order_state(){
        final DatabaseReference state_ref=FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.current_online_users.getPhone());
        state_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String shipping_state = snapshot.child("State").getValue().toString();
                    String user_name = snapshot.child("Name").getValue().toString();

                    if (shipping_state.equals("shipped")) {
                        Total_Price.setText("Dear "+user_name+" your order is shipped successfully");
                        RecyclerView.setVisibility(View.GONE);
                        Msg.setVisibility(View.VISIBLE);
                        Msg.setText("Congratulations, Your order has been shipped successfully");
                        Next_Btn.setVisibility(View.GONE);


                    } else if (shipping_state.equals("not shipped")) {
                        Total_Price.setText("Order not shipped still now");
                        RecyclerView.setVisibility(View.GONE);
                        Msg.setVisibility(View.VISIBLE);
                        Next_Btn.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}