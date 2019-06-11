package com.example.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AddReceiptActivity extends Activity {

    private static final String CLOUD_VISION_API_KEY = BuildConfig.API_KEY;
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    ImageButton btn_camera;
    ImageButton btn_photo;
    ImageButton btn_cancel;
    ImageButton btn_write;
    TextView btn_change;
    TextView btn_cancel2;
    Spinner spinner;

    String userID;
    String myhId = null;
    String recepitFlag = "";
    int original = -1;


    LinearLayout parentLayout;

    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_receipt);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        recepitFlag = intent.getStringExtra("receiptFlag");

        if(recepitFlag.equals("F")) {
            LinearLayout initialLayout = (LinearLayout) findViewById(R.id.layout_initial);
            LinearLayout resultLayout = (LinearLayout) findViewById(R.id.layout_result);
            initialLayout.setVisibility(View.GONE);
            resultLayout.setVisibility(View.VISIBLE);
        }

        btn_camera =(ImageButton)findViewById(R.id.cameraBtn);
        btn_photo = (ImageButton)findViewById(R.id.photoBtn);
        btn_cancel = (ImageButton)findViewById(R.id.cancelBtn);
        spinner =(Spinner)findViewById(R.id.categorySpinner);
        btn_change = (TextView)findViewById(R.id.change_button);
        btn_cancel2 = (TextView)findViewById(R.id.cancel_button);

        btn_write = (ImageButton)findViewById(R.id.writeBtn);
        arrayList = new ArrayList();
        arrayList.add("술/유흥");
        arrayList.add("생활(쇼핑)");
        arrayList.add("교통");
        arrayList.add("주거/통신");
        arrayList.add("의료/건강");
        arrayList.add("금융");
        arrayList.add("문화/여가");
        arrayList.add("여행/숙박");
        arrayList.add("식비");
        arrayList.add("카페/간식");
        arrayList.add("미분류");
        arrayList.add("계좌입금");
        arrayList.add("계좌출금");

        SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), arrayList);
        spinner.setAdapter(adapter);
        spinner.setSelection(10);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }

        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryChooser();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout initialLayout = (LinearLayout) findViewById(R.id.layout_initial);
                LinearLayout resultLayout = (LinearLayout) findViewById(R.id.layout_result);
                initialLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.VISIBLE);
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout initialLayout = (LinearLayout) findViewById(R.id.layout_initial);
                LinearLayout resultLayout = (LinearLayout) findViewById(R.id.layout_result);
                initialLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.VISIBLE);


                EditText store = (EditText) findViewById(R.id.storeEdit);
                EditText date = (EditText) findViewById(R.id.dateEdit);
                EditText dateTime = findViewById(R.id.dateTimeEdit);
                EditText cost = (EditText) findViewById(R.id.moneyEdit);
                Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);

                // 시간 분 0 없어도댐
                // 날짜 시간 사이 공백
                //yyyy-mm-dd hh:mm
                String dateStr = date.getText().toString();
                String dateTimeStr = dateTime.getText().toString();


                String yearStr = dateStr.substring(0, 4);
                String monthStr = dateStr.substring(4, 6);
                String dayStr = dateStr.substring(6);
                String hourStr = dateTimeStr.substring(0, 2);
                String minStr = dateTimeStr.substring(2);

                int stopFlag = 0;
                if (Integer.parseInt(monthStr) > 12 || Integer.parseInt(dayStr) > 31) {
                    Toast.makeText(getApplicationContext(), "유효하지 않은 날짜 입니다", Toast.LENGTH_SHORT).show();
                    stopFlag = 1;
                }
                if (Integer.parseInt(hourStr) > 24 || Integer.parseInt(minStr) > 59) {
                    Toast.makeText(getApplicationContext(), "유효하지 않은 시간 입니다", Toast.LENGTH_SHORT).show();
                    stopFlag = 1;
                }

                // 0 1 2 3
                String resultDate = yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minStr;



                if (stopFlag == 0) {
                    //상정명 확인 요청
                    ReceiptRequest test = new ReceiptRequest(store.getText().toString(), RequestInfo.RequestType.STORE_CHECK, getApplicationContext());
                    test.StoreRequest(cId -> {
                        if (!cId.equals("null")) {
//                        spinner.setSelection(Integer.parseInt(cId)-1);
                            original = Integer.parseInt(cId);
                        }
                        if (original != -1) {
                            if (original == spinner.getSelectedItemPosition() + 1) {
                                //영수증 추가 요청시 cId가 있으며 cId를 바꾸지 않은 경우
                                ReceiptRequest test2 = new ReceiptRequest(resultDate, cost.getText().toString(), store.getText().toString(), userID, Integer.toString(spinner.getSelectedItemPosition() + 1), RequestInfo.RequestType.ADD_RECEIPT, getApplicationContext());
                                test2.AddReceipt(hId -> {
                                    myhId = hId;
                                    Toast.makeText(getApplicationContext(), "영수증 추가 성공", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent();
                                    setResult(1, intent);
                                    finish();
                                });
                            } else {
                                //영수증 추가 요청시 cId가 있으며 cId를 바꾼 경우에만 카테고리 변경을 요청
                                final String defaultcId = Integer.toString(original);
                                final Context context = getApplicationContext();
                                //영수증 추가 요청 - 사용자가 카테고리 변경했을 경우
                                ReceiptRequest test4 = new ReceiptRequest(resultDate, cost.getText().toString(), store.getText().toString(), userID, Integer.toString(spinner.getSelectedItemPosition() + 1), RequestInfo.RequestType.ADD_RECEIPT, getApplicationContext());
                                test4.AddReceipt(hId -> {

                                    // /////////////////////카테고리 변경 요청////////////////////////////
                                    ///////////////////////////////////////////////////////////////////
                                    CategoryRequest categoryRequest = new CategoryRequest(
                                            userID,                    //사용자 아이디
                                            Integer.parseInt(hId),          //hId
                                            Integer.parseInt(defaultcId),   //기존 카테고리 번호
                                            spinner.getSelectedItemPosition() + 1,                  //바꿀 카테고리 번호
                                            context,                        //context 고정
                                            RequestInfo.RequestType.UPDATE_CATEGORY);   //고정

                                    categoryRequest.UpdateCategoryHandler(new CategoryRequest.VolleyCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(getApplicationContext(), "영수증 추가 성공", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent();
                                            setResult(1, intent);
                                            finish();
                                        }

                                        @Override
                                        public void onFail() {

                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                                    ///////////////////////////////////////////////////////////////////
                                    ///////////////////////////////////////////////////////////////////

                                });
                            }
                        } else {
                            //영수증 추가 요청시 기존 cId가 없는 경우
                            ReceiptRequest test3 = new ReceiptRequest(resultDate, cost.getText().toString(), store.getText().toString(), userID, Integer.toString(spinner.getSelectedItemPosition() + 1), RequestInfo.RequestType.ADD_NEW_RECEIPT, getApplicationContext());
                            test3.AddNewReceipt(hId -> {
                                Toast.makeText(getApplicationContext(), "영수증 추가 성공", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                setResult(1, intent);
                                finish();
                            });
                        }


                    });
                }
            }
                });
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LinearLayout initialLayout = (LinearLayout)findViewById(R.id.layout_initial);
        LinearLayout resultLayout = (LinearLayout)findViewById(R.id.layout_result);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            initialLayout.setVisibility(View.GONE);
            resultLayout.setVisibility(View.VISIBLE);
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            initialLayout.setVisibility(View.GONE);
            resultLayout.setVisibility(View.VISIBLE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                callCloudVision(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("TEXT_DETECTION");
                labelDetection.setMaxResults(10);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<AddReceiptActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;
        private CustomProgressDialog dialog;
        private ProgressDialog progressDialog;
        private AddReceiptActivity activity;
        LableDetectionTask(AddReceiptActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            this.activity = activity;
            mRequest = annotate;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(activity,"Please Wait", "잠시만 기다려주세요");
//            dialog = new CustomProgressDialog(activity);
//            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            AddReceiptActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                String str[] = result.split("&");
                EditText store = (EditText) activity.findViewById(R.id.storeEdit);
                EditText date = (EditText) activity.findViewById(R.id.dateEdit);
                EditText time = (EditText) activity.findViewById(R.id.dateTimeEdit);
                EditText cost = (EditText) activity.findViewById(R.id.moneyEdit);
                Spinner spinner = (Spinner) activity.findViewById(R.id.categorySpinner);

                if (str.length > 0) {
                    //상정명 확인 요청
                    ReceiptRequest test = new ReceiptRequest(str[0], RequestInfo.RequestType.STORE_CHECK, activity.getApplicationContext());
                    test.StoreRequest(cId -> {
                        if (!cId.equals("null")) {

                            Log.i("cID", cId);

                            spinner.setSelection(Integer.parseInt(cId) - 1);
                        }

                        if (str.length > 0) {
                            store.setText(str[0]);
                        }
                        if (str.length > 1)
                            date.setText(str[1]);

                        if (str.length > 2)
                            time.setText(str[2]);
                        if (str.length > 3)
                            cost.setText(str[3]);
                    });
//                    dialog.dismiss();
                    progressDialog.dismiss();
                }
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading
      //  mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder();
        String date1 = "";
        String dateTime="";
        String time ="";
        String storeName ="";
        String totalCost ="";

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        if (labels != null) {
            String str = labels.get(0).getDescription();
            String data[] = str.split("\n");
            int dateFlag = 0;
            int moneyFlag = 0;
            int storeFlag = 0;
            int count = 0;
            HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

            for (String d : data) {
                d.replace(" ", "");
                Log.d("CHJ", "d : "+d);
                if (count == 0 && !d.contains("영수증"))
                    storeName = d.replaceAll("^\\s+", "");
                else if(storeFlag!= 1 && storeName.contains("[") && !d.contains("]")) {
                    storeName = d.replaceAll("^\\s+", "");
                    storeFlag = 1;
                }
                if ((d.contains("매장명") || d.contains("상호") || d.contains("가맹점")) && !d.contains("번호")) {
                    int index = d.indexOf(' ');
                    storeFlag = -1;
                    storeName = d.substring(index + 1).replaceAll("^\\s+", "");
                    if (storeName.contains("]")) {
                        index = storeName.indexOf(']');
                        storeName = storeName.substring(index + 1).replaceAll("^\\s+", "");
                    } else if (storeName.contains(":")) {
                        index = storeName.indexOf(":");
                        storeName = storeName.substring(index + 1).replaceAll("^\\s+", "");
                    }
                }

                if (storeFlag == 1) {
                    if (!d.contains("-"))
                        storeName = d.replaceAll("^\\s+", "");;
                    storeFlag = 0;
                }
                if (storeFlag != -1 && d.contains("영수증") &&!d.contains("주십시오") &&!d.contains("반드시") && !d.contains("소지") && !d.contains("현금") && !d.contains("카드") && !d.contains("결제") &&!d.contains("습니다")) {
                    storeFlag = 1;
                    String[] storeN = d.split(" ");
                    if(storeN.length>1) {
                        storeName = storeN[1].replaceAll("^\\s+", "");;
                        storeFlag = -1;
                    }
                }

                if (d.contains(",") || d.contains(".")) {
                    String confirm[] = d.split(" ");

                    int n = 0;
                    for (int h = 0; h < confirm.length; h++) {
                        String c = confirm[h];
                        String ff = c.replace(",", "");
                        ff = ff.replace(".", "");
                        try {
                            int num = Integer.parseInt(ff);
                            n++;
                            if (num < 1000) {
                                if (h + 1 < confirm.length)
                                    c = confirm[++h];
                                else break;
                                ff += c.replace(",", "");
                                num = Integer.parseInt(ff);
                                n++;
                            }

                            if(hashMap.containsKey(ff)) {
                                Integer value = hashMap.get(ff);
                                hashMap.put(ff, new Integer(value.intValue()+1));
                            } else {
                                hashMap.put(ff, new Integer(1));
                            }

                            if (n < 3 && moneyFlag < num) {
                                moneyFlag = num;
                                totalCost = ff;
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                if(d.contains("]") && d.length()>d.indexOf("]")+1) {
                    d = d.substring(d.indexOf("]")+1);
                }
                String date[] = d.split(" |-|:|/|\\.|년|월|일|\\(");

                int flag = 0;
                int none = 0;
                for (String a : date) {
                    Log.d("CHJ", "date : " + a+" / dateFlag : "+Integer.toString(dateFlag) );
                    try {
                        a.replaceAll(" ", "");
                        int num = Integer.parseInt(a);

                        if (!d.contains("사업자") && dateFlag == 0 && flag == 0) {
                            if (date.length == 5 && d.contains("/") && !d.startsWith("20")) {
                                String[] temp = d.split("/");
                                if (temp.length == 2) {
                                    a = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) + a;
                                    flag = 2;
                                    none = 1;
                                    dateFlag = 1;
                                }
                            } else if (a.length() == 2 || (a.length() == 4 && a.startsWith("20"))) {
                                if (a.length() == 2) {
                                    a = "20" + a;
                                    num = Integer.parseInt(a);
                                }
                                Log.i("CHJ", "YEAR : "+Integer.toString(num));
                                if (num <= Calendar.getInstance().get(Calendar.YEAR)) {
                                    flag = 1;
                                    dateFlag = 1;
                                }
                            }
                        }

                        if (flag > 0) {
                            if((none == 1 && a.length() == 6) || (flag >1 &&a.length() == 2) || (flag==1 && a.length()==4)) {
                                if (flag < 5) dateTime += a;

                                if (flag == 5) {
                                    dateTime += a;
                                    flag = -1;
                                }
                                flag++;
                            } else {
                                dateFlag = 0;
                                dateTime = "";
                            }
                            Log.d("CHJ", "dateTime : " + dateTime);
                        }


                    } catch (Exception e) {
                    }
                }
                if(dateTime.length()<12) {
                    dateTime = "";
                    dateFlag = 0;
                }

                count++;
            }
            Set<String> set = hashMap.keySet();
            int max = -1;
            String final_money = "";
            for(String tempKey: set) {
                if(hashMap.get(tempKey).intValue() >max) {
                    max = hashMap.get(tempKey).intValue();
                    final_money = tempKey;
                } else if(hashMap.get(tempKey).intValue() == max) {
                    int a = Integer.parseInt(final_money);
                    int b = Integer.parseInt(tempKey);
                    if(a<b)
                        final_money = tempKey;
                }
            }

            totalCost = final_money;
            Log.i("CHJ", "integer :"+ hashMap.values());

        } else {
            message.append("nothing");
        }

        if(dateTime.length()>=12) {
            Log.d("date", Integer.toString(dateTime.length()));
            time = dateTime.substring(8,12);
            date1 = dateTime.substring(0, 8);
        }
        message.append(storeName+ "&" + date1 +"&"+time+"&"+totalCost);
        return message.toString();
    }
}
