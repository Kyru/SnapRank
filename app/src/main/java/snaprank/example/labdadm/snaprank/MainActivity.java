package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private ListView lv;
    public static ArrayList<Model> modelArrayList;
    private CustomAdapter customAdapter;
    private String[] categorylist = new String[]{"Apples", "Oranges", "Potatoes", "Tomatoes","Grapes"};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);

        modelArrayList = getModel();
        customAdapter = new CustomAdapter(this);
        lv.setAdapter(customAdapter);
    }

    private ArrayList<Model> getModel(){
        ArrayList<Model> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){

            Model model = new Model();
            model.setCategory(categorylist[i]);
            list.add(model);
        }
        return list;
    }

}
