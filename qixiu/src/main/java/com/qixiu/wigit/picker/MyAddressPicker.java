package com.qixiu.wigit.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 地址选择器（包括省级、地级、县级）。
 * 地址数据见示例项目的“city.json”，来源于国家统计局官网（http://www.stats.gov.cn/tjsj/tjbz/xzqhdm）
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /12/15
 */
public class MyAddressPicker extends WheelPicker {
    private ArrayList<AddressBean.OBean> provinceList = new ArrayList<AddressBean.OBean>();
    private ArrayList<ArrayList<AddressBean.OBean.CitylistBean>> cityList = new ArrayList<ArrayList<AddressBean.OBean.CitylistBean>>();
    private ArrayList<ArrayList<ArrayList<AddressBean.OBean.CitylistBean.ArealistBean>>> countyList = new ArrayList<ArrayList<ArrayList<AddressBean.OBean.CitylistBean.ArealistBean>>>();
    private OnAddressPickListener onAddressPickListener;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private int selectedProvinceIndex = 0, selectedCityIndex = 0, selectedCountyIndex = 0;
    private boolean hideProvince = false;
    private boolean hideCounty = false;
    //id
    private String selectedProvinceId="",selectedCityId="",selectedConuntyId="";
    /**
     * Instantiates a new Address picker.
     *
     * @param activity the activity
     * @param data     the data
     */
    public MyAddressPicker(Activity activity, List<AddressBean.OBean> data) {
        super(activity);
        try {
            int provinceSize = data.size();
            //添加省
            for (int x = 0; x < provinceSize; x++) {
                AddressBean.OBean pro = data.get(x);
                provinceList.add(pro);
                ArrayList<AddressBean.OBean.CitylistBean> cities = (ArrayList<AddressBean.OBean.CitylistBean>) pro.getCitylist();
                ArrayList<AddressBean.OBean.CitylistBean> xCities = new ArrayList<AddressBean.OBean.CitylistBean>();
                ArrayList<ArrayList<AddressBean.OBean.CitylistBean.ArealistBean>> xCounties = new ArrayList<ArrayList<AddressBean.OBean.CitylistBean.ArealistBean>>();
                int citySize = cities.size();
                //添加地市
                for (int y = 0; y < citySize; y++) {
                    AddressBean.OBean.CitylistBean cit = cities.get(y);
                    xCities.add(cit);
                    ArrayList<AddressBean.OBean.CitylistBean.ArealistBean> counties = (ArrayList<AddressBean.OBean.CitylistBean.ArealistBean>) cit.getArealist();
                    ArrayList<AddressBean.OBean.CitylistBean.ArealistBean> yCounties = new ArrayList<AddressBean.OBean.CitylistBean.ArealistBean>();
                    int countySize = 0;
                    if (cit.getArealist() != null) {
                        countySize = counties.size();
                    }
                    //添加区县
                    if (countySize == 0) {
                        AddressBean.OBean.CitylistBean.ArealistBean bean = new AddressBean.OBean.CitylistBean.ArealistBean();
                        bean.setCode(cit.getCode());
                        bean.setName(cit.getName());
//                        yCounties.add(cit.getArea().get(0));
                        yCounties.add(bean);
                    } else {
                        for (int z = 0; z < countySize; z++) {
                            yCounties.add(counties.get(z));
                        }
                    }
                    xCounties.add(yCounties);
                }
                cityList.add(xCities);
                countyList.add(xCounties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets selected item.
     *
     * @param province the province
     * @param city     the city
     * @param county   the county
     */
    public void setSelectedItem(String province, String city, String county) {
        for (int i = 0; i < provinceList.size(); i++) {
            String pro = provinceList.get(i).getName();
            if (pro.contains(province)) {
                selectedProvinceIndex = i;
                LogUtils.debug("init select province: " + pro);
                break;
            }
        }
        ArrayList<AddressBean.OBean.CitylistBean> cities = cityList.get(selectedProvinceIndex);
        for (int j = 0; j < cities.size(); j++) {
            String cit = cities.get(j).getName();
            if (cit.contains(city)) {
                selectedCityIndex = j;
                LogUtils.debug("init select city: " + cit);
                break;
            }
        }
        ArrayList<AddressBean.OBean.CitylistBean.ArealistBean> counties = countyList.get(selectedProvinceIndex).get(selectedCityIndex);
        for (int k = 0; k < counties.size(); k++) {
            String cou = counties.get(k).getName();
            if (cou.contains(county)) {
                selectedCountyIndex = k;
                LogUtils.debug("init select county: " + cou);
                break;
            }
        }
        LogUtils.debug(String.format("init select index: %s-%s-%s", selectedProvinceIndex, selectedCityIndex, selectedCountyIndex));
    }

    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见示例中的“city2.json”
     *
     * @param hideProvince the hide province
     */
    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    /**
     * 隐藏县级行政区，只显示省级和市级。
     * 设置为true的话，hideProvince将强制为false
     * 数据源依然使用“city.json” 仅在逻辑上隐藏县级选择框。
     *
     * @param hideCounty the hide county
     */
    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    /**
     * Sets on address pick listener.
     *
     * @param listener the listener
     */
    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.onAddressPickListener = listener;
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        if (hideCounty) {
            hideProvince = false;
        }
        if (provinceList.size() == 0) {
            throw new IllegalArgumentException("please initial options at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        final WheelView provinceView = new WheelView(activity);
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        provinceView.setTextSize(textSize);
        provinceView.setTextColor(textColorNormal, textColorFocus);
        provinceView.setLineVisible(lineVisible);
        provinceView.setLineColor(lineColor);
        provinceView.setOffset(offset);
        layout.addView(provinceView);
        if (hideProvince) {
            provinceView.setVisibility(View.GONE);
        }
        final WheelView cityView = new WheelView(activity);
        cityView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        cityView.setTextSize(textSize);
        cityView.setTextColor(textColorNormal, textColorFocus);
        cityView.setLineVisible(lineVisible);
        cityView.setLineColor(lineColor);
        cityView.setOffset(offset);
        layout.addView(cityView);
        final WheelView countyView = new WheelView(activity);
        countyView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT));
        countyView.setTextSize(textSize);
        countyView.setTextColor(textColorNormal, textColorFocus);
        countyView.setLineVisible(lineVisible);
        countyView.setLineColor(lineColor);
        countyView.setOffset(offset);
        layout.addView(countyView);
        if (hideCounty) {
            countyView.setVisibility(View.GONE);
        }


        provinceView.setItems(TransDataBeanToListString.getProvice(provinceList), selectedProvinceIndex);
        provinceView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                try {
                    selectedProvince = item;
                    selectedProvinceIndex = selectedIndex;
                    selectedCountyIndex = 0;
                    selectedProvinceId=provinceList.get(selectedProvinceIndex).getId();
                    //根据省份获取地市
                    cityView.setItems(TransDataBeanToListString.getCity(cityList.get(selectedProvinceIndex)), isUserScroll ? 0 : selectedCityIndex);
                    //根据地市获取区县
//                countyView.setItems(countyList.get(selectedProvinceIndex).get(0), isUserScroll ? 0 : selectedCountyIndex);
                    countyView.setItems(TransDataBeanToListString.getArea(countyList.get(selectedProvinceIndex).get(0))
                            , isUserScroll ? 0 : selectedCountyIndex);
                } catch (Exception e) {
                }
            }
        });
        cityView.setItems(TransDataBeanToListString.getCity(cityList.get(selectedProvinceIndex)), selectedCityIndex);
        cityView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                try {
                    selectedCity = item;
                    selectedCityIndex = selectedIndex;
                    selectedCityId=provinceList.get(selectedProvinceIndex).getCitylist().get(selectedCityIndex).getCode();
                    //根据地市获取区县
                    countyView.setItems(TransDataBeanToListString.getArea(countyList.get(selectedProvinceIndex).get(selectedCityIndex)), isUserScroll ? 0 : selectedCountyIndex);
                } catch (Exception e) {
                }
            }
        });
        countyView.setItems(TransDataBeanToListString.getArea(countyList.get(selectedProvinceIndex).get(selectedCityIndex)), selectedCountyIndex);
        countyView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                try {
                    selectedCounty = item;
                    selectedCountyIndex = selectedIndex;
                    selectedConuntyId=provinceList.get(selectedProvinceIndex).getCitylist().get(selectedCityIndex).getArealist().get(selectedCountyIndex).getCode();
                } catch (Exception e) {
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onAddressPickListener != null) {
            if (hideCounty) {
                onAddressPickListener.onAddressPicked(selectedProvince, selectedCity, null,selectedProvinceId,selectedCityId,null);
            } else {
                onAddressPickListener.onAddressPicked(selectedProvince, selectedCity, selectedCounty,selectedProvinceId,selectedCityId,selectedConuntyId);
            }
        }
    }

    /**
     * The interface On address pick listener.
     */
    public interface OnAddressPickListener {

        /**
         * On address picked.
         *
         * @param province the province
         * @param city     the city
         * @param county   the county ，if {@hideCounty} is true，this is null
         */
        void onAddressPicked(String province, String city, String county, String provinceId, String cityId, String countyId);

    }

    /**
     * The type Area.
     */
    public abstract static class Area {
        /**
         * The Area id.
         */
        String id;
        /**
         * The Area name.
         */
        String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
/**
 * Gets area id.
 *
 * @return the area id
 */


        /**
         * Gets area name.
         *
         * @return the area name
         */


        @Override
        public String toString() {
            return "areaId=" + id + ",areaName=" + title;
        }

    }

    /**
     * The type Province.
     */
    public static class Province extends Area {
        /**
         * The Cities.
         */
        ArrayList<City> cities = new ArrayList<City>();

        /**
         * Gets cities.
         *
         * @return the cities
         */
        public ArrayList<City> getCities() {
            return cities;
        }

        /**
         * Sets cities.
         *
         * @param cities the cities
         */
        public void setCities(ArrayList<City> cities) {
            this.cities = cities;
        }

    }

    /**
     * The type City.
     */
    public static class City extends Area {
        private ArrayList<County> counties = new ArrayList<County>();

        /**
         * Gets counties.
         *
         * @return the counties
         */
        public ArrayList<County> getCounties() {
            return counties;
        }

        /**
         * Sets counties.
         *
         * @param counties the counties
         */
        public void setCounties(ArrayList<County> counties) {
            this.counties = counties;
        }

    }

    /**
     * The type County.
     */
    public static class County extends Area {
    }

}
