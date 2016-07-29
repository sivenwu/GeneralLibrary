package cn.wsy.generallib.httplib.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * json model 转化工具
 * Created by wsy on 16/7/14.
 */
public class JsonParseUtil {

    private static final String LIST_TAG = "list";
    private static final String OBJ_TAG = "obj";

    /**
     * 实体转化为json
     * @param bean
     * @return
     */
    public static <T> String modeToJson(T bean) {
        return JSON.toJSONString(bean);
    }

    /**
     * json转换为实体
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToMode(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String json, Class<T> cls)  {
        JSONObject jsonObject = JSON.parseObject(json);
        return JSON.parseObject(jsonObject.getString(OBJ_TAG), cls);
    }

    /**
     * @param json
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String json, String key, Class<T> cls){
        JSONObject jsonObject = JSON.parseObject(json);
        return JSON.parseObject(jsonObject.getString(key), cls);
    }

    /**
     * json转换为List<T>
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonArrayToList(String json, Class<T> cls){
        JSONObject jsonObject = JSON.parseObject(json);
        return JSON.parseArray(jsonObject.getString(LIST_TAG), cls);
    }

    /**
     * @param json
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonArrayToList(String json, String key, Class<T> cls){
        JSONObject jsonObject = JSON.parseObject(json);
        return JSON.parseArray(jsonObject.getString(key), cls);
    }

}
