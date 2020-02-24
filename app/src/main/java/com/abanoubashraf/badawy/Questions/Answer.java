package com.abanoubashraf.badawy.Questions;

import java.util.ArrayList;

public class Answer {
    private String user_name;
    private String comment_text;
    private String comment_pic;
    private String profile_pic;
    private String answer_id;
    private String question_id;
    private String num_of_votes;
    private boolean verified;
    private ArrayList<String> upvotes;
    private ArrayList<String> downvotes;

    public Answer(String user_name, String comment_text, String comment_pic, String num_of_votes,
                  boolean is_verified, ArrayList<String> upvotes,
                  ArrayList<String> downvotes, String answer_id, String question_id, String profile_pic) {
        this.user_name = user_name;
        this.comment_text = comment_text;
        this.comment_pic = comment_pic;
        this.num_of_votes = num_of_votes;
        this.verified = is_verified;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.profile_pic = profile_pic;
    }
    public Answer() {

    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_pic() {
        return comment_pic;
    }

    public void setComment_pic(String comment_pic) {
        this.comment_pic = comment_pic;
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

    public void setVerified(boolean is_verified) {
        this.verified = is_verified;
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

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

}
