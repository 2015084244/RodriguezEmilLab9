package com.rodriguez.emil;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText nameText, ageText, genderText;
    TextView tvName, tvAge, tvGender;

    FirebaseDatabase fb;
    DatabaseReference db;
    ArrayList<Person> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameText = (EditText) findViewById(R.id.nameText);
        ageText = (EditText) findViewById(R.id.ageText);
        genderText = (EditText) findViewById(R.id.genderText);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvGender = (TextView) findViewById(R.id.tvGender);


        fb = FirebaseDatabase.getInstance();
        db = fb.getReference("fb");
        personList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = 28)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Person person = ds.getValue(Person.class);
                    personList.add(person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void Save(View v){
        String name = nameText.getText().toString().trim();
        int age = Integer.parseInt(ageText.getText().toString().trim());
        String gender = genderText.getText().toString().trim();


        for (int x = 0; x < personList.size(); x++) {
            if (personList.get(x).getName().equals(name)){
                Toast.makeText(this, "Record already exists", Toast.LENGTH_LONG).show();
                return;
            }
        }


            String key = db.push().getKey();
            Person person = new Person(name,age,gender);
            db.child(key).setValue(person);
            Toast.makeText(this, "Record saved", Toast.LENGTH_LONG).show();



    }



    public void Search(View v){
        String name = nameText.getText().toString().trim();

        for (int x = 0; x < personList.size(); x++){
            if((personList.get(x).getName()).equals(name)){
                tvName.setText(personList.get(x).getName());
                tvAge.setText(personList.get(x).getAge()+"");
                tvGender.setText(personList.get(x).getGender());
                Toast.makeText(this, "Record searched", Toast.LENGTH_LONG).show();

            }

        }

    }



}
