package com.cpm.pgattendance.getterSetter;

import java.util.ArrayList;

/**
 * Created by deepakp on 23-01-2018.
 */

public class QuestionsGetterSetter {
    public String getTable_QUESTIONS() {
        return table_QUESTIONS;
    }

    public void setTable_QUESTIONS(String table_QUESTIONS) {
        this.table_QUESTIONS = table_QUESTIONS;
    }

    public ArrayList<String> getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(String QUESTION_ID) {
        this.QUESTION_ID.add(QUESTION_ID);
    }

    public ArrayList<String> getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION.add(QUESTION);
    }

    public ArrayList<String> getQUESTION_GROUP_ID() {
        return QUESTION_GROUP_ID;
    }

    public void setQUESTION_GROUP_ID(String QUESTION_GROUP_ID) {
        this.QUESTION_GROUP_ID.add(QUESTION_GROUP_ID);
    }

    public ArrayList<String> getQUESTION_GROUP() {
        return QUESTION_GROUP;
    }

    public void setQUESTION_GROUP(String QUESTION_GROUP) {
        this.QUESTION_GROUP.add(QUESTION_GROUP);
    }

    public ArrayList<String> getQUESTION_TYPE() {
        return QUESTION_TYPE;
    }

    public void setQUESTION_TYPE(String QUESTION_TYPE) {
        this.QUESTION_TYPE.add(QUESTION_TYPE);
    }

    String table_QUESTIONS;
    ArrayList<String> QUESTION_ID = new ArrayList<>();
    ArrayList<String> QUESTION = new ArrayList<>();
    ArrayList<String> QUESTION_GROUP_ID = new ArrayList<>();
    ArrayList<String> QUESTION_GROUP = new ArrayList<>();
    ArrayList<String> QUESTION_TYPE = new ArrayList<>();
}
