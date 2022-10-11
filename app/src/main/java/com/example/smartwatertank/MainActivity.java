package com.example.smartwatertank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText modelip;
    LottieAnimationView happyvialottie, sadvialottie, searchingvialottie;
    String model = "";
    Button button2;
    TextView textView5;
    SharedPreferences shrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        shrd = getSharedPreferences("demo",MODE_PRIVATE);
        String d = shrd.getString("Model Number","Not found");
        if(d.equals("Not found")){
            modelip = findViewById(R.id.modelip);
            textView5=findViewById(R.id.textView5);
            happyvialottie = findViewById(R.id.happyvialottie);
            sadvialottie = findViewById(R.id.sadvialottie);
            searchingvialottie = findViewById(R.id.searchingvialottie);
            button2=findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String h = modelip.getText().toString();
                    String t = h+"/Name";
                    Log.d("Hats",t);
                    searchingvialottie.setVisibility(View.VISIBLE);
                    searchingvialottie.playAnimation();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference(t);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String Answer = dataSnapshot.getValue(String.class);
                            if(Answer == null){
                                searchingvialottie.pauseAnimation();
                                searchingvialottie.setVisibility(View.GONE);
                                sadvialottie.setVisibility(View.VISIBLE);
                                sadvialottie.playAnimation();
                            }
                            else {
                                searchingvialottie.pauseAnimation();
                                searchingvialottie.setVisibility(View.GONE);
                                happyvialottie.setVisibility(View.VISIBLE);
                                happyvialottie.playAnimation();
                                SharedPreferences.Editor editor = shrd.edit();
                                editor.putString("Model Number",h);
                                editor.apply();
                                if(Answer.equals("Pending")){
                                    button2.setText("Save Name");
                                    modelip.setHint("Enter your name");
                                    textView5.setText("Name");
                                    button2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String Name = modelip.getText().toString();
                                            databaseReference.setValue(Name);

                                        }
                                    });

                                }
                                else{
                                    Intent intent = new Intent(MainActivity.this,HomePage.class);
                                    startActivity(intent);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else{
            Intent intent = new Intent(MainActivity.this,HomePage.class);
            startActivity(intent);
        }



    }
}