package com.example.testexpandablelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

        private ExpandableListView listView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Display newDisplay = getWindowManager().getDefaultDisplay();
            int width = newDisplay.getWidth();

            ArrayList<myGroup> DataList = new ArrayList<myGroup>();
            listView = (ExpandableListView)findViewById(R.id.mylist);
            myGroup temp = new myGroup("한글");
            temp.child.add("ㄱ");
            temp.child.add("ㄴ");
            temp.child.add("ㄷ");
            DataList.add(temp);
            temp = new myGroup("영어");
            temp.child.add("a");
            temp.child.add("b");
            temp.child.add("c");
            DataList.add(temp);
            temp = new myGroup("숫자");
            temp.child.add("1");
            temp.child.add("2");
            temp.child.add("3");
            DataList.add(temp);

            ExpandAdapter adapter = new ExpandAdapter(getApplicationContext(),R.layout.group_row,R.layout.child_row,DataList);
            listView.setIndicatorBounds(width-50, width); //이 코드를 지우면 화살표 위치가 바뀐다.
            listView.setAdapter(adapter);

            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Toast.makeText(getApplicationContext(), String.valueOf(groupPosition), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Toast.makeText(getApplicationContext(), String.valueOf(groupPosition) + " " + childPosition, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

}
