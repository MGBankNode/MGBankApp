package com.example.myapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPayInfo extends Fragment {
    private RecyclerView recyclerView;
    private DetailPayInfoAdapter adapter = null;
    private LinearLayoutManager layoutManager = null;

    private Stat s;
    private CreditCard c;
    private TextView tv;

    public DetailPayInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        s = (Stat)getArguments().get("STAT");
        c = (CreditCard)getArguments().get("CARD");
        return inflater.inflate(R.layout.fragment_detail_pay_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setRecyclerView();
        tv = (TextView)getView().findViewById(R.id.detail_card_stat_name);
        tv.setText(s.toString());

        super.onActivityCreated(savedInstanceState);
    }

    //리사이클러뷰 셋팅
    public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.detail_card_account_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new DetailPayInfoAdapter(s, c);
        recyclerView.setAdapter(adapter);
    }
}
