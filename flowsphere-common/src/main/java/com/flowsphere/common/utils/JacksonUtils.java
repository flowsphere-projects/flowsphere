package com.flowsphere.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class JacksonUtils {

    private JacksonUtils() {
    }

    static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 对于原生数据类型的属性允许 JSON 字符串中的值为 null
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        // 禁止使用 int 代表 Enum 的 order() 来反序列化 Enum，非常危险
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        // 允许数组为单值
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        //可以用空对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        //是否考虑换成标准的使时间
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.setLocale(Locale.CHINA);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T toObj(byte[] json, Class<T> cls) {
        try {
            return toObj(new String(json, Charset.forName("UTF-8")), cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(byte[] json, Type cls) {
        try {
            return toObj(new String(json, Charset.forName("UTF-8")), cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(InputStream inputStream, Class<T> cls) {
        try {
            return mapper.readValue(inputStream, cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(byte[] json, TypeReference<T> typeReference) {
        try {
            return toObj(new String(json, Charset.forName("UTF-8")), typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T toObj(String json, Class<T> cls) {
        try {
            return mapper.readValue(json, cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T toObj(String json, Type type) {
        try {
            return mapper.readValue(json, mapper.constructType(type));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T toObj(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(InputStream inputStream, Type type) {
        try {
            return mapper.readValue(inputStream, mapper.constructType(type));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static JsonNode toObj(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toList(final String json, final Class<T> clazz) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
