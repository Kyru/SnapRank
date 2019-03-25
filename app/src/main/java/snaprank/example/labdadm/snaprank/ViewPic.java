package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class ViewPic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pic);
        getSupportActionBar().hide();

    }

    public void close(View view){
        onBackPressed();
    }
}
