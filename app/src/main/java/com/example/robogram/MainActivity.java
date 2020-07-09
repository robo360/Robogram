package com.example.robogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.robogram.adapters.PostAdapter;
import com.example.robogram.data.model.Post;
import com.example.robogram.fragments.ComposeFragment;
import com.example.robogram.fragments.HomeFragment;
import com.example.robogram.fragments.ProfileFragment;
import com.example.robogram.helpers.EndlessRecyclerViewScrollListener;
import com.example.robogram.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.xml.sax.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.example.robogram.data.model.Post.KEY_USERNAME;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView bottomNavigationView;
    private MenuItem logoutMenuItem;
    private Toolbar toolbar;
    private ImageButton ibCam;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        logoutMenuItem = findViewById(R.id.logout);
        toolbar = findViewById(R.id.toolbar);
        ibCam = findViewById(R.id.ibCam);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //set a listener on the menu for logout
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    ParseUser.logOutInBackground(new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while logging out:" + e);
                                Toast.makeText(MainActivity.this, "Error Logging out. Try again!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                            goToLogin();
                            return;
                        }
                    });

                }
                return true;
            }
        });

        //set a listener on the Bottom Navigation to launch fragments
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Log.i("MainActivity", "Home");
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_compose:
                        Toast.makeText(MainActivity.this, "Compose", Toast.LENGTH_SHORT).show();
                        Log.i("MainActivity", "Compose");
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        Log.i("MainActivity", "Profile");
                        fragment = new ProfileFragment();
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;


            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        //set clicklistener on camera ImageView
        ibCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ComposeFragment();
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });

    }

    private void goToLogin() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}