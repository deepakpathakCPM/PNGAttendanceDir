package com.cpm.pgattendance.upload.Retrofit_method;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.cpm.pgattendance.R;
import com.cpm.pgattendance.constant.AlertandMessages;
import com.cpm.pgattendance.constant.CommonString;
import com.cpm.pgattendance.database.PNGAttendanceDB;
import com.cpm.pgattendance.gsonGetterSetter.ReferenceVariablesForDownloadActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import okhttp3.MultipartBody;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by deepakp on 12/20/2017.
 */

public class UploadImageWithRetrofit extends ReferenceVariablesForDownloadActivity {

    boolean isvalid;
    RequestBody body1;
    private Retrofit adapter;
    Context context;
    public static int uploadedFiles = 0;
    public int listSize = 0;
    int status = 0;
    PNGAttendanceDB db;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String _UserId, date, app_ver;
    String[] jj;
    boolean statusUpdated = true;
    int from;

    public UploadImageWithRetrofit(Context context) {
        this.context = context;
    }

    public UploadImageWithRetrofit(Context context, PNGAttendanceDB db, ProgressDialog pd, int from) {
        this.context = context;
        this.db = db;
        this.pd = pd;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        this.from = from;
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        date = preferences.getString(CommonString.KEY_DATE, null);
        try {
            app_ver = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.open();
    }


    String getParsedDate(String filename) {
        String testfilename = filename;
        testfilename = testfilename.substring(testfilename.indexOf("-") + 1);
        testfilename = testfilename.substring(0, testfilename.indexOf("-"));
        return testfilename;
    }


    public String UploadImage2(String filename, String foldername, String path) {
        String image_valid = "";
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path + filename, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;

            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(path + filename, o2);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            String ba1 = Base64.encodeBytes(ba);

            SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_IMAGE);
            //String[] split = path.split("/");
            request.addProperty("img", ba1);
            request.addProperty("name", filename);
            request.addProperty("FolderName", foldername);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    CommonString.URL, CommonString.TIMEOUT);

            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_NAME_IMAGE, envelope);
            Object result = (Object) envelope.getResponse();
            System.out.println(result);

          /*  datacheck = result.toString();
            words = datacheck.split("\\;");
            validity = (words[0]);
*/
            if (result.toString().contains("Success")) {

                image_valid = "Success";
                new File(path + filename).delete();
            }
            if (result.toString().equals("Failure")) {

                image_valid = "Failure";

            }

        } catch (MalformedURLException e) {
            image_valid = "Failure";
            e.printStackTrace();
        } catch (IOException e) {
            image_valid = "Failure";
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return image_valid;
    }

    public String UploadBackup(File backupFile, String foldername, String path) {
        String image_valid = "";
        try {
            SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_NAME_IMAGE);
            //String[] split = path.split("/");

          /*  BitmapFactory.Options o2 = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(path + "/" + backupFile.getName(), o2);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            //bitmap.compress(null, 0, bao);
            byte[] ba = bao.toByteArray();
            String ba1 = Base64.encodeBytes(ba);*/
            byte[] b = new byte[(int) backupFile.length()];
            String ba1 = Base64.encodeBytes(b);
            request.addProperty("img", ba1);
            request.addProperty("name", backupFile.getName());
            request.addProperty("FolderName", foldername);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    CommonString.URL, CommonString.TIMEOUT);

            androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_NAME_IMAGE, envelope);
            Object result = (Object) envelope.getResponse();
            System.out.println(result);

          /*  datacheck = result.toString();
            words = datacheck.split("\\;");
            validity = (words[0]);
*/
            if (result.toString().contains("Success")) {

                image_valid = "Success";
                new File(path + "/" + backupFile.getName()).delete();
            }
            if (result.toString().equals("Failure")) {

                image_valid = "Failure";

            }

        } catch (MalformedURLException e) {
            image_valid = "Failure";
            e.printStackTrace();
        } catch (IOException e) {
            image_valid = "Failure";
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
        return image_valid;
    }

    public void uploadBackupWithRetrofit(File backupFile, String foldername, String folderPath, final ProgressDialog pd) {
        try {
            final File finalFile = backupFile;
            if (finalFile.getName().contains("-")) {
                date = getParsedDate(finalFile.getName());
            } else {
                date = date;
            }
            com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
            body1 = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("file", finalFile.getName(), photo)
                    .addFormDataPart("Foldername", foldername)
                    .addFormDataPart("Path", date)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

            //.addNetworkInterceptor(networkInterceptor)


            adapter = new Retrofit.Builder()
                    .baseUrl(CommonString.URL2)
                    .client(okHttpClient)
                    .addConverterFactory(new StringConverterFactory())
                    .build();
            PostApi api = adapter.create(PostApi.class);
            Call<String> call = api.getUploadImageRetrofitOne(body1);
            call.enqueue(new retrofit.Callback<String>() {

                @Override
                public void onResponse(Response<String> response) {
                    if (response.isSuccess() && response.body().contains("Success")) {
                        finalFile.delete();
                        pd.dismiss();
                        AlertandMessages.showToastMsg(context, context.getString(R.string.data_uploaded_successfully));
                    } else {
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, context.getString(R.string.database_not_uploaded), true);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
                    } else {
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, context.getString(R.string.errordatabase_not_uploaded), true);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_EXCEPTION, true);
        }
    }

    class StringConverterFactory implements Converter.Factory {
        private StringConverterFactory() {
        }

        @Override
        public Converter<String> get(Type type) {
            Class<?> cls = (Class<?>) type;
            if (String.class.isAssignableFrom(cls)) {
                return new StringConverter();
            }
            return null;
        }
    }

    private static class StringConverter implements Converter<String> {
        private static final MediaType PLAIN_TEXT = MediaType.parse("text/plain; charset=UTF-8");

        @Override
        public String fromBody(ResponseBody body) throws IOException {
            return new String(body.bytes());
        }

        @Override
        public RequestBody toBody(String value) {
            return RequestBody.create(PLAIN_TEXT, convertToBytes(value));
        }

        private static byte[] convertToBytes(String string) {
            try {
                return string.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
