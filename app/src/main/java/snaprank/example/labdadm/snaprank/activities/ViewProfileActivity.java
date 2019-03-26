package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ViewProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
    }

    public void close(View view){
        onBackPressed();
    }
}
