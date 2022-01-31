
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class page1 extends AppCompatActivity {
    private Button creeCompte;
    private Button connecter;
    private EditText mail,code;
    private ProgressDialog LoadingBar;
    private String parentDbName="users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);

        connecter = (Button) findViewById(R.id.seconnecter);
        mail = (EditText) findViewById(R.id.username);
        code = (EditText) findViewById(R.id.motDePass);
        LoadingBar = new ProgressDialog(this);


//Lorsqu'on click sur le button se connecter pour s'inscrire une premiere fois :
        creeCompte = (Button) findViewById(R.id.sinscrir);
        creeCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(page1.this, inscription.class);
                startActivity(intent);
            }
        });
        //pour se connecter:


        //pour oublier mot de passe

        TextView motDePasseOub;
        motDePasseOub = (TextView) findViewById(R.id.OubMotDpass);
        motDePasseOub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(page1.this, oubpasse.class);
                startActivity(intent);
            }
        });

//verifiation de code et nutel et entrer ces derniers element a la base de donner:
        connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeConnecter();
            }

            private void SeConnecter() {
                String mail = page1.this.mail.getText().toString();
                String motdepasse = code.getText().toString();
                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(page1.this, "write your Email..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(motdepasse)) {
                    Toast.makeText(page1.this, "write your password..", Toast.LENGTH_SHORT).show();
                } else {
                    LoadingBar.setTitle("account verification");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();
                    AcceCompte(mail, motdepasse);
                }

            }

            private void AcceCompte(String mail, String motdepasse) {
                FirebaseAuth fAut;
                fAut = FirebaseAuth.getInstance();
                fAut.signInWithEmailAndPassword(mail, motdepasse).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoadingBar.dismiss();
                            Toast.makeText(page1.this, " successfully connected...", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(page1.this, Acceuil.class);
                            startActivity(intent);
                        } else {
                            LoadingBar.dismiss();
                            Toast.makeText(page1.this, "error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });
    }}



