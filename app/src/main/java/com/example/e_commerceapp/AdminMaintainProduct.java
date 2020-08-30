package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_commerceapp.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProduct extends AppCompatActivity {
    private ImageView Maintain_Product_Img;
    private EditText Maintain_product_Name,Maintain_productPrice,Maintain_product_Description;
    private Button Maintain_Product_Btn,Maintain_delete_Product;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        Maintain_Product_Img = findViewById(R.id.maintain_imgID);
        Maintain_product_Name = findViewById(R.id.maintain_product_nameID);
        Maintain_productPrice = findViewById(R.id.maintain_product_priceID);
        Maintain_product_Description = findViewById(R.id.maintain_product_descriptionID);
        Maintain_Product_Btn = findViewById(R.id.apply_change_BtnID);
        Maintain_delete_Product = findViewById(R.id.maintain_product_delete_btnID);

         pid = getIntent().getStringExtra("pid");

        final DatabaseReference maintain_product_ref = FirebaseDatabase.getInstance().getReference().child("Products");

        maintain_product_ref.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String p_name = snapshot.child("pname").getValue().toString();
                    String p_price = snapshot.child("price").getValue().toString();
                    String p_description = snapshot.child("description").getValue().toString();
                    String p_image = snapshot.child("image").getValue().toString();

                    Maintain_product_Name.setText(p_name);
                    Maintain_productPrice.setText(p_price);
                    Maintain_product_Description.setText(p_description);
                    Picasso.get().load(p_image).into(Maintain_Product_Img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Maintain_Product_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apply_changes();
            }
        });

        Maintain_delete_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("Products").child(pid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminMaintainProduct.this, "This Product deleted successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminMaintainProduct.this,HomeActivity.class));
                            finish();
                        }
                    }
                });
            }
        });

    }

    private void apply_changes() {
        String product_name = Maintain_product_Name.getText().toString();
        String product_price = Maintain_productPrice.getText().toString().trim();
        String product_description = Maintain_product_Description.getText().toString();

        HashMap<String, Object> update_map = new HashMap<>();
        update_map.put("pid", pid);
        update_map.put("pname", product_name);
        update_map.put("price", product_price);
        update_map.put("description", product_description);

        DatabaseReference update_ref = FirebaseDatabase.getInstance().getReference().child("Products");
        update_ref.child(pid).updateChildren(update_map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminMaintainProduct.this, "Applied Changes successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
    }
}