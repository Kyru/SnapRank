package snaprank.example.labdadm.snaprank.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class GalleryService {
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    private static final int REQUEST_CODE = 1;

    // variable to hold context
    private Context context;
    private Activity activity;

    public GalleryService(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
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
            ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE);
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
            if(photoPickerIntent.resolveActivity(context.getPackageManager()) != null) {
                activity.startActivityForResult(chooser, REQUEST_CODE);
            }

        }

    }
}
