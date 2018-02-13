package com.cpm.pgattendance.getterSetter;

import java.util.ArrayList;

/**
 * Created by deepakp on 23-01-2018.
 */

public class AnswersGetterSetter {

    String table_ANSWERS;
    ArrayList<String> QUESTION_ID = new ArrayList<>();
    ArrayList<String> ANSWER_ID = new ArrayList<>();
    ArrayList<String> ANSWER = new ArrayList<>();

    public String getTable_ANSWERS() {
        return table_ANSWERS;
    }

    public void setTable_ANSWERS(String table_ANSWERS) {
        this.table_ANSWERS = table_ANSWERS;
    }

    public ArrayList<String> getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(String QUESTION_ID) {
        this.QUESTION_ID.add(QUESTION_ID);
    }

    public ArrayList<String> getANSWER_ID() {
        return ANSWER_ID;
    }

    public void setANSWER_ID(String ANSWER_ID) {
        this.ANSWER_ID.add(ANSWER_ID);
    }

    public ArrayList<String> getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER.add(ANSWER);
    }
}
