package snaprank.example.labdadm.snaprank.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.fragments.HomeFragment;
import snaprank.example.labdadm.snaprank.fragments.ProfileFragment;
import snaprank.example.labdadm.snaprank.fragments.RankingFragment;
import snaprank.example.labdadm.snaprank.fragments.SearchFragment;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    FirebaseService firebaseService = new FirebaseService();

    private JSONObject userInfo;

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


        String username = getIntent().getExtras().getString("username");
        boolean goToProfile = getIntent().getExtras().getBoolean("goToProfile");
        if(goToProfile){
            goToProfile(username);
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
        Log.d("User info", userInfo.get("username").toString());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                findViewById(R.id.custom_bar_add).setVisibility(View.VISIBLE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.VISIBLE);
                findViewById(R.id.logoutButton).setVisibility(View.GONE);
                findViewById(R.id.back).setVisibility(View.GONE);
                break;
            case R.id.navigation_search:
                fragment = new SearchFragment();
                findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                findViewById(R.id.logoutButton).setVisibility(View.GONE);
                findViewById(R.id.back).setVisibility(View.GONE);
                break;
            case R.id.navigation_ranking:
                fragment = new RankingFragment();
                findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                findViewById(R.id.logoutButton).setVisibility(View.GONE);
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                String username = "";

                userInfo = firebaseService.getCurrentUser();
                try {
                    username = userInfo.get("username").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                fragment.setArguments(bundle);

                findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
                findViewById(R.id.back).setVisibility(View.GONE);
                break;
        }

        if(fragment!=null) getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();
    }
}