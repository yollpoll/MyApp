package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.model.gitmodel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by xlm on 2015/11/19.
 */
public interface gitapi {
    @GET("/users/{user}")
    public void getFeed(@Path("user") String user, Callback<gitmodel> response);
}
