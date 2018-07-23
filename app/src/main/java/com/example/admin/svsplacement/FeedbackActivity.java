package com.example.admin.svsplacement;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mRecyclerView = (RecyclerView) findViewById(R.id.feedback_recyclerview);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback");
        databaseReference.keepSynced(true);
        FirebaseRecyclerAdapter<FeedbackClass,FeedbackViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FeedbackClass, FeedbackViewHolder>(
                        FeedbackClass.class, R.layout.feedbacklayout,FeedbackViewHolder.class,databaseReference
                ) {
            @Override
            protected void populateViewHolder(FeedbackViewHolder feedbackViewHolder, FeedbackClass feedbackClass, int i) {
                feedbackViewHolder.setName(feedbackClass.getName());
                feedbackViewHolder.setMail(feedbackClass.getMail());
                feedbackViewHolder.setFeedback(feedbackClass.getFeedback());
                feedbackViewHolder.setTime(feedbackClass.getTime());
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class FeedbackViewHolder extends RecyclerView.ViewHolder{
        View viewcomment;
        TextView feedbackName;
        TextView feedbackFeedback;
        TextView feedbackTime;
        TextView feedbackMail;
        public FeedbackViewHolder(View itemView) {
            super(itemView);
            viewcomment = itemView;
            feedbackName = (TextView) viewcomment.findViewById(R.id.feedback_name);
            feedbackMail = (TextView) viewcomment.findViewById(R.id.feedback_mail);
            feedbackFeedback = (TextView) viewcomment.findViewById(R.id.feedback_feedback);
            feedbackTime = (TextView) viewcomment.findViewById(R.id.feedback_time);
        }
        public void setName(String time){
            feedbackName.setText("Name : "+time);
        }
        public void setMail(String time){
            feedbackMail.setText("MailId : "+time);
        }
        public void setFeedback(String time){
            feedbackFeedback.setText("FeedBack : "+time);
        }
        public void setTime(String time){
            feedbackTime.setText(time);
        }
    }
}
