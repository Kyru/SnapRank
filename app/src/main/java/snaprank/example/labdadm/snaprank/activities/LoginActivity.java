package snaprank.example.labdadm.snaprank.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.services.FirebaseService;

public class LoginActivity extends AppCompatActivity {

    private EditText fieldEmail;
    private EditText fieldPassword;

    private Button loginButton;
    private Button signupButton;

    private String email;
    private String password;

    private ProgressBar progressBar;

    private FirebaseAuth auth;

    SharedPreferences preferences;
    private boolean loggedIn;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fieldEmail = findViewById(R.id.emailEditText);
        fieldPassword = findViewById(R.id.passwordEditText);

        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        progressBar = findViewById(R.id.progressBarLogin);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loggedIn = preferences.getBoolean("loggedIn", false);
        if (loggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            goToMainActivity(intent);
            //startActivity(intent);
            finish();
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        // updateUI(currentUser);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void logIn(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        hideKeyboard(this);

        if (!validateForm()) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);

                    preferences.edit().putBoolean("loggedIn", true).apply();
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = auth.getCurrentUser();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    goToMainActivity(intent);

                    //startActivity(intent);
                    // updateUI(user);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    // updateUI(null);
                }
            }
        });
    }

    public void logIn(View view) {
        email = fieldEmail.getText().toString();
        password = fieldPassword.getText().toString();

        logIn(email, password);
    }

    public void goToMainActivity(Intent intent){
        Bundle bundle = new Bundle();
        FirebaseService service = new FirebaseService();
        JSONObject userInfo = service.getCurrentUser();
        try {
            username = userInfo.get("username").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bundle.putString("username", username);
        bundle.putBoolean("goToProfile", false);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openSignup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
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

        return valid;
    }
}
