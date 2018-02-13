package com.cpm.pgattendance.dailyEntry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpm.pgattendance.R;
import com.cpm.pgattendance.constant.AlertandMessages;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.database.PNGAttendanceDB;
import com.cpm.pgattendance.getterSetter.CampaignEntryGetterSetter;
import com.cpm.pgattendance.getterSetter.JCPMasterGetterSetter;
import com.cpm.pgattendance.getterSetter.SpecialActivityGetterSetter;

import java.util.ArrayList;

public class CampaignListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Context context;
    String date, username;
    CampaignListAdapter adapter;
    Intent campaignEntry;
    SharedPreferences preferences;
    PNGAttendanceDB db;
    JCPMasterGetterSetter jcpGetSet;
    ArrayList<SpecialActivityGetterSetter> campaignList;
    LinearLayout storelist_ll, no_data_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_list);
        declaration();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareList();
    }

    void prepareList() {
        db.open();
        campaignList = db.getSpecialActivityData(jcpGetSet.getREGION_CD().get(0));
        if (campaignList.size() > 0) {
            adapter = new CampaignListAdapter(context);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            storelist_ll.setVisibility(View.VISIBLE);
            no_data_ll.setVisibility(View.GONE);
        } else {
            no_data_ll.setVisibility(View.VISIBLE);
            storelist_ll.setVisibility(View.GONE);
        }

    }

    class CampaignListAdapter extends RecyclerView.Adapter<MyViewHolder> {

        LayoutInflater inflater;

        CampaignListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_campaignlist_item_view,parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.textView.setText(campaignList.get(position).getACTIVITY().get(0));
            final ArrayList<CampaignEntryGetterSetter> list = db.getSavedSpecialActivityData(jcpGetSet.getSTORE_CD().get(0), campaignList.get(position).getACTIVITY_CD().get(0), date, username);
            if (list != null && list.size() > 0) {
                holder.tickImg.setVisibility(View.VISIBLE);
            } else {
                holder.tickImg.setVisibility(View.GONE);
            }
            holder.storelist_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (list != null && list.size() > 0) {
                        AlertandMessages.showSnackbarMsg(context, "Data Already Filled");
                    } else {
                        campaignEntry.putExtra(CommonString.TAG_OBJECT, campaignList.get(position));
                        campaignEntry.putExtra(CommonString.TAG_OBJECT1, jcpGetSet);
                        startActivity(campaignEntry);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return campaignList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout storelist_ll;
        ImageView tickImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_storeName);
            tickImg = itemView.findViewById(R.id.tickImg);
            storelist_ll = itemView.findViewById(R.id.storelist_ll);

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

    void declaration() {
        context = this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.campaignList);
        campaignEntry = new Intent(context, CampaignEntryActivity.class);
        db = new PNGAttendanceDB(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, "");
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        storelist_ll = (LinearLayout) findViewById(R.id.storelist);
        no_data_ll = (LinearLayout) findViewById(R.id.no_data_lay);
        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            jcpGetSet = (JCPMasterGetterSetter) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        }
    }
}
