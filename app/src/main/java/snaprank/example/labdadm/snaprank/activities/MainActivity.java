package snaprank.example.labdadm.snaprank.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.fragments.HomeFragment;
import snaprank.example.labdadm.snaprank.fragments.ProfileFragment;
import snaprank.example.labdadm.snaprank.fragments.RankingFragment;
import snaprank.example.labdadm.snaprank.fragments.SearchFragment;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FirebaseService firebaseService;
String username;
    TextView header_name;


    private JSONObject userInfo;
boolean goToProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BottomNavigationView) findViewById(R.id.navigation)).setOnNavigationItemSelectedListener(this);


        // Setting custom ActionBar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        // Cambiar el color del ActionBar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xeeeeeeee));

        firebaseService = new FirebaseService(this);

         username = getIntent().getExtras().getString("username");
         goToProfile = getIntent().getExtras().getBoolean("goToProfile");
        if(goToProfile){
            ((BottomNavigationView)findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_profile);

        } else {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if(fragment==null){
                fragment = new HomeFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,fragment).commit();
        }

        try {
            getUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserInfo() throws JSONException {
        userInfo = firebaseService.getCurrentUser();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                findViewById(R.id.navigation_home).setClickable(false);
                findViewById(R.id.navigation_search).setClickable(true);
                findViewById(R.id.navigation_ranking).setClickable(true);
                findViewById(R.id.navigation_profile).setClickable(true);
                findViewById(R.id.custom_bar_add).setVisibility(View.VISIBLE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.VISIBLE);
                findViewById(R.id.logoutButton).setVisibility(View.GONE);
                findViewById(R.id.settingsButton).setVisibility(View.GONE);
                findViewById(R.id.back).setVisibility(View.GONE);
                header_name = findViewById(R.id.custom_bar_name);
                header_name.setText("SnapRank");
                break;
            case R.id.navigation_search:
                fragment = new SearchFragment();
                findViewById(R.id.navigation_home).setClickable(true);
                findViewById(R.id.navigation_search).setClickable(false);
                findViewById(R.id.navigation_ranking).setClickable(true);
                findViewById(R.id.navigation_profile).setClickable(true);
                findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                findViewById(R.id.logoutButton).setVisibility(View.GONE);
                findViewById(R.id.settingsButton).setVisibility(View.GONE);
                findViewById(R.id.back).setVisibility(View.GONE);
                header_name = findViewById(R.id.custom_bar_name);
                header_name.setText(R.string.title_search);
                break;
            case R.id.navigation_ranking:
                fragment = new RankingFragment();
                findViewById(R.id.navigation_home).setClickable(true);
                findViewById(R.id.navigation_search).setClickable(true);
                findViewById(R.id.navigation_ranking).setClickable(false);
                findViewById(R.id.navigation_profile).setClickable(true);
                findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                findViewById(R.id.logoutButton).setVisibility(View.GONE);
                findViewById(R.id.settingsButton).setVisibility(View.GONE);
                header_name = findViewById(R.id.custom_bar_name);
                header_name.setText(R.string.title_ranking);
                break;
            case R.id.navigation_profile:
                if (goToProfile) goToProfile(username);
                else {
                    fragment = new ProfileFragment();
                    username = "";

                    userInfo = firebaseService.getCurrentUser();
                    try {
                        username = userInfo.get("username").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    fragment.setArguments(bundle);


                    findViewById(R.id.navigation_home).setClickable(true);
                    findViewById(R.id.navigation_search).setClickable(true);
                    findViewById(R.id.navigation_ranking).setClickable(true);
                    findViewById(R.id.navigation_profile).setClickable(false);
                    findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                    findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                    findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.settingsButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.back).setVisibility(View.GONE);
                    header_name = findViewById(R.id.custom_bar_name);
                    header_name.setText(R.string.title_profile);
                    break;
                }
        }
        if (fragment != null) getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment).commit();
        return true;
    }

    public void goToProfile(String username){
        Fragment fragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fragment.setArguments(bundle);

        findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
        findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
        findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
        findViewById(R.id.settingsButton).setVisibility(View.VISIBLE);
        findViewById(R.id.navigation_profile).setClickable(false);
        goToProfile=false;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}