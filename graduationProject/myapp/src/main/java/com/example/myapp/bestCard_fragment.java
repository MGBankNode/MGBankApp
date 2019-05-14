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
    //HashMap<CreditCard, ArrayList<String>> discountedAccountNames;
    Util util = new Util();


    CreditCard BestCard;
    int BestDiscountedPrice = 0;
    ArrayList<String> BestCardAccountsNames;
    HashMap<CreditCard, HashMap<Stat, Integer>> discountData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bestcard, container, false);
        CardDataBase cardDataBase = new CardDataBase();
        cData = new ArrayList<CreditCard>();
        discountedPrices = new HashMap<CreditCard, Integer>();
        //discountedAccountNames = new HashMap<CreditCard, ArrayList<String>>();
        for(int position = 0; position < cardDataBase.CardList.size(); position++){
            cData.add(cardDataBase.CardList.get(position));
        }
        Log.d("KJH", "cData Size() : "+ cData.size());


        sData = new ArrayList<Stat>();
        sData = (ArrayList<Stat>)getArguments().get("DATA");

        Log.d("KJH", "sData size : " + sData.size()) ;
        discountData = new HashMap<CreditCard, HashMap<Stat, Integer>>();

        ////////모든 카드에 대해 모든 내역에 대한 할인 금액을 구한다////////
        for(int cPosition = 0; cPosition < cData.size(); cPosition++){
            cData.get(cPosition).resetCondition();
            int temp = 0;
            discountData.put(cData.get(cPosition), new HashMap<Stat, Integer>());
            for(int sPosition = 0; sPosition < sData.size(); sPosition++){
                int discounted = sData.get(sPosition).getDiscountedPrice(cData.get(cPosition));
                temp += discounted;
                if(discounted > 0){
                    Log.d("KJH", "card :" + cData.get(cPosition) + ", temp : " + temp);
                    discountData.get(cData.get(cPosition)).put(sData.get(sPosition), discounted);
                }
            }
            if(cPosition == 0) {
                BestDiscountedPrice = temp;
                BestCard = cData.get(cPosition);
            }
            if(BestDiscountedPrice < temp){
                BestCard = cData.get(cPosition);
                BestDiscountedPrice = temp;
            }
            discountedPrices.put(cData.get(cPosition), temp);
        }
        discountedPrices.remove(BestCard);
        discountData.remove(BestCard);
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

        adapter = new CardRecyclerViewAdapter(discountedPrices, discountData);
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
