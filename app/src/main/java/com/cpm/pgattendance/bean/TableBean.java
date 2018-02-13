package com.cpm.pgattendance.bean;

/**
 * Created by deepakp on 12/27/2017.
 */

public class TableBean {
    public static String getTable_JCPMaster() {
        return table_JCPMaster;
    }

    public static void setTable_JCPMaster(String table_JCPMaster) {
        TableBean.table_JCPMaster = table_JCPMaster;
    }

    public static String getNonworkingtable() {
        return nonworkingtable;
    }

    public static void setNonworkingtable(String nonworkingtable) {
        TableBean.nonworkingtable = nonworkingtable;
    }

    public static String getVisitorLogintable() {
        return visitorLogintable;
    }

    public static void setVisitorLogintable(String visitorLogintable) {
        TableBean.visitorLogintable = visitorLogintable;
    }

    public static String questiontable;

    public static String getQuestiontable() {
        return questiontable;
    }

    public static void setQuestiontable(String questiontable) {
        TableBean.questiontable = questiontable;
    }

    public static String getQuestionnaireTable() {
        return questionnaireTable;
    }

    public static void setQuestionnaireTable(String questionnaireTable) {
        TableBean.questionnaireTable = questionnaireTable;
    }

    public static String getSpecialActivityTable() {
        return specialActivityTable;
    }

    public static void setSpecialActivityTable(String specialActivityTable) {
        TableBean.specialActivityTable = specialActivityTable;
    }

    public static String getQuestionsTable() {
        return QuestionsTable;
    }

    public static void setQuestionsTable(String questionsTable) {
        QuestionsTable = questionsTable;
    }

    public static String getAnswersTable() {
        return AnswersTable;
    }

    public static void setAnswersTable(String answersTable) {
        AnswersTable = answersTable;
    }

    public static String AnswersTable;
    public static String QuestionsTable;
    public static String specialActivityTable;
    public static String questionnaireTable;
    public static String visitorLogintable;
    public static String nonworkingtable;
    public static String table_JCPMaster;

}
