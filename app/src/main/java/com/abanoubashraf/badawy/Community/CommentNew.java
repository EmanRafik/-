package com.abanoubashraf.badawy.Community;

import java.util.ArrayList;

public class CommentNew {
    private String comment;
    private String comment_id;
    private String user_id;
    private String comment_img;
    private String comment_audio;
    private String user_name;
    private String profile_pic;
    private String num_of_votes;
    private boolean verified;
    private ArrayList<String> upvotes;
    private ArrayList<String> downvotes;

    public CommentNew(String comment, String comment_id, String user_id, String comment_img,
                      String comment_audio, String user_name, String profile_pic, String num_of_votes,
                      boolean verified, ArrayList<String> upvotes, ArrayList<String> downvotes) {
        this.comment = comment;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.comment_img = comment_img;
        this.comment_audio = comment_audio;
        this.user_name = user_name;
        this.profile_pic = profile_pic;
        this.num_of_votes = num_of_votes;
        this.verified = verified;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public CommentNew(){

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment_img() {
        return comment_img;
    }

    public void setComment_img(String comment_img) {
        this.comment_img = comment_img;
    }

    public String getComment_audio() {
        return comment_audio;
    }

    public void setComment_audio(String comment_audio) {
        this.comment_audio = comment_audio;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getNum_of_votes() {
        return num_of_votes;
    }

    public void setNum_of_votes(String num_of_votes) {
        this.num_of_votes = num_of_votes;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public ArrayList<String> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(ArrayList<String> upvotes) {
        this.upvotes = upvotes;
    }

    public ArrayList<String> getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(ArrayList<String> downvotes) {
        this.downvotes = downvotes;
    }
}
