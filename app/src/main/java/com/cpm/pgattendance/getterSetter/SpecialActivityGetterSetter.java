package com.cpm.pgattendance.getterSetter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by deepakp on 16-01-2018.
 */

public class SpecialActivityGetterSetter implements Serializable {

    public String getTable_SPECIAL_ACTIVITY() {
        return table_SPECIAL_ACTIVITY;
    }

    public void setTable_SPECIAL_ACTIVITY(String table_SPECIAL_ACTIVITY) {
        this.table_SPECIAL_ACTIVITY = table_SPECIAL_ACTIVITY;
    }

    public ArrayList<String> getREGION_CD() {
        return REGION_CD;
    }

    public void setREGION_CD(String REGION_CD) {
        this.REGION_CD.add(REGION_CD);
    }

    public ArrayList<String> getACTIVITY_CD() {
        return ACTIVITY_CD;
    }

    public void setACTIVITY_CD(String ACTIVITY_CD) {
        this.ACTIVITY_CD.add(ACTIVITY_CD);
    }

    public ArrayList<String> getACTIVITY() {
        return ACTIVITY;
    }

    public void setACTIVITY(String ACTIVITY) {
        this.ACTIVITY.add(ACTIVITY);
    }

    String table_SPECIAL_ACTIVITY;
    ArrayList<String> REGION_CD = new ArrayList<>();
    ArrayList<String> ACTIVITY_CD = new ArrayList<>();
    ArrayList<String> ACTIVITY = new ArrayList<>();


}
