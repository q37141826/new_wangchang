package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OkHttpUIUpdataAdapterListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by my on 2018/9/10.
 */

public class PopInjury {
    OnItemClickListenner listenner;
    PopupWindow popupWindow;
    private View contentView;
    private TextView textPayPopHead, textViewServicePrice, textViewModifyTimes, textViewGotoMyMember, textViewGotoPay;
    ViewGroup layoutFather;
    private RecyclerView recyclerViewAddress;
    Context context;
    private ZProgressHUD zProgressHUD;


    public void requstPop() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        } else {
            zProgressHUD = new ZProgressHUD(context);
            zProgressHUD.show();
            Map<String, String> map = new HashMap<>();
            OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(new OkHttpUIUpdataAdapterListener() {
                @Override
                public void onSuccess(Object data, int i) {
                    if (data instanceof InjuryBean) {
                        InjuryBean beans = (InjuryBean) data;
                        setPopupWindow(context, beans.getO(), listenner);
                    }
                    zProgressHUD.dismiss();
                }

                @Override
                public void onError(Call call, Exception e, int i) {
                    zProgressHUD.dismiss();
                }

                @Override
                public void onFailure(C_CodeBean c_codeBean) {
                    zProgressHUD.dismiss();
                }
            });
            okHttpRequestModel.okHttpGet(ConstantUrl.getInjuryUrl, map, new InjuryBean());
        }
    }

    public PopInjury(Context context, OnItemClickListenner listenner) {
        this.listenner = listenner;
        this.context = context;
    }

    private void setPopupWindow(Context context, List<InjuryBean.OBean> datas, OnItemClickListenner listenner) {
        contentView = View.inflate(context, R.layout.pop_address, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setClippingEnabled(false);
        recyclerViewAddress = contentView.findViewById(R.id.recyclerViewRegiste);
        TextView textViewPopTitle = contentView.findViewById(R.id.textViewPopTitle);
        textViewPopTitle.setText("伤残等级");
        AddressAdapter addressAdapter = new AddressAdapter();
        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewAddress.setAdapter(addressAdapter);
        addressAdapter.refreshData(datas);
        addressAdapter.setOnItemClickListener(new OnRecyclerItemListener() {
            @Override
            public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
                if (listenner != null) {
                    listenner.onItemClick((InjuryBean.OBean) data);
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
        void onItemClick(InjuryBean.OBean data);
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

        public class AddressHolder extends RecyclerBaseHolder<InjuryBean.OBean> {
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

    public class InjuryBean extends BaseBean<List<InjuryBean.OBean>> {


        public class OBean {
            private String id;
            private String type;
            private String name;
            private String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }

}

