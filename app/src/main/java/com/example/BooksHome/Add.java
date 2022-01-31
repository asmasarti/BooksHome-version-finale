package com.example.BooksHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BooksHome.Model.users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Add<pubilc> extends AppCompatActivity  {
    private Spinner spinnerCat;
    private Spinner spinnerType;
    ArrayAdapter<String> arrayAdapterCteg;
    ArrayAdapter<String> arrayAdapterType;
   String usnom,uspre,usville,usmail,ustel,usad;

    String[] Categories ={"Personal development","Programming","Languages","Novels","School books","Others"};
    String[] Types ={"Sell","Exchange"};

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private Uri mImageUri;
    private TextView mAuthor ;
    FirebaseUser userf;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userID;
    private ProgressBar mProgressBar;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,reference;
    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //menu:
        //Initialize variables
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);
        //Set Home
        bottomNavigationView.setSelectedItemId(R.id.add);
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
                        startActivity(new Intent(getApplicationContext()
                                ,Profil.class));
                        overridePendingTransition(0,0);
                        return true ;
                    case R.id.add:
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
                        return true ;
                }
                return false;
            }
        });

        /////////////////////////////////////////////////////////
        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.button_choose_image);
        mAuthor =findViewById(R.id.edit_author);
        firebaseAuth= FirebaseAuth.getInstance();
        userf =firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        userID=userf.getUid();
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        reference=FirebaseDatabase.getInstance().getReference("users");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(Add.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {

                    uploadFile();

                }
            }
        });
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });
        ////////////////////

        //////////////////////
        //Spinner Categories
        spinnerCat =findViewById(R.id.categorie);
        arrayAdapterCteg = new ArrayAdapter<String>(Add.this, android.R.layout.simple_spinner_dropdown_item,Categories);
        spinnerCat.setAdapter(arrayAdapterCteg);

        //spinnerCat types
        spinnerType =findViewById(R.id.typeEchVend);
        arrayAdapterType=new ArrayAdapter<String>(Add.this, android.R.layout.simple_spinner_dropdown_item,Types);
        spinnerType.setAdapter(arrayAdapterType);
    }



    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {


        if (mImageUri != null) {

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(Add.this, "Upload successful", Toast.LENGTH_LONG).show();


                            reference.child(userID).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    users userprof = snapshot.getValue(users.class);
                                    if(userf!=null) {
                                        usnom = userprof.getNom();
                                        usmail = userprof.getMail();
                                        uspre = userprof.getPrenom();
                                        usad = userprof.getAdresse();
                                        ustel = userprof.getTel();
                                        usville = userprof.getVille();
                                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!urlTask.isSuccessful());
                                        Uri downloadUrl = urlTask.getResult();

                                        Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString(),
                                                spinnerType.getSelectedItem().toString(),spinnerCat.getSelectedItem().toString(),mAuthor.getText().toString().trim(),uspre,usnom,usmail,usad,ustel,usville);


                                        String uploadId = mDatabaseRef.push().getKey();
                                        mDatabaseRef.child(uploadId).setValue(upload);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });







                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void openImagesActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }
}