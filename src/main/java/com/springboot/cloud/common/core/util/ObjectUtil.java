package com.springboot.cloud.common.core.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ObjectUtil {
    public static Object map2Obj(Map<String,Object> map, Class<?> clz) throws Exception{
       Object obj = clz.newInstance();
       Field[] declaredFields = obj.getClass().getDeclaredFields();
       for(Field field:declaredFields){
       int mod = field.getModifiers();
       if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
       continue;
       }
       field.setAccessible(true);
       field.set(obj, map.get(field.getName()));
       }
       return obj;
    }

    public static Map<String,Object> obj2Map(Object obj) throws Exception{
       Map<String,Object> map = new HashMap<>();
       Field[] fields = obj.getClass().getDeclaredFields();
       for (Field field : fields) {
          int mod = field.getModifiers();
          if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
             continue;
          }
          field.setAccessible(true);
          if (field.get(obj) == null){
              continue;
          }
          if(field.getType() == Date.class){
              String pattern = "yyyy-MM-dd HH:mm:ss";
              JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
              if (jsonFormat != null){
                  pattern = jsonFormat.pattern();
              }
              DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
              Date date = (Date)field.get(obj);
              LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
              String formatDate = dateTime.format(dateTimeFormatter);
              map.put(field.getName(),formatDate);
          }else {
              map.put(field.getName(),field.get(obj));
          }
       }
       return map;
    }

    public static JSONObject obj2Json(Object obj) throws Exception{
       return new JSONObject(obj2Map(obj));
    }

    public static List<JSONObject> obj2JsonList(List<?> objList){
        List<JSONObject> list = new ArrayList<>();
        try {
            for (Object o : objList) {
                list.add(obj2Json(o));
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("obj to json error -> " + e.getMessage());
        }

        return list;
    }

    /**
     * map to object 类型转换
    * @author llc
    * @Date 2019/11/14
    **/
    public static Object map2ObjAndCast(Map<String,Object> map, Class<?> clz) throws Exception{
        Object obj = clz.newInstance();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for(Field field:declaredFields){
            if (!map.containsKey(field.getName())){
                continue;
            }
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            Class clzz = field.getType();
            if (clzz == long.class || clzz == Long.class){
                field.set(obj, Long.parseLong(map.get(field.getName()).toString()));
            }else if (clzz == int.class || clzz == Integer.class){
                field.set(obj, Integer.parseInt(map.get(field.getName()).toString()));
            }else if (clzz == Date.class){
                DateTimeFormat dtf = field.getAnnotation(DateTimeFormat.class);
                String pattern = "yyyy-MM-dd";
                if (dtf != null){
                    pattern = dtf.pattern();
                }
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                field.set(obj,sdf.parse(map.get(field.getName()).toString()));
            }else if(clzz == BigDecimal.class) {
                field.set(obj,new BigDecimal(map.get(field.getName()).toString()));
            } else {
                field.set(obj, map.get(field.getName()));
            }
        }
        return obj;
    }
}
