package com.minhao.nov.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {


    private static ObjectMapper objectMapper=new ObjectMapper();




    static {
        //对象所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);


        //取消默认转换timestamps形似
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);



        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);


        //所有日期格式统一
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));


        //反序列化开始

        //忽略在json中存在 但在java对象中不存在
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);






    }







    public static <T> String obj2String(T obj){

        if (obj==null){
            return null;
        }

        try {
            return obj instanceof String ? (String)obj:objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse error",e);
            return null;
        }


    }




    //返回格式化好的json字符串
    public static <T> String obj2StringPretty(T obj){

        if (obj==null){
            return null;
        }

        try {
            return obj instanceof String ? (String)obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse error",e);
            return null;
        }


    }




    public static <T> T String2Obj(String str,Class<T> clazz){

        if (StringUtils.isEmpty(str)||clazz==null){
            return null;
        }

        try {
            return clazz.equals(String.class)?(T)str:objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            log.warn("Parse error",e);
            return null;
        }


    }




    public static <T> T string2Obj(String str, TypeReference<T> typeReference){

        if (StringUtils.isEmpty(str)||typeReference==null){
            return null;
        }

        try {
            return (T) (typeReference.getType().equals(String.class)?str:objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            log.warn("Parse error",e);
            return null;
        }

    }




    public static <T> T string2Obj(String str, Class<?> collectionClass,Class<?>...elemtClasses){

        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(collectionClass,elemtClasses);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse error",e);
            return null;
        }

    }


























}
