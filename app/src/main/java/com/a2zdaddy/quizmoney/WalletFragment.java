package com.a2zdaddy.quizmoney;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WalletFragment extends Fragment{
    TextView name,email,number,promocode,invite,coin,dollar,withdrawbutton;
    String promo;
    Double indollar;


    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);

        name=view.findViewById(R.id.nametext);
        number=view.findViewById(R.id.mobileno);
        email=view.findViewById(R.id.emailid);
        coin=view.findViewById(R.id.coin);
        dollar=view.findViewById(R.id.rupees);
        withdrawbutton=view.findViewById(R.id.witdrawbttn);

//        promocode=view.findViewById(R.id.promocode);
        invite=view.findViewById(R.id.invitebttn);
        updateprofile();


        withdrawbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(),WithdrawActivity.class);
                startActivity(intent);

            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invite();
            }
        });


        return view;
    }
    public void updateprofile(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.getActionBar();
        progressDialog.setCancelable(false);
        progressDialog.show();
        String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mUserDb = FirebaseDatabase.getInstance().getReference(mUid);
        mUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username=dataSnapshot.child("name").getValue(String.class);
                String emailids=dataSnapshot.child("email").getValue(String.class);
                String mobilenos=dataSnapshot.child("mobileno").getValue(String.class);
                promo=dataSnapshot.child("promocode").getValue(String.class);
                String wallcoin = dataSnapshot.child("walletcoin").getValue(String.class);

                //new
                coin.setText(wallcoin);
                Double walleint = Double.parseDouble(wallcoin);
                indollar = ((walleint / 2000));
                dollar.setText(String.valueOf(indollar));
                //
                name.setText(username);
                email.setText(emailids);
                number.setText(mobilenos);
                //          promocode.setText(promo);
                progressDialog.cancel();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), " No Internet", Toast.LENGTH_SHORT).show();
            }
        });





    }

    public void invite(){

        Intent a = new Intent(Intent.ACTION_SEND);

        //this is to get the app link in the playstore without launching your app.
        final String appPackageName = getContext().getPackageName();
        String strAppLink = "";

        try {
            strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        } catch (android.content.ActivityNotFoundException anfe) {
            strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }
        // this is the sharing part
        a.setType("text/link");
        String shareBody = "Hey! Download this app to practice quiz , it's updated daily with new questions and earn money while learning, use my refer code to get 5000 coins , my refer code is: "+promo +
                "\n" + "" + strAppLink;
        String shareSub = "Quiz aPP";
        a.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        a.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(a, "Share with.."));
    }


    public void sendfeedback(){
        Intent mIntent = new Intent(Intent.ACTION_SENDTO);
        mIntent.setData(Uri.parse("mailto:"));
        mIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"surajpkl86a@gmail.com"});
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Help me,");
        startActivity(Intent.createChooser(mIntent, "Send Email Using..."));
    }


}


