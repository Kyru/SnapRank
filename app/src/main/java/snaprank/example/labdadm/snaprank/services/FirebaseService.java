package snaprank.example.labdadm.snaprank.services;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;

public class FirebaseService {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private JSONObject userInfo = new JSONObject();
    private FirebaseUser currentUser;

    // variable to hold context
    private Context context;

    public FirebaseService(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public JSONObject getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                try {
                    userInfo.put("email", email);
                    userInfo.put("username", name);
                    userInfo.put("profilePicture", photoUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return userInfo;
    }

    public void updateUser(String username) {
        currentUser = auth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Profile updated
                        }
                    }
                });
    }

    public void setProfilePicture(String photoUri) {
        currentUser = auth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUri))
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createToast(Resources.getSystem().getString(R.string.profile_pic_changed_message));
                        }
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }

    public void uploadImage(ImagenSubida image, String username) {

        String uuid = "" + UUID.randomUUID();
        db.collection("images").add(image);
        db.collection("users").document(uuid).set(image);
        db.collection("categories").document(image.getCategory()).set(image);
    }

    public void changePassword(String newPassword) {
        currentUser = auth.getCurrentUser();
        currentUser.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password changed
                        }
                    }
                });
    }

    public void deleteAccount() {
        currentUser = auth.getCurrentUser();
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createToast(Resources.getSystem().getString(R.string.delete_accound_message_confirmation));
                        }
                    }
                });
    }

    public void signUp(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    updateUser(username);

                    // updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    createToast("Authentication failed.");
                    // updateUI(null);
                }
            }
        });
    }

    private void createToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
