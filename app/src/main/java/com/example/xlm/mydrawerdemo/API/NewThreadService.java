package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.http.Port;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by 鹏祺 on 2017/5/25.
 */

public interface NewThreadService {
    /**
     * 带图片
     *
     * @param fid
     * @param name
     * @param title
     * @param email
     * @param content
     * @param water
     * @param file
     * @return
     */
    @Multipart
    @POST(Port.NEW_THREAD)
    Call<String> newThread(@Query("fid") String fid, @Query("name") String name, @Query("title") String title, @Query("email") String email,
                           @Query("content") String content, @Query("water") String water, @Part("image") RequestBody file);

//    "/fid/{fid}/name/{name}/title/{title}/email/{email}/content/{content}/water/{water}/"

    /**
     * 不带图片
     *
     * @param fid
     * @param name
     * @param title
     * @param email
     * @param content
     * @param water
     * @return
     */
    @Multipart
    @POST(Port.NEW_THREAD)
    Call<String> newThread(@Query("fid") String fid, @Query("name") String name, @Query("title") String title, @Query("email") String email,
                           @Query("content") String content, @Query("water") String water);

    @POST(Port.REPLY_THREAD + "/fid/{fid}/name/{name}/title/{title}/email/{email}/content/{content}/water/{water}/image/{image}")
    Call<String> replyThread();
}
