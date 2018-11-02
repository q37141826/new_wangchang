package com.qixiu.newoulingzhu.mvp.view.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.EaseConstant;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jude.rollviewpager.RollPagerView;
import com.qixiu.newoulingzhu.beans.home.HomeBean;
import com.qixiu.newoulingzhu.beans.home.HomeServiceBean;
import com.qixiu.newoulingzhu.beans.home.HotInformationBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.ConsultActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.CaculatorActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.cases.ClasicCaseActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.customization.CustomizationListActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.template.NewTemplateActivity;
import com.qixiu.newoulingzhu.mvp.view.adapter.homne.ImageUrlAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.wight.loading.RefreshHeader;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.newoulingzhu.utils.XrecyclerViewUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Unbinder;
import okhttp3.Call;

import static com.qixiu.newoulingzhu.constant.ConstantUrl.HotInformationListURl;
import static com.qixiu.newoulingzhu.mvp.view.activity.home.cases.ClasicCaseActivity.TITLE_NAME_CASE;
import static com.qixiu.newoulingzhu.mvp.view.activity.home.cases.ClasicCaseActivity.TITLE_NAME_CONSULTING;

/**
 * Created by my on 2018/8/9.
 */

public class HomeFragment extends BaseFragment implements XRecyclerView.LoadingListener, OKHttpUIUpdataListener, View.OnClickListener, OnRecyclerItemListener {
    OKHttpRequestModel mOkHttpRequestModel;

    XRecyclerView recyclerHome;
//    VerticalSwipeRefreshLayout swipRefreshHome;

    Unbinder unbinder;

    private RollPagerView bannerView;

    private List<HomeBean.HomeInfo.BannerBean> mBanner;
    private List<HomeBean.HomeInfo.PostBean> mPostBeanList;
    private ImageUrlAdapter mImageUrlAdapter;
    private LinearLayout rl_consult;
    private LinearLayout rl_writ_template;
    private TextView rl_bottom_one;
    private TextView rl_bottom_two;
    private TextView rl_bottom_three;
    private TextView rl_bottom_four;

    private RecyclerBaseAdapter.ClickListenner listenner;
    private LinearLayout linearlayoutGotoData;
    private ImageView imageViewRollMask;//轮播图数量只有一张的时候，不滑动

    public void setListenner(RecyclerBaseAdapter.ClickListenner listenner) {
        this.listenner = listenner;
    }
    ZProgressHUD zProgressHUD;
    HotInformationAdapter adapter = new HotInformationAdapter();

    @Override
    protected void onInitData() {
        getHomeInfo();
        getHotInformationList();
    }


    public void getHomeInfo() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.HomeInfoURl, stringMap, new HomeBean());

    }

    @Override
    protected void onInitView(View view) {
        zProgressHUD=new ZProgressHUD(getContext());
        recyclerHome = view.findViewById(R.id.recycler_home);
        imageViewRollMask = view.findViewById(R.id.imageViewRollMask);
//        swipRefreshHome = view.findViewById(R.id.swipRefreshHome);
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.home_head, null);
        XrecyclerViewUtil.setXrecyclerView(recyclerHome, this, getContext(), true, 1, null);
        recyclerHome.addHeaderView(headView);
        RefreshHeader header = new RefreshHeader(getContext());
        header.setArrowImageView(R.mipmap.refresh_head_arrow);
        recyclerHome.setRefreshHeader(header);
//        swipRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getHomeInfo();
//                getHotInformationList();
//                if (listenner != null) {
//                    listenner.click(null, null);
//                }
//            }
//        });
        adapter = new HotInformationAdapter();
        adapter.setOnItemClickListener(this);
        recyclerHome.setAdapter(adapter);
        initHeadView(headView);
        bannerView = view.findViewById(R.id.bannerView);
        mImageUrlAdapter = new ImageUrlAdapter(bannerView);
        bannerView.setAdapter(mImageUrlAdapter);
        setClick();
//        swipRefreshHome.setColorSchemeResources(R.color.theme_color);
        recyclerHome.setLoadingMoreEnabled(false);
    }

    private void setClick() {
        rl_consult.setOnClickListener(this);
        rl_writ_template.setOnClickListener(this);
        rl_bottom_one.setOnClickListener(this);
        rl_bottom_two.setOnClickListener(this);
        rl_bottom_three.setOnClickListener(this);
        rl_bottom_four.setOnClickListener(this);
        linearlayoutGotoData.setOnClickListener(this);
    }

    private void initHeadView(View view) {
        rl_consult = view.findViewById(R.id.rl_consult);
        rl_writ_template = view.findViewById(R.id.rl_writ_template);
        rl_bottom_one = view.findViewById(R.id.rl_bottom_one);
        rl_bottom_two = view.findViewById(R.id.rl_bottom_two);
        rl_bottom_three = view.findViewById(R.id.rl_bottom_three);
        rl_bottom_four = view.findViewById(R.id.rl_bottom_four);
        linearlayoutGotoData = view.findViewById(R.id.linearlayoutGotoData);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }


    @Override
    public void onRefresh() {
        getHomeInfo();
        getHotInformationList();
        if (listenner != null) {
            listenner.click(null, null);
        }
    }

    @Override
    public void onLoadMore() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }


    @Override
    public void onSuccess(Object data, int i) {
//        swipRefreshHome.setRefreshing(false);
        recyclerHome.refreshComplete();
        recyclerHome.loadMoreComplete();
        if (data instanceof HomeBean) {
            zProgressHUD.dismiss();
            HomeBean homeBean = (HomeBean) data;
            HomeBean.HomeInfo homeInfo = homeBean.getO();
            mBanner = homeInfo.getBanner();
            mPostBeanList = homeInfo.getPost();
            disposeAdvInfo();
            if(mBanner.size()>1){
                imageViewRollMask.setVisibility(View.GONE);
                bannerView.setVisibility(View.VISIBLE);
            }else {
                imageViewRollMask.setVisibility(View.VISIBLE);
                bannerView.setVisibility(View.GONE);
            }
            if(mBanner.size()==1){
                Glide.with(getContext()).load(mBanner.get(0).getImg()).error(R.mipmap.banner2x).into(imageViewRollMask);
            }
//            disposePostInfo();
        }
        if (data instanceof HotInformationBean) {
            HotInformationBean bean = (HotInformationBean) data;
            adapter.refreshData(bean.getO().getList());
        }
        if (data instanceof HomeServiceBean) {
            zProgressHUD.dismiss();
            HomeServiceBean homeServicebean = (HomeServiceBean) data;
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.PROBLEM, "");
            bundle.putString(EaseConstant.TYPE, "1");
            bundle.putString(EaseConstant.CURRENT_UID, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
            bundle.putString(EaseConstant.TOCHAT_NAME, homeServicebean.getO().getUser_nicename());
            bundle.putString(EaseConstant.PRO_ID, "0");
            String serviceId = "ls" + homeServicebean.getO().getId();
            HyEngine.startConversationSingle(getActivity(), serviceId, bundle, ChatActivity.class);
//            HyEngine.startConversationSingle(getActivity(), "553", bundle, ChatActivity.class);
        }
    }

    private void disposeAdvInfo() {
        if (mBanner != null) {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < mBanner.size(); i++) {
                strings.add(mBanner.get(i).getImg());
            }
            mImageUrlAdapter.refreshData(strings);
        }
    }


    @Override
    public void onError(Call call, Exception e, int i) {
//        swipRefreshHome.setRefreshing(false);
        recyclerHome.loadMoreComplete();
        recyclerHome.refreshComplete();
        zProgressHUD.dismiss();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
//        swipRefreshHome.setRefreshing(false);
        recyclerHome.loadMoreComplete();
        recyclerHome.refreshComplete();
        ToastUtil.toast(c_codeBean.getM());
        zProgressHUD.dismiss();
    }

    private void disposePostInfo() {
        if (mPostBeanList != null) {
            for (int i = 0; i < mPostBeanList.size(); i++) {
                HomeBean.HomeInfo.PostBean postBean = mPostBeanList.get(i);
                switch (i) {
                    case 0:
                        rl_bottom_one.setText(postBean.getTitle());
                        break;
                    case 1:
                        rl_bottom_two.setText(postBean.getTitle());
                        break;
                    case 2:
                        rl_bottom_three.setText(postBean.getTitle());
                        break;
                    case 3:
                        rl_bottom_four.setText(postBean.getTitle());
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_consult:
                startActivity(new Intent(getActivity(), ConsultActivity.class));
                break;
            case R.id.rl_bottom_one:
                intent = new Intent(getContext(), ClasicCaseActivity.class);
                intent.putExtra(IntentDataKeyConstant.URL, ConstantUrl.clasicCaseMenu);
                intent.putExtra(IntentDataKeyConstant.TITLE, TITLE_NAME_CASE);
                startActivity(intent);
                break;
            case R.id.rl_bottom_two:
                NewTemplateActivity.start(getContext());
                break;
            case R.id.rl_bottom_three:
                intent = new Intent(getContext(), CaculatorActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_bottom_four:
                //跳转客服
                zProgressHUD.show();
                getRightData();
                break;
            case R.id.linearlayoutGotoData:
                intent = new Intent(getContext(), ClasicCaseActivity.class);
                intent.putExtra(IntentDataKeyConstant.URL, ConstantUrl.clasicConsultMenu);
                intent.putExtra(IntentDataKeyConstant.TITLE, TITLE_NAME_CONSULTING);
                startActivity(intent);
                break;
            case R.id.rl_writ_template:
                intent = new Intent(getContext(), CustomizationListActivity.class);
                startActivity(intent);
                break;
        }

    }


    public void getRightData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.serviceUrl, map, new HomeServiceBean());
    }

    public void getHotInformationList() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", 1 + "");
        map.put("pageSize", 5 + "");
        zProgressHUD.show();
        mOkHttpRequestModel.okhHttpPost(HotInformationListURl, map, new HotInformationBean());
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof HotInformationBean.OBean.ListBean) {
            HotInformationBean.OBean.ListBean listBean = (HotInformationBean.OBean.ListBean) data;
            Intent intent = new Intent(getContext(), GoToActivity.class);
            intent.putExtra(ConstantString.TITLE_NAME, "热门资讯详情");
            intent.putExtra(IntentDataKeyConstant.SHOW_SHARE, true);
            intent.putExtra(IntentDataKeyConstant.DATA, listBean);
            intent.putExtra(ConstantString.URL, ConstantUrl.detailsWebConslting + listBean.getId());
            startActivity(intent);
        }
    }


    public class HotInformationAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_home_information;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new HotInfomationHolder(itemView, context, this);
        }

        public class HotInfomationHolder extends RecyclerBaseHolder {
            TextView textViewContentItem;
            ImageView imageViewHomeHotInformation;

            public HotInfomationHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewContentItem = itemView.findViewById(R.id.textViewContentItem);
                imageViewHomeHotInformation = itemView.findViewById(R.id.imageViewHomeHotInformation);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof HotInformationBean.OBean.ListBean) {
                    HotInformationBean.OBean.ListBean bean = (HotInformationBean.OBean.ListBean) mData;
                    textViewContentItem.setText(bean.getTitle());
                    Glide.with(mContext).load(bean.getSmeta()).into(imageViewHomeHotInformation);
                }
            }
        }
    }


}
