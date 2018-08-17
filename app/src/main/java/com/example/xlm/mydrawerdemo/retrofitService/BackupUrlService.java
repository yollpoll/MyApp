package com.example.xlm.mydrawerdemo.retrofitService;

import com.example.xlm.mydrawerdemo.bean.ImgCdn;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by 鹏祺 on 2018/1/26.
 */

public interface BackupUrlService {
    @GET(Port.BACKUP_URL)
    Call<List<String>> getBackUpUrl();

    @GET(Port.CDN_URL)
    Call<List<ImgCdn>> getCdns();
}
