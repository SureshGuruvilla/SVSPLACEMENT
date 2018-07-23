package com.example.admin.svsplacement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText loginMail,loginPassword;
    TextView loginLogin;
    FirebaseAuth loginFireBaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DatabaseReference loginDatabaseReference;
    private ProgressBar progressBar;
    private String s_uid = null;
    private TextView conviewpass;
    private Boolean is = false;
    private String s_mail, s_password;
    int  i= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        loginFireBaseAuth = FirebaseAuth.getInstance();
        loginMail = (EditText)findViewById(R.id.login_mail);
        loginPassword = (EditText)findViewById(R.id.login_password);
        loginLogin = (TextView) findViewById(R.id.login_login);
        conviewpass = (TextView) findViewById(R.id.conviewpass);
        loginDatabaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        TextView textView = (TextView) findViewById(R.id.login_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/angelina.TTF");
        textView.setTypeface(typeface);

        resume();
    }
    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }
    protected void resume() {

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
                else {

                }
            }
        };
        conviewpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is = is ? false : true;
                if (is){
                    conviewpass.setBackgroundResource(R.drawable.ic_visibility_off);
                    loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    conviewpass.setBackgroundResource(R.drawable.ic_visibility_black);
                    loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                int position = loginPassword.length();
                Editable etext = loginPassword.getText();
                Selection.setSelection(etext, position);
            }
        });
        loginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    s_mail = "SVS"+loginMail.getText().toString().trim()+"@gmail.com";
                    s_password = loginPassword.getText().toString().trim();
                    progressBar.setVisibility(View.VISIBLE);
                    if(isOnline()){
                        if ( !TextUtils.isEmpty(s_mail) || !TextUtils.isEmpty(s_password)||!TextUtils.isEmpty(s_mail) &&
                                !TextUtils.isEmpty(s_password)) {
                            loginFireBaseAuth.signInWithEmailAndPassword(s_mail,s_password)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            try {
                                                if (!task.isSuccessful()) {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getApplicationContext(),
                                                            "Invalid UserName or Password \n Contact Admin to create new Account",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Intent intent = new Intent(LoginActivity.this,SetupActivity.class);
                                                    intent.putExtra("regno",loginMail.getText().toString());
                                                    startActivity(intent);
                                                    finish();
//                                                loginDatabaseReference.addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                            if (dataSnapshot.hasChild(loginFireBaseAuth.getUid().toString())) {
//                                                                Toast.makeText(getApplicationContext(), "You Have Successfully Logged in", Toast.LENGTH_SHORT).show();
//                                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                                finish();
//                                                            }
//                                                            else{
//                                                                Intent intent = new Intent(LoginActivity.this,SetupActivity.class);
//                                                                intent.putExtra("regno",loginMail.getText().toString());
//                                                                startActivity(intent);
//                                                                finish();
//                                                            }
//                                                        }
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//                                                    }
//                                                });
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                            catch (Exception e){
                                                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Fill The fields", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Please !, Turn On Your Internet Connection...", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginFireBaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }
}