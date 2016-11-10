package org.openalpr.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Area;
import models.Payment;
import util.HttpHandler;

import static org.openalpr.app.AppConstants.*;


public class MainActivity extends Activity implements AsyncListener<AlprResult> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private String mCurrentPhotoPath;
    private ImageView mImageView;

    private EditText plate;
    private EditText processingTime;
    private EditText confidence;

    private TextView errorText;

    ImageView ivPaidStatePaid;
    ImageView ivPaidStateUnpaid;
    ImageView ivPaidState;

    private ProgressDialog progressDialog;

    private Payment payment;
    List<Area> areas;
    private String scannedLicencePlate = "S 123 AB";

    Button.OnClickListener takePhotoBtnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            dispatchTakePictureIntent();
        }
    };

    private static String CHECKPAYMENT_URL = "http://192.168.1.101:8080/checkPayment"; // GET
    private static String PARKING_AREA = "1";

    private static String CHECKAREAS_URL = "http://192.168.1.101:8080/getAreas"; // GET

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        areas = new ArrayList<Area>();

        if (!PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext())
                .getBoolean(PREF_INSTALLED_KEY, false)) {

            PreferenceManager.getDefaultSharedPreferences(
                    getApplicationContext())
                    .edit().putBoolean(PREF_INSTALLED_KEY, true).commit();

            /*PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                    edit().putString(RUNTIME_DATA_DIR_ASSET, ANDROID_DATA_DIR).commit();*/

            copyAssetFolder(getAssets(), RUNTIME_DATA_DIR_ASSET,
                    ANDROID_DATA_DIR + File.separatorChar + RUNTIME_DATA_DIR_ASSET);
        }

        setContentView(R.layout.activity_main);

        mImageView = (ImageView)findViewById(R.id.imageView);

        plate = (EditText)findViewById(R.id.plateNumberId);
        processingTime = (EditText)findViewById(R.id.processingTimeId);
        confidence = (EditText)findViewById(R.id.plateConfidenceLabelId);


        errorText = (TextView)findViewById(R.id.errorTextView);

        //Button takePicBtn = (Button)findViewById(R.id.button);
        final ImageButton ib_checkpayment = (ImageButton) findViewById(R.id.ib_checkpayment);
        setBtnListenerOrDisable(ib_checkpayment, takePhotoBtnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE);

        ivPaidStatePaid = (ImageView) findViewById(R.id.iv_paid_valid);
        ivPaidStateUnpaid = (ImageView) findViewById(R.id.iv_paid_invalid);
        ivPaidState = (ImageView) findViewById(R.id.iv_paid_open);
        resetPaidState();

        // Check payment
        //final Button button = (Button) findViewById(R.id.buttonWS);
        final ImageView ivCheckPayment = (ImageView) findViewById(R.id.ivCheckPayment);
        ivCheckPayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new CheckPayment().execute();
            }
        });

        // start Area Update
        new CheckAreas().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private File getStorageDir(){
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = getExternalFilesDir(null);
            /*if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("camera-app-photos", "failed to create directory");
                        return null;
                    }
                }
            }*/

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File storageDir = getStorageDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, storageDir);

        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            //mCurrentPhotoPath = null;
        }
    }

    /**
     * calculate needed rotation of image
     * @param filepath
     * @return
     */
    public static int neededRotation(String filepath)
    {
        try
        {
            ExifInterface exif = new ExifInterface(filepath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            { return 270; }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            { return 180; }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            { return 90; }
            return 0;

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }


    private void setPaidState(boolean paid) {

        ivPaidState.setVisibility(View.GONE);

        if (paid) {
            ivPaidStatePaid.setVisibility(View.VISIBLE);
            ivPaidStateUnpaid.setVisibility(View.GONE);
        } else {
            ivPaidStatePaid.setVisibility(View.GONE);
            ivPaidStateUnpaid.setVisibility(View.VISIBLE);
        }
    }

    private void resetPaidState() {

        ivPaidState.setVisibility(View.VISIBLE);
        ivPaidStatePaid.setVisibility(View.GONE);
        ivPaidStateUnpaid.setVisibility(View.GONE);
    }


    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;


		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        int rotation = neededRotation(mCurrentPhotoPath);

        /* rotate photo */
        Log.d(LOG_TAG, "Rotate Photo");
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(mCurrentPhotoPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

        } catch (FileNotFoundException e1) {
            Log.d(LOG_TAG,"FileNotFound Exception");
            e1.printStackTrace();
        } catch (IOException e) {
            Log.d(LOG_TAG,"IO Exception");
            e.printStackTrace();
        }


		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

    private void dispatchTakePictureIntent() {
        setErrorText("");
        clearData();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = null;

        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {

            String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar +
                    RUNTIME_DATA_DIR_ASSET + File.separatorChar +OPENALPR_CONF_FILE;
            handleBigCameraPhoto();

            // default eu
            String parameters[] = {"eu", "", this.mCurrentPhotoPath, openAlprConfFile, "1"};
            Bundle args = new Bundle();
            args.putStringArray(ALPR_ARGS, parameters);
            AlprFragment alprFragment = (AlprFragment)getFragmentManager()
                    .findFragmentByTag(ALPR_FRAGMENT_TAG);

            if(alprFragment == null){
                alprFragment = new AlprFragment();
                alprFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(alprFragment, ALPR_FRAGMENT_TAG);
                transaction.commitAllowingStateLoss();
            }
        }
    }


    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            ImageButton btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        }
    }


    public void setPlate(String plate) {

        scannedLicencePlate = plate;
        this.plate.setText(plate);
    }

    public void setProcessingTime(long processingTime){
        this.processingTime.setText(String.format("%d %s", processingTime, "ms"));
    }

    public void setConfidence(double confidence) {
        this.confidence.setText(String.format("%.0f", confidence, " %"));
    }

    private void setErrorText(String text) {
        errorText.setText(text);
    }

    private void clearData(){
        plate.setText("");
        processingTime.setText("");
    }

    @Override
    public void onPreExecute() {
        onProgressUpdate();
    }

    @Override
    public void onProgressUpdate() {

        //if(progressDialog == null){
            prepareProgressDialog("Processing Image data");
        //}
    }

    @Override
    public void onPostExecute(AlprResult alprResult) {
        if(alprResult.isRecognized()) {
            List<AlprResultItem> resultItems = alprResult.getResultItems();
            if (resultItems.size() > 0) {
                AlprResultItem resultItem = resultItems.get(0);
                setPlate(resultItem.getPlate());
                setProcessingTime(alprResult.getProcessingTime());
                setConfidence(resultItem.getConfidence());

                dismissProgressDialog();

                // Check payment
                new CheckPayment().execute();
            }
            cleanUp();
        }else {
            setErrorText(getString(R.string.recognition_error));
            dismissProgressDialog();
            cleanUp();
        }
    }

    private void cleanUp() {
        
        FragmentManager fm = getFragmentManager();
        AlprFragment alprFragment = (AlprFragment) fm.findFragmentByTag(ALPR_FRAGMENT_TAG);
        fm.beginTransaction().remove(alprFragment).commitAllowingStateLoss();
    }

    private void prepareProgressDialog(String message){

        Log.d(LOG_TAG,"Show progress dialog for:" + message);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();

    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class CheckPayment extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            prepareProgressDialog("Check Payment");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr;
            try {
                jsonStr = sh.makeServiceCall(CHECKPAYMENT_URL + "/" + PARKING_AREA + "/" + URLEncoder.encode(scannedLicencePlate,"UTF-8").replaceAll("\\+", "%20"), "GET");
            } catch (final UnsupportedEncodingException ex) {
                Log.e(LOG_TAG, "URL Encoding Problem UnsupportedEncodingException with: " + scannedLicencePlate);
                return null;
            }

            Log.e(LOG_TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    payment = new Payment(jsonObj);

                } catch (final JSONException e) {
                    Log.e(LOG_TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(LOG_TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dismissProgressDialog();

            TextView quoteText = (TextView)findViewById(R.id.tvCheckPaymentId);

            if (payment != null) {
                quoteText.setText(payment.toString());
                //setPaidState(payment.getPaying());

                setPaidState(true);;

            } else {
                Log.d(LOG_TAG,"Payment empty");
            }
        }

    }

    private void dismissProgressDialog() {

        Log.d(LOG_TAG,"Dismiss Progress dialog");
        if (progressDialog != null) {
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class CheckAreas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            prepareProgressDialog("Check Payment");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(CHECKAREAS_URL, "GET");

            Log.e(LOG_TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray array = new JSONArray(jsonStr);
                    for (int i = 0; i < array.length(); i++) {
                        Area area = new Area(array.getJSONObject(i));
                        areas.add(area);
                    }
                } catch (final JSONException e) {
                    Log.e(LOG_TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(LOG_TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dismissProgressDialog();

            TextView areaInfo = (TextView)findViewById(R.id.tvAreaInfoId);

            if (areas != null && areas.size() > 0) {
                areaInfo.setText(areas.get(0).toString());
            } else {
                Log.d(LOG_TAG,"Areas empty");
            }
        }

    }


}
