package com.example.BooksHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.BooksHome.Model.users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profil extends AppCompatActivity {
    FirebaseUser userf;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userID;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    TextView nm,prenom,ville,tel,mail,adresse;
    ImageView modpr;
    StorageReference storageReference;
    ImageView prof;
    ImageView poster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
// menu:
        //Initialize variables
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.profil);
        //Perform item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,Acceuil.class));
                        overridePendingTransition(0,0);
                        return true ;
                    case R.id.profil:
                        return true ;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext()
                                ,Add.class));
                        overridePendingTransition(0,0);
                        return true ;
                    case R.id.favorite:
                        startActivity(new Intent(getApplicationContext()
                                ,Favorit.class));
                        overridePendingTransition(0,0);
                        return true ;
                    case R.id.shopping:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });

        ////////////////////////////afficher info : ///////////////////////////////////////

        firebaseAuth=FirebaseAuth.getInstance();
        userf =firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");
        userID=userf.getUid();
        nm=(TextView) findViewById(R.id.nomProf);
        prenom=(TextView) findViewById(R.id.prenomProf);
        mail=(TextView) findViewById(R.id.mailProf);
        adresse=(TextView)findViewById(R.id.adresseProf);
        tel=(TextView) findViewById(R.id.telProf);
        ville=(TextView) findViewById(R.id.villeProf);
        modpr=(ImageView) findViewById(R.id.modifPro);
        poster=(ImageView)findViewById(R.id.imageViewP);
        //pour poster :
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profil.this,Add.class);
                startActivity(intent);
            }
        });

modpr.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(Profil.this,ModProfil.class);
        startActivity(intent);
    }
});

        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                users userprof=snapshot.getValue(users.class);
                Intent intent=getIntent();
                if(userf!=null)
                {
                    String name=userprof.getNom();
                    String p=userprof.getPrenom();
                    String vil=userprof.getVille();
                    String tell=userprof.getTel();
                    String ad=userprof.getAdresse();
                    String email=userprof.getMail();



                    nm.setText(name);
                    prenom.setText(p);
                    mail.setText(email);
                    adresse.setText(ad);
                    tel.setText(tell);
                    ville.setText(vil);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


}


}

