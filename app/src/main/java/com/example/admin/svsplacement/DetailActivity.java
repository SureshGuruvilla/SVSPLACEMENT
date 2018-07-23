package com.example.admin.svsplacement;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {
    private String key;
    private DatabaseReference databaseReference;
    private TextView detailName,detailDOB,detailGender,detailDept,detailRegno,detailEmail,detailPhone,detailTM,detailTWM,detailCGPA,detailSA,detailHOA,detailHOD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        key = getIntent().getExtras().getString("key");
        detailName = (TextView) findViewById(R.id.detail_Name);
        detailDOB = (TextView) findViewById(R.id.detail_DOB);
        detailGender = (TextView) findViewById(R.id.detail_Gender);
        detailDept = (TextView) findViewById(R.id.detail_Dept);
        detailRegno = (TextView) findViewById(R.id.detail_Register_no);
        detailEmail = (TextView) findViewById(R.id.detail_email);
        detailPhone = (TextView) findViewById(R.id.detail_phone);
        detailTM = (TextView) findViewById(R.id.detail_TM);
        detailTWM = (TextView) findViewById(R.id.detail_TWM);
        detailCGPA = (TextView) findViewById(R.id.detail_CGPA);
        detailSA = (TextView) findViewById(R.id.detail_SA);
        detailHOA = (TextView) findViewById(R.id.detail_HOA);
        detailHOD = (TextView) findViewById(R.id.detail_HOD);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Database").child("usersData");
        databaseReference.keepSynced(true);
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                detailName.setText(Html.fromHtml("<b>Name : </b>")+dataSnapshot.child("NAME").getValue().toString());
                detailDOB.setText(Html.fromHtml("<b>Date Of Birth : </b>")+dataSnapshot.child("DOB").getValue().toString());
                if(dataSnapshot.child("GENDER").getValue(String.class).equalsIgnoreCase("M")){
                    detailGender.setText(Html.fromHtml("<b>Gender : </b>")+"Male");
                }
                else{
                    detailGender.setText(Html.fromHtml("<b>Gender : </b>")+"Female");
                }
                detailDept.setText(Html.fromHtml("<b>Department : </b>")+dataSnapshot.child("DEPT").getValue().toString());
                detailRegno.setText(Html.fromHtml("<b>Register No : </b>")+dataSnapshot.child("REGNO").getValue().toString());
                detailEmail.setText(Html.fromHtml("<b>E Mail : </b>")+dataSnapshot.child("EMAILID").getValue().toString());
                detailPhone.setText(Html.fromHtml("<b>Phone Number: </b>")+dataSnapshot.child("PHONENO").getValue().toString());
                detailTM.setText(Html.fromHtml("<b>Tenth Percentage : </b>")+dataSnapshot.child("TM").getValue().toString());
                detailTWM.setText(Html.fromHtml("<b>Twelth Percentage : </b>")+dataSnapshot.child("TWM").getValue().toString());
                detailCGPA.setText(Html.fromHtml("<b>CGPA : </b>")+dataSnapshot.child("CGPA").getValue().toString());
                detailSA.setText(Html.fromHtml("<b>Standing Arrears : </b>")+dataSnapshot.child("SA").getValue().toString());
                detailHOA.setText(Html.fromHtml("<b>History of Arrerars : </b>")+dataSnapshot.child("HOA").getValue().toString());
                if(dataSnapshot.child("HOD").getValue().toString()=="H"){
                    detailHOD.setText(Html.fromHtml("<b>Hosteller or Dayschollar : </b>")+"Hosteller");
                }
                else{
                    detailHOD.setText(Html.fromHtml("<b>Hosteller or Dayschollar : </b>")+"Dayschollar");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
