package snaprank.example.labdadm.snaprank.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.ViewPicActivity;
import snaprank.example.labdadm.snaprank.adapters.ImagenSubidaAdapter;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;

public class PhotoRankingFragment extends Fragment  {

    GridView gridView;
    ImagenSubidaAdapter imagenSubidaAdapter;
    List<ImagenSubida> imagenRankingList;
    List<ImagenSubida> imagenesGrid;
    String imageURL;

    Bitmap bitmap;
    ImageView primero;
    ImageView segundo;
    ImageView tercero;

    Handler handler;

    ImageButton ib_filter;
    ImageButton ib_back;
    String category="All";
    int counter;

    View view;
    final long ONE_MEGABYTE = 2048 * 2048;

    SharedPreferences preferences;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firestoreDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_photo_ranking,container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        imagenRankingList = new ArrayList<ImagenSubida>();

        ib_filter = ((AppCompatActivity)getActivity()).findViewById(R.id.custom_bar_filter);
        ib_filter.setVisibility(View.INVISIBLE);
        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenuFilter(v, ib_filter);
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

        fillRanking();

        return view;
    }

    public void orderList(List<ImagenSubida> list) {
        Collections.sort(list, new Comparator<ImagenSubida>() {
            @Override
            public int compare(ImagenSubida i1, ImagenSubida i2) {
                int pointsi1 = i1.getLikes() - i1.getDislikes();
                int pointsi2 = i2.getLikes() - i2.getDislikes();

                return new Integer(pointsi2).compareTo(new Integer(pointsi1));
            }
        });
    }

    public void initializePodium(){
        StorageReference storageRef = firebaseStorage.getReference();

        imageURL = imagenRankingList.get(0).getUrl();
        StorageReference imageRef = storageRef.child(imageURL);


        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                primero.setImageBitmap(Bitmap.createScaledBitmap(bitmap, primero.getWidth(),
                        primero.getHeight(), false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        imageURL = imagenRankingList.get(1).getUrl();
        imageRef = storageRef.child(imageURL);

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                segundo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, segundo.getWidth(),
                        segundo.getHeight(), false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        imageURL = imagenRankingList.get(2).getUrl();
        imageRef = storageRef.child(imageURL);

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                tercero.setImageBitmap(Bitmap.createScaledBitmap(bitmap, tercero.getWidth(),
                        tercero.getHeight(), false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void initializeGridAdapter(){

        imagenesGrid = imagenRankingList.subList(3, imagenRankingList.size());
        imagenSubidaAdapter = new ImagenSubidaAdapter(getContext(), R.layout.profile_grid_item, imagenesGrid, firebaseStorage);
        gridView.setAdapter(imagenSubidaAdapter);

        ib_filter.postDelayed(new Runnable() {
            public void run() {
                ib_filter.setVisibility(View.VISIBLE);
            }
        }, 5000);

    }

    public void gotoViewPic(View view, int position){
        Intent intent = new Intent(getContext(), ViewPicActivity.class);
        intent.putExtra("imageURL", imagenRankingList.get(position).getUrl());
        startActivity(intent);
    }

    public void showPopUpMenuFilter(final View view, ImageButton filter){
        PopupMenu popupMenu = new PopupMenu(getActivity(), filter);
        popupMenu.getMenuInflater().inflate(R.menu.category_filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String clickaste = getResources().getString(R.string.do_click);
                Toast.makeText(getActivity(), clickaste + item.getTitle(), Toast.LENGTH_SHORT).show();
                ib_filter.setVisibility(View.INVISIBLE);
                category = (String) item.getTitle();
                category = translateCategory(category);
                fillRanking();
                return true;
            }
        });

        popupMenu.show();
    }

    public String translateCategory(String category){
        switch(category){
            case "Montaña": return "Mountain";
            case "Mar": return "Sea";
            case "Planetas y satélites": return "Planets";
            case "Amigos": return "Friends";
            case "Animales": return "Animals";
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

    public void fillRanking(){

        firebaseStorage = FirebaseStorage.getInstance();
        firestoreDatabase = FirebaseFirestore.getInstance();

        primero = view.findViewById(R.id.imageFirst);
        segundo = view.findViewById(R.id.imageSecond);
        tercero = view.findViewById(R.id.imageThird);
        gridView = view.findViewById(R.id.photoRanking_grid);
        imagenRankingList.clear();
        counter=0;

        firestoreDatabase.collection("images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (category.equals(document.toObject(ImagenSubida.class).getCategory()) || category.equals("All")) {
                                    imagenRankingList.add(document.toObject(ImagenSubida.class));
                                    counter++;
                                    if(counter>22) break;
                                }
                            }
                            if(imagenRankingList.size()<3){
                                String categoryString = getResources().getString(R.string.category);
                                String categoryMessage = getResources().getString(R.string.no_enough_photos_in_category);

                                Toast.makeText(getActivity(),categoryString + " " + category + " " + categoryMessage, Toast.LENGTH_SHORT).show();
                                category="All";
                                fillRanking();
                            }
                            else{
                                orderList(imagenRankingList);
                                initializePodium();
                                initializeGridAdapter();
                                primero.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gotoViewPic(v, 0);
                                    }
                                });

                                segundo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gotoViewPic(v, 1);
                                    }
                                });
                                tercero.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gotoViewPic(v, 2);
                                    }
                                });
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        gotoViewPic(view, (position + 3));
                                    }
                                });
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void returnBack(View view){
        Fragment fragment = new RankingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}