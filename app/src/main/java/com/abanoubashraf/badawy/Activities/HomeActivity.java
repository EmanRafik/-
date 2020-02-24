package com.abanoubashraf.badawy.Activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abanoubashraf.badawy.BedouinHeritage.BedouinHeritageFragment;
import com.abanoubashraf.badawy.ChooseSpecialists.ChooseSpecialistFragment;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.Community.CommunityFragmentNew;
import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.Questions.AskABedouinFragment;
import com.abanoubashraf.badawy.R;
import com.abanoubashraf.badawy.Settings.SettingsFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    private FrameLayout main_frame;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AskABedouinFragment askABedouinFragment;
    private BedouinHeritageFragment bedouinHeritageFragment;
    private CommunityFragmentNew communityFragmentNew;
    private ChooseSpecialistFragment chooseSpecialistFragment;
    private SettingsFragment settingsFragment;
    private Menu menu;
    private String user_type;
    private ImageView imageView_profile_header;
    private TextView textView_username_header, textView_tribe_header;
    private LinearLayout nav_header_layout;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        main_frame = findViewById(R.id.main_frame);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menu = navigationView.getMenu();
        //menu fragments
        askABedouinFragment = new AskABedouinFragment();
        bedouinHeritageFragment = new BedouinHeritageFragment();
        chooseSpecialistFragment = new ChooseSpecialistFragment();
        settingsFragment = new SettingsFragment();
        communityFragmentNew = new CommunityFragmentNew();

        //header profile
        View headerView = navigationView.getHeaderView(0);
        imageView_profile_header = headerView.findViewById(R.id.imageView_profile_header);
        textView_username_header = headerView.findViewById(R.id.textView_username_header);
        textView_tribe_header = headerView.findViewById(R.id.textView_tribe_header);
        nav_header_layout = headerView.findViewById(R.id.nav_header_layout);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(SharedHelper.getSharedHelper(getApplicationContext()).getCurrentUser().getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                textView_username_header.setText(user.getUsername());

                //setting user profile picture
                String image_URI = user.getImage_URL();
                if (image_URI.equals("default")) {
                    imageView_profile_header.setImageResource(R.drawable.default_pp);
                } else {
                    Glide.with(getApplicationContext()).load(image_URI).into(imageView_profile_header);
                }
                if (user.getType().equals("b")) {
                    textView_tribe_header.setText(user.getTribe());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //showing menu items according to user type
        user_type = SharedHelper.getSharedHelper(getApplicationContext()).getCurrentUser().getType();
        switch (user_type) {
            case "b": {
                menu.findItem(R.id.nav_community).setVisible(true);
                menu.findItem(R.id.nav_questions).setVisible(true);
                menu.findItem(R.id.nav_choose_specialist).setVisible(true);
                setFragment(communityFragmentNew);
                break;
            }
            case "nb": {
                textView_tribe_header.setVisibility(View.GONE);
                menu.findItem(R.id.nav_ask_a_bedouin).setVisible(true);
                menu.findItem(R.id.nav_bedouin_heritage).setVisible(true);
                setFragment(askABedouinFragment);
                break;
            }
        }

        //toggle button
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(user_type.equals("b")){
            getSupportActionBar().setTitle(R.string.community_option);
        }
        else{
            getSupportActionBar().setTitle(R.string.ask_a_bedouin_option);
        }
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {getResources().getColor(R.color.colorPrimaryDark),getResources().getColor(R.color.colorPrimaryDark)});
        gd.setCornerRadius(0f);
        getSupportActionBar().setBackgroundDrawable(gd);
        // menu item listener
        navigationView.setNavigationItemSelectedListener(this);

        Log.i("language", Locale.getDefault().getDisplayLanguage());
        if(Locale.getDefault().getDisplayLanguage().equals("العربية")){
            navigationView.setBackground(ContextCompat.getDrawable(this, R.drawable.menu_body_style_ar));
            nav_header_layout.setBackground(ContextCompat.getDrawable(this, R.drawable.menu_header_style_ar));
        }
    }
    // function to change fragments inside frameLayout
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment, "my_fragment");
        fragmentTransaction.commit();
    }
    public void refresh(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment frg = getSupportFragmentManager().findFragmentByTag("my_fragment");
        if(frg != null){
            fragmentTransaction.detach(frg);
            fragmentTransaction.attach(frg);
            fragmentTransaction.commit();
        }
    }
    //menu items transition
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_ask_a_bedouin){
            setFragment(askABedouinFragment);
            getSupportActionBar().setTitle(R.string.ask_a_bedouin_option);
        }
        else if(id == R.id.nav_bedouin_heritage){
            setFragment(bedouinHeritageFragment);
            getSupportActionBar().setTitle(R.string.bedouin_heritage_option);
        }
        else if(id == R.id.nav_community){
            setFragment(communityFragmentNew);
            getSupportActionBar().setTitle(R.string.community_option);
        }
        else if(id == R.id.nav_questions){
            setFragment(askABedouinFragment);
            getSupportActionBar().setTitle(R.string.questions_option);
        }
        else if (id == R.id.nav_choose_specialist) {
            setFragment(chooseSpecialistFragment);
            getSupportActionBar().setTitle(R.string.choose_specialists_option);
        } else if (id == R.id.nav_settings) {
            setFragment(settingsFragment);
            getSupportActionBar().setTitle(R.string.settings_option);
        }
        drawerLayout.closeDrawers();
        return false;
    }
    //toggle switch action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
