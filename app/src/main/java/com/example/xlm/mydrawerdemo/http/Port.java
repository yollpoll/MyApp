package com.example.xlm.mydrawerdemo.http;

import com.example.xlm.mydrawerdemo.base.MyApplication;

/**
 * Created by xlm on 2015/11/19.
 */
public class Port {
    public static final String MAIN_HEAD_URL = "http://adnmb.com";//显式的主url
    public static final String HEAD_URL = "http://adnmb1.com/";//隐性url暂时写死,应该动态获取
    public static final String BACKUP_URL = HEAD_URL + "Api/backupUrl";//重定向获取根url
    public static final String COVER = "http://cover.acfunwiki.org/cover.php";
    public static final String ANNOUNCEMENT = "http://cover.acfunwiki.org/nmb-notice.json";  //公告
    public static final String IMG_THUMB_URL = "/thumb/";
    public static final String IMG_URL = "/image/";
    //获取板块列表
    public static final String GET_ForumList = "Api/getForumList";
    //获取串
    public static final String GET_ARTICLE = "Api/showf";
    public static final String GET_CHILD_ARTICLE = "Api/thread";
    public static final String GET_COOKIE = "/Api/getCookie";


    //    public static final String NEW_THREAD = "Home/Forum/doPostThread.html";
//    public static final String REPLY_THREAD = "Home/Forum/doReplyThread.html";
    public static final String NEW_THREAD = HEAD_URL + "/Home/Forum/doPostThread.html";
    public static final String REPLY_THREAD = HEAD_URL + "/Home/Forum/doReplyThread.html";
    //查看订阅
    public static final String COLLECTION = HEAD_URL + "/Api/feed";
    public static final String ADD_COLLECTION = HEAD_URL + "/Api/addFeed";
    public static final String DEL_COLLECTION = HEAD_URL + "/Api/delFeed";
    //    public static final String COLLECTION = "Api/feed";
//    public static final String ADD_COLLECTION = "Api/addFeed";
//    public static final String DEL_COLLECTION = "Api/delFeed";
    //时间线
//    public static final String TIME_LINE = "Api/timeline";
    public static final String TIME_LINE = HEAD_URL + "/Api/timeline";


    public static String getThumbUrl() {
        return MyApplication.getInstance().getBackUpUrl() + IMG_THUMB_URL;
    }

    public static String getImg() {
        return MyApplication.getInstance().getBackUpUrl() + IMG_URL;
    }

}
