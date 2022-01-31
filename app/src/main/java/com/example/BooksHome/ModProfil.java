package com.example.BooksHome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.BooksHome.Model.users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class ModProfil extends AppCompatActivity {
    EditText nom,prenom,email,phoneNo,adresse,ville;
    DatabaseReference reference;
    String _NOM,_PRENOM,_VILLE,_MAIL,_ADRESSE,_PHONE;
    FirebaseUser userf;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userID;
    ImageView imageProf;
    Uri imageUri;
    String myUri="";
    StorageTask uploadTask;
StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mod_profil);
        nom=(EditText) findViewById(R.id.nomEdit);
        prenom=(EditText) findViewById(R.id.prenomEdit);
        email=(EditText) findViewById(R.id.mailEdit);
        adresse=(EditText) findViewById(R.id.adressEdit);
        phoneNo=(EditText) findViewById(R.id.telEdit);
        ville=(EditText) findViewById(R.id.villeEdit);
        firebaseAuth=FirebaseAuth.getInstance();
        imageProf=(ImageView)findViewById(R.id.imagEdit);

        userf =firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        userID=userf.getUid();
        reference=FirebaseDatabase.getInstance().getReference("users");
        storageReference= FirebaseStorage.getInstance().getReference().child("image");
        showUserData();
        imageProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choisirProfile();

            }
        });
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

    }

    private void choisirProfile() {
        getUserInfo();
    }
    private void getUserInfo() {
      reference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0)
                {
                    String image=snapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(imageProf);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data !=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            imageProf.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this, "Error,Try again", Toast.LENGTH_SHORT).show();
        }
    }
    private void showUserData() {

        reference.child(userID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                users user =snapshot.getValue(users.class);
                if(userf!=null)
                {
                    _NOM= user.getNom();
                    _PRENOM= user.getPrenom();
                    _VILLE= user.getVille();
                    _PHONE= user.getTel();
                    _ADRESSE= user.getAdresse();
                    _MAIL= user.getMail();



                    nom.setText(_NOM);
                    prenom.setText(_PRENOM);
                    ville.setText(_VILLE);
                    phoneNo.setText(_PHONE);
                    adresse.setText(_ADRESSE);
                    email.setText(_MAIL);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void update(View view)
    {
        infochage();
        photochange();
        Intent intent=new Intent(this,Profil.class);
            startActivity(intent);
            Toast.makeText(this, "data has been updated..", Toast.LENGTH_SHORT).show();

    }

    private void photochange() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("set your profile");
        progressDialog.setMessage("please wait,while we are setting your photo");
        if(imageUri!=null)
        {
            final StorageReference fileRef=storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUrl=task.getResult();
                    myUri=downloadUrl.toString();
                    HashMap<String, Object> userMap=new HashMap<>();
                    userMap.put("image",myUri);
                    reference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(userMap);
                    progressDialog.dismiss();
                }
            });


        }
        else{
            progressDialog.dismiss();

            Toast.makeText(this,"image not selected",Toast.LENGTH_SHORT).show();
        }


    }




    private void infochage() {
        String firstName = nom.getText().toString();
        String lastName = prenom.getText().toString();
        String mail = email.getText().toString();
        String contactNo = phoneNo.getText().toString();
        String v = ville.getText().toString();
        String adre = adresse.getText().toString();
        users users=new users(lastName,firstName,mail,adre,contactNo,v);

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(users);
    }
}