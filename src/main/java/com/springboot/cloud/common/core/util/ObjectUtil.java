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

/**
 * 类型转换工具类
 */
public class ObjectUtil {

    /**
    map：{                                           obj:Person类：{
            "name":"小明",                                       "name":"小明",
            "sex":"男",                                          "sex":"男",
            "age":"18",                 ---->                    "age":"18",
            "phone":"123456"                                    "phone":"123456"
         }                                                        }

     **/
    public static Object map2Obj(Map<String,Object> map, Class<?> clz) throws Exception{
        //利用反射机制来调用空构造方法新建clz类的一个实例化对象
       Object obj = clz.newInstance();
       //获取obj对象中的所有定义了的属性字段
       Field[] declaredFields = obj.getClass().getDeclaredFields();
       for(Field field:declaredFields){
            //获取该字段前的修饰符
            int mod = field.getModifiers();
            //如果修饰符为static或final则不用理
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){continue;}
            //当字段被private修饰的时候如果accessible为false的话无法访问反射访问该字段
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
       }
       return obj;
    }

    /**
     obj:Person类：{                                           map：{
                    "name":"小明",                                       "name":"小明",
                    "sex":"男",                                          "sex":"男",
                    "age":"18",                 ---->                    "age":"18",
                    "phone":"123456"                                    "phone":"123456"
                    }                                               }
     **/
    public static Map<String,Object> obj2Map(Object obj) throws Exception{
       Map<String,Object> map = new HashMap<>();
       Field[] fields = obj.getClass().getDeclaredFields();
       for (Field field : fields) {
          int mod = field.getModifiers();
          if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
             continue;
          }
          field.setAccessible(true);
          //在obj中如果该字段对应的值是null的话，就不注入到map中
          if (field.get(obj) == null){
              continue;
          }
          //在obj中如果该字段的类型是date的话
          if(field.getType() == Date.class){
              //默认使用这个时间格式
              String pattern = "yyyy-MM-dd HH:mm:ss";
              //如果在obj对应的实体类中的该属性上有用注解JsonFormat来指定格式的话就会它的
              JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
              if (jsonFormat != null){
                  pattern = jsonFormat.pattern();
              }
              //利用pattern生成日期格式化器dateTimeFormatter
              DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
              //获取到obj中对应的Date类型的时间（虽然obj中本来的字段就是Date类型，但是通过field.get方法来获取到返回的默认是Object）
              Date date = (Date)field.get(obj);
              //Date转化成LocalDateTime（表示与时区无关的日期和时间信息，不直接对应时刻，需要通过时区转换）
              LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
              //LocalDateTime利用日期格式化器dateTimeFormatter转化成String
              String formatDate = dateTime.format(dateTimeFormatter);
              map.put(field.getName(),formatDate);
          //除了date的其他类型的字段都正常的put
          }else {
              map.put(field.getName(),field.get(obj));
          }
       }
       return map;
    }

    /**
     obj --> map --> json
     **/
    public static JSONObject obj2Json(Object obj) throws Exception{
       return new JSONObject(obj2Map(obj));
    }

    /**
     objList --> jsonList
     [obj --> map --> json]
     **/
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
