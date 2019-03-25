package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LogrosActivity extends AppCompatActivity {

    public List<Logro> logroList;
    public LogroAdapter logroAdapter;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_logros);
        getSupportActionBar().hide();

        logroList = new ArrayList<Logro>();

        for(int i = 0; i < 3; i++){
            Logro logro = new Logro("Animals", "Marzo 2019", R.drawable.gold_trophy);
            logroList.add(logro);
            Logro logro1 = new Logro("Animals", "Marzo 2019", R.drawable.silver_trophy);
            logroList.add(logro1);
            Logro logro2 = new Logro("Animals", "Marzo 2019", R.drawable.bronze_trophy);
            logroList.add(logro2);
        }

        logroAdapter = new LogroAdapter(this, logroList);
        recyclerView = findViewById(R.id.rv_profile_logros);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(logroAdapter);

    }

    public void close(View view){
        onBackPressed();
    }
}
