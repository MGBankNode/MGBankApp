package com.example.myapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class menu1_RecyclerAdapter extends RecyclerView.Adapter<menu1_RecyclerAdapter.ViewHolder> {
    private List<menu1_rvData> menu1rvDataList;
    private RecyclerViewClickListener mListener;

    private int itemLayout;
    int date = 0;

    public interface RecyclerViewClickListener{
        void onItemClicked(int position);
    }

    public void setOnClickListener(RecyclerViewClickListener listener){
        mListener = listener;
    }

    public menu1_RecyclerAdapter(List<menu1_rvData> items , int itemLayout){

        this.menu1rvDataList = items;
        this.itemLayout = itemLayout;
    }

     /** 레이아웃을 만들어서 Holder에 저장 */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout,viewGroup,false);
        return new ViewHolder(view);
    }

     /** listView getView 를 대체
     * 넘겨 받은 데이터를 화면에 출력하는 역할*/

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");

        //RecyclerAdapter에 설정된 리스트로부터 rvData를 구함
        menu1_rvData item = menu1rvDataList.get(position);

        if (item.getDate() != date) {
            date = item.getDate();
            viewHolder.day_price.setVisibility(View.VISIBLE);
            viewHolder.date.setText(Integer.toString(item.getDate()));
        } else { //동일 날짜인경우
            // 인덱스용의 데이터에 없는 경우는 텍스트만 표시
            viewHolder.day_price.setVisibility(View.GONE);
        }

        viewHolder.hour.setText(item.getHour());
        viewHolder.minute.setText(item.getMinute());
        if (!item.getType().equals("입금")) { //지출일때
            viewHolder.name.setText(item.getName());    //입금처
            viewHolder.card.setText(item.getCard());    //출금처
            viewHolder.ingredient.setText(item.getCategori());
            viewHolder.ingredient.setBackgroundResource(R.drawable.loss_label);
            viewHolder.ingredient.setTextColor(Color.RED);
            viewHolder.price.setTextColor(Color.RED);
        } else {    //입금일때
            viewHolder.name.setText(item.getName());    //입금처
            viewHolder.card.setText("내 계좌 "+item.getAnum());    //출금처
            viewHolder.ingredient.setText(item.getCategori());
            viewHolder.ingredient.setBackgroundResource(R.drawable.benefit_label);
            viewHolder.ingredient.setTextColor(Color.rgb(60, 193, 238));
            viewHolder.price.setTextColor(Color.rgb(60, 193, 238));
        }
        viewHolder.price.setText(myFormatter.format(item.getPrice()) + "원");            //금액
        viewHolder.balance_price.setText(myFormatter.format(item.getBalance()) + "원");  //잔액

        //클릭 이벤트
        if(mListener != null){
            final int pos = position;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return menu1rvDataList.size();
    }

    /** 뷰 재활용을 위한 viewHolder     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView date, hour, minute, name, card, price, balance_price, ingredient;
        LinearLayout day_price;

        public ViewHolder(View itemView){
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.list_date);
            hour = (TextView) itemView.findViewById(R.id.hh);
            minute = (TextView) itemView.findViewById(R.id.mm);
            name = (TextView) itemView.findViewById(R.id.name);
            card = (TextView) itemView.findViewById(R.id.user_card);
            price = (TextView) itemView.findViewById(R.id.price);
            balance_price = (TextView) itemView.findViewById(R.id.balance_price);

            day_price = (LinearLayout) itemView.findViewById(R.id.day_price);
            ingredient = (TextView) itemView.findViewById(R.id.ingredient);

        }
    }
}




