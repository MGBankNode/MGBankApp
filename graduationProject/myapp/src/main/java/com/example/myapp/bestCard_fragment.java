package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout bestCardLayout;
    //HashMap<CreditCard, ArrayList<String>> discountedAccountNames;
    Util util = new Util();


    CreditCard BestCard;
    int BestDiscountedPrice = 0;
    HashMap<CreditCard, HashMap<Stat, Integer>> bestDiscountData;
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
        bestDiscountData = new HashMap<CreditCard, HashMap<Stat, Integer>>();
        bestDiscountData.putAll(discountData);
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

        bestCardLayout = (LinearLayout)getView().findViewById(R.id.bestCardLayout);
        bestCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KJH", BestCard.toString() + " click Event");

                DetailCardInfo fr = new DetailCardInfo();
                Bundle args = new Bundle();
                args.putSerializable("CARD", BestCard);
                args.putSerializable("DATA", bestDiscountData);
                //nkw
                args.putSerializable("sDATA", sData);

                args.putString("PRICE", util.comma(BestDiscountedPrice));

                fr.setArguments(args);
                FragmentActivity f = (FragmentActivity)v.getContext();
                FragmentManager fm = f.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.dynamic_mainFragment, fr);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }
    public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.cardRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        //nkw
        adapter = new CardRecyclerViewAdapter(discountedPrices, discountData, sData);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent();
        intent.setAction("HomeFragment");
        getActivity().sendBroadcast(intent);
        super.onDestroy();
    }
}
