package com.a2zdaddy.quizmoney;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ActionBar toolbar;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {

                case R.id.navigation_home:

                    //toolbar.setTitle("Home");

                    fragment = new HomeFragment();

                    loadFragment(fragment);

                    //mTextMessage.setText(R.string.title_home);

                    return true;

/*
                case R.id.navigation_notifications:
                    //                  toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);

                    // mTextMessage.setText(R.string.title_notifications);
                    return true;

                    */
                case R.id.navigation_wallet:
                    //                  toolbar.setTitle("Profile");
                    fragment = new WalletFragment();
                    loadFragment(fragment);

                    // mTextMessage.setText(R.string.title_notifications);
                    return true;case R.id.navigation_latestpay:
                    //                  toolbar.setTitle("Profile");
                    fragment = new LatestPayFragment();
                    loadFragment(fragment);

                    // mTextMessage.setText(R.string.title_notifications);
                    return true;


            }



            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isConnected(MainActivity.this)){
            Toast.makeText(MainActivity.this,"No internet connection, Please turn on internet", Toast.LENGTH_SHORT).show();


        }

        else {
            Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_SHORT);
        }

        //toolbar = getSupportActionBar();
        // Toolbar toolbar = findViewById(R.id.toolbar);

        //toolbar.setTitle("Home");
        loadFragment(new HomeFragment());

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

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
            if (id == R.id.action_feedback) {
                sendemail();
                return true;
            }
            if (id == R.id.action_about) {

                Intent intent = new Intent(this, Aboutus.class);
                startActivity(intent);

                return true;
            }
            if (id == R.id.action_share) {
                share();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */
    public void sendemail() {

        Intent mIntent = new Intent(Intent.ACTION_SENDTO);
        mIntent.setData(Uri.parse("mailto:"));
        mIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"surajpkl86a@gmail.com"});
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Enter subject");
        startActivity(Intent.createChooser(mIntent, "Send Email Using..."));
    }

    public void share() {

        Intent a = new Intent(Intent.ACTION_SEND);

        //this is to get the app link in the playstore without launching your app.
        final String appPackageName = getApplicationContext().getPackageName();
        String strAppLink = "";

        try {
            strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        } catch (android.content.ActivityNotFoundException anfe) {
            strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }
        // this is the sharing part
        a.setType("text/link");
        String shareBody = "Hey! Download this app to practice quiz , it's updated daily with new questions and earn money while learning" +
                "\n" + "" + strAppLink;
        String shareSub = "Quiz App";
        a.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        a.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(a, "Share with.."));
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_feedback) {
            sendemail();
            return true;
        }

        if (id == R.id.action_share) {
            share();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void dialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit);
        builder.setMessage(getString(R.string.dialogueClose))
                .setPositiveButton("Rate App", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id="
                                        + getPackageName())));
                    }
                })
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });

        builder.show();

    }
    @Override
    public void onBackPressed() {
        dialogue();
    }

}

