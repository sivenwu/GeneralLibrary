package cn.wsy.generallib.httplib.base;

import android.app.Activity;
import android.os.Bundle;

import cn.wsy.generallib.httplib.interfaces.LoadingViewListener;


/**
 * 可以直接继承这个类,向外抛出加载接口
 * Created by wsy on 16/7/14.
 */
public class NetWorkActivity extends Activity implements LoadingViewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStartLoading(String message) {

    }

    @Override
    public void onFinshLoading() {

    }
}
