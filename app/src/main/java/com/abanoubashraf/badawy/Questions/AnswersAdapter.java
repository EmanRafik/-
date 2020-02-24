package com.abanoubashraf.badawy.Questions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.MyViewHolder> {

    Context mContext;
    List<Answer> mData;
    User user;

    public AnswersAdapter(Context mContext, List<Answer> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = SharedHelper.getSharedHelper(mContext).getCurrentUser();
        View row = LayoutInflater.from(mContext).inflate(R.layout.answer_item,parent,false);
        return new AnswersAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswersAdapter.MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.comment_text.setText(mData.get(position).getComment_text());
        holder.user_name.setText(mData.get(position).getUser_name());
        holder.num_of_votes.setText(String.valueOf(mData.get(position).getNum_of_votes()));
        if(mData.get(position).getUpvotes()!= null){
            holder.upvote.setImageResource(mData.get(position).getUpvotes().contains(user.getId())? R.drawable.thumbs_up_filled  : R.drawable.thumbs_up_not_filled);
        }
        if(mData.get(position).getDownvotes() != null){
            holder.downvote.setImageResource(mData.get(position).getDownvotes().contains(user.getId())? R.drawable.thumbs_down_filled  : R.drawable.thumbs_down_not_filled);
        }
        //verified comment
        if(!mData.get(position).isVerified()){
            holder.is_verified.setVisibility(View.GONE);
        }

        // comment contains pic or not
        if(mData.get(position).getComment_pic().equals("default")){
            holder.cardView_answer_image.setVisibility(View.GONE);
            holder.comment_pic.setVisibility(View.GONE);
        }
        else{
            Glide.with(mContext).load(mData.get(position).getComment_pic().toString()).into(holder.comment_pic);
        }

        // set profile picture
        if(mData.get(position).getProfile_pic().equals("default")){
            holder.profile_pic.setImageResource(R.drawable.default_pp);
        } else {
            Glide.with(mContext).load(mData.get(position).getProfile_pic()).into(holder.profile_pic);
        }

        //show or hide upvote and downvote
        if(user.getType().equals("nb")){
            holder.upvote.setVisibility(View.GONE);
            holder.downvote.setVisibility(View.GONE);
        } else {
            holder.textView_votes_mum.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView is_verified, comment_pic, profile_pic;
        TextView comment_text, user_name, num_of_votes, textView_votes_mum;
        ImageView downvote,upvote;
        CardView cardView_answer_image;
        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //init
            is_verified = itemView.findViewById(R.id.is_verified);
            comment_pic = itemView.findViewById(R.id.comment_pic);
            profile_pic = itemView.findViewById(R.id.answer_profile_pic);
            comment_text = itemView.findViewById(R.id.comment_text);
            user_name = itemView.findViewById(R.id.comment_user_name);
            num_of_votes = itemView.findViewById(R.id.num_of_votes);
            textView_votes_mum = itemView.findViewById(R.id.textView_votes_mum);
            upvote = itemView.findViewById(R.id.upvote);
            downvote = itemView.findViewById(R.id.downvote);
            cardView_answer_image = itemView.findViewById(R.id.cardView_answer_image);
            relativeLayout = itemView.findViewById(R.id.comment_layout);

            //handling upvotes and multiple clicks
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ArrayList<String> upvotes=mData.get(position).getUpvotes();
                    ArrayList<String> downvotes=mData.get(position).getDownvotes();
                    if(upvotes == null){
                        upvotes = new ArrayList<String>();
                    }
                    if(downvotes == null) {
                        downvotes = new ArrayList<String>();
                    }
                    int votes = Integer.parseInt(mData.get(position).getNum_of_votes());
                    if(!upvotes.contains(user.getId())){
                        upvotes.add(user.getId());
                        if(downvotes.contains(user.getId())){
                            votes++;
                            downvotes.remove(user.getId());
                        }
                        upvote.setImageResource(R.drawable.thumbs_up_filled);
                        downvote.setImageResource(R.drawable.thumbs_down_not_filled);
                        votes++;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,mData.get(position).getQuestion_id(),mData.get(position).getAnswer_id(),upvotes , downvotes);
                    }
                    else{
                        upvote.setImageResource(R.drawable.thumbs_up_not_filled);
                        upvotes.remove(user.getId());
                        votes--;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,mData.get(position).getQuestion_id(),mData.get(position).getAnswer_id(),upvotes , downvotes);
                    }
                }
            });
            // handling downvotes and multiple clicks
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ArrayList<String> upvotes=mData.get(position).getUpvotes();
                    ArrayList<String> downvotes=mData.get(position).getDownvotes();
                    if(upvotes == null){
                        upvotes = new ArrayList<String>();
                    }
                    if(downvotes == null) {
                        downvotes = new ArrayList<String>();
                    }
                    int votes = Integer.parseInt(mData.get(position).getNum_of_votes());
                    if(!downvotes.contains(user.getId())){
                        downvotes.add(user.getId());
                        if(upvotes.contains(user.getId())){
                            votes--;
                            upvotes.remove(user.getId());
                        }
                        upvote.setImageResource(R.drawable.thumbs_up_not_filled);
                        downvote.setImageResource(R.drawable.thumbs_down_filled);
                        votes--;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,mData.get(position).getQuestion_id(),mData.get(position).getAnswer_id(),upvotes , downvotes);
                    }
                    else{
                        downvote.setImageResource(R.drawable.thumbs_down_not_filled);
                        downvotes.remove(user.getId());
                        votes++;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,mData.get(position).getQuestion_id(),mData.get(position).getAnswer_id(),upvotes , downvotes);
                    }
                }
            });
        }
    }

    //update votes in firebase database
    public void updateVotes(int votes, String question_id, String answer_id, ArrayList<String> upvotes, ArrayList<String> downvotes){
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Questions").child(question_id).child("answers").child(answer_id);
        reff.child("num_of_votes").setValue(String.valueOf(votes));
        reff.child("upvotes").setValue(upvotes);
        reff.child("downvotes").setValue(downvotes);
        notifyDataSetChanged();
    }
}
