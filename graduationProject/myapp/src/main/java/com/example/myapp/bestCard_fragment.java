package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class bestCard_fragment extends Fragment {

    public bestCard_fragment() {}

    ArrayList<cardElement> arraylist = new ArrayList<>();
    ImageView bestCardImage;
    TextView bestCardName;
    TextView bestcardDiscountedPrice;
    RecyclerView recyclerView;
    CardRecyclerViewAdapter adapter = null;
    LinearLayoutManager layoutManager = null;
    ArrayList<CreditCard> cData;
    ArrayList<Stat> sData;
    HashMap<CreditCard, Integer> discountedPrices;
    HashMap<CreditCard, ArrayList<String>> discountedAccountNames;
    Util util = new Util();


    CreditCard BestCard;
    int BestDiscountedPrice = 0;
    ArrayList<String> BestCardAccountsNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bestcard, container, false);
        CardDataBase cardDataBase = new CardDataBase();
        cData = new ArrayList<CreditCard>();
        discountedPrices = new HashMap<CreditCard, Integer>();
        discountedAccountNames = new HashMap<CreditCard, ArrayList<String>>();
        for(int position = 0; position < cardDataBase.CardList.size(); position++){
            cData.add(cardDataBase.CardList.get(position));
        }
        Log.d("KJH", "cData Size() : "+ cData.size());


        sData = new ArrayList<Stat>();
        sData = (ArrayList<Stat>)getArguments().get("DATA");

        ////////모든 카드에 대해 모든 내역에 대한 할인 금액을 구한다////////
        for(int cPosition = 0; cPosition < cData.size(); cPosition++){
            int temp = 0;
            discountedAccountNames.put(cData.get(cPosition), new ArrayList<String>());
            for(int sPosition = 0; sPosition < sData.size(); sPosition++){
                Stat tempStat = sData.get(sPosition);
                ArrayList<String> tempString = new ArrayList<String>();
                tempStat.setClassificationData();
                temp += cData.get(cPosition).getDiscountedPrice(tempStat.getClassificationData(),
                         tempString);
                if(tempString.size()>0) {
                    discountedAccountNames.get(cData.get(cPosition)).addAll(tempString);
                    Log.d("TESTLOG", cData.get(cPosition) + " tempString size : " + tempString.size());
                }
            }
            if(cPosition == 0) BestDiscountedPrice = temp;
            if(BestDiscountedPrice < temp){
                BestCard = cData.get(cPosition);
                BestDiscountedPrice = temp;
                BestCardAccountsNames = discountedAccountNames.get(cData.get(cPosition));
            }
            discountedPrices.put(cData.get(cPosition), temp);
            //discountedAccountNames.put(cData.get(cPosition), tempString);
            //제일 할인율이 높은 카드는 리사이클러뷰에서 제외시킨다.
        }
        discountedPrices.remove(BestCard);
        discountedAccountNames.remove(BestCard);
        ArrayList<CreditCard> forList = new ArrayList<CreditCard>();
        forList.addAll(discountedAccountNames.keySet());
        for(int i = 0; i < forList.size(); i++){
            Log.d("DiscountedAccountNames", forList.get(i).toString() +
                    ", " + discountedAccountNames.get(forList.get(i)).size() +
                    ", " + discountedPrices.get(forList.get(i)));
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        bestCardImage = (ImageView)getView().findViewById(R.id.bestCardImage);
        bestCardName = (TextView)getView().findViewById(R.id.bestCardName);
        bestcardDiscountedPrice = (TextView)getView().findViewById(R.id.discountTextView);
        bestCardImage.setImageResource(BestCard.getIcon());
        bestCardName.setText(BestCard.toString());
        bestcardDiscountedPrice.setText(util.comma(BestDiscountedPrice));
        setRecyclerView();
        super.onActivityCreated(savedInstanceState);
    }
    public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.cardRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new CardRecyclerViewAdapter(discountedPrices, discountedAccountNames, sData);
        recyclerView.setAdapter(adapter);

    }
    public void alertDialog() {

        final String url = "https://mgcheck.kfcc.co.kr/pers/appl/persPeachGuid.do";

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("링크연결");
        builder.setMessage("더 자세한 혜택을 보시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getContext(), "아니오를 선택했습니다", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
