package com.abanoubashraf.badawy.Community;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapterNew extends RecyclerView.Adapter<CommentsAdapterNew.MyViewHolder>{

    Context mContext;
    List<CommentNew> mData;
    User user;
    String post_id;
    MediaPlayer mediaPlayer;
    int count;

    public CommentsAdapterNew(Context mContext, List<CommentNew> mData, String post_id) {
        this.mContext = mContext;
        this.mData = mData;
        this.post_id=post_id;
    }

    @NonNull
    @Override
    public CommentsAdapterNew.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = SharedHelper.getSharedHelper(mContext).getCurrentUser();
        View row = LayoutInflater.from(mContext).inflate(R.layout.comment_item,parent,false);
        return new CommentsAdapterNew.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapterNew.MyViewHolder holder, int position) {

                holder.setIsRecyclable(false);
                holder.comment_text.setText(mData.get(position).getComment());
                holder.user_name.setText(mData.get(position).getUser_name());
                holder.num_of_votes.setText(String.valueOf(mData.get(position).getNum_of_votes()));

        if(mData.get(position).getUpvotes()!= null){
            holder.upvote.setImageResource(mData.get(position).getUpvotes().contains(user.getId())? R.drawable.thumbs_up_filled  : R.drawable.thumbs_up_not_filled);
        }
        if(mData.get(position).getDownvotes() != null){
            holder.downvote.setImageResource(mData.get(position).getDownvotes().contains(user.getId())? R.drawable.thumbs_down_filled  : R.drawable.thumbs_down_not_filled);
        }

                //verified post
                if(!mData.get(position).isVerified()){
                    holder.is_verified.setVisibility(View.GONE);
                }

                // set profile picture
                if(mData.get(position).getProfile_pic().equals("default")){
                    holder.profile_pic.setImageResource(R.drawable.default_pp);
                } else {
                    Glide.with(mContext).load(mData.get(position).getProfile_pic()).into(holder.profile_pic);
                }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView is_verified, comment_pic, profile_pic, downvote,upvote;
        TextView comment_text, user_name, num_of_votes;
        LinearLayout audio_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //init
            is_verified = itemView.findViewById(R.id.is_verified_comment);
            //comment_pic = itemView.findViewById(R.id.comment_image);
            profile_pic = itemView.findViewById(R.id.comment_user_img);
            comment_text = itemView.findViewById(R.id.comment_content);
            user_name = itemView.findViewById(R.id.comment_user_name);
            num_of_votes = itemView.findViewById(R.id.comment_votes_count);
            upvote = itemView.findViewById(R.id.comment_upvote_button);
            downvote = itemView.findViewById(R.id.comment_downvote_button);


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
                        updateVotes(votes,post_id,upvotes , downvotes, mData.get(position).getComment_id());
                    }
                    else{
                        upvote.setImageResource(R.drawable.thumbs_up_not_filled);
                        upvotes.remove(user.getId());
                        votes--;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,post_id,upvotes , downvotes, mData.get(position).getComment_id());
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
                        updateVotes(votes,post_id, upvotes, downvotes, mData.get(position).getComment_id());
                    }
                    else{
                        downvote.setImageResource(R.drawable.thumbs_down_not_filled);
                        downvotes.remove(user.getId());
                        votes++;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,post_id, upvotes, downvotes, mData.get(position).getComment_id());
                    }
                }
            });
        }
    }

    //update votes in firebase database

    public void updateVotes(int votes, String post_id, ArrayList<String> upvotes, ArrayList<String> downvotes, String comment_id){
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_id).child("comments").child(comment_id);
        reff.child("num_of_votes").setValue(String.valueOf(votes));
        reff.child("upvotes").setValue(upvotes);
        reff.child("downvotes").setValue(downvotes);
        notifyDataSetChanged();
    }
}
