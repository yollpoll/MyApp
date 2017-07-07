package com.example.xlm.mydrawerdemo.retrofitService;

import com.example.xlm.mydrawerdemo.http.Port;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by 鹏祺 on 2017/5/24.
 */

public interface GetCookieService  {
    @GET(Port.GET_COOKIE)
    Call<String> getArticleList();
}
