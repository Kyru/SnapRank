package snaprank.example.labdadm.snaprank.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import snaprank.example.labdadm.snaprank.R;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
    }

    public void close(View view){
        onBackPressed();
    }
}
