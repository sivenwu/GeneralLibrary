package cn.wsy.generallib.httplib;

import android.content.Context;

import cn.wsy.generallib.R;
import cn.wsy.generallib.httplib.interfaces.HttpLoadingListener;
import cn.wsy.generallib.httplib.interfaces.LoadingViewListener;

/**
 * 请求回调类
 * Created by wsy on 16/7/14.
 */
public abstract class HttpRespondResult implements HttpLoadingListener {

    private Context mContext;
    private LoadingViewListener mLoadViewCallBack;

    private boolean isShowLoad = false;
    private String loadMessage;

    public HttpRespondResult() {

    }

    public HttpRespondResult(Context mContext) {
        this(mContext.getResources().getString(R.string.load_message),mContext);
    }

    public HttpRespondResult(String loadMessage, Context mContext) {
        this.loadMessage = loadMessage;
        this.mContext = mContext;
        isShowLoad = loadMessage.length()>0?true:false;

        try {
            mLoadViewCallBack = (LoadingViewListener) mContext;
        }catch (Exception e){
            mLoadViewCallBack = null;
        }
    }

    @Override
    public void onPreViewAction() {
        if (isShowLoad && (mLoadViewCallBack!=null)){
            mLoadViewCallBack.onStartLoading(this.loadMessage);
        }
    }

    @Override
    public void onAfterViewAction() {
        if (isShowLoad && (mLoadViewCallBack!=null)){
            mLoadViewCallBack.onFinshLoading();
        }
    }

    //向外抛出的两个方法
    public abstract void onSuccess(String content);
    public abstract void onFailure(Throwable error,String content);
}
