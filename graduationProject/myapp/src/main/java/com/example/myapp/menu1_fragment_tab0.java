package com.example.myapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class menu1_fragment_tab0 extends Fragment {

    String userID;
    protected ListView accountListView;

    public menu1_fragment_tab0() {    }

    @Nullable
    @Override //child fragment 구현
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.menu1_fragment_tab0,
                container, false);

        if(getArguments() != null){
            userID = getArguments().getString("ID");
            Log.i("nkw","menu1_tab0_userID="+userID);
        }

        ArrayList<AccountListData> listItem = new ArrayList<>();

        listItem.add(new AccountListData("NameA","123213124", "5000000"));
        listItem.add(new AccountListData("NameB","123213124", "5000000"));

        accountListView = layout.findViewById(R.id.accountList);
        accountListViewAdapter accountListViewAdapter = new accountListViewAdapter(getContext(), listItem, R.layout. accountlistitem);
        accountListView.setAdapter(accountListViewAdapter);


        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
