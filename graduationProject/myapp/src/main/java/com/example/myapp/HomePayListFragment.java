package com.example.myapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePayListFragment extends Fragment {
    //리스트뷰에 사용할 데이터
    ArrayList<Stat> sData = null;
    //리스트뷰와 어댑터
    ListView homeListView = null;
    FragmentHomeListViewAdapter adapter = null;


    public HomePayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //리스트뷰 어댑터에 사용할 데이터
        sData = new ArrayList<Stat>();
        sData.add(new Stat(223000));
        sData.add(new Stat(142800));
        sData.add(new Stat(15420));
        sData.add(new Stat(214000));
        sData.add(new Stat(105000));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pay_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //리스트뷰 어댑터 설정
        homeListView = (ListView)getView().findViewById(R.id.mainFragment_list_view);
        adapter = new FragmentHomeListViewAdapter(getActivity(), sData);
        homeListView.setAdapter(adapter);

        super.onActivityCreated(savedInstanceState);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////리스트 어댑터//////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    public class FragmentHomeListViewAdapter extends BaseAdapter {
        Context context = null;
        ArrayList<Stat> data = null;
        LayoutInflater layoutInflater = null;
        int labelCount = 0;
        int allPrice = 0;

        public FragmentHomeListViewAdapter(Context c, ArrayList<Stat> d)
        {
            this.context = c;
            this.data = d;
            layoutInflater = LayoutInflater.from(this.context);
        }
        //모든 데이터의 총금액을 구함
        public void getAllPrice(){
            for(int i = 0; i < data.size(); i++){
                allPrice += data.get(i).getPrice();
            }
        }
        public int getCount(){return data.size();}
        public long getItemId(int p){return p;}
        public Stat getItem(int p){return data.get(p);}
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemLayout = convertView;
            ViewHolder viewHolder = null;

            if(itemLayout == null)
            {
                itemLayout = layoutInflater.inflate(R.layout.item_list_view_fragment_home
                        , null);
                //Item 높이 설정
                /*ViewGroup.LayoutParams layoutParams = itemLayout.getLayoutParams();
                layoutParams.height = 60;
                itemLayout.setLayoutParams(layoutParams);*/

                viewHolder = new ViewHolder();
                viewHolder.labelTv = (TextView) itemLayout.findViewById(R.id.list_number);
                viewHolder.nameTv = (TextView)itemLayout.findViewById(R.id.stat_textView);
                viewHolder.percentBtn = (Button)itemLayout.findViewById(R.id.percent_button);
                viewHolder.percentReverseTv = (TextView)itemLayout.findViewById(R.id.percent_reverse);
                viewHolder.moneyTv = (TextView)itemLayout.findViewById(R.id.money_textView);
                itemLayout.setTag(viewHolder);
            }else
            {
                viewHolder = (ViewHolder)itemLayout.getTag();
            }
            viewHolder.labelTv.setText(Integer.toString(position + 1));
            //임시로 퍼센트를 지정
            return itemLayout;
        }
        public class ViewHolder{
            TextView labelTv;
            TextView nameTv;
            Button percentBtn;
            TextView percentReverseTv;
            TextView moneyTv;
        }
    }
}
