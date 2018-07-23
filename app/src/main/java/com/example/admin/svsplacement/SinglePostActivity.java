package com.example.admin.svsplacement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinglePostActivity extends AppCompatActivity {
    private ImageView singleImage;
    private TextView singleName;
    private CircleImageView singleDp;
    private TextView singleDesc, singleLike, singleComment, singleLikeView,singleShare,singleTime;
    String s_post;
    String s_typeposts, s_uid;
    private Boolean like;
    private DatabaseReference databaseReference;
    private DatabaseReference likedatabaseReference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference loginstatusdatabaseReference;
    private DatabaseReference notificationdatabaseReference;
    private DatabaseReference setlike;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        singleImage = (ImageView) findViewById(R.id.singlepost_image);
        singleName = (TextView) findViewById(R.id.singleblog_name);
        singleDp = (CircleImageView) findViewById(R.id.singleblog_dp);
        singleDesc = (TextView) findViewById(R.id.singlepost_desc);
        singleLike = (TextView) findViewById(R.id.singleblog_like);
        singleComment = (TextView) findViewById(R.id.singleblog_comment);
        singleLikeView = (TextView) findViewById(R.id.singleblog_likeview);
        singleShare = (TextView) findViewById(R.id.singleblog_share);
        singleTime = (TextView) findViewById(R.id.singleblog_time);
        linearLayout = (LinearLayout) findViewById(R.id.singlesharingimage);
        s_post = getIntent().getExtras().getString("s_post");
        s_typeposts = getIntent().getExtras().getString("posts");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        loginstatusdatabaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        loginstatusdatabaseReference.keepSynced(true);
        likedatabaseReference = FirebaseDatabase.getInstance().getReference().child(s_typeposts + "Like");
        likedatabaseReference.keepSynced(true);
        notificationdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        notificationdatabaseReference.keepSynced(true);
        setlike = FirebaseDatabase.getInstance().getReference().child(s_typeposts + "Like");
        setlike.keepSynced(true);
        firebaseAuth = FirebaseAuth.getInstance();
        s_uid = firebaseAuth.getCurrentUser().getUid().toString();
        setBlogComment(s_post);
        setBlogLikeView(s_post);
        singleShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = getBitmapFromView(linearLayout);
                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), "logicchip.png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/png");
                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        singleLikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LikeActivity.class);
                intent.putExtra("s_post", s_post);
                intent.putExtra("posts", s_typeposts + "Like");
                startActivity(intent);
            }
        });
        singleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.putExtra("s_post", s_post);
                intent.putExtra("posts", s_typeposts);
                startActivity(intent);
            }
        });
        singleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                intent.putExtra("s_post", s_post);
                intent.putExtra("posts", s_typeposts);
                startActivity(intent);
            }
        });
        singleLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                Calendar cal = Calendar.getInstance();
                final String time = dateFormat.format(cal.getTime());
                like = true;
                likedatabaseReference.child(s_post).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (like == true) {
                            if (dataSnapshot.hasChild(s_uid)) {
                                DatabaseReference data = likedatabaseReference.child(s_post).child(s_uid);
                                data.child("Name").removeValue();
                                data.child("dp").removeValue();
                                data.child("time").removeValue();
                                like = false;
                            } else {
                                loginstatusdatabaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String likename = dataSnapshot.child(s_uid).child("Name").getValue().toString();
                                        String dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
                                        likedatabaseReference.child(s_post).child(s_uid).child("Name").setValue(likename);
                                        likedatabaseReference.child(s_post).child(s_uid).child("dp").setValue(dp);
                                        likedatabaseReference.child(s_post).child(s_uid).child("time").setValue(time);
                                        DatabaseReference data = notificationdatabaseReference.push();
                                        data.child("posts").setValue("Post1");
                                        data.child("name").setValue(likename);
                                        data.child("dp").setValue(dp);
                                        data.child("type").setValue("Post1Like");
                                        data.child("s_uid").setValue(s_uid);
                                        data.child("s_post").setValue(s_post);
                                        data.child("time").setValue(time);
                                        like = false;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        databaseReference.child(s_typeposts).child(s_post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                singleName.setText(dataSnapshot.child("name").getValue().toString());
                singleDesc.setText(dataSnapshot.child("desc").getValue().toString());
                singleTime.setText(dataSnapshot.child("time").getValue().toString());
                if(dataSnapshot.hasChild("image")){
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).
                        networkPolicy(NetworkPolicy.OFFLINE).into(singleImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(singleImage);
                    }
                });
                }
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("dp").getValue().toString()).networkPolicy(NetworkPolicy.OFFLINE).into(singleDp, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("dp").getValue().toString()).into(singleDp);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setlike.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(s_post).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                    singleLike.setBackgroundResource(R.drawable.ic_thumb_up_blue);
                } else {
                    singleLike.setBackgroundResource(R.drawable.ic_thumb_up_white);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }
    public void setBlogLikeView(final String post_key){
        setlike.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int likecount = (int) dataSnapshot.getChildrenCount();
                if(likecount>0){
                    if(dataSnapshot.hasChild(firebaseAuth.getUid().toString())){
                        likecount = likecount - 1;
                        singleLikeView.setText(Html.fromHtml("<b>You and </b>"+likecount+" others liked this post"));
                    }
                    else{
                        singleLikeView.setText(Html.fromHtml(likecount+" others liked this post"));
                    }
                }
                else{
                    singleLikeView.setText("No one liked this post...");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setBlogComment(final String post_key){
        databaseReference.child("PostComment").child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int commentcount = (int) dataSnapshot.getChildrenCount();
                singleComment.setText("Comment"+"("+commentcount +")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
