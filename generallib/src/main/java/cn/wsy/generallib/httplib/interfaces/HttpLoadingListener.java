package cn.wsy.generallib.httplib.interfaces;

/**
 * Created by wsy on 2016/2/24.
 */
public interface HttpLoadingListener {

    /**
     * HTTP刚刚开始请求的时候
     */
    public void onPreViewAction();

    /**
     * HTTP结束
     */
    public void onAfterViewAction();

}
