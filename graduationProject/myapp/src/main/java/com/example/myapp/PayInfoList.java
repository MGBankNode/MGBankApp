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
public class PayInfoList extends Fragment {

    Stat stat;
    RecyclerView recyclerView = null;
    DetailedRecyclerViewAdapter adapter = null;
    LinearLayoutManager layoutManager = null;

    public PayInfoList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_pay_info_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //이전 프레그먼트로부터 객체를 받아온다
        stat = (Stat)getArguments().get("STAT");
        stat.setClassificationData();
        TextView tv = (TextView) getView().findViewById(R.id.testTextView);
        tv.setText(stat.getName());
        setRecyclerView();
        super.onActivityCreated(savedInstanceState);
    }

    public void setRecyclerView(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerView02);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new DetailedRecyclerViewAdapter(stat.getClassificationData(), stat);
        recyclerView.setAdapter(adapter);
    }

}
