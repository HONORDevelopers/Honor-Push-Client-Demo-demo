package com.honor.pushdemo;

import java.util.List;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hihonor.push.sdk.HonorPushCallback;
import com.hihonor.push.sdk.HonorPushClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DemoLog-MainActivity";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.showEt);
        initIntentData(getIntent());
    }

    /**
     * 校验是否支持荣耀Push
     *
     * @param view view
     */
    public void checkSupportHonorPush(View view) {
        boolean isSupport = HonorPushClient.getInstance().checkSupportHonorPush(getApplicationContext());
        show("checkSupportHonorPush result is : " + isSupport);
    }

    /**
     * 查询荣耀Push通知栏消息状态
     *
     * @param view view
     */
    public void queryPushStatus(View view) {
        HonorPushClient.getInstance().getNotificationCenterStatus(new HonorPushCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean pushStatus) {
                show("query pushStatus successfully , " + (pushStatus ? "on" : "off"));
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                show("query pushStatus failed, " + errorCode + " " + errorString);
            }
        });
    }

    /**
     * 打开荣耀Push通知栏消息推送
     *
     * @param view view
     */
    public void turnOnPush(View view) {
        HonorPushClient.getInstance().turnOnNotificationCenter(new HonorPushCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                show("turnOn push successfully.");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                show("turnOn push failed, " + errorCode + " " + errorString);
            }
        });
    }

    /**
     * 关闭荣耀Push通知栏消息推送
     *
     * @param view view
     */
    public void turnOffPush(View view) {
        HonorPushClient.getInstance().turnOffNotificationCenter(new HonorPushCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                show("turnOff push successfully.");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                show("turnOff push failed, " + errorCode + " " + errorString);
            }
        });
    }

    /**
     * 获取PushToken
     * 应用不应固定PushToken的长度，后续长度可能会变
     * 建议应用每次启动都去获取PushToken，如发现不一致，需要将新获取的PushToken上报到您的服务器。
     * 建议非常驻应用在启动后首个Activity的onCreate方法中调用getPushToken方法。
     * 常驻应用严禁定期频繁申请Token，如果一定要周期申请建议周期大于1天。
     *
     * @param view view
     */
    public void getPushToken(View view) {
        HonorPushClient.getInstance().getPushToken(new HonorPushCallback<String>() {
            @Override
            public void onSuccess(String pushToken) {
                //PushToken保存到您的服务器上
                show("get pushToken " + pushToken);
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                show("get pushToken failed, " + errorCode + ":" + errorString);
            }
        });
    }

    /**
     * 注销PushToken
     * 用户拒绝接受您应用的使用协议和隐私声明后，可以调用deletePushToken方法注销PushToken，注销成功后，客户端将不再接收到消息。
     * 如果需要关闭通知中心的通知栏消息，不建议您调用deletePushToken方法注销PushToken，建议您将通过turnOffPush设置Push通知栏消息为关闭状态。
     *
     * @param view view
     */
    public void deletePushToken(View view) {
        HonorPushClient.getInstance().deletePushToken(new HonorPushCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                show("delete pushToken successfully");
            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                show("delete pushToken failed, " + errorCode + " " + errorString);
            }
        });
    }

    /**
     * 生成点击消息动作的Intent
     *
     * @param view view
     */
    public void generateIntent(View view) {
        Intent intent = generateIntentByUri();
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);

        // 打印出的intentUri值就是设置到推送消息中intent字段的值
        Log.d(TAG, "intentUri = " + intentUri);
        editText.setText(intentUri);
    }

    /**
     * 校验点击消息动作的Intent
     * 校验不通过的intent消息会展示失败，回执状态码为40000015
     *
     * @param view view
     */
    public void checkIntent(View view) {
        try {
            String intentUri = String.valueOf(editText.getText());
            Intent intent = Intent.parseUri(intentUri, 0);
            // 需要添加android.intent.category.BROWSABLE
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            // 荣耀推送服务7.0.51版本之前不支持Component配置，若要兼容之前版本，校验时需把Component置空
            // intent.setComponent(null);
            intent.setSelector(null);
            List<ResolveInfo> resolveInfos = this.getPackageManager().queryIntentActivities(intent, 0);
            if (resolveInfos != null && resolveInfos.size() > 0) {
                for (ResolveInfo resolveInfo : resolveInfos) {
                    if (resolveInfo.activityInfo != null && this.getPackageName().equals(resolveInfo.activityInfo.packageName)) {
                        show("checkIntent result = " + true);
                        return;
                    }
                }
            }
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
        }
        show("checkIntent result = " + false);
    }

    /**
     * 生成intent方式一：通过uri拼接数据
     */
    private Intent generateIntentByUri() {
        // Scheme协议格式:[scheme]://[host]:[port]/[path]?[query] 需要根据跳转的activity配置填写
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("pushscheme://com.honor.push:8888/deeplink?text=abc&number=18"));

        // 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    /**
     * 生成intent方式二：通过putExtra拼接数据
     */
    private Intent generateIntentByExtra() {
        // Scheme协议格式:[scheme]://[host]:[port]/[path]?[query] 需要根据跳转的activity配置填写
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("pushscheme://com.honor.push:8888/deeplink?"));
        intent.putExtra("text", "abc");
        intent.putExtra("number", 18);

        // 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    /**
     * 解析通知栏消息intent携带数据
     *
     * @param intent intent
     */
    private void initIntentData(Intent intent) {
        if (null != intent) {
            try {
                Uri data = intent.getData();
                StringBuilder sb = new StringBuilder();
                if (data != null) {
                    String param1 = data.getQueryParameter("msg");
                    String param2 = data.getQueryParameter("content");
                    sb.append("msg: ").append(param1).append(" content: ").append(param2);
                    Log.i(TAG, "receive data from push, " + sb);
                }
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        String content = bundle.getString(key);
                        Log.i(TAG, "receive data from push, key = " + key + ", content = " + content);
                    }
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointer," + e);
            } catch (NumberFormatException e) {
                Log.e(TAG, "NumberFormatException," + e);
            } catch (UnsupportedOperationException e) {
                Log.e(TAG, "UnsupportedOperationException," + e);
            }
        } else {
            Log.i(TAG, "intent = null");
        }
    }

    private void show(String msg) {
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            Log.d(TAG, msg);
        });
    }
}