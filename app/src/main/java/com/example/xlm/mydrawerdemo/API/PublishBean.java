package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.http.Port;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by 鹏祺 on 2017/5/25.
 */

public interface PublishBean {
    @GET(Port.NEW_OR_REPLY)
    Call<String> getArticleList();
}
