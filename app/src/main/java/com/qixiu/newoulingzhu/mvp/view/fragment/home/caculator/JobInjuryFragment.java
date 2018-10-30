package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopAddress;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopInjury;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.InjuryResultActivity;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.MyEditTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by my on 2018/9/3.
 */

public class JobInjuryFragment extends BaseFragment {

    @BindView(R.id.textViewSelectAddress)
    TextView textViewSelectAddress;
    @BindView(R.id.textViewInjureLevel)
    TextView textViewInjureLevel;
    @BindView(R.id.editextWages)
    MyEditTextView editextWages;
    @BindView(R.id.btnCalculation)
    Button btnCalculation;
    @BindView(R.id.btnReset)
    Button btnReset;
    Unbinder unbinder;
    private PopAddress popAddress;
    private String addressId;
    private PopInjury popInjury;
    private String injuryId;
    private String address, injury, money;

    @Override
    protected void onInitData() {
        popAddress = new PopAddress(getContext(), new PopAddress.OnItemClickListenner() {
            @Override
            public void onItemClick(PopAddress.ProvicesBean.OBean data) {
                textViewSelectAddress.setText(getContext().getString(R.string.please_select_address) + "        " + data.getProvince());
                addressId = data.getId();
                address = data.getProvince();
            }
        });
        popInjury = new PopInjury(getContext(), new PopInjury.OnItemClickListenner() {
            @Override
            public void onItemClick(PopInjury.InjuryBean.OBean data) {
                textViewInjureLevel.setText(getContext().getString(R.string.please_select_level) + "        " + data.getName());
                injuryId = data.getType();
                injury = data.getName();
            }
        });
    }

    @Override
    protected void onInitView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.item_fragment_injury;
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

    @OnClick({R.id.textViewSelectAddress, R.id.textViewInjureLevel, R.id.btnCalculation, R.id.btnReset})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.textViewSelectAddress:
                popAddress.requstPop();
                break;
            case R.id.textViewInjureLevel:
                popInjury.requstPop();
                break;
            case R.id.btnCalculation:
                money = editextWages.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    ToastUtil.toast("请选择地区");
                    return;
                }
                if (TextUtils.isEmpty(injury)) {
                    ToastUtil.toast("请选择伤残等级");
                    return;
                }
                if (TextUtils.isEmpty(money)) {
                    ToastUtil.toast("请填写薪资");
                    return;
                }
                InjuryResultActivity.start(getContext(), new DataBean(injury, money, address, injuryId, addressId));
                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }


    public void reset() {
        try {
            address = "";
            injury = "";
            addressId = "";
            injuryId = "";
            textViewInjureLevel.setText(R.string.please_select_level);
            textViewSelectAddress.setText(R.string.please_select_address);
            editextWages.setText("");
        } catch (Exception e) {
        }
    }

    public static class DataBean implements Parcelable {
        String injury;
        String money;
        String address;
        String injuryId;
        String addressId;

        public String getInjuryId() {
            return injuryId;
        }

        public String getAddressId() {
            return addressId;
        }

        public DataBean(String injury, String money, String address, String injuryId, String addressId) {
            this.injury = injury;
            this.money = money;
            this.address = address;
            this.injuryId = injuryId;
            this.addressId = addressId;
        }

        public void setInjuryId(String injuryId) {
            this.injuryId = injuryId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getInjury() {
            return injury;
        }

        public void setInjury(String injury) {
            this.injury = injury;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public DataBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.injury);
            dest.writeString(this.money);
            dest.writeString(this.address);
            dest.writeString(this.injuryId);
            dest.writeString(this.addressId);
        }

        protected DataBean(Parcel in) {
            this.injury = in.readString();
            this.money = in.readString();
            this.address = in.readString();
            this.injuryId = in.readString();
            this.addressId = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
