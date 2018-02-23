package com.zdlw.demo.dingdang.data;

import java.util.List;

/**
 * @author LinWei on 2017/8/25 14:13
 */
public class IssueData {
    public List<Data> data;
    public class Data{
       public int _id;
       public String author;
       public int age;
       public String course;
       public int money;
       public String msg;//上课简介
       public int receive;//接受状态
       public String receiver;//接受者名称
       public String time;//上课时间
       public String sms;//接受者留言
       public boolean isCheck;
    }
}
