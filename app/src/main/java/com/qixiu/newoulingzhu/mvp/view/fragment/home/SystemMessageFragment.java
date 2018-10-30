package com.qixiu.newoulingzhu.mvp.view.fragment.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.beans.BaseParameter;
import com.qixiu.newoulingzhu.beans.messge.SystemMessageListBean;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.adapter.message.SystemMessageAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.TitleFragment;
import com.qixiu.newoulingzhu.mvp.wight.loading.RefreshHeader;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.decalaration.LinearSpacesItemDecoration;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by my on 2018/8/13.
 */

public class SystemMessageFragment  extends TitleFragment implements XRecyclerView.LoadingListener, OnRecyclerItemListener, OKHttpUIUpdataListener, SwipeRefreshLayout.OnRefreshListener {
    private SystemMessageAdapter mMMessageListAdapter;
    private OKHttpRequestModel mOkHttpRequestModel;
//    private SwipeRefreshLayout mSrl_system_message;
    private XRecyclerView mRv_system_message;
    private BaseParameter mBaseParameter;
    private boolean isMore;
   ImageView imageViewNothing;
    @Override
    protected void onInitViewNew(View view) {
        mTitleView.getView().setVisibility(View.GONE);
        imageViewNothing=findViewById(R.id.imageViewNothing);
//        mSrl_system_message = (SwipeRefreshLayout)view. findViewById(R.id.srl_system_message);
//        mSrl_system_message.setOnRefreshListener(this);
//        mSrl_system_message.setColorSchemeResources(R.color.theme_color);
        mRv_system_message = (XRecyclerView) view.findViewById(R.id.rv_system_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMMessageListAdapter = new SystemMessageAdapter();
        RefreshHeader refreshHeader=new RefreshHeader(getContext());
        refreshHeader.setArrowImageView(R.mipmap.refresh_head_arrow);
        mRv_system_message.setRefreshHeader(refreshHeader);

        mRv_system_message.setPullRefreshEnabled(true);
        mRv_system_message.setLoadingListener(this);
        mRv_system_message.setLoadingMoreEnabled(true);
        mRv_system_message.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        mRv_system_message.setLayoutManager(layoutManager);
        mRv_system_message.addItemDecoration(new LinearSpacesItemDecoration(LinearLayoutManager.VERTICAL, ArshowContextUtil.dp2px(10)));
        mRv_system_message.setAdapter(mMMessageListAdapter);
        mMMessageListAdapter.setOnItemClickListener(this);
    }



    @Override
    protected void onInitData() {

        mOkHttpRequestModel = new OKHttpRequestModel(this);
        mBaseParameter = new BaseParameter();

        requestData();
    }

    private void requestData() {
        Map<String, String> stringMap = new HashMap<>();
        if (!isMore) {
            mBaseParameter.setPageNo(1);
        }
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put(IntentDataKeyConstant.PAGENO, mBaseParameter.getPageNo() + StringConstants.EMPTY_STRING);
        stringMap.put(IntentDataKeyConstant.PAGESIZE, mBaseParameter.getPageSize() + StringConstants.EMPTY_STRING);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.systemMessageList, stringMap, new SystemMessageListBean());
    }


    @Override
    public void onRefresh() {
        isMore = false;
        requestData();
    }

    @Override
    public void onLoadMore() {
        isMore = true;
        requestData();
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {

    }

    @Override
    public void onSuccess(Object data, int i) {
//        if (mSrl_system_message.isRefreshing()) {
//            mSrl_system_message.setRefreshing(false);
//        }
        mRv_system_message.refreshComplete();
        mRv_system_message.loadMoreComplete();
        if (data instanceof SystemMessageListBean) {
            SystemMessageListBean systemMessageListBean = (SystemMessageListBean) data;

            SystemMessageListBean.SystemMessageListInfo systemMessageListInfo = systemMessageListBean.getO();
            if (!isMore) {

                mMMessageListAdapter.refreshData(systemMessageListInfo.getList());
            } else {
                mMMessageListAdapter.addDatas(systemMessageListInfo.getList());
            }
            if (systemMessageListInfo.getList().size() > 0) {
                mBaseParameter.setPageNo(mBaseParameter.getPageNo() + 1);
            }
        }
        if(mMMessageListAdapter.getDatas().size()==0){
            imageViewNothing.setVisibility(View.VISIBLE);
        }else {
            imageViewNothing.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError(Call call, Exception e, int i) {
//        if (mSrl_system_message.isRefreshing()) {
//            mSrl_system_message.setRefreshing(false);
//        }

            mRv_system_message.refreshComplete();
            mRv_system_message.loadMoreComplete();

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
//        if (mSrl_system_message.isRefreshing()) {
//            mSrl_system_message.setRefreshing(false);
//        }
        mRv_system_message.refreshComplete();
        mRv_system_message.loadMoreComplete();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_sysytem_message;
    }


}
