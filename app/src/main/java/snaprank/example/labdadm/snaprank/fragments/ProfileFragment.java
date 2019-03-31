package snaprank.example.labdadm.snaprank.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    TextView usernameText;

    private FirebaseService firebaseService = new FirebaseService();
    SharedPreferences preferences;
    private String username;
    private JSONObject userInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        usernameText = view.findViewById(R.id.usernameText);
        setUsername();

        imagenSubidaList = new ArrayList<ImagenSubida>();

        ImagenSubida imagenSubida = new ImagenSubida("id", R.drawable.ferran);
        imagenSubidaList.add(imagenSubida);
        for(int i = 0; i < 10; i++){
            ImagenSubida imagenSubida2 = new ImagenSubida("id " + i, R.drawable.taylor);
            imagenSubidaList.add(imagenSubida2);
        }



        imagenSubidaAdapter = new ImagenSubidaAdapter(getContext(), R.layout.profile_grid_item, imagenSubidaList);
        gridView = view.findViewById(R.id.profile_grid);
        gridView.setAdapter(imagenSubidaAdapter);

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

        return view;
    }

    public void gotoLogroActivity(View view){
        Intent intent = new Intent(getContext(), LogrosActivity.class);
        startActivity(intent);
    }

    public void gotoViewPic(View view, int position){
        Intent intent = new Intent(getContext(), ViewPicActivity.class);
        intent.putExtra("imageID", imagenSubidaList.get(position).getImageId());
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

    public void setUsername() {
        userInfo = firebaseService.getCurrentUser();
        try {
            username = userInfo.get("username").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        usernameText.setText(username);
    }

}
