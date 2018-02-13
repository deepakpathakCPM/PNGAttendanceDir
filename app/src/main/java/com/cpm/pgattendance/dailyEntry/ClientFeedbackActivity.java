package com.cpm.pgattendance.dailyEntry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.pgattendance.R;
import com.cpm.pgattendance.constant.AlertandMessages;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.database.PNGAttendanceDB;
import com.cpm.pgattendance.getterSetter.QuestionnaireGetterSetter;
import com.cpm.pgattendance.upload.Retrofit_method.UploadImageWithRetrofit;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientFeedbackActivity extends AppCompatActivity {

    Context context;
    FloatingActionButton fab;
    PNGAttendanceDB db;
    SharedPreferences preferences;
    String visit_date, username, msg;
    AnswerAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<QuestionnaireGetterSetter> headerListData;
    ArrayList<QuestionnaireGetterSetter> listDataHeader;
    HashMap<QuestionnaireGetterSetter, ArrayList<QuestionnaireGetterSetter>> listDataChild;
    ArrayList<QuestionnaireGetterSetter> childListData;
    List<Integer> checkHeaderArray = new ArrayList<>();
    boolean checkflag = true;
    LinearLayout ll_recycler, no_data_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_feedback);
        declaration();
        prepareList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.clearFocus();
                if (validateData(listDataHeader)) {
                    new UploadClientFeedbackData(listDataHeader).execute();
                } else {
                    AlertandMessages.showSnackbarMsg(context, msg);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (headerListData.size() > 0) {
                new AlertandMessages((Activity) context, null, null, null).backpressedAlert();
            } else {
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (headerListData.size() > 0) {
            new AlertandMessages((Activity) context, null, null, null).backpressedAlert();
        } else {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
    }

    private void prepareList() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

     /*   headerListData = db.getAfterSaveAuditQuestionAnswerData(store_cd, category_id);
        if (!(headerListData.size() > 0)) {
            headerListData = db.getAuditQuestionData(category_id);
        }
        else{
            btn.setText("Update");
        }*/
        db.open();
        headerListData = db.getQuestionnaireData("2");

        if (headerListData.size() > 0) {
            // Adding child data
            for (int i = 0; i < headerListData.size(); i++) {
                listDataHeader.add(headerListData.get(i));
                db.open();
                childListData = db.getQuestionnaireAnswerData(headerListData.get(i));
                ArrayList<QuestionnaireGetterSetter> answerList = new ArrayList<>();
                for (int j = 0; j < childListData.size(); j++) {
                    answerList.add(childListData.get(j));
                }

                listDataChild.put(listDataHeader.get(i), answerList); // Header, Child data
            }
            adapter = new AnswerAdapter(listDataHeader, listDataChild);
            recyclerView.setAdapter(adapter);
            ll_recycler.setVisibility(View.VISIBLE);
            no_data_lay.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        } else {
            ll_recycler.setVisibility(View.GONE);
            no_data_lay.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        }


    }

    class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
        ArrayList<QuestionnaireGetterSetter> questionList;
        HashMap<QuestionnaireGetterSetter, ArrayList<QuestionnaireGetterSetter>> answerHashMap;
        ArrayList<QuestionnaireGetterSetter> answerList;
        // ArrayList<Audit_QuestionDataGetterSetter> ans_list;

        public AnswerAdapter(ArrayList<QuestionnaireGetterSetter> questionList,
                             HashMap<QuestionnaireGetterSetter, ArrayList<QuestionnaireGetterSetter>> answerHashMap) {
            this.questionList = questionList;
            this.answerHashMap = answerHashMap;
        }

        public AnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_question_item_list, parent, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(final AnswerAdapter.ViewHolder holder, final int position) {
            holder.data = questionList.get(position);

            holder.txt_question.setText(holder.data.getQUESTION().get(0));

            ArrayList<QuestionnaireGetterSetter> ans_list = answerHashMap.get(holder.data);

            if (holder.data.getQUESTION_TYPE().get(0).equalsIgnoreCase("Dropdown")) {
                holder.sp_auditAnswer.setVisibility(View.VISIBLE);
                holder.edt_answer.setVisibility(View.GONE);
            } else {
                holder.edt_answer.setVisibility(View.VISIBLE);
                holder.sp_auditAnswer.setVisibility(View.GONE);
            }

            holder.sp_auditAnswer.setAdapter(new AnswerSpinnerAdapter((Activity) context, R.layout.spinner_text_view, ans_list));

            final ArrayList<QuestionnaireGetterSetter> finalAns_list = ans_list;
            holder.sp_auditAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    QuestionnaireGetterSetter ans = finalAns_list.get(position);
                    holder.data.setSp_AnswerId(ans.getANSWER_ID().get(0));
                    holder.data.setSp_Answer(ans.getANSWER().get(0));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.edt_answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        holder.data.setSp_AnswerId("0");
                        holder.data.setSp_Answer(((EditText) v).getText().toString());
                    }
                }
            });

            for (int i = 0; i < ans_list.size(); i++) {
                if (holder.data.getQUESTION_TYPE().get(0).equalsIgnoreCase("Dropdown") && ans_list.get(i).getANSWER_ID().get(0).equals(holder.data.getSp_AnswerId())) {
                    holder.sp_auditAnswer.setSelection(i);
                    break;
                } else {
                    holder.edt_answer.setText(ans_list.get(i).getANSWER().get(0));
                }
            }

            if (!checkflag) {
                if (checkHeaderArray.contains(position)) {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.card_view.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView txt_question;
            public final Spinner sp_auditAnswer;
            public final EditText edt_answer;
            CardView card_view;
            QuestionnaireGetterSetter data;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                txt_question = (TextView) view.findViewById(R.id.txt_question);
                sp_auditAnswer = (Spinner) view.findViewById(R.id.sp_auditAnswer);
                card_view = (CardView) view.findViewById(R.id.card_view);
                edt_answer = (EditText) view.findViewById(R.id.edt_answer);
            }
        }
    }


    public class AnswerSpinnerAdapter extends ArrayAdapter<QuestionnaireGetterSetter> {
        List<QuestionnaireGetterSetter> list;
        Context context;
        int resourceId;

        public AnswerSpinnerAdapter(Context context, int resourceId, ArrayList<QuestionnaireGetterSetter> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            QuestionnaireGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getANSWER().get(0));

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            QuestionnaireGetterSetter cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getANSWER().get(0));

            return view;
        }
    }

    boolean validateData(ArrayList<QuestionnaireGetterSetter> data) {
        //boolean flag = true;
        checkHeaderArray.clear();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getQUESTION_TYPE().get(0).equalsIgnoreCase("Dropdown") && data.get(i).getSp_AnswerId().equalsIgnoreCase("0")) {

                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                msg = "Please Select Answer from Dropdown";
                checkflag = false;
                break;

            } else if (data.get(i).getQUESTION_TYPE().get(0).equalsIgnoreCase("Freetext") && (data.get(i).getSp_Answer() == null || data.get(i).getSp_Answer().equalsIgnoreCase(""))) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                msg = "Please Fill Answer";
                checkflag = false;
                break;
            } else {
                checkflag = true;
            }

            if (checkflag == false) {
                break;
            }
        }
        return checkflag;
    }


    void declaration() {
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        recyclerView = (RecyclerView) findViewById(R.id.list);
        ll_recycler = (LinearLayout) findViewById(R.id.ll_recycler);
        no_data_lay = (LinearLayout) findViewById(R.id.no_data_lay);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        db = new PNGAttendanceDB(context);
    }

    class UploadClientFeedbackData extends AsyncTask<Void, Void, String> {
        ArrayList<QuestionnaireGetterSetter> listDataHeader;

        UploadClientFeedbackData(ArrayList<QuestionnaireGetterSetter> listDataHeader) {
            this.listDataHeader = listDataHeader;
        }

        private ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setTitle("Client Feedback data");
            dialog.setMessage("Uploading....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String finalxml = "";
                for (int i = 0; i < listDataHeader.size(); i++) {
                    String xml = "[QUESTION]"
                            + "[QUESTION_ID]"
                            + listDataHeader.get(i).getQUESTION_ID().get(0)
                            + "[/QUESTION_ID]"
                            + "[ANSWER]"
                            + listDataHeader.get(i).getSp_Answer()
                            + "[/ANSWER]"
                            + "[CREATED_BY]"
                            + username
                            + "[/CREATED_BY]"
                            + "[VISIT_DATE]"
                            + visit_date
                            + "[/VISIT_DATE]"
                            + "[/QUESTION]";

                    finalxml = finalxml + xml;
                }

                String visitdata = "[USER_DATA]" + finalxml + "[/USER_DATA]";

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                request.addProperty("XMLDATA", visitdata);
                request.addProperty("KEYS", "CLIENTFEEDBACK_DATA");
                request.addProperty("USERNAME", username);
                request.addProperty("MID", 0);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        CommonString.URL);
                androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_XML, envelope);

                Object result = (Object) envelope.getResponse();

                if (!result
                        .toString()
                        .equalsIgnoreCase(
                                CommonString.KEY_SUCCESS)) {

                    return "Failure";

                }

                return CommonString.KEY_SUCCESS;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.cancel();

            if (result.equals(CommonString.KEY_SUCCESS)) {
                Toast.makeText(getApplicationContext(), "Client Feedback Uploaded Successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Data not uploaded!", Toast.LENGTH_LONG).show();
            }

            if (db.saveClientFeedBackData(listDataHeader, username, visit_date) > 0) {
                AlertandMessages.showToastMsg(context, "Data Saved Successfully");
                finish();
                ((Activity) context).overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            } else {
                AlertandMessages.showSnackbarMsg(context, "Error in Data Saving");
            }
        }

    }


}
