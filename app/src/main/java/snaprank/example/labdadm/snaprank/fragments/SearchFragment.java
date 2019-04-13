package snaprank.example.labdadm.snaprank.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import snaprank.example.labdadm.snaprank.adapters.CategoryAdapter;
import snaprank.example.labdadm.snaprank.adapters.CategoryData;
import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.models.Usuario;


public class SearchFragment extends Fragment {
    ListView listview;
    ArrayList<Usuario> categories = new ArrayList<>(), categories2 = new ArrayList<>();
    ArrayList<View> items = new ArrayList<>();
    SearchView search;
    CategoryAdapter myAdapter;
    FirebaseFirestore firestoreDatabase;
    FirebaseStorage firebaseStorage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, null);
        search = (SearchView) v.findViewById(R.id.searchView);
        listview = (ListView) v.findViewById(R.id.listviewcat);

        firebaseStorage = FirebaseStorage.getInstance();
        firestoreDatabase = FirebaseFirestore.getInstance();

        firestoreDatabase.collection("users").orderBy("score")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Usuario> temp = new ArrayList<Usuario>();
                            temp.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                temp.add(document.toObject(Usuario.class));
                            }
                            listview.setAdapter(new CategoryAdapter(getContext(), R.layout.fragment_search, temp, firebaseStorage));
                            categories = temp;
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        myAdapter = new CategoryAdapter(getContext(), R.layout.fragment_search, categories, firebaseStorage);

        listview.setAdapter(myAdapter);
        listview.setScrollBarSize(10);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Usuario> temp = new ArrayList<Usuario>();
                int textlength = newText.length();
                temp.clear();
                for (int i = 0; i < categories.size(); i++) {
                    if (textlength <= categories.get(i).getUsername().length()) {
                        if (newText.equalsIgnoreCase((String) categories.get(i).getUsername().subSequence(0, textlength))) {
                            temp.add(categories.get(i));
                        }
                    }
                }
                listview.setAdapter(new CategoryAdapter(getContext(), R.layout.fragment_search, temp, firebaseStorage));
                return true;
            }
        });

        return v;

    }


}