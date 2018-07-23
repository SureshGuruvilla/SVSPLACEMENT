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
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Tab_2 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView tab2RecyclerView;
    private EditText tab2PostEditText;
    private ImageView tab2PostImageView;
    private TextView tab2PostTextView;
    private TextView tab2PostPostTextView;
    private DatabaseReference tab2DataBaseReference;
    public FirebaseAuth tab2FireBaseAuth;
    private static final int GALLERY_REQUEST = 1;
    private Uri mImagUri = null;
    private StorageReference tab2StorageReference;
    private LinearLayoutManager mLayoutManager;
    public Context ctx;
    private LinearLayout tab2Layout;
    private String s_uid = null;
    private ProgressDialog tab2progressDialog;
    private String name = null;
    private String dp = null;
    private DatabaseReference likedatabaseReference;
    private DatabaseReference loginstatusdatabaseReference;
    private Boolean like;
    private String mParam1;
    private String mParam2;
    private DatabaseReference notificationdatabaseReference;
    private OnFragmentInteractionListener mListener;
    private String spinnerName,s_spinnerAuto;
    private Spinner tab2spinner;
    private AutoCompleteTextView spinnerAuto;
    public static String Dept,s_CGPA;
    public static Float CGPA;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void Start() {
        tab2RecyclerView = (RecyclerView) v.findViewById(R.id.tab2_recyclerview);
        tab2RecyclerView.setHasFixedSize(false);
        tab2RecyclerView.setNestedScrollingEnabled(false);
        tab2PostEditText = (EditText) v.findViewById(R.id.tab2_postEditText);
        tab2PostTextView = (TextView) v.findViewById(R.id.tab2_insertphoto);
        tab2PostImageView = (ImageView) v.findViewById(R.id.tab2_postImageView);
        tab2PostPostTextView = (TextView) v.findViewById(R.id.tab2_postPostTextView);
        tab2DataBaseReference = FirebaseDatabase.getInstance().getReference();
        tab2FireBaseAuth = FirebaseAuth.getInstance();
        tab2StorageReference = FirebaseStorage.getInstance().getReference();
        tab2DataBaseReference.keepSynced(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        tab2Layout = (LinearLayout) v.findViewById(R.id.tab2_layout);
        tab2spinner = (Spinner) v.findViewById(R.id.tab2_spinner);
        spinnerAuto = (AutoCompleteTextView) v.findViewById(R.id.tab2_auto);
        s_uid = tab2FireBaseAuth.getCurrentUser().getUid();
        tab2progressDialog = new ProgressDialog(getActivity());
        likedatabaseReference = FirebaseDatabase.getInstance().getReference().child("Post2Like");
        likedatabaseReference.keepSynced(true);
        loginstatusdatabaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        loginstatusdatabaseReference.keepSynced(true);
        notificationdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        notificationdatabaseReference.keepSynced(true);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_name2, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tab2spinner.setAdapter(adapter);
        tab2spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerName = (String) parent.getItemAtPosition(position);
                if(spinnerName == parent.getItemAtPosition(0)){
                    spinnerAuto.setVisibility(View.GONE);
                }
                else{
                    spinnerAuto.setVisibility(View.VISIBLE);
                    ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                            R.array.autocomplete1, android.R.layout.simple_list_item_1);
                    spinnerAuto.setAdapter(adapter1);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        loginstatusdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(tab2FireBaseAuth.getUid()).child("position").getValue(String.class)
                        .equalsIgnoreCase("student")){
                    tab2Layout.setVisibility(LinearLayout.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        tab2PostTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        tab2PostPostTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tab2PostEditText.getText().length()>0){
                    tab2DataBaseReference.child("LoginStatus").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            name = dataSnapshot.child(s_uid).child("Name").getValue().toString();
                            dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
                            if(mImagUri!=null){
                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setTitle("Uploading");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                StorageReference storageReference = tab2StorageReference.child("Post_Images").child(mImagUri.getLastPathSegment());
                                storageReference.putFile(mImagUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        s_spinnerAuto = spinnerAuto.getText().toString();
                                        if(s_spinnerAuto.length()>0){
                                        }
                                        else {
                                            s_spinnerAuto = "all";
                                        }
                                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                        Date date = new Date();
                                        System.out.println(dateFormat.format(date));
                                        Calendar cal = Calendar.getInstance();
                                        String time = dateFormat.format(cal.getTime());
                                        DatabaseReference databaseReference = tab2DataBaseReference.child("Post2").push();
                                        databaseReference.child("image").setValue(downloadUrl.toString());
                                        databaseReference.child("desc").setValue(tab2PostEditText.getText().toString());
                                        databaseReference.child("u_id").setValue(s_uid.toString());
                                        databaseReference.child("name").setValue(name.toString());
                                        databaseReference.child("dp").setValue(dp.toString());
                                        databaseReference.child("time").setValue(time);
                                        databaseReference.child("spinner").setValue(spinnerName);
                                        databaseReference.child("spinnerAuto").setValue(s_spinnerAuto);
                                        tab2PostEditText.setText("");
                                        mImagUri=null;
                                        tab2PostImageView.setVisibility(View.GONE);
                                        progressDialog.dismiss();
                                    }
                                }
                                )


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
                                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                                //displaying percentage in progress dialog
                                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                            }
                                        });
                            }
                            else {
                                s_spinnerAuto = spinnerAuto.getText().toString();
                                if(s_spinnerAuto.length()>0){
                                }
                                else {
                                    s_spinnerAuto = "all";
                                }
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                System.out.println(dateFormat.format(date));
                                Calendar cal = Calendar.getInstance();
                                String time = dateFormat.format(cal.getTime());
                                DatabaseReference databaseReference = tab2DataBaseReference.child("Post2").push();
                                databaseReference.child("desc").setValue(tab2PostEditText.getText().toString());
                                databaseReference.child("u_id").setValue(s_uid);
                                databaseReference.child("name").setValue(name);
                                databaseReference.child("dp").setValue(dp);
                                databaseReference.child("time").setValue(time);
                                databaseReference.child("spinner").setValue(spinnerName);
                                databaseReference.child("spinnerAuto").setValue(s_spinnerAuto);
                                tab2PostEditText.setText("");
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
        loginstatusdatabaseReference.child(s_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Dept = dataSnapshot.child("Dept").getValue(String.class);
                s_CGPA = dataSnapshot.child("CGPA").getValue(String.class);
                try {
                    CGPA = Float.parseFloat(s_CGPA);
                }
                catch (NumberFormatException e){
                    CGPA = 0f;
                    System.out.println("numberStr is not a number");
                }
                catch (NullPointerException e){
                    CGPA = 0f;
                    System.out.println("Null");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerAdapter<FirebaseHelper,Tab_2.BlogViewHolder> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<FirebaseHelper,Tab_2.BlogViewHolder>(
                        FirebaseHelper.class,
                        R.layout.blog_row1,
                        Tab_2.BlogViewHolder.class,
                        tab2DataBaseReference.child("Post2")
                ) {
                    @Override
                    protected void populateViewHolder(final Tab_2.BlogViewHolder viewHolder,
                                                      final FirebaseHelper model,
                                                      int position) {
                            final String s_post = getRef(position).getKey();

                            tab2DataBaseReference.child("Post2").child(s_post).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String postspinner = dataSnapshot.child("spinner").getValue(String.class);
                                    String postspinnerauto = dataSnapshot.child("spinnerAuto").getValue(String.class);
                                    assert postspinner != null;
                                    assert postspinnerauto != null;
                                    if(postspinner.equals("All")||
                                            postspinner.equals("Department") && postspinnerauto.equals(Dept)
                                            ||postspinner.equals("CGPA") &&
                                            Float.parseFloat(postspinnerauto) <= CGPA ||Dept.equals("admin")) {

                                        viewHolder.coordinatorLayout.setVisibility(View.VISIBLE);
                                        viewHolder.setName(model.getName());
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
                                                intent.putExtra("posts", "Post2");
                                                getActivity().startActivity(intent);
                                            }
                                        });
                                        viewHolder.blogLikeView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), LikeActivity.class);
                                                intent.putExtra("s_post", s_post);
                                                intent.putExtra("posts", "Post2Like");
                                                getActivity().startActivity(intent);
                                            }
                                        });
                                        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(getActivity(), SinglePostActivity.class);
                                                intent.putExtra("s_post", s_post);
                                                intent.putExtra("posts", "Post2");
                                                getActivity().startActivity(intent);
                                            }
                                        });
                                        viewHolder.blog1Share.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Bitmap bitmap = viewHolder.getBitmapFromView(viewHolder.linearLayout);
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
                                        viewHolder.blogLikeView.setVisibility(View.GONE);
                                        viewHolder.blogLike.setVisibility(View.GONE);
                                        viewHolder.blogLikeView.setVisibility(View.GONE);
                                        viewHolder.blogComment.setVisibility(View.GONE);
                                        viewHolder.blog1Share.setVisibility(View.GONE);
                                        viewHolder.blogTime.setVisibility(View.GONE);
                                        viewHolder.linearLayout.setVisibility(View.GONE);
                                        viewHolder.mView.setVisibility(View.GONE);
                                        viewHolder.coordinatorLayout.setVisibility(View.GONE);
                                        viewHolder.blogName.setVisibility(View.GONE);
                                        viewHolder.post_desc.setVisibility(View.GONE);
                                        viewHolder.dp.setVisibility(View.GONE);
                                        viewHolder.post_image.setVisibility(View.GONE);
//                                        viewHolder.mView.setLayoutParams(new RecyclerView.LayoutParams(
//                                                ViewGroup.LayoutParams.WRAP_CONTENT,
//                                                ViewGroup.LayoutParams.WRAP_CONTENT));
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                };
        final LinearLayoutManager mLayoutManager1= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        tab2RecyclerView.setLayoutManager(mLayoutManager1);
        tab2RecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public Tab_2() {
    }
    public static Tab_2 newInstance(String param1, String param2) {
        Tab_2 fragment = new Tab_2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImagUri = data.getData();
            tab2PostImageView.setImageURI(mImagUri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tab_2, container, false);
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
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        private CoordinatorLayout coordinatorLayout;
        View mView;
        private TextView blogLikeView;
        private ImageView post_image;
        private TextView blogLike;
        private TextView blogComment;
        private CircleImageView dp;
        private DatabaseReference setlike;
        private FirebaseAuth firebaseAuth;
        private TextView blogTime;
        private LinearLayout linearLayout;
        private TextView blog1Share;
        private DatabaseReference databaseReference;
        private TextView post_desc,blogName;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            blogLike = (TextView) mView.findViewById(R.id.blog1_like);
            blogLikeView = (TextView) mView.findViewById(R.id.blog1_likeview);
            setlike = FirebaseDatabase.getInstance().getReference().child("Post2Like");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("PostComment");
            firebaseAuth = FirebaseAuth.getInstance();
            blogComment = (TextView) mView.findViewById(R.id.blog1_comment);
            blogTime = (TextView) mView.findViewById(R.id.blog1_time);
            linearLayout = (LinearLayout) mView.findViewById(R.id.sharingimage1);
            blog1Share = (TextView) mView.findViewById(R.id.blog1_share);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
            coordinatorLayout = (CoordinatorLayout) mView.findViewById(R.id.blog1row);
            post_image = (ImageView) mView.findViewById(R.id.post1_image);
            post_desc = (TextView) mView.findViewById(R.id.post1_desc);
            blogName = (TextView) mView.findViewById(R.id.blog1_name);
            dp = (CircleImageView) mView.findViewById(R.id.blog1_dp);
        }
        public void setTime(String time){
            blogTime.setText(time);
        }
        public void setImage(final Context ctx,final String image) {

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
                Thread.sleep(2000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            tab2DataBaseReference.child("LoginStatus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child(s_uid).child("Name").getValue().toString();
                    dp = dataSnapshot.child(s_uid).child("Image").getValue().toString();
                    if(mImagUri!=null){
                        StorageReference storageReference = tab2StorageReference.child("Post_Images").child(mImagUri.getLastPathSegment());
                        storageReference.putFile(mImagUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                s_spinnerAuto = spinnerAuto.getText().toString();
                                if(s_spinnerAuto.length()>0){
                                }
                                else {
                                    s_spinnerAuto = "all";
                                }
                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                System.out.println(dateFormat.format(date));
                                Calendar cal = Calendar.getInstance();
                                String time = dateFormat.format(cal.getTime());
                                DatabaseReference databaseReference = tab2DataBaseReference.child("Post2").push();
                                databaseReference.child("image").setValue(downloadUrl.toString());
                                databaseReference.child("desc").setValue(tab2PostEditText.getText().toString());
                                databaseReference.child("u_id").setValue(s_uid.toString());
                                databaseReference.child("name").setValue(name.toString());
                                databaseReference.child("dp").setValue(dp.toString());
                                databaseReference.child("time").setValue(time);
                                databaseReference.child("spinner").setValue(spinnerName);
                                databaseReference.child("spinnerAuto").setValue(s_spinnerAuto);

                                DatabaseReference data = notificationdatabaseReference.push();
                                data.child("posts").setValue("Post2");
                                data.child("name").setValue(name);
                                data.child("dp").setValue(dp);
                                data.child("desc").setValue(tab2PostEditText.getText().toString());
                                data.child("type").setValue("Post2");
                                data.child("s_uid").setValue(s_uid);
                                data.child("s_post").setValue(databaseReference.getRef().getKey().toString());
                                data.child("time").setValue(time);
                                data.child(s_uid).setValue("seen");
                                tab2PostEditText.setText("");
                                spinnerAuto.setText("");
                                mImagUri=null;
                                tab2PostImageView.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        s_spinnerAuto = spinnerAuto.getText().toString();
                        if(s_spinnerAuto.length()>0){
                        }
                        else {
                            s_spinnerAuto = "all";
                        }
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));
                        Calendar cal = Calendar.getInstance();
                        String time = dateFormat.format(cal.getTime());
                        DatabaseReference databaseReference = tab2DataBaseReference.child("Post2").push();
                        databaseReference.child("desc").setValue(tab2PostEditText.getText().toString());
                        databaseReference.child("u_id").setValue(s_uid.toString());
                        databaseReference.child("name").setValue(name.toString());
                        databaseReference.child("dp").setValue(dp.toString());
                        databaseReference.child("time").setValue(time);
                        databaseReference.child("spinner").setValue(spinnerName);
                        databaseReference.child("spinnerAuto").setValue(s_spinnerAuto);

                        DatabaseReference data = notificationdatabaseReference.push();
                        data.child("posts").setValue("Post2");
                        data.child("name").setValue(name);
                        data.child("dp").setValue(dp);
                        data.child("desc").setValue(tab2PostEditText.getText().toString());
                        data.child("type").setValue("Post2");
                        data.child("s_uid").setValue(s_uid);
                        data.child("s_post").setValue(databaseReference.getRef().getKey().toString());
                        data.child("time").setValue(time);
                        data.child(s_uid).setValue("seen");
                        tab2PostEditText.setText("");
                        spinnerAuto.setText("");
                        mImagUri=null;
                        tab2PostImageView.setVisibility(View.GONE);
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