package com.honor.pushdemo.services;

import android.util.Log;
import android.widget.Toast;

import com.hihonor.push.sdk.HonorMessageService;
import com.hihonor.push.sdk.HonorPushDataMsg;

/**
 * 功能描述
 *
 * @since 2024-04-12
 */
public class MyHonorMsgService extends HonorMessageService {
    private static final String TAG = "DemoLog-MyHonorMsgService";

    /**
     * Token发生变化时，会以onNewToken方法返回
     * 当前只有init接口设置为true时会触发
     * @param pushToken pushToken
     */
    @Override
    public void onNewToken(String pushToken) {
        // 更新token至您的服务器
        Toast.makeText(this, "onNewToken: " + pushToken, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onNewToken: " + pushToken);
    }

    /**
     * 接收透传消息
     * @param msg 透传消息体
     */
    @Override
    public void onMessageReceived(HonorPushDataMsg msg) {
        if (msg == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }

        // 处理收到的透传消息
        Log.d(TAG, "Received message msgId: " + msg.getMsgId()
                + "\n content: " + msg.getData());
        Toast.makeText(this, "Received message msgId: " + msg.getMsgId()
                + "\n content: " + msg.getData(), Toast.LENGTH_SHORT).show();
    }
}
