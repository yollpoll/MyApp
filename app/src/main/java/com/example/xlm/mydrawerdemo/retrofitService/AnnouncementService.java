package com.example.xlm.mydrawerdemo.retrofitService;

import com.example.xlm.mydrawerdemo.bean.Announcement;
import com.example.xlm.mydrawerdemo.http.Port;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by 鹏祺 on 2017/7/5.
 */

public interface AnnouncementService {
    @GET(Port.ANNOUNCEMENT)
    Call<Announcement> getAnnouncement();
}
