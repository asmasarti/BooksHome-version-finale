package com.example.BooksHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.BooksHome.Model.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class inscription extends AppCompatActivity
{
    private Button inscrept;
    private EditText prenom,nom, email,adresse,motdepasse,confirmemotdepass,tel,ville;
    private ProgressDialog LoadingBar;
FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        inscrept=(Button) findViewById(R.id.inscrire);
        prenom=(EditText) findViewById(R.id.prenom);
        nom=(EditText) findViewById(R.id.nom);
        email =(EditText) findViewById(R.id.mail);
        adresse=(EditText) findViewById(R.id.adresse);
        motdepasse=(EditText) findViewById(R.id.motdepasse);
        confirmemotdepass=(EditText) findViewById(R.id.confirmemotdepasse);
        tel=(EditText) findViewById(R.id.tel);
        ville=(EditText) findViewById(R.id.ville);
        LoadingBar=new ProgressDialog(this);
        fAuth=FirebaseAuth.getInstance();

        inscrept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }
    private void CreateAccount()
    {
        String nom = this.nom.getText().toString().trim();
        String prenom = this.prenom.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String adresse = this.adresse.getText().toString().trim();
        String motDePasse =this.motdepasse.getText().toString().trim();
        String confirmerPasse =this.confirmemotdepass.getText().toString().trim();
        String tel =this.tel.getText().toString().trim();
        String ville =this.ville.getText().toString().trim();
        if(TextUtils.isEmpty(nom))
        {
            Toast.makeText(this,"write your first name...",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(prenom))
        {
            Toast.makeText(this,"write your last name...",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"write your Email..",Toast.LENGTH_LONG).show();
        }

        if(TextUtils.isEmpty(adresse))
        {
            Toast.makeText(this,"write your address...",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(motDePasse))
        {
            Toast.makeText(this,"write your  password...",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(confirmerPasse))
        {
            Toast.makeText(this,"Confirm your password...",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(tel))
        {
            Toast.makeText(this,"write your phone number...",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(ville))
        {
            Toast.makeText(this,"write your city...",Toast.LENGTH_LONG).show();
        }


        else {
            LoadingBar.setTitle("Account creation..");
            LoadingBar.setMessage("we verify credentials...");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            if (motDePasse.equals(confirmerPasse)){

                fAuth.createUserWithEmailAndPassword(email, motDePasse).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoadingBar.dismiss();
                            users users =new users(prenom, nom, email,  adresse, motDePasse, confirmerPasse,tel,ville);
                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        LoadingBar.dismiss();
                                        Toast.makeText(inscription.this, "Congratulation!you have created your account", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(inscription.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else {
                            LoadingBar.dismiss();
                            Toast.makeText(inscription.this, "error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }else{
                LoadingBar.dismiss();
                Toast.makeText(inscription.this, "le mot de passe n'est pas confirmer correctement", Toast.LENGTH_LONG).show();

            }
        }



        }}