package com.abanoubashraf.badawy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerificationCodeActivity extends AppCompatActivity {

    private TextView toolbar_text, textView_resend;
    private User user;
    private EditText editText_code;
    private Button button_confirm;
    private RelativeLayout mActivityIndicator;


    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        toolbar_text = findViewById(R.id.toolbar_text);
        editText_code = findViewById(R.id.edittext_verification_code);
        button_confirm = findViewById(R.id.button_confirm);
        textView_resend = findViewById(R.id.textView_resend);
        mActivityIndicator = findViewById(R.id.activityIndicatorLayout);
        mActivityIndicator.setOnClickListener(null);

        toolbar_text.setText(R.string.enter_verification_code);

        //getting current user object
        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"), User.class);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.useAppLanguage();

        String phoneNumber = user.getMobile_number();

        setUpVerificatonCallbacks();

        Toast.makeText(getApplicationContext(), user.getMobile_number(), Toast.LENGTH_SHORT).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);

//        mActivityIndicator.setVisibility(View.VISIBLE);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(v);
            }
        });

        textView_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode(v);
            }
        });

    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d("verification_failed", "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d("verification_failed", "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        Toast.makeText(getApplicationContext(), "Code sent", Toast.LENGTH_SHORT).show();

                        button_confirm.setEnabled(true);
                        textView_resend.setEnabled(true);
                    }
                };
    }

    public void verifyCode(View view) {
        mActivityIndicator.setVisibility(View.VISIBLE);
        String code = editText_code.getText().toString();
        if (!TextUtils.isEmpty(code)) {
            try {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            } catch (Exception e) {

            }

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VerificationCodeActivity.this);
            alertDialogBuilder.setTitle(R.string.enter_verification_code_sent_in_message);
            alertDialogBuilder.setPositiveButton(R.string.ok, null);
            alertDialogBuilder.create().show();
//            Toast.makeText(getApplicationContext(), R.string.enter_verification_code_sent_in_message, Toast.LENGTH_LONG).show();
        }
        mActivityIndicator.setVisibility(View.GONE);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            editText_code.setText("");
                            textView_resend.setEnabled(false);
                            button_confirm.setEnabled(false);
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            user.setId(firebaseUser.getUid());

                            Toast.makeText(getApplicationContext(), R.string.registered_successfully, Toast.LENGTH_SHORT).show();

                            //save current user in shared preferences
                            SharedHelper.getSharedHelper(getApplicationContext()).setCurrentUser(user);

                            //save user in firebase
                            saveUserOnFirebase(user);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), R.string.wrong_code, Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void resendCode(View view) {

        String phoneNumber = user.getMobile_number();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }

    public void saveUserOnFirebase (User user) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        final HashMap<String, Object> users = new HashMap<>();
        users.put(user.getId(), user);

        databaseReference.updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(VerificationCodeActivity.this, HomeActivity.class);
                    finishAffinity();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.dataBase_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
