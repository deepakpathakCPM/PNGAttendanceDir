package com.cpm.pgattendance.dailyEntry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.pgattendance.R;
import com.cpm.pgattendance.constant.CommonFunctions;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.database.PNGAttendanceDB;
import com.cpm.pgattendance.getterSetter.VisitorDetailGetterSetter;
import com.cpm.pgattendance.getterSetter.VisitorSearchGetterSetter;
import com.cpm.pgattendance.upload.Retrofit_method.UploadImageWithRetrofit;
import com.cpm.pgattendance.xmlHandler.XMLHandlers;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class VisitorLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private PNGAttendanceDB database;
    LinearLayout heading;
    FloatingActionButton fab_save;
    RecyclerView recyclerView;
    VisitorDetailGetterSetter visitorLoginGetterSetter;
    ArrayList<VisitorDetailGetterSetter> visitorLoginData = new ArrayList<>();
    TextView tv_in_time, tv_out_time;
    String empid, emp_name, name, designation;
    boolean isUpdate = false;
    String intime_img, outtime_img;
    TextView tvname, tvdesignation;
    String error_msg = "";
    protected static String _pathforcheck = "";
    protected String _path, str;
    boolean ResultFlag = true;
    boolean camin_clicked = false, camout_clicked = false;
    Activity activity;
    ProgressBar progressBar;
    String visit_date, username;
    RelativeLayout rel_intime, rel_outtime;
    ImageView img_intime, img_outtime;
    SharedPreferences preferences;
    String Path;
    ImageView imgcam_in, imgcam_out;
    Context context;
    VisitorDetailGetterSetter vistorObject;
    Button btnclear, btngo;
    int eventType;
    VisitorSearchGetterSetter visitordata;
    EditText et_emp_name;
    MyRecyclerAdapter adapter;
    ArrayList<VisitorDetailGetterSetter> visitorListData;
    Dialog dialog_list;
    ArrayList<VisitorDetailGetterSetter> list;
    String result1;
    TextView tv_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        declaration();
        //prepareList();
       /* visitorSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    tvname.setText(list.get(position).getName());
                    tvdesignation.setText(list.get(position).getDesignation());
                    empid = list.get(position).getEmp_code();
                    name = list.get(position).getName();
                    designation = list.get(position).getDesignation();

                } else {
                    tvname.setText("");
                    tvdesignation.setText("");
                    empid = null;
                    name = null;
                    designation = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

    }

    void prepareList() {

        list = database.getVisitorData();

        //------------for state Master List---------------
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        for (int i = 0; i < list.size(); i++) {
            arrayAdapter.add(list.get(i).getName());
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //visitorSpn.setAdapter(arrayAdapter);
        //------------------------------------------------

    }

    void declaration() {
        activity = this;
        context = this;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_user_id = (TextView) findViewById(R.id.tv_user_id);
        btngo = (Button) findViewById(R.id.btngo);
        btnclear = (Button) findViewById(R.id.btnClear);
        et_emp_name = (EditText) findViewById(R.id.et_emp_name);
        tvname = (TextView) findViewById(R.id.tv_name);
        tvdesignation = (TextView) findViewById(R.id.tv_designation);
        imgcam_in = (ImageView) findViewById(R.id.imgcam_intime);
        imgcam_out = (ImageView) findViewById(R.id.imgcam_outtime);
        tv_in_time = (TextView) findViewById(R.id.tvintime);
        tv_out_time = (TextView) findViewById(R.id.tvouttime);
        rel_intime = (RelativeLayout) findViewById(R.id.rel_intime);
        rel_outtime = (RelativeLayout) findViewById(R.id.rel_outtime);
        fab_save = (FloatingActionButton) findViewById(R.id.fab);
        img_intime = (ImageView) findViewById(R.id.img_intime);
        img_outtime = (ImageView) findViewById(R.id.img_outtime);
        progressBar = (ProgressBar) findViewById(R.id.progress_empid);
        heading = (LinearLayout) findViewById(R.id.lay_heading);
        recyclerView = (RecyclerView) findViewById(R.id.rv_visitor);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        database = new PNGAttendanceDB(this);
        database.open();

        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        username = preferences.getString(CommonString.KEY_USERNAME, "");
        tv_user_id.setText("Current User - " + username);

        str = CommonString.FILE_PATH;

        fab_save.setOnClickListener(this);
        rel_intime.setOnClickListener(this);
        rel_outtime.setOnClickListener(this);
        btnclear.setOnClickListener(this);
        btngo.setOnClickListener(this);
        Path = CommonString.FILE_PATH;

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int id = v.getId();

        switch (id) {
            case R.id.fab:
                name = tvname.getText().toString();
                designation = tvdesignation.getText().toString();
                if (check_condition()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            VisitorLoginActivity.this);
                    builder.setMessage("Do you want to save the data ")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {

                                            if (isUpdate) {

                                                String out_time = tv_out_time.getText().toString();
                                                visitorLoginGetterSetter.setOut_time(out_time);

                                                database.updateOutTimeVisitorLoginData(visitorLoginGetterSetter.getOut_time_img(), out_time, String.valueOf(visitorLoginGetterSetter.getEmpId()));

                                                if (CheckNetAvailability()) {
                                                    new UploadVisitorData().execute();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No internet connection! try again later", Toast.LENGTH_LONG).show();
                                                    clearVisitorData();
                                                    setLoginData();
                                                }

                                            } else {

                                                visitorLoginGetterSetter = new VisitorDetailGetterSetter();
                                                visitorLoginGetterSetter.setEmpId(Integer.valueOf(empid));
                                                visitorLoginGetterSetter.setName(name);
                                                visitorLoginGetterSetter.setDesignation(designation);
                                                visitorLoginGetterSetter.setVisit_date(visit_date);
                                                visitorLoginGetterSetter.setIn_time_img(intime_img);
                                                visitorLoginGetterSetter.setIn_time(tv_in_time.getText().toString());
                                                String out_time = tv_out_time.getText().toString();
                                                if (out_time.equalsIgnoreCase("Out Time")) {
                                                    visitorLoginGetterSetter.setOut_time("");
                                                } else {
                                                    visitorLoginGetterSetter.setOut_time(out_time);
                                                }


                                                visitorLoginGetterSetter.setEmp_code(empid);
                                                if (outtime_img == null) {
                                                    visitorLoginGetterSetter.setOut_time_img("");
                                                } else {
                                                    visitorLoginGetterSetter.setOut_time_img(outtime_img);
                                                }

                                                visitorLoginData.add(visitorLoginGetterSetter);
                                                database.open();
                                                database.InsertVisitorLogindata(visitorLoginData);

                                                clearVisitorData();

                                                setLoginData();

                                            }


                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    if (error_msg.equals("Employee ID already entered")) {
                        clearVisitorData();
                    }
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.rel_intime:
                if (et_emp_name.getText().toString() != null && !et_emp_name.getText().toString().equalsIgnoreCase("")) {
                    camin_clicked = true;
                    _pathforcheck = username + getCurrentTime() + "visitor_intime" + ".jpg";
                    //imageV = _pathforcheck;
                    _path = str + _pathforcheck;
                    CommonFunctions.startCameraActivity((Activity) context, _path);
                    //startCameraActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill employee code first", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.rel_outtime:
                //if(!isUpdate && (intime_img == null)){
                if (!isUpdate) {
                    error_msg = "Please click Out Time image at out time";
                    Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();

                } else {
                    camout_clicked = true;
                    _pathforcheck = username + getCurrentTime() + "visitor_outtime" + ".jpg";
                    //imageV = _pathforcheck;
                    _path = str + _pathforcheck;
                    CommonFunctions.startCameraActivity((Activity) context, _path);
                    //startCameraActivity();
                }

                break;
            case R.id.btngo:

                emp_name = et_emp_name.getText().toString().trim().replaceAll("[&^<>{}'$]", "");
                if (emp_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter Employee Name", Toast.LENGTH_SHORT).show();
                } else if (CheckNetAvailability()) {
                    new GetCredentials().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection! try again later", Toast.LENGTH_LONG).show();
                }

                break;


            case R.id.btnClear:

                clearVisitorData();

                break;
        }

    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();

        String intime = m_cal.get(Calendar.DAY_OF_MONTH) + "" + m_cal.get(Calendar.MONTH) + "" + m_cal.get(Calendar.YEAR) + "" + m_cal.get(Calendar.HOUR_OF_DAY) + ""
                + m_cal.get(Calendar.MINUTE) + "" + m_cal.get(Calendar.SECOND);

        return intime;

    }

    public boolean CheckNetAvailability() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            // we are connected to a network
            connected = true;
        }
        return connected;
    }

    class GetCredentials extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(VisitorLoginActivity.this);
            dialog.setTitle("Employee Id");
            dialog.setMessage("Fetching....");
            dialog.setCancelable(false);
            dialog.show();


        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {

                // JCP

                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE,
                        CommonString.METHOD_NAME_UNIVERSAL_DOWNLOAD);
                request.addProperty("UserName", emp_name);
                request.addProperty("Type", "VISITOR_SEARCH");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL, CommonString.TIMEOUT);

                androidHttpTransport.call(
                        CommonString.SOAP_ACTION_UNIVERSAL, envelope);


                Object result = (Object) envelope.getResponse();

                // for failure
                xpp.setInput(new StringReader(result.toString()));
                xpp.next();
                eventType = xpp.getEventType();

                visitordata = XMLHandlers.visitorDataXML(xpp, eventType);

                if (visitordata.getEMP_CD().size() == 0) {
                    return CommonString.KEY_NO_DATA;
                } else {
                    return CommonString.KEY_SUCCESS;
                }


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return CommonString.MESSAGE_SOCKETEXCEPTION;
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return CommonString.MESSAGE_INVALID_XML;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return CommonString.MESSAGE_EXCEPTION;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            dialog.cancel();

            if (result.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                dialog.dismiss();
                if (visitordata != null && visitordata.getEMP_CD().size() > 0) {
                    tvname.setText(visitordata.getEMPLOYEE().get(0));
                    tvdesignation.setText(visitordata.getDESIGNATION().get(0));
                    empid = String.valueOf(visitordata.getEMP_CD().get(0));
                    name = visitordata.getEMPLOYEE().get(0);
                    designation = visitordata.getDESIGNATION().get(0);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please enter valid Employee Code", Toast.LENGTH_LONG).show();
                }

            } else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error : " + result, Toast.LENGTH_LONG).show();
            }


        }

    }


    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

        private LayoutInflater inflator;

        List<VisitorDetailGetterSetter> data = Collections.emptyList();

        public MyRecyclerAdapter(Context context, List<VisitorDetailGetterSetter> data) {

            inflator = LayoutInflater.from(context);
            this.data = data;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.item_visitor_search, parent, false);

            MyRecyclerAdapter.MyViewHolder holder = new MyRecyclerAdapter.MyViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            final VisitorDetailGetterSetter current = data.get(position);

            holder.name.setText(current.getName());
            holder.designation.setText(current.getDesignation());
            holder.emp_code.setText(current.getEmp_code());

            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvname.setText(current.getName());
                    tvdesignation.setText(current.getDesignation());
                    empid = current.getEmp_code();
                    name = current.getName();
                    designation = current.getDesignation();
                    dialog_list.cancel();

                }
            });
        }

        @Override
        public int getItemCount() {
            return visitorListData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name, designation, emp_code;
            LinearLayout parent_layout;

            public MyViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_name);
                designation = (TextView) itemView.findViewById(R.id.tv_designation);
                emp_code = (TextView) itemView.findViewById(R.id.tv_emp_code);
                parent_layout = (LinearLayout) itemView.findViewById(R.id.layout_parent);

            }

        }
    }

    public void clearVisitorData() {

        et_emp_name.setText("");
        tvname.setText("");
        tvdesignation.setText("");
        tv_in_time.setText("");
        tv_out_time.setText("");
        img_intime.setVisibility(View.GONE);
        img_outtime.setVisibility(View.GONE);

        rel_intime.setVisibility(View.VISIBLE);
        rel_outtime.setVisibility(View.VISIBLE);

        empid = null;
        name = null;
        designation = null;
        intime_img = null;
        outtime_img = null;
        fab_save.setClickable(true);
        fab_save.setVisibility(View.VISIBLE);
        rel_intime.setClickable(true);
        rel_outtime.setClickable(true);
        btnclear.setVisibility(View.INVISIBLE);

        isUpdate = false;

    }

    protected void startCameraActivity() {

        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(intent, 0);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:

                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {

                        if (camin_clicked) {
                            intime_img = _pathforcheck;

                            tv_in_time.setText(getClicktime());

                            _pathforcheck = "";
                            camin_clicked = false;

                            rel_intime.setVisibility(View.GONE);
                            rel_intime.setClickable(false);

                            setScaledImage(img_intime, _path);
                            img_intime.setVisibility(View.VISIBLE);

                        } else if (camout_clicked) {

                            visitorLoginGetterSetter.setOut_time_img(_pathforcheck);
                            tv_out_time.setText(getClicktime());

                            _pathforcheck = "";
                            camout_clicked = false;

                            rel_outtime.setVisibility(View.GONE);
                            rel_outtime.setClickable(false);

                            img_outtime.setVisibility(View.VISIBLE);
                            setScaledImage(img_outtime, _path);

                        }

                        break;

                    }
                }

                break;
        }
    }

    public String getClicktime() {

        Calendar m_cal = Calendar.getInstance();
        String time = m_cal.get(Calendar.HOUR_OF_DAY) + ":" + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);

        return time;
    }

    private void setScaledImage(ImageView imageView, final String path) {
        final ImageView iv = imageView;
        ViewTreeObserver viewTreeObserver = iv.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                int imageViewHeight = iv.getMeasuredHeight();
                int imageViewWidth = iv.getMeasuredWidth();
                iv.setImageBitmap(decodeSampledBitmapFromPath(path, imageViewWidth, imageViewHeight));
                return true;
            }
        });
    }

    private static Bitmap decodeSampledBitmapFromPath(String path,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);

        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public boolean check_condition() {

        if (isUpdate) {
            if (visitorLoginGetterSetter.getOut_time_img().equals("")) {

                error_msg = "Please click out time image";
                return false;
            } else {
                return true;
            }
        } else {
            if (empid == null || name == null || designation == null) {
                error_msg = "Please Select Employee";
                return false;
            } else if (intime_img == null) {
                error_msg = "Please click in time image";
                return false;
            } else if (database.isVistorDataExists(empid)) {
                error_msg = "Employee already entered";
                return false;
            } else {
                return true;
            }
        }
    }

    public void setLoginData() {

        visitorLoginData = database.getVisitorLoginData(visit_date);

        if (visitorLoginData.size() > 0) {

            heading.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext()));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            heading.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        Context context;
        LayoutInflater inflater;

        RecyclerAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.child_visitor_login, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv_name.setText(visitorLoginData.get(position).getName());
            holder.tv_intime.setText(visitorLoginData.get(position).getIn_time());
            holder.tv_outtime.setText(visitorLoginData.get(position).getOut_time());
            if (visitorLoginData.get(position).getUpload_status() != null && visitorLoginData.get(position).getUpload_status().equals("U")) {
                holder.img_upload_tick.setVisibility(View.VISIBLE);
            } else {
                holder.img_upload_tick.setVisibility(View.INVISIBLE);
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    visitorLoginGetterSetter = visitorLoginData.get(position);

                    if (visitorLoginGetterSetter.getUpload_status() != null && visitorLoginGetterSetter.getUpload_status().equalsIgnoreCase("U")) {
                        Toast.makeText(getApplicationContext(), "Data already uploaded", Toast.LENGTH_SHORT).show();
                    } else {

                        btnclear.setVisibility(View.VISIBLE);
                        tvname.setText(visitorLoginGetterSetter.getName());
                        tvdesignation.setText(visitorLoginGetterSetter.getDesignation());

                      /*  for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getName().equalsIgnoreCase(visitorLoginGetterSetter.getName())) {
                                visitorSpn.setSelection(i);
                            }
                        }*/

                        et_emp_name.setText(visitorLoginGetterSetter.getName());


                        tv_in_time.setText(visitorLoginGetterSetter.getIn_time());
                        et_emp_name.setFocusable(false);
                        String intime_img = visitorLoginGetterSetter.getIn_time_img();
                        if (intime_img != null && !intime_img.equals("")) {

                            rel_intime.setVisibility(View.GONE);
                            rel_intime.setClickable(false);
                            setScaledImage(img_intime, str + intime_img);
                            img_intime.setVisibility(View.VISIBLE);
                        }

                        String outtime_img = visitorLoginGetterSetter.getOut_time_img();
                        if (outtime_img != null && !outtime_img.equals("")) {

                            rel_outtime.setVisibility(View.GONE);
                            rel_outtime.setClickable(false);
                            tv_out_time.setText(visitorLoginGetterSetter.getOut_time());
                            setScaledImage(img_outtime, str + outtime_img);
                            img_outtime.setVisibility(View.VISIBLE);
                        } else {
                            tv_out_time.setText(visitorLoginGetterSetter.getOut_time());
                            rel_outtime.setVisibility(View.VISIBLE);
                            img_outtime.setVisibility(View.GONE);
                            rel_outtime.setClickable(true);
                        }
                        //  fab_save.setText("Upload");
                        if (!visitorLoginGetterSetter.getOut_time().equals("")) {
                            fab_save.setClickable(true);
                            fab_save.setVisibility(View.VISIBLE);
                        }
                        isUpdate = true;

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return visitorLoginData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_name;
            TextView tv_intime;
            TextView tv_outtime;
            LinearLayout layout;
            ImageView img_upload_tick;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_intime = (TextView) itemView.findViewById(R.id.tv_intime);
                tv_outtime = (TextView) itemView.findViewById(R.id.tv_outtime);
                layout = (LinearLayout) itemView.findViewById(R.id.ll_item);
                img_upload_tick = (ImageView) itemView.findViewById(R.id.img_upload_tick);
            }
        }
    }

    //upload Visitor data

    class UploadVisitorData extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = new ProgressDialog(VisitorLoginActivity.this);
            dialog.setTitle("Visitor data");
            dialog.setMessage("Uploading....");
            dialog.setCancelable(false);
            dialog.show();


        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                UploadImageWithRetrofit uploadRetro = new UploadImageWithRetrofit(context);

                String visitdata = "[USER_DATA][CREATED_BY]"
                        + username
                        + "[/CREATED_BY][EMP_ID]"
                        + visitorLoginGetterSetter.getEmp_code()
                        + "[/EMP_ID][VISIT_DATE]"
                        + visit_date
                        + "[/VISIT_DATE][IN_TIME_IMAGE]"
                        + visitorLoginGetterSetter.getIn_time_img()
                        + "[/IN_TIME_IMAGE][OUT_TIME_IMAGE]"
                        + visitorLoginGetterSetter.getOut_time_img()
                        + "[/OUT_TIME_IMAGE][IN_TIME]"
                        + visitorLoginGetterSetter.getIn_time()
                        + "[/IN_TIME][OUT_TIME]"
                        + visitorLoginGetterSetter.getOut_time()
                        + "[/OUT_TIME]"
                        + "[/USER_DATA]";


                XmlPullParserFactory factory = XmlPullParserFactory
                        .newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_XML);
                request.addProperty("XMLDATA", visitdata);
                request.addProperty("KEYS", "MER_VISITOR_LOGIN");
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


                if (visitorLoginGetterSetter.getIn_time_img() != null
                        && !visitorLoginGetterSetter.getIn_time_img()
                        .equals("")) {

                    if (new File(CommonString.FILE_PATH + visitorLoginGetterSetter.getIn_time_img()).exists()) {
                        //result = UploadImage(addNewStoreList.get(j).getImg_Camera(), "StoreImages");
                        result = uploadRetro.UploadImage2(visitorLoginGetterSetter.getIn_time_img(), "Visitor", CommonString.FILE_PATH);
                        if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                                return "Visitor Images";
                            } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return "Visitor Images";
                            } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                throw new IOException();
                            }
                        }
                        runOnUiThread(new Runnable() {

                            public void run() {
                                // message.setText("Visitor Images Uploaded");
                            }
                        });

                    }
                }

                if (visitorLoginGetterSetter.getOut_time_img() != null
                        && !visitorLoginGetterSetter.getOut_time_img()
                        .equals("")) {

                    if (new File(CommonString.FILE_PATH + visitorLoginGetterSetter.getOut_time_img()).exists()) {
                        //result = UploadImage(addNewStoreList.get(j).getImg_Camera(), "StoreImages");
                        result = uploadRetro.UploadImage2(visitorLoginGetterSetter.getOut_time_img(), "Visitor", CommonString.FILE_PATH);
                        if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                            if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                                return "Visitor Images";
                            } else if (result.toString().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                return "Visitor Images";
                            } else if (result.toString().equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                throw new IOException();
                            }
                        }

                        runOnUiThread(new Runnable() {

                            public void run() {
                                // message.setText("Visitor Images Uploaded");
                            }
                        });

                    }
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

                Toast.makeText(getApplicationContext(), "Visit Data Uploaded", Toast.LENGTH_LONG).show();
                //database.deleteOneVisitorData(visitordata.getEmp_id());
                //update upload_status to U
                database.updateVisitorUploadData(visitorLoginGetterSetter.getEmp_code());
                clearVisitorData();

                setLoginData();
            } else {
                Toast.makeText(getApplicationContext(), "Data not uploaded!", Toast.LENGTH_LONG).show();
            }


        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        setLoginData();
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
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }


}
