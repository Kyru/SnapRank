package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PopUpFilter extends AppCompatActivity {

    private ListView lv_filterCategory;
    private FilterCategoryAdapter filterCategoryAdapter;
    private List<FilterCategory> filterCategoryList;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.popup_filter);



        initCategoryList();

        filterCategoryAdapter = new FilterCategoryAdapter(this, R.layout.popup_filter_item, filterCategoryList);
        lv_filterCategory = findViewById(R.id.filter_category_list);
        lv_filterCategory.setAdapter(filterCategoryAdapter);
    }

    public void initCategoryList(){
        filterCategoryList = new ArrayList<>();
        filterCategoryList.add(new FilterCategory("Mountain"));
        filterCategoryList.add(new FilterCategory("Sea"));
        filterCategoryList.add(new FilterCategory("Planets and satellites"));
        filterCategoryList.add(new FilterCategory("Friends"));
        filterCategoryList.add(new FilterCategory("Animals"));
        filterCategoryList.add(new FilterCategory("Streets"));
        filterCategoryList.add(new FilterCategory("Vehicles"));
        filterCategoryList.add(new FilterCategory("Food"));
        filterCategoryList.add(new FilterCategory("People"));
    }
}
