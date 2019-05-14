package com.example.myapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailCardInfo extends Fragment {

//    private ArrayList<String> sData;
    private CreditCard selectedCard;
    private HashMap<CreditCard, HashMap<Stat,Integer>> discountData;
//    private ArrayList<Stat> stats;

    RecyclerView recyclerView = null;
    DetailCardInfoAdapter adapter = null;
    LinearLayoutManager layoutManager = null;

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imageView;
    TextView textView;
    //nkw
    ImageButton imgbutton;
    ArrayList<Stat> sData;

    public DetailCardInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        sData = (ArrayList<String>) getArguments().get("ACCOUNTS");
//        Log.d("KJH", "ACCOUNTS SIZE : " + sData.size());

        selectedCard = (CreditCard) getArguments().get("CARD");
        discountData = (HashMap<CreditCard, HashMap<Stat,Integer>>) getArguments().get("DATA");

        //nkw
        sData= (ArrayList<Stat>)getArguments().get("sDATA");

//        stats = (ArrayList<Stat>) getArguments().get("STAT");
        return inflater.inflate(R.layout.fragment_detail_card_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setRecyclerView();
        collapsingToolbarLayout = (CollapsingToolbarLayout)getView().findViewById(R.id.collapsingToolbarLayout03);
        imageView = (ImageView) getView().findViewById(R.id.detailCardImage);
        textView = (TextView) getView().findViewById(R.id.detailDiscountTextView);
        imgbutton = (ImageButton) getView().findViewById(R.id.close_fr_btn) ;   //nkw
        collapsingToolbarLayout.setTitle("  "+selectedCard.toString());         //nkw
        imageView.setImageResource(selectedCard.getIcon());
        textView.setText((getArguments().getString("PRICE")));


        //뒤로가기 버튼 누르면 프래그먼트 안보이게 하기 nkw
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new bestCard_fragment();
                Bundle bundle = new Bundle(1);
                bundle.putSerializable("DATA", sData);
                fragment.setArguments(bundle);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.dynamic_mainFragment, fragment);
                fragmentTransaction.commit();
            }
        });
        super.onActivityCreated(savedInstanceState);

    }

    //리사이클러뷰 셋팅
    public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.detailCardRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new DetailCardInfoAdapter(discountData, selectedCard);
        recyclerView.setAdapter(adapter);
    }
}
