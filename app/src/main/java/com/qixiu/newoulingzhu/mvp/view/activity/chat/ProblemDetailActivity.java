package com.qixiu.newoulingzhu.mvp.view.activity.chat;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qixiu.newoulingzhu.beans.messge.ProblemDetailBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.adapter.problem.ProblemPictureAdapter;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.utils.ArshowDialogUtils;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.five_star.FiveStarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;

import static com.qixiu.newoulingzhu.constant.ConstantUrl.commentsUrl;


/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class ProblemDetailActivity extends TitleActivity implements OKHttpUIUpdataListener<BaseBean>, ArshowDialogUtils.ArshowDialogListener, OnRecyclerItemListener {
    @BindView(R.id.fiveStar_speciality_judy)
    FiveStarView fiveStar_speciality_judy;
    @BindView(R.id.fiveStar_serviceAttitude_judy)
    FiveStarView fiveStar_serviceAttitude_judy;


    private OKHttpRequestModel mOkHttpRequestModel;
    private View mBt_complete;
    private View mLl_status_one;
    private View mLl_status_two;
    private View mLl_status_three;
    private TextView mTv_time_one;
    private TextView mTv_content_one;
    private TextView mTv_time_two;
    private TextView mTv_content_two;
    private TextView mTv_time_three;
    private TextView mTv_content_three;
    private RecyclerView mRv_picture_list;
    private TextView mTv_picture_hint;
    private String mId;
    private TextView mTv_describe_content;
    private ProblemPictureAdapter mProblemPictureAdapter;
    private ZProgressHUD mZProgressHUD;
    int type;//评价在哪边
    private String lawyerId;

    @Override
    protected void onInitViewNew() {
        mTitleView.setTitle("咨询详情");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLl_status_one = findViewById(R.id.ll_status_one);
        mLl_status_two = findViewById(R.id.ll_status_two);
        mLl_status_three = findViewById(R.id.ll_status_three);

        mTv_time_one = (TextView) findViewById(R.id.tv_time_one);
        mTv_content_one = (TextView) findViewById(R.id.tv_content_one);
        mTv_time_two = (TextView) findViewById(R.id.tv_time_two);
        mTv_content_two = (TextView) findViewById(R.id.tv_content_two);
        mTv_time_three = (TextView) findViewById(R.id.tv_time_three);
        mTv_content_three = (TextView) findViewById(R.id.tv_content_three);
        mTv_describe_content = (TextView) findViewById(R.id.tv_describe_content);

        mRv_picture_list = (RecyclerView) findViewById(R.id.rv_picture_list);
        mProblemPictureAdapter = new ProblemPictureAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv_picture_list.setLayoutManager(linearLayoutManager);
        mRv_picture_list.setAdapter(mProblemPictureAdapter);
        mProblemPictureAdapter.setOnItemClickListener(this);
        mTv_picture_hint = (TextView) findViewById(R.id.tv_picture_hint);
        mBt_complete = findViewById(R.id.bt_complete);
        mBt_complete.setOnClickListener(this);
        mZProgressHUD = new ZProgressHUD(this);
        fiveStar_speciality_judy.setListenner(new FiveStarView.SelectedListenner() {
            @Override
            public void seleted(int num) {
                 type=1;
                judge("speciality",num);
            }
        });
        fiveStar_serviceAttitude_judy.setListenner(new FiveStarView.SelectedListenner() {
            @Override
            public void seleted(int num) {
                type=2;
                judge("attitude",num);
            }
        });
    }

    private void judge(String key, int num) {
        Map<String,String> map=new HashMap();
        map.put("uid", Preference.get(ConstantString.USERID,""));
        map.put("problem",mId);
        map.put("type",type+"");
        map.put("lawyer",lawyerId);
        map.put(key,num+"");
        mOkHttpRequestModel .okhHttpPost(commentsUrl,map,new BaseBean());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_complete:
                ArshowDialogUtils.showDialog(this, "关闭后咨询将结束,是否确认关闭问题?", this);

                break;

        }
    }

    @Override
    protected void onInitData() {
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        mId = getIntent().getStringExtra(IntentDataKeyConstant.ID);
        requestData();
    }

    private void requestComplete() {
        if (mZProgressHUD != null) {
            mZProgressHUD.show();
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put(IntentDataKeyConstant.ID, mId);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.ProblemDetialCompleteUrl, stringMap, new BaseBean());

    }

    private void requestData() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put(IntentDataKeyConstant.ID, mId);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.ProblemDetialUrl, stringMap, new ProblemDetailBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_consultdetail;
    }


    @Override
    public void onSuccess(BaseBean data, int i) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();

        }
        if (data instanceof ProblemDetailBean) {


            ProblemDetailBean problemDetailBean = (ProblemDetailBean) data;
            lawyerId = problemDetailBean.getO().getLawyer();
            ProblemDetailBean.ProblemDetailInfo problemDetailInfo = problemDetailBean.getO();
            if (StringConstants.STRING_2.equals(problemDetailInfo.getType())) {
                mLl_status_three.setVisibility(View.VISIBLE);
                mTv_time_three.setText(problemDetailInfo.getCtime());
                mBt_complete.setVisibility(View.GONE);
            }
            mTv_time_one.setText(problemDetailInfo.getAddtime());
            mTv_time_two.setText(problemDetailInfo.getAddtime());
            mTv_content_two.setText("律师 " + problemDetailInfo.getLawyer() + " 跟进处理");
            mTv_describe_content.setText(problemDetailInfo.getProblem());
            mProblemPictureAdapter.refreshData(problemDetailInfo.getImg());
            if (problemDetailInfo.getImg() != null && problemDetailInfo.getImg().size() > 4) {
                mTv_picture_hint.setVisibility(View.VISIBLE);
            }
            if(problemDetailBean.getO().getAttitude()!=0){
                fiveStar_serviceAttitude_judy.setEnabled(false);
            }
            if(problemDetailBean.getO().getSpeciality()!=0){
                fiveStar_speciality_judy.setEnabled(false);
            }
            fiveStar_serviceAttitude_judy.setStarNum(problemDetailBean.getO().getAttitude());
            fiveStar_speciality_judy.setStarNum(problemDetailBean.getO().getSpeciality());

        } else if(!data.getUrl().equals(commentsUrl)){
            setResult(IntentRequestCodeConstant.RESULTCODE_START_PROBLEMDETAIL);
            finish();
        }
        if(data.getUrl().equals(commentsUrl)){
            ToastUtil.toast(data.getM());
            if(type==1){
                fiveStar_speciality_judy.setEnabled(false);
            }
            if(type==2){
                fiveStar_serviceAttitude_judy.setEnabled(false);
            }
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismissWithFailure("网络异常");

        }
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismissWithFailure("提交失败");

        }
    }

    @Override
    public void onClickPositive(DialogInterface dialogInterface, int which) {
        requestComplete();
    }

    @Override
    public void onClickNegative(DialogInterface dialogInterface, int which) {

    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        ArrayList<String> strings = new ArrayList<>();
        strings.addAll(mProblemPictureAdapter.getDatas());
        PhotoPreview.builder().setPhotos(strings).setCurrentItem(mRv_picture_list.getChildLayoutPosition(v)).setShowDeleteButton(false).start(
                this);
    }
}
