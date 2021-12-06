package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import static android.widget.Toast.LENGTH_LONG;

public class drawer extends AppCompatActivity {
    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    private  static  final  int MY_PERMISION_REQUEST=1;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        drawerLayout=findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        frameLayout=findViewById(R.id.framel);
        navigationView=findViewById(R.id.navigationview);
        coordinatorLayout=findViewById(R.id.coordinator);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All songs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitleTextColor(0xffffffff);
        navigationView.setItemIconTintList(null);
        toggle=new ActionBarDrawerToggle(this, drawerLayout , toolbar , R.string. navigation_drawer_open ,
                R.string. navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle) ;
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState() ;
//      loadFragment(new Allsong());
        Allsongopen();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                loadFragment(new Allsong());
                setTitle(item.getTitle());
                int id= item.getItemId();
                switch (id){
                    case R.id.allsong:
                        Allsongopen();

                        break;
                    case R.id.favraout:
                        loadFragment(new fav_song());
                        break;
                    case R.id.setting:
                        loadFragment(new setting());
                        break;


                }
                drawerLayout.closeDrawer(GravityCompat.START);
                toolbar.setTitle(item.getTitle());

                return  true;
            }
        });
        permission();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }



    public void  loadFragment(Fragment fragment){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framel,fragment);
        transaction.commit();

    }
    public void  Allsongopen(){
        Fragment fragment= new Allsong();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framel,fragment,TAG_FRAGMENT);
        transaction.commit();

    }
    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(drawer.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISION_REQUEST);

        }else{
            Toast.makeText(drawer.this,"permissiongranted", LENGTH_LONG).show();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISION_REQUEST){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else {
                ActivityCompat.requestPermissions(drawer.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISION_REQUEST);
            }

        }
    }
    }
