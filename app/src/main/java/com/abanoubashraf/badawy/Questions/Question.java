package com.abanoubashraf.badawy.Questions;

import java.util.List;

public class Question {

    private String question;
    private List<Answer> answers;
    private String tag;
    private String question_id;
    private String user_id;

    public Question(String question,List<Answer> answers,String tag, String question_id, String user_id){
        this.question=question;
        this.answers=answers;
        this.tag=tag;
        this.question_id = question_id;
        this.user_id = user_id;
    }
    public Question(){

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
