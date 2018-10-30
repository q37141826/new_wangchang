package com.qixiu.newoulingzhu.mvp.view.fragment.home.template;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.beans.BaseParameter;
import com.qixiu.newoulingzhu.beans.home.template.TemplateListBean;
import com.qixiu.newoulingzhu.beans.home.template.TemplateTypeBean;
import com.qixiu.newoulingzhu.beans.member.MyMemberBean;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.template.WritTemplateActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.template.confidentiality.ConfidentialityActivity;
import com.qixiu.newoulingzhu.mvp.view.adapter.homne.template.WritTemplateAdapter;
import com.qixiu.newoulingzhu.mvp.view.adapter.homne.template.WritTemplateTagAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.newoulingzhu.utils.ArshowDialogUtils;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.decalaration.LinearSpacesItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class WritTemplateFragment extends BaseFragment implements XRecyclerView.LoadingListener, OnRecyclerItemListener, ArshowDialogUtils.ArshowDialogListener, OKHttpUIUpdataListener, View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {

    private XRecyclerView mRv_writ_template;
    private WritTemplateAdapter mWritTemplateAdapter;
    private RecyclerView mRv_template_tag;
    private WritTemplateTagAdapter mWritTemplateTagAdapter;

    private boolean isAllunfold;
    private OKHttpRequestModel mOkHttpRequestModel;
    private List<TemplateTypeBean.TemplateTypeInfo> mTemplateTypeInfos;
    private BaseParameter mBaseParameter;
    private boolean isMore;
    private String mTypeId;
    private String content;
    private EditText mEt_search;
    private SwipeRefreshLayout mSrl_writ_template;
    private ZProgressHUD mZProgressHUD;
    private TemplateEditTextListener mTemplateEditTextListener;
    private TemplateListBean.TemplateListInfo.ListBean listBean;
    private MyMemberBean memberBean;
    private String notice="升级会员";

    public void getMember() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.myVipUrl, stringMap, new MyMemberBean());
    }

    public static class TemplateEditTextListener implements TextWatcher {

        private final WeakReference<WritTemplateFragment> mWeakReference;

        public TemplateEditTextListener(WritTemplateFragment writTemplateFragment) {
            mWeakReference = new WeakReference(writTemplateFragment);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            WritTemplateFragment writTemplateFragment = mWeakReference.get();
            if (writTemplateFragment != null) {
                writTemplateFragment.content = s.toString();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    protected void onInitData() {
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        mBaseParameter = new BaseParameter();
        if (getActivity() instanceof WritTemplateActivity) {

            mOkHttpRequestModel.okhHttpPost(ConstantUrl.TemplateTypeURl, null, new TemplateTypeBean());
        }
        requestTemplateList();
        getMember();
    }

    public void onSearch(String content) {
        if (mZProgressHUD != null) {
            mZProgressHUD.show();
        }
        this.content = content;
        isMore = false;
        requestTemplateList();
    }

    private void requestTemplateList() {

        if (mOkHttpRequestModel == null) {
            mOkHttpRequestModel = new OKHttpRequestModel(this);
        }
        if (mBaseParameter == null) {
            mBaseParameter = new BaseParameter();
        }
        String url;
        if (getActivity() instanceof MainActivity) {

            url = ConstantUrl.HotTemplateListURl;
        } else {
            url = ConstantUrl.HomeTemplateListURl;
        }

        Map<String, String> stringMap = new HashMap<>();
        if (!isMore) {
            mBaseParameter.setPageNo(1);

        }
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put(IntentDataKeyConstant.PAGENO, mBaseParameter.getPageNo() + StringConstants.EMPTY_STRING);
        stringMap.put(IntentDataKeyConstant.PAGESIZE, mBaseParameter.getPageSize() + StringConstants.EMPTY_STRING);
        if (!TextUtils.isEmpty(mTypeId)) {
            stringMap.put("type", mTypeId);
        }
        if (!TextUtils.isEmpty(content)) {
            stringMap.put("title", content);
        }
        mOkHttpRequestModel.okhHttpPost(url, stringMap, new TemplateListBean());
    }

    @Override
    protected void onInitView(View view) {
        mSrl_writ_template = findViewById(R.id.srl_writ_template);
        mSrl_writ_template.setOnRefreshListener(this);
        mSrl_writ_template.setColorSchemeResources(R.color.theme_color);
        mZProgressHUD = new ZProgressHUD(getActivity());
        initListView();


    }

    private void initListView() {

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mWritTemplateAdapter = new WritTemplateAdapter();
        mRv_writ_template = findViewById(R.id.rv_writ_template);
        mRv_writ_template.setPullRefreshEnabled(false);
        mRv_writ_template.setLoadingListener(this);
        mRv_writ_template.setLoadingMoreEnabled(true);
        mRv_writ_template.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        mRv_writ_template.setLayoutManager(layoutManager);
        if (getActivity() instanceof WritTemplateActivity) {
            View templateHead = View.inflate(getActivity(), R.layout.layout_template_head, null);
            mRv_template_tag = (RecyclerView) templateHead.findViewById(R.id.rv_template_tag);
            mWritTemplateTagAdapter = new WritTemplateTagAdapter();
            mRv_template_tag.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            mRv_template_tag.setAdapter(mWritTemplateTagAdapter);

            mWritTemplateTagAdapter.setOnItemClickListener(this);

            mRv_writ_template.addHeaderView(templateHead);
            //  mRv_writ_template.addItemDecoration(new DiyDividerItemDecoration(getActivity(), 0, 0, LinearLayoutManager.VERTICAL, templateHead));
            mRv_writ_template.addItemDecoration(new LinearSpacesItemDecoration(LinearLayoutManager.VERTICAL, ArshowContextUtil.dp2px(13)));

        } else if (getActivity() instanceof MainActivity) {
            View ll_search = findViewById(R.id.ll_search);
            ll_search.setVisibility(View.VISIBLE);
            mRv_writ_template.addItemDecoration(new LinearSpacesItemDecoration(LinearLayoutManager.VERTICAL, ArshowContextUtil.dp2px(13)));
            mEt_search = findViewById(R.id.et_search);
            mTemplateEditTextListener = new TemplateEditTextListener(this);
            mEt_search.addTextChangedListener(mTemplateEditTextListener);
            mEt_search.setOnTouchListener(this);
        } else {
            mRv_writ_template.addItemDecoration(new LinearSpacesItemDecoration(LinearLayoutManager.VERTICAL, ArshowContextUtil.dp2px(13)));
        }


        mWritTemplateAdapter.setOnItemClickListener(this);

        mRv_writ_template.setAdapter(mWritTemplateAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_writ_template;
    }

    @Override
    public void onRefresh() {
        isMore = false;
        requestTemplateList();
    }

    @Override
    public void onLoadMore() {
        isMore = true;
        requestTemplateList();
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {

        if (adapter instanceof WritTemplateTagAdapter) {
            int position = mRv_template_tag.getChildLayoutPosition(v);
            if (position == mWritTemplateTagAdapter.getDatas().size()) {
                if (isAllunfold) {
                    mWritTemplateTagAdapter.refreshData(mTemplateTypeInfos);
                } else {
                    List<TemplateTypeBean.TemplateTypeInfo> templateTypeInfos = splitData(mTemplateTypeInfos, 7);
                    if (templateTypeInfos != null) {
                        mWritTemplateTagAdapter.refreshData(templateTypeInfos);
                    } else {
                        mWritTemplateTagAdapter.refreshData(mTemplateTypeInfos);
                    }
                }
                isAllunfold = !isAllunfold;
            } else {
                //请求服务器
                TemplateTypeBean.TemplateTypeInfo templateTypeInfo = (TemplateTypeBean.TemplateTypeInfo) data;
                for (int i = 0; i < mTemplateTypeInfos.size(); i++) {
                    mTemplateTypeInfos.get(i).setIs_selected(false);
                }
                //第一次点击选中之后刷新列表，再点一次取消选中刷新列表
                if (templateTypeInfo.getId().equals(mTypeId)) {
                    mTypeId = "";
                    templateTypeInfo.setIs_selected(false);
                } else {
                    mTypeId = templateTypeInfo.getId();
                    templateTypeInfo.setIs_selected(true);
                }
                if(TextUtils.isEmpty(mTypeId)){//如果非选中状态或者选中的是第一个,设置第一个全部为选中状态
                    mTemplateTypeInfos.get(0).setIs_selected(true);
                }
                mWritTemplateTagAdapter.notifyDataSetChanged();
                requestTemplateList();
            }
        } else {
            listBean = (TemplateListBean.TemplateListInfo.ListBean) data;
            if(memberBean.getO().getType().equals("1")){
                ArshowDialogUtils.showDialog(getActivity(), "以下内容为会员权益,开通会员后可浏览及下载", notice, "取消", this);
                return;
            }
            if (Double.parseDouble(listBean.getRead()) <= 0) {
                ArshowDialogUtils.showDialog(getActivity(), "您的次数已用完,升级会员后可浏览及下载", notice, "取消", this);
            } else {
                Intent intent = new Intent(getActivity(), ConfidentialityActivity.class);
                intent.putExtra(IntentDataKeyConstant.ID, listBean.getId());
                intent.putExtra(IntentDataKeyConstant.FILE_PATH,listBean.getCount());
                startActivity(intent);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodeConstant.REQUESTCODE_EMAIL && resultCode == IntentRequestCodeConstant.RESULTCODE_EMAIL) {
            onRefresh();
        }
    }

    @Override
    public void onClickPositive(DialogInterface dialogInterface, int which) {
        // TODO: 2018/8/23  
//        Intent intent=new Intent(getActivity(), DredgeMemberActivity.class);
//        intent.putExtra(IntentDataKeyConstant.TYPE, memberBean.getO().getType());
//        startActivity(intent);
    }

    @Override
    public void onClickNegative(DialogInterface dialogInterface, int which) {

    }

    @Override
    public void onSuccess(Object data, int i) {
        if(data instanceof MyMemberBean){
            memberBean = (MyMemberBean) data;
            if(memberBean.getO().getType().equals("1")){
                notice="开通会员";
            }
        }
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();
        }
        if (mSrl_writ_template.isRefreshing()) {
            mSrl_writ_template.setRefreshing(false);
        }
        if (isMore) {
            mRv_writ_template.refreshComplete();
        }
        if (data instanceof TemplateTypeBean) {
            TemplateTypeBean templateTypeBean = (TemplateTypeBean) data;
            mTemplateTypeInfos=new ArrayList<>();
            TemplateTypeBean.TemplateTypeInfo  firstData=new TemplateTypeBean.TemplateTypeInfo();
            firstData.setIs_selected(true);
            firstData.setId("");
            firstData.setValue("全部");
            mTemplateTypeInfos.add(firstData);
            mTemplateTypeInfos.addAll(templateTypeBean.getO());
            List<TemplateTypeBean.TemplateTypeInfo> templateTypeInfos = splitData(mTemplateTypeInfos, 7);
            mWritTemplateTagAdapter.setAllDataCount(mTemplateTypeInfos.size());
            if (templateTypeInfos != null) {
                mWritTemplateTagAdapter.refreshData(templateTypeInfos);
            } else {
                mWritTemplateTagAdapter.refreshData(mTemplateTypeInfos);
            }

        } else if (data instanceof TemplateListBean) {
            TemplateListBean templateListBean = (TemplateListBean) data;
            if (!isMore) {
                mWritTemplateAdapter.refreshData(templateListBean.getO().getList());
            } else {
                mWritTemplateAdapter.addDatas(templateListBean.getO().getList());
            }
            if (templateListBean.getO().getList().size() > 0) {
                mBaseParameter.setPageNo(mBaseParameter.getPageNo() + 1);
            }
        }

    }

    private List<TemplateTypeBean.TemplateTypeInfo> splitData(List<TemplateTypeBean.TemplateTypeInfo> source, int count) {
        if (source == null) {
            return null;
        }

        if (source.size() > count) {
            List<TemplateTypeBean.TemplateTypeInfo> templateTypeInfos = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                templateTypeInfos.add(source.get(i));
            }
            return templateTypeInfos;
        } else {
            return null;
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        if (mSrl_writ_template.isRefreshing()) {
            mSrl_writ_template.setRefreshing(false);
        }
        if (isMore) {
            mRv_writ_template.refreshComplete();
        }
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();
        }
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        if (mSrl_writ_template.isRefreshing()) {
            mSrl_writ_template.setRefreshing(false);
        }
        if (isMore) {
            mRv_writ_template.refreshComplete();
        }
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        if (mEt_search != null && mTemplateEditTextListener != null) {
            mEt_search.removeTextChangedListener(mTemplateEditTextListener);
        }
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Drawable drawable = mEt_search.getCompoundDrawables()[2];
            int width = drawable.getBounds().width();
            if (event.getX() + 4 * width >= (mEt_search.getRight() - 2 * width)) {
                onSearch(mEt_search.getText().toString().trim());
                ArshowContextUtil.hideSoftKeybord(v);
                return true;
            }

        }

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        isMore = false;
        requestTemplateList();
    }
}
