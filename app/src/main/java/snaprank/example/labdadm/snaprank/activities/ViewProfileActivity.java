package snaprank.example.labdadm.snaprank.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ViewProfileActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
    }

    public void close(View view){
        onBackPressed();
    }
}