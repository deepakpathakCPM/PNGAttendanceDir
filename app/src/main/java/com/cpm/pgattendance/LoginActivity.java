package com.cpm.pgattendance;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cpm.pgattendance.Get_IMEI_number.ImeiNumberClass;
import com.cpm.pgattendance.autoupdate.AutoUpdateActivity;
import com.cpm.pgattendance.bean.TableBean;
import com.cpm.pgattendance.constant.AlertandMessages;
import com.cpm.pgattendance.constant.CommonFunctions;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.getterSetter.FailureGetterSetter;
import com.cpm.pgattendance.getterSetter.LoginGetterSetter;
import com.cpm.pgattendance.getterSetter.QuestionGetterSetter;
import com.cpm.pgattendance.gsonGetterSetter.GsonGetterSetter;
import com.cpm.pgattendance.upload.Retrofit_method.UploadImageWithRetrofit;
import com.cpm.pgattendance.xmlHandler.XMLHandlers;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AuthenticateTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    Intent in;
    Context context;
    private String app_ver;
    private final String lat = "0.0";
    private final String lon = "0.0";
    private String manufacturer, right_answer, rigth_answer_cd;
    private String model, qns_cd, ans_cd;
    private static int counter = 1;
    private String os_version;
    private String[] imeiNumbers;
    private ImeiNumberClass imei;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;
    private int versionCode;
    TextView versiontxt;
    LoginGetterSetter lgs = null;
    QuestionGetterSetter questionGetterSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        declaration();
        /*mEmailView.setText("testmer");
        mPasswordView.setText("cpm123");*/
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        File file = new File(Environment.getExternalStorageDirectory(), CommonString.IMAGES_FOLDER_NAME);
        if (!file.isDirectory()) {
            file.mkdir();
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            imeiNumbers = imei.getDeviceImei();
        }
        getDeviceName();
    }

    public void getDeviceName() {
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        os_version = android.os.Build.VERSION.RELEASE;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imeiNumbers = imei.getDeviceImei();
        }

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else if (!isuseridValid(email)) {
            AlertandMessages.showSnackbarMsg(context, getString(R.string.error_incorrect_username));
        } else if (!isPasswordValid(password)) {
            AlertandMessages.showSnackbarMsg(context, getString(R.string.error_incorrect_password));
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new AuthenticateTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isuseridValid(String userid) {
        //TODO: Replace this with your own logic
        boolean flag = true;
      /*  String u_id = preferences.getString(CommonString.KEY_USERNAME, "");
        if (!u_id.equals("") && !userid.equalsIgnoreCase(u_id)) {
            flag = false;
        }*/
        return flag;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        boolean flag = true;
      /*  String pw = preferences.getString(CommonString.KEY_PASSWORD, "");
        if (!pw.equals("") && !password.equals(pw)) {
            flag = false;
        }*/
        return flag;
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    private class AuthenticateTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog = null;

        String password, userid;
        int eventType;

        AuthenticateTask(String userid, String password) {
            this.userid = userid;
            this.password = password;
        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Login");
            dialog.setMessage("Authenticating....");
            dialog.setCancelable(false);
            if (!dialog.isShowing())
                dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

                String imei1, imei2;
                if (imeiNumbers.length > 0) {
                    imei1 = imeiNumbers[0];
                    if (imeiNumbers.length > 1) {
                        imei2 = imeiNumbers[1];
                    } else {
                        imei2 = "0";
                    }
                } else {
                    imei1 = "0";
                    imei2 = "0";
                }

                String userauth_xml = "[DATA]" + "[USER_DATA]"
                        + "[USER_ID]" + userid + "[/USER_ID]"
                        + "[PASSWORD]" + password + "[/PASSWORD]"
                        + "[IN_TIME]" + CommonFunctions.getCurrentTimeHHMMSS() + "[/IN_TIME]"
                        + "[LATITUDE]" + lat + "[/LATITUDE]"
                        + "[LONGITUDE]" + lon + "[/LONGITUDE]"
                        + "[APP_VERSION]" + app_ver + "[/APP_VERSION]"
                        + "[ATT_MODE]OnLine[/ATT_MODE]"
                        + "[NETWORK_STATUS]0[/NETWORK_STATUS]"
                        + "[MANUFACTURER]" + manufacturer + "[/MANUFACTURER]"
                        + "[MODEL_NUMBER]" + model + "[/MODEL_NUMBER]"
                        + "[OS_VERSION]" + model + "[/OS_VERSION]"
                        + "[IMEI_1]" + imei1 + "[/IMEI_1]"
                        + "[IMEI_2]" + imei2 + "[/IMEI_2]"
                        + "[/USER_DATA][/DATA]";


                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_LOGIN);
                request.addProperty("onXML", userauth_xml);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION_LOGIN, envelope);

                Object result_str = (Object) envelope.getResponse();

                if (result_str.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                    return CommonString.MESSAGE_EXCEPTION;

                } else if (result_str.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                    return CommonString.KEY_FALSE;

                } else if (result_str.toString().equalsIgnoreCase(CommonString.KEY_CHANGED)) {
                    return CommonString.MESSAGE_CHANGED;

                } else {

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(new StringReader(result_str.toString()));
                    xpp.next();
                    eventType = xpp.getEventType();
                    FailureGetterSetter failureGetterSetter = XMLHandlers.failureXMLHandler(xpp, eventType);

                    if (failureGetterSetter.getStatus().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                        return CommonString.KEY_FAILURE;
                    } else {
                        try {
                            // For String source
                            xpp.setInput(new StringReader(result_str.toString()));
                            xpp.next();
                            eventType = xpp.getEventType();
                            lgs = XMLHandlers.loginXMLHandler(xpp, eventType);
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // PUT IN PREFERENCES
                        editor.putString(CommonString.KEY_USERNAME, userid);
                        editor.putString(CommonString.KEY_PASSWORD, password);
                        editor.putString(CommonString.KEY_VERSION, String.valueOf(lgs.getVERSION()));
                        //editor.putString(CommonString.KEY_VERSION, String.valueOf(2));
                        editor.putString(CommonString.KEY_PATH, lgs.getPATH());
                        editor.putString(CommonString.KEY_DATE, lgs.getDATE());

                        Date initDate = new SimpleDateFormat("MM/dd/yyyy").parse(lgs.getDATE());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        String parsedDate = formatter.format(initDate);
                        editor.putString(CommonString.KEY_YYYYMMDD_DATE, parsedDate);
                        //date is changed for previous day data
                        //editor.putString(CommonString.KEY_DATE, "11/22/2017");
                        editor.commit();
                        //return CommonString.KEY_SUCCESS;
                    }

                    //Question download
                    request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                    request.addProperty("UserName", userid);
                    request.addProperty("Type", "TODAY_QUESTION");
                    envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    androidHttpTransport = new HttpTransportSE(CommonString.URL);

                    androidHttpTransport.call(CommonString.SOAP_ACTION_UNIVERSAL, envelope);
                    Object result = (Object) envelope.getResponse();
                    if (result.toString() != null) {
                        xpp.setInput(new StringReader(result.toString()));
                        xpp.next();
                        eventType = xpp.getEventType();
                        questionGetterSetter = XMLHandlers.QuestionXMLHandler(xpp, eventType);
                        if (questionGetterSetter.getQuestion_cd().size() > 0) {
                            String qnsTable = questionGetterSetter.getTable_question_today();
                            TableBean.setQuestiontable(qnsTable);
                        } else {
                            return CommonString.KEY_SUCCESS;
                        }
                    }
                }
                return CommonString.KEY_SUCCESS;
            } catch (MalformedURLException e) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_EXCEPTION, false);
                    }
                });

            } catch (IOException e) {
                counter++;
                return CommonString.MESSAGE_SOCKETEXCEPTION;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.equals(CommonString.KEY_SUCCESS)) {
                if (preferences.getString(CommonString.KEY_VERSION, "").equals(Integer.toString(versionCode))) {
                    String visit_date = preferences.getString(CommonString.KEY_DATE, "");
                    if (questionGetterSetter.getAnswer_cd().size() > 0 && questionGetterSetter.getStatus().get(0).equals("N") &&
                            !preferences.getBoolean(CommonString.KEY_IS_QUIZ_DONE + visit_date, false)) {
                        for (int i = 0; i < questionGetterSetter.getRight_answer().size(); i++) {
                            if (questionGetterSetter.getRight_answer().get(i).equals("1")) {
                                right_answer = questionGetterSetter.getAnswer().get(i);
                                rigth_answer_cd = questionGetterSetter.getAnswer_cd().get(i);
                                break;
                            }
                        }
                        final AnswerData answerData = new AnswerData();
                        final Dialog customD = new Dialog(context);
                        customD.setTitle("Todays Question");
                        customD.setCancelable(false);
                        customD.setContentView(R.layout.show_answer_layout);
                        customD.setContentView(R.layout.todays_question_layout);
                        ((TextView) customD.findViewById(R.id.tv_qns)).setText(questionGetterSetter.getQuestion().get(0));
                        Button btnsubmit = (Button) customD.findViewById(R.id.btnsubmit);
                        final TextView txt_timer = (TextView) customD.findViewById(R.id.txt_timer);
                        RadioGroup radioGroup = (RadioGroup) customD.findViewById(R.id.radiogrp);
                        new CountDownTimer(30000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                txt_timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                if (answerData.getAnswer_id() == null || answerData.getAnswer_id().equals("")) {
                                    txt_timer.setText("done!");
                                    customD.cancel();
                                    String ansisright = "";
                                    ansisright = "Your Time is over";
                                    final Dialog ans_dialog = new Dialog(LoginActivity.this);
                                    ans_dialog.setTitle("Answer");
                                    ans_dialog.setCancelable(false);
                                    //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    ans_dialog.setContentView(R.layout.show_answer_layout);
                                    ((TextView) ans_dialog.findViewById(R.id.tv_ans)).setText(ansisright);
                                    Button btnok = (Button) ans_dialog.findViewById(R.id.btnsubmit);
                                    btnok.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            answerData.setQuestion_id(questionGetterSetter.getQuestion_cd().get(0));
                                            answerData.setUsername(userid);
                                            answerData.setVisit_date(lgs.getDATE());
                                            if (CommonFunctions.checkNetIsAvailable(context)) {
                                                ans_dialog.cancel();
                                                new AnswerTodayTask().execute(answerData);
                                            } else {
                                                AlertandMessages.showToastMsg(context, "No internet connection");
                                            }
                                        }
                                    });
                                    ans_dialog.show();
                                }
                            }
                        }.start();

                        for (int i = 0; i < questionGetterSetter.getAnswer_cd().size(); i++) {
                            RadioButton rdbtn = new RadioButton(LoginActivity.this);
                            rdbtn.setId(i);
                            rdbtn.setText(questionGetterSetter.getAnswer().get(i));
                            radioGroup.addView(rdbtn);
                        }

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {

                                answerData.setAnswer_id(questionGetterSetter.getAnswer_cd().get(checkedId));
                                answerData.setRight_answer(questionGetterSetter.getRight_answer().get(checkedId));

                            }
                        });

                        btnsubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (answerData.getAnswer_id() == null || answerData.getAnswer_id().equals("")) {
                                    AlertandMessages.showSnackbarMsg(context, "First select an answer");
                                } else {
                                    customD.cancel();
                                    String ansisright = "";
                                    if (answerData.getRight_answer().equals("1")) {
                                        ansisright = "Your Answer Is Right!";
                                    } else {
                                        ansisright = "Your Answer is Wrong! Right Answer Is :- " + right_answer;
                                    }
                                    final Dialog ans_dialog = new Dialog(LoginActivity.this);
                                    ans_dialog.setTitle("Answer");
                                    ans_dialog.setCancelable(false);
                                    ans_dialog.setContentView(R.layout.show_answer_layout);
                                    ((TextView) ans_dialog.findViewById(R.id.tv_ans)).setText(ansisright);
                                    Button btnok = (Button) ans_dialog.findViewById(R.id.btnsubmit);
                                    btnok.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            answerData.setQuestion_id(questionGetterSetter.getQuestion_cd().get(0));
                                            answerData.setUsername(userid);
                                            answerData.setVisit_date(lgs.getDATE());
                                            if (CommonFunctions.checkNetIsAvailable(context)) {
                                                new AnswerTodayTask().execute(answerData);
                                                ans_dialog.cancel();
                                            } else {
                                                AlertandMessages.showToastMsg(context, "No internet connection");
                                            }
                                        }
                                    });
                                    ans_dialog.show();
                                }
                            }
                        });
                        customD.show();


                    } else {
                        Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    Intent intent = new Intent(getBaseContext(), AutoUpdateActivity.class);
                    intent.putExtra(CommonString.KEY_PATH, preferences.getString(CommonString.KEY_PATH, ""));
                    startActivity(intent);
                    finish();
                }
            } else {
                AlertandMessages.showAlert((Activity) context, "Error in Login:" + result, false);
            }

        }


    }

    class AnswerData {
        public String question_id, answer_id, username, visit_date, right_answer;

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getAnswer_id() {
            return answer_id;
        }

        public void setAnswer_id(String answer_id) {
            this.answer_id = answer_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVisit_date() {
            return visit_date;
        }

        public void setVisit_date(String visit_date) {
            this.visit_date = visit_date;
        }

        public String getRight_answer() {
            return right_answer;
        }

        public void setRight_answer(String right_answer) {
            this.right_answer = right_answer;
        }
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


    void declaration() {
        context = this;
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        in = new Intent(context, MainMenuActivity.class);
        mPasswordView = (EditText) findViewById(R.id.password);
        versiontxt = (TextView) findViewById(R.id.version);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            app_ver = "0";
            e.printStackTrace();
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        versiontxt.setText("Version - " + app_ver);
        imei = new ImeiNumberClass(context);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }


    class AnswerTodayTask extends AsyncTask<AnswerData, Void, String> {
        private ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setTitle("Todays Question");
            dialog.setMessage("Submitting Answer..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(AnswerData... params) {

            try {
                AnswerData answerData = params[0];
                if (answerData.getAnswer_id() == null) {
                    answerData.setAnswer_id("0");
                }
                String resultHttp = "";
                versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

                qns_cd = answerData.getQuestion_id();
                ans_cd = answerData.getAnswer_id();

                String userauth_xml = "[DATA]" + "[TODAY_ANSWER][USER_ID]"
                        + answerData.getUsername() + "[/USER_ID]" + "[QUESTION_ID]" + answerData.getQuestion_id()
                        + "[/QUESTION_ID]" + "[ANSWER_ID]" + answerData.getAnswer_id()
                        + "[/ANSWER_ID]" + "[VISIT_DATE]" + answerData.getVisit_date()
                        + "[/VISIT_DATE]"
                        + "[/TODAY_ANSWER][/DATA]";
                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                request.addProperty("XMLDATA", userauth_xml);
                request.addProperty("KEYS", "TODAYS_ANSWER");
                request.addProperty("USERNAME", answerData.getUsername());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                Object result = (Object) envelope.getResponse();
                if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {

                } else {
                    String visit_date = preferences.getString(CommonString.KEY_DATE, null);
                    editor = preferences.edit();
                    editor.putBoolean(CommonString.KEY_IS_QUIZ_DONE + visit_date, true);
                    editor.commit();
                    return CommonString.KEY_SUCCESS;
                }


            } catch (MalformedURLException e) {

                return CommonString.MESSAGE_EXCEPTION;

                // AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_EXCEPTION, false);

            } catch (IOException e) {
                // AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, false);
                return CommonString.MESSAGE_SOCKETEXCEPTION;

            } catch (Exception e) {
                // AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_EXCEPTION, false);.
                return CommonString.MESSAGE_EXCEPTION;
            }
            return CommonString.KEY_FAILURE;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            super.onPostExecute(result);
            if (result.equals(CommonString.KEY_SUCCESS)) {
                Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            } else {
                //Save question cd and ans cd here for upload
                String visit_date = preferences.getString(CommonString.KEY_DATE, null);
                editor = preferences.edit();
                editor.putString(CommonString.KEY_QUESTION_CD + visit_date, qns_cd);
                editor.putString(CommonString.KEY_ANSWER_CD + visit_date, ans_cd);
                editor.commit();
                Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }

}

