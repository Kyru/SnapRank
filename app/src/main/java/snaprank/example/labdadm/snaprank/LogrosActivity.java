package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LogrosActivity extends AppCompatActivity {

    public List<Logro> logroList;
    public LogroAdapter logroAdapter;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_logros);
        getSupportActionBar().hide();

        logroList = new ArrayList<Logro>();

        for(int i = 0; i < 10; i++){
            Logro logro = new Logro("Animals", "Marzo 2019", R.drawable.like);
            logroList.add(logro);
        }

        logroAdapter = new LogroAdapter(this, logroList);
        recyclerView = findViewById(R.id.rv_profile_logros);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(logroAdapter);

    }
}
