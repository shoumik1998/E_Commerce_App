package com.example.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView Female_Dress,T_shirt,Sweater,Glass,Shoe,Hats,Bag,HeadPhones,Laptop,Mobile,Watch,Book;
    private Button Check_New_Orders,Maintain_product,Log_Out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Female_Dress = findViewById(R.id.female_dress_imgID);
        T_shirt = findViewById(R.id.sports_imgID);
        Sweater = findViewById(R.id.sweater);
        Glass = findViewById(R.id.glassID);
        Shoe = findViewById(R.id.shoeID);
        Hats = findViewById(R.id.hatsID);
        Bag = findViewById(R.id.purses_bagID);
        HeadPhones = findViewById(R.id.headPhonesID);
        Laptop = findViewById(R.id.laptopsID);
        Mobile = findViewById(R.id.mobileID);
        Watch = findViewById(R.id.watchID);
        Book = findViewById(R.id.bookID);
        Check_New_Orders = findViewById(R.id.new_order_btnID);
        Maintain_product = findViewById(R.id.product_maintain_btnID);
        Log_Out = findViewById(R.id.log_out_admin_btnID);

        Female_Dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Female Dresses");
                startActivity(intent);
            }
        });
        T_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Sports tShirt");
                startActivity(intent);
            }
        });
        Sweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Sweathers");
                startActivity(intent);
            }
        });
        Glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Glasses");
                startActivity(intent);
            }
        });
        Shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Shoes");
                startActivity(intent);
            }
        });
        Hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Hats Caps");
                startActivity(intent);
            }
        });
        Bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Wallets Bag Purses");
                startActivity(intent);
            }
        });
        HeadPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Headphones HandFree");
                startActivity(intent);
            }
        });
        Laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Laptops");
                startActivity(intent);
            }
        });
        Mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Mobile Phones");
                startActivity(intent);
            }
        });
        Watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Watches");
                startActivity(intent);
            }
        });
        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProduct.class);
                intent.putExtra("Category", "Books");
                startActivity(intent);
            }
        });

        Check_New_Orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);

            }
        });
        Maintain_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        Log_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}