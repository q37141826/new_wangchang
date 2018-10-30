package com.qixiu.wigit.picker;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2017/10/16.
 */

public class TransDataBeanToListString {


    public static List<String> getProvice(List<AddressBean.OBean> list) {
        List<String> listString=new ArrayList<>();
        if(list.size()==0){
            return listString;
        }
        for (int i = 0; i <list.size() ; i++) {
            listString.add(list.get(i).getName());
        }
        return listString;
    }
    public static List<String> getCity(List<AddressBean.OBean.CitylistBean> list) {
        List<String> listString=new ArrayList<>();
        if(list.size()==0){
            return listString;
        }
        for (int i = 0; i <list.size() ; i++) {
            listString.add(list.get(i).getName());
        }
        return listString;
    }

    public static List<String> getArea(List<AddressBean.OBean.CitylistBean.ArealistBean> list) {
        List<String> listString=new ArrayList<>();
        if(list.size()==0){
            return listString;
        }
        for (int i = 0; i <list.size() ; i++) {
            listString.add(list.get(i).getName());
        }
        return listString;
    }

}
