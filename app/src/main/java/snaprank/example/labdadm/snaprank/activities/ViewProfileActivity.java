package snaprank.example.labdadm.snaprank.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.fragments.ProfileFragment;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new ProfileFragment();
    }

    public void close(View view){
        onBackPressed();
    }
}