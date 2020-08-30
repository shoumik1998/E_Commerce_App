package com.example.e_commerceapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.R;

public class User_Product_ViewHolder extends RecyclerView.ViewHolder {
    public TextView Product_Name,Product_Quantity,Product_Price;

    public User_Product_ViewHolder(@NonNull View itemView) {
        super(itemView);
        Product_Name = itemView.findViewById(R.id.admin_user_product_p_name_txtID);
        Product_Quantity = itemView.findViewById(R.id.admin_user_product_quantity_txtID);
        Product_Price = itemView.findViewById(R.id.admin_user_product_price_txtID);



    }
}
