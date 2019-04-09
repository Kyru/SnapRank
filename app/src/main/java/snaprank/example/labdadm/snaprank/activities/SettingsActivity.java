package snaprank.example.labdadm.snaprank.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.services.FirebaseService;
import snaprank.example.labdadm.snaprank.services.GalleryService;

public class SettingsActivity extends AppCompatActivity {

    FirebaseService firebaseService;

    private GalleryService galleryService;
    private static final int REQUEST_CODE = 1;
    private Uri uri;

    SharedPreferences preferences;

    private ImageView profilePicture;

    private FirebaseStorage storage;
    private String URLProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profilePicture = findViewById(R.id.profilePicture);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        storage = FirebaseStorage.getInstance();

        /* Initialize services */
        firebaseService =  new FirebaseService(this);
        galleryService = new GalleryService(this, this);
    }

    public void changePassword(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
        alertDialog.setTitle(R.string.password_summary);
        final EditText oldPass = new EditText(SettingsActivity.this);
        final EditText newPass = new EditText(SettingsActivity.this);
        final EditText confirmPass = new EditText(SettingsActivity.this);

        oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        oldPass.setHint("Old Password");
        newPass.setHint("Nueva contraseña");
        confirmPass.setHint("Confirmar contraseña");
        LinearLayout layout = new LinearLayout(SettingsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(oldPass);

        layout.addView(newPass);
        layout.addView(confirmPass);
        alertDialog.setView(layout);
        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseService.changePassword(newPass.getText().toString());
                        createToast(getResources().getString(R.string.password_changed));
                    }
                });
        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void deleteAccount(View view) {
        AlertDialog.Builder deleteAccountDialog = new AlertDialog.Builder(Objects.requireNonNull(this));
        deleteAccountDialog.setMessage(R.string.delete_accound_message);

        deleteAccountDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseService.deleteAccount();
                preferences.edit().putBoolean("loggedIn", false).apply();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        deleteAccountDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        deleteAccountDialog.create().show();
    }

    public void changeProfilePic(View view) {
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

                ImageView imageView = findViewById(R.id.profilePicture);
                imageView.setImageBitmap(bitmap);
                uploadImage();
                // firebaseService.setProfilePicture(URLProfilePic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage() {

        profilePicture.setDrawingCacheEnabled(true);
        profilePicture.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int imageHeight = profilePicture.getDrawable().getIntrinsicHeight();
        int imageWidth = profilePicture.getDrawable().getIntrinsicWidth();

        Bitmap croppedBitmap;
        if(imageHeight > imageWidth){
            croppedBitmap = Bitmap.createScaledBitmap(bitmap, 200 , 400, true);
        } else {
            croppedBitmap = Bitmap.createScaledBitmap(bitmap, 400 , 200, true);
        }

        //bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);

        profilePicture.setDrawingCacheEnabled(false);
        byte[] data = baos.toByteArray();

        String path = "profile-pics/" + UUID.randomUUID() + ".jpeg";
        final StorageReference storageReference = storage.getReference(path);

        /*progressBar.setVisibility(View.VISIBLE);
        uploadButton.setEnabled(false);*/
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(SettingsActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                /*progressBar.setVisibility(View.GONE);
                uploadButton.setEnabled(true);*/

                String successMessage = getResources().getString(R.string.success_upload_photo);
                createToast(successMessage);
            }
        });

    }

    public void createToast(String message) {
        Toast.makeText(SettingsActivity.this, message,
                Toast.LENGTH_LONG).show();
    }
}
