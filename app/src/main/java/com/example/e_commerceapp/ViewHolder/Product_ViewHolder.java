package com.example.e_commerceapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Interfaces.Item_Click_Listener;
import com.example.e_commerceapp.R;

public class Product_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView Product_name_txt,Product_Price_txt,Product_Description_txt;
            public ImageView Product_Image;
            public   Item_Click_Listener listener;

    public Product_ViewHolder(@NonNull View itemView) {
        super(itemView);

        Product_name_txt =  itemView.findViewById(R.id.product_name_textID);
        Product_Price_txt =  itemView.findViewById(R.id.product_price_textID);
        Product_Description_txt = itemView.findViewById(R.id.product_description_textID);
        Product_Image = itemView.findViewById(R.id.product_imageID);

    }

    public  void setItem_Click_Listener(Item_Click_Listener listener){
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {
                listener.onClick(view,getAdapterPosition(),false);
    }
}
