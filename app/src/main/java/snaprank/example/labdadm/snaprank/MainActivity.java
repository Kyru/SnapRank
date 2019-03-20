package snaprank.example.labdadm.snaprank;

import android.drm.DrmStore;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


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
            fragment = new HomeFeedFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFeedFragment();
                findViewById(R.id.custom_bar_add).setVisibility(View.VISIBLE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.VISIBLE);
                break;
            case R.id.navigation_search:
                findViewById(R.id.custom_bar_add).setVisibility(View.INVISIBLE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.INVISIBLE);
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                findViewById(R.id.custom_bar_add).setVisibility(View.INVISIBLE);
                findViewById(R.id.custom_bar_filter).setVisibility(View.INVISIBLE);
                break;
        }
        if(fragment!=null) getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();
        return true;
    }
}
