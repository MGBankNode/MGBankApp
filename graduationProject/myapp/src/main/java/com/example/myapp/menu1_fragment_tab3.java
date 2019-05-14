package com.example.myapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class menu1_fragment_tab3 extends Fragment {

    protected ListView accountListView;

    public menu1_fragment_tab3() {
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu1_fragment_tab3, container, false);

        ArrayList<AccountListData> listItem = new ArrayList<>();

        listItem.add(new AccountListData("NameA","123213124", "5000000"));
        listItem.add(new AccountListData("NameB","123213124", "5000000"));

        accountListView = view.findViewById(R.id.accountList);

        accountListViewAdapter accountListViewAdapter = new accountListViewAdapter(getContext(), listItem, R.layout. accountlistitem);
        accountListView.setAdapter(accountListViewAdapter);

        return view;
    }
}
