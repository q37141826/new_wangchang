package com.qixiu.newoulingzhu.mvp.view.adapter.homne;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.List;

public class ImageUrlAdapter extends LoopPagerAdapter {
    List<Object> datas = new ArrayList<>();

    public ImageUrlAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public void refreshData(List<String> datas) {

        if (this.datas.size() > 0) {
            this.datas.clear();
        }
        this.datas.addAll(datas == null ? new ArrayList<String>() : datas);
        notifyDataSetChanged();
    }

    public void refreshData(ArrayList<Integer> resIds) {

        if (this.datas.size() > 0) {
            this.datas.clear();
        }
        this.datas.addAll(resIds == null ? new ArrayList<>() : resIds);
        notifyDataSetChanged();
    }

    @Override
    public View getView(ViewGroup container, int position) {

        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);

        view.setLayoutParams(new ViewGroup.LayoutParams(ArshowContextUtil.getWidthPixels() - 16, (ArshowContextUtil.getWidthPixels() - 16) * 3 / 4));
        if (datas.get(position) instanceof Integer) {
            Glide.with(container.getContext()).load((int) datas.get(position)).error(R.mipmap.banner2x).skipMemoryCache(false).into(view);
        } else if (datas.get(position) instanceof String) {
            Glide.with(container.getContext()).load((String) datas.get(position)).error(R.mipmap.banner2x).skipMemoryCache(false).into(view);
        }

        return view;
    }

    @Override
    public int getRealCount() {

        return datas == null ? 0 : datas.size();
    }
}