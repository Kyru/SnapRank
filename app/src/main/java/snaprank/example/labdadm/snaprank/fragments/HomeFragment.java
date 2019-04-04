package snaprank.example.labdadm.snaprank.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.UploadImageActivity;
import snaprank.example.labdadm.snaprank.activities.ViewPicActivity;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class HomeFragment extends Fragment {

    ImageButton ib_filter;
    ImageButton ib_upload_image;
    ImagenSubida imagenSubida;
    List<ImagenSubida> imagenSubidaList;
    Bitmap bitmap;
    ImageView iv_imagenSubida;

    private FirebaseFirestore firestoreDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;

    ImageButton ib_like;
    ImageButton ib_dislike;
    ImageButton ib_next;

    String category="All";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);

        iv_imagenSubida = view.findViewById(R.id.main_pic);

        ib_like = view.findViewById(R.id.like);
        ib_dislike = view.findViewById(R.id.dislike);
        ib_next = view.findViewById(R.id.next);

        firebaseStorage = FirebaseStorage.getInstance();
        imagenSubidaList = new ArrayList<ImagenSubida>();

        firestoreDatabase = FirebaseFirestore.getInstance();
        storageRef = firebaseStorage.getReference();

        firestoreDatabase.collection("images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imagenSubidaList.add( document.toObject(ImagenSubida.class));
                            }
                            getRandomImage();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomImage();
            }
        });

        iv_imagenSubida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoViewPic(v);
            }
        });


        ib_filter = ((AppCompatActivity)getActivity()).findViewById(R.id.custom_bar_filter);
        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenuFilter(v, ib_filter);
            }
        });


        ib_upload_image = ((AppCompatActivity)getActivity()).findViewById(R.id.custom_bar_add);
        ib_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return view;

    }

    public void gotoViewPic(View view){
        Intent intent = new Intent(getContext(), ViewPicActivity.class);
        intent.putExtra("imageURL",imagenSubida.getUrl());
        startActivity(intent);
    }

    public void showPopUpMenuFilter(View view, ImageButton filter){
        PopupMenu popupMenu = new PopupMenu(getActivity(), filter);
        popupMenu.getMenuInflater().inflate(R.menu.category_filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                category = (String) item.getTitle();
                category = translateCategory(category);
                return true;
            }
        });

        popupMenu.show();
    }

    public void uploadImage() {
        Intent intent = new Intent(getContext(), UploadImageActivity.class);
        startActivity(intent);
    }

    public String translateCategory(String category){
        switch(category){
            case "Montaña": return "Mountain";
            case "Mar": return "Sea";
            case "Planetas y satélites": return "Planets";
            case "Amigos": return "Friends";
            case "Animales": return "Animales";
            case "Calles": return "Streets";
            case "Vehículos": return "Vehicles";
            case "Comida": return "Food";
            case "Gente": return "People";
            case "Música": return "Music";
            case "Festivales": return "Festivals";
            case "Cultura": return "Culture";
            case "Todo": return "All";
            default: return category;
        }
    }

    public void getRandomImage(){

        while(true) {
            Random randomGenerator = new Random();
            int i = randomGenerator.nextInt(imagenSubidaList.size());
            imagenSubida = imagenSubidaList.get(i);
            if (imagenSubida.getCategory().equals(category) || category.equals("All")) break;
        }

        final StorageReference imageRef = storageRef.child(imagenSubida.getUrl());

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv_imagenSubida.setImageBitmap(Bitmap.createScaledBitmap(bitmap, iv_imagenSubida.getWidth(),
                        iv_imagenSubida.getHeight(), false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
