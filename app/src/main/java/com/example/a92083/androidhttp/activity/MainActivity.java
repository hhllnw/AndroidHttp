package com.example.a92083.androidhttp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.a92083.androidhttp.R;
import com.inthub.baselibrary.common.Logger;
import com.inthub.baselibrary.http.Request;
import com.inthub.baselibrary.http.callback.StringCallBack;
import com.inthub.baselibrary.http.err.AppException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       getTest();
    }

    /**
     * post请求
     */
    private void getTest(){

        String url = "http://192.168.0.189/data.xml";
        new Request.Builder()
                .with(this)
                .url(url)
                .tag(toString())
                .callBack(new StringCallBack() {
                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onFailure(AppException e) {
                           e.printStackTrace();
                    }
                }).build();
    }
}
