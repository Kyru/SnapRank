package snaprank.example.labdadm.snaprank.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class SignupActivity extends AppCompatActivity {
    private EditText fieldUsername;
    private EditText fieldEmail;
    private EditText fieldPassword;
    private EditText fieldConfirmPassword;

    private Button signupButton;

    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    private ProgressBar progressBar;
    SharedPreferences preferences;

    private FirebaseAuth auth;
    private FirebaseService firebaseService = new FirebaseService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fieldUsername = findViewById(R.id.usernameText);
        fieldEmail = findViewById(R.id.emailEditText);
        fieldPassword = findViewById(R.id.passwordEditText);
        fieldConfirmPassword = findViewById(R.id.confirmPasswordEditText);

        progressBar = findViewById(R.id.progressBarSignup);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
    }

    public void createAccount(View view) {
        progressBar.setVisibility(View.VISIBLE);

        username = fieldUsername.getText().toString();
        email = fieldEmail.getText().toString();
        password = fieldPassword.getText().toString();
        confirmPassword = fieldConfirmPassword.getText().toString();

        createAccount(email, password, confirmPassword);
    }

    public void createAccount(String email, String password, String confirmPassword) {

        if (!validateForm()) {
            removeProgressBar();
            return;
        }

        if (!password.equals(confirmPassword)) {
            createToast("Las contraseñas no coinciden.");
            removeProgressBar();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    removeProgressBar();
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = auth.getCurrentUser();
                    firebaseService.updateUser(username);

                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    // updateUI(user);
                } else {
                    removeProgressBar();
                    // If sign in fails, display a message to the user.
                    createToast("Authentication failed.");
                    // updateUI(null);
                }
            }
        });
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
