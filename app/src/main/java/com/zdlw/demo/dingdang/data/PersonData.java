package com.zdlw.demo.dingdang.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LinWei on 2017/11/7 14:57
 */
public class PersonData {
    public List<Data> data;
    public class Data implements Serializable{
        public String name;
        public String _id;
        public String pwd;
        public String pic;
        public String phone;
        public String school;
        public String subject;
        public String age;
        public String introduction;
    }
}
