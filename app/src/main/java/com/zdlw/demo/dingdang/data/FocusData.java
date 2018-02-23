package com.zdlw.demo.dingdang.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LinWei on 2017/8/29 14:28
 */
public class FocusData {
    public List<Data> data;
    public static class Data implements Serializable{
        public int _id;
        public String name;
        public String pic;
        public String phone;
        public String introduction;
    }
}
