package snaprank.example.labdadm.snaprank.services;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.UUID;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.MainActivity;
import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.models.Logro;
import snaprank.example.labdadm.snaprank.models.Usuario;

public class FirebaseService {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private JSONObject userInfo = new JSONObject();
    private FirebaseUser currentUser;

    // variable to hold context
    private Context context;
    Bundle bundle = new Bundle();

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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

    public void getProfileId(final String photoUri) throws JSONException {
        final String[] id = new String[1];
        /* Get profile url */
        db.collection("users")
                .whereEqualTo("username", getCurrentUser().get("username"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("USERTGH", " = " + document.getData());
                                id[0] = document.getData().get("id") + "";
                                setProfilePicture(id[0], photoUri);
                            }
                        } else {
                            // Error getting document
                        }
                    }
                });


    }

    public void setProfilePicture(String id, String photoUri) {
        DocumentReference docRef = db.collection("users").document(id);
        docRef.update("profilePicUrl", photoUri)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /* Photo URL updated */
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }

    public void uploadImage(ImagenSubida image, String username) {
        db.collection("images").document(image.getId()).set(image);
    }

    public void uploadUser(Usuario user) {
        db.collection("users").document(user.getId()).set(user);
    }

    public void changePassword(String newPassword) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createToast(context.getString(R.string.password_changed));
                        }
                    }
                });
    }

    /**
     * Deletes user
     * @param userID User's ID
     */
    public void deleteAccount(String userID, ArrayList<String> imagesID) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createToast(context.getString(R.string.delete_accound_message_confirmation));
                        } else {
                            createToast(context.getString(R.string.delete_accound_message_fail));
                        }
                    }
                });

        /* Deletes user from database */
        db.collection("users").document(userID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User deleted from database successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error deleting user from database
                    }
                });
        for (String imageID : imagesID) {
            /* Deletes user from database */
            db.collection("images").document(imageID)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Image deleted from database successfully
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error deleting image from database
                }
            });
        }
    }

    public void signUp(final String username, String email, String password, final String location) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    updateUser(username);

                    saveUserToDatabase(username, location);

                    Intent intent = new Intent(context, MainActivity.class);
                    bundle.putString("username", username);
                    bundle.putBoolean("goToProfile", false);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                    // updateUI(user);
                } else if (!task.isSuccessful()){

                    // If sign in fails, display a message to the user.
                    createToast("Authentication failed.");
                    // updateUI(null);
                }
            }
        });
    }

    public void saveUserToDatabase(String username, String location) {
        ArrayList<Logro> awards = new ArrayList<>();
        /* Save user in database */
        UUID userID = UUID.randomUUID();
        String path = "profile-pics/profilepic_default.jpeg";
        Usuario user = new Usuario("" + userID, username, location, path, 0, awards);
        uploadUser(user);
    }

    private void createToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
