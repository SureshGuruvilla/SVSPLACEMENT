package com.example.admin.svsplacement;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
public class LikeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    DatabaseReference databaseReference;
    String s_post = null,s_Postlike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        s_post = getIntent().getExtras().getString("s_post");
        s_Postlike = getIntent().getExtras().getString("posts");
        init();
    }
    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.like_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(s_Postlike).child(s_post);
        databaseReference.keepSynced(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseRecyclerAdapter<LikeClass,LikeHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LikeClass, LikeHolder>(
                LikeClass.class,R.layout.likelayout,LikeHolder.class,databaseReference
        ) {
            @Override
            protected void populateViewHolder(final LikeHolder likeHolder, LikeClass likeClass, int i) {
                final String s_key = getRef(i).getKey();
                likeHolder.setDp(getApplicationContext(),likeClass.getDp());
                databaseReference.child(s_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = "<b>"+dataSnapshot.child("Name").getValue().toString()+"</b>";
                        likeHolder.LikeName.setText(Html.fromHtml(s)+" liked this Post on"+"\n\n\t\t\t"+dataSnapshot.child("time").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class LikeHolder extends RecyclerView.ViewHolder{
        private TextView LikeName;
        private CircleImageView LikeDp;
        View likeview;

        public LikeHolder(View itemView) {
            super(itemView);
            likeview = itemView;
            LikeName = (TextView) likeview.findViewById(R.id.likelayout_name);
        }
        public void setDp(final Context ctx, final String image){
            LikeDp = (CircleImageView) likeview.findViewById(R.id.likelayout_dp);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(LikeDp, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(LikeDp);
                }
            });
        }
    }

}