package snaprank.example.labdadm.snaprank.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.models.Usuario;
import snaprank.example.labdadm.snaprank.services.FirebaseService;
import snaprank.example.labdadm.snaprank.services.GalleryService;

public class SettingsActivity extends AppCompatActivity {

    FirebaseService firebaseService;

    private GalleryService galleryService;
    private static final int REQUEST_CODE = 1;
    private Uri uri;

    SharedPreferences preferences;

    private ImageView profilePicture;
    ImageButton back;

    private FirebaseStorage storage;
    private String URLProfilePic;
    private String username;
    private List<String> usernameList;
    private TextView usernameText;
    private Bitmap bitmap;
    private FirebaseFirestore firestoreDatabase;
    private Object user;
    private StorageReference storageRef;
    private String userID;
    ArrayList<String> imagesID = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firestoreDatabase = FirebaseFirestore.getInstance();

        usernameText = findViewById(R.id.usernameText);
        profilePicture = findViewById(R.id.profilePicture);

        usernameList = new ArrayList<>();

        // Setting custom ActionBar
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        // Cambiar el color del ActionBar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xeeeeeeee));

        findViewById(R.id.custom_bar_add).setVisibility(View.GONE);
        findViewById(R.id.custom_bar_filter).setVisibility(View.GONE);
        findViewById(R.id.logoutButton).setVisibility(View.GONE);
        findViewById(R.id.back).setVisibility(View.VISIBLE);

        back = findViewById(R.id.back);

        ((View) back).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(getBaseContext(), MainActivity.class);
                     Bundle bundle = new Bundle();
                     bundle.putString("username", username);
                     bundle.putBoolean("goToProfile", true);
                     intent.putExtras(bundle);
                     startActivity(intent);
                 }
             }
        );

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        /* Initialize services */
        firebaseService = new FirebaseService(this);
        galleryService = new GalleryService(this, this);

        try {
            username = firebaseService.getCurrentUser().get("username").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        usernameText.setText(username);

        /* Get profile url and user id */
        firestoreDatabase.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                URLProfilePic = document.getData().get("profilePicUrl") + "";
                                userID = document.getData().get("id") + "";
                            }
                        } else {
                            createToast(getResources().getString(R.string.fail_getting_profile_pic));
                        }
                    }
                });

        /* Get profile url and user id */
        firestoreDatabase.collection("images")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                imagesID.add(document.getId());
                            }
                        } else {
                            createToast(getResources().getString(R.string.fail_getting_profile_pic));
                        }
                    }
                });
    }

    public void changeProfilePicture(String URLProfilePic) {
        StorageReference imageRef = storageRef.child(URLProfilePic);

        final long ONE_MEGABYTE = 2048 * 2048;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                profilePicture.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 350,
                        250, false));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void changePassword(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
        alertDialog.setTitle(R.string.password_summary);
        final EditText newPass = new EditText(SettingsActivity.this);
        final EditText confirmPass = new EditText(SettingsActivity.this);

        newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        newPass.setHint(getResources().getString(R.string.password));
        confirmPass.setHint(getResources().getString(R.string.confirm_password));

        LinearLayout layout = new LinearLayout(SettingsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(newPass);
        layout.addView(confirmPass);
        alertDialog.setView(layout);
        alertDialog.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (newPass.getText().toString().equals(confirmPass.getText().toString()) && !newPass.getText().toString().equals("")) {
                            firebaseService.changePassword(newPass.getText().toString());
                        } else {
                            createToast(getResources().getString(R.string.passwords_do_not_match));
                        }

                    }
                });
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void changeUserNameAlert(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
        alertDialog.setTitle(getResources().getString(R.string.username_alert));
        final EditText newUser = new EditText(SettingsActivity.this);

        newUser.setHint(getResources().getString(R.string.new_username));
        LinearLayout layout = new LinearLayout(SettingsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(newUser);
        alertDialog.setView(layout);
        alertDialog.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (newUser.getText().toString().equals("") || newUser.getText().toString().equals(username)) {
                            String toastMessage = getResources().getString(R.string.new_username_error);
                            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            checkUserName(newUser.getText().toString());
                        }
                    }
                });
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void checkUserName(final String newUserName) {
        firestoreDatabase.collection("users")
                .whereEqualTo("username", newUserName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usernameList.add(document.toObject(Usuario.class).getUsername());
                            }
                            if (usernameList.size() != 0) {
                                String toastMessage = getResources().getString(R.string.new_username_taken);
                                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                changeUserName(newUserName);
                            }
                        } else {
                            createToast(getResources().getString(R.string.fail_getting_profile_pic));
                        }
                    }
                });
    }

    public void changeUserName(final String newUserName) {
        firestoreDatabase.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                usuario.setUsername(newUserName);
                                firestoreDatabase.collection("users").document(usuario.getId())
                                        .set(usuario, SetOptions.merge());
                            }
                            upateUserPhotos(username, newUserName);
                            firebaseService.updateUser(newUserName);
                            username = newUserName;
                        } else {
                            createToast(getResources().getString(R.string.fail_getting_profile_pic));
                        }
                    }
                });
    }

    public void upateUserPhotos(String oldUser, final String newUser) {
        firestoreDatabase.collection("images")
                .whereEqualTo("username", oldUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ImagenSubida imagenSubida = document.toObject(ImagenSubida.class);
                                imagenSubida.setUsername(newUser);
                                firestoreDatabase.collection("images").document(imagenSubida.getId())
                                        .set(imagenSubida, SetOptions.merge());
                            }
                        } else {
                            createToast(getResources().getString(R.string.fail_getting_profile_pic));
                        }
                    }
                });
    }

    public void changeLocationAlert(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);

        alertDialog.setTitle(getResources().getString(R.string.location_alert));
        final EditText newLocation = new EditText(SettingsActivity.this);

        newLocation.setHint(getResources().getString(R.string.new_location));
        LinearLayout layout = new LinearLayout(SettingsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(newLocation);
        alertDialog.setView(layout);
        alertDialog.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (newLocation.getText().toString().equals("")) {
                            String toastMessage = getResources().getString(R.string.new_location_error);
                            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            changeLocation(newLocation.getText().toString());
                        }
                    }
                });
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void changeLocation(final String newLocation) {
        firestoreDatabase.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                usuario.setLocation(newLocation);
                                firestoreDatabase.collection("users").document(usuario.getId())
                                        .set(usuario, SetOptions.merge());
                            }
                        } else {
                            createToast(getResources().getString(R.string.fail_getting_profile_pic));
                        }
                    }
                });
    }

    public void deleteAccount(View view) {
        AlertDialog.Builder deleteAccountDialog = new AlertDialog.Builder(Objects.requireNonNull(this));
        deleteAccountDialog.setMessage(R.string.delete_accound_message);

        deleteAccountDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseService.deleteAccount(userID, imagesID);
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
        if (imageHeight > imageWidth) {
            croppedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 400, true);
        } else {
            croppedBitmap = Bitmap.createScaledBitmap(bitmap, 400, 200, true);
        }

        //bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);

        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);

        profilePicture.setDrawingCacheEnabled(false);
        byte[] data = baos.toByteArray();

        UUID userID = UUID.randomUUID();
        String path = "profile-pics/" + userID + ".jpeg";
        final StorageReference storageReference = storage.getReference(path);

        try {
            firebaseService.getProfileId(path);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
