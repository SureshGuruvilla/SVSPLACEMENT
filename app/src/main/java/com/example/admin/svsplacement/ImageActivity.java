package com.example.admin.svsplacement;
import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private DatabaseReference databaseReference;
    private String s_post;
    private String s_typeofpost;
    private String image;
    private FloatingActionButton download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        imageView = (ImageView) findViewById(R.id.image_image);
        download = (FloatingActionButton) findViewById(R.id.download);
//        download.setBackgroundColor(Color.parseColor("#FF4081"));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        s_post = getIntent().getExtras().getString("s_post");
        s_typeofpost = getIntent().getExtras().getString("posts");
        databaseReference.child(s_typeofpost).child(s_post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(imageView);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.update();
        download.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ImageActivity.this, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, 1000);
                    BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = draw.getBitmap();
                    File sdCard = Environment.getExternalStorageDirectory();
                    File outFile = null;
                    File dir = null;
                    dir = new File(sdCard.getAbsolutePath()+"/SVSPLACEMENT");
                    dir.mkdir();
                    String fileName = "SVS"+String.valueOf(System.currentTimeMillis())+".jpg";
                    outFile = new File(dir, fileName);
                    try{
                        FileOutputStream outputStream = new FileOutputStream(outFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                    catch(Exception r){
                        Toast.makeText(getApplicationContext(),"Error"+r.toString(),Toast.LENGTH_SHORT).show();
                    }
                    scanFile(getApplicationContext(), Uri.fromFile(dir));
                    return;
                }
                else{
                    BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = draw.getBitmap();
                    File sdCard = Environment.getExternalStorageDirectory();
                    File outFile = null;
                    File dir = null;
                    dir = new File(sdCard.getAbsolutePath()+"/SVSPLACEMENT");
                    dir.mkdir();
                    String fileName = "SVS"+String.valueOf(System.currentTimeMillis())+".jpg";
                    outFile = new File(dir, fileName);
                    try{
                        FileOutputStream outputStream = new FileOutputStream(outFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                        Toast.makeText(getApplicationContext(),"Download Completed...",Toast.LENGTH_SHORT).show();
                        outputStream.flush();
                        outputStream.close();
                    }
                    catch(Exception r){
                        Toast.makeText(getApplicationContext(),"Error"+r.toString(),Toast.LENGTH_SHORT).show();
                    }
                    scanFile(getApplicationContext(), Uri.fromFile(dir));
                }
            }
            private void scanFile(Context applicationContext, Uri uri) {
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(uri);
                applicationContext.sendBroadcast(scanIntent);
            }
        });
    }
}
