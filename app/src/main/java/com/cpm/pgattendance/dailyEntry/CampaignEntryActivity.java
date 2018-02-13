package com.cpm.pgattendance.dailyEntry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.cpm.pgattendance.R;
import com.cpm.pgattendance.constant.AlertandMessages;
import com.cpm.pgattendance.constant.CommonFunctions;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.database.PNGAttendanceDB;
import com.cpm.pgattendance.getterSetter.CampaignEntryGetterSetter;
import com.cpm.pgattendance.getterSetter.JCPMasterGetterSetter;
import com.cpm.pgattendance.getterSetter.SpecialActivityGetterSetter;

import java.io.File;

public class CampaignEntryActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    PNGAttendanceDB db;
    private String date, username, visit_date_formatted;
    SharedPreferences preferences;
    ImageView imageView1, imageView2, imageView3;
    EditText editText;
    SpecialActivityGetterSetter spcActivityGetSet;
    JCPMasterGetterSetter jcpGetSet;
    String _path, _pathforcheck, img_str="", img_str2="", img_str3="", msg;
    Runnable task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_entry);
        declaration();

        task = new Runnable() {
            @Override
            public void run() {

                if (img_str != null && !img_str.equalsIgnoreCase("")) {
                    if (new File(CommonString.FILE_PATH + img_str).exists()) {
                        new File(CommonString.FILE_PATH + img_str).delete();
                    }
                }
                if (img_str2 != null && !img_str2.equalsIgnoreCase("")) {
                    if (new File(CommonString.FILE_PATH + img_str2).exists()) {
                        new File(CommonString.FILE_PATH + img_str2).delete();
                    }
                } else if (img_str3 != null && !img_str3.equalsIgnoreCase("")) {
                    if (new File(CommonString.FILE_PATH + img_str3).exists()) {
                        new File(CommonString.FILE_PATH + img_str3).delete();
                    }
                } else if (!editText.getText().toString().isEmpty()) {
                    editText.getText().clear();
                }
            }
        };

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        new AlertandMessages((Activity) context, null, null, null).backpressedAlertWithTaskRun((Activity) context, CommonString.MSG_DATA_LOST, task);
    }

    void declaration() {
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, "");
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date_formatted = preferences.getString(CommonString.KEY_YYYYMMDD_DATE, "");
        db = new PNGAttendanceDB(context);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        editText = (EditText) findViewById(R.id.edittext);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);

        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            spcActivityGetSet = (SpecialActivityGetterSetter) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
            jcpGetSet = (JCPMasterGetterSetter) getIntent().getSerializableExtra(CommonString.TAG_OBJECT1);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate2()) {
                    CampaignEntryGetterSetter campaignEntryGetterSetter = new CampaignEntryGetterSetter();
                    campaignEntryGetterSetter.setStoreCd(jcpGetSet.getSTORE_CD().get(0));
                    campaignEntryGetterSetter.setActivityCd(spcActivityGetSet.getACTIVITY_CD().get(0));
                    campaignEntryGetterSetter.setUserid(username);
                    campaignEntryGetterSetter.setVisitDate(date);
                    campaignEntryGetterSetter.setImage1(img_str);
                    campaignEntryGetterSetter.setImage2(img_str2);
                    campaignEntryGetterSetter.setImage3(img_str3);
                    campaignEntryGetterSetter.setRemark(editText.getText().toString());

                    db.open();
                    if (db.saveSpecialActivityData(campaignEntryGetterSetter) > 0) {
                        AlertandMessages.showToastMsg(context, "Data Saved Successfully");
                        finish();
                    } else {
                        AlertandMessages.showToastMsg(context, "Error in Data Saving");
                    }

                } else {
                    AlertandMessages.showSnackbarMsg(context, msg);
                }
            }
        });
    }

    boolean validate() {
        boolean isvalid = true;
        if (img_str == null || img_str.equalsIgnoreCase("")) {
            msg = "Please Click First Image";
            isvalid = false;
        } else if (img_str2 == null || img_str2.equalsIgnoreCase("")) {
            msg = "Please Click Second Image";
            isvalid = false;
        } else if (img_str3 == null || img_str3.equalsIgnoreCase("")) {
            msg = "Please Click Three Image";
            isvalid = false;
        } else if (editText.getText().toString().isEmpty()) {
            msg = "Please Fill Remark";
            isvalid = false;
        }
        return isvalid;
    }

    boolean validate2() {
        boolean isvalid = true;
        if ((img_str == null || img_str.equalsIgnoreCase(""))
                && (img_str2 == null || img_str2.equalsIgnoreCase(""))
                && (img_str3 == null || img_str3.equalsIgnoreCase(""))) {
            msg = "Please Click Atleast One Image";
            isvalid = false;
        } else if (editText.getText().toString().isEmpty()) {
            msg = "Please Fill Remark";
            isvalid = false;
        }
        return isvalid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView1:
                img_str = "";
                _pathforcheck = jcpGetSet.getSTORE_CD().get(0) + "_" + spcActivityGetSet.getACTIVITY_CD().get(0) + "_" + username.replace(".", "") + "_ActivityImgOne-" + visit_date_formatted + "-" + CommonFunctions.getCurrentTimeHHMMSS() + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startCameraActivityWithRequestCode((Activity) context, _path, 0);
                break;
            case R.id.imageView2:
                img_str2 = "";
                _pathforcheck = jcpGetSet.getSTORE_CD().get(0) + "_" + spcActivityGetSet.getACTIVITY_CD().get(0) + "_" + username.replace(".", "") + "_ActivityImgTwo-" + visit_date_formatted + "-" + CommonFunctions.getCurrentTimeHHMMSS() + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startCameraActivityWithRequestCode((Activity) context, _path, 1);
                break;
            case R.id.imageView3:
                img_str3 = "";
                _pathforcheck = jcpGetSet.getSTORE_CD().get(0) + "_" + spcActivityGetSet.getACTIVITY_CD().get(0) + "_" + username.replace(".", "") + "_ActivityImgThree-" + visit_date_formatted + "-" + CommonFunctions.getCurrentTimeHHMMSS() + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startCameraActivityWithRequestCode((Activity) context, _path, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(_path).exists()) {

                        switch (requestCode) {
                            case 0:
                                try {
                                    img_str = _pathforcheck;
                                    Bitmap bmp = BitmapFactory.decodeFile(_path);
                                    imageView1.setImageBitmap(bmp);
                                } catch (OutOfMemoryError ex) {
                                    CommonFunctions.setScaledImage(imageView1, _path);
                                }
                                break;
                            case 1:
                                try {
                                    img_str2 = _pathforcheck;
                                    Bitmap bmp = BitmapFactory.decodeFile(_path);
                                    imageView2.setImageBitmap(bmp);
                                } catch (OutOfMemoryError ex) {
                                    CommonFunctions.setScaledImage(imageView2, _path);
                                }
                                break;
                            case 2:
                                try {
                                    img_str3 = _pathforcheck;
                                    Bitmap bmp = BitmapFactory.decodeFile(_path);
                                    imageView3.setImageBitmap(bmp);
                                } catch (OutOfMemoryError ex) {
                                    CommonFunctions.setScaledImage(imageView3, _path);
                                }
                                break;
                        }
                        _pathforcheck = "";
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
