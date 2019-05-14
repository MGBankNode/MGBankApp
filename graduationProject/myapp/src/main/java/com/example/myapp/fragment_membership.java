package com.example.myapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class fragment_membership extends Fragment {

    public fragment_membership() {    }

    private  static final  int WHITE = 0xffffffff;
    private  static final  int BLACK = 0xff000000;
    ImageView barcoode_img;
    TextView barcode_txt;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<DataModel> data;

    Fragment fragment;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_membership, container, false);
        if(getArguments() != null){
            userID = getArguments().getString("ID");
            Log.i("nkw","menu3_membership_userID="+userID);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        barcoode_img = (ImageView)getView().findViewById(R.id.bcd_img);
        barcode_txt=(TextView)getView().findViewById(R.id.bcd_txt);

        BarcodePointRequestHandler(new VolleyCallback() {
            @Override
            public void onSuccess(String barcode_data, String point) {
                //사용자의 바코드 번호 16자리 -----회원가입시 바코드 번호 생성하여 디비에 등록된것 불러오기
                //String barcode_data = "9999999999999999";

                //Toast.makeText(getContext(), barcode_data + point, Toast.LENGTH_LONG).show();
                //바코드 번호 4자리씩 끊기

                String barcode_num=(barcode_data.substring(0, 4) +"  "+ barcode_data.substring(4, 8)
                        + "  " + barcode_data.substring(8, 12) +"  "+ barcode_data.substring(12, 16));

                //바코드 출력
                try{
                    Bitmap bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128,800,200);
                    barcoode_img.setImageBitmap(bitmap);
                    barcode_txt.setText(barcode_num);
                }catch (WriterException e){
                    e.printStackTrace();
                }

                //리사이클러뷰 (카드뷰)
                recyclerView = (RecyclerView)getView().findViewById(R.id.point_list);
                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                membership_Data.setScoreArray(point + "p");

                data = new ArrayList<DataModel>();
                for (int i = 0; i < membership_Data.nameArray.length; i++) {
                    data.add(new DataModel(
                            membership_Data.nameArray[i],
                            membership_Data.scoreArray[i],
                            membership_Data.id_[i],
                            membership_Data.drawableArray[i],
                            membership_Data.backgroundArray[i]
                    ));
                }


                adapter = new CustomAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });

        super.onActivityCreated(savedInstanceState);

    }

    //////////////////////////////////////////////바코드 이미지 생성////////////////////////////////////
    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();

        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
    ////////////////////////////////////바코드 끝/////////////////////////////////////////////

    ///////////////////////////리사이클러뷰 어댑터///////////////////////////////////

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private ArrayList<DataModel> dataSet;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textViewName;
            TextView textViewScore;
            ImageView imageViewIcon;
            CardView cardView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
                this.textViewScore = (TextView) itemView.findViewById(R.id.textViewScore);
                this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
                this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            }
        }

        public CustomAdapter(ArrayList<DataModel> data) {
            this.dataSet = data;
        }

        @Override

        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.menu3_list_item, parent, false);

            //////////////////////리사이클러뷰 카드 아이템 클릭시////////////////////////////
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectCardPosition = recyclerView.getChildAdapterPosition(v);
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectCardPosition);
                    TextView textView = (TextView)viewHolder.itemView.findViewById(R.id.textViewName);
                    String selectedName = (String) textView.getText();
                    Log.i("NKW","selectedName = "+selectedName);
                    int selectedID = -1;
                    for(int i=0; i<membership_Data.nameArray.length;i++){
                        if(selectedName.equals(membership_Data.nameArray[i])){
                            selectedID=membership_Data.id_[i];
                        }
                    }
                    fragment = new fragment_pointcard();
                    Bundle bundle = new Bundle();
                    bundle.putInt("point_id",selectedID);
                    bundle.putString("ID",userID);
                    fragment.setArguments(bundle);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_pointCard, fragment);
                    fragmentTransaction.commit();
                }
            });


            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override

        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            TextView textViewName = holder.textViewName;
            TextView textViewScore = holder.textViewScore;
            ImageView imageView = holder.imageViewIcon;
            CardView cardView = holder.cardView;

            textViewName.setText(dataSet.get(listPosition).getName());
            textViewScore.setText(dataSet.get(listPosition).getScore());
            imageView.setImageResource(dataSet.get(listPosition).getImage());
            cardView.setCardBackgroundColor(dataSet.get(listPosition).getBackground());
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }


    /////////////////////////////////////////데이터모델 클래스 /////////////////////////////
    public class DataModel {

        String name;
        String score;
        int id_;
        int image;
        int background;

        public DataModel(String name, String score, int id_, int image, int background) {
            this.name = name;
            this.score = score;
            this.id_ = id_;
            this.image=image;
            this.background=background;
        }

        public String getName() {
            return name;
        }

        public String getScore() {
            return score;
        }

        public int getImage() {
            return image;
        }

        public int getId() {
            return id_;
        }

        public int getBackground() {return background; }
    }


    public interface VolleyCallback{
        void onSuccess(String barcode, String point);
    }

    //바코드 + 적립 포인트 얻어오기 요청
    public void BarcodePointRequestHandler(final VolleyCallback callback){
        RequestInfo requestInfo = new RequestInfo(RequestInfo.RequestType.BARCODE_POINT);

        String url = "http://" + requestInfo.GetRequestIP() + ":" + requestInfo.GetRequestPORT() + requestInfo.GetProcessURL();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        BarcodePointResponse(response, callback);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams(){
                return BarcodePointRequest();
            }
        };
        request.setShouldCache(false);
        Volley.newRequestQueue(getContext()).add(request);
        Log.d("요청 url: ", url);

    }

/*
        BarcodePointRequest(): Map<String, String>
        = 바코드 + 포인트 적립 요청 전달 파라미터 설정 함수
    */

    private Map<String, String> BarcodePointRequest(){

        Map<String, String> params = new HashMap<>();

        params.put("id", userID);
        return params;

    }

    /*
        BarcodePointResponse(String, final VolleyCallback): void
        = 일별 분석 요청 응답 처리 함수
    */

    private void BarcodePointResponse(String response, final VolleyCallback callback){
        try{
            Log.d("onResponse 호출 ", response);

            JSONObject json = new JSONObject(response);
            String resultString = (String) json.get("message");

            switch (resultString) {
                case "success":
                    JSONObject data = json.getJSONObject("data");

                    String barcode = (String) data.get("barcode");
                    String point = (String) data.get("point");

                    callback.onSuccess(barcode, point);
                    break;

                case "error":
                    break;

                case "db_fail":
                    break;

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
