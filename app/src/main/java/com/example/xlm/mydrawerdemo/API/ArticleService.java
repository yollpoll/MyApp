package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by xlm on 2015/11/24.
 */
public interface ArticleService {
    @GET(Port.GET_ARTICLE+"/page/{page}/id/{id}")
    Call<List<Article>> getArticleList(@Path("page") String page,@Path("id") String id);
}
