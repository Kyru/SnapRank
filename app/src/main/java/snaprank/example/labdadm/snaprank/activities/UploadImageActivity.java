package snaprank.example.labdadm.snaprank.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.services.FirebaseService;
import snaprank.example.labdadm.snaprank.services.GalleryService;

public class UploadImageActivity extends AppCompatActivity {

    private Button uploadButton;
    private ImageView imageToUpload;
    private EditText descriptionText;
    private EditText locationText;
    private Spinner categorySpinner;
    private ProgressBar progressBar;
    private boolean hasImageUpload = false;

    ImageButton back;
    Intent intent;

    private FirebaseAuth auth;
    private String username;

    private static final int REQUEST_CODE = 1;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private FirebaseService firebaseService;
    private Uri uri;

    private GalleryService galleryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        /* Initialize services */
        galleryService = new GalleryService(this, this);
        firebaseService = new FirebaseService(this);

        /* Initialize interface */
        // Setting custom ActionBar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
        findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
        findViewById(R.id.logoutButton).setVisibility(View.GONE);
        findViewById(R.id.back).setVisibility(View.VISIBLE);


        // Cambiar el color del ActionBar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xeeeeeeee));


        imageToUpload = findViewById(R.id.imageToUpload);
        descriptionText = findViewById(R.id.desciptionText);
        locationText = findViewById(R.id.locationText);
        uploadButton = findViewById(R.id.uploadButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBarUploadPhoto);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            username = user.getDisplayName();
        }

        Bundle bundle = new Bundle();
        intent = new Intent(this, MainActivity.class);

        bundle.putString("username", username);
        bundle.putBoolean("goToProfile", false);
        intent.putExtras(bundle);


        back = findViewById(R.id.back);

        ((View) back).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     onBackPressed();
                 }
             }
        );

    }

    public void pickPictureFromGallery(View view) {
        galleryService.pickPictureFromGallery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    galleryService.pickPictureFromGallery();

                } else {
                    String permissionsDeniedMessage = getResources().getString(R.string.permissions_denied_message);
                    createToast(permissionsDeniedMessage);
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = findViewById(R.id.imageToUpload);
                imageView.setImageBitmap(bitmap);
                hasImageUpload = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            default: return category;
        }
    }

    public void uploadImage(View view) {
        String description = descriptionText.getText().toString();
        String location = locationText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();

        category = translateCategory(category);

        if (!description.isEmpty() && !location.isEmpty() && hasImageUpload) {
            imageToUpload.setDrawingCacheEnabled(true);
            imageToUpload.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int imageHeight = imageToUpload.getDrawable().getIntrinsicHeight();
            int imageWidth = imageToUpload.getDrawable().getIntrinsicWidth();

            Bitmap croppedBitmap;
            if(imageHeight > imageWidth){
                croppedBitmap = Bitmap.createScaledBitmap(bitmap, 200 , 400, true);
            } else {
                croppedBitmap = Bitmap.createScaledBitmap(bitmap, 400 , 200, true);
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

            //croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);


            imageToUpload.setDrawingCacheEnabled(false);
            byte[] data = baos.toByteArray();

            UUID id = UUID.randomUUID();

            String path = "images/" + id + ".jpeg";
            StorageReference storageReference = storage.getReference(path);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("description", description)
                    .setCustomMetadata("location", location)
                    .setCustomMetadata("category", category)
                    .setCustomMetadata("user", username)
                    .build();

            // Subir la información de la imagen tambien a la base de datos
            ImagenSubida imagenSubida = new ImagenSubida(username, description, category, location, path,0,0, ""+id);
            firebaseService.uploadImage(imagenSubida, username);

            progressBar.setVisibility(View.VISIBLE);
            uploadButton.setEnabled(false);
            UploadTask uploadTask = storageReference.putBytes(data, metadata);
            uploadTask.addOnSuccessListener(UploadImageActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    uploadButton.setEnabled(true);
                    String successMessage = getResources().getString(R.string.success_upload_photo);
                    createToast(successMessage);
                    startActivity(intent);
                }
            });
        } else {
            createToast(getResources().getString(R.string.no_fields_upload_image));
        }
    }

    public void createToast(String message) {
        Toast.makeText(UploadImageActivity.this, message,
                Toast.LENGTH_LONG).show();
    }

}

