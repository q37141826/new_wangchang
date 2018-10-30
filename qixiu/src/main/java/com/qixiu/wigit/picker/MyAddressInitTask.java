package com.qixiu.wigit.picker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取地址数据并显示地址选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @version 2015/12/15
 */
public class MyAddressInitTask extends AsyncTask<String, Void, ArrayList<AddressBean.OBean>> {
    private  List<AddressBean.OBean> datas;
    private Activity activity;
    private ProgressDialog dialog;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideCounty = false;

    /**
     * 初始化为不显示区县的模式
     *
     * @param activity
     * @param hideCounty is hide County
     */
    public MyAddressInitTask(Activity activity, boolean hideCounty,List<AddressBean.OBean> datas) {
        this.datas=datas;
        this.activity = activity;
        this.hideCounty = hideCounty;
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    public MyAddressInitTask(Activity activity,List<AddressBean.OBean> datas) {
        this.datas=datas;
        this.activity = activity;
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    @Override
    protected ArrayList<AddressBean.OBean> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<AddressBean.OBean> data = new ArrayList<AddressBean.OBean>();
        try {
            data.addAll(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<AddressBean.OBean> result) {
        dialog.dismiss();
        if (result.size() > 0) {
            MyAddressPicker picker = new MyAddressPicker(activity, result);
            picker.setHideCounty(hideCounty);
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(new MyAddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(String province, String city, String county, String provinceId, String cityId, String countyId) {
                    if (county == null) {
                        pickListene.setOnAddressPickListene(province, city, "全部");
//                        Toast.makeText(activity, province + city, Toast.LENGTH_LONG).show();
                    } else {
                        pickListene.setOnAddressPickListene(province, city, county);
//                        Toast.makeText(activity, province + city + county, Toast.LENGTH_LONG).show();
                    }
                }
            });
            picker.show();
        } else {
            Toast.makeText(activity, "数据初始化失败", Toast.LENGTH_SHORT).show();
        }
    }


    private PickListene pickListene;

    public void setOnAddressPickListene(PickListene pickListene) {
        this.pickListene = pickListene;
    }

    public interface PickListene {
        void setOnAddressPickListene(String province, String city, String count);
    }

}
