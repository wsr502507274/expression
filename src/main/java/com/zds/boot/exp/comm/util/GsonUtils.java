package com.zds.boot.exp.comm.util;

import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * ==================================================
 * <p>
 * FileName: GsonUtils
 *
 * @author : shihongwei
 * @create 2018/8/30
 * @since 1.0.0
 * 〈功能〉：gson工具类
 * ==================================================
 */
public class GsonUtils {
	private static final Logger logger = LoggerFactory.getLogger(GsonUtils.class);

    public static final GsonBuilder GSONBUILDER;

    private GsonUtils() {
    }

    /* *
     *========================================
     * @方法说明 ： 将json转换为通用JSONObj
     * @author : shihongwei
     * @param json
     * @exception
     * @创建时间：     2018/12/19 20:26
     *========================================
     */
    public static JSONObject parsJsonToJsonObject(String json){
        return JSONObject.fromObject(json);
    }
    /* *
     *========================================
     * @方法说明 ： 将json转换为通用map 用于消除多次json序列化 转义问题
     * @author : shihongwei
     * @param json
     * @exception
     * @创建时间：     2018/12/19 20:26
     *========================================
     */
    public static Map<String, Object> parsJsonIgnoreESC(String json){
        JsonObject jsonObject =new JsonParser().parse(json).getAsJsonObject();

        return toMap(jsonObject);
    }

    /**
     * 将jsonObject对象转换为map对象
     *
     * @param json
     * @return
     */
    private static Map<String, Object> toMap(JsonObject json ) {
        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        entrySet.stream().forEach(jb -> {
            String key = jb.getKey();
            Object value = jb.getValue();
            if (value instanceof JsonArray) {
                map.put(key, toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                map.put(key, toMap((JsonObject) value));
            } else {
                map.put(key, value);
            }});
        return map;
    }

    /**
     * 将jsonArray对象转换为List集合
     *
     * @param json
     * @return
     */
    private static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<>();
        int length = json.size();
        for (int i = 0; i < length; i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            }
        }
        return list;
    }

    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        @Override
        public String read(JsonReader reader) {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return "";// 原先是返回Null，这里改为返回空字符串
                }
                return reader.nextString();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            return "";
        }

        @Override
        public void write(JsonWriter writer, String value) {
            try {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };

    /**
     * 自定义adapter，解决由于数据类型为Int,实际传过来的值为Float，导致解析出错的问题
     * 目前的解决方案为将所有Int类型当成Double解析，再强制转换为Int
     */
    public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            JsonToken jsonToken = in.peek();
            switch (jsonToken) {
                case NULL:
                    in.nextNull();
                    return null;
                case NUMBER:
                    return new LazilyParsedNumber(in.nextString());
                default:
                    throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(String.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };

    public static final TypeAdapter<BigDecimal> BIGDECIMAL = new TypeAdapter<BigDecimal>() {
        @Override
        public BigDecimal read(JsonReader in) throws IOException {

            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String stringValue = in.nextString();
                if (EmptyChecker.isEmpty(stringValue)) {
                    return null;
                }
                return new BigDecimal(stringValue);
            } catch (Exception e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, BigDecimal value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(String.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };

    public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String str = in.nextString();
                if (EmptyChecker.isEmpty(str)) {
                    return null;
                } else {
                    return Short.parseShort(str);
                }
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(String.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };

    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String str = in.nextString();
                if (EmptyChecker.isEmpty(str)) {
                    return null;
                } else {
                    return Integer.parseInt(str);
                }
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(String.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };
    public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String str = in.nextString();
                if (EmptyChecker.isEmpty(str)) {
                    return null;
                } else {
                    return Long.parseLong(str);
                }
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(String.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };
    public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String str = in.nextString();
            if (EmptyChecker.isEmpty(str)) {
                return null;
            } else {
                return Float.parseFloat(str);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(String.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };
    public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String str = in.nextString();
            if (EmptyChecker.isEmpty(str)) {
                return null;
            } else {
                return Double.parseDouble(str);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(String.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };


    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
        @Override
        public Boolean read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (EmptyChecker.isEmpty(str)) {
                    return null;
                }

                if ("1".equals(str)) {
                    return true;
                }

                if ("0".equals(str)) {
                    return false;
                }

                return Boolean.parseBoolean(str);

            }
            return in.nextBoolean();

        }

        @Override
        public void write(JsonWriter out, Boolean value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                if (value) {
                    out.value("1");
                    return;
                }

                out.value("0");
//                out.value(STRING.valueOf(value));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };

    public static final TypeAdapter<Date> DATE = new TypeAdapter<Date>() {
        @Override
        public Date read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;

            }
            String str = in.nextString();
            if (EmptyChecker.isEmpty(str)) {
                return null;
            }
            if (NumberUtils.isNumber(str)){
                return new Date(Long.parseLong(str));
            }
            return DateUtils.parseGeneralDate(str);
        }

        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            try {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(value.getTime());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    };

    static {
        // GSONBUILDER = new GsonBuilder().setPrettyPrinting()
        GSONBUILDER = new GsonBuilder();
        GSONBUILDER.registerTypeAdapter(String.class, STRING); // 所有String类型null替换为字符串“”
        GSONBUILDER.registerTypeAdapter(BigDecimal.class, BIGDECIMAL); // BigDecimal转String
        // 并判断空字符串返回null
        GSONBUILDER.registerTypeAdapter(Number.class, NUMBER); // Number转String
        // 并判断空字符串返回null
        GSONBUILDER.registerTypeAdapter(Short.class, SHORT); // Short转String
        // 并判断空字符串返回null
        GSONBUILDER.registerTypeAdapter(Integer.class, INTEGER); // Integer转String
        // 并判断空字符串返回null
        GSONBUILDER.registerTypeAdapter(Long.class, LONG); // Long转String
        // 并判断空字符串返回null
        GSONBUILDER.registerTypeAdapter(Float.class, FLOAT); // Float转String
        // 并判断空字符串返回null
        GSONBUILDER.registerTypeAdapter(Double.class, DOUBLE); // Double转String
        GSONBUILDER.registerTypeAdapter(Boolean.class, BOOLEAN); // Double转String

        GSONBUILDER.registerTypeAdapter(Date.class, DATE); // Date空处理



        // 并判断空字符串返回null
        // GSONBUILDER.setPrettyPrinting();// 添加格式化设置
    }
}
