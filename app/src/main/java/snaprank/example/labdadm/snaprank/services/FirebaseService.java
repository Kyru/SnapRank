package snaprank.example.labdadm.snaprank.services;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseService {
    private FirebaseAuth auth;

    JSONObject userInfo = new JSONObject();

    public FirebaseService() {
        auth = FirebaseAuth.getInstance();
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
                    userInfo.put("uid", photoUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return userInfo;
    }

    public void updateUser(String username) {
        FirebaseUser currentUser = auth.getCurrentUser();
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

    public void logout() {
        auth.signOut();
    }
}
