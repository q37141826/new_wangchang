package com.qixiu.newoulingzhu.mvp.view.activity.home.customization;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class CustomizationListActivity extends RequstActivity implements OnRecyclerItemListener {

    @BindView(R.id.recyclerViewCustom)
    RecyclerView recyclerViewCustom;
    @BindView(R.id.swipRefresh)
    SwipeRefreshLayout swipRefresh;
    int images[] = {R.mipmap.custom_contract2x, R.mipmap.custom_review2x, R.mipmap.custom_lawyer_letter2x,
            R.mipmap.custom_notification2x, R.mipmap.custom_documents2x, R.mipmap.custom_meeting2x,};
    String titles[] = {"合同订制", "合同审查","律师函",
            "告知函","诉讼文书", "会面咨询"};
    private CustomAdapter adapter;
    Map<String, Integer> imageMap = new HashMap<>();

    @Override
    protected void onInitData() {
        mTitleView.setTitle("个人定制");
        adapter = new CustomAdapter();
        recyclerViewCustom.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewCustom.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        reqestData();
        swipRefresh.setColorSchemeResources(R.color.theme_color);

    }

    private void reqestData() {
        Map<String, String> map = new HashMap<>();
        get(ConstantUrl.customListUrl, map, new CustomItemBean());

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_customization_list;
    }

    @Override
    protected void onInitViewNew() {
        for (int i = 0; i < images.length; i++) {
            imageMap.put(titles[i], images[i]);
        }
        swipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqestData();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof CustomItemBean) {
            CustomItemBean bean = (CustomItemBean) data;
            adapter.refreshData(bean.getO());
        }
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
        swipRefresh.setRefreshing(false);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
        swipRefresh.setRefreshing(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof CustomItemBean.OBean) {
            CustomItemBean.OBean bean = (CustomItemBean.OBean) data;
            if (bean.getTitle().equals(getString(R.string.custom_meeting))) {
                CustomMeetingActivity.start(getContext(), bean);
            } else {
                CustomCommtActivity.start(getContext(), bean);
            }
        }
    }


    public class CustomAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_custom;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {

            return new CustomHoledr(itemView, context, this);
        }

        public class CustomHoledr extends RecyclerBaseHolder {
            ImageView imageViewCustItem;
            TextView textViewCustItem;

            public CustomHoledr(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                imageViewCustItem = itemView.findViewById(R.id.imageViewCustItem);
                textViewCustItem = itemView.findViewById(R.id.textViewCustItem);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof CustomItemBean.OBean) {
                    CustomItemBean.OBean bean = (CustomItemBean.OBean) mData;
                    Glide.clear(imageViewCustItem);
                    Glide.with(mContext).load(bean.getLogoimg()).asBitmap().into(imageViewCustItem);
                    textViewCustItem.setText(bean.getTitle());
                }

            }
        }
    }

    public static class CustomItemBean extends BaseBean<List<CustomItemBean.OBean>> {


        public static class OBean implements Parcelable {
            private String id;
            private String title;
            private String logoimg;
            private String tips;
            private String type;
            private String mark;
            private String price;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLogoimg() {
                return logoimg;
            }

            public void setLogoimg(String logoimg) {
                this.logoimg = logoimg;
            }

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.title);
                dest.writeString(this.logoimg);
                dest.writeString(this.tips);
                dest.writeString(this.type);
                dest.writeString(this.mark);
                dest.writeString(this.price);
            }

            public OBean() {
            }

            protected OBean(Parcel in) {
                this.id = in.readString();
                this.title = in.readString();
                this.logoimg = in.readString();
                this.tips = in.readString();
                this.type = in.readString();
                this.mark = in.readString();
                this.price = in.readString();
            }

            public static final Parcelable.Creator<OBean> CREATOR = new Parcelable.Creator<OBean>() {
                @Override
                public OBean createFromParcel(Parcel source) {
                    return new OBean(source);
                }

                @Override
                public OBean[] newArray(int size) {
                    return new OBean[size];
                }
            };
        }
    }

}
