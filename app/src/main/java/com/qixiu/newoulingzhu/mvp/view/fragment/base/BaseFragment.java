package com.qixiu.newoulingzhu.mvp.view.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qixiu.newoulingzhu.mvp.view.activity.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 高玉恒 on 2017/4/11 0011.
 */

public abstract class BaseFragment extends Fragment implements AccessDataFragmentInterface {
    protected BaseActivity mContext;
    View rootView = null;
    protected static final int REQUEST_CODE = 100;
    public int windowHeight, windowWith;
    protected Bundle mSavedInstanceState;
    protected Bundle data;
    public Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(mSavedInstanceState = savedInstanceState);
        mContext = (BaseActivity) this.getActivity();
    }

    @Override
    public void setBundle(Bundle bundle) {
        this.data = bundle;
    }

    @Override
    public Bundle getBundle() {
        return data;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getLayoutResource() <= 0) {
            rootView = getLayoutView();
            if (rootView == null) {
                //throw new RuntimeException("您的View都没有，拿什么显示？");
                Toast.makeText(getActivity(), "请添加当前" + getClass().getSimpleName() + "的布局", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                rootView = View.inflate(getContext(), getLayoutResource(), null);
            }catch (Exception e){
            }
        }
        ButterKnife.bind(this, rootView);//绑定framgent
        onInitView(rootView);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics mtr = new DisplayMetrics();
        defaultDisplay.getMetrics(mtr);
        windowHeight = mtr.heightPixels;
        windowWith = mtr.widthPixels;
        unbinder = ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitData();

    }

    public final <E extends View> E findViewById(int id) {
        return (E) rootView.findViewById(id);
    }

    protected abstract void onInitData();

    protected abstract void onInitView(View view);

    protected abstract int getLayoutResource();

    //该方法可以考虑重写
    protected View getLayoutView() {
        return null;
    }

    /**
     * 添加tag
     *
     * @param fragment
     * @param resId
     * @param tag
     */
    public void switchFragment(Fragment fragment, int resId, String tag) {
        switchFragment(fragment, resId, null, true, false, tag);
    }

    public void switchFragment(Fragment fragment, int resId, String tag, Bundle bundle) {
        switchFragment(fragment, resId, bundle, true, false, tag);
    }

    /**
     * 切换Fragment，与上面的方法不同的是判断是否需要添加堆栈中
     *
     * @param fragment
     * @param resId
     * @param arguments
     * @param isBack
     */
    public void switchFragment(Fragment fragment, int resId, Bundle arguments, boolean isBack) {
        switchFragment(fragment, resId, arguments, isBack, false);
    }

    /**
     * @param fragment  要替换的Fragment
     * @param arguments 需要携带的参数
     * @param isBack    是否添加到回退栈
     * @param isHide    是否隐藏
     */
    public void switchFragment(Fragment fragment, int resId, Bundle arguments, boolean isBack,
                               boolean isHide) {
        switchFragment(fragment, resId, arguments, isBack, isHide, null);
    }

    public void switchFragment(Fragment fragment, int resId, Bundle arguments, boolean isBack,
                               String tag) {
        switchFragment(fragment, resId, arguments, isBack, false, tag);
    }

    public void switchFragment(Fragment fragment, int resId, Bundle arguments, boolean isBack,
                               boolean isHide, String tag) {
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);// 设置动画效果
        ft.replace(resId, fragment, tag);
        if (isBack)
            ft.addToBackStack(null);
        if (isHide)
            ft.hide(this);
        ft.commit();
    }

    protected void finish() {
        getActivity().finish();
    }

    //检查权限
    public boolean hasPermission(Context context, String... permission) {
        for (String permissiom : permission) {
            if (ActivityCompat.checkSelfPermission(context, permissiom) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //添加权限
    public void hasRequse(Activity activity, int code, String... permission) {
        ActivityCompat.requestPermissions(activity, permission, 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
