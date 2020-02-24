package com.abanoubashraf.badawy.Questions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {
    Context mContext;
    List<Question> mData;
    private boolean loading;
    public QuestionsAdapter(Context mContext, List<Question> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.question_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.question.setText(mData.get(position).getQuestion());
       holder.num_of_answers.setText(String.valueOf(mData.get(position).getAnswers().size()));
       holder.tag.setText(mContext.getResources().getStringArray(R.array.heritage_categories)
               [Integer.parseInt(mData.get(position).getTag())]);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView question;
        TextView num_of_answers;
        TextView tag;
        LinearLayout show;
        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            num_of_answers = itemView.findViewById(R.id.num_of_answers);
            relativeLayout = itemView.findViewById(R.id.relative_layout_questions);
            tag = itemView.findViewById(R.id.tag);
            show = itemView.findViewById(R.id.show_question_layout);
            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ViewQuestionActivity.class);
                    int position = getAdapterPosition();
                    intent.putExtra("question",mData.get(position).getQuestion());
                    intent.putExtra("num_of_answers",String.valueOf(mData.get(position).getAnswers().size()));
                    intent.putExtra("tag", mContext.getResources().getStringArray(R.array.heritage_categories)
                            [Integer.parseInt(mData.get(position).getTag())]);
                    intent.putExtra("question_id",mData.get(position).getQuestion_id());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}

