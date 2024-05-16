package com.honor.pushdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class DeeplinkActivity extends Activity {
    private static final String TAG = "DemoLog-DeeplinkActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);
        initIntentDataByUri(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntentDataByUri(intent);
    }

    /**
     * 方式一：通过Uri解析通知栏消息intent携带数据
     *
     * @param intent intent
     */
    private void initIntentDataByUri(Intent intent) {
        if (intent != null) {
            int number = 0;
            String text;
            try {
                Uri uri = intent.getData();
                if (uri == null) {
                    Log.e(TAG, "getData null");
                    return;
                }
                String ageString = uri.getQueryParameter("number");
                text = uri.getQueryParameter("text");
                if (ageString != null) {
                    number = Integer.parseInt(ageString);
                }
                Log.i(TAG, "text " + text + ",number " + number);
                Toast.makeText(this, "text " + text + ",number " + number, Toast.LENGTH_SHORT).show();
            } catch (NullPointerException nullPointerException) {
                Log.e(TAG, "NullPointer," + nullPointerException);
            } catch (NumberFormatException numberFormatException) {
                Log.e(TAG, "NumberFormatException," + numberFormatException);
            } catch (UnsupportedOperationException unsupportedOperationException) {
                Log.e(TAG, "UnsupportedOperationException," + unsupportedOperationException);
            }
        }
    }

    /**
     * 方式二：通过getExtra解析通知栏消息intent携带数据
     *
     * @param intent intent
     */
    private void initIntentDataByExtra(Intent intent) {
        if (intent != null) {
            try {
                String text = intent.getStringExtra("text");
                int number = intent.getIntExtra("number", -1);
                Log.i(TAG, "text " + text + ",number " + number);
                Toast.makeText(this, "text " + text + ",number " + number, Toast.LENGTH_SHORT).show();
            } catch (Exception exception) {
                Log.e(TAG, "get intent extra error");
            }
        }
    }
}
