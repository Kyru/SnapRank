package snaprank.example.labdadm.snaprank.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import snaprank.example.labdadm.snaprank.R;

public class GalleryService extends Activity {
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    private static final int REQUEST_CODE = 1;

    // variable to hold context
    private Context context;

    public GalleryService(Context context) {
        this.context = context;
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
        if (!hasPermissions(context, PERMISSIONS)) {
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

                    pickPictureFromGallery();

                } else {
                    String permissionsDeniedMessage = getResources().getString(R.string.permissions_denied_message);
                    createToast(permissionsDeniedMessage);
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void pickPictureFromGallery() {
        int storagePermissions = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermissions = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

        if (storagePermissions != PackageManager.PERMISSION_GRANTED || cameraPermissions != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions();
        } else {
            Intent photoPickerIntent = new Intent();
            photoPickerIntent.setType("image/*");
            photoPickerIntent.setAction(Intent.ACTION_PICK);
            Intent chooser = Intent.createChooser(photoPickerIntent,"Select picture");
            if(photoPickerIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(chooser, REQUEST_CODE);
            }

        }

    }

    public void createToast(String message) {
        Toast.makeText(context, message,
                Toast.LENGTH_LONG).show();
    }
}
