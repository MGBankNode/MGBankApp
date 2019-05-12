package com.example.myapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ArrayList<String> sData;
    private CreditCard selectedCard;
    private ArrayList<Stat> stats;

    RecyclerView recyclerView = null;
    DetailCardInfoAdapter adapter = null;
    LinearLayoutManager layoutManager = null;

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imageView;
    TextView textView;

    public DetailCardInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sData = (ArrayList<String>) getArguments().get("ACCOUNTS");
        Log.d("KJH", "ACCOUNTS SIZE : " + sData.size());
        selectedCard = (CreditCard) getArguments().get("CARD");
        stats = (ArrayList<Stat>) getArguments().get("STAT");
        return inflater.inflate(R.layout.fragment_detail_card_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setRecyclerView();
        collapsingToolbarLayout = (CollapsingToolbarLayout)getView().findViewById(R.id.collapsingToolbarLayout03);
        imageView = (ImageView) getView().findViewById(R.id.detailCardImage);
        textView = (TextView) getView().findViewById(R.id.detailDiscountTextView);
        collapsingToolbarLayout.setTitle(selectedCard.toString());
        imageView.setImageResource(selectedCard.getIcon());
        textView.setText((getArguments().getString("PRICE")));
        super.onActivityCreated(savedInstanceState);
    }

    //리사이클러뷰 셋팅
    public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.detailCardRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new DetailCardInfoAdapter(sData, selectedCard, stats);
        recyclerView.setAdapter(adapter);
    }
}
