package com.example.admin.svsplacement;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ContactActivity extends AppCompatActivity {
    private DatabaseReference feedbackdatabase;
    private EditText name,mail,phone,feedback;
    private TextView submit;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.contact_toolbar);
//        setSupportActionBar(toolbar);
        feedbackdatabase = FirebaseDatabase.getInstance().getReference().child("Feedback");
        name = (EditText) findViewById(R.id.contact_name);
        mail = (EditText) findViewById(R.id.contact_mail);
        phone = (EditText) findViewById(R.id.contact_phone);
        feedback = (EditText) findViewById(R.id.contact_feedback);
        submit = (TextView) findViewById(R.id.contact_submit);
        firebaseAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(ContactActivity.this);
        TextView textView = (TextView) findViewById(R.id.contact_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/angelina.TTF");
        textView.setTypeface(typeface);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Loading");
                pd.show();
                    if(!TextUtils.isEmpty(name.getText().toString())&&!TextUtils.isEmpty(mail.getText().toString())
                            &&!TextUtils.isEmpty(phone.getText().toString())&&!TextUtils.isEmpty(feedback.getText().toString())){

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));
                        Calendar cal = Calendar.getInstance();
                        final String time = dateFormat.format(cal.getTime());
                        DatabaseReference data = feedbackdatabase.push();
                        data.child("name").setValue(name.getText().toString());
                        data.child("mail").setValue(mail.getText().toString());
                        data.child("phone").setValue(phone.getText().toString());
                        data.child("feedback").setValue(feedback.getText().toString());
                        data.child("uid").setValue(firebaseAuth.getUid());
                        data.child("time").setValue(time);
                        name.setText("");
                        mail.setText("");
                        phone.setText("");
                        feedback.setText("");
                        if(pd.isShowing()){
                            pd.dismiss();
                        }
                    }
                    else{
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),"Please fill all fields to continue",Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
    private class Contact extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        public Contact(Context applicationContext) {
            dialog = new ProgressDialog(applicationContext);
        }

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            dialog.setMessage("Creating new user...");

            //show dialog
//            dialog.show();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
            // do background work here


            return null;
        }

        protected void onPostExecute(Void result) {
            // do UI work here
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }

        }
    }
}
