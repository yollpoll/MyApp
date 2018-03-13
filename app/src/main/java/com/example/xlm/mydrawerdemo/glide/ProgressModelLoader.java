package com.example.xlm.mydrawerdemo.glide;

import android.os.Handler;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

/**
 * Created by 鹏祺 on 2018/3/5.
 */

public class ProgressModelLoader implements StreamModelLoader<String> {
    private Handler handler;

    public ProgressModelLoader(Handler handler) {
        this.handler = handler;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
        return new ProgressDataFetcher(model, handler);
    }
}
