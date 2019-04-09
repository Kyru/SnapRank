package snaprank.example.labdadm.snaprank.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import snaprank.example.labdadm.snaprank.activities.SettingsActivity;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.adapters.ImagenSubidaAdapter;
import snaprank.example.labdadm.snaprank.activities.LoginActivity;
import snaprank.example.labdadm.snaprank.activities.LogrosActivity;
import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.ViewPicActivity;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class ProfileFragment extends Fragment {

    GridView gridView;
    ImagenSubidaAdapter imagenSubidaAdapter;
    List<ImagenSubida> imagenSubidaList;
    ImageButton bt_logros;
    ImageButton bt_logout;
    ImageButton bt_settings;
    TextView usernameText;

    private FirebaseService firebaseService = new FirebaseService(getContext());
    SharedPreferences preferences;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestoreDatabase;

    private String username;
    private JSONObject userInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        imagenSubidaList = new ArrayList<ImagenSubida>();
        usernameText = view.findViewById(R.id.usernameText);
        username = getArguments().getString("username");
        Log.d("username", username);
        usernameText.setText(username);

        firebaseStorage = FirebaseStorage.getInstance();

        firestoreDatabase = FirebaseFirestore.getInstance();

        firestoreDatabase.collection("images")
                .whereEqualTo("username", username)
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

        gridView = view.findViewById(R.id.profile_grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoViewPic(view, position);
            }
        });


        bt_logros = view.findViewById(R.id.ib_profile_logros);
        bt_logros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogroActivity(v);
            }
        });

        bt_logout = ((AppCompatActivity) Objects.requireNonNull(getActivity())).findViewById(R.id.logoutButton);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        bt_settings = ((AppCompatActivity) Objects.requireNonNull(getActivity())).findViewById(R.id.settingsButton);
        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void initializeGridAdapter(){
        imagenSubidaAdapter = new ImagenSubidaAdapter(getContext(), R.layout.profile_grid_item, imagenSubidaList, firebaseStorage);
        gridView.setAdapter(imagenSubidaAdapter);
    }

    public void gotoLogroActivity(View view){
        Intent intent = new Intent(getContext(), LogrosActivity.class);
        startActivity(intent);
    }

    public void gotoViewPic(View view, int position){
        Intent intent = new Intent(getContext(), ViewPicActivity.class);
        intent.putExtra("imageURL", imagenSubidaList.get(position).getUrl());
        startActivity(intent);
    }

    public void logout() {
        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        logoutDialog.setMessage(R.string.logout_message);

        logoutDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseService.logout();
                preferences.edit().putBoolean("loggedIn", false).apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        logoutDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        logoutDialog.create().show();
    }


}
