package com.example.xlm.mydrawerdemo.API;

import com.example.xlm.mydrawerdemo.bean.Announcement;
import com.example.xlm.mydrawerdemo.bean.Article;
import com.example.xlm.mydrawerdemo.http.Port;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by 鹏祺 on 2017/7/5.
 */

public interface AnnouncementService {
    @GET(Port.ANNOUNCEMENT)
    Call<Announcement> getAnnouncement();
}
