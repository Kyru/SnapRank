package snaprank.example.labdadm.snaprank.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.Usuario;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class SignupActivity extends AppCompatActivity {
    private EditText fieldUsername;
    private EditText fieldEmail;
    private EditText fieldPassword;
    private EditText fieldConfirmPassword;
    private EditText fieldLocation;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String location;
    private List<String> usernameList;

    private ProgressBar progressBar;
    SharedPreferences preferences;

    private FirebaseService firebaseService;
    private FirebaseFirestore firestoreDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fieldUsername = findViewById(R.id.usernameText);
        fieldEmail = findViewById(R.id.emailEditText);
        fieldPassword = findViewById(R.id.passwordEditText);
        fieldConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        fieldLocation = findViewById(R.id.locationEditText);

        progressBar = findViewById(R.id.progressBarSignup);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        firebaseService = new FirebaseService(this);
        firestoreDatabase = FirebaseFirestore.getInstance();

        usernameList = new ArrayList<>();
    }

    public void createAccount(View view) {
        progressBar.setVisibility(View.VISIBLE);

        username = fieldUsername.getText().toString();
        email = fieldEmail.getText().toString();
        password = fieldPassword.getText().toString();
        confirmPassword = fieldConfirmPassword.getText().toString();
        location = fieldLocation.getText().toString();

        createAccount(username, email, password, confirmPassword, location);
    }

    public void createAccount(String usernamex, String emailx, String passwordx, String confirmPasswordx, String locationx) {

        this.username = usernamex;
        this.email = emailx;
        this.password = passwordx;
        this.location = locationx;

        if (!validateForm()) {
            removeProgressBar();
            return;
        }

        if (!password.equals(confirmPassword)) {
            createToast("Las contraseñas no coinciden.");
            removeProgressBar();
            return;
        }

        usernameList.clear();
        firestoreDatabase.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usernameList.add(document.toObject(Usuario.class).getUsername());
                            }
                            if (usernameList.size() != 0) {
                                createToast(getResources().getString(R.string.new_username_taken));
                            }
                            else{
                                firebaseService.signUp(username, email, password, location);
                            }
                        }
                    }
                });

        removeProgressBar();

    }

    /**
     * Validates the form to create the account
     * @return Returns true if everything is fine, false if there is a problem
     */
    private boolean validateForm() {
        boolean valid = true;

        email = fieldEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            fieldEmail.setError("Required.");
            valid = false;
        } else {
            fieldEmail.setError(null);
        }

        password = fieldPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            fieldPassword.setError("Required.");
            valid = false;
        } else {
            fieldPassword.setError(null);
        }

        confirmPassword = fieldConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            fieldConfirmPassword.setError("Required.");
            valid = false;
        } else {
            fieldConfirmPassword.setError(null);
        }

        return valid;
    }

    private void createToast(String message) {
        Toast.makeText(SignupActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    private void removeProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
