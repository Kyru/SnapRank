package snaprank.example.labdadm.snaprank.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class ViewPicActivity extends AppCompatActivity {

    ImageView iv_imagenSubida;
    String imageURL;
    Bitmap bitmap;
    TextView tv_username_info;
    TextView tv_category_info;
    TextView tv_location_info;
    TextView tv_descripcion_info;
    ImagenSubida imagenSubida;


    private FirebaseDatabase database;
    private DatabaseReference dbref_img;
    private FirebaseStorage firebaseStorage;
    private FirebaseService firebaseService;
    private FirebaseFirestore firestoreDatabase;
    String username;
    private JSONObject userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpic);
        getSupportActionBar().hide();

        firebaseStorage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        firestoreDatabase = FirebaseFirestore.getInstance();
        firebaseService = new FirebaseService(this);

        getUsername();

        dbref_img = database.getReference("images").child(username);

        // Obtener la imagen que se usa para acceder a viewpic y asignarsela
        imageURL = getIntent().getExtras().getString("imageURL");

        tv_username_info = findViewById(R.id.tv_username_info);
        tv_category_info = findViewById(R.id.tv_category_info);
        tv_location_info = findViewById(R.id.tv_location_info);
        tv_descripcion_info = findViewById(R.id.tv_descripcion_info);

        firestoreDatabase.collection("images")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("url").equals(imageURL)) {
                                    tv_username_info.setText(document.get("username").toString());
                                    tv_category_info.setText(document.get("category").toString());
                                    tv_location_info.setText(document.get("location").toString());
                                    tv_descripcion_info.setText(document.get("description").toString());
                                    break;
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        iv_imagenSubida = findViewById(R.id.iv_viewPicImagen);

        StorageReference storageRef = firebaseStorage.getReference();
        final StorageReference imageRef = storageRef.child(imageURL);

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

    public void getUsername() {
        userInfo = firebaseService.getCurrentUser();
        try {
            username = userInfo.get("username").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void close(View view){
        onBackPressed();
    }
}

