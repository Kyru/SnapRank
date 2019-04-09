package snaprank.example.labdadm.snaprank.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    public void uploadImage(View view) {
        String description = descriptionText.getText().toString();
        String location = locationText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();

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

            //bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);


            imageToUpload.setDrawingCacheEnabled(false);
            byte[] data = baos.toByteArray();

            String path = "images/" + UUID.randomUUID() + ".jpeg";
            StorageReference storageReference = storage.getReference(path);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("description", description)
                    .setCustomMetadata("location", location)
                    .setCustomMetadata("category", category)
                    .setCustomMetadata("user", username)
                    .build();

            // Subir la información de la imagen tambien a la base de datos
            ImagenSubida imagenSubida = new ImagenSubida(username, description, category, location, path,0,0);
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
                    onBackPressed();
                }
            });
        }
    }

    public void createToast(String message) {
        Toast.makeText(UploadImageActivity.this, message,
                Toast.LENGTH_LONG).show();
    }
}
