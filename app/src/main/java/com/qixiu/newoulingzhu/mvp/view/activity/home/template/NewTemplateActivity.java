package com.qixiu.newoulingzhu.mvp.view.activity.home.template;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.beans.home.template.TemplateListBean;
import com.qixiu.newoulingzhu.beans.home.template.TemplateMenuBean;
import com.qixiu.newoulingzhu.beans.member.MyMemberBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.customization.CustomCommtActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.customization.CustomizationListActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.template.confidentiality.ConfidentialityActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.member.MyMemberActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.RefreshHeader;
import com.qixiu.newoulingzhu.utils.ArshowDialogUtils;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.XrecyclerViewUtil;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.qixiu.utils.DrawableUtils;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class NewTemplateActivity extends RequstActivity implements XRecyclerView.LoadingListener, ArshowDialogUtils.ArshowDialogListener {
    TemplateMenuAdapter templateMenuAdapter = new TemplateMenuAdapter();
    TemplateListAdapter templateListAdapter = new TemplateListAdapter();
    @BindView(R.id.recyclerTemplateMenu)
    RecyclerView recyclerTemplateMenu;
    @BindView(R.id.recyclerTamplateList)
    XRecyclerView recyclerTamplateList;
    @BindView(R.id.imageViewGotoContract)
    ImageView imageViewGotoContract;
    private int pageNo = 1, pageSize = 10;
    private String type;
    private MyMemberBean memberBean;
    private String notice = "升级会员";

    public static void start(Context context) {
        context.startActivity(new Intent(context, NewTemplateActivity.class));
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("文书模板");
        requestTemplateMenu();
        getMember();
        imageViewGotoContract.setOnClickListener(this);
    }

    private void requestTemplateList() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        CommonUtils.putDataIntoMap(map, "title", "");
        CommonUtils.putDataIntoMap(map, "type", type);
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        post(ConstantUrl.HotTemplateListURl, map, new TemplateListBean());
    }

    private void requestTemplateMenu() {
        Map<String, String> map = new HashMap<>();
        post(ConstantUrl.TemplateTypeURl, map, new TemplateMenuBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_template;
    }

    @Override
    protected void onInitViewNew() {
        recyclerTemplateMenu.setLayoutManager(new LinearLayoutManager(this));
        recyclerTemplateMenu.setAdapter(templateMenuAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        XrecyclerViewUtil.setXrecyclerView(recyclerTamplateList, this, this, true, 1, layoutManager);
        recyclerTamplateList.setAdapter(templateListAdapter);
        RefreshHeader refreshHeader = new RefreshHeader(getContext());
        refreshHeader.setArrowImageView(R.mipmap.refresh_head_arrow);
        recyclerTamplateList.setRefreshHeader(refreshHeader);

        templateMenuAdapter.setOnItemClickListener(new OnRecyclerItemListener() {
            @Override
            public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
                if (data instanceof TemplateMenuBean.OBean) {
                    TemplateMenuBean.OBean oBean = (TemplateMenuBean.OBean) data;
                    pageNo = 1;
                    type = oBean.getId();
                    requestTemplateList();
                    refreshSelect(oBean);//刷新左侧的选择状态
                }
            }
        });
        templateListAdapter.setOnItemClickListener(new OnRecyclerItemListener() {
            @Override
            public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
                if (data instanceof TemplateListBean.TemplateListInfo.ListBean) {
                    TemplateListBean.TemplateListInfo.ListBean listBean = (TemplateListBean.TemplateListInfo.ListBean) data;
//                    if (Double.parseDouble(listBean.getRead()) <= 0) {
//                        ArshowDialogUtils.showDialog(getActivity(), "您的次数已用完,升级会员后可浏览及下载", notice, "取消", NewTemplateActivity.this);
//                    }
//                    if (memberBean.getO().getType().equals("1")) {
//                        ArshowDialogUtils.showDialog(getActivity(), "以下内容为会员权益,开通会员后可下载", notice, "取消", NewTemplateActivity.this);
//                        return;
//                    }
                    Intent intent = new Intent(getActivity(), ConfidentialityActivity.class);
                    intent.putExtra(IntentDataKeyConstant.ID, listBean.getId());
                    intent.putExtra(IntentDataKeyConstant.FILE_PATH, listBean.getCount());
                    intent.putExtra(IntentDataKeyConstant.IMAGE, listBean.getSmeta());
                    intent.putExtra(IntentDataKeyConstant.TYPE, memberBean.getO().getType());
                    startActivity(intent);
                }


            }
        });


    }

    private void refreshSelect(TemplateMenuBean.OBean oBean) {
        List<TemplateMenuBean.OBean> datas = templateMenuAdapter.getDatas();
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setIs_selected(false);
        }
        oBean.setIs_selected(true);
        templateMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewGotoContract:

                break;
        }
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data, i);
        if (data instanceof MyMemberBean) {
            memberBean = (MyMemberBean) data;
            if (memberBean.getO().getType().equals("1")) {
                notice = "开通会员";
            }
        }
        if (data instanceof TemplateMenuBean) {
            TemplateMenuBean templateMenuBean = (TemplateMenuBean) data;
            List<TemplateMenuBean.OBean> datas = new ArrayList<>();
            TemplateMenuBean.OBean oBean = new TemplateMenuBean.OBean();//先创建一个全部
            oBean.setIs_selected(true);
            oBean.setId("");
            oBean.setValue("全部");
            datas.add(oBean);
            datas.addAll(templateMenuBean.getO());
//            if (templateMenuBean.getO().size() != 0) {
//                type = templateMenuBean.getO().get(0).getId();
//                templateMenuBean.getO().get(0).setSelected(true);
//            }
            requestTemplateList();
            templateMenuAdapter.refreshData(datas);
        }
        if (data instanceof TemplateListBean) {
            TemplateListBean bean = (TemplateListBean) data;
            if (pageNo == 1) {
                templateListAdapter.refreshData(bean.getO().getList());
            } else {
                templateListAdapter.addDatas(bean.getO().getList());
            }
        }
        recyclerTamplateList.loadMoreComplete();
        recyclerTamplateList.refreshComplete();

        if (data instanceof CustomizationListActivity.CustomItemBean) {
            CustomizationListActivity.CustomItemBean bean = (CustomizationListActivity.CustomItemBean) data;
            for (int j = 0; j <bean.getO().size() ; j++) {
                if(bean.getO().get(j).getId().equals("7")){
                    CustomCommtActivity.start(getContext(),bean.getO().get(j));
                 break;
                }
            }
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call, e, i);
        recyclerTamplateList.loadMoreComplete();
        recyclerTamplateList.refreshComplete();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
        recyclerTamplateList.loadMoreComplete();
        recyclerTamplateList.refreshComplete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void getMember() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        post(ConstantUrl.myVipUrl, stringMap, new MyMemberBean());
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        requestTemplateList();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        requestTemplateList();
    }

    @Override
    public void onClickPositive(DialogInterface dialogInterface, int which) {
        // TODO: 2018/8/23
        Intent intent = new Intent(getActivity(), MyMemberActivity.class);
        intent.putExtra(IntentDataKeyConstant.TYPE, memberBean.getO().getType());
        startActivity(intent);
    }

    @Override
    public void onClickNegative(DialogInterface dialogInterface, int which) {

    }
    @OnClick(R.id.imageViewGotoContract)
    public void gotoContract(View view) {
        Map<String, String> map = new HashMap<>();
        get(ConstantUrl.customListUrl, map, new CustomizationListActivity.CustomItemBean());
    }

    public class TemplateMenuAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_template_menu;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new TemplateMenuHolder(itemView, context, this);
        }

        public class TemplateMenuHolder extends RecyclerBaseHolder {
            TextView textViewTemplateMenu;
            View viewRightLine;

            public TemplateMenuHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewTemplateMenu = itemView.findViewById(R.id.textViewTemplateMenu);
                viewRightLine = itemView.findViewById(R.id.viewRightLine);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof TemplateMenuBean.OBean) {
                    TemplateMenuBean.OBean bean = (TemplateMenuBean.OBean) mData;
                    textViewTemplateMenu.setText(bean.getValue());
                    if (bean.isIs_selected()) {
                        viewRightLine.setVisibility(View.GONE);
                        textViewTemplateMenu.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        DrawableUtils.setDrawableResouce(textViewTemplateMenu, mContext, 0, 0, R.mipmap.triangle_right2x, 0);
                    } else {
                        viewRightLine.setVisibility(View.VISIBLE);
                        textViewTemplateMenu.setBackgroundColor(mContext.getResources().getColor(R.color.back_grey));
                        DrawableUtils.setDrawableResouce(textViewTemplateMenu, mContext, 0, 0, 0, 0);
                    }
                }
            }
        }
    }

    public class TemplateListAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_templatelist;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new TemplateListHolder(itemView, context, this);
        }

        public class TemplateListHolder extends RecyclerBaseHolder {
            ImageView imageViewTemplateContent;
            TextView textViewTemplateContent;

            public TemplateListHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                imageViewTemplateContent = itemView.findViewById(R.id.imageViewTemplateContent);
                textViewTemplateContent = itemView.findViewById(R.id.textViewTemplateContent);

            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof TemplateListBean.TemplateListInfo.ListBean) {
                    TemplateListBean.TemplateListInfo.ListBean bean = (TemplateListBean.TemplateListInfo.ListBean) mData;
                    Glide.with(mContext).load(bean.getSmeta()).into(imageViewTemplateContent);
                    textViewTemplateContent.setText(bean.getTitle());
                }
            }
        }
    }
}
