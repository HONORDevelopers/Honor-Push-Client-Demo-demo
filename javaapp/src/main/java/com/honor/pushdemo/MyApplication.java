package com.honor.pushdemo;

import android.app.Application;

import com.hihonor.push.sdk.HonorPushClient;

/**
 * 应用启动程序
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        boolean isSupport = HonorPushClient.getInstance().checkSupportHonorPush(this);
        if (isSupport) {
            /*
              除checkSupportHonorPush接口，其余接口调用前必须调用init接口完成SDK的初始化，否则会调用失败。
              initToken = true，调用初始化接口时SDK会同时进行异步请求PushToken。会触发HonorMessageService.onNewToken(String)回调。
              initToken = false，不会异步请求PushToken，需要应用主动请求获取PushToken。
             */
            HonorPushClient.getInstance().init(this, true);
        }
    }
}
