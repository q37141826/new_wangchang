package com.qixiu.newoulingzhu.mvp.view.activity.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2018/9/10.
 */

public class PopIsAsset {
    OnItemClickListenner listenner;
    PopupWindow popupWindow;
    private View contentView;
    private TextView textPayPopHead, textViewServicePrice, textViewModifyTimes, textViewGotoMyMember, textViewGotoPay;
    ViewGroup layoutFather;
    private  RecyclerView recyclerViewRegiste;
    Context context;
    private TextView textViewTitile;


    public void requstPop() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        } else {
            List<CaseType>  datas=new ArrayList<>();
            CaseType bean=new CaseType("是",true);
            datas.add(bean);
            bean=new CaseType("否",false);
            datas.add(bean);
            setPopupWindow(context,datas,listenner);
        }
    }

    public PopIsAsset(Context context, OnItemClickListenner listenner) {
        this.listenner = listenner;
        this.context = context;
    }

    public void setTitle(String title){
        if(textViewTitile!=null){
            textViewTitile.setText(title);
        }
    }

    private void setPopupWindow(Context context, List<CaseType> datas, OnItemClickListenner listenner) {
        contentView = View.inflate(context, R.layout.pop_registe, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setClippingEnabled(false);
        recyclerViewRegiste = contentView.findViewById(R.id.recyclerViewRegiste);
        textViewTitile = contentView.findViewById(R.id.textViewTitile);
        textViewTitile.setText("涉及财产");
        AddressAdapter addressAdapter = new AddressAdapter();
        recyclerViewRegiste.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewRegiste.setAdapter(addressAdapter);
        addressAdapter.refreshData(datas);
        addressAdapter.setOnItemClickListener(new OnRecyclerItemListener() {
            @Override
            public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
                if (listenner != null) {
                    listenner.onItemClick((CaseType) data);
                }
                popupWindow.dismiss();
            }
        });

    }

    public void show() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        }
    }

    public interface OnItemClickListenner {
        void onItemClick(CaseType data);
    }


    public class AddressAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_address_pop;
        }

        @Override
        public AddressHolder createViewHolder(View itemView, Context context, int viewType) {
            return new AddressHolder(itemView, context, this);
        }

        public class AddressHolder extends RecyclerBaseHolder<CaseType>{
            TextView textViewAddressItemPop;

            public AddressHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewAddressItemPop = itemView.findViewById(R.id.textViewAddressItemPop);
            }

            @Override
            public void bindHolder(int position) {
                textViewAddressItemPop.setText(mData.getName());
            }
        }
    }
    public class CaseType {
        private String  type;
        private String name;
        private boolean asset;

        public CaseType(String name, boolean asset) {
            this.name = name;
            this.asset = asset;
        }

        public boolean isAsset() {
            return asset;
        }

        public void setAsset(boolean asset) {
            this.asset = asset;
        }

        public CaseType(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

