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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import snaprank.example.labdadm.snaprank.adapters.CategoryAdapter;
import snaprank.example.labdadm.snaprank.adapters.CategoryData;
import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.Usuario;


public class SearchFragment extends Fragment  {
    ListView listview;
    ArrayList<Usuario> categories = new ArrayList<>(), categories2=new ArrayList<>();
    ArrayList<View> items=new ArrayList<>();
    SearchView search;
    CategoryAdapter myAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_search, null);
        search=(SearchView) v.findViewById(R.id.searchView);
        listview = (ListView) v.findViewById(R.id.listviewcat);
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"Adrian","Valencia",R.drawable.ic_profile));
        categories.add(new Usuario(1,"rx8","Valencia",R.drawable.ic_profile));
        myAdapter=new CategoryAdapter(getContext(),R.layout.fragment_search, categories);

        listview.setAdapter(myAdapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               // myAdapter.clear();
                //myAdapter.addAll(categories);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Usuario> temp = new ArrayList<Usuario>();
                int textlength = newText.length();
                temp.clear();
                for (int i = 0; i < categories.size(); i++)
                {
                    if (textlength <= categories.get(i).getNombre().length())
                    {
                        if(newText.equalsIgnoreCase(
                                (String)
                                        categories.get(i).getNombre().subSequence(0,
                                                textlength)))
                        {
                            temp.add(categories.get(i));
                        }
                    }
                }
                listview.setAdapter(new CategoryAdapter(getContext(),R.layout.fragment_search, temp));
                return false;


            }
        });



        return v;

    }


}