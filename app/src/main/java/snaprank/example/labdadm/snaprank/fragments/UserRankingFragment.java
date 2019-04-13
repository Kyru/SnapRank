package snaprank.example.labdadm.snaprank.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import snaprank.example.labdadm.snaprank.adapters.RankingAdapter;
import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.Usuario;

import java.util.ArrayList;

public class UserRankingFragment extends Fragment {
    ListView listview;
    ArrayList<Usuario> usuariosRankingList;
    FirebaseFirestore firestoreDatabase;
    FirebaseStorage firebaseStorage;
    ImageButton ib_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_ranking, null);

        listview = v.findViewById(R.id.user_ranking_list);
        usuariosRankingList = new ArrayList<Usuario>();


        firebaseStorage = FirebaseStorage.getInstance();
        firestoreDatabase = FirebaseFirestore.getInstance();

        firestoreDatabase.collection("users").orderBy("score", Query.Direction.DESCENDING).limit(50)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            usuariosRankingList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usuariosRankingList.add(document.toObject(Usuario.class));
                            }
                            listview.setAdapter(new RankingAdapter(getContext(), R.layout.fragment_user_ranking, usuariosRankingList, firebaseStorage));
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        ib_back = ((AppCompatActivity)getActivity()).findViewById(R.id.back);
        ib_back.setVisibility(View.VISIBLE);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBack(v);
            }
        });

        return v;
    }

    public void returnBack(View view){
        Fragment fragment = new RankingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}