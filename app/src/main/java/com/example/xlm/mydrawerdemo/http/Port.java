package com.example.xlm.mydrawerdemo.http;

/**
 * Created by xlm on 2015/11/19.
 */
public class Port {
    public final static String IMG_THUMB_URL = "http://img6.nimingban.com/thumb/";
    public final static String IMG_URL = "http://img6.nimingban.com/image/";
    public final static String HEAD_URL = "https://h.nimingban.com/";
    public final static String COVER = "http://cover.acfunwiki.org/cover.php";
    public static final String ANNOUNCEMENT = "http://cover.acfunwiki.org/nmb-notice.json";  //公告
    //获取板块列表
    public final static String GET_ForumList = "Api/getForumList";
    //获取串
    public final static String GET_ARTICLE = "Api/showf";
    public final static String GET_CHILD_ARTICLE = "Api/thread";
    public final static String GET_COOKIE = "/Api/getCookie";
    public final static String NEW_THREAD = HEAD_URL + "Home/Forum/doPostThread.html";
    public final static String REPLY_THREAD = HEAD_URL + "Home/Forum/doReplyThread.html";

    //查看订阅
    public static final String COLLECTION = HEAD_URL + "Api/feed";
    public static final String ADD_COLLECTION = HEAD_URL + "Api/addFeed";
    public static final String DEL_COLLECTION = HEAD_URL + "Api/delFeed";

}
