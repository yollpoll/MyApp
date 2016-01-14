package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.bean.ChildArticle;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by xlm on 2016/1/12.
 */
public interface ChildArticleService  {
    @GET(Port.GET_CHILD_ARTICLE+"/page/{page}/id/{id}")
    Call<List<ChildArticle>> getChildArticle(@Path("page")String page,@Path("id") String id);
}
