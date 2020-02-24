package com.abanoubashraf.badawy.Questions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.Questions.AddAQuestion;
import com.abanoubashraf.badawy.Questions.Answer;
import com.abanoubashraf.badawy.Questions.Question;
import com.abanoubashraf.badawy.Questions.QuestionsAdapter;
import com.abanoubashraf.badawy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AskABedouinFragment extends Fragment {
    private TextView textView_no_info;
    private ProgressBar progress_loading;
    private RecyclerView recyclerView;
    private QuestionsAdapter questionsAdapter;
    private List<Question> mData;
    private FloatingActionButton fab;
    private DatabaseReference reff;
    private ValueEventListener listener;
    private Query query;
    private LinearLayout tabLayout;
    private Button all_questions, my_questions;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinner_adapter;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ask_a_bedouin, container, false);
        reff = FirebaseDatabase.getInstance().getReference().child("Questions");
        tabLayout = v.findViewById(R.id.tab_layout);
        all_questions = v.findViewById(R.id.all_questions_tab);
        my_questions = v.findViewById(R.id.my_questions_tab);
        recyclerView=v.findViewById(R.id.recycler_view);
        spinner = v.findViewById(R.id.spinner_ask_a_bedouin);
        fab=v.findViewById(R.id.fab);
        textView_no_info = v.findViewById(R.id.textView_no_info);
        progress_loading = v.findViewById(R.id.progress_loading);
        User user = SharedHelper.getSharedHelper(getContext()).getCurrentUser();

        //hiding add question btn for b
        if(user.getType().equals("b")){
            tabLayout.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }
        mData = new ArrayList<Question>();

        //fill spinner
        spinner_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.heritage_categories, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                progress_loading.setVisibility(View.VISIBLE);
                textView_no_info.setVisibility(View.GONE);
                query = reff;
                listener = query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mData.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Question question = new Question();
                            if(snapshot.child("question_id").getValue() != null){
                                question.setQuestion_id(snapshot.child("question_id").getValue().toString());
                            }
                            if(snapshot.child("question").getValue()!= null){
                                question.setQuestion(snapshot.child("question").getValue().toString());
                            }
                            if(snapshot.child("tag").getValue()!= null){
                                question.setTag(snapshot.child("tag").getValue().toString());
                            }
                            if(snapshot.child("user_id").getValue() != null){
                                question.setUser_id(snapshot.child("user_id").getValue().toString());
                            }

                            // read answers list
                            if(!snapshot.hasChild("answers")){
                                question.setAnswers(new ArrayList<Answer>());
                            }
                            else{
                                ArrayList<Answer> answersList=new ArrayList<Answer>();
                                DataSnapshot ss=snapshot.child("answers");
                                for(DataSnapshot shot : ss.getChildren()){
                                    Answer ans = shot.getValue(Answer.class);
                                    answersList.add(ans);
                                }
                                question.setAnswers(answersList);
                            }
                            if (spinner.getSelectedItemPosition() != 0) {
                                if (question.getTag().equals(String.valueOf(spinner.getSelectedItemPosition()))) {
                                    mData.add(question);
                                }
                            } else {
                                mData.add(question);
                            }
                            questionsAdapter.notifyDataSetChanged();
                        }
                        progress_loading.setVisibility(View.GONE);
                        if (mData.isEmpty()) {
                            textView_no_info.setVisibility(View.VISIBLE);
                        } else {
                            textView_no_info.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progress_loading.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        all_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = reff;
                listener = query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mData.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Question question = new Question();
                            if(snapshot.child("question_id").getValue() != null){
                                question.setQuestion_id(snapshot.child("question_id").getValue().toString());
                            }
                            if(snapshot.child("question").getValue()!= null){
                                question.setQuestion(snapshot.child("question").getValue().toString());
                            }
                            if(snapshot.child("tag").getValue()!= null){
                                question.setTag(snapshot.child("tag").getValue().toString());
                            }
                            if(snapshot.child("user_id").getValue() != null){
                                question.setUser_id(snapshot.child("user_id").getValue().toString());
                            }

                            // read answers list
                            if(!snapshot.hasChild("answers")){
                                question.setAnswers(new ArrayList<Answer>());
                            }
                            else{
                                ArrayList<Answer> answersList=new ArrayList<Answer>();
                                DataSnapshot ss=snapshot.child("answers");
                                for(DataSnapshot shot : ss.getChildren()){
                                    Answer ans = shot.getValue(Answer.class);
                                    answersList.add(ans);
                                }
                                question.setAnswers(answersList);
                            }
                            mData.add(question);
                            questionsAdapter.notifyDataSetChanged();
                        }
                        progress_loading.setVisibility(View.GONE);
                        if (mData.isEmpty()) {
                            textView_no_info.setVisibility(View.VISIBLE);
                        } else {
                            textView_no_info.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progress_loading.setVisibility(View.GONE);
                    }
                });

                all_questions.setBackgroundResource(R.drawable.rounded_button_foshya_bg);
                my_questions.setBackgroundResource(R.drawable.rounded_button_black_overlay_bg);
            }
        });

        my_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = reff.orderByChild("user_id").equalTo(
                        SharedHelper.getSharedHelper(getContext()).getCurrentUser().getId());
                listener = query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mData.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Question question = new Question();
                            if(snapshot.child("question_id").getValue() != null){
                                question.setQuestion_id(snapshot.child("question_id").getValue().toString());
                            }
                            if(snapshot.child("question").getValue()!= null){
                                question.setQuestion(snapshot.child("question").getValue().toString());
                            }
                            if(snapshot.child("tag").getValue()!= null){
                                question.setTag(snapshot.child("tag").getValue().toString());
                            }
                            if(snapshot.child("user_id").getValue() != null){
                                question.setUser_id(snapshot.child("user_id").getValue().toString());
                            }

                            // read answers list
                            if(!snapshot.hasChild("answers")){
                                question.setAnswers(new ArrayList<Answer>());
                            }
                            else{
                                ArrayList<Answer> answersList=new ArrayList<Answer>();
                                DataSnapshot ss=snapshot.child("answers");
                                for(DataSnapshot shot : ss.getChildren()){
                                    Answer ans = shot.getValue(Answer.class);
                                    answersList.add(ans);
                                }
                                question.setAnswers(answersList);
                            }
                            mData.add(question);
                            questionsAdapter.notifyDataSetChanged();
                        }
                        progress_loading.setVisibility(View.GONE);
                        if (mData.isEmpty()) {
                            textView_no_info.setVisibility(View.VISIBLE);
                        } else {
                            textView_no_info.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progress_loading.setVisibility(View.GONE);
                    }
                });

                all_questions.setBackgroundResource(R.drawable.rounded_button_black_overlay_bg);
                my_questions.setBackgroundResource(R.drawable.rounded_button_foshya_bg);
            }
        });

        progress_loading.setVisibility(View.VISIBLE);
        textView_no_info.setVisibility(View.GONE);
        query = reff;
        listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Question question = new Question();
                    if(snapshot.child("question_id").getValue() != null){
                        question.setQuestion_id(snapshot.child("question_id").getValue().toString());
                    }
                    if(snapshot.child("question").getValue()!= null){
                        question.setQuestion(snapshot.child("question").getValue().toString());
                    }
                    if(snapshot.child("tag").getValue()!= null){
                        question.setTag(snapshot.child("tag").getValue().toString());
                    }
                    if(snapshot.child("user_id").getValue() != null){
                        question.setUser_id(snapshot.child("user_id").getValue().toString());
                    }

                    // read answers list
                    if(!snapshot.hasChild("answers")){
                        question.setAnswers(new ArrayList<Answer>());
                    }
                    else{
                        ArrayList<Answer> answersList=new ArrayList<Answer>();
                        DataSnapshot ss=snapshot.child("answers");
                        for(DataSnapshot shot : ss.getChildren()){
                            Answer ans = shot.getValue(Answer.class);
                            answersList.add(ans);
                        }
                        question.setAnswers(answersList);
                    }
                    mData.add(question);
                    questionsAdapter.notifyDataSetChanged();
                }
                progress_loading.setVisibility(View.GONE);
                if (mData.isEmpty()) {
                    textView_no_info.setVisibility(View.VISIBLE);
                } else {
                    textView_no_info.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress_loading.setVisibility(View.GONE);
            }
        });

        questionsAdapter = new QuestionsAdapter(getContext(),mData);
        recyclerView.setAdapter(questionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAQuestion.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return v;
    }

}
