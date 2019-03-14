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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity
{

    private EditText user_email_login , user_password_login;
    private Button login_button;
    private TextView register_link;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        user_email_login = findViewById(R.id.login_email_input);
        user_password_login = findViewById(R.id.login_password_input);
        login_button = findViewById(R.id.login_btn);
        register_link = findViewById(R.id.login_register_link);
        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        //link to send user to register activity--------------------------------
        register_link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendUserToRegisterActiity();
            }
        });
        //-----------------------------------------------------------------------

        //--------login button action--------------------------------------------
        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                user_login_method();
            }
        });
        //------------------------------------------------------------------------
    }

    //-------------user login method-----------------------------
    private void user_login_method()
    {
        String email = user_email_login.getText().toString();
        String password = user_password_login.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "please fill email feild", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please fill password feild", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login to Account");
            loadingBar.setMessage("please wait until you login to your account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                sendUserToMainActivity();

                                Toast.makeText(Login_Activity.this, "you are logged in successfully ", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login_Activity.this, "error occured : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }

    //-------------send user to login activity ---------------------
    private void sendUserToMainActivity()
    {
        Intent mainActivityIntent = new Intent(getApplicationContext() , MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }
    //--------------------------------------------------------------


    //-----------------------------------------------------------

    //-------------link to register Activity----------------------
    private  void sendUserToRegisterActiity()
    {
        Intent goToRegisterActivity = new Intent(getApplicationContext(),Register_Activity.class);
        startActivity(goToRegisterActivity);
        finish();
    }
    //------------------------------------------------------------
}
