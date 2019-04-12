package snaprank.example.labdadm.snaprank.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.adapters.LogroAdapter;
import snaprank.example.labdadm.snaprank.models.Logro;

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


        Logro logro = new Logro("Animals", "Marzo 2019", R.drawable.gold_trophy);
        logroList.add(logro);
        Logro logro1 = new Logro("Mountain", "Agosto 2019", R.drawable.silver_trophy);
        logroList.add(logro1);
        Logro logro2 = new Logro("Animals", "Diciembre 2017", R.drawable.bronze_trophy);
        logroList.add(logro2);


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
