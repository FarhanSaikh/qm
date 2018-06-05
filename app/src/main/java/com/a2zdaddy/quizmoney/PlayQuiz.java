package com.a2zdaddy.quizmoney;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Timer;

public class PlayQuiz extends AppCompatActivity {
    DatabaseReference mQusetionRef, mStatousref;
    private TextView mScoreView, questioncountno;
    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;
    private Button mButtonChoice4, quit;
    private TextView mTextViewCountDown;
    private long fixedtime = 2000;
    int reward = 50;
    private Button pointsbtn;
    Dialog dialog;
    private long START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS = 600000;
    long questionnocount, countq;
    DatabaseReference findtime;
    String poost_key = null;
    private String qno;
    private String mAnswer;
    private int mScore = 0;
    public int mQuestionNumber = 0;
    public String timeLeftFormatted;
    AdView myad, myad2;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);
        getSupportActionBar().hide();


        mTextViewCountDown = findViewById(R.id.text_view_countdown);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // START_TIME_IN_MILLIS=questionnocount*fixedtime;

        quit = findViewById(R.id.quit);
        questioncountno = findViewById(R.id.qustioncount);
        mScoreView = (TextView) findViewById(R.id.score);
        mQuestionView = (TextView) findViewById(R.id.question);
        mButtonChoice1 = (Button) findViewById(R.id.choice1);
        mButtonChoice2 = (Button) findViewById(R.id.choice2);
        mButtonChoice3 = (Button) findViewById(R.id.choice3);
        mButtonChoice4 = findViewById(R.id.choice4);
        myad = findViewById(R.id.adView);
        //myad2=findViewById(R.id.adView2);
        pointsbtn = findViewById(R.id.msgg1);
        startquizdialog();
        updateQuestion();


        //ad code is here
        MobileAds.initialize(this,
                "ca-app-pub-8829795590534646/1759737259");
        AdRequest adRequest = new AdRequest.Builder().build();
        myad.loadAd(adRequest);


        //done

        //interstitial ad code is here
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-8829795590534646/5879231076");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                dialog.dismiss();
                final ProgressDialog progressDialog = new ProgressDialog(PlayQuiz.this);
                progressDialog.setMessage("Please wait..");
                progressDialog.getActionBar();
                progressDialog.setCancelable(false);
                progressDialog.show();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference mref = FirebaseDatabase.getInstance().getReference(uid).child("walletcoin");
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String walletvalue = dataSnapshot.getValue(String.class);
                        Integer walletcoininint = Integer.parseInt(walletvalue);
                        Integer updatedcoin = (walletcoininint + reward);
                        mref.setValue(String.valueOf(updatedcoin));
                        pointsbtn.setText(String.valueOf(updatedcoin));
                        pointsbtn.setVisibility(View.VISIBLE);

                        progressDialog.cancel();
                        Toast.makeText(PlayQuiz.this, "Rewards added", Toast.LENGTH_SHORT).show();



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(PlayQuiz.this, "Error", Toast.LENGTH_SHORT).show();


                    }
                });

                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });


        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlayQuiz.this, QuizActivity.class);
                intent.putExtra("score", Integer.toString(mScore));
                intent.putExtra("outof", qno);

                startActivity(intent);
                finish();


            }
        });


        //Start of Button Listener for Button1
        mButtonChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                if (mButtonChoice1.getText().equals(mAnswer)) {
                    // mButtonChoice1.setBackgroundColor(Color.BLUE);
                    // mButtonChoice1.setBackgroundDrawable(getResources().getDrawable(R.drawable.oval));
                    mButtonChoice1.setTextColor(Color.BLUE);
                    rewards();
                    mScore = mScore + 1;
                    updateScore(mScore);
                    //This line of code is optiona
                    //Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                    updateQuestion();

                } else {
                    //                  mButtonChoice1.setBackgroundColor(Color.RED);
                    mButtonChoice1.setTextColor(Color.RED);

                    Toast.makeText(PlayQuiz.this, "Wrong, Ans is: " + mAnswer, Toast.LENGTH_SHORT).show();
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button1

        //Start of Button Listener for Button2
        mButtonChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                if (mButtonChoice2.getText().equals(mAnswer)) {
                    //mButtonChoice2.setBackgroundColor(Color.BLUE);

                    mButtonChoice2.setTextColor(Color.BLUE);
                    rewards();
                    mScore = mScore + 1;
                    updateScore(mScore);
                    //This line of code is optiona
                    //Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();

                    updateQuestion();


                } else {
//                    mButtonChoice2.setBackgroundColor(Color.RED);
                    mButtonChoice2.setTextColor(Color.RED);

                    Toast.makeText(PlayQuiz.this, "Wrong, Ans was: " + mAnswer, Toast.LENGTH_SHORT).show();
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button2


        //Start of Button Listener for Button3
        mButtonChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                if (mButtonChoice3.getText().equals(mAnswer)) {
                    //mButtonChoice3.setBackgroundColor(Color.BLUE);
                    mButtonChoice3.setTextColor(Color.BLUE);
                    rewards();
                    mScore = mScore + 1;
                    updateScore(mScore);
                    //This line of code is optiona
                    //Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                    updateQuestion();


                } else {
                    //mButtonChoice3.setBackgroundColor(Color.RED);
                    mButtonChoice3.setTextColor(Color.RED);

                    Toast.makeText(PlayQuiz.this, "Wrong, Ans is: " + mAnswer, Toast.LENGTH_SHORT).show();
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button3
        //Start of Button Listener for Button4
        mButtonChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //My logic for Button goes in here

                if (mButtonChoice4.getText().equals(mAnswer)) {
                    mButtonChoice4.setTextColor(Color.BLUE);
                    rewards();
                    //mButtonChoice4.setBackgroundColor(Color.BLUE);

                    mScore = mScore + 1;
                    updateScore(mScore);
                    updateQuestion();

                    //This line of code is optiona
                    //Toast.makeText(MainActivity.this, "Correct", Toast.LENGTH_SHORT).show();

                } else {
                    // mButtonChoice4.setBackgroundColor(Color.RED);
                    mButtonChoice4.setTextColor(Color.RED);
                    Toast.makeText(PlayQuiz.this, "Wrong, Ans is: " + mAnswer, Toast.LENGTH_SHORT).show();
                    updateQuestion();
                }
            }
        });


    }

    private void updateQuestion() {
        final ProgressDialog progressDialog = new ProgressDialog(PlayQuiz.this);
        progressDialog.setTitle("Updating Questions");
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        poost_key = getIntent().getExtras().getString("post_id");

        try {
            mQusetionRef = FirebaseDatabase.getInstance().getReference("Post").child(poost_key).child("quiz").child(String.valueOf(mQuestionNumber));
            Log.d("updateQuestion: ", "databaseref for question" + mQusetionRef);
            mQusetionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("onDataChange: ", "datasnapsohot for question" + dataSnapshot);

                    String question = dataSnapshot.child("question").getValue(String.class);
                    mQuestionView.setText(question);
                    String choice1 = dataSnapshot.child("choice1").getValue(String.class);
                    mButtonChoice1.setText(choice1);

                    mButtonChoice1.setTextColor(Color.WHITE);
                    String choice2 = dataSnapshot.child("choice2").getValue(String.class);
                    mButtonChoice2.setText(choice2);
                    mButtonChoice2.setTextColor(Color.WHITE);

                    String choice3 = dataSnapshot.child("choice3").getValue(String.class);
                    mButtonChoice3.setText(choice3);
                    mButtonChoice3.setTextColor(Color.WHITE);

                    String choice4 = dataSnapshot.child("choice4").getValue(String.class);
                    mButtonChoice4.setText(choice4);
                    mButtonChoice4.setTextColor(Color.WHITE);

                    mAnswer = dataSnapshot.child("answer").getValue(String.class);
                    progressDialog.cancel();


                    //starting the diagalog now..
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(PlayQuiz.this, "Something wrong", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {

            Toast.makeText(PlayQuiz.this, "No Question found in this post", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PlayQuiz.this, QuizActivity.class);
            finish();


        }


        mStatousref = FirebaseDatabase.getInstance().getReference("Post").child(poost_key).child("quiz").child(String.valueOf(mQuestionNumber));

        mStatousref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questionnocount = dataSnapshot.getChildrenCount();

                if (dataSnapshot.exists()) {
                    mQuestionNumber++;

                    qno = Integer.toString(mQuestionNumber);
                    questioncountno.setText(qno);


                } else {


                    //need to edit before releasing
                    Intent intent = new Intent(PlayQuiz.this, QuizActivity.class);
                    intent.putExtra("score", Integer.toString(mScore));
                    intent.putExtra("outof", qno);
                    intent.putExtra("time", timeLeftFormatted);
                    startActivity(intent);
                    finish();

                    mCountDownTimer.cancel();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void updateScore(int point) {
        mScoreView.setText("" + mScore);
    }


    //timer
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(PlayQuiz.this, PlayQuiz.class);
                intent.putExtra("score", Integer.toString(mScore));
                intent.putExtra("outof", qno);
                startActivity(intent);
                finish();
            }
        }.start();


    }

    public void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);


        mTextViewCountDown.setText(timeLeftFormatted);
    }

    //aleart for starting the quiz..
    public void startquizdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PlayQuiz.this);
        builder.setCancelable(false);

        View view = LayoutInflater.from(PlayQuiz.this).inflate(R.layout.alertbox, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);

        title.setText("Confirmation");

        imageButton.setImageResource(R.drawable.ic_touch_app_black_24dp);

        builder.setPositiveButton("Yes, Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //timer function is here
                startTimer();
                checkquestioncount();

                // Toast.makeText(MainActivity.this, "Quiz Started "+countq, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(MainActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlayQuiz.this, MainActivity.class);
                startActivity(intent);


            }
        });

        builder.setView(view);
        builder.show();


    }

    public void checkquestioncount() {
        findtime = FirebaseDatabase.getInstance().getReference();
        findtime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countq = dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("databse error at count", "error");

            }
        });


    }

    public void rewards() {
         dialog = new Dialog(this);
        dialog.setContentView(R.layout.aleartdialog);


        TextView title = dialog.findViewById(R.id.title);
        Button claim = dialog.findViewById(R.id.claim);
        Button cancel = dialog.findViewById(R.id.cancel);

//        ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
        TextView rewardpoints = dialog.findViewById(R.id.coin);
        title.setText("You have Won");

        //      imageButton.setImageResource(R.drawable.ic_style_black_24dp);
        rewardpoints.setText(String.valueOf(reward));
        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (interstitialAd.isLoaded()) {
                    //checkadavail();
                    interstitialAd.show();

                } else {

                    //add rewards to firebase database to fetch it later in wallet section


                    final ProgressDialog progressDialog = new ProgressDialog(PlayQuiz.this);
                    progressDialog.setMessage("Please wait..");
                    progressDialog.getActionBar();
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    final DatabaseReference mref = FirebaseDatabase.getInstance().getReference(uid).child("walletcoin");
                    mref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String walletvalue = dataSnapshot.getValue(String.class);
                            Integer walletcoininint = Integer.parseInt(walletvalue);
                            reward = 50;
                            Integer updatedcoin = (walletcoininint + reward);
                            mref.setValue(String.valueOf(updatedcoin));

                            progressDialog.cancel();
                            dialog.dismiss();

                            Toast.makeText(PlayQuiz.this, "Rewards added", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(PlayQuiz.this, "Error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(PlayQuiz.this, "Ok", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });


        dialog.show();

        dialog.setCancelable(true);

    }
}
