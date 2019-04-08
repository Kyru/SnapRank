package snaprank.example.labdadm.snaprank.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class SettingsActivity extends AppCompatActivity {

    FirebaseService firebaseService =  new FirebaseService(getApplicationContext());
    String password = "";

    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    private static final int REQUEST_CODE = 1;
    private Uri uri;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        firebaseService.deleteAccount();
        preferences.edit().putBoolean("loggedIn", false).apply();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void requestPermissions() {
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    changeProfilePic(findViewById(android.R.id.content));

                } else {
                    String permissionsDeniedMessage = getResources().getString(R.string.permissions_denied_message);
                    createToast(permissionsDeniedMessage);
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void changeProfilePic(View view) {
        int storagePermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (storagePermissions != PackageManager.PERMISSION_GRANTED || cameraPermissions != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions();
        } else {
            Intent photoPickerIntent = new Intent();
            photoPickerIntent.setType("image/*");
            photoPickerIntent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), REQUEST_CODE);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createToast(String message) {
        Toast.makeText(SettingsActivity.this, message,
                Toast.LENGTH_LONG).show();
    }
}
