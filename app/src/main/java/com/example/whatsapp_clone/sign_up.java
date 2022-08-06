package com.example.whatsapp_clone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsapp_clone.Models.Users;
import com.example.whatsapp_clone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(sign_up.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("we're creating your account.");

        binding.btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.txtusername.getText().toString().isEmpty() && !binding.txtemail.getText().toString().isEmpty() && !binding.txtpassword.getText().toString().isEmpty()){

                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtemail.getText().toString() , binding.txtpassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){

                                        Users user = new Users(binding.txtusername.getText().toString(),binding.txtemail.getText().toString(),binding.txtpassword.getText().toString());
                                        String Id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(Id).setValue(user);

                                        Toast.makeText(sign_up.this,"Sign Up Successful", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(sign_up.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(sign_up.this,"Enter Credential", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sign_up.this, sign_in.class);
                startActivity(intent);
            }
        });
    }
}