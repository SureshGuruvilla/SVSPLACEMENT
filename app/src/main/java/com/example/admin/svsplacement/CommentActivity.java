package com.example.admin.svsplacement;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
public class CommentActivity extends AppCompatActivity {
    private EditText commentText;
    private TextView commentButton;
    private RecyclerView commentRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String s_post = null;
    private DatabaseReference commentdatabaseReference;
    private DatabaseReference loginStatusDataBase;
    private FirebaseAuth firebaseAuth;
    public String s_uid,Name,Dp,s_posts;
    private DatabaseReference notificationdatabaseReference;
    private CircleImageView commentImage;
    private String Profiledp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentText = (EditText) findViewById(R.id.comment_text);
        commentButton = (TextView) findViewById(R.id.comment_button);
        commentRecyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
        commentImage = (CircleImageView) findViewById(R.id.comment_image);
        s_post = getIntent().getExtras().getString("s_post");
        commentdatabaseReference = FirebaseDatabase.getInstance().getReference().child("PostComment").child(s_post);
        commentdatabaseReference.keepSynced(true);
        loginStatusDataBase = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        loginStatusDataBase.keepSynced(true);
        notificationdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        notificationdatabaseReference.keepSynced(true);
        firebaseAuth = FirebaseAuth.getInstance();
        s_uid = firebaseAuth.getUid();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        s_posts = getIntent().getExtras().getString("posts");
        loginStatusDataBase.child(s_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profiledp=dataSnapshot.child("Image").getValue().toString();
                Picasso.with(getApplicationContext()).load(Profiledp).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(commentImage, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError() {
                                Picasso.with(getApplicationContext()).load(Profiledp).into(commentImage);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentText.getText().toString().length()>0){
                    loginStatusDataBase.child(s_uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();
                            System.out.println(dateFormat.format(date));
                            Calendar cal = Calendar.getInstance();
                            String time = dateFormat.format(cal.getTime());
                            Name = dataSnapshot.child("Name").getValue().toString();
                            Dp =  dataSnapshot.child("Image").getValue().toString();
                            DatabaseReference data = commentdatabaseReference.push();
                            data.child("Name").setValue(Name);
                            data.child("dp").setValue(Dp);
                            data.child("uid").setValue(s_uid);
                            data.child("Comment").setValue(commentText.getText().toString());
                            data.child("time").setValue(time);
                            DatabaseReference data1 = notificationdatabaseReference.push();
                            data1.child("posts").setValue(s_posts);
                            data1.child("name").setValue(Name);
                            data1.child("dp").setValue(Dp);
                            data1.child("comment").setValue(commentText.getText().toString());
                            data1.child("type").setValue(s_posts+"Comment");
                            data1.child("s_uid").setValue(s_uid);
                            data1.child("s_post").setValue(s_post);
                            data1.child("time").setValue(time);
                            data1.child(s_uid).setValue("seen");
                            commentText.setText("");
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter Your Comment",Toast.LENGTH_SHORT).show();
                }
            }
        });
        FirebaseRecyclerAdapter<CommentClass,CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentClass, CommentViewHolder>(
                CommentClass.class,R.layout.commentlayout,CommentViewHolder.class,commentdatabaseReference
        ) {
            @Override
            protected void populateViewHolder(final CommentViewHolder commentViewHolder, CommentClass commentClass, int i) {
                final String s_key = getRef(i).getKey();
                commentViewHolder.setProfiledp(getApplicationContext(),commentClass.getDp());
                commentdatabaseReference.child(s_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = "<b>"+dataSnapshot.child("Name").getValue().toString()+"</b>";
                        commentViewHolder.mName.setText(Html.fromHtml(s));
                        commentViewHolder.commentComment.setText(dataSnapshot.child("Comment").getValue().toString());
                        commentViewHolder.commentTime.setText(dataSnapshot.child("time").getValue().toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        View viewcomment;
        TextView mName;
        TextView commentComment;
        TextView commentTime;
        private CircleImageView mDp;
        public CommentViewHolder(View itemView) {
            super(itemView);
            viewcomment = itemView;
            mName = (TextView) viewcomment.findViewById(R.id.comment_name);
            commentTime = (TextView) viewcomment.findViewById(R.id.comment_time);
            commentComment = (TextView) viewcomment.findViewById(R.id.comment_comment);
        }
        public void setProfiledp(final Context ctx, final String Profiledp){
            mDp = (CircleImageView) viewcomment.findViewById(R.id.comment_dp);
            Picasso.with(ctx).load(Profiledp).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mDp, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                    Picasso.with(ctx).load(Profiledp).into(mDp);
                }
            });
        }
    }
}