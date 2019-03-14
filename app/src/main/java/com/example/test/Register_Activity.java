package com.example.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_Activity extends AppCompatActivity
{
    private EditText user_email , user_password , user_confirm_password;
    private Button register_form_btn;
    private TextView login_link;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        user_email = findViewById(R.id.register_email_input);
        user_password = findViewById(R.id.register_password_input);
        user_confirm_password = findViewById(R.id.register_confirmPassword_input);
        register_form_btn = findViewById(R.id.register_btn);
        login_link = findViewById(R.id.register_login_link);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        login_link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToLoginActivity();
            }
        });

        register_form_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createNewAccount();
            }
        });
    }

    private void goToLoginActivity()
    {
        Intent loginActivityIntent = new Intent(getApplicationContext(),Login_Activity.class);
        startActivity(loginActivityIntent);
        finish();

    }

    private void createNewAccount()
    {
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();
        String confirm_password= user_confirm_password.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "please fill your email field ", Toast.LENGTH_SHORT).show();
        }


       else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please fill your password field ", Toast.LENGTH_SHORT).show();
        }


       else if(TextUtils.isEmpty(confirm_password))
        {
            Toast.makeText(this, "please fill your confirm password field ", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirm_password))
        {
            Toast.makeText(this, "password is not matching with confirm password", Toast.LENGTH_SHORT).show();
        }
        else
        {


         loadingBar.setTitle("Creating your new Account");
         loadingBar.setMessage("Please wait we are setup your new Account now ....");
         loadingBar.show();
         loadingBar.setCanceledOnTouchOutside(true);

         mAuth.createUserWithEmailAndPassword(email , password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                 {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task)
                     {
                         if(task.isSuccessful())
                         {
                             sendUserTosetupActivity();
                             Toast.makeText(Register_Activity.this, "your Account created successfully", Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();

                         }
                         else
                         {
                             String message = task.getException().getMessage();
                             Toast.makeText(Register_Activity.this, "an error occured : "+ message, Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();
                         }

                     }
                 });

        }
    }

    //send user to setup activity method---------------------
    private void sendUserTosetupActivity()
    {
        Intent setupIntent = new Intent(getApplicationContext() , setup_user_Activity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
    //-------------------------------------------------------
}
