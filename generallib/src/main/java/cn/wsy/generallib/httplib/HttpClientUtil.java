package cn.wsy.generallib.httplib;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.wsy.generallib.httplib.base.BaseEnetity;
import cn.wsy.generallib.httplib.base.HttpMethod;
import cn.wsy.generallib.httplib.utils.NetWorkUtil;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * http + https加载工具
 * Created by wsy on 16/7/14.
 */
public class HttpClientUtil {

    private static final int TIME_OUT = 30 * 1000;
    private static final String CONTENT_TYPE = "application/json";
    private static final int THREAD_POOL_NUM = 5;

    private static AsyncHttpClient httpClient = null;
    private static ExecutorService mExecutorService = null;

//    public HttpClientUtil(Context context) {
//        this.mContext = context;
//        httpClient = new AsyncHttpClient(true,80,443);//https
//        httpClient.setTimeout(TIME_OUT);
//        httpClient.setThreadPool(createDefaThreadPool());
//    }

    private static AsyncHttpClient checkHttpClient() {
        if (httpClient == null) {
            httpClient = new AsyncHttpClient(true, 80, 443);//https
            httpClient.setTimeout(TIME_OUT);
            httpClient.setThreadPool(createDefaThreadPool());
        }
        return httpClient;
    }

    private static ExecutorService createDefaThreadPool() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(THREAD_POOL_NUM);
        }
        return mExecutorService;
    }

    public static void SEND(HttpMethod httpMethod, Context mContext, BaseEnetity entity, HttpRespondResult callback) {
        RequestEnetity requestEnetity = null;
        if (entity != null) {
            if (httpMethod.toString().equals(HttpMethod.GET_JSON.toString()) || httpMethod.toString().equals(HttpMethod.POST_JSON.toString())) {
                requestEnetity = new RequestEnetity(entity, true);
            }else{
                requestEnetity = new RequestEnetity(entity, false);
            }
            if (!NetWorkUtil.isNetworkAvailable(mContext)) {
                Log.i("wusy", "网络暂时无法连接");
                callback.onAfterViewAction();
                return;
            }
            if (httpMethod.toString().equals("POST")||httpMethod.toString().equals("POST_JSON")) {
                POST(mContext, requestEnetity.getRequestURL(), requestEnetity, callback);
            } else if (httpMethod.toString().equals("GET")||httpMethod.toString().equals("GET_JSON")) {
                GET(mContext, requestEnetity.getRequestURL(), requestEnetity, callback);
            }

        }else{
            Log.i("wusy", "请求参数为null");
        }

    }

    private static void POST(Context context, String url, RequestEnetity entity, final HttpRespondResult callback) {
        if (isJSON(entity)) {
            try {
                String json = fiterURLFromJSON((String) entity.getRequestParam());
                StringEntity stringEntity = new StringEntity(json);
                checkHttpClient().post(context, url, stringEntity, CONTENT_TYPE, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        callback.onFailure(throwable, responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        callback.onSuccess(responseString);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        callback.onPreViewAction();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        callback.onAfterViewAction();
                    }

                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            RequestParams params = (RequestParams) entity.getRequestParam();
            params = fiterURLFromRequestParams(params);
            checkHttpClient().post(url, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    callback.onFailure(throwable, responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    callback.onSuccess(responseString);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    callback.onPreViewAction();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    callback.onAfterViewAction();
                }

            });
        }
    }

    private static void GET(Context context, String url, RequestEnetity entity, final HttpRespondResult callback) {
        if (isJSON(entity)) {
            try {
                String json = fiterURLFromJSON((String) entity.getRequestParam());
                StringEntity stringEntity = new StringEntity(json);
                checkHttpClient().get(context, url, stringEntity, CONTENT_TYPE, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        callback.onFailure(throwable, responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        callback.onSuccess(responseString);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        callback.onPreViewAction();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        callback.onAfterViewAction();
                    }

                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            RequestParams params = (RequestParams) entity.getRequestParam();
            params = fiterURLFromRequestParams(params);
            checkHttpClient().get(url, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    callback.onFailure(throwable, responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    callback.onSuccess(responseString);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    callback.onPreViewAction();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    callback.onAfterViewAction();
                }

            });
        }
    }

    private static boolean isJSON(RequestEnetity entity) {
        Object obj = entity.getRequestParam();
        return obj instanceof String ? true : false;
    }

    private static String fiterURLFromJSON(String params) {
        JSONObject jsonObject = JSON.parseObject(params);
        if (jsonObject.containsKey("ruqestURL"))
            jsonObject.remove("ruqestURL");
        return jsonObject.toString();
    }

    private static RequestParams fiterURLFromRequestParams(RequestParams params) {
        if (params.has("ruqestURL"))
            params.remove("ruqestURL");
        return params;
    }

}
