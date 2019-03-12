package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((BottomNavigationView) findViewById(R.id.navigation)).setOnNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                if(fragment==null){
                    fragment = new HomeFeedFragment();
                }
                break;
            case R.id.navigation_search:
                break;
            case R.id.navigation_profile:
                break;
        }
        if(fragment!=null) getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,fragment).commit();
        return true;
    }
}
