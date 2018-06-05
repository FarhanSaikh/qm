package com.a2zdaddy.quizmoney;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class QuizActivity extends AppCompatActivity {

    public LinearLayoutManager mLayoutManager;
    RecyclerView mpostlist;
    DatabaseReference mDatabse;
    FirebaseAuth mAuth;
    AdView myad3;
    InterstitialAd interstitialAd;
    String post_keyref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
//        getSupportActionBar().hide();


        //banner ad on top of the post lis is here
        MobileAds.initialize(this,
                "ca-app-pub-8829795590534646/1759737259");
        AdRequest adRequest = new AdRequest.Builder().build();

        myad3=findViewById(R.id.adView3);
        myad3.loadAd(adRequest);


//done

        //interstitial ad code is here
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-7918328644005959/5916805505");
        interstitialAd.loadAd( new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Intent intent=new Intent(QuizActivity.this,PlayQuiz.class);
                intent.putExtra("post_id",post_keyref);
                startActivity(intent);
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });




        mpostlist=(RecyclerView) findViewById(R.id.recyclerview);
        mpostlist.setHasFixedSize(true);
        // mpostlist.setLayoutManager(new LinearLayoutManager(PostList.this));
        mLayoutManager = new LinearLayoutManager(QuizActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        // Now set the layout manager and the adapter to the RecyclerView
        mpostlist.setLayoutManager(mLayoutManager);

        mAuth=FirebaseAuth.getInstance();

        mDatabse= FirebaseDatabase.getInstance().getReference().child("Post");
        //floating button code is here..
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Redirecting to New Activity", Snackbar.LENGTH_LONG);

                Intent intent=new Intent(QuizActivity.this,PostQuiz.class);
                startActivity(intent);
                finish();

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        final ProgressDialog progressDialog = new ProgressDialog(QuizActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(true);
        progressDialog.show();

        FirebaseRecyclerAdapter<Post,PostViewFolder> PSVF=new FirebaseRecyclerAdapter<Post, PostViewFolder>(
                Post.class,
                R.layout.singlepostitem,
                PostViewFolder.class,
                mDatabse) {



            @Override
            protected void populateViewHolder(PostViewFolder viewHolder, Post model, int position) {

                viewHolder.setTitle(model.getPosttitle());
                viewHolder.setDesc(model.getPostdesc());
                viewHolder.setImage(getApplicationContext(),model.getPostimage());
                progressDialog.cancel();

                final String post_key= getRef(position).getKey();


                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_keyref=post_key;

                        if (interstitialAd.isLoaded()){

                            interstitialAd.show();

                        }
                        else {
                            Intent intent = new Intent(QuizActivity.this, PlayQuiz.class);
                            intent.putExtra("post_id", post_key);
                            startActivity(intent);

                        }
                    }
                });

            }
        };

        mpostlist.setAdapter(PSVF);


    }


    public static class PostViewFolder extends RecyclerView.ViewHolder{

        View mview;
        public PostViewFolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setTitle(String posttitle){
            TextView post_title=(TextView) mview.findViewById(R.id.posttitle);
            post_title.setText(posttitle);

        }

        public void setDesc(String postdesc){
            TextView post_desc=(TextView) mview.findViewById(R.id.postdescription);
            post_desc.setText(postdesc);


        }
        public void setImage(Context ctx, String postimage){

            ImageView post_image=(ImageView) mview.findViewById(R.id.postimage);

            Picasso.with(ctx).load(postimage).into(post_image);

        }


    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            AlertDialog.Builder builder = new AlertDialog.Builder(PostList.this);

            View view = LayoutInflater.from(PostList.this).inflate(R.layout.alertbox, null);

            TextView title = (TextView) view.findViewById(R.id.title);
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);

            title.setText("Hello There!");

            imageButton.setImageResource(R.drawable.ic_check_circle_black_24dp);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //logout function is here
                    logout();

                    Toast.makeText(PostList.this, "Logged Out", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(PostList.this, "Thank you", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setView(view);
            builder.show();


            return true;
        }
        if (id == R.id.action_about) {

            Intent intent=new Intent(PostList.this,MainActivity.class);
            startActivity(intent);


        }


        return super.onOptionsItemSelected(item);
    }

*/
    public void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent=new Intent(QuizActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

}

