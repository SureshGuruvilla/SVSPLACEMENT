package com.example.admin.svsplacement;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
public class Tab_4 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private TextView personName;
    private CircleImageView personDp;
    private RecyclerView personsRecyclerView;
    private View view;
    private LinearLayoutManager linearLayout;
    private FirebaseAuth firebaseAuth;
    private Boolean like;
    private DatabaseReference post1DatabaseReference;
    private DatabaseReference likedatabaseReference;
    private DatabaseReference notificationdatabaseReference;
    private DatabaseReference loginstatusdatabaseReference;
    private LinearLayout tab4Layout;
    public Tab_4() {
    }
    public static Tab_4 newInstance(String param1, String param2) {
        Tab_4 fragment = new Tab_4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_4, container,false);
        try {
            Start();
        }
        catch (Exception e){
        }
        return view;
    }

    public void Start() {
        personName = (TextView) view.findViewById(R.id.person_name);
        personDp = (CircleImageView) view.findViewById(R.id.person_dp);
        personsRecyclerView = (RecyclerView) view.findViewById(R.id.person_recyclerview);
        tab4Layout = (LinearLayout) view.findViewById(R.id.tab4_layout);
        personsRecyclerView.setHasFixedSize(false);
        personsRecyclerView.setNestedScrollingEnabled(false);
        linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        firebaseAuth = FirebaseAuth.getInstance();
        post1DatabaseReference = FirebaseDatabase.getInstance().getReference();
        final String s_uid = firebaseAuth.getCurrentUser().getUid();
        loginstatusdatabaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        loginstatusdatabaseReference.keepSynced(true);
        likedatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post1Like");
        likedatabaseReference.keepSynced(true);
        notificationdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        notificationdatabaseReference.keepSynced(true);
        tab4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),EditProfileActivity.class);
                intent.putExtra("s_uid",s_uid);
                startActivity(intent);
            }
        });
        post1DatabaseReference.child("LoginStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child(s_uid).child("Name").getValue().toString();
                final String dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
                personName.setText(name);
                Picasso.with(getContext()).load(dp).networkPolicy(NetworkPolicy.OFFLINE).into(personDp, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getContext()).load(dp).into(personDp);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerAdapter<FirebaseHelper,Tab_4.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FirebaseHelper, Tab_4.BlogViewHolder>(
                FirebaseHelper.class,R.layout.blog_row,BlogViewHolder.class,post1DatabaseReference.child("Post1")
        ) {
            @Override
            protected void populateViewHolder(final Tab_4.BlogViewHolder viewHolder, final FirebaseHelper model, int i) {
                final String s_post =getRef(i).getKey();
                if(s_uid.equals(model.getU_id())){
                    viewHolder.setName(model.getName());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());
                    viewHolder.setDp(getActivity().getApplicationContext(),model.getU_id());
                    viewHolder.setlikeImage(s_post);
                    viewHolder.setTime(model.getTime());
                    viewHolder.setBlogLikeView(s_post);
                    viewHolder.setBlogComment(s_post);
                    viewHolder.blogComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),CommentActivity.class);
                            intent.putExtra("s_post",s_post);
                            intent.putExtra("posts","Post1");
                            getActivity().startActivity(intent);
                        }
                    });
                    viewHolder.blogLikeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),LikeActivity.class);
                            intent.putExtra("s_post",s_post);
                            intent.putExtra("posts","Post1Like");
                            getActivity().startActivity(intent);
                        }
                    });
                    viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),SinglePostActivity.class);
                            intent.putExtra("s_post",s_post);
                            intent.putExtra("posts","Post1");
                            getActivity().startActivity(intent);
                        }
                    });
                    viewHolder.blogShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bitmap bitmap = viewHolder.getBitmapFromView(viewHolder.idforShare);
                            try {
                                File file = new File(getContext().getExternalCacheDir(), "logicchip.png");
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
                    viewHolder.blogLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            like = true;
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();
                            System.out.println(dateFormat.format(date));
                            Calendar cal = Calendar.getInstance();
                            final String time1 = dateFormat.format(cal.getTime());
                            likedatabaseReference.child(s_post).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(like == true){
                                        if(dataSnapshot.hasChild(s_uid)){
                                            DatabaseReference data = likedatabaseReference.child(s_post).child(s_uid);
                                            data.child("Name").removeValue();
                                            data.child("dp").removeValue();
                                            data.child("time").removeValue();
                                            like = false;
                                        }
                                        else{
                                            loginstatusdatabaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String likename = dataSnapshot.child(s_uid).child("Name").getValue().toString();
                                                    String dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
                                                    likedatabaseReference.child(s_post).child(s_uid).child("Name").setValue(likename);
                                                    likedatabaseReference.child(s_post).child(s_uid).child("dp").setValue(dp);
                                                    likedatabaseReference.child(s_post).child(s_uid).child("time").setValue(time1);
                                                    DatabaseReference data = notificationdatabaseReference.push();
                                                    data.child("posts").setValue("Post1");
                                                    data.child("name").setValue(likename);
                                                    data.child("dp").setValue(dp);
                                                    data.child("type").setValue("Post1Like");
                                                    data.child("s_uid").setValue(s_uid);
                                                    data.child("s_post").setValue(s_post);
                                                    data.child("time").setValue(time1);
                                                    data.child(s_uid).setValue("seen");
                                                    like = false;
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    }
                                    else{
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    });
                }
                else {
                    viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }
        };
        personsRecyclerView.setLayoutManager(linearLayout);
        personsRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        private TextView blogLikeView;
        private ImageView post_image;
        private TextView blogLike;
        private TextView blogComment;
        private CircleImageView dp;
        private DatabaseReference setlike;
        private FirebaseAuth firebaseAuth;
        private TextView blogTime;
        private TextView blogShare;
        private LinearLayout idforShare;
        private DatabaseReference databaseReference;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            blogLike = (TextView) mView.findViewById(R.id.blog_like);
            blogLikeView = (TextView) mView.findViewById(R.id.blog_likeview);
            setlike = FirebaseDatabase.getInstance().getReference().child("Post1Like");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("PostComment");
            firebaseAuth = FirebaseAuth.getInstance();
            blogComment = (TextView) mView.findViewById(R.id.blog_comment);
            blogTime = (TextView) mView.findViewById(R.id.blog_time);
            blogShare = (TextView) mView.findViewById(R.id.blog_share);
            idforShare = (LinearLayout) mView.findViewById(R.id.sharingimage);
        }
        public void setTime(String time){
            blogTime.setText(time);
        }
        public void setImage(final Context ctx,final String image) {
            post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(post_image);
                }
            });
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
        public void setName(String name){
            TextView blogName = (TextView) mView.findViewById(R.id.blog_name);
            String s = "<b><font color='#000000'>"+String.valueOf(name)+"</font></b>"+" posted a new Post";
            blogName.setText(Html.fromHtml(s));
        }
        public void setDp(final Context ctx,final String u_id){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
            final String[] image = new String[1];
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    image[0] = dataSnapshot.child(u_id).child("Image").getValue(String.class);
                    dp = (CircleImageView) mView.findViewById(R.id.blog_dp);
                    Picasso.with(ctx).load(image[0]).networkPolicy(NetworkPolicy.OFFLINE).into(dp, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(image[0]).into(dp);
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        public void setlikeImage(final String post_key){
            setlike.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(post_key).hasChild(firebaseAuth.getCurrentUser().getUid())){
                        blogLike.setBackgroundResource(R.drawable.ic_thumb_up_blue);
                    }
                    else {
                        blogLike.setBackgroundResource(R.drawable.ic_thumb_up_white);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setBlogLikeView(final String post_key){
            setlike.child(post_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int likecount = (int) dataSnapshot.getChildrenCount();
                    if(likecount>0){
                        if(dataSnapshot.hasChild(firebaseAuth.getUid().toString())){
                            likecount = likecount - 1;
                            blogLikeView.setText(Html.fromHtml("<b>You</b> and "+likecount+" others liked this post"));
                        }
                        else{
                            blogLikeView.setText(Html.fromHtml(likecount+" others liked this post"));
                        }
                    }
                    else{
                        blogLikeView.setText("No one liked this post...");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        public void setBlogComment(final String post_key){
            databaseReference.child(post_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int commentcount = (int) dataSnapshot.getChildrenCount();
                    blogComment.setText("Comment"+"("+commentcount +")");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        private Bitmap getBitmapFromView(View view) {
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            Drawable bgDrawable =view.getBackground();
            if (bgDrawable!=null) {
                //has background drawable, then draw it on the canvas
                bgDrawable.draw(canvas);
            }   else{
                //does not have background drawable, then draw white background on the canvas
                canvas.drawColor(Color.WHITE);
            }
            view.draw(canvas);
            return returnedBitmap;
        }
    }
}