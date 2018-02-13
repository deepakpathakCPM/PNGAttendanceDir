package com.cpm.pgattendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.cpm.pgattendance.constant.AlertandMessages;
import com.cpm.pgattendance.constant.CommonFunctions;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.dailyEntry.ClientFeedbackActivity;
import com.cpm.pgattendance.dailyEntry.MyPerfromanceActivity;
import com.cpm.pgattendance.dailyEntry.QuestionnaireActivity;
import com.cpm.pgattendance.dailyEntry.StoreListActivity;
import com.cpm.pgattendance.dailyEntry.StoreListForCampaignActivity;
import com.cpm.pgattendance.dailyEntry.VisitorLoginActivity;
import com.cpm.pgattendance.database.PNGAttendanceDB;
import com.cpm.pgattendance.download.DownloadActivity;
import com.cpm.pgattendance.upload.Retrofit_method.UploadImageWithRetrofit;
import com.example.deepakp.customcamera.CustomCameraActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Intent storeListIntent, campaignIntent;
    Context context;
    private WebView webView;
    private ImageView imageView;
    String url;
    private SharedPreferences preferences;
    Intent myPerformanceintent, clientFeedbackIntent, visitorIntent;
    Intent exitIntent, questionnaireIntent;
    String visit_date_formatted, user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        declaration();
        myPerformanceintent = new Intent(context, MyPerfromanceActivity.class);
        exitIntent = new Intent(context, LoginActivity.class);
        visitorIntent = new Intent(context, VisitorLoginActivity.class);
        questionnaireIntent = new Intent(context, QuestionnaireActivity.class);
        clientFeedbackIntent = new Intent(context, ClientFeedbackActivity.class);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        if (!url.equals("")) {
            webView.loadUrl(url);
        }
        File file = new File(Environment.getExternalStorageDirectory(), CommonString.IMAGES_FOLDER_NAME);
        if (!file.isDirectory()) {
            file.mkdir();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.dailyEntry) {
            startActivity(storeListIntent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        } else if (id == R.id.downloadData) {
            if (CommonFunctions.checkNetIsAvailable(context)) {
                Intent startDownload = new Intent(context, DownloadActivity.class);
                startActivity(startDownload);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            } else {
                AlertandMessages.showSnackbarMsg(context, CommonString.MESSAGE_INTERNET_NOT_AVAILABLE);
            }
        } else if (id == R.id.attendance) {
            if (!preferences.getBoolean(CommonString.KEY_ISDATADOWNLOADED, false)) {
                Toast.makeText(getBaseContext(), "Please Download Data First",
                        Toast.LENGTH_LONG).show();
            } else {

               /* attendanceList2 = db.getAttendanceListFromDownload(user_name, visit_date);
                attendanceList = db.getAttendanceList(user_name, visit_date);
                if (attendanceList.size() == 0 && attendanceList2.size() == 0) {
                    Intent in = new Intent(getApplicationContext(), AttendanceActivity.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                } else {
                    AlertandMessages.showToastMsg(context, "Attendence has been marked already");
                }*/
            }

        } else if (id == R.id.uploadData) {

        } else if (id == R.id.campaign) {
            startActivity(campaignIntent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        } else if (id == R.id.visitorLogin) {
            if (preferences.getBoolean(CommonString.KEY_ISDATADOWNLOADED, false)) {
                startActivity(visitorIntent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            } else {
                AlertandMessages.showSnackbarMsg(context, "Please download data");
            }
        } else if (id == R.id.myPerformance) {
            startActivity(myPerformanceintent);
        } else if (id == R.id.customCamera) {
            CustomCameraActivity customCameraActivity = new CustomCameraActivity(context, CommonString.FILE_PATH, "abc");
            customCameraActivity.startCameraActivity();
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

        } else if (id == R.id.clientFeedback) {
            startActivity(clientFeedbackIntent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        } else if (id == R.id.questionnaire) {
            startActivity(questionnaireIntent);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        } else if (id == R.id.exit) {
            exitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exitIntent);
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            finish();
        } else if (id == R.id.nav_export_database) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Are you sure you want to take the backup of your data")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @SuppressWarnings("resource")
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                File file = new File(Environment.getExternalStorageDirectory(), "pngAttendance_backup");
                                if (!file.isDirectory()) {
                                    file.mkdir();
                                }

                                File sd = Environment.getExternalStorageDirectory();
                                File data = Environment.getDataDirectory();

                                if (sd.canWrite()) {
                                    long date = System.currentTimeMillis();

                                    SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yy");
                                    String dateString = sdf.format(date);

                                    String currentDBPath = "//data//com.cpm.pgattendance//databases//" + PNGAttendanceDB.DATABASE_NAME;
                                    //String backupDBPath = "PngAttendance_backup" + dateString.replace('/', '-');
                                    String backupDBPath = "PngAttendance_" + user_name.replace(".", "") + "_backup-" + visit_date_formatted + "-" + CommonFunctions.getCurrentTimeHHMMSS().replace(":", "") + ".db";

                                    String path = Environment.getExternalStorageDirectory().getPath();

                                    File currentDB = new File(data, currentDBPath);
                                    File backupDB = new File(path, backupDBPath);

                                    AlertandMessages.showSnackbarMsg(context, "Database Exported Successfully");

                                    if (currentDB.exists()) {
                                        @SuppressWarnings("resource")
                                        FileChannel src = new FileInputStream(currentDB).getChannel();
                                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                        dst.transferFrom(src, 0, src.size());
                                        src.close();
                                        dst.close();
                                    }
                                    new uploadbackup(backupDBPath).execute();

                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert1 = builder1.create();
            alert1.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class uploadbackup extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String backupName;

        uploadbackup(String backupName) {
            this.backupName = backupName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Uploading Backup");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String path = Environment.getExternalStorageDirectory().getPath();
            File backupfile = new File(path + "/" + backupName);
            if (backupfile.exists()) {
                UploadImageWithRetrofit uploadRetro = new UploadImageWithRetrofit(context);
                uploadRetro.uploadBackupWithRetrofit(backupfile, "DBBackup", path, pd);
            }
            return "";
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            imageView.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
            view.clearCache(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int requestCode4 = requestCode;
        int resultCode4 = resultCode;
    }

    void declaration() {
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        url = preferences.getString(CommonString.KEY_NOTICE_BOARD_LINK, "");
        user_name = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date_formatted = preferences.getString(CommonString.KEY_YYYYMMDD_DATE, "");
        storeListIntent = new Intent(context, StoreListActivity.class);
        campaignIntent = new Intent(context, StoreListForCampaignActivity.class);
        clientFeedbackIntent = new Intent(context, ClientFeedbackActivity.class);
        imageView = (ImageView) findViewById(R.id.img_main);
        webView = (WebView) findViewById(R.id.webview);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //((TextView)findViewById(R.id.textview)).setText(user_name);
    }
}
