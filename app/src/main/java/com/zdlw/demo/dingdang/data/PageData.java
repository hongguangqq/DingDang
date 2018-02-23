package com.zdlw.demo.dingdang.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LinWei on 2017/8/23 14:14
 */
public class PageData  {
    public List<Data> data;
    public class Data implements Serializable{
        public int _id;
        public String author;
        public int aid;
        public String course;
        public String time;
        public String msg;
        public int money;
        public int receive;
        public String receiver;
        public int rid;
        public String sms;
    }
}
