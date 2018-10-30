package com.qixiu.newoulingzhu.mvp.view.fragment.home.cases;

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
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.beans.home.cases.CasesListBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.wight.loading.RefreshHeader;
import com.qixiu.newoulingzhu.utils.XrecyclerViewUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by my on 2018/8/23.
 */

public class CasesFragment extends BaseFragment implements OKHttpUIUpdataListener, XRecyclerView.LoadingListener, OnRecyclerItemListener {
    OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(this);
    String type;
    String id;
    @BindView(R.id.recyclerViewClasicCases)
    XRecyclerView recyclerViewClasicCases;
//    @BindView(R.id.swipClasicCases)
//    SwipeRefreshLayout swipClasicCases;
    Unbinder unbinder;
    CaseAdapter adapter = new CaseAdapter();
    @BindView(R.id.imageNothing)
    ImageView imageNothing;
    private int page = 1, num = 10;
    private int currentPage;
    private String itemTitle;



    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void onInitData() {
        requstData();
    }

    private void requstData() {
        Map<String, String> map = new HashMap<>();
        map.put("typeid", id);
        map.put("page", page + "");
        map.put("num", num + "");
        okHttpRequestModel.okhHttpPost(ConstantUrl.clasicCaseList, map, new CasesListBean());

    }

    @Override
    protected void onInitView(View view) {
//        swipClasicCases.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                requstData();
//            }
//        });
        XrecyclerViewUtil.setXrecyclerView(recyclerViewClasicCases, this, getContext(), true, 1, null);
        recyclerViewClasicCases.setAdapter(adapter);
        RefreshHeader refreshHeader=new RefreshHeader(getContext());
        refreshHeader.setArrowImageView(R.mipmap.refresh_head_arrow);
        recyclerViewClasicCases.setRefreshHeader(refreshHeader);
        adapter.setOnItemClickListener(this);
//        swipClasicCases.setColorSchemeResources(R.color.theme_color);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_cases;
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof CasesListBean) {
            CasesListBean bean = (CasesListBean) data;
            if (page == 1) {
                adapter.refreshData(bean.getO().getData());
            } else {
                adapter.addDatas(bean.getO().getData());
            }
        }
//        swipClasicCases.setRefreshing(false);
        recyclerViewClasicCases.loadMoreComplete();
        recyclerViewClasicCases.refreshComplete();
        if(adapter.getDatas().size()==0){
            imageNothing.setVisibility(View.VISIBLE);
        }else {
            imageNothing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
//        swipClasicCases.setRefreshing(false);
        recyclerViewClasicCases.loadMoreComplete();
        recyclerViewClasicCases.refreshComplete();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
//        swipClasicCases.setRefreshing(false);
        recyclerViewClasicCases.loadMoreComplete();
        recyclerViewClasicCases.refreshComplete();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        page=1;
        requstData();
    }

    @Override
    public void onLoadMore() {
        page++;
        requstData();
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof CasesListBean.OBean.DataBean) {
            CasesListBean.OBean.DataBean bean = (CasesListBean.OBean.DataBean) data;
            bean.setType(type);
            Intent intent = new Intent(getContext(), GoToActivity.class);
            intent.putExtra(ConstantString.TITLE_NAME, itemTitle);
            intent.putExtra(IntentDataKeyConstant.SHOW_SHARE, true);
            intent.putExtra(IntentDataKeyConstant.DATA, bean);
            String url = ConstantUrl.hosturl + "/detail/advisoryDetail.html?URL=" + ConstantUrl.hosturl + "&id=" + bean.getId();
            intent.putExtra(ConstantString.URL, url);
            startActivity(intent);
        }
    }


    public class CaseAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_case;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new CaseHolder(itemView, context, this);
        }

        public class CaseHolder extends RecyclerBaseHolder {
            LinearLayout linearlayout_head, linearlayout_item, linearlayoutCaseLayout;
            ImageView imageViewHeadCase;
            TextView textViewContentHead, textViewCasename;

            TextView textViewCaseContent, textViewGotoDetails;
            ImageView imageViewCaseItem;

            public CaseHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                linearlayout_head = itemView.findViewById(R.id.linearlayout_head);
                linearlayoutCaseLayout = itemView.findViewById(R.id.linearlayoutCaseLayout);
                linearlayout_item = itemView.findViewById(R.id.linearlayout_item);
                textViewCaseContent = itemView.findViewById(R.id.textViewCaseContent);
                textViewGotoDetails = itemView.findViewById(R.id.textViewGotoDetails);
                textViewCasename = itemView.findViewById(R.id.textViewCasename);
                textViewContentHead = itemView.findViewById(R.id.textViewContentHead);
                imageViewCaseItem = itemView.findViewById(R.id.imageViewCaseItem);
                imageViewHeadCase = itemView.findViewById(R.id.imageViewHeadCase);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof CasesListBean.OBean.DataBean) {
                    CasesListBean.OBean.DataBean bean = (CasesListBean.OBean.DataBean) mData;
                    textViewCaseContent.setText(bean.getTitle());
                    Glide.with(mContext).load(bean.getThumbnail()).into(imageViewCaseItem);
                    Glide.with(mContext).load(bean.getArticlesmeta()).into(imageViewHeadCase);
                    textViewContentHead.setText(bean.getTitle());
                    if (itemTitle.contains("案例")) {
                        textViewCasename.setText("热门案例");
                    } else {
                        textViewCasename.setText("热门资讯");
                    }
                    if (currentPage == 0 && position == 0) {
                        linearlayout_head.setVisibility(View.VISIBLE);
                        linearlayout_item.setVisibility(View.GONE);
                    } else if (currentPage != 0 && position == 0) {
                        linearlayoutCaseLayout.setVisibility(View.VISIBLE);
                        linearlayout_head.setVisibility(View.GONE);
                        linearlayout_item.setVisibility(View.VISIBLE);
                    } else {
                        linearlayoutCaseLayout.setVisibility(View.GONE);
                        linearlayout_head.setVisibility(View.GONE);
                        linearlayout_item.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }


}
