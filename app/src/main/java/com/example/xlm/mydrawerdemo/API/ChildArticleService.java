package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.bean.ChildArticleList;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by 鹏祺 on 2016/1/1.
 */
public interface ChildArticleService {
    @GET(Port.GET_CHILDE_FORM+"/page/{page}/id/{id}")
    Call<List<ChildArticleList>> getArticleList(@Path("page") String page,@Path("id") String id);
}
