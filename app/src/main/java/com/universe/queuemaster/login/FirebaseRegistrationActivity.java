package com.universe.queuemaster.login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.universe.queuemaster.R;
public class FirebaseRegistrationActivity extends AppCompatActivity {

    private Button btnSignUp;
    private  Button btnSignIn;
    private EditText password;
    private EditText repassword;
    private EditText emailID;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        firebaseAuth=FirebaseAuth.getInstance();

        emailID= findViewById(R.id.email);
        password= findViewById(R.id.password);
        repassword= findViewById(R.id.repassword);
        btnSignUp= findViewById(R.id.btnsignup);
        btnSignIn= findViewById(R.id.btnsignin);

        progressDialog= new ProgressDialog(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent s= new Intent(FirebaseRegistrationActivity.this, FirebaseLoginActivity.class);
                startActivity(s);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUsers();

            }
        });

    }

    private void addUsers(){
        String email=emailID.getText().toString().trim();
        final String pass=password.getText().toString().trim();
        final String repass=repassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email ID", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(repass)){
            Toast.makeText(this,"Please enter confirm password", Toast.LENGTH_LONG).show();
            return;
        }
        if(!pass.equals(repass)){
            Toast.makeText(this,"Passwords doesn't match", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();

                    Intent segregate= new Intent(FirebaseRegistrationActivity.this, SegregationActivity.class);
                    startActivity(segregate);
                }
                else {
                    progressDialog.dismiss();

                    if (pass.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Make sure password is at least 6 characters long", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Account already exists \n / Email ID invalid", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

}