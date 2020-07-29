package com.springboot.cloud.enums;

import java.util.List;
import java.util.Map;

public enum roleEnums {

    SUPER_ADMIN(0,"超级管理员",""),
    MEMBER(1,"人员",""),
    ADMIN(2,"管理员","");

    int code;
    String name;
    String description;

    roleEnums(int code,String name,String description){
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static String getNameByCode(int code){
        String result = "";
        for (roleEnums value : values()) {
            if (value.getCode() == code){
                result = value.getName();
                break;
            }
        }
        return result;
    }
}
