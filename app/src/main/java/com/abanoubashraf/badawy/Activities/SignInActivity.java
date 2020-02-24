package com.abanoubashraf.badawy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.abanoubashraf.badawy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private Button button_bedouin, button_non_bedouin;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        button_bedouin = findViewById(R.id.button_bedouin);
        button_non_bedouin = findViewById(R.id.button_non_bedouin);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }

        button_bedouin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, BedouinQuestionsActivity.class);
                intent.putExtra("type", "b");
                startActivity(intent);
            }
        });

        button_non_bedouin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.putExtra("type", "nb");
                startActivity(intent);
            }
        });
    }
}
