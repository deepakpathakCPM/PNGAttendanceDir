package com.cpm.pgattendance.dailyEntry;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpm.pgattendance.R;
import com.cpm.pgattendance.constant.AlertandMessages;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.database.PNGAttendanceDB;
import com.cpm.pgattendance.download.DownloadActivity;
import com.cpm.pgattendance.getterSetter.CampaignEntryGetterSetter;
import com.cpm.pgattendance.getterSetter.CoverageBean;
import com.cpm.pgattendance.getterSetter.JCPMasterGetterSetter;
import com.cpm.pgattendance.getterSetter.SpecialActivityGetterSetter;
import com.cpm.pgattendance.upload.Retrofit_method.UploadImageWithRetrofit;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class StoreListForCampaignActivity extends AppCompatActivity {

    Context context;
    RecyclerView recyclerView;
    Intent campaignListIntent;
    PNGAttendanceDB db;
    private String date, username;
    SharedPreferences preferences;
    ArrayList<JCPMasterGetterSetter> storelist;
    StoreListAdapter adapter;
    LinearLayout storelist_ll, no_data_ll;
    FloatingActionButton fab;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list_for_campaign);
        declaration();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareList();
    }

    class StoreListAdapter extends RecyclerView.Adapter<MyViewHolder> {

        LayoutInflater inflater;
        ArrayList<JCPMasterGetterSetter> list;

        StoreListAdapter(Context context, ArrayList<JCPMasterGetterSetter> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_add_store_list_item_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final JCPMasterGetterSetter jcpMasterGetterSetter = list.get(position);
            int storeCd = 0;

            if (jcpMasterGetterSetter.getUPLOAD_STATUS().get(0).equalsIgnoreCase("U")) {
                holder.img_storeImage.setImageDrawable(getResources().getDrawable(R.drawable.ticku));
                holder.img_storeImage.setVisibility(View.VISIBLE);
            } else {
                holder.img_storeImage.setVisibility(View.INVISIBLE);
            }

            if (!jcpMasterGetterSetter.getUPLOAD_STATUS().get(0).equalsIgnoreCase("U") && jcpMasterGetterSetter.isFilled()) {
                holder.checkoutbtn.setText("Upload");
                holder.checkoutbtn.setVisibility(View.VISIBLE);
                // holder.img_storeImage.setVisibility(View.INVISIBLE);
                holder.img_storeImage.setImageDrawable(getResources().getDrawable(R.drawable.checkin_ico));
                holder.img_storeImage.setVisibility(View.VISIBLE);
            } else {
                holder.checkoutbtn.setVisibility(View.GONE);
            }

            holder.textView.setText(jcpMasterGetterSetter.getSTORENAME().get(0));

            holder.storelist_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jcpMasterGetterSetter.getUPLOAD_STATUS().get(0).equalsIgnoreCase("U")) {
                        AlertandMessages.showSnackbarMsg(context, "Store Already Uploaded");
                    } else {
                        campaignListIntent.putExtra(CommonString.TAG_OBJECT, jcpMasterGetterSetter);
                        startActivity(campaignListIntent);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }

                }
            });

            holder.checkoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UploadCampaignTask(jcpMasterGetterSetter).execute();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout storelist_ll;
        Button checkoutbtn;
        ImageView img_storeImage;


        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_storeName);
            checkoutbtn = view.findViewById(R.id.checkoutbtn);
            storelist_ll = view.findViewById(R.id.storelist_ll);
            img_storeImage = view.findViewById(R.id.img_storeImage);
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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }


    void prepareList() {
        db.open();
        storelist = db.getStoreListCampaignData(date, username);
        if (storelist.size() > 0) {
            for (int i = 0; i < storelist.size(); i++) {
                ArrayList<SpecialActivityGetterSetter> campaignList = db.getSpecialActivityData(storelist.get(i).getREGION_CD().get(0));
                if (campaignList != null && campaignList.size() > 0) {
                    db.open();
                    for (int j = 0; j < campaignList.size(); j++) {
                        if (db.getSavedSpecialActivityData(storelist.get(i).getSTORE_CD().get(0), campaignList.get(j).getACTIVITY_CD().get(0), date, username).size() > 0) {
                            storelist.get(i).setFilled(true);
                        } else {
                            storelist.get(i).setFilled(false);
                        }
                    }
                } else {
                    storelist.get(i).setFilled(false);
                }
            }


            adapter = new StoreListAdapter(context, storelist);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            storelist_ll.setVisibility(View.VISIBLE);
            no_data_ll.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);

        } else {
            no_data_ll.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            storelist_ll.setVisibility(View.GONE);
        }
    }


    void declaration() {
        context = this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        storelist_ll = (LinearLayout) findViewById(R.id.storelist);
        no_data_ll = (LinearLayout) findViewById(R.id.no_data_lay);
        campaignListIntent = new Intent(context, CampaignListActivity.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        db = new PNGAttendanceDB(context);
        db.open();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finish();
            }
        });
    }

    class UploadCampaignTask extends AsyncTask<String, String, String> {

        JCPMasterGetterSetter jcpMasterGetterSetter;
        ArrayList<CampaignEntryGetterSetter> campaignList;

        UploadCampaignTask(JCPMasterGetterSetter jcpMasterGetterSetter) {
            this.jcpMasterGetterSetter = jcpMasterGetterSetter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            campaignList = db.getSavedSpecialActivityFromStoreCdData(jcpMasterGetterSetter.getSTORE_CD().get(0), date, username);
            if (campaignList.size() > 0) {
                pd = new ProgressDialog(context);
                pd.setCancelable(false);
                pd.setMessage("Uploading Data: Store - " + jcpMasterGetterSetter.getSTORENAME().get(0));
                pd.show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                UploadImageWithRetrofit uploadRetro = new UploadImageWithRetrofit(context);
                String resulthttp = "";

                if (campaignList.size() > 0) {
                    for (int i = 0; i < campaignList.size(); i++) {

                        String onXML = "[DATA][USER_DATA]"
                                + "[STORE_CD]" + jcpMasterGetterSetter.getSTORE_CD().get(0) + "[/STORE_CD]"
                                + "[VISIT_DATE]" + date + "[/VISIT_DATE]"
                                + "[USER_ID]" + username + "[/USER_ID]"
                                + "[ACTIVITY_CD]" + campaignList.get(i).getActivityCd() + "[/ACTIVITY_CD]"
                                + "[IMAGE1]" + campaignList.get(i).getImage1() + "[/IMAGE1]"
                                + "[IMAGE2]" + campaignList.get(i).getImage2() + "[/IMAGE2]"
                                + "[IMAGE3]" + campaignList.get(i).getImage3() + "[/IMAGE3]"
                                + "[REMARK]" + campaignList.get(i).getRemark() + "[/REMARK]"
                                + "[/USER_DATA][/DATA]";


                        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                        request.addProperty("XMLDATA", onXML);
                        request.addProperty("KEYS", "CAMPAIGN_DATA");
                        request.addProperty("USERNAME", username);
                        request.addProperty("MID", 0);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);
                        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                        androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);
                        Object result = (Object) envelope.getResponse();

                        if (result.toString().contains(CommonString.KEY_SUCCESS)) {
                            resulthttp = CommonString.KEY_SUCCESS;
                        } else {

                            if (!result.toString().contains(CommonString.KEY_SUCCESS)) {
                                resulthttp = result.toString();
                            }
                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                                resulthttp = result + "in uploading coverage";
                            }
                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                resulthttp = result + "in uploading coverage";
                            }

                        }
                    }

                    if (campaignList.size() > 0) {
                        for (int i = 0; i < campaignList.size(); i++) {

                            if (campaignList.get(i).getImage1() != null
                                    && !campaignList.get(i).getImage1()
                                    .equals("")) {

                                if (new File(CommonString.FILE_PATH + campaignList.get(i).getImage1()).exists()) {
                                    //result = UploadImage(addNewStoreList.get(j).getImg_Camera(), "StoreImages");
                                    String result = uploadRetro.UploadImage2(campaignList.get(i).getImage1(), "StoreImages", CommonString.FILE_PATH);
                                    if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                        if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                                            resulthttp = "Images 1 not uploaded";
                                        } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                            resulthttp = " Images 1 not uploaded";
                                        } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                            throw new IOException();
                                        }
                                    }

                                }
                            }

                            if (campaignList.get(i).getImage2() != null
                                    && !campaignList.get(i).getImage2()
                                    .equals("")) {

                                if (new File(CommonString.FILE_PATH + campaignList.get(i).getImage2()).exists()) {
                                    //result = UploadImage(addNewStoreList.get(j).getImg_Camera(), "StoreImages");
                                    String result = uploadRetro.UploadImage2(campaignList.get(i).getImage2(), "StoreImages", CommonString.FILE_PATH);
                                    if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                        if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                                            resulthttp = "Images 2 not uploaded";
                                        } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                            resulthttp = "Images 2 not uploaded";
                                        } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                            throw new IOException();
                                        }
                                    }
                                }
                            }

                            if (campaignList.get(i).getImage3() != null
                                    && !campaignList.get(i).getImage3()
                                    .equals("")) {
                                if (new File(CommonString.FILE_PATH + campaignList.get(i).getImage3()).exists()) {
                                    //result = UploadImage(addNewStoreList.get(j).getImg_Camera(), "StoreImages");
                                    String result = uploadRetro.UploadImage2(campaignList.get(i).getImage3(), "StoreImages", CommonString.FILE_PATH);
                                    if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                        if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                                            resulthttp = "Images 3 not uploaded";
                                        } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                            resulthttp = "Images 3 not uploaded";
                                        } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                            throw new IOException();
                                        }
                                    }
                                }
                            }


                        }
                    }

                    return resulthttp;
                }
            } catch (MalformedURLException e) {
                return CommonString.MESSAGE_EXCEPTION;
            } catch (IOException e) {
                return CommonString.MESSAGE_SOCKETEXCEPTION;
            } catch (Exception e) {
                return CommonString.MESSAGE_EXCEPTION;
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                if (db.updateCampaignStatusData(jcpMasterGetterSetter, "U") > 0) {
                    prepareList();
                    AlertandMessages.showToastMsg(context, "Campaign Uploaded Successfully");
                }

            } else {
                AlertandMessages.showSnackbarMsg(context, "Error in Campaign uploading ");
            }
        }
    }

}
