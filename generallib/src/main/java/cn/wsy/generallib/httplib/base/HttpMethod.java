package cn.wsy.generallib.httplib.base;

/**
 * 请求方式
 * Created by wsy on 16/7/14.
 */
public enum HttpMethod {

    //默认是键值
    GET("GET"),
    POST("POST"),
    GET_JSON("GET_JSON"),
    POST_JSON("POST_JSON");

    private final String value;

    private HttpMethod(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

}
