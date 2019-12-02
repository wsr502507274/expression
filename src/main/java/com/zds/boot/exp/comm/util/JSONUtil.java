

package com.zds.boot.exp.comm.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.List;

/* *
 *========================================
 * @方法说明 ： json工具类
 * @author : shihongwei
 * @创建时间：     2019/2/27 16:18
 *========================================
*/
@Slf4j
public class JSONUtil {
    private static final Gson gson = GsonUtils.GSONBUILDER.setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, clazz);
        } catch (JsonSyntaxException e) {
            log.error("gson.fromJson error", e);
        }
        return null;
    }
    public static <T> List<T>  fromJson2List(String jsonStr, Class<T> clazz) {
        try {
            return gson.fromJson(jsonStr, fromJson(jsonStr, new TypeToken<List<T>>(){}.getType()));
        } catch (JsonSyntaxException e) {
            log.error("gson.fromJson error", e);
        }
        return null;
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        try {
            return gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            log.error("gson.fromJson error", e);
        }
        return null;
    }

    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            log.error("gson.toJson error", e);
        }
        return null;
    }
}
