package com.example.admin.svsplacement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class AdminActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText signUpEditText,promotionEditText;
    private TextView signUpTextView,promotionTextView;
    private FirebaseAuth firebaseAuth;
    private String spinnerName,s_uid;
    private DatabaseReference logindatabsereference;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        spinner = (Spinner) findViewById(R.id.admin_spinner);
        signUpEditText = (EditText) findViewById(R.id.admin_signUpEditText);
        promotionEditText = (EditText) findViewById(R.id.admin_promotionEditText);
        signUpTextView = (TextView) findViewById(R.id.admin_signUp);
        firebaseAuth = FirebaseAuth.getInstance();
        promotionTextView = findViewById(R.id.admin_promotion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(AdminActivity.this, R.array.spinner_name1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerName = (String) parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        logindatabsereference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = ProgressDialog
                        .show(AdminActivity.this, "Please wait", "Processing",true);
                if(signUpEditText.getText().toString().length()>0){
                    firebaseAuth.createUserWithEmailAndPassword("SVS"+signUpEditText.getText().toString()+
                            "@gmail.com",signUpEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                        firebaseAuth.signOut();
                                        finish();
                                        startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminActivity.this, "Authentication Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Enter Valid Details...",Toast.LENGTH_SHORT).show();
                }
            }
        });
        promotionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = ProgressDialog.show(AdminActivity.this, "Please wait", "Processing",true);
                if(promotionEditText.getText().toString().length()>0){
                    logindatabsereference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data:dataSnapshot.getChildren()){
                                if((data.child("Rollno").getValue(String.class)
                                        .equalsIgnoreCase("SVS" +promotionEditText.getText().toString()))){
                                    if(spinnerName.equalsIgnoreCase("Select promotion method")){
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Select Valid promotion method", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        s_uid = data.getKey();
                                        logindatabsereference.child(s_uid).child("position").
                                                setValue(spinnerName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    logindatabsereference.child(s_uid).child("Dept").setValue("admin");
                                                    logindatabsereference.child(s_uid).child("CGPA").setValue("10");
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                                    promotionEditText.setText("");
                                                }
                                            }
                                        });
                                    }

                                }
                                else {
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
               else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Enter Valid Details...",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
