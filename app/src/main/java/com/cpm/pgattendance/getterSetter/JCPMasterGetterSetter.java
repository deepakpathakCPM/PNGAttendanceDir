package com.cpm.pgattendance.getterSetter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by deepakp on 2/22/2017.
 */

public class JCPMasterGetterSetter implements Serializable {

    String table_JOURNEY_PLAN = "";

    ArrayList<String> STORE_CD = new ArrayList<String>();
    ArrayList<String> EMP_CD = new ArrayList<String>();
    ArrayList<String> VISIT_DATE = new ArrayList<String>();
    ArrayList<String> KEYACCOUNT = new ArrayList<String>();
    ArrayList<String> STORENAME = new ArrayList<String>();
    ArrayList<String> CITY = new ArrayList<String>();
    ArrayList<String> STORETYPE = new ArrayList<String>();
    ArrayList<String> STATE_CD = new ArrayList<String>();
    ArrayList<String> STORETYPE_CD = new ArrayList<String>();
    ArrayList<String> UPLOAD_STATUS = new ArrayList<String>();
    ArrayList<String> CHECKOUT_STATUS = new ArrayList<String>();
    ArrayList<String> GEO_TAG = new ArrayList<String>();

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    boolean isFilled;

    public ArrayList<String> getREGION_CD() {
        return REGION_CD;
    }

    public void setREGION_CD(String REGION_CD) {
        this.REGION_CD.add(REGION_CD);
    }

    ArrayList<String> REGION_CD = new ArrayList<String>();

    public ArrayList<String> getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME.add(USERNAME);
    }

    ArrayList<String> USERNAME = new ArrayList<String>();

    public String getTable_JOURNEY_PLAN() {
        return table_JOURNEY_PLAN;
    }

    public void setTable_JOURNEY_PLAN(String table_JOURNEY_PLAN) {
        this.table_JOURNEY_PLAN = table_JOURNEY_PLAN;
    }

    public ArrayList<String> getSTORE_CD() {
        return STORE_CD;
    }

    public void setSTORE_CD(String STORE_CD) {
        this.STORE_CD.add(STORE_CD);
    }

    public ArrayList<String> getEMP_CD() {
        return EMP_CD;
    }

    public void setEMP_CD(String EMP_CD) {
        this.EMP_CD.add(EMP_CD);
    }

    public ArrayList<String> getVISIT_DATE() {
        return VISIT_DATE;
    }

    public void setVISIT_DATE(String VISIT_DATE) {
        this.VISIT_DATE.add(VISIT_DATE);
    }

    public ArrayList<String> getKEYACCOUNT() {
        return KEYACCOUNT;
    }

    public void setKEYACCOUNT(String KEYACCOUNT) {
        this.KEYACCOUNT.add(KEYACCOUNT);
    }

    public ArrayList<String> getSTORENAME() {
        return STORENAME;
    }

    public void setSTORENAME(String STORENAME) {
        this.STORENAME.add(STORENAME);
    }

    public ArrayList<String> getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY.add(CITY);
    }

    public ArrayList<String> getSTORETYPE() {
        return STORETYPE;
    }

    public void setSTORETYPE(String STORETYPE) {
        this.STORETYPE.add(STORETYPE);
    }

    public ArrayList<String> getSTATE_CD() {
        return STATE_CD;
    }

    public void setSTATE_CD(String STATE_CD) {
        this.STATE_CD.add(STATE_CD);
    }

    public ArrayList<String> getSTORETYPE_CD() {
        return STORETYPE_CD;
    }

    public void setSTORETYPE_CD(String STORETYPE_CD) {
        this.STORETYPE_CD.add(STORETYPE_CD);
    }

    public ArrayList<String> getUPLOAD_STATUS() {
        return UPLOAD_STATUS;
    }

    public void setUPLOAD_STATUS(String UPLOAD_STATUS) {
        this.UPLOAD_STATUS.add(UPLOAD_STATUS);
    }

    public ArrayList<String> getCHECKOUT_STATUS() {
        return CHECKOUT_STATUS;
    }

    public void setCHECKOUT_STATUS(String CHECKOUT_STATUS) {
        this.CHECKOUT_STATUS.add(CHECKOUT_STATUS);
    }

    public ArrayList<String> getGEO_TAG() {
        return GEO_TAG;
    }

    public void setGEO_TAG(String GEO_TAG) {
        this.GEO_TAG.add(GEO_TAG);
    }
}
