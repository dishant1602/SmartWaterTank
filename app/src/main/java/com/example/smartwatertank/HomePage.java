package com.example.smartwatertank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView textView;
    String Value="";
    LottieAnimationView levellottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_page);
        textView = findViewById(R.id.waterlevel);
        levellottie = findViewById(R.id.levelvialottie);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Dishant1/Water Level");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long Answer = dataSnapshot.getValue(Long.class);
                try{
                Value = Long.toString(Answer);
                float x = 100-Answer;
                float t = x/100;
                Log.d("Gotham", String.valueOf(t));
                levellottie.setMinAndMaxProgress(0.0f,t);
                levellottie.playAnimation();
                textView.setText(Value);}
                catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomePage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}