package com.example.admin.svsplacement;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;

public class Tab_1 extends Fragment {
    private RecyclerView tab1RecyclerView;
    private EditText tab1PostEditText;
    private ImageView tab1PostImageView;
    private TextView tab1PostTextView;
    private TextView tab1PostPostTextView;
    private DatabaseReference tab1DataBaseReference;
    public FirebaseAuth tab1FireBaseAuth;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImagUri = null;
    private StorageReference tab1StorageReference;
    private LinearLayoutManager mLayoutManager;
    public Context ctx;
    private LinearLayout tab1Layout;
    private String s_uid = null;
    private ProgressDialog tab1progressDialog;
    private String name = null;
    private String dp = null;
    private DatabaseReference likedatabaseReference;
    private DatabaseReference loginstatusdatabaseReference;
    private Boolean like;
    private DatabaseReference notificationdatabaseReference;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public static final String TIME_FORMAT = "h:mm a";
    View v;
    private String string_post;
    public void Start() {
        tab1RecyclerView = (RecyclerView) v.findViewById(R.id.tab1_recyclerview);
        tab1RecyclerView.setHasFixedSize(false);
        tab1RecyclerView.setNestedScrollingEnabled(false);
        tab1PostEditText = (EditText) v.findViewById(R.id.tab1_postEditText);
        tab1PostTextView = (TextView) v.findViewById(R.id.tab1_posttextView);
        tab1PostImageView = (ImageView) v.findViewById(R.id.tab1_postImageView);
        tab1PostPostTextView = (TextView) v.findViewById(R.id.tab1_postPostTextView);
        tab1DataBaseReference = FirebaseDatabase.getInstance().getReference();
        tab1FireBaseAuth = FirebaseAuth.getInstance();
        tab1StorageReference = FirebaseStorage.getInstance().getReference();
        tab1DataBaseReference.keepSynced(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        tab1Layout = (LinearLayout) v.findViewById(R.id.tab1_layout);
        s_uid = tab1FireBaseAuth.getCurrentUser().getUid();
        tab1progressDialog = new ProgressDialog(getActivity());
        loginstatusdatabaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        loginstatusdatabaseReference.keepSynced(true);
        likedatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post1Like");
        likedatabaseReference.keepSynced(true);
        notificationdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        notificationdatabaseReference.keepSynced(true);
        tab1PostTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        tab1PostPostTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tab1PostEditText.getText().length()>0){
                    tab1DataBaseReference.child("LoginStatus").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            name = dataSnapshot.child(s_uid).child("Name").getValue().toString();
                            dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
                            if(mImagUri!=null){
                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setTitle("Uploading");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                StorageReference storageReference = tab1StorageReference.child("Post_Images").child(mImagUri.getLastPathSegment());
                                storageReference.putFile(mImagUri)


                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Date date = new Date();
                                                System.out.println(dateFormat.format(date));
                                                Calendar cal = Calendar.getInstance();
                                                String time = dateFormat.format(cal.getTime());
                                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                DatabaseReference databaseReference = tab1DataBaseReference.child("Post1").push();
                                                databaseReference.child("image").setValue(downloadUrl.toString());
                                                databaseReference.child("desc").setValue(tab1PostEditText.getText().toString());
                                                databaseReference.child("u_id").setValue(s_uid.toString());
                                                databaseReference.child("name").setValue(name.toString());
                                                databaseReference.child("dp").setValue(dp.toString());
                                                databaseReference.child("time").setValue(time);
                                                DatabaseReference data = notificationdatabaseReference.push();
                                                data.child("posts").setValue("Post1");
                                                data.child("name").setValue(name);
                                                data.child("dp").setValue(dp);
                                                data.child("desc").setValue(tab1PostEditText.getText().toString());
                                                data.child("type").setValue("Post1");
                                                data.child("s_uid").setValue(s_uid);
                                                data.child("s_post").setValue(databaseReference.getRef().getKey().toString());
                                                data.child("time").setValue(time);
                                                data.child(s_uid).setValue("seen");
                                                mImagUri=null;
                                                tab1PostEditText.setText("");
                                                tab1PostImageView.setVisibility(View.GONE);
                                                progressDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                //if the upload is not successfull
                                                //hiding the progress dialog
                                                progressDialog.dismiss();

                                                //and displaying error message
                                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        })

                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                //calculating progress percentage
                                                double progress = (100.0 * taskSnapshot.getBytesTransferred())
                                                        / taskSnapshot.getTotalByteCount();

                                                //displaying percentage in progress dialog
                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                            }
                                        });
                            }
                            else {
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                System.out.println(dateFormat.format(date));
                                Calendar cal = Calendar.getInstance();
                                String time = dateFormat.format(cal.getTime());
                                DatabaseReference databaseReference = tab1DataBaseReference.child("Post1").push();
                                databaseReference.child("desc").setValue(tab1PostEditText.getText().toString());
                                databaseReference.child("u_id").setValue(s_uid.toString());
                                databaseReference.child("name").setValue(name.toString());
                                databaseReference.child("dp").setValue(dp.toString());
                                databaseReference.child("time").setValue(time);
                                DatabaseReference data = notificationdatabaseReference.push();
                                data.child("posts").setValue("Post1");
                                data.child("name").setValue(name);
                                data.child("dp").setValue(dp);
                                data.child("desc").setValue(tab1PostEditText.getText().toString());
                                data.child("type").setValue("Post1");
                                data.child("s_uid").setValue(s_uid);
                                data.child("s_post").setValue(databaseReference.getRef().getKey().toString());
                                data.child("time").setValue(time);
                                data.child(s_uid).setValue("seen");
                                mImagUri=null;
                                tab1PostEditText.setText("");
                                tab1PostImageView.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
                else {
                    Toast.makeText(ctx, "Enter Post description to post", Toast.LENGTH_SHORT).show();
                }
            }
        });
        FirebaseRecyclerAdapter<FirebaseHelper,Tab_1.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<FirebaseHelper,Tab_1.BlogViewHolder>(
                        FirebaseHelper.class,
                        R.layout.blog_row,
                        Tab_1.BlogViewHolder.class,
                        tab1DataBaseReference.child("Post1")
                ) {
                    @Override
                    protected void populateViewHolder(final Tab_1.BlogViewHolder viewHolder, final FirebaseHelper model, int position) {

                        viewHolder.setName(model.getName());final String s_post = getRef(position).getKey();
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                        viewHolder.setDp(getActivity().getApplicationContext(),model.getU_id());
                        viewHolder.setlikeImage(s_post);
                        viewHolder.setTime(model.getTime());
                        viewHolder.setBlogLikeView(s_post);
                        viewHolder.setBlogComment(s_post);
                        viewHolder.blogComment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), CommentActivity.class);
                                intent.putExtra("s_post", s_post);
                                intent.putExtra("posts", "Post1");
                                getActivity().startActivity(intent);
                            }
                        });
                        viewHolder.blogLikeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), LikeActivity.class);
                                intent.putExtra("s_post", s_post);
                                intent.putExtra("posts", "Post1Like");
                                getActivity().startActivity(intent);
                            }
                        });
                        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), SinglePostActivity.class);
                                intent.putExtra("s_post", s_post);
                                intent.putExtra("posts", "Post1");
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
                };
        final LinearLayoutManager mLayoutManager1= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        tab1RecyclerView.setLayoutManager(mLayoutManager1);
        tab1RecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public Tab_1() {
    }
    public static Tab_1 newInstance(String param1, String param2) {
        Tab_1 fragment = new Tab_1();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab_1, container, false);
        ctx = getActivity().getApplicationContext();
        try {
            Start();
        }
        catch (Exception e){

        }
        return v;
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImagUri = data.getData();
            tab1PostImageView.setImageURI(mImagUri);
        }
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        private DatabaseReference databaseReference;
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
        private TextView post_desc;
        private TextView blogName;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            setlike = FirebaseDatabase.getInstance().getReference().child("Post1Like");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("PostComment");
            databaseReference.keepSynced(true);
            blogLike = (TextView) mView.findViewById(R.id.blog_like);
            blogLikeView = (TextView) mView.findViewById(R.id.blog_likeview);
            firebaseAuth = FirebaseAuth.getInstance();
            blogComment = (TextView) mView.findViewById(R.id.blog_comment);
            blogTime = (TextView) mView.findViewById(R.id.blog_time);
            blogShare = (TextView) mView.findViewById(R.id.blog_share);
            idforShare = (LinearLayout) mView.findViewById(R.id.sharingimage);
            dp = (CircleImageView) mView.findViewById(R.id.blog_dp);
            post_desc = (TextView) mView.findViewById(R.id.post_desc);
            blogName = (TextView) mView.findViewById(R.id.blog_name);
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

            post_desc.setText(desc);
        }
        public void setName(String name){

            String s = "<b><font color='#000000'>"+String.valueOf(name)+"</font></b>"+" posted a new Post";
            blogName.setText(Html.fromHtml(s));
        }
        public void setDp(final Context ctx,final String u_id){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
            try {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        try {
                            if(dataSnapshot.child(u_id).child("Image").getValue().toString()!=null){
                                Picasso.with(ctx).load(dataSnapshot.child(u_id).child("Image").getValue().toString())
                                        .networkPolicy(NetworkPolicy.OFFLINE).into(dp, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }
                                    @Override
                                    public void onError() {
                                        Picasso.with(ctx).load(dataSnapshot.child(u_id).child("Image").getValue().toString()).into(dp);
                                    }
                                });
                            }
                            else {
                            }
                        }
                        catch (Exception e){
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            catch (Exception e){
            }
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
    public class Post extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        public Post(Context activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Posting...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Thread.sleep(0);
            }
             catch (InterruptedException e) {
                e.printStackTrace();
            }
            tab1DataBaseReference.child("LoginStatus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    name = dataSnapshot.child(s_uid).child("Name").getValue().toString();
                    dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
                    if(mImagUri!=null){
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();
                        StorageReference storageReference = tab1StorageReference.child("Post_Images").child(mImagUri.getLastPathSegment());
                        storageReference.putFile(mImagUri)


                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                System.out.println(dateFormat.format(date));
                                Calendar cal = Calendar.getInstance();
                                String time = dateFormat.format(cal.getTime());
                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                DatabaseReference databaseReference = tab1DataBaseReference.child("Post1").push();
                                databaseReference.child("image").setValue(downloadUrl.toString());
                                databaseReference.child("desc").setValue(tab1PostEditText.getText().toString());
                                databaseReference.child("u_id").setValue(s_uid.toString());
                                databaseReference.child("name").setValue(name.toString());
                                databaseReference.child("dp").setValue(dp.toString());
                                databaseReference.child("time").setValue(time);
                                DatabaseReference data = notificationdatabaseReference.push();
                                data.child("posts").setValue("Post1");
                                data.child("name").setValue(name);
                                data.child("dp").setValue(dp);
                                data.child("desc").setValue(tab1PostEditText.getText().toString());
                                data.child("type").setValue("Post1");
                                data.child("s_uid").setValue(s_uid);
                                data.child("s_post").setValue(databaseReference.getRef().getKey().toString());
                                data.child("time").setValue(time);
                                data.child(s_uid).setValue("seen");
                                mImagUri=null;
                                tab1PostEditText.setText("");
                                tab1PostImageView.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })

                         .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //calculating progress percentage
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred())
                                                / taskSnapshot.getTotalByteCount();

                                        //displaying percentage in progress dialog
                                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                    }
                                });
                    }
                    else {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));
                        Calendar cal = Calendar.getInstance();
                        String time = dateFormat.format(cal.getTime());
                        DatabaseReference databaseReference = tab1DataBaseReference.child("Post1").push();
                        databaseReference.child("desc").setValue(tab1PostEditText.getText().toString());
                        databaseReference.child("u_id").setValue(s_uid.toString());
                        databaseReference.child("name").setValue(name.toString());
                        databaseReference.child("dp").setValue(dp.toString());
                        databaseReference.child("time").setValue(time);
                        DatabaseReference data = notificationdatabaseReference.push();
                        data.child("posts").setValue("Post1");
                        data.child("name").setValue(name);
                        data.child("dp").setValue(dp);
                        data.child("desc").setValue(tab1PostEditText.getText().toString());
                        data.child("type").setValue("Post1");
                        data.child("s_uid").setValue(s_uid);
                        data.child("s_post").setValue(databaseReference.getRef().getKey().toString());
                        data.child("time").setValue(time);
                        data.child(s_uid).setValue("seen");
                        mImagUri=null;
                        tab1PostEditText.setText("");
                        tab1PostImageView.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            return null;
        }
    }
}
