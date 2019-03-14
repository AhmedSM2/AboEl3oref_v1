package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Area_Activirt extends AppCompatActivity
{

    private EditText area_name , area_lat ,area_lng;
    private Button area_save_btn , btn_intent_save_category;

    private DatabaseReference AbuEl3orif_DB_Ref ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__area__activirt);

        area_name = findViewById(R.id.area_name_txt);
        area_lat = findViewById(R.id.area_lat_txt);
        area_lng = findViewById(R.id.area_lng_txt);
        area_save_btn = findViewById(R.id.btn_save_area);
        btn_intent_save_category = findViewById(R.id.btn_save_category_intent);

        AbuEl3orif_DB_Ref  = FirebaseDatabase.getInstance().getReference().child("AbuEl3orifDB");

        area_save_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save_area_to_DB();
            }
        });

        btn_intent_save_category.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_to_save_category_activity();
            }
        });


    }

    private void go_to_save_category_activity()
    {
     Intent save_category_intent = new Intent(getApplicationContext() , Add_Category_Activity.class);
     startActivity(save_category_intent);
     finish();
    }

    private void save_area_to_DB()
    {
        String AreaName = area_name.getText().toString();
        double AreaLat = Double.parseDouble(area_lat.getText().toString());
        double AreaLng = Double.parseDouble(area_lng.getText().toString());

        Area_class Area_object = new Area_class(AreaName , AreaLat , AreaLng);

        FirebaseHelper fh = new FirebaseHelper(AbuEl3orif_DB_Ref);

        if(fh.save_Area_DB(Area_object))
        {
            Toast.makeText(this, "Area Saved to Database Successfully ...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Error occuired...", Toast.LENGTH_SHORT).show();
        }
    }
}
