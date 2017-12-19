package com.example.xlm.mydrawerdemo.http;

/**
 * Created by xlm on 2015/11/19.
 */
public class Port {
    public static final String MAIN_HEAD_URL = "http://adnmb.com";//显式的主url
    public static final String IMG_THUMB_URL = "http://img6.nimingban.com/thumb/";
    public static final String IMG_URL = "http://img6.nimingban.com/image/";
    public static final String HEAD_URL = "http://adnmb1.com/Mobile/switchType";
    public static final String COVER = "http://cover.acfunwiki.org/cover.php";
    public static final String ANNOUNCEMENT = "http://cover.acfunwiki.org/nmb-notice.json";  //公告
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
}
