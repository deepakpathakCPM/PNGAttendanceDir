package com.cpm.pgattendance.xmlHandler;

import com.cpm.pgattendance.getterSetter.AnswersGetterSetter;
import com.cpm.pgattendance.getterSetter.FailureGetterSetter;
import com.cpm.pgattendance.getterSetter.JCPMasterGetterSetter;
import com.cpm.pgattendance.getterSetter.LoginGetterSetter;
import com.cpm.pgattendance.getterSetter.NonWorkingReasonGetterSetter;
import com.cpm.pgattendance.getterSetter.QuestionGetterSetter;
import com.cpm.pgattendance.getterSetter.QuestionnaireGetterSetter;
import com.cpm.pgattendance.getterSetter.QuestionsGetterSetter;
import com.cpm.pgattendance.getterSetter.SpecialActivityGetterSetter;
import com.cpm.pgattendance.getterSetter.VisitorLoginGetterSetter;
import com.cpm.pgattendance.getterSetter.VisitorSearchGetterSetter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by deepakp on 12/26/2017.
 */

public class XMLHandlers {

    public static FailureGetterSetter failureXMLHandler(XmlPullParser xpp, int eventType) {
        FailureGetterSetter failureGetterSetter = new FailureGetterSetter();
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("STATUS")) {
                        failureGetterSetter.setStatus(xpp.nextText());
                    }
                    if (xpp.getName().equals("ERRORMSG")) {
                        failureGetterSetter.setErrorMsg(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return failureGetterSetter;
    }

    public static LoginGetterSetter loginXMLHandler(XmlPullParser xpp, int eventType) {
        LoginGetterSetter lgs = new LoginGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("SUCCESS")) {
                        lgs.setResult(xpp.nextText());
                    }
                    if (xpp.getName().equals("APP_VERSION")) {
                        lgs.setVERSION(xpp.nextText());
                    }
                    if (xpp.getName().equals("APP_PATH")) {
                        lgs.setPATH(xpp.nextText());
                    }
                    if (xpp.getName().equals("CURRENTDATE")) {
                        lgs.setDATE(xpp.nextText());
                    }

                    if (xpp.getName().equals("RIGHTNAME")) {
                        lgs.setRIGHTNAME(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lgs;
    }


    public static JCPMasterGetterSetter JCPMasterXMLHandler(XmlPullParser xpp, int eventType) {
        JCPMasterGetterSetter jcpGetterSetter = new JCPMasterGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        jcpGetterSetter.setTable_JOURNEY_PLAN(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORE_CD")) {
                        jcpGetterSetter.setSTORE_CD(xpp.nextText());
                    }
                    if (xpp.getName().equals("EMP_CD")) {
                        jcpGetterSetter.setEMP_CD(xpp.nextText());
                    }
                    if (xpp.getName().equals("USERNAME")) {
                        jcpGetterSetter.setUSERNAME(xpp.nextText());
                    }
                    if (xpp.getName().equals("VISIT_DATE")) {
                        jcpGetterSetter.setVISIT_DATE(xpp.nextText());
                    }
                    if (xpp.getName().equals("KEYACCOUNT")) {
                        jcpGetterSetter.setKEYACCOUNT(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORENAME")) {
                        jcpGetterSetter.setSTORENAME(xpp.nextText());
                    }
                    if (xpp.getName().equals("CITY")) {
                        jcpGetterSetter.setCITY(xpp.nextText());
                    }
                    if (xpp.getName().equals("STORETYPE")) {
                        jcpGetterSetter.setSTORETYPE(xpp.nextText());
                    }
                    if (xpp.getName().equals("UPLOAD_STATUS")) {
                        jcpGetterSetter.setUPLOAD_STATUS(xpp.nextText());
                    }
                    if (xpp.getName().equals("CHECKOUT_STATUS")) {
                        jcpGetterSetter.setCHECKOUT_STATUS(xpp.nextText());
                    }
                    if (xpp.getName().equals("REGION_CD")) {
                        jcpGetterSetter.setREGION_CD(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jcpGetterSetter;
    }

    public static NonWorkingReasonGetterSetter nonWorkinReasonXML(XmlPullParser xpp,
                                                                  int eventType) {
        NonWorkingReasonGetterSetter nonworking = new NonWorkingReasonGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        nonworking.setNonworking_table(xpp.nextText());
                    }
                    if (xpp.getName().equals("REASON_CD")) {
                        nonworking.setReason_cd(xpp.nextText());
                    }
                    if (xpp.getName().equals("REASON")) {
                        nonworking.setReason(xpp.nextText());
                    }
                    if (xpp.getName().equals("FOR_ATT")) {
                        nonworking.setFOR_ATT(xpp.nextText());
                    }
                    if (xpp.getName().equals("FOR_STORE")) {
                        nonworking.setFOR_STORE(xpp.nextText());
                    }
                    if (xpp.getName().equals("ENTRY_ALLOW")) {
                        nonworking.setEntry_allow(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return nonworking;
    }


    public static VisitorLoginGetterSetter visitorLoginXMLHandler(XmlPullParser xpp, int eventType) {
        VisitorLoginGetterSetter visitorLoginGetterSetter = new VisitorLoginGetterSetter();
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        visitorLoginGetterSetter.setTable_VisitorLogin(xpp.nextText());
                    }
                    if (xpp.getName().equals("EMP_CD")) {
                        visitorLoginGetterSetter.setEMP_CD(xpp.nextText());
                    }
                    if (xpp.getName().equals("NAME")) {
                        visitorLoginGetterSetter.setNAME(xpp.nextText());
                    }
                    if (xpp.getName().equals("DESIGNATION")) {
                        visitorLoginGetterSetter.setDESIGNATION(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return visitorLoginGetterSetter;
    }

    public static QuestionGetterSetter QuestionXMLHandler(XmlPullParser xpp, int eventType) {
        QuestionGetterSetter qnsGetterSetter = new QuestionGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("META_DATA")) {
                        qnsGetterSetter.setTable_question_today(xpp.nextText());
                    }

                    if (xpp.getName().equals("QUESTION_ID")) {
                        qnsGetterSetter.setQuestion_cd(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION")) {
                        qnsGetterSetter.setQuestion(xpp.nextText());
                    }
                    if (xpp.getName().equals("ANSWER_ID")) {
                        qnsGetterSetter.setAnswer_cd(xpp.nextText());
                    }
                    if (xpp.getName().equals("ANSWER")) {
                        qnsGetterSetter.setAnswer(xpp.nextText());
                    }
                    if (xpp.getName().equals("RIGHT_ANSWER")) {
                        qnsGetterSetter.setRight_answer(xpp.nextText());
                    }
                    if (xpp.getName().equals("STATUS")) {
                        qnsGetterSetter.setStatus(xpp.nextText());
                    }

                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return qnsGetterSetter;
    }

    public static VisitorSearchGetterSetter visitorDataXML(XmlPullParser xpp,
                                                           int eventType) {
        VisitorSearchGetterSetter displaytype = new VisitorSearchGetterSetter();

        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        displaytype.setTable_VISITOR_SEARCH(xpp.nextText());
                    }
                    if (xpp.getName().equals("EMP_CD")) {
                        displaytype.setEMP_CD(xpp.nextText());
                    }
                    if (xpp.getName().equals("NAME")) {
                        displaytype.setEMPLOYEE(xpp.nextText());
                    }
                    if (xpp.getName().equals("DESIGNATION")) {
                        displaytype.setDESIGNATION(xpp.nextText());
                    }

                    if (xpp.getName().equals("HR_LEGACY")) {
                        displaytype.setHR_LEGACY(xpp.nextText());
                    }


                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return displaytype;
    }

    public static QuestionnaireGetterSetter QuestionnaireXMLHandler(XmlPullParser xpp, int eventType) {
        QuestionnaireGetterSetter questionnaireGetterSetter = new QuestionnaireGetterSetter();
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        questionnaireGetterSetter.setTable_QUESTIONNAIRE(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_ID")) {
                        questionnaireGetterSetter.setQUESTION_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION")) {
                        questionnaireGetterSetter.setQUESTION(xpp.nextText());
                    }
                    if (xpp.getName().equals("ANSWER_ID")) {
                        questionnaireGetterSetter.setANSWER_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("ANSWER")) {
                        questionnaireGetterSetter.setANSWER(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_GROUP_ID")) {
                        questionnaireGetterSetter.setQUESTION_GROUP_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_GROUP")) {
                        questionnaireGetterSetter.setQUESTION_GROUP(xpp.nextText());
                    }

                    if (xpp.getName().equals("QUESTION_TYPE")) {
                        questionnaireGetterSetter.setQUESTION_TYPE(xpp.nextText());
                    }


                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return questionnaireGetterSetter;
    }

    public static SpecialActivityGetterSetter SpecialActivityXMLHandler(XmlPullParser xpp, int eventType) {
        SpecialActivityGetterSetter specialActivityGetterSetter = new SpecialActivityGetterSetter();
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        specialActivityGetterSetter.setTable_SPECIAL_ACTIVITY(xpp.nextText());
                    }
                    if (xpp.getName().equals("REGION_CD")) {
                        specialActivityGetterSetter.setREGION_CD(xpp.nextText());
                    }
                    if (xpp.getName().equals("ACTIVITY_CD")) {
                        specialActivityGetterSetter.setACTIVITY_CD(xpp.nextText());
                    }
                    if (xpp.getName().equals("ACTIVITY")) {
                        specialActivityGetterSetter.setACTIVITY(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return specialActivityGetterSetter;
    }

    public static QuestionsGetterSetter QUESTIONSXMLHandler(XmlPullParser xpp, int eventType) {
        QuestionsGetterSetter questionsGetterSetter = new QuestionsGetterSetter();
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        questionsGetterSetter.setTable_QUESTIONS(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_ID")) {
                        questionsGetterSetter.setQUESTION_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION")) {
                        questionsGetterSetter.setQUESTION(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_GROUP_ID")) {
                        questionsGetterSetter.setQUESTION_GROUP_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_GROUP")) {
                        questionsGetterSetter.setQUESTION_GROUP(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_TYPE")) {
                        questionsGetterSetter.setQUESTION_TYPE(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return questionsGetterSetter;
    }

    public static AnswersGetterSetter ANSWERSXMLHandler(XmlPullParser xpp, int eventType) {
        AnswersGetterSetter answersGetterSetter = new AnswersGetterSetter();
        try {
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("META_DATA")) {
                        answersGetterSetter.setTable_ANSWERS(xpp.nextText());
                    }
                    if (xpp.getName().equals("QUESTION_ID")) {
                        answersGetterSetter.setQUESTION_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("ANSWER_ID")) {
                        answersGetterSetter.setANSWER_ID(xpp.nextText());
                    }
                    if (xpp.getName().equals("ANSWER")) {
                        answersGetterSetter.setANSWER(xpp.nextText());
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answersGetterSetter;
    }

}
