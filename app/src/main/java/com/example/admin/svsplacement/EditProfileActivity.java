package com.example.admin.svsplacement;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private EditText editName,editDob,editPhone,editFrom,editSchool,editPass,editConpass,editOldPass;
    private TextView t1,t2,t3,t4,t5,submit,change;
    private TextView editTextName,editTextDob,editTextPhine,editTextFrom,editTextSchool;
    private FirebaseAuth firebaseAuth;
    private CircleImageView editDp;
    private Boolean a = false,b=false;
    private FloatingActionButton fat;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageuri = null;
    private StorageReference setupstorageReference;
    private ProgressBar pb1,pb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        databaseReference.keepSynced(true);
        editName = (EditText)findViewById(R.id.edit_editname);
        editDob = (EditText)findViewById(R.id.edit_editdob);
        editPhone = (EditText)findViewById(R.id.edit_editphoneno);
        editFrom = (EditText)findViewById(R.id.edit_editfrom);
        editSchool = (EditText)findViewById(R.id.edit_editschool);
        editPass = (EditText)findViewById(R.id.edit_pass);
        editConpass = (EditText)findViewById(R.id.edit_conpass);
        editOldPass = (EditText) findViewById(R.id.edit_oldpass);
        editTextName = (TextView) findViewById(R.id.edit_textname);
        editTextDob = (TextView) findViewById(R.id.edit_textdob);
        editTextPhine = (TextView) findViewById(R.id.edit_textphoneno);
        editTextFrom = (TextView) findViewById(R.id.edit_textfrom);
        editTextSchool = (TextView) findViewById(R.id.edit_textschool);
        t1 = (TextView) findViewById(R.id.editname);
        t2 = (TextView) findViewById(R.id.editdob);
        t3 = (TextView) findViewById(R.id.editphoneno);
        t4 = (TextView) findViewById(R.id.editfrom);
        t5 = (TextView) findViewById(R.id.editschool);
        submit = (TextView) findViewById(R.id.edit_submit);
        change = (TextView) findViewById(R.id.edit_change);
        editDp = (CircleImageView) findViewById(R.id.edit_dp);
        fat = (FloatingActionButton) findViewById(R.id.edit_editdp);
        pb1 = (ProgressBar) findViewById(R.id.edit_pb1);
        pb2 = (ProgressBar) findViewById(R.id.edit_pb2);
        firebaseAuth = FirebaseAuth.getInstance();
        setupstorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editName.setText(dataSnapshot.child("Name").getValue().toString());
                editDob.setText(dataSnapshot.child("Dob").getValue().toString());
                editPhone.setText(dataSnapshot.child("Phoneno").getValue().toString());
                editFrom.setText(dataSnapshot.child("from").getValue().toString());
                editSchool.setText(dataSnapshot.child("school").getValue().toString());

                editTextName.setText(dataSnapshot.child("Name").getValue().toString());
                editTextDob.setText(dataSnapshot.child("Dob").getValue().toString());
                editTextPhine.setText(dataSnapshot.child("Phoneno").getValue().toString());
                editTextFrom.setText(dataSnapshot.child("from").getValue().toString());
                editTextSchool.setText(dataSnapshot.child("school").getValue().toString());
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("Image").getValue().toString()).into(editDp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        editDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b = true;
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b = true;
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = true;
                editTextName.setVisibility(View.GONE);
                editTextDob.setVisibility(View.VISIBLE);
                editTextPhine.setVisibility(View.VISIBLE);
                editTextFrom.setVisibility(View.VISIBLE);
                editTextSchool.setVisibility(View.VISIBLE);

                editName.setVisibility(View.VISIBLE);
                editDob.setVisibility(View.GONE);
                editPhone.setVisibility(View.GONE);
                editFrom.setVisibility(View.GONE);
                editSchool.setVisibility(View.GONE);

                editName.setSelection(editName.getText().length());
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextDob.setVisibility(View.GONE);
                editTextName.setVisibility(View.VISIBLE);
                editTextPhine.setVisibility(View.VISIBLE);
                editTextFrom.setVisibility(View.VISIBLE);
                editTextSchool.setVisibility(View.VISIBLE);

                editDob.setVisibility(View.VISIBLE);
                editName.setVisibility(View.GONE);
                editPhone.setVisibility(View.GONE);
                editFrom.setVisibility(View.GONE);
                editSchool.setVisibility(View.GONE);

                a = true;
                editDob.setSelection(editDob.getText().length());
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPhine.setVisibility(View.GONE);
                editTextName.setVisibility(View.VISIBLE);
                editTextDob.setVisibility(View.VISIBLE);
                editTextFrom.setVisibility(View.VISIBLE);
                editTextSchool.setVisibility(View.VISIBLE);

                editPhone.setVisibility(View.VISIBLE);
                editName.setVisibility(View.GONE);
                editDob.setVisibility(View.GONE);
                editFrom.setVisibility(View.GONE);
                editSchool.setVisibility(View.GONE);
                a = true;
                editPhone.setSelection(editPhone.getText().length());
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextFrom.setVisibility(View.GONE);
                editTextName.setVisibility(View.VISIBLE);
                editTextDob.setVisibility(View.VISIBLE);
                editTextPhine.setVisibility(View.VISIBLE);
                editTextSchool.setVisibility(View.VISIBLE);

                editFrom.setVisibility(View.VISIBLE);
                editName.setVisibility(View.GONE);
                editDob.setVisibility(View.GONE);
                editPhone.setVisibility(View.GONE);
                editSchool.setVisibility(View.GONE);

                a = true;
                editFrom.setSelection(editFrom.getText().length());
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSchool.setVisibility(View.GONE);
                editTextName.setVisibility(View.VISIBLE);
                editTextDob.setVisibility(View.VISIBLE);
                editTextPhine.setVisibility(View.VISIBLE);
                editTextFrom.setVisibility(View.VISIBLE);

                editSchool.setVisibility(View.VISIBLE);
                editName.setVisibility(View.GONE);
                editDob.setVisibility(View.GONE);
                editPhone.setVisibility(View.GONE);
                editFrom.setVisibility(View.GONE);

                a = true;
                editSchool.setSelection(editSchool.getText().length());
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb1.setVisibility(View.VISIBLE);
                if(a == true || b==true){
                    pb1.setVisibility(View.VISIBLE);
                    if(b==true&&a!=true){
                        StorageReference filepath = setupstorageReference.child("ProfileDp").child(mImageuri.getLastPathSegment());
                        filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadurl = taskSnapshot.getDownloadUrl();
                                DatabaseReference data = databaseReference.child(firebaseAuth.getUid());
                                data.child("Image").setValue(downloadurl.toString());
                                pb1.setVisibility(View.GONE);
                            }
                        });
                    }
                    if(b==true&&a==true){
                        StorageReference filepath = setupstorageReference.child("ProfileDp").child(mImageuri.getLastPathSegment());
                        filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadurl = taskSnapshot.getDownloadUrl();
                                DatabaseReference data = databaseReference.child(firebaseAuth.getUid());
                                data.child("Name").setValue(editName.getText().toString());
                                data.child("Dob").setValue(editDob.getText().toString());
                                data.child("Phoneno").setValue(editPhone.getText().toString());
                                data.child("from").setValue(editFrom.getText().toString());
                                data.child("school").setValue(editSchool.getText().toString());
                                data.child("Image").setValue(downloadurl.toString());
                                pb1.setVisibility(View.GONE);
                            }
                        });
                    }
                    if(a ==true &&b!=true){
                        DatabaseReference data = databaseReference.child(firebaseAuth.getUid());
                        data.child("Name").setValue(editName.getText().toString());
                        data.child("Dob").setValue(editDob.getText().toString());
                        data.child("Phoneno").setValue(editPhone.getText().toString());
                        data.child("from").setValue(editFrom.getText().toString());
                        data.child("school").setValue(editSchool.getText().toString());
                        pb1.setVisibility(View.GONE);
                    }
                    editName.setVisibility(View.GONE);
                    editDob.setVisibility(View.GONE);
                    editPhone.setVisibility(View.GONE);
                    editFrom.setVisibility(View.GONE);
                    editSchool.setVisibility(View.GONE);
                    editTextName.setVisibility(View.VISIBLE);
                    editTextDob.setVisibility(View.VISIBLE);
                    editTextPhine.setVisibility(View.VISIBLE);
                    editTextFrom.setVisibility(View.VISIBLE);
                    editTextSchool.setVisibility(View.VISIBLE);
                }
                else {
                    pb1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"No changes made",Toast.LENGTH_SHORT).show();
                }
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb2.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(editPass.getText())&&!TextUtils.isEmpty(editConpass.getText())&&!TextUtils.isEmpty(editOldPass.getText())){
                    if(editPass.getText().toString().equals(editConpass.getText().toString())){
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String e_mail = user.getEmail();
                        final String newpass = editPass.getText().toString();
                        final String oldPass = editOldPass.getText().toString();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(e_mail, oldPass);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        editPass.setText("");
                                                        editConpass.setText("");
                                                        editOldPass.setText("");editPass.setText("");
                                                        editConpass.setText("");
                                                        editOldPass.setText("");
                                                        pb2.setVisibility(View.GONE);
                                                        Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();


                                                    }
                                                    else {
                                                        pb2.setVisibility(View.GONE);
                                                        Toast.makeText(getApplicationContext(),"Error Please try again later",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            pb2.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"Incorrect old password",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else {
                        pb2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Password mismatched",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    pb2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Enter valid details",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageuri = data.getData();
            CropImage.activity(mImageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(EditProfileActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageuri = result.getUri();
                editDp.setImageURI(mImageuri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(EditProfileActivity.this, "Error : " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
