package snaprank.example.labdadm.snaprank.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


import snaprank.example.labdadm.snaprank.activities.ViewPicActivity;
import snaprank.example.labdadm.snaprank.adapters.ImagenSubidaAdapter;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

import java.util.ArrayList;
import java.util.List;

public class PhotoRankingFragment extends Fragment {

    GridView gridView;
    ImageView primero;
    ImageView segundo;
    ImageView tercero;
    ImagenSubidaAdapter imagenSubidaAdapter;
    List<ImagenSubida> imagenSubidaList;

    private FirebaseService firebaseService = new FirebaseService();
    SharedPreferences preferences;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestoreDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_ranking,container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        imagenSubidaList = new ArrayList<ImagenSubida>();

        firebaseStorage = FirebaseStorage.getInstance();

        firestoreDatabase = FirebaseFirestore.getInstance();

        firestoreDatabase.collection("images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imagenSubidaList.add( document.toObject(ImagenSubida.class));
                            }
                            initializeGridAdapter();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        primero = view.findViewById(R.id.imageFirst);
        segundo = view.findViewById(R.id.imageSecond);
        tercero = view.findViewById(R.id.imageThird);

        gridView = view.findViewById(R.id.photoRanking_grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoViewPic(view, position);
            }
        });

        return view;
    }

    public void initializeGridAdapter(){
        imagenSubidaAdapter = new ImagenSubidaAdapter(getContext(), R.layout.profile_grid_item, imagenSubidaList, firebaseStorage);
        gridView.setAdapter(imagenSubidaAdapter);
    }

    public void gotoViewPic(View view, int position){
        Intent intent = new Intent(getContext(), ViewPicActivity.class);
        intent.putExtra("imageURL", imagenSubidaList.get(position).getUrl());
        startActivity(intent);
    }
}