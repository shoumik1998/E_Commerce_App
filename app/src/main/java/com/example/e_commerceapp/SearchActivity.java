package com.example.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.e_commerceapp.Model.Products;
import com.example.e_commerceapp.ViewHolder.Product_ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView Recycler_View_Search;
    private EditText Search_EditText;
    private Button Search_Button;
    private DatabaseReference search_ref;
    private  String search_key_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_ref=FirebaseDatabase.getInstance().getReference().child("Products");
        Recycler_View_Search = findViewById(R.id.search_recyclerViewID);
        Recycler_View_Search.setLayoutManager(new LinearLayoutManager(this));
        Recycler_View_Search.setHasFixedSize(true);
        Search_EditText = findViewById(R.id.search_editTextID);
        Search_Button = findViewById(R.id.search_btnID);

       Search_Button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               search_key_word = Search_EditText.getText().toString();
               onStart();
           }
       });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(search_ref.orderByChild("pname").startAt(search_key_word), Products.class).build();

        FirebaseRecyclerAdapter<Products, Product_ViewHolder> adapter=new
                FirebaseRecyclerAdapter<Products, Product_ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Product_ViewHolder holder, int position, @NonNull final Products model) {
                        holder.Product_name_txt.setText(model.getPname());
                        holder.Product_Price_txt.setText(model.getPrice());
                        holder.Product_Description_txt.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.Product_Image);

                        holder.Product_Image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Product_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);

                        return new Product_ViewHolder(view);
                    }
                };

        Recycler_View_Search.setAdapter(adapter);
        adapter.startListening();

    }
}