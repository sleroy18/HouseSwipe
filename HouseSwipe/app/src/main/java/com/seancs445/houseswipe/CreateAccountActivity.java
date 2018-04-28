package com.seancs445.houseswipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import java.security.Provider;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String USER_CACHE_DATA = "UserLoginInfo";
    private static final String USER_CACHE_EMAIL = "HouseSwipeEmail";
    private static final String USER_CACHE_PASSWORD = "HouseSwipePassword";
    private static final String USER = "User";
    private static final String LOADER = "loading";

    private static final String TAG = "Authentication";

    private FirebaseAuth mAuth;
    private LinearLayout loading;
    private ConstraintLayout content;
    private Button cancelBtn;
    private Button signUpBtn;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        cancelBtn = findViewById(R.id.buttonCancel);
        signUpBtn = findViewById(R.id.buttonSignUp);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirmPassword = findViewById(R.id.editTextConfirm);
        loading = findViewById(R.id.loading);
        content = findViewById(R.id.content);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(firstName.getText().toString(), lastName.getText().toString(),
                        email.getText().toString(), password.getText().toString());

                if (CheckEmail(user.getEmail())) {
                    if (CheckMatchingPasswords(user.getPassword(), confirmPassword.getText().toString())) {
                        if (CheckPasswordRequirements(user.getPassword())) {
                            ShowProgressBar();
                            CreateAccount(user);
                        }
                        else{
                            //put red notification here
                            Toast toast = Toast.makeText(getApplicationContext(), "Invalid Password",Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                    else{
                        //put red notification here
                        Toast toast = Toast.makeText(getApplicationContext(), "Passwords do NOT match",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Email",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private boolean CheckEmail(String email){
        if(email.contains("@") && email.contains(".") && email.length()>5){
            return true;
        }
        return false;
    }


    private boolean CheckMatchingPasswords(String password, String confirmPassword){
        if(password.equals(confirmPassword)){
            return true;
        }
            return false;
    }

    private boolean CheckPasswordRequirements(String password){
        if(password.length() > 6 && password.matches("[a-zA-Z0-9]*")){
            return true;
        }
        return false;
    }

    private void CreateAccount(User user){
        final User newUser = user;
        final String email = user.getEmail();
        final String password = user.getPassword();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mPrefs = getApplicationContext().getSharedPreferences(USER_CACHE_DATA, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString(USER_CACHE_EMAIL, email);
                            editor.putString(USER_CACHE_PASSWORD, password);
                            editor.commit();

                            newUser.setUID(task.getResult().getUser().getUid());

                            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                            db.addUser(newUser);

                            Intent intent = new Intent(CreateAccountActivity.this, SettingsActivity.class);
                            intent.putExtra(USER, newUser);
                            intent.putExtra(LOADER, true);
                            startActivity(intent);
                        } else {
                            HideProgressBar();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            try
                            {
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Log.d(TAG, "EmailPasswordCreate: exist_email");
                                Toast toast = Toast.makeText(CreateAccountActivity.this, "An account with this email already exists",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            catch (Exception e)
                            {
                                Toast toast = Toast.makeText(CreateAccountActivity.this, "Unable to create account. Please try again latter.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                Log.d(TAG, "EmailPasswordCreate: " + e.getMessage());
                            }
                        }
                    }
                });
    }

    private void ShowProgressBar(){
        loading.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    private void HideProgressBar(){
        loading.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }
}
