package com.qixiu.newoulingzhu.mvp.view.activity.mine.member;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.PayResult;
import com.alipay.interf.IPay;
import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.application.LoginStatus;
import com.qixiu.newoulingzhu.beans.member.MyMemberBean;
import com.qixiu.newoulingzhu.beans.mine_bean.member.DredgeMemberPromptPayBean;
import com.qixiu.newoulingzhu.beans.mine_bean.member.MemberListBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.MyprofileActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.ProblemPaymentActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.pay.AliWeixinPayActivity;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowDialog;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
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

import static com.qixiu.newoulingzhu.constant.ConstantUrl.is_info_enough;
import static com.qixiu.newoulingzhu.constant.ConstantUrl.myVipWeb;


/**
 * Created by Administrator on 2017/11/17 0017.
 */

public class MyMemberActivity extends TitleActivity implements OKHttpUIUpdataListener<BaseBean>, OnRecyclerItemListener ,IPay{
    @BindView(R.id.webViewMenber)
    WebView webViewMenber;
    @BindView(R.id.webViewMenberInroduction)
    WebView webViewMenberInroduction;
    private TextView tv_buy_member;
    private TextView mTv_dredge_status;
    private OKHttpRequestModel mOkHttpRequestModel;
    private TextView mTv_dredge_member;
    private ImageView mCircular_head_mine;
    private TextView mTextView_nickname_mine;

    private MyMemberBean.MyMemberInfo mMyMemberInfo;
    private TextView mTv_validity;
    private TextView mTv_member_number;
    @BindView(R.id.textView_member_level)
    TextView textView_member_level;
    private MemberListBean memberListBean;
    private MemberListAdapter adapterMember;
    private MemberListBean.BuyOrUpData selectedData;
    private PopupWindow popupWindow;
    private BroadcastReceiver receiver;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyMemberActivity.class));
    }

    @Override
    protected void onInitViewNew() {
        mTitleView.setTitle("我的会员");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTv_dredge_status = (TextView) findViewById(R.id.tv_dredge_status);
        mTv_dredge_member = (TextView) findViewById(R.id.tv_dredge_member);
        mCircular_head_mine = (ImageView) findViewById(R.id.circular_head_mine);
        mTextView_nickname_mine = (TextView) findViewById(R.id.textView_nickname_mine);
        tv_buy_member = (TextView) findViewById(R.id.tv_buy_member);
        mTv_member_number = (TextView) findViewById(R.id.tv_member_number);

        mTv_validity = (TextView) findViewById(R.id.tv_validity);
        tv_buy_member.setOnClickListener(this);
        Glide.with(this).load(Preference.get(ConstantString.HEAD, StringConstants.EMPTY_STRING)).error(R.mipmap.headplace).skipMemoryCache(false).crossFade().into(mCircular_head_mine);
        mTv_dredge_member.setOnClickListener(this);
        //微信支付监听
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                onInitData();
                finish();
            }
        };
        IntentFilter intentFilter=new IntentFilter(IntentDataKeyConstant.BROADCAST_WECHAT_PAY_OK);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dredge_member://todo 开通会员的弹窗
                if (mTv_dredge_member.getText().toString().contains("开通")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", Preference.get(ConstantString.USERID, ""));
                    mOkHttpRequestModel.okhHttpPost(is_info_enough, map, new BaseBean());

                } else {//todo 升级会员的弹窗
                    if (mMyMemberInfo != null) {
//                        Intent intent = new Intent(this, DredgeMemberActivity.class);
//                        intent.putExtra(IntentDataKeyConstant.TYPE, mMyMemberInfo.getType());
//                        startActivity(intent);
                        //需求变更  升级从跳转界面变更为  弹窗
                        if (memberListBean != null) {
                            showPop(2);
                        }
                    }
                }
                break;

            case R.id.tv_buy_member:
                if (mMyMemberInfo != null) {
                    Intent intent = new Intent(this, ProblemPaymentActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }

    @Override
    protected void onInitData() {
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        requestData();
        requestMemberData();
    }

    private void requestData() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.myVipUrl, stringMap, new MyMemberBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_mymember;
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        if (data instanceof MyMemberBean) {
            MyMemberBean myMemberBean = (MyMemberBean) data;
            mMyMemberInfo = myMemberBean.getO();
            MyMemberBean.EBean eBean = myMemberBean.getE();
            textView_member_level.setText(mMyMemberInfo.getVipname());
            mTv_dredge_member.setEnabled(true);
            if (StringConstants.STRING_1.equals(mMyMemberInfo.getType())) {
                mTv_dredge_member.setText("开通会员");
            } else if (StringConstants.STRING_2.equals(mMyMemberInfo.getType())) {
                mTv_dredge_member.setText("升级会员");
                if (mMyMemberInfo.getLevel().equals("4")) {
                    mTv_dredge_member.setBackgroundResource((R.drawable.btn_grey_unclick));
                    mTv_dredge_member.setEnabled(false);
                }
            } else {
                mTv_dredge_member.setText("续费会员");
            }
            mTv_member_number.setText("会员编号 : " + mMyMemberInfo.getNum());
            mTv_validity.setText("有效日期 : " + mMyMemberInfo.getTime());
            mTextView_nickname_mine.setText(mMyMemberInfo.getTrue_name());
//            Glide.with(this).load(mMyMemberInfo.getHead()).error(R.mipmap.headplace).skipMemoryCache(false).crossFade().into(mCircular_head_mine);
            mTv_dredge_status.setText("当前状态 : " + mMyMemberInfo.getVipname());
            webViewMenber.loadUrl( myVipWeb+ LoginStatus.getToken());
            WebSettings settings = webViewMenber.getSettings();
//            settings.setBuiltInZoomControls(true);//oppo r11手机存在一个问题，webview无法翻滚的问题
//            settings.setSupportZoom(false);
//            settings.setDisplayZoomControls(false);
            settings.setJavaScriptEnabled(true);
            webViewMenberInroduction.setVisibility(View.GONE);

        }
        if (data.getUrl().equals(is_info_enough)) {//如果资料齐全
            if (data.getC() == 1) {
                if (mMyMemberInfo != null) {
//                    Intent intent = new Intent(this, DredgeMemberActivity.class);
//                    intent.putExtra(IntentDataKeyConstant.TYPE, mMyMemberInfo.getType());
//                    startActivity(intent);
                    //需求调整了  原先跳转界面换成了 这个
                    if (memberListBean != null) {
                        showPop(1);
                    }
                }
            } else {
                ToastUtil.toast(data.getM());
            }
        }
        if (data instanceof MemberListBean) {
            memberListBean = (MemberListBean) data;
        }
        if (data instanceof DredgeMemberPromptPayBean) {
            DredgeMemberPromptPayBean bean = (DredgeMemberPromptPayBean) data;
            Intent intent = new Intent(this, AliWeixinPayActivity.class);
            intent.putExtra(IntentDataKeyConstant.ORDER_ID, bean.getO().getOrder());
            intent.putExtra(IntentDataKeyConstant.ID, bean.getO().getId());
            intent.putExtra(IntentDataKeyConstant.MONEY, bean.getO().getMoney());
            startActivity(intent);
        }
    }

    private void showPop(int type) {
        PopClickListenner clickListenner = new PopClickListenner();
        View contentView = View.inflate(getContext(), R.layout.pop_buy_dgree_menber, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setClippingEnabled(false);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        Button btnConfirmOpen = contentView.findViewById(R.id.btnConfirmOpen);
        RelativeLayout relativeLayout_pop_dismiss = contentView.findViewById(R.id.relativeLayout_pop_dismiss);
        TextView textViewChargeRules = contentView.findViewById(R.id.textViewChargeRules);
        TextView textViewTitlePop = contentView.findViewById(R.id.textViewTitlePop);
        RecyclerView recyclerViewMenberList = contentView.findViewById(R.id.recyclerViewMenberList);
        recyclerViewMenberList.setLayoutManager(new LinearLayoutManager(this));
        btnConfirmOpen.setOnClickListener(clickListenner);
        textViewChargeRules.setOnClickListener(clickListenner);
        relativeLayout_pop_dismiss.setOnClickListener(clickListenner);
        adapterMember = new MemberListAdapter();
        recyclerViewMenberList.setAdapter(adapterMember);
        if (type == 1) {
            adapterMember.refreshData(memberListBean.getO().getBuy());
        } else {
            adapterMember.refreshData(memberListBean.getO().getUp());
        }
        adapterMember.setOnItemClickListener(this);
        if (mTv_dredge_member.getText().toString().contains("开通")) {
            textViewTitlePop.setText("开通会员");
            btnConfirmOpen.setText("确认开通");
        } else if (mTv_dredge_member.getText().toString().contains("升级")) {
            textViewTitlePop.setText("升级会员");
            btnConfirmOpen.setText("确认升级");
        } else {
            textViewTitlePop.setText("续费会员");
            btnConfirmOpen.setText("确认续费");
        }
        popupWindow.showAsDropDown(contentView);
    }



    public class PopClickListenner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnConfirmOpen:
                    if (selectedData == null) {
                        ToastUtil.toast("请选择升级会员");
                        return;
                    }
                    if (mTv_dredge_member.getText().toString().contains("开通")) {
                        requestPromptlyPay(selectedData.getMoney(), "");
                    } else if (mTv_dredge_member.getText().toString().contains("升级")) {
                        requestPromptlyPay(selectedData.getMoney(), "2");
                    } else {
                        requestPromptlyPay(selectedData.getMoney(), "1");
                    }
                    popupWindow.dismiss();
                    break;
                case R.id.textViewChargeRules:
                    Intent intent = new Intent(getContext(), GoToActivity.class);
                    intent.putExtra(ConstantString.TITLE_NAME,"欧伶猪VIP会员服务协议");
                    String url= ConstantUrl.hosturl+"/detail/vipAgreement.html?URL="+ ConstantUrl.hosturl;
                    intent.putExtra(ConstantString.URL,url);
                    startActivity(intent);
                    break;
                case R.id.relativeLayout_pop_dismiss:
                    popupWindow.dismiss();
                    break;
            }
        }
    }

    //先生成订单
    private void requestPromptlyPay(String money, String renew) {
        if (TextUtils.isEmpty(mMyMemberInfo.getLevel())) {
            return;
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put("vset_id", selectedData.getId());
        stringMap.put("money", money);
        stringMap.put("num", StringConstants.STRING_1);
        stringMap.put("renew", renew);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.promptlyPayUrl, stringMap, new DredgeMemberPromptPayBean());
    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        if (c_codeBean.getUrl().equals(is_info_enough)) {
            setDialog(c_codeBean.getM());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestData();
    }

    private void setDialog(String message) {
        ArshowDialog.Builder builder = new ArshowDialog.Builder(mContext);
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        builder.setPositiveButton("去完善", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                intent = new Intent(MyMemberActivity.this, MyprofileActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setMessage(message);
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    //请求会员列表
    private void requestMemberData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.getListVip, map, new MemberListBean());
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof MemberListBean.BuyOrUpData) {
            MemberListBean.BuyOrUpData bean = (MemberListBean.BuyOrUpData) data;
            List<MemberListBean.BuyOrUpData> datas = adapterMember.getDatas();
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).setIs_selected(false);
            }
            bean.setIs_selected(true);
            selectedData = bean;
            adapterMember.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccess(String msg) {
        onInitData();
        popupWindow.dismiss();
        mTv_dredge_member.setEnabled(false);
    }

    @Override
    public void onFailure(PayResult payResult) {

    }
    public class MemberListAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_pop_buy_dgree_menber;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new MemberListHolder(itemView, context, this);
        }

        public class MemberListHolder extends RecyclerBaseHolder {
            private RelativeLayout relative_pop_item;
            private TextView textViewNameMember, textViewMoney, textViewTime;
            private ImageView imageViewSelect;

            public MemberListHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                relative_pop_item = itemView.findViewById(R.id.relative_pop_item);
                textViewNameMember = itemView.findViewById(R.id.textViewNameMember);
                textViewMoney = itemView.findViewById(R.id.textViewMoney);
                textViewTime = itemView.findViewById(R.id.textViewTime);
                imageViewSelect = itemView.findViewById(R.id.imageViewSelect);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof MemberListBean.BuyOrUpData) {
                    MemberListBean.BuyOrUpData bean = (MemberListBean.BuyOrUpData) mData;
                    textViewNameMember.setText(bean.getName());
                    textViewMoney.setText("¥"+bean.getMoney());
                    textViewTime.setText("享有"+bean.getNum()+"个法律事件咨询机会，文书模板任意下载，\n免费浏览资讯版块");
                    if (bean.isIs_selected()) {
                        imageViewSelect.setImageResource(R.mipmap.select2x);
                    } else {
                        imageViewSelect.setImageResource(R.mipmap.no_selected2x);
                    }
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
