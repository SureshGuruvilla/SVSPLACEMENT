package com.example.admin.svsplacement;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements Tab_1.OnFragmentInteractionListener,
        Tab_2.OnFragmentInteractionListener,Tab_3.OnFragmentInteractionListener,Tab_4.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth mainFirebaseAuth;
//    private PagerAdapter pagerAdapter;

    private DatabaseReference logindatabasereference;
    private DatabaseReference databaseReference;
    private TextView name;
    private CircleImageView dp;
    public static TextView tv;
    private NavigationView navigationView;
    public static View v;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainFirebaseAuth = FirebaseAuth.getInstance();
        logindatabasereference = FirebaseDatabase.getInstance().getReference().child("LoginStatus");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notification");

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Drawable drawable = navigationView.getMenu().findItem(R.id.main).getIcon();
        if(drawable!=null){
            drawable.mutate();
            drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        }


        viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        FragmentHelper pagerAdapter = new FragmentHelper(getSupportFragmentManager());
        pagerAdapter.addFragment(new Tab_1(),"Tab_1");
        pagerAdapter.addFragment(new Tab_2(),"Tab_2");
        pagerAdapter.addFragment(new Tab_3(),"Tab_3");
        pagerAdapter.addFragment(new Tab_4(),"Tab_4");
        viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        settabicons();
        navigation();
    }
    private void navigation() {
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.admin).setVisible(false);
        nav_menu.findItem(R.id.search).setVisible(false);
        nav_menu.findItem(R.id.feedback).setVisible(false);
        try{
            logindatabasereference.child(mainFirebaseAuth.getUid().toString()).child("position").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String position = dataSnapshot.getValue(String.class);
                    if(position!=null && position.equalsIgnoreCase("admin")){
                        Menu nav_menu = navigationView.getMenu();
                        nav_menu.findItem(R.id.admin).setVisible(true);
                        nav_menu.findItem(R.id.search).setVisible(true);
                        nav_menu.findItem(R.id.feedback).setVisible(true);
                    }
                    else {
                        Menu nav_menu = navigationView.getMenu();
                        nav_menu.findItem(R.id.admin).setVisible(false);
                        nav_menu.findItem(R.id.search).setVisible(false);
                        nav_menu.findItem(R.id.feedback).setVisible(false);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        try{
            View header = navigationView.getHeaderView(0);
            name = (TextView)header.findViewById(R.id.header_name);
            dp = (CircleImageView) header.findViewById(R.id.header_dp);
            dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,EditProfileActivity.class));
                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,EditProfileActivity.class));
                }
            });

            logindatabasereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                        name.setText(dataSnapshot.child(Objects.requireNonNull(mainFirebaseAuth.getUid())).child("Name").getValue(String.class));
//                        Picasso.with(getApplicationContext()).load(dataSnapshot.child(Objects.requireNonNull(mainFirebaseAuth.getUid()))
//                                .child("Image").getValue(String.class)).into(dp);
                    Picasso.with(getApplicationContext()).load(dataSnapshot.child(mainFirebaseAuth.getUid()).child("Image").getValue(String.class))
                            .networkPolicy(NetworkPolicy.OFFLINE).into(dp, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                            String dataSnapshot1 = dataSnapshot.child(mainFirebaseAuth.getUid()).child("Image").getValue(String.class);
                            Picasso.with(getApplicationContext()).load(String.valueOf(dataSnapshot1)).into(dp);
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e) {
        }
    }
    private void settabicons() {
        TabLayout.Tab tab1 = tabLayout.getTabAt(2).setCustomView(R.layout.tablayout);
        v = tab1.getCustomView();
        tv = (TextView) v.findViewById(R.id.tablayout_text);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(data.hasChild(Objects.requireNonNull(mainFirebaseAuth.getUid()))){
                        }
                        else {
                            count++;
                        }
                }
                if(count>0){
                    tv.setText(String.valueOf(count));
                    tv.setVisibility(View.VISIBLE);
                }
                else {
                    tv.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_navhome);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_chrome_reader);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_menu);



        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#803399"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                int position = tab.getPosition();
                if(position==2){
                    ImageView iv = (ImageView) v.findViewById(R.id.tablayout_icon);
                    iv.setImageResource(R.drawable.ic_notificationblue);
                }
                else{
                    tab.getIcon().setColorFilter(Color.parseColor("#803399"), PorterDuff.Mode.SRC_IN);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab1) {
                int pos = tab1.getPosition();
                if(pos == 2){
                    ImageView iv = (ImageView) v.findViewById(R.id.tablayout_icon);
                    iv.setImageResource(R.drawable.ic_notifications);
                }
                else{
                    tab1.getIcon().setColorFilter(Color.parseColor("#595959"), PorterDuff.Mode.SRC_IN);
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_logut:
                mainFirebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.main_menu_refresh:
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main:{
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                break;
            }
            case R.id.admin:{
                startActivity(new Intent(MainActivity.this,AdminActivity.class));
                break;
            }
            case R.id.search:{
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
                break;
            }
            case R.id.about:{
                startActivity(new Intent(MainActivity.this,AboutUsActivity.class));
                break;
            }
            case R.id.svs:{
                startActivity(new Intent(MainActivity.this,SvsceActivity.class));
                break;
            }
            case R.id.contact:{
                startActivity(new Intent(MainActivity.this,ContactActivity.class));
                break;
            }
            case R.id.feedback:{
                startActivity(new Intent(MainActivity.this,FeedbackActivity.class));
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}