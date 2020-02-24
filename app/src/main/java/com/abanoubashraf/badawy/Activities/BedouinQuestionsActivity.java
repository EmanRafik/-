package com.abanoubashraf.badawy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abanoubashraf.badawy.R;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

public class BedouinQuestionsActivity extends AppCompatActivity {

    private TextView question_title, toolbar_text;
    private Button button_answer_1, button_answer_2, button_answer_3;
    private int score = 0, counter = 0;
    DotIndicator dotIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedouin_questions);

        toolbar_text = findViewById(R.id.toolbar_text);
        question_title = findViewById(R.id.questionTitle);
        button_answer_1 = findViewById(R.id.answerButton1);
        button_answer_2 = findViewById(R.id.answerButton2);
        button_answer_3 = findViewById(R.id.answerButton3);
        dotIndicator = findViewById(R.id.dotIndicator);

        toolbar_text.setText(R.string.verification_questions_ar);

        question_title.setText(R.string.q1_text);
        button_answer_1.setText(R.string.q1_a1);
        button_answer_2.setText(R.string.q1_a2);
        button_answer_3.setText(R.string.q1_a3);

    }

    public void onClick(View v) {
        switch (counter) {
            case 0: {
                if (v.getId() == R.id.answerButton1) {
                    score++;
                }
                question_title.setText(R.string.q2_text);
                button_answer_1.setText(R.string.q2_a1);
                button_answer_2.setText(R.string.q2_a2);
                button_answer_3.setText(R.string.q2_a3);
                dotIndicator.setSelectedItem(1, true);
                break;
            }
            case 1: {
                if (v.getId() == R.id.answerButton1) {
                    score++;
                }
                question_title.setText(R.string.q3_text);
                button_answer_1.setText(R.string.q3_a1);
                button_answer_2.setText(R.string.q3_a2);
                button_answer_3.setText(R.string.q3_a3);
                dotIndicator.setSelectedItem(2, true);
                break;
            }
            case 2: {
                if (v.getId() == R.id.answerButton3) {
                    score++;
                }
                if (score == 3) {
                    finishAffinity();
                    Intent intent = new Intent(BedouinQuestionsActivity.this, SignUpActivity.class);
                    intent.putExtra("type", "b");
                    startActivity(intent);
                } else {
                    finishAffinity();
                    Intent intent = new Intent(BedouinQuestionsActivity.this, SignInActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), R.string.bedauth_alert_message_failed, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        counter++;
    }

}
