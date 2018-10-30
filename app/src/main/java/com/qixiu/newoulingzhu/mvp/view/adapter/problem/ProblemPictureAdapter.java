package com.qixiu.newoulingzhu.mvp.view.adapter.problem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;


/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class ProblemPictureAdapter extends RecyclerBaseAdapter {


    @Override
    public int getLayoutId() {
        return R.layout.item_consultdetail;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        setTileWith(itemView, 4, 0, ArshowContextUtil.dp2px(10), 0, 0);
        return new ProblemPictureHolder(itemView, context, this);
    }

    public class ProblemPictureHolder extends RecyclerBaseHolder {


        private final ImageView mIv_consultdetail;

        public ProblemPictureHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            mIv_consultdetail = (ImageView) itemView.findViewById(R.id.iv_consultdetail);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof String) {
                String imgpath = (String) mData;
                Glide.with(itemView.getContext()).load(imgpath).skipMemoryCache(false).centerCrop().into(mIv_consultdetail);
            }

        }
    }
}
