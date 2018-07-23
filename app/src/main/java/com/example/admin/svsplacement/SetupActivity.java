package com.example.admin.svsplacement;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
public class SetupActivity extends AppCompatActivity {
    private Spinner spinner;
    private String spinnerName;
    private EditText setupName,setupDob,setupphoneno,setupInterest,setupFrom,setupSchool;
    private TextView setupSetup;
    private DatabaseReference setupDatabaseReference;
    private FirebaseAuth setupFirebaseAuth;
    private String s_uid;
    private StorageReference setupstorageReference;
    private FloatingActionButton floatingActionButton;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImageuri = null;
    private CircleImageView imageView;
    private DatabaseReference databaseReference;
    private String s_rollno,CGPA,Dept;
    private ProgressBar pb;
    private LinearLayout setupLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setup);
        setupFirebaseAuth = FirebaseAuth.getInstance();
        setupLayout = (LinearLayout) findViewById(R.id.setup_layout);
        setupDatabaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        setupDatabaseReference.keepSynced(true);
        setupDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(setupFirebaseAuth.getUid())){
                    startActivity(new Intent(SetupActivity.this,MainActivity.class));
                    finish();
                }
                else {
                    setupLayout.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinner = (Spinner) findViewById(R.id.setup_gender);
        setupName = (EditText) findViewById(R.id.setup_name);
        setupDob = (EditText) findViewById(R.id.setup_dob);
        setupphoneno = (EditText) findViewById(R.id.setup_phoneno);
        setupInterest = (EditText) findViewById(R.id.setup_interest);
        setupSetup = (TextView) findViewById(R.id.setup_setup);
        setupFrom = (EditText) findViewById(R.id.setup_lives);
        setupSchool = (EditText) findViewById(R.id.setup_school);
        pb = (ProgressBar) findViewById(R.id.setup_pb);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Database").child("usersData");
        databaseReference.keepSynced(true);
        TextView textView = (TextView) findViewById(R.id.setup_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/angelina.TTF");
        textView.setTypeface(typeface);
        imageView = (CircleImageView) findViewById(R.id.setup_dp);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.setup_setdp);
        s_rollno = getIntent().getExtras().getString("regno");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, R.layout.spinnerlayout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        setupstorageReference = FirebaseStorage.getInstance().getReference();
        Picasso.with(getApplicationContext()).load(R.drawable.dp).into(imageView);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerName = (String) parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        setupSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(setupName.getText().length()>0&&setupDob.getText().length()>0
                        &&setupFrom.getText().length()>0&&setupSchool.getText().length()>0
                        &&setupphoneno.getText().length()>0&&setupInterest.getText().length()>0&spinnerName!="Select Your Gender"
                        && mImageuri!=null){
                    pb.setVisibility(View.VISIBLE);
                    StorageReference filepath = setupstorageReference.child("ProfileDp").child(mImageuri.getLastPathSegment());
                        filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot data1 : dataSnapshot.getChildren()){
                                                if(data1.child("REGNO").getValue(String.class).equalsIgnoreCase(s_rollno)){
                                                    CGPA = data1.child("CGPA").getValue(String.class);
                                                    Dept = data1.child("DEPT").getValue(String.class);
                                                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                                                    s_uid = setupFirebaseAuth.getCurrentUser().getUid();
                                                    final DatabaseReference data = setupDatabaseReference.child(s_uid);
                                                    data.child("Image").setValue(downloadurl.toString());
                                                    data.child("Name").setValue(setupName.getText().toString());
                                                    data.child("Dob").setValue(setupDob.getText().toString());
                                                    data.child("Phoneno").setValue(setupphoneno.getText().toString());
                                                    data.child("Interest").setValue(setupInterest.getText().toString());
                                                    data.child("Gender").setValue(spinnerName.toString());
                                                    data.child("position").setValue("student");
                                                    data.child("from").setValue(setupFrom.getText().toString());
                                                    data.child("school").setValue(setupSchool.getText().toString());
                                                    data.child("regno").setValue(s_rollno);
                                                    data.child("CGPA").setValue(CGPA);
                                                    data.child("Dept").setValue(Dept);
                                                    Toast.makeText(SetupActivity.this, "You have Successfully Logged in...", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SetupActivity.this,MainActivity.class));
                                                    finish();
                                                    mImageuri=null;
                                                    break;
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
                                catch (Exception e){
                                    Toast.makeText(SetupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                else{
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Please Fill all the details to continue",Toast.LENGTH_SHORT).show();
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
                    .start(SetupActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageuri = result.getUri();
                imageView.setImageURI(mImageuri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }
}