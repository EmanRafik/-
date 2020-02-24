package com.abanoubashraf.badawy.Community;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.ChooseSpecialists.User;
import com.abanoubashraf.badawy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostsAdapterNew extends RecyclerView.Adapter<PostsAdapterNew.MyViewHolder>{

    Context mContext;
    List<PostNew> mData;
    List<CommentNew> mDataComments;
    User user;
    boolean isExpanded;
    DatabaseReference reff;
    CommentsAdapterNew commentsAdapter;
    int count;
    List<Boolean> isExpandedList;
    boolean isHeritage;

    public PostsAdapterNew(Context mContext, List<PostNew> mData, boolean isHeritage) {
        this.mContext = mContext;
        this.mData = mData;
        this.isExpandedList = new ArrayList<Boolean>(mData.size());
        this.mDataComments = new ArrayList<CommentNew>();
        this.reff = FirebaseDatabase.getInstance().getReference().child("Posts");
        this.count=0;
        this.isHeritage = isHeritage;
    }

    @NonNull
    @Override
    public PostsAdapterNew.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = SharedHelper.getSharedHelper(mContext).getCurrentUser();
        View row = LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new PostsAdapterNew.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsAdapterNew.MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        while (isExpandedList.size()<mData.size()){
            isExpandedList.add(false);
        }
        mDataComments = new ArrayList<>(mData.get(position).getComments());
        String post_img= String.valueOf(mData.get(position).getPost_img());
        commentsAdapter = new CommentsAdapterNew(mContext,mDataComments,mData.get(position).getPost_id());
        holder.recyclerView_comments.setAdapter(commentsAdapter);
        holder.recyclerView_comments.setLayoutManager(new LinearLayoutManager(mContext));
        holder.post_text.setText(mData.get(position).getPost());
        holder.user_name.setText(mData.get(position).getUser_name());
        holder.num_of_votes.setText(String.valueOf(mData.get(position).getNum_of_votes()));
        holder.num_of_comments.setText(String.valueOf(mData.get(position).getComments().size()));
        holder.expandable_comments_layout.setVisibility(isExpandedList.get(position)? View.VISIBLE : View.GONE);
        if(mData.get(position).getUpvotes()!= null){
            holder.upvote.setImageResource(mData.get(position).getUpvotes().contains(user.getId())? R.drawable.thumbs_up_filled  : R.drawable.thumbs_up_not_filled);
        }
        if(mData.get(position).getDownvotes() != null){
            holder.downvote.setImageResource(mData.get(position).getDownvotes().contains(user.getId())? R.drawable.thumbs_down_filled  : R.drawable.thumbs_down_not_filled);
        }
        count++;
        //setting comment profile pic
         String image_URI = user.getImage_URL();
          if (image_URI.equals("default")) {
           holder.make_comment_user_img.setImageResource(R.drawable.default_pp);
              } else {
                  Glide.with(mContext).load(image_URI).into(holder.make_comment_user_img);
              }

        //verified post
        if(!mData.get(position).isVerified()){
            holder.is_verified.setVisibility(View.GONE);
        }

        // post contains pic or not
        if(post_img.equals("default")){
            holder.post_pic.setVisibility(View.GONE);
        }
        else{
            Glide.with(mContext).load(post_img).into(holder.post_pic);
        }

        // set post profile picture
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
        ImageView is_verified, post_pic, profile_pic, downvote,upvote,make_comment_user_img, make_comment_img, choose_img, send_comment, expand_arrow;
        TextView post_text, user_name, num_of_votes, num_of_comments, comment;
        EditText make_comment_text;
        Button play,pause;
        RecyclerView recyclerView_comments;
        RelativeLayout expandable_comments_layout;
        LinearLayout show_comments_layout, linear_layout_top;
        CardView cardView_bottom;

        LinearLayout audio_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //init
            is_verified = itemView.findViewById(R.id.is_verified_post);
            post_pic = itemView.findViewById(R.id.post_image);
            profile_pic = itemView.findViewById(R.id.post_user_img);
            post_text = itemView.findViewById(R.id.post_content);
            user_name = itemView.findViewById(R.id.post_user_name);
            num_of_votes = itemView.findViewById(R.id.post_votes_count);
            num_of_comments = itemView.findViewById(R.id.post_comments_count);
            upvote = itemView.findViewById(R.id.post_upvote_button);
            downvote = itemView.findViewById(R.id.post_downvote_button);
            comment = itemView.findViewById(R.id.post_comment_button);
            recyclerView_comments = itemView.findViewById(R.id.recyclerview_comments);
            expandable_comments_layout = itemView.findViewById(R.id.expandable_comments_layout);
            show_comments_layout = itemView.findViewById(R.id.show_comments_layout);
            make_comment_user_img = itemView.findViewById(R.id.make_comment_user_img);
            make_comment_img = itemView.findViewById(R.id.make_comment_pic);
            send_comment = itemView.findViewById(R.id.comment_send_btn);
            make_comment_text = itemView.findViewById(R.id.make_comment_edit_text);
            expand_arrow = itemView.findViewById(R.id.show_comments_arrow);
            linear_layout_top = itemView.findViewById(R.id.linear_layout_top);
            cardView_bottom = itemView.findViewById(R.id.cardView_bottom);

            if (isHeritage) {
                linear_layout_top.setVisibility(View.GONE);
                cardView_bottom.setVisibility(View.GONE);
            }

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
                        updateVotes(votes,mData.get(position).getPost_id(),upvotes , downvotes);
                    }
                    else{
                        upvote.setImageResource(R.drawable.thumbs_up_not_filled);
                        upvotes.remove(user.getId());
                        votes--;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,mData.get(position).getPost_id(),upvotes , downvotes);
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
                        updateVotes(votes,mData.get(position).getPost_id(), upvotes, downvotes);
                    }
                    else{
                        downvote.setImageResource(R.drawable.thumbs_down_not_filled);
                        downvotes.remove(user.getId());
                        votes++;
                        mData.get(position).setNum_of_votes(String.valueOf(votes));
                        num_of_votes.setText(String.valueOf(votes));
                        updateVotes(votes,mData.get(position).getPost_id(), upvotes, downvotes);
                    }
                }
            });

            //expanding the post

            show_comments_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("post image is: ","------------------------------------");
                    int position = getAdapterPosition();
                    isExpandedList.set(position, !isExpandedList.get(position));
                    mDataComments = mData.get(position).getComments();
                    notifyDataSetChanged();

                }
            });

            //send a new comment

            send_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNewComment(MyViewHolder.this, getAdapterPosition());
                }
            });
        }
    }

    //update votes in firebase database

    public void updateVotes(int votes, String post_id, ArrayList<String> upvotes, ArrayList<String> downvotes){
        reff.child(post_id).child("num_of_votes").setValue(String.valueOf(votes));
        reff.child(post_id).child("upvotes").setValue(upvotes);
        reff.child(post_id).child("downvotes").setValue(downvotes);
        notifyDataSetChanged();
    }

    //add new comment

    public void addNewComment(PostsAdapterNew.MyViewHolder holder, int position){
        CommentNew comment = new CommentNew();
        comment.setUser_id(user.getId());
        comment.setComment(holder.make_comment_text.getText().toString());
        comment.setUser_name(user.getUsername());
        comment.setNum_of_votes("0");
        comment.setProfile_pic(user.getImage_URL());
        comment.setVerified(user.is_verified());
        comment.setComment_img("default");
        comment.setComment_audio("default");
        String comment_id = reff.child(mData.get(position).getPost_id()).child("comments").push().getKey();
        reff.child(mData.get(position).getPost_id()).child("comments").child(comment_id).setValue(comment);
        reff.child(mData.get(position).getPost_id()).child("comments").child(comment_id).child("comment_id").setValue(comment_id);
        holder.make_comment_text.getText().clear();
        commentsAdapter.notifyDataSetChanged();
    }
}
