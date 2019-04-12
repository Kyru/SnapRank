package snaprank.example.labdadm.snaprank.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.firestore.SetOptions;
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
import snaprank.example.labdadm.snaprank.models.Usuario;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class HomeFragment extends Fragment {

    ImageButton ib_filter;
    ImageButton ib_upload_image;
    ImagenSubida imagenSubida;
    List<ImagenSubida> imagenSubidaList;
    Bitmap bitmap;
    ImageView iv_imagenSubida;
    String username_pic;
    Usuario usuario_pic;

    private FirebaseService firebaseService = new FirebaseService(getContext());
    SharedPreferences preferences;
    private FirebaseDatabase database;
    private DatabaseReference dbref_img;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef;
    private FirebaseFirestore firestoreDatabase;

    ImageButton ib_like;
    ImageButton ib_dislike;
    ImageButton ib_next;

    private String username;
    private JSONObject userInfo;

    Handler handler;

    String category="All";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);

        handler = new Handler();

        iv_imagenSubida = view.findViewById(R.id.main_pic);

        ib_like = view.findViewById(R.id.like);
        ib_dislike = view.findViewById(R.id.dislike);
        ib_next = view.findViewById(R.id.next);

        firebaseStorage = FirebaseStorage.getInstance();
        imagenSubidaList = new ArrayList<>();

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
                            if(imagenSubidaList.size() == 0){
                                disableButtons();
                            } else {
                                enableButtons();
                                getRandomImage();
                            }
                        } else {
                        }
                    }
                });


        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomImage();
            }
        });

        ib_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenSubida.setLikes(imagenSubida.getLikes()+1);
                usuario_pic.setScore(usuario_pic.getScore()+1);
                updateCurrentImage();
            }
        });

        ib_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenSubida.setDislikes(imagenSubida.getDislikes()+1);
                usuario_pic.setScore(usuario_pic.getScore()-1);
                updateCurrentImage();
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

    public void disableButtons(){
        ib_next.setClickable(false);
        ib_dislike.setClickable(false);
        ib_like.setClickable(false);
    }

    public void enableButtons(){
        ib_next.setClickable(true);
        ib_dislike.setClickable(true);
        ib_like.setClickable(true);
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
                getCategoryImage();
                return true;
            }
        });

        popupMenu.show();
    }

    public void uploadImage() {
        Intent intent = new Intent(getContext(), UploadImageActivity.class);
        startActivity(intent);
    }

    public void setUsername() {
        userInfo = firebaseService.getCurrentUser();
        try {
            username = userInfo.get("username").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void getCategoryImage(){
        for(int i = 0; i < imagenSubidaList.size(); i++){
            if(imagenSubidaList.get(i).getCategory() == category){
                getRandomImage();
            }
        }

        String categoryString = getResources().getString(R.string.category);
        String categoryMessge = getResources().getString(R.string.no_photos_in_category);

        Toast.makeText(getActivity(),categoryString + " " + category + " " + categoryMessge, Toast.LENGTH_SHORT).show();
    }


    public void getRandomImage(){

        while(true) {
            Random randomGenerator = new Random();
            int i = randomGenerator.nextInt(imagenSubidaList.size());
            imagenSubida = imagenSubidaList.get(i);
            username_pic = imagenSubida.getUsername();
            if (imagenSubida.getCategory().equals(category) || category.equals("All")) break;
        }

        firestoreDatabase.collection("users")
                .whereEqualTo("username", username_pic)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usuario_pic = document.toObject(Usuario.class);
                                break;
                            }
                        }
                    }
                });

        StorageReference imageRef = storageRef.child(imagenSubida.getUrl());

        final long ONE_MEGABYTE = 2048 * 2048;
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

    public void updateCurrentImage(){

        firestoreDatabase.collection("images").document(imagenSubida.getId())
                .set(imagenSubida, SetOptions.merge());


        firestoreDatabase.collection("users").document(usuario_pic.getId())
                .set(usuario_pic, SetOptions.merge());


        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             getRandomImage();
                         }
        });

    }
}
