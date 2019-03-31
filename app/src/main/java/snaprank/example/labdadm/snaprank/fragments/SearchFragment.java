package snaprank.example.labdadm.snaprank.fragments;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import snaprank.example.labdadm.snaprank.adapters.CategoryAdapter;
import snaprank.example.labdadm.snaprank.adapters.CategoryData;
import snaprank.example.labdadm.snaprank.R;


public class SearchFragment extends Fragment  {
    ListView listview;
    ArrayList<CategoryData> categories = new ArrayList<>();
    ArrayList<View> items=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_search, null);
        listview = (ListView) v.findViewById(R.id.listviewcat);
        categories.add(new CategoryData(1,"Lion",R.drawable.ic_profile));
        categories.add(new CategoryData(1,"Lion",R.drawable.ic_profile));
        categories.add(new CategoryData(1,"Lion",R.drawable.ic_profile));
        categories.add(new CategoryData(1,"Lion", R.drawable.ic_profile));

        CategoryAdapter myAdapter=new CategoryAdapter(getContext(),R.layout.fragment_search, categories);
        listview.setAdapter(myAdapter);


        return v;

    }

}