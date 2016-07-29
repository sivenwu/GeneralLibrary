package cn.wsy.generallib.httplib;

import com.loopj.android.http.RequestParams;

import java.lang.reflect.Field;

import cn.wsy.generallib.httplib.base.BaseEnetity;
import cn.wsy.generallib.httplib.utils.JsonParseUtil;


/**
 * 请求包装类
 * Created by wsy on 16/7/14.
 */
public class RequestEnetity<T> {

    private BaseEnetity enetity;
    private boolean isJSON = true;//是否请求是通过json数据,默认需要

    public RequestEnetity(BaseEnetity enetity) {
        this.enetity = enetity;
    }

    public RequestEnetity(BaseEnetity enetity, boolean isJSON) {
        this.enetity = enetity;
        this.isJSON = isJSON;
    }

    public T getRequestParam(){
        return (T) (isJSON? JsonParseUtil.modeToJson(this.enetity):switchToParams(this.enetity));
    }

    public String getRequestURL(){
        return this.enetity.getRuqestURL();
    }

    private RequestParams switchToParams(Object obj) {
        RequestParams params = new RequestParams();
        //解析数据
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                //私有成员必须设置这个属性
                field.setAccessible(true);

                if (field.getType() == String.class) {
                    // 属性值为null, 用空字符串取代
                    String value = ((field.get(obj) == null) ? "" : String
                            .valueOf(field.get(obj)));
                    params.put(field.getName(), value);
                }
                // int类型
                else if (field.getType() == int.class) {
                    int temp = field.getInt(obj);
                    String value = Integer.toString(temp);
                    params.put(field.getName(), value);
                }
                //float
                else if (field.getType() == float.class) {
                    float temp = field.getFloat(obj);
                    String value = Float.toString(temp);
                    params.put(field.getName(), value);
                }
                //double
                else if (field.getType() == double.class) {
                    double temp = field.getDouble(obj);
                    String value = Double.toString(temp);
                    params.put(field.getName(), value);
                }
                // boolean类型
                else if (field.getType() == boolean.class) {
                    boolean temp = field.getBoolean(obj);
                    String value = String.valueOf(temp);
                    params.put(field.getName(), String.valueOf(value));
                }
                // Object类型
                else {
                    Object fieldObject = field.get(obj);
                    if (fieldObject instanceof Object) {
                        // 如果对象中含有对象类型的变量 递归遍历
                        switchToParams(fieldObject);
                    } else {
                        continue;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }

}
