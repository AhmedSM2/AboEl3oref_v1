package com.example.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Add_Category_Activity extends AppCompatActivity
{
    private EditText ca_name , ca_type , ca_rate , ca_lat , ca_lng;
    private Button save_ca_btn , btn_show_ca;
    private DatabaseReference AbuEl3orif_DB_Ref , AbuEl3orif_DB_Ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__category_);

        ca_name = findViewById(R.id.category_name_txt);
        ca_type = findViewById(R.id.category_type_txt);
        ca_rate = findViewById(R.id.category_rate_txt);
        ca_lat = findViewById(R.id.category_lat_txt);
        ca_lng = findViewById(R.id.category_lng_txt);
        save_ca_btn = findViewById(R.id.btn_save_category);
        btn_show_ca = findViewById(R.id.btn_show_category);

        AbuEl3orif_DB_Ref  = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB").child("Areas");

        AbuEl3orif_DB_Ref2  = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB");



        save_ca_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                save_category_DB();
            }
        });

        btn_show_ca.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
              x();
            }
        });


    }

    private void x()
    {
        ArrayList<String> AllCategories =new ArrayList<>();

        FirebaseHelper fh = new FirebaseHelper(AbuEl3orif_DB_Ref);

        AllCategories = fh.retrieve_All_Areas_DB();

        for(int i =0 ; i <= AllCategories.size() ; i++)
        {
            Toast.makeText(Add_Category_Activity.this, "resturant name : " + AllCategories.get(i), Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this,"number :  "  + AllCategories.size(), Toast.LENGTH_SHORT).show();
    }

    private void save_category_DB()
    {
        String categoryName = ca_name.getText().toString();
        int categoryType = Integer.parseInt(ca_type.getText().toString());
        int categoryRate = Integer.parseInt(ca_rate.getText().toString());
        double categoryLat = Double.parseDouble(ca_lat.getText().toString());
        double categoryLng = Double.parseDouble(ca_lng.getText().toString());

        Category_class category_object = new Category_class(categoryName , categoryRate , categoryType , categoryLat ,categoryLng);
        FirebaseHelper fh = new FirebaseHelper(AbuEl3orif_DB_Ref);
        
        if(fh.save_Category_DB(category_object))
        {
            Toast.makeText(this, "Category Saved to DB Successfully...", Toast.LENGTH_SHORT).show();
        }
        else 
        {
            Toast.makeText(this, "Error occuried", Toast.LENGTH_SHORT).show();
        }

    }
}
