package com.zdlw.demo.dingdang.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**获取汉字的首字母
 * Created by Administrator on 2017/3/13.
 */
public class PinYinUtil {
    public static String getPinYin(String chinese){
        if(!TextUtils.isEmpty(chinese)){
            //字符串转化为字符数组，再将汉字逐个转化
            //用于设置拼音的大小写
            HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置转化的拼音，大写字母
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//设置音调
            char[] charArray=chinese.toCharArray();
            String pinyin="";
            //遍历每个字符，之后拼接起来
            for (int i=0;i<charArray.length;i++){
                //过滤空格
                if (Character.isWhitespace(charArray[i])){continue;}
                //需要判断是否是汉字
                //汉字占两个字节。一个字节-128~127
                if (charArray[i]>127){
                    //是汉字
                    try {
                        //获得汉字的首字符，由于多音字，所以用字符串数组
                        String[] pinyinArray=PinyinHelper.toHanyuPinyinStringArray(charArray[i],format);
                        if (pinyinArray!=null){
                            pinyin+=pinyinArray[0];//此处只取第一个拼音
                        }else {
                            //没有找到拼音，汉字有问题，忽略
                        }
                    } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                        badHanyuPinyinOutputFormatCombination.printStackTrace();
                    }
                }else {
                    //不是汉字,不需要转拼音，直接拼接
                    if ((45<charArray[0]&&charArray[0]<90)||(97<charArray[0]&&charArray[0]<122)){
                        pinyin+="#";
                        break;
                    }
                    pinyin+=charArray[i];
                }
            }
            return pinyin;
        }
        return null;
    }
}
