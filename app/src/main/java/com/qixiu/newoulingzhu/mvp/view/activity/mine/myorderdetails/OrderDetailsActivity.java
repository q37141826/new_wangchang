package com.qixiu.newoulingzhu.mvp.view.activity.mine.myorderdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.EaseConstant;
import com.qixiu.newoulingzhu.beans.messge.ProblemDetailBean;
import com.qixiu.newoulingzhu.beans.mine_bean.order.MyConsultingBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPreview;

public class OrderDetailsActivity extends TitleActivity implements OnRecyclerItemListener {


    @BindView(R.id.circular_head_order_detauls)
    CircleImageView circularHeadOrderDetauls;
    @BindView(R.id.textViewNameOrderDetails)
    TextView textViewNameOrderDetails;
    @BindView(R.id.textViewTimeOrderDetails)
    TextView textViewTimeOrderDetails;
    @BindView(R.id.relativeFinished)
    RelativeLayout relativeFinished;
    @BindView(R.id.textViewContent)
    TextView textViewContent;
    @BindView(R.id.recyclerViewImage)
    RecyclerView recyclerViewImage;
    @BindView(R.id.btnGotoCominucate)
    Button btnGotoCominucate;
    private Object datas;
    ImageAdapter adapter = new ImageAdapter();

    public static void start(Context context, MyConsultingBean.OBean.DataBean datas) {
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra(IntentDataKeyConstant.DATA, datas);
        context.startActivity(intent);
    }

    public static void start(Context context, ProblemDetailBean.ProblemDetailInfo datas) {
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra(IntentDataKeyConstant.DATA, datas);
        context.startActivity(intent);
    }

    @Override
    protected void onInitData() {
        datas = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        recyclerViewImage.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewImage.setAdapter(adapter);
        if (datas instanceof MyConsultingBean.OBean.DataBean) {
            MyConsultingBean.OBean.DataBean dataBean = (MyConsultingBean.OBean.DataBean) datas;
            List<String> list = removeBadData(dataBean.getImg());
            adapter.refreshData(list);
            adapter.setOnItemClickListener(this);
            textViewContent.setText(dataBean.getProblem());
            textViewNameOrderDetails.setText(dataBean.getUser_nicename());
            textViewTimeOrderDetails.setText(dataBean.getAddtime());
            mTitleView.setTitle(dataBean.getTitle());
            if (dataBean.getType().equals("1")) {
                relativeFinished.setVisibility(View.GONE);
                btnGotoCominucate.setVisibility(View.VISIBLE);
            } else {
                relativeFinished.setVisibility(View.VISIBLE);
                btnGotoCominucate.setVisibility(View.GONE);

            }
            Glide.with(getContext()).load(dataBean.getAvatar()).into(circularHeadOrderDetauls);
        }
        if (datas instanceof ProblemDetailBean.ProblemDetailInfo) {
            ProblemDetailBean.ProblemDetailInfo dataBean = (ProblemDetailBean.ProblemDetailInfo) datas;
            adapter.refreshData(dataBean.getImg());
            adapter.setOnItemClickListener(this);
            textViewContent.setText(dataBean.getProblem());
            textViewNameOrderDetails.setText(dataBean.getLawyer());
            textViewTimeOrderDetails.setText(dataBean.getAddtime());
            mTitleView.setTitle(dataBean.getQustiontype());
            if (dataBean.getType().equals("1")) {
                relativeFinished.setVisibility(View.GONE);
                btnGotoCominucate.setVisibility(View.VISIBLE);
            } else {
                relativeFinished.setVisibility(View.VISIBLE);
                btnGotoCominucate.setVisibility(View.GONE);

            }
            Glide.with(getContext()).load(dataBean.getAvatar()).into(circularHeadOrderDetauls);
        }


    }

    private List<String> removeBadData(ArrayList<String> img) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < img.size(); i++) {
            if (!TextUtils.isEmpty(img.get(i))) {
                list.add(img.get(i));
            }
        }
        return list;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void onInitViewNew() {

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (datas instanceof MyConsultingBean.OBean.DataBean) {
            MyConsultingBean.OBean.DataBean bean = (MyConsultingBean.OBean.DataBean) datas;
            PhotoPreview.builder().setPhotos(bean.getImg()).setShowDeleteButton(false).setCurrentItem(recyclerViewImage.getChildAdapterPosition(v)).start(
                    this);
        }
        if (datas instanceof ProblemDetailBean.ProblemDetailInfo) {
            ProblemDetailBean.ProblemDetailInfo dataBean = (ProblemDetailBean.ProblemDetailInfo) datas;
            PhotoPreview.builder().setPhotos(dataBean.getImg()).setShowDeleteButton(false).setCurrentItem(recyclerViewImage.getChildAdapterPosition(v)).start(
                    this);
        }

    }

    @OnClick(R.id.btnGotoCominucate)
    public void onViewClicked() {
        if (datas instanceof MyConsultingBean.OBean.DataBean) {
            MyConsultingBean.OBean.DataBean bean = (MyConsultingBean.OBean.DataBean) datas;
            Preference.put(ConstantString.OTHER_HEAD, bean.getAvatar());
            Preference.put(ConstantString.OTHER_NAME, bean.getUser_nicename());
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.PRO_ID, bean.getId());
            bundle.putString(EaseConstant.TOCHAT_NAME, bean.getUser_nicename());
            bundle.putString(EaseConstant.PROBLEM, bean.getProblem());
            bundle.putString(EaseConstant.TYPE, "1");
            if (Preference.getBoolean(ConstantString.IS_GROUP)) {
                HyEngine.startConversationGroup(this, bean.getGroup_id(), bundle, ChatActivity.class);
            } else {
                HyEngine.startConversationSingle(this, "ls" + bean.getLawyer(), bundle, ChatActivity.class);
            }
        }
        if (datas instanceof ProblemDetailBean.ProblemDetailInfo) {
            ProblemDetailBean.ProblemDetailInfo bean = (ProblemDetailBean.ProblemDetailInfo) datas;
            Preference.put(ConstantString.OTHER_HEAD, bean.getAvatar());
            Preference.put(ConstantString.OTHER_NAME, bean.getLawyer());
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.PRO_ID, bean.getId());
            bundle.putString(EaseConstant.TOCHAT_NAME, bean.getLawyer());
            bundle.putString(EaseConstant.PROBLEM, bean.getProblem());
            bundle.putString(EaseConstant.TYPE, "1");
            if (Preference.getBoolean(ConstantString.IS_GROUP)) {
                HyEngine.startConversationGroup(this, bean.getGroup_id(), bundle, ChatActivity.class);
            } else {
                HyEngine.startConversationSingle(this, "ls" + bean.getLawyer(), bundle, ChatActivity.class);
            }
        }

    }

    public class ImageAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_image;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new ImageHoder(itemView, context, this);
        }

        public class ImageHoder extends RecyclerBaseHolder {
            ImageView imageViewItem;

            public ImageHoder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                imageViewItem = itemView.findViewById(R.id.imageViewItem);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof String) {
                    Glide.with(mContext).load((String) mData).into(imageViewItem);
                }
            }
        }
    }
}
