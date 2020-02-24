package com.abanoubashraf.badawy.Community;

import java.util.ArrayList;
import java.util.List;

public class PostNew {

    private String post;
    private List<CommentNew> comments;
    private String tag;
    private String post_id;
    private String user_id;
    private String post_img;
    private String post_audio;
    private String user_name;
    private String profile_pic;
    private String num_of_votes;
    private String num_of_comments;
    private boolean verified;
    private ArrayList<String> upvotes;
    private ArrayList<String> downvotes;

    public PostNew(String question,List<CommentNew> comments,String tag, String post_id, String user_id,
                   String post_img, String post_audio,String user_name, String profile_pic, String num_of_votes,
                   boolean verified, ArrayList<String> upvotes, ArrayList<String> downvotes, String num_of_comments){
        this.post=question;
        this.comments = comments;
        this.tag=tag;
        this.post_id = post_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.profile_pic = profile_pic;
        this.num_of_votes = num_of_votes;
        this.verified = verified;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.num_of_comments = num_of_comments;
    }
    public PostNew(){

    }
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<CommentNew> getComments() {
        return comments;
    }

    public void setComments(List<CommentNew> comments) {
        this.comments = comments;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    public String getPost_audio() {
        return post_audio;
    }

    public void setPost_audio(String post_audio) {
        this.post_audio = post_audio;
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

    public String getNum_of_comments() {
        return num_of_comments;
    }

    public void setNum_of_comments(String num_of_comments) {
        this.num_of_comments = num_of_comments;
    }





}
