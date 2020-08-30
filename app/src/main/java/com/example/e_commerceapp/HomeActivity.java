package com.example.e_commerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerceapp.Model.Products;
import com.example.e_commerceapp.Prevalent.Prevalent;
import com.example.e_commerceapp.ViewHolder.Product_ViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference product_ref;
    private RecyclerView Product_RecyclerView;
    private  FloatingActionButton Fab;
    private String admin_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        admin_key = getIntent().getStringExtra("Admin");
        product_ref = FirebaseDatabase.getInstance().getReference().child("Products");
        Product_RecyclerView = findViewById(R.id.user_recyclerID);
        Product_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Fab = (FloatingActionButton) findViewById(R.id.fab);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });


        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header_view = navigationView.getHeaderView(0);
        if (admin_key!=null) {
            if (admin_key.equals("Admin")) {

            }
        }else {
            TextView User_name = header_view.findViewById(R.id.user_name_textID);
            CircleImageView User_Profile_pic = header_view.findViewById(R.id.user_profile_image);
            User_name.setText(Prevalent.current_online_users.getName());
            Picasso.get().load(Prevalent.current_online_users.getImage()).placeholder(R.drawable.profile).into(User_Profile_pic);
        }

        fetching_product_images();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id=menuItem.getItemId();

        if (id==R.id.nav_cart){
            if (admin_key==null) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }


        }else if (id==R.id.nav_search){
            if (admin_key==null) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }


        }else if (id==R.id.nav_categories) {
            if (admin_key==null) {
                Toast.makeText(this, "Categories", Toast.LENGTH_SHORT).show();
            }




        }else if (id==R.id.nav_settings){
                if (admin_key==null) {

                    startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
            }


        }else if (id==R.id.nav_logout){
                if (admin_key==null) {
                    Paper.book().destroy();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }



        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private  void fetching_product_images(){
        admin_key = getIntent().getStringExtra("Admin");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(product_ref, Products.class).build();

        FirebaseRecyclerAdapter<Products, Product_ViewHolder> adapter=new
                FirebaseRecyclerAdapter<Products, Product_ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Product_ViewHolder holder, int position, @NonNull final Products model) {
                        holder.Product_name_txt.setText(model.getPname());
                        holder.Product_Price_txt.setText("$"+model.getPrice());
                        holder.Product_Description_txt.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.Product_Image);

                        holder.Product_Image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (admin_key!=null) {
                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainProduct.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }

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

        Product_RecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}