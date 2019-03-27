package snaprank.example.labdadm.snaprank.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.fragments.HomeFragment;
import snaprank.example.labdadm.snaprank.fragments.ProfileFragment;
import snaprank.example.labdadm.snaprank.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth auth;

    SharedPreferences preferences;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BottomNavigationView) findViewById(R.id.navigation)).setOnNavigationItemSelectedListener(this);

        // Setting custom ActionBar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment==null){
            fragment = new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();

        auth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString("username", "");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Profile updated
                        }
                    }
                });

        /*updateUI(currentUser);*/
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
                break;
            case R.id.navigation_search:
                fragment = new SearchFragment();
                findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                findViewById(R.id.logoutButton).setVisibility(View.GONE);
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
                findViewById(R.id.logoutButton).setVisibility(View.VISIBLE);
                break;
        }
        if(fragment!=null) getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();
        return true;
    }
}
