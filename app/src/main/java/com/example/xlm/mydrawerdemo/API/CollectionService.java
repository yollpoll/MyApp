package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.bean.CollectionBean;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by 鹏祺 on 2017/5/25.
 */

public interface CollectionService {
    @GET(Port.COLLECTION+"/page/{page}/uuid/{uuid}")
    Call<List<CollectionBean>> getCollection(@Path("page") int page,@Path("uuid") String uuid);

    @GET(Port.ADD_COLLECTION+"/uuid/{uuid}/tid/{tid}")
    Call<String> addCollection(@Path("uuid") String uuid,@Path("tid") String tid);

    @GET(Port.DEL_COLLECTION+"/uuid/{uuid}/tid/{tid}")
    Call<String> delCollection(@Path("uuid") String uuid,@Path("tid") String tid);
}
