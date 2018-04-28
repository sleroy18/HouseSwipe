package com.seancs445.houseswipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String USER_CACHE_DATA = "UserLoginInfo";
    private static final String USER_CACHE_EMAIL = "HouseSwipeEmail";
    private static final String USER_CACHE_PASSWORD = "HouseSwipePassword";
    private static final String USER = "User";
    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;

    private SharedPreferences mPrefs;
    private FirebaseAuth mAuth;

    private Button signUpBtn;
    private Button loginBtn;

    private LinearLayout loading;
    private ConstraintLayout content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = findViewById(R.id.editTextEmail);
        mPasswordField = findViewById(R.id.editTextPassword);
        mPrefs = getApplicationContext().getSharedPreferences(USER_CACHE_DATA, Context.MODE_PRIVATE);
        loginBtn = findViewById(R.id.btn_login);
        signUpBtn = findViewById(R.id.btn_sign_up);
        loading = findViewById(R.id.loading);
        content = findViewById(R.id.content);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String submitEmail = mEmailField.getText().toString();
                String submitPassword = mPasswordField.getText().toString();
                if (!submitEmail.equals("") && !submitPassword.equals("")) {
                    signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "enter user and pass", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
            }
        });


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences(USER_CACHE_DATA, Context.MODE_PRIVATE);
        String email = mPrefs.getString(USER_CACHE_EMAIL, "");
        String password = mPrefs.getString(USER_CACHE_PASSWORD, "");
        if (!email.equals("") && !password.equals("")) {
            Log.d(TAG, "signInWithEmail: " + email);
            Log.d(TAG, "signInWithEmail: " + password);
            signIn(email, password);
        }
    }

    private void signIn(final String email, final String password) {
        ShowProgressBar();
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email, password)) {
            Toast toast = Toast.makeText(getApplicationContext(), "invalid form", Toast.LENGTH_LONG);
            toast.show();
            HideProgressBar();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser fbUser = mAuth.getCurrentUser();

//                          If info not in system cache then add it
                            if (!mPrefs.contains(USER_CACHE_EMAIL) || !mPrefs.contains(USER_CACHE_PASSWORD)) {
                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putString(USER_CACHE_EMAIL, email);
                                editor.putString(USER_CACHE_PASSWORD, password);
                                editor.commit();
                            }
                            User user = new User();
                            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                            user = db.getUser(task.getResult().getUser().getUid());

                            //if first login or not in database: add
                            if (user.getUID() == null) {
                                user.setUID(task.getResult().getUser().getUid());
                                user.setEmail(email);
                                user.setPassword(password);
                                db.addUser(user);
                                Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                                intent.putExtra(USER, user);
                                startActivity(intent);
                            }
                            Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
                            intent.putExtra(USER, user);
                            startActivity(intent);

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            HideProgressBar();
                        }
                    }
                });
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        //Note: TextUtils always returns boolean. String.isEmpty will return nullPointer
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void ShowProgressBar() {
        loading.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    private void HideProgressBar() {
        loading.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    //    private void createAccount(String email, String password) {
//        Log.d(TAG, "createAccount:" + email);
//        if (!validateForm()) {
//            return;
//        }
//
//
//        // [START create_user_with_email]
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(getApplicationContext(), "User Created Success",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    //    private void sendEmailVerification() {
//        findViewById(R.id.verify_email_button).setEnabled(false);
//
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // Re-enable button
//                        findViewById(R.id.verify_email_button).setEnabled(true);
//
//                        if (task.isSuccessful()) {
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

//    private void signOut() {
//        mAuth.signOut();
//        //updateUI(null);
//    }
}