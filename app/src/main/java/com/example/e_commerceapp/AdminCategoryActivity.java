package com.example.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView Female_Dress,T_shirt,Sweater,Glass,Shoe,Hats,Bag,HeadPhones,Laptop,Mobile,Watch,Book;

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


    }
}