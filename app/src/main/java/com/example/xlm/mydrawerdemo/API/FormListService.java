package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.bean.Form;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by xlm on 2015/11/24.
 */
public interface FormListService {
    @GET(Port.GET_ForumList)
    Call<List<Form>> getFormList();
}
