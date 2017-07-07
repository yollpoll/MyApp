package com.example.xlm.mydrawerdemo.retrofitService;

import com.example.xlm.mydrawerdemo.http.Port;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

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
    Call<ResponseBody> newThread(@Part("fid") RequestBody fid, @Part("name") RequestBody name, @Part("title") RequestBody title, @Part("email") RequestBody email,
                                 @Part("content") RequestBody content, @Part("water") RequestBody water, @Part("image\"; filename=\"a.jpg\"") RequestBody file);


    /**
     * 回复(帶圖片)
     *
     * @param resto
     * @param name
     * @param title
     * @param email
     * @param content
     * @param water
     * @return
     */
    @Multipart
    @POST(Port.REPLY_THREAD)
    Call<ResponseBody> replyThread(@Part("resto") RequestBody resto, @Part("content") RequestBody content, @Part("name") RequestBody name, @Part("title") RequestBody title,
                                   @Part("email") RequestBody email, @Part("water") RequestBody water, @Part("image\"; filename=\"a.jpg\"") RequestBody file);
}
