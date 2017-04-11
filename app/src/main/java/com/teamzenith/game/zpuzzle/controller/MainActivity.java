package com.teamzenith.game.zpuzzle.controller;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.teamzenith.game.zpuzzle.R;
import com.teamzenith.game.zpuzzle.dbhandler.GetUserInformation;
import com.teamzenith.game.zpuzzle.model.Level;
import com.teamzenith.game.zpuzzle.model.LevelFactory;
import com.teamzenith.game.zpuzzle.model.LevelType;
import com.teamzenith.game.zpuzzle.model.User;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, GetUserInformation {
    boolean doubleBackToExitPressedOnce = false;
    private Button hardBtn;
    private Button medelBtn;
    private Button kidsBtn;
    private FirebaseAuth firebaseAuth;
    private String userEmail;
    private String userImage;
    private String userID;
    private String userName;
    private User player;
    private String facebook_id, f_name, m_name, l_name, gender, full_name, email_id;
    private String profile_image;
    private User user;
    private Profile profile;
    private ProfileController profileController;
    private FirebaseUser firebaseUser;
    private TextView userNameView, userEmailView;
    private NavigationView navigationView;
    private ImageView userImageView;
    private View headerView;
    private DrawerLayout drawer;

    private boolean faceBookLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            faceBookLogin = true;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = new User();
        Intent mIntent = getIntent();
        FacebookSdk.sdkInitialize(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        player = (User) mIntent.getSerializableExtra("player");
        createComponents();
        Actions();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        userImage = player.getUserImage();
        Picasso.with(getBaseContext()).load(userImage).into(userImageView);
    }

    private void createComponents() {
        hardBtn = (Button) findViewById(R.id.hardBtn);
        medelBtn = (Button) findViewById(R.id.medelBtn);
        kidsBtn = (Button) findViewById(R.id.kidsBtn);
        profileController = new ProfileController();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        userNameView = (TextView) headerView.findViewById(R.id.user_name);
        userEmailView = (TextView) headerView.findViewById(R.id.user_email_header);
        userImageView = (ImageView) headerView.findViewById(R.id.user_image);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void Actions() {
        hardBtn.setOnClickListener(this);
        medelBtn.setOnClickListener(this);
        kidsBtn.setOnClickListener(this);
        profileController.setToController(this, player);



    }


    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        Level level=null;
        LevelFactory levelFactory = LevelFactory.getInstance();
        if (btn.getId() == R.id.hardBtn) {
            level = levelFactory.createLevel(LevelType.HARD);
        } else if (btn.getId() == R.id.medelBtn) {
            level = levelFactory.createLevel(LevelType.MEDIUM);
        } else {
            level = levelFactory.createLevel(LevelType.EASY);
        }
        Intent intent = new Intent(this, ImageChooser.class);
        intent.putExtra("player", player);
        intent.putExtra("Level", level);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            i.putExtra("faceBookLogin", faceBookLogin);
            i.putExtra("player", player);
            startActivity(i);
        } else if (id == R.id.nav_history) {
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            i.putExtra("player", player);
            startActivity(i);
        } else if (id == R.id.nav_send_Invitation) {
            Intent i = new Intent(MainActivity.this, SendInvitationActivity.class);
            i.putExtra("player", player);
            startActivity(i);
        } else if (id == R.id.nav_sent_Invitation) {
            Intent i = new Intent(MainActivity.this, SentInvitationActivity.class);
            i.putExtra("player", player);
            startActivity(i);
        } else if (id == R.id.nav_receive_Invitation) {
            Intent i = new Intent(MainActivity.this, InboxActivity.class);
            i.putExtra("player", player);
            startActivity(i);
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);

            finish();

        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
/*
        else if(id==R.id.nav_top_games){
            Intent i = new Intent(MainActivity.this, TopGamesActivity.class);
            startActivity(i);
        }*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void get(User user) {
        userNameView.setText(user.getUserName());
        userEmailView.setText(user.getUserEmail());
        userImage = user.getUserImage();
        Picasso.with(getBaseContext()).load(userImage).into(userImageView);
    }
}
