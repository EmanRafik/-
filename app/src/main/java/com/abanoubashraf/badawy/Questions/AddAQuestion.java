package com.abanoubashraf.badawy.Questions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.Activities.HomeActivity;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddAQuestion extends AppCompatActivity {
    private EditText question_txt;
    private Button submitButton;
    private Question question;
    private DatabaseReference reff;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinner_adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aquestion);

        //init
        question_txt = findViewById(R.id.question_txt);
        submitButton = findViewById(R.id.button_submit_question);
        spinner = findViewById(R.id.spinner);
        user = SharedHelper.getSharedHelper(getApplicationContext()).getCurrentUser();

        //fill spinner
        spinner_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.heritage_categories, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        //firebase reference
        reff= FirebaseDatabase.getInstance().getReference().child("Questions");
        question=new Question();
    }
    //handling submit button click action
    public void submitClicked(View view){
        if(validate_data()){
            question.setUser_id(user.getId());
            question.setQuestion(question_txt.getText().toString());
            question.setAnswers(new ArrayList<Answer>());
            question.setTag(String.valueOf(spinner.getSelectedItemPosition()));
//            Toast.makeText(this, ,Toast.LENGTH_SHORT).show();
            String question_id = reff.push().getKey();
            reff.child(question_id).setValue(question);
            reff.child(question_id).child("question_id").setValue(question_id);
            Intent intent = new Intent(AddAQuestion.this, HomeActivity.class);
            intent.putExtra("type", "nb");
            startActivity(intent);
            finish();
        }
    }
    private boolean validate_data () {
        if (TextUtils.isEmpty(question_txt.getText().toString().trim())){
            Toast.makeText(this, getText(R.string.enter_a_question),Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this,getText(R.string.select_a_tag),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
