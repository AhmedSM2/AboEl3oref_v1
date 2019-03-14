package com.example.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class setup_user_Activity extends AppCompatActivity
{
    private EditText setup_username , setup_fullname , setup_address;
    private CircleImageView user_pp;
    private Button get_location_btn , setup_save_btn;
    private ProgressDialog loadingBar;

    final static int Gallery_pick = 1; // to used as second parametar in open phone gallery method
    private StorageReference userProfileImageRef; // to use to store the link of pp to firebase storage

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_user_);

        user_pp = findViewById(R.id.setup_user_pp);

        setup_username = findViewById(R.id.setup_user_username);
        setup_fullname = findViewById(R.id.setup_user_fullname);
        setup_address = findViewById(R.id.setup_user_Address);
        get_location_btn = findViewById(R.id.setup_user_location);
        setup_save_btn = findViewById(R.id.setup_user_save_btn);
        loadingBar = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

        //create folder to store images in firebase storage
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("users prifile images");


        //---------button action to save user informatiom method ---------------------------
        setup_save_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               save_setup_user_info();
            }
        });

        //------------------------------------------------------------------------------------

        //-----button action to open phone gallery method -------------------------------------
        user_pp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openGallery = new Intent();
                openGallery.setAction(Intent.ACTION_GET_CONTENT);
                openGallery.setType("image/*");
                startActivityForResult(openGallery , Gallery_pick);
            }
        });


        get_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_intent = new Intent(setup_user_Activity.this,MapsActivity.class);
                startActivity(map_intent);

            }
        });
        //--------------------------------------------------------------------------------------

        //-----------to display the user profile image------------------------------------------
        usersRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    final String userImage = dataSnapshot.child("profileImage").getValue().toString();
//                    Picasso.get()
//                            .load(userImage)
//                            .placeholder(R.drawable.profile_icon)
//                            .error(R.drawable.home_icon2)
//                            .resize(50, 50)
//                            .centerCrop()
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .into(user_pp);
                    Glide.with(getApplicationContext())
                    .load(userImage)
                    .apply(new RequestOptions()
                    .placeholder(R.drawable.profile_icon)
                    .fitCenter())
                    .into(user_pp);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        //--------------------------------------------------------------------------------------
    }


    //---------------method of croping image-----------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_pick && resultCode == RESULT_OK && data != null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result =CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                loadingBar.setTitle("profile image uploading");
                loadingBar.setMessage("please wait until your profile image uploading complete...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(setup_user_Activity.this, "profile image stored successfully to firebase storage", Toast.LENGTH_SHORT).show();

                            final String downloadUrl =task.getResult().getStorage().getDownloadUrl().toString();
                            usersRef.child("profileImage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Intent selfSetupIntent = new Intent(getApplicationContext(), setup_user_Activity.class);
                                                startActivity(selfSetupIntent);

                                                Toast.makeText(setup_user_Activity.this, "profile image stored to firebase database successfully", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(setup_user_Activity.this, "Error occured : "+message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(setup_user_Activity.this, "Error occured : Image can't cropped , try Again", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    //-------------------------------------------------------------------------------------------

    private void save_setup_user_info()
    {
        String username = setup_username.getText().toString();
        String fullname = setup_fullname.getText().toString();
        String address = setup_address.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "please fill username field", Toast.LENGTH_SHORT).show();
        }

       else if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "please fill fullname field", Toast.LENGTH_SHORT).show();
        }

       else if(TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "please fill Address field", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("complete Account setup");
            loadingBar.setMessage("please wait until your account setup complete...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

           user_class userClassObject = new user_class(username , fullname ,address);
            HashMap userMap = new HashMap();
            userMap.put("username",userClassObject.getUser_username());
            userMap.put("fullname",userClassObject.getUser_fullname());
            userMap.put("address",userClassObject.getUser_userAddress());

            usersRef.updateChildren(userMap)
                    .addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                sendUserToMainActivity();
                                Toast.makeText(setup_user_Activity.this, "your account setup finished successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(setup_user_Activity.this, "error occured : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });


        }
    }

    //send user to main activity after account is situped-------------------------
    private void sendUserToMainActivity()
    {
        Intent mainActivityIntent = new Intent(getApplicationContext() , MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }
    //-----------------------------------------------------------------------------
}
