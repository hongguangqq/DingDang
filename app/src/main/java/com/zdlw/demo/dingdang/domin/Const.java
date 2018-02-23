package com.zdlw.demo.dingdang.domin;

import com.zdlw.demo.dingdang.utils.UIUtils;

/**
 * Created by Administrator on 2017/5/18.
 */
public class Const {
    //侧边栏Fragment的标识符
    public final static String FRAGMENT_LEFTMENU="FRAGMENT_LEFTMENU";
    //主界面Fragment的标识符
    public final static String FRAGMENT_CONTENT="FRAGMENT_CONTENT";
    //用户ID、用户名、密码、联系电话
    public static final String USER_ID="user_id";
    public static final String USER_NAME="user_name";
    public static final String USER_PASSWODR="user_pwd";
    public static final String USER_PHONE="user_phone";
    public static final String USER_INTRODUCTION="user_introduction";
    public static final String Extra_Detail="DetailData";
    public static final String Extra_MessageID="MessageID";
    public static boolean IsChangeState=false;//当item有用户预约后，变为true，此时回到发布板界面要刷新一次，之后回到false状态
    public static boolean isShowCheck;//true显示勾选项，false隐藏勾选项
    public static boolean isPlilsh;//是否发布了一条新信息
    public static boolean isModify;//是否修改了接受的委托信息中的留言、取消委托或者删除课程的发布
    public static boolean isCommu;//是否处于聊天状态；
    public static String ComuName="";//聊天对象
    /*--------------------------------------状态码-------------------------------------------*/
    public final static int STATE_Fail=0X01;
    public final static int STATE_Success=0X02;
    public final static int STATE_Refresh=0X03;
    public final static int STATE_LoadMore=0X04;
    public final static int STATE_Undo=0X05;
    public final static int STATE_First=0X06;
    public final static int STATE_Null=0X07;
    public final static int STATE_DELETE=0X08;
    public final static int STATE_MODIFY=0X09;
    public final static int STATE_SendSuccess=0X10;
    public final static int STATE_SendFail=0X11;
    /*---------------------------------------地址-------------------------------------------*/
//    public final static String URL_Path="http://192.168.3.7:80/dingdang/";
    public final static String URL_Path="http://192.168.3.73:80/dingdang/";
//    public final static String URL_Path="http://linweiandroid.oicp.io:31238/dingdang/";
    public final static String URL_Pic=URL_Path+"img/";
    public final static String URL_Login=URL_Path+"login.php";
    public final static String URL_Registere=URL_Path+"register.php";
    public final static String URL_Message=URL_Path+"message.php?";
    public final static String URL_Reserve=URL_Path+"reserve.php?";
    public final static String URL_CommentAll=URL_Path+"comment_all.php?";
    public final static String URL_CommentInsert=URL_Path+"comment_insert.php?";
    public final static String URL_Issue=URL_Path+"issue.php?";
    public final static String URL_IssuePublish=URL_Path+"issue_publish.php?";
    public final static String URL_IssueDelete=URL_Path+"issue_delete.php?";
    public final static String URL_Entrust=URL_Path+"entrust.php?";
    public final static String URL_EntrustModify=URL_Path+"entrust_modify.php?";
    public final static String URL_EntrustDelete=URL_Path+"entrust_delete.php?";
    public final static String URL_Focus=URL_Path+"focus.php?";
    public final static String URL_GetPersonData=URL_Path+"get_personData.php";
    public final static String URL_GetPersonDataByUser=URL_Path+"get_personDataByname.php";
    public final static String URL_UpdataPic=URL_Path+"updata_pic.php";
    public final static String URL_ReInfo=URL_Path+"reinfo.php";
    public final static String URL_SearchFous=URL_Path+"other.php";
    public final static String URL_ToFocus=URL_Path+"befllower.php";
    public final static String URL_SearchUser=URL_Path+"search.php";
    /*---------------------------------------广播-------------------------------------------*/
    public static final String ACTION1 = "com.zdlw.demo.dingdang.ModifyPage";
    public static final String ACTION2 = "com.zdlw.demo.dingdang.UpDataPic";
    public static final String ACTION3 = "com.zdlw.demo.dingdang.UpDataFocus";

    public static String getUserName(){
        return UIUtils.getSpString(USER_NAME);
    }

    public static int getUserID(){
        return UIUtils.getSpInt(USER_ID);
    }

}
