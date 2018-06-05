package com.a2zdaddy.quizmoney;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment{
    Button startbtn;
    TextView faqs;
Dialog dialog;
    public HomeFragment() {
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
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        startbtn=view.findViewById(R.id.startquiz);
        faqs=view.findViewById(R.id.faqstext);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),QuizActivity.class);
                startActivity(intent);
            }
        });

        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.faqs);



                    Button cancel = dialog.findViewById(R.id.cancel);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Toast.makeText(getContext(), "Thank You", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    });


                    dialog.show();

                    dialog.setCancelable(false);

                }



        });
       return view;

    }

}
