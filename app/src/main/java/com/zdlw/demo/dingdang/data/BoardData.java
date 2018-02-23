package com.zdlw.demo.dingdang.data;

import java.util.List;

/**
 * @author LinWei on 2017/8/24 15:25
 */
public class BoardData {
    public List<Data> data;
    public class Data{
        public String _id;
        public String cid;
        public String commentator;
        public String info;
        public String time;
    }
}
