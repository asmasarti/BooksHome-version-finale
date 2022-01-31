package com.example.BooksHome;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class oubpasse extends AppCompatActivity {

    Button  bn1;
    EditText mail;
    private ProgressDialog gg;
    private String parentDbName="users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oubpasse);

        mail =(EditText) findViewById(R.id.NT);
        bn1=(Button) findViewById(R.id.btn_oubpa);

        bn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                verif();
            }
            private void verif()
            {
                FirebaseAuth auth;
                auth=FirebaseAuth.getInstance();
                String email = mail.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(oubpasse.this, "Ecrivez votre Email..", Toast.LENGTH_SHORT).show();
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    mail.setError("please provide valide email");
                    mail.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(oubpasse.this, "le nouveau mot de passe est transferet à votre email", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(oubpasse.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(oubpasse.this, "\n" + "réessayez une autre foi une erreur s'est produit", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }




        });
    }





}