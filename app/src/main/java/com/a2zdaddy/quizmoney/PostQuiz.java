package com.a2zdaddy.quizmoney;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostQuiz extends AppCompatActivity {

    ImageView postimage;

    private static final int GALLREQ = 1;

    EditText titletext, descriptiontext;

    Button submitbutton,imagebuttton,logoutbt,postbt;

    StorageReference storageReference=null;

    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference mref;

    Uri imageUri;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_quiz);

        titletext = (EditText) findViewById(R.id.post_title);

        descriptiontext = (EditText) findViewById(R.id.post_desc);

        submitbutton = (Button) findViewById(R.id.updatebtton);

        postbt= (Button) findViewById(R.id.getpostbt);

        imagebuttton= (Button) findViewById(R.id.imagbutton);

        postimage=(ImageView)findViewById(R.id.imageview);

        logoutbt= (Button) findViewById(R.id.logout);

        mAuth=FirebaseAuth.getInstance();


        //String usermobile = mAuth.getCurrentUser().getPhoneNumber();

        storageReference = FirebaseStorage.getInstance().getReference("User Posts");

        mref=FirebaseDatabase.getInstance().getReference().child("Post");





    }

    public void upldimage(View view)

    {
        mAuth=FirebaseAuth.getInstance();

        // Toast.makeText(PostQuiz.this,mAuth.getCurrentUser().getPhoneNumber(),Toast.LENGTH_SHORT).show();


        Intent gallery = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(gallery, GALLREQ);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLREQ && data != null && data.getData() != null) {
            imageUri = data.getData();
            postimage.setImageURI(imageUri);
        }


        else {
            Toast.makeText(PostQuiz.this,"Please select an image",Toast.LENGTH_SHORT).show();


        }
    }




    public void update(View view) {
        final String posttitleuser=titletext.getText().toString();
        final String postdesc=descriptiontext.getText().toString();

        if (!TextUtils.isEmpty(posttitleuser) && !TextUtils.isEmpty(postdesc) && postimage.getDrawable()!=null) {
            final ProgressDialog progressDialog=new ProgressDialog(PostQuiz.this);
            progressDialog.setTitle("Updating your post");
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference filepath = storageReference.child(imageUri.getLastPathSegment());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    try {


                        mAuth = FirebaseAuth.getInstance();

                        String usermobile = mAuth.getCurrentUser().getPhoneNumber();

                        titletext.setText("");

                        descriptiontext.setText("");

                        postimage.setImageDrawable(null);
                        //Toast.makeText(Createpost.this, "Image added", Toast.LENGTH_SHORT).show();

                        final Uri downloadurl = taskSnapshot.getDownloadUrl();

                        final DatabaseReference newpost = mref.push();

                        newpost.child("posttitle").setValue(posttitleuser);

                        newpost.child("postdesc").setValue(postdesc);

                        newpost.child("postimage").setValue(downloadurl.toString());


                        newpost.child("Created by").setValue(usermobile);

                        //cancelling the progress dialog after post added..
                        progressDialog.cancel();

                        Toast.makeText(PostQuiz.this, "Post updated successfully..", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                        Toast.makeText(PostQuiz.this, "something not right, retry..", Toast.LENGTH_SHORT).show();

                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(PostQuiz.this, "Please,choose an Image", Toast.LENGTH_SHORT).show();



                }
            });
        }


        else {

            Toast.makeText(PostQuiz.this,"Pic an image and fill all the feilds. "+mAuth.getCurrentUser().getPhoneNumber(),Toast.LENGTH_SHORT).show();

        }
    }


    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();

        Intent intent=new Intent(PostQuiz.this,WelcomeActivity.class);

        startActivity(intent);

        finish();


    }

    public void seepost(View view) {

        Intent intent2=new Intent(PostQuiz.this,QuizActivity.class);

        startActivity(intent2);



    }
}


