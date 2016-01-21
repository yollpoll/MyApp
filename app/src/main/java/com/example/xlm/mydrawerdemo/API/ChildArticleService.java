package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.bean.ChildArticle;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
<<<<<<< HEAD
 * Created by 鹏祺 on 2016/1/1.
 */
public interface ChildArticleService {
    @GET(Port.GET_CHILD_ARTICLE+"/page/{page}/id/{id}")
    Call<ChildArticle> getArticleList(@Path("page") int page,@Path("id") String id);
}
